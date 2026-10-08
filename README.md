# Prompt Window Optimizer

A high-performance Java engine that optimizes LLM prompt contexts using classic algorithmic paradigms (**Prefix Sum** and **Sliding Window**). It guarantees $O(1)$ token range queries and $O(N)$ optimal window selection while strictly preserving critical instructions (`PINNED` system prompts and user queries).

---

## The Problem

Sending large conversational histories or uncompressed documents to LLMs causes:
1. **High Token Costs:** Paying for repetitive or stale chat turns.
2. **High Latency:** Slower time-to-first-token.
3. **Attention Dilution:** Performance degradation ("Lost in the Middle") when irrelevant context floods the prompt.

Simple substring truncation often breaks prompts by stripping out core system instructions or critical user constraints.

---

## How It Works

Instead of naive slicing, this engine splits context into structured segments:

1. **Pinned Context (`PINNED`):** Essential system instructions and the latest user query. These are strictly protected from pruning.
2. **Dynamic Context (`DYNAMIC`):** Conversational history or supporting reference text.

### Algorithmic Pipeline:
- **1-based Prefix Sum:** Precomputes token distribution across dynamic chunks in $O(N)$ time, allowing subsequent range token calculations in $O(1)$.
- **Sliding Window (Two Pointers):** Evaluates the longest continuous segment of dynamic context that fits the remaining token budget in a single linear pass ($O(N)$).

$$\text{Available Budget} = \text{Total Budget} - \sum \text{Pinned Tokens}$$

---

## Complexity

| Operation | Time Complexity | Space Complexity |
| :--- | :--- | :--- |
| Chunk Preprocessing | $O(N)$ | $O(N)$ |
| Prefix Sum Build | $O(N)$ | $O(N)$ |
| Range Token Query | $O(1)$ | $O(1)$ |
| Optimal Window Search | $O(N)$ | $O(1)$ |

*Total runtime complexity is $O(N)$ with zero nested loops.*

---

## Quickstart

### Usage Example

```java
List<ContextChunk> context = Arrays.asList(
    new ContextChunk("System: You are an expert AI software architect.", ContextChunk.Type.PINNED),
    new ContextChunk("User: Explain merge sort step by step.", ContextChunk.Type.DYNAMIC),
    new ContextChunk("Assistant: Merge sort splits arrays in half recursively...", ContextChunk.Type.DYNAMIC),
    new ContextChunk("User: How does the merge step work exactly?", ContextChunk.Type.DYNAMIC),
    new ContextChunk("Assistant: It compares heads of both halves using two pointers.", ContextChunk.Type.DYNAMIC),
    new ContextChunk("User: Can you write the Java code for it?", ContextChunk.Type.PINNED)
);

// Target budget: 45 tokens
PromptOptimizer.Result result = PromptOptimizer.optimize(context, 45);

System.out.printf("Tokens: %d -> %d (Saved: %.1f%%)%n", 
    result.originalTokens, result.optimizedTokens, result.savings());
System.out.println(result.prompt);
