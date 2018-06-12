package com.infosupport.demos.qnabot;

import java.io.File;
import java.util.Map;

public class TrainingApplication {
    public static void main(String... args) throws Exception {
        Map<Integer, String> answers = AnswersMappingFactory.create(new File("data/answers.csv"));

        TextVectorizer vectorizer = QuestionVectorizerFactory.create(new File("data/"));
        vectorizer.fit();
        vectorizer.save(new File("model/vectorizer.bin"));

        QuestionClassifier classifier = QuestionClassifierFactory.create(vectorizer, answers);
        classifier.fit(new File("data/questions_train.csv"));
        classifier.save(new File("model/classifier.bin"));
    }
}
