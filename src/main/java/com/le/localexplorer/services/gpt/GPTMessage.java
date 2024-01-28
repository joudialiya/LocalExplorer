package com.le.localexplorer.services.gpt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GPTMessage {
    public static final String USER = "user";
    public static final String SYSTEM = "system";
    public static final String ASSISTANT = "assistant";
    String role = USER;
    String content = "";
}
