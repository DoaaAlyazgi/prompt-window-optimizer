import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeminiClient {

    private static final String MODEL = "gemini-1.5-flash";

    public static String generate(String apiKey, String prompt) throws Exception {
        String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/" 
                + MODEL + ":generateContent?key=" + apiKey;

        // Escape special JSON characters
        String escapedPrompt = prompt
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");

        String jsonPayload = "{\"contents\":[{\"parts\":[{\"text\":\"" + escapedPrompt + "\"}]}]}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API Error (" + response.statusCode() + "): " + response.body());
        }

        return extractResponseText(response.body());
    }

    private static String extractResponseText(String responseBody) {
        // Lightweight extraction without external JSON dependencies
        String marker = "\"text\": \"";
        int index = responseBody.indexOf(marker);
        if (index == -1) {
            return "No text response found.";
        }
        int start = index + marker.length();
        int end = responseBody.indexOf("\"", start);
        if (end == -1) return responseBody.substring(start);

        return responseBody.substring(start, end)
                .replace("\\n", "\n")
                .replace("\\\"", "\"");
    }
}
