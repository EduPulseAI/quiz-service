package xyz.catuns.edupulse.quiz.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class VertexAiConfig {

    @Value("${spring.ai.vertex.ai.gemini.chat.options.model:gemini-2.5-flash}")
    private String model;

    @Value("${spring.ai.vertex.ai.gemini.chat.options.temperature:0.7}")
    private Double temperature;

    @Value("${spring.ai.vertex.ai.gemini.chat.options.max-output-tokens:4096}")
    private Integer maxOutputTokens;

    @Value("${spring.ai.vertex.ai.gemini.chat.options.response-mime-type}")
    private String responseMimeType;

    @Value("classpath:vertex-ai/question-gen-schema.json")
    private Resource questionGenerationSchema;

    @Value("classpath:vertex-ai/question-gen-prompt.txt")
    private Resource questionGenerationPrompt;


    /**
     * Creates ChatClient bean configured for Vertex AI Gemini.
     * Uses builder pattern for fluent configuration.
     *
     * @param chatModel the auto-configured Vertex AI Gemini chat model
     * @return configured ChatClient instance
     */
    @Bean
    public ChatClient chatClient(VertexAiGeminiChatModel chatModel) throws IOException {
        String responseSchema = questionGenerationSchema.getContentAsString(StandardCharsets.UTF_8);
        return ChatClient.builder(chatModel)
                .defaultOptions(VertexAiGeminiChatOptions.builder()
                        .model(model)
                        .temperature(temperature)
//                        .maxOutputTokens(maxOutputTokens)
                        .responseMimeType(responseMimeType)
//                        .responseSchema(responseSchema)
                        .build())
                .build();
    }

    @Bean
    PromptTemplate questionGeneratorPromptTemplate() throws IOException {
        String promptText = questionGenerationPrompt.getContentAsString(StandardCharsets.UTF_8);
        return PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder()
                        .startDelimiterToken('<')
                        .endDelimiterToken('>')
                        .build())
                .template(promptText)
                .build();
    }
}
