///*
package com.epam.training.gen.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Slf4j
@Service
public class ImagePromptService {

    @Value("${client-openai-image-deployment-name}")
    private String openAiImageDeploymentName;

    @Value("${client-openai-key}")
    private String openAiKey;

    @Value("${client-openai-endpoint}")
    private String openAiEndpoint;

    private final String IMAGE_URL = "https://ai-proxy.lab.epam.com/v1/";

    private final HttpClient httpClient = HttpClient.newBuilder().build();

    private final ObjectMapper objectMapper;

    public ImagePromptService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String generateImage(String input) throws IOException, InterruptedException {
        String API_URL = openAiEndpoint + "/openai/deployments/"+ openAiImageDeploymentName + "/chat/completions?api-version=2023-12-01-preview";
        var requestBody = new HashMap<>();
        requestBody.put("messages", new Object[]{Map.of("role", "user", "content", input)});
        requestBody.put("max_tokens", 1000);
        var requestJson = objectMapper.writeValueAsString(requestBody);

        var request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Api-Key", openAiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            var jsonResponse = objectMapper.readTree(response.body());
            // Skip directly to the "url" node
            JsonNode urlNode = findUrlNode(jsonResponse);
            // Print the result
            if (urlNode != null) {
                log.info("Extracted URL: {}", urlNode.asText());
                // Fetch image using HttpClient
                HttpRequest imageRequest = HttpRequest.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Api-Key", openAiKey)
                        .uri(URI.create(IMAGE_URL + urlNode.asText()))
                        .build();
                // Get the image as a byte array
                HttpResponse<byte[]> ImageResponse = httpClient.send(imageRequest, HttpResponse.BodyHandlers.ofByteArray());
                byte[] imageBytes = ImageResponse.body();
                log.info("imageBytes {}", imageBytes.length);
                // Convert byte array to Base64 encoded string
                return Base64.getEncoder().encodeToString(imageBytes);
            } else {
                log.info("URL field not found!");
            }
            return "Not found";//jsonResponse.at("/choices/0/message/custom_content/attachments/1/url").asText();
        } else {
            throw new RuntimeException("Failed to generate image: " + response.body());
        }
    }

    // Method to get the "url"
    private static JsonNode findUrlNode(JsonNode node) {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                if ("url".equals(entry.getKey())) {
                    return entry.getValue();  // Return the URL node if found
                }
                JsonNode foundNode = findUrlNode(entry.getValue());  // Recurse into nested objects
                if (foundNode != null) {
                    return foundNode;  // Return if URL found
                }
            }
        } else if (node.isArray()) {
            for (JsonNode item : node) {
                JsonNode foundNode = findUrlNode(item);
                if (foundNode != null) {
                    return foundNode;
                }
            }
        }
        return null;  // URL not found
    }
}

