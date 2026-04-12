package xyz.catuns.edupulse.quiz.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

import java.time.Duration;
import java.time.Instant;

@Slf4j
public class TokenUsageAuditAdvisor implements CallAdvisor {

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        Instant start = Instant.now();
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

        Duration duration = Duration.between(start, Instant.now());
        log.info("Execution duration {}s", duration.toSeconds());
        ChatResponse chatResponse = chatClientResponse.chatResponse();

        if (chatResponse.getMetadata() != null) {
            Usage usage = chatResponse.getMetadata().getUsage();
            if (usage != null) {
                log.info("Token usage details: {}",usage.toString());
            }
        }
        return chatClientResponse;
    }

    @Override
    public String getName() {
        return "tokenUsageAudit";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
