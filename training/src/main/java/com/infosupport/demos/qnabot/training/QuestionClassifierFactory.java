package com.infosupport.demos.qnabot.training;

import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.AutoEncoder;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.Map;

/**
 * This factory can be used to create preconfigured instances of the {@link QuestionClassifier}.
 * It will create a new multi-layer neural network and produce a new instance of {@link QuestionClassifier} with it.
 */
public class QuestionClassifierFactory {
    /**
     * Creates a new instance of the question classifier based on the provided parameters
     *
     * @param vectorizer The question vectorizer to use
     * @param answers    The set of possible answers
     * @return Returns a new instance of {@link QuestionClassifier}
     */
    public static QuestionClassifier create(
            QuestionVectorizer vectorizer,
            Map<Integer, String> answers) {

        StatsStorage statsStorage = new InMemoryStatsStorage();
        MultiLayerNetwork network = createNeuralNetwork(
                vectorizer.vocabularySize(),
                answers.size(),
                statsStorage);

        return new QuestionClassifier(network, vectorizer, statsStorage, answers);
    }

    private static MultiLayerNetwork createNeuralNetwork(
            int inputLayerSize, int outputLayerSize, StatsStorage statsStorage) {
        MultiLayerConfiguration networkConfiguration = new NeuralNetConfiguration.Builder()
                .seed(1337)
                .weightInit(WeightInit.XAVIER)
                .updater(new Adam(0.001, 0.9, 0.999, 1e-07))
                .list()
                .layer(0, new AutoEncoder.Builder()
                        .nIn(inputLayerSize).nOut(128)
                        .corruptionLevel(0.3)
                        .lossFunction(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY)
                        .build()
                )
                .layer(1, new AutoEncoder.Builder()
                        .nIn(128).nOut(64)
                        .corruptionLevel(0.3)
                        .lossFunction(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY)
                        .build()
                )
                .layer(2, new OutputLayer.Builder()
                        .lossFunction(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX)
                        .nIn(64).nOut(outputLayerSize)
                        .build()
                )
                .pretrain(true)
                .backprop(true)
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(networkConfiguration);

        network.setListeners(new ScoreIterationListener(1));
        network.setListeners(new StatsListener(statsStorage, 1));

        network.init();
        return network;
    }
}
