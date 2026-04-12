package xyz.catuns.edupulse.quiz.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import xyz.catuns.edupulse.quiz.advisor.TokenUsageAuditAdvisor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class VertexAiConfig {

    @Value("${spring.ai.vertex.ai.gemini.chat.options.model:gemini-2.5-flash}")
    private String model;

    @Value("${spring.ai.vertex.ai.gemini.chat.options.temperature:0.7}")
    private Double temperature;

    @Value("${spring.ai.vertex.ai.gemini.chat.options.max-output-tokens:4096}")
    private Integer maxOutputTokens;



    /**
     * Creates ChatClient bean configured for Vertex AI Gemini.
     * Uses builder pattern for fluent configuration.
     *
     * @param chatModel the auto-configured Vertex AI Gemini chat model
     * @return configured ChatClient instance
     */
    @Bean
    public ChatClient chatClient(VertexAiGeminiChatModel chatModel) {
        Advisor tokenUsageAdviser = new TokenUsageAuditAdvisor();
        Advisor loggingAdvisor = new SimpleLoggerAdvisor();
        return ChatClient.builder(chatModel)
                .defaultAdvisors(List.of(
                        tokenUsageAdviser,
                        loggingAdvisor))
                .build();
    }

}
