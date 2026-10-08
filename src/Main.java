import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read API key from Environment variable or prompt user
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            if (args.length > 0) {
                apiKey = args[0];
            } else {
                System.out.println("=== Gemini Prompt Window Optimizer ===");
                System.out.print("Enter your Gemini API Key (or press Enter to run demo mode): ");
                apiKey = scanner.nextLine().trim();
            }
        }

        // Running in standalone Demo Mode (used for CI build tests)
        if (apiKey.isEmpty()) {
            runAutomatedDemo();
            return;
        }

        System.out.println("\n[✓] Connected! Type your message (or 'exit' to quit).");
        System.out.print("Set Max Token Budget for dynamic context (e.g. 150): ");
        int budget = 150;
        try {
            budget = Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            budget = 150;
        }

        List<ContextChunk> history = new ArrayList<>();
        // Pinned system persona
        history.add(new ContextChunk(
            "System: You are an expert AI software engineering mentor. Give clear and concise answers.",
            ContextChunk.Type.PINNED
        ));

        while (true) {
            System.out.print("\nYou: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                break;
            }
            if (input.isEmpty()) continue;

            // Add user query
            history.add(new ContextChunk("User: " + input, ContextChunk.Type.DYNAMIC));

            // Run Algorithmic Optimization
            PromptOptimizer.Result optimized = PromptOptimizer.optimize(history, budget);
            System.out.printf("[Tokens: %d -> %d | Saved: %.1f%%]%n", 
                optimized.originalTokens, optimized.optimizedTokens, optimized.savings());

            System.out.println("Gemini is thinking...");
            try {
                String aiReply = GeminiClient.generate(apiKey, optimized.prompt);
                System.out.println("Gemini: " + aiReply);

                // Add AI answer to history for continuous conversation
                history.add(new ContextChunk("Assistant: " + aiReply, ContextChunk.Type.DYNAMIC));
            } catch (Exception e) {
                System.err.println("Request failed: " + e.getMessage());
            }
        }
    }

    private static void runAutomatedDemo() {
        System.out.println("[Notice] Running in CI/Demo mode (No API Key provided).");
        List<ContextChunk> conversation = List.of(
            new ContextChunk("System: You are an expert AI software architect.", ContextChunk.Type.PINNED),
            new ContextChunk("User: Explain merge sort step by step.", ContextChunk.Type.DYNAMIC),
            new ContextChunk("Assistant: Merge sort splits arrays in half recursively...", ContextChunk.Type.DYNAMIC),
            new ContextChunk("User: Can you write the Java code for it?", ContextChunk.Type.PINNED)
        );

        PromptOptimizer.Result res = PromptOptimizer.optimize(conversation, 45);
        System.out.printf("Tokens: %d -> %d (Saved: %.1f%%)%n", 
            res.originalTokens, res.optimizedTokens, res.savings());
        System.out.println("Demo finished successfully.");
    }
}
