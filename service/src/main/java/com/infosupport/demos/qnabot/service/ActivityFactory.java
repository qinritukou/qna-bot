package com.infosupport.demos.qnabot.service;

import com.microsoft.bot.schema.models.Activity;
import com.microsoft.bot.schema.models.ActivityTypes;
import org.joda.time.DateTime;

public class ActivityFactory {
    public static Activity createReply(Activity activity, String text) {
        Activity reply = new Activity();

        reply.withFrom(activity.recipient());
        reply.withRecipient(activity.from());
        reply.withConversation(activity.conversation());
        reply.withChannelId(activity.channelId());
        reply.withReplyToId(activity.id());
        reply.withServiceUrl(activity.serviceUrl());
        reply.withTimestamp(new DateTime());
        reply.withType(ActivityTypes.MESSAGE);

        if (text != null && !text.isEmpty()) {
            reply.withText(text);
        }

        return reply;
    }


}
