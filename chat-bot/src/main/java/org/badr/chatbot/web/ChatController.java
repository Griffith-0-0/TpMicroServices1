package org.badr.chatbot.web;



import org.badr.chatbot.agent.AiAgent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;

@RestController
public class ChatController {
    private AiAgent aiAgent;
    public ChatController(AiAgent aiAgent) {
        this.aiAgent = aiAgent;
    }
    @GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestParam(name = "query") String query) {
        return aiAgent.chat(query);
    }
    @GetMapping(value = "/chatStream", produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<String> chatStream(@RequestParam(name = "query") String query) {
        return aiAgent.chatStream(query);
    }

}
