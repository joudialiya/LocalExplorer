package com.le.localexplorer.services.gpt;

import java.util.List;

import jakarta.annotation.Generated;
import lombok.Data;

@Data
public class GPTResponse {
    String id;
    String object;
    Integer created;
    String model;
    List<Choice> choices;
    Usage usage;
    Object systemFingerprint;
    @Data
    public static class Choice {
        Integer index;
        GPTMessage message;
        Object logprobs;
        String finishReason;
    }
    @Data
    public static class Usage {
        Integer promptTokens;
        Integer completionTokens;
        Integer totalTokens;
    }
}
