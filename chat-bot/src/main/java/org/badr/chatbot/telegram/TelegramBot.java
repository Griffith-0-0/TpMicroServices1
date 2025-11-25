package org.badr.chatbot.telegram;

import jakarta.annotation.PostConstruct;
import org.badr.chatbot.agent.AiAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${telegram.api.key}")
    private String telegramBotToken;
    private AiAgent aiAgent;

    public TelegramBot(AiAgent aiAgent) {
        this.aiAgent = aiAgent;
    }
    @PostConstruct
    public void registerBot() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onUpdateReceived(Update request) {
        try {
            if (!request.hasMessage()) return;
            String message = request.getMessage().getText();
            String answer = aiAgent.chat(message);
            Long chatId= request.getMessage().getChatId();
            sendTextMessage(chatId, answer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String getBotUsername() {
        return "badrAiMcp_Bot";
    }
    @Override
    public String getBotToken() {
        return telegramBotToken;
    }

    private void sendTextMessage(Long chatId,String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(chatId.toString(), text);
        execute(sendMessage);
    }
}
