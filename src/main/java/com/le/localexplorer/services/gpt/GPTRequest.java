package com.le.localexplorer.services.gpt;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@ToString
public class GPTRequest {
    final String model = "gpt-3.5-turbo";
    List<GPTMessage> messages = new ArrayList<>();
    public GPTRequest addMessage(String role, String content)
    {
        messages.add(GPTMessage.builder().role(role).content(content).build());
        return this;
    }
}
