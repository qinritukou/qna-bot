package com.infosupport.demos.qnabot.training;

import org.deeplearning4j.bagofwords.vectorizer.BagOfWordsVectorizer;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.Arrays;

/**
 * Converts questions to a correct representation for the question classifier
 */
public class QuestionVectorizer {
    private final BagOfWordsVectorizer vectorizer;

    /**
     * Initializes a new instance of {@link QuestionVectorizer}
     *
     * @param vectorizer Bag of words vectorizer to use for vectorization
     */
    public QuestionVectorizer(BagOfWordsVectorizer vectorizer) {
        this.vectorizer = vectorizer;
    }

    /**
     * Transforms questions to a vector representation suitable for our model
     *
     * @param questions Questions to transform
     * @return Returns a 2D-matrix with the correct representation for the neural network
     */
    public INDArray transform(String... questions) {
        return vectorizer.transform(Arrays.asList(questions));
    }

    /**
     * Gets the size of the vocabulary
     *
     * @return Returns the number of elements in the vocabulary
     */
    public int vocabularySize() {
        return vectorizer.getVocabCache().numWords();
    }

    /**
     * Trains the vectorizer
     */
    public void fit() {
        vectorizer.fit();
    }
}
