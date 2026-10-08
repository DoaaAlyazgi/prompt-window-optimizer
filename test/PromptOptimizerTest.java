import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PromptOptimizerTest {

    public static void main(String[] args) {
        testAllFitBudget();
        testStrictBudgetRejection();
        testSlidingWindowSelection();
        testEmptyDynamicList();

        System.out.println("All unit tests passed successfully!");
    }

    private static void testAllFitBudget() {
        List<ContextChunk> chunks = Arrays.asList(
            new ContextChunk("System prompt", ContextChunk.Type.PINNED),
            new ContextChunk("First line", ContextChunk.Type.DYNAMIC),
            new ContextChunk("Second line", ContextChunk.Type.DYNAMIC)
        );

        PromptOptimizer.Result result = PromptOptimizer.optimize(chunks, 100);
        assert result.optimizedTokens == result.originalTokens : "All chunks should fit";
        assert result.savings() == 0.0 : "Savings should be 0 when everything fits";
    }

    private static void testStrictBudgetRejection() {
        List<ContextChunk> chunks = Collections.singletonList(
            new ContextChunk("Very critical long system instruction that exceeds budget", ContextChunk.Type.PINNED)
        );

        try {
            PromptOptimizer.optimize(chunks, 2);
            throw new AssertionError("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected behavior
        }
    }

    private static void testSlidingWindowSelection() {
        List<ContextChunk> chunks = Arrays.asList(
            new ContextChunk("Sys", ContextChunk.Type.PINNED),
            new ContextChunk("Chunk A", ContextChunk.Type.DYNAMIC),
            new ContextChunk("Chunk B", ContextChunk.Type.DYNAMIC),
            new ContextChunk("Chunk C", ContextChunk.Type.DYNAMIC)
        );

        // Budget allows only pinned + a subset of dynamic
        PromptOptimizer.Result result = PromptOptimizer.optimize(chunks, 8);
        assert result.optimizedTokens <= 8 : "Optimized tokens must stay within budget";
        assert result.optimizedTokens < result.originalTokens : "Should prune tokens";
    }

    private static void testEmptyDynamicList() {
        List<ContextChunk> chunks = Collections.singletonList(
            new ContextChunk("System instruction only", ContextChunk.Type.PINNED)
        );

        PromptOptimizer.Result result = PromptOptimizer.optimize(chunks, 20);
        assert result.optimizedTokens == result.originalTokens : "Should handle zero dynamic chunks";
    }
}
