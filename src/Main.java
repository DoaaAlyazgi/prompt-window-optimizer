import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<ContextChunk> conversation = Arrays.asList(
            new ContextChunk("System: You are an expert AI software architect.", ContextChunk.Type.PINNED),
            new ContextChunk("User: Explain merge sort step by step.", ContextChunk.Type.DYNAMIC),
            new ContextChunk("Assistant: Merge sort splits arrays in half recursively...", ContextChunk.Type.DYNAMIC),
            new ContextChunk("User: How does the merge step work exactly?", ContextChunk.Type.DYNAMIC),
            new ContextChunk("Assistant: It compares heads of both halves using two pointers.", ContextChunk.Type.DYNAMIC),
            new ContextChunk("User: Can you write the Java code for it?", ContextChunk.Type.PINNED)
        );

        PromptOptimizer.Result res = PromptOptimizer.optimize(conversation, 45);

        System.out.printf("Tokens: %d -> %d (Saved: %.1f%%)%n", 
            res.originalTokens, res.optimizedTokens, res.savings());
        System.out.println("\nGenerated Prompt:\n" + res.prompt);
    }
}
