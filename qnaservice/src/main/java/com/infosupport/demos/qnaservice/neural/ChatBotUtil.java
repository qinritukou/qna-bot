package com.infosupport.demos.qnaservice.neural;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infosupport.demos.qnaservice.share.AnswersMappingFactory;
import com.infosupport.demos.qnaservice.share.QuestionClassifier;
import com.infosupport.demos.qnaservice.share.QuestionClassifierFactory;
import com.infosupport.demos.qnaservice.share.QuestionVectorizerFactory;
import com.infosupport.demos.qnaservice.share.TextVectorizer;

public class ChatBotUtil {
	static Logger log = LoggerFactory.getLogger(ChatBotUtil.class);

	private static QuestionClassifier classifier;

    public static String handle(String question) {
    	String path = System.getProperty("user.home") + "/botdata/";
        try {
            TextVectorizer vectorizer = QuestionVectorizerFactory.restore(new File(path + "vectorizer.bin"));
            Map<Integer, String> answerMapping = AnswersMappingFactory.create(new File(path + "answers.csv"));

            classifier = QuestionClassifierFactory.restore(new File(path + "classifier.bin"), vectorizer, answerMapping);
        } catch (Exception e) {
            // Do nothing
        	e.printStackTrace();
        }
    	String replyText = classifier.predict(question);
    	return replyText;
    }

    
}
