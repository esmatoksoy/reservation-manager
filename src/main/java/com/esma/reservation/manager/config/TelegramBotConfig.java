package com.esma.reservation.manager.config;

import com.esma.reservation.manager.service.TelegramNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Slf4j
public class TelegramBotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramNotificationService bot) {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(bot);
            log.info("Telegram bot registered successfully: {}", bot.getBotUsername());
            return api;
        } catch (TelegramApiException e) {
            log.error("Failed to register Telegram bot — notifications will be unavailable: {}", e.getMessage());
            try {
                return new TelegramBotsApi(DefaultBotSession.class) {};
            } catch (TelegramApiException ex) {
                throw new RuntimeException("Could not create TelegramBotsApi fallback", ex);
            }
        }
    }
}

