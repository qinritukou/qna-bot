package com.infosupport.demos.qnabot.service;

import com.microsoft.bot.connector.ConnectorClient;
import com.microsoft.bot.schema.models.Activity;

public interface ConversationContext {
    ConnectorClient connector();
    Activity activity();

    void sendActivity(Activity activity);
}
