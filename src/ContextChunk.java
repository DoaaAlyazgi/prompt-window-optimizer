public class ContextChunk {
    public enum Type { PINNED, DYNAMIC }

    private final String content;
    private final int tokens;
    private final Type type;

    public ContextChunk(String content, Type type) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        this.content = content.trim();
        this.type = type;
        this.tokens = (int) Math.ceil(this.content.split("\\s+").length * 1.3);
    }

    public String getContent() { return content; }
    public int getTokens() { return tokens; }
    public Type getType() { return type; }
}
