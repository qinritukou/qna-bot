package com.infosupport.demos.qnabot.service;

import com.infosupport.demos.qnabot.*;
import com.microsoft.bot.schema.models.Activity;
import com.microsoft.bot.schema.models.ActivityTypes;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Chatbot implementation
 */
public final class ChatBotImpl implements ChatBot {
    private QuestionClassifier classifier;

    public ChatBotImpl() {
        try {
            TextVectorizer vectorizer = QuestionVectorizerFactory.restore(getResourceFile("vectorizer.bin"));
            Map<Integer, String> answerMapping = AnswersMappingFactory.create(getResourceFile("answers.csv"));

            classifier = QuestionClassifierFactory.restore(
                    getResourceFile("classifier.bin"),
                    vectorizer, answerMapping);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            // Do nothing
        }
    }

    /**
     * Handles incoming chatbot activities
     *
     * @param context Context for the current conversation
     */
    @Override
    public void handle(ConversationContext context) {
        if (context.activity().type().equals(ActivityTypes.MESSAGE)) {
            String replyText;

            if (classifier != null) {
                replyText = classifier.predict(context.activity().text());
            } else {
                replyText = "Sorry, I have no clue what to do here. My brain ain't what it used to be.";
            }

            Activity reply = ActivityFactory.createReply(context.activity(), replyText);
            context.sendActivity(reply);
        }
    }

    private File getResourceFile(String path) throws URISyntaxException {
        return new File(getClass().getResource(path).toURI());
    }
}
