package com.seashells.backend.sevice;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChatService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${CUSTOMER_API_URL:http://localhost:8080/api/customers}")
    private String customerApiUrl;

    @Value("${OLLAMA_URL:http://localhost:11434}")
    private String ollamaUrl;

    public ChatService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public String processQuestion(String question) {
        try {
            String customerData = fetchCustomerData();
            return queryWithOllama(question, customerData);
        } catch (Exception e) {
            return "Sorry, I encountered an error: " + e.getMessage();
        }
    }

    private String fetchCustomerData() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(customerApiUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch customer data: " + response.statusCode());
        }

        return response.body();
    }

    private String queryWithOllama(String question, String customerData) throws IOException, InterruptedException {
        String prompt = String.format("""
            You are a helpful assistant that analyzes customer data and answers questions.

            Customer Data (JSON):
            %s

            Question: %s

            Please analyze the data and provide a helpful answer. If you need to perform calculations or aggregations, do so based on the provided data.
            """, customerData, question);

        Map<String, Object> requestBody = Map.of(
                "model", "llama2",
                "prompt", prompt,
                "stream", false
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ollamaUrl + "/api/generate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(response.body());
                return jsonResponse.get("response").asText();
            } else {
                throw new RuntimeException("Ollama API error: " + response.statusCode());
            }
        } catch (Exception e) {
            return analyzeDataLocally(question, customerData);
        }
    }

    private String analyzeDataLocally(String question, String customerData) {
        try {
            JsonNode data = objectMapper.readTree(customerData);
            String lowerQuestion = question.toLowerCase();

            if (lowerQuestion.contains("how many") || lowerQuestion.contains("count")) {
                if (data.isArray()) {
                    return "I found " + data.size() + " customers in the dataset.";
                }
            }

            if (lowerQuestion.contains("total") || lowerQuestion.contains("sum")) {
                return "I can see the data but need an AI model to perform complex analysis. Please install Ollama for better responses.";
            }

            if (lowerQuestion.contains("show") || lowerQuestion.contains("display")) {
                if (data.isArray() && data.size() > 0) {
                    JsonNode firstCustomer = data.get(0);
                    StringBuilder fields = new StringBuilder("Available fields: ");
                    firstCustomer.fieldNames().forEachRemaining(field -> fields.append(field).append(", "));
                    return fields.toString();
                }
            }

            return "I can see your customer data but need an AI model like Ollama to provide detailed analysis. For now, I can tell you that your data contains " +
                    (data.isArray() ? data.size() + " records." : "customer information.");
        } catch (Exception e) {
            return "I'm having trouble analyzing the data: " + e.getMessage();
        }
    }
}

