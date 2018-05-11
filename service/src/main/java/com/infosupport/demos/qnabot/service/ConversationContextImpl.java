package com.infosupport.demos.qnabot.service;

import com.microsoft.bot.connector.ConnectorClient;
import com.microsoft.bot.connector.models.ErrorResponseException;
import com.microsoft.bot.schema.models.Activity;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConversationContextImpl implements ConversationContext {
    private static Logger logger = Logger.getLogger(ConversationContextImpl.class.getName());

    private final ConnectorClient connector;
    private final Activity activity;

    public ConversationContextImpl(ConnectorClient connector, Activity activity) {
        this.connector = connector;
        this.activity = activity;
    }

    @Override
    public ConnectorClient connector() {
        return connector;
    }

    @Override
    public Activity activity() {
        return activity;
    }

    @Override
    public void sendActivity(Activity activity) {
        try {
            connector.conversations().sendToConversation(activity.conversation().id(), activity);
        } catch(ErrorResponseException ex) {
            logger.log(Level.SEVERE, "Failed to deliver activity to channel", ex);
        }
    }
}
