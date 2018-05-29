package com.infosupport.demos.qnabot.training;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class AnswerMappingFactoryTests {
    @Test
    public void createReturnsMapping() throws Exception {
        Map<Integer,String> mapping = AnswersMappingFactory.create(new File("../data/answers.csv"));

        assertThat(mapping.size()).isEqualTo(5);
    }
}
