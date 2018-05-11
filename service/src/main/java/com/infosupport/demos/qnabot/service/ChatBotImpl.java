package com.infosupport.demos.qnabot.service;

import com.microsoft.bot.schema.models.Activity;
import com.microsoft.bot.schema.models.ActivityTypes;

public class ChatBotImpl implements ChatBot {
    @Override
    public void handle(ConversationContext context) {
        if(context.activity().type().equals(ActivityTypes.MESSAGE)) {
            Activity reply = ActivityFactory.createReply(context.activity(), context.activity().text());
            context.sendActivity(reply);
        }
    }
}
