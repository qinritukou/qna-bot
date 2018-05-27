package com.infosupport.demos.qnabot.training;

import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.Writable;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.bagofwords.vectorizer.BagOfWordsVectorizer;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.text.documentiterator.FileDocumentIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TrainingApplication {
    public static void main(String... args) throws Exception {
        StatsStorage statsStorage = new InMemoryStatsStorage();
        UIServer server = UIServer.getInstance();

        BagOfWordsVectorizer vectorizer = fitVectorizer();

        int inputLayerSize = vectorizer.getVocabCache().vocabWords().size();
        int numAnswers = Files.readAllLines(Paths.get("data/answers.csv")).size();

        MultiLayerConfiguration networkConfiguration = new NeuralNetConfiguration.Builder()
                .seed(1337)
                .weightInit(WeightInit.UNIFORM)
                .updater(new Sgd(0.01))
                .list()
                .layer(0, new DenseLayer.Builder().nIn(inputLayerSize).nOut(32).activation(Activation.RELU).build())
                .layer(1, new DenseLayer.Builder().nIn(32).nOut(32).activation(Activation.RELU).build())
                .layer(2, new OutputLayer.Builder()
                        .lossFunction(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX)
                        .weightInit(WeightInit.UNIFORM)
                        .nIn(32).nOut(numAnswers)
                        .build())
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(networkConfiguration);

        network.setListeners(new ScoreIterationListener(1));
        network.setListeners(new StatsListener(statsStorage, 1));

        network.init();

        server.attach(statsStorage);

        try (CSVRecordReader reader = new CSVRecordReader(1, ',')) {
            reader.initialize(new FileSplit(new File("data/questions.csv")));

            for(int epoch = 0; epoch < 5; epoch++) {
                while (reader.hasNext()) {
                    List<List<Writable>> rawData = reader.next(32);

                    List<INDArray> data = rawData.stream()
                            .map(record -> vectorizer.transform(rawData.get(1).toString()))
                            .collect(Collectors.toList());

                    List<INDArray> labelData = rawData.stream()
                            .map(record -> oneHotEncode(record.get(2).toInt(), numAnswers))
                            .collect(Collectors.toList());

                    int[] featureShape = {rawData.size(), vectorizer.getVocabCache().numWords()};
                    int[] labelShape = {rawData.size(), numAnswers};

                    INDArray features = Nd4j.create(data, featureShape);
                    INDArray labels = Nd4j.create(data, labelShape);

                    network.fit(features, labels);
                }

                reader.reset();
            }
        }

        File outputFile = new File("model/textclassifier.nb");
        ModelSerializer.writeModel(network, outputFile, false);
    }

    static BagOfWordsVectorizer fitVectorizer() {
        BagOfWordsVectorizer vectorizer = new BagOfWordsVectorizer.Builder()
                .setIterator(new FileDocumentIterator(new File("data")))
                .setMinWordFrequency(1)
                .setTokenizerFactory(new DefaultTokenizerFactory())
                .build();

        vectorizer.fit();

        return vectorizer;
    }

    static INDArray oneHotEncode(int value, int numClasses) {
        INDArray output = Nd4j.create(numClasses);
        output.put(0, value, 1.0f);

        return output;
    }

}
