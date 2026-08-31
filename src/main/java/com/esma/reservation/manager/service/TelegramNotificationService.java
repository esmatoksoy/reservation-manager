package com.esma.reservation.manager.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class TelegramNotificationService extends TelegramLongPollingBot {

    @Value("${telegram.bot.chat-id}")
    private String chatId;

    private final String botUsername;

    public TelegramNotificationService(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username:ReservationManager_bot}") String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {//no need
    }

    public void sendNotification(String message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .parseMode("HTML")
                .build();
        try {
            execute(sendMessage);
            log.info("Telegram notification sent successfully");
        } catch (TelegramApiException e) {
            log.error("Failed to send Telegram notification: {}", e.getMessage(), e);
        }
    }
}

