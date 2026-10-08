import java.util.ArrayList;
import java.util.List;

public class PromptOptimizer {

    public static class Result {
        public final String prompt;
        public final int originalTokens;
        public final int optimizedTokens;

        public Result(String prompt, int originalTokens, int optimizedTokens) {
            this.prompt = prompt;
            this.originalTokens = originalTokens;
            this.optimizedTokens = optimizedTokens;
        }

        public double savings() {
            if (originalTokens == 0) return 0.0;
            return ((double) (originalTokens - optimizedTokens) / originalTokens) * 100.0;
        }
    }

    public static Result optimize(List<ContextChunk> chunks, int budget) {
        List<ContextChunk> pinned = new ArrayList<>();
        List<ContextChunk> dynamic = new ArrayList<>();

        int pinnedTokens = 0;
        for (ContextChunk c : chunks) {
            if (c.getType() == ContextChunk.Type.PINNED) {
                pinned.add(c);
                pinnedTokens += c.getTokens();
            } else {
                dynamic.add(c);
            }
        }

        int remainingBudget = budget - pinnedTokens;
        if (remainingBudget < 0) {
            throw new IllegalArgumentException("Budget too small for pinned content");
        }

        int m = dynamic.size();
        int[] pref = new int[m + 1];
        for (int i = 1; i <= m; i++) {
            pref[i] = pref[i - 1] + dynamic.get(i - 1).getTokens();
        }

        int bestL = 0, bestR = -1;
        int maxLen = 0;
        int left = 0;

        for (int right = 0; right < m; right++) {
            while (left <= right && (pref[right + 1] - pref[left]) > remainingBudget) {
                left++;
            }

            if (left <= right && (right - left + 1) > maxLen) {
                maxLen = right - left + 1;
                bestL = left;
                bestR = right;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (ContextChunk p : pinned) {
            sb.append(p.getContent()).append("\n\n");
        }

        if (bestR >= bestL) {
            for (int i = bestL; i <= bestR; i++) {
                sb.append(dynamic.get(i).getContent()).append("\n\n");
            }
        }

        int dynamicUsed = (bestR >= bestL) ? (pref[bestR + 1] - pref[bestL]) : 0;
        int totalUsed = pinnedTokens + dynamicUsed;
        int originalTotal = pref[m] + pinnedTokens;

        return new Result(sb.toString().trim(), originalTotal, totalUsed);
    }
}
