package com.seashells.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seashells.backend.sevice.ChatService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        System.out.println("Received question: " + question);

        Map<String, String> result = new HashMap<>();
        if (question == null || question.isBlank()) {
            result.put("response", "No question provided.");
            return result;
        }

        String response = chatService.processQuestion(question);
        result.put("response", response);
        return result;
    }
}
