package org.badr.chatbot.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@Component
public class AiAgent {
    private ChatClient chatClient;

    public AiAgent(ChatClient.Builder builder, ChatMemory chatMemory, ToolCallbackProvider toolCallbackProvider) {
        Arrays.stream(toolCallbackProvider.getToolCallbacks()).forEach(toolCallback -> {
            System.out.println("---------------------");
            System.out.println(toolCallback.getToolDefinition());
            System.out.println("---------------------");
        });

        this.chatClient = builder.defaultSystem("Vous est un assistant ai qui reponds au questions dans le context, si question hors context tu di 'c est hors de mes acces' et tu comprent francais et anglais")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultToolCallbacks(toolCallbackProvider).build();
    }

    public String chat(String query) {
        return chatClient.prompt().user(query).call().content();
    }

    public Flux<String> chatStream(String query) {
        return chatClient.prompt().user(query).stream().content();
    }
}
