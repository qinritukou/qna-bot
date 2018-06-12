package com.infosupport.demos.qnabot;

import org.deeplearning4j.bagofwords.vectorizer.BagOfWordsVectorizer;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.File;

public class QuestionVectorizerFactory {
    /**
     * Creates a new preconfigured question vectorizer
     *
     * @return A new instance of {@link QuestionVectorizer}
     */
    public static QuestionVectorizer create(File inputFile) throws Exception {
        // The tokenizer factory will use the preprocessor to make everything lower-case
        // and remove any punctuation in the text.
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        // This vectorizer uses the TF-IDF algorithm to produce
        // a unique fingerprint for every question we feed it.
        BagOfWordsVectorizer vectorizer = new BagOfWordsVectorizer.Builder()
                .setTokenizerFactory(tokenizerFactory)
                .setIterator(new CSVSentenceIterator(inputFile))
                .build();

        return new QuestionVectorizer(vectorizer);
    }
}
