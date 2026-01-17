package costa.bruno.screenmatch.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsultaGemini {

    private static final String API_KEY = System.getProperty("GEMINIAPISCREENMATCH");
    private static final String URL_GEMINI = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

    public static String obterTraducao(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "Sinopse não disponível.";
        }

        ObjectMapper mapper = new ObjectMapper();

        String jsonBody = """
                {
                  "contents": [{
                    "parts":[{
                      "text": "traduza para o português o texto: %s"
                    }]
                  }]
                }
                """.formatted(texto.replace("\"", "\\\"")); // Escapa aspas no texto

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_GEMINI))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                GeminiResponse geminiResponse = mapper.readValue(response.body(), GeminiResponse.class);
                return geminiResponse.candidates[0].content.parts[0].text;
            } else {
                return "Erro na tradução: " + response.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erro ao consultar Gemini", e);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GeminiResponse {
        public Candidate[] candidates;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Candidate {
        public Content content;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Content {
        public Part[] parts;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Part {
        public String text;
    }
}
