package com.infosupport.demos.qnabot.service;

/**
 * Defines the structure of a chatbot
 */
public interface ChatBot {
    /**
     * Handles incoming chatbot activities
     *
     * @param context Conversation context for the current incoming activity
     */
    void handle(ConversationContext context);
}
