package xyz.catuns.edupulse.quiz.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import xyz.catuns.spring.base.properties.KafkaTopicProperties;
import xyz.catuns.spring.base.properties.OpenApiProperties;

@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {

	@NestedConfigurationProperty
	private OpenApiProperties openApi = new OpenApiProperties();

	@NestedConfigurationProperty
	private KafkaTopicProperties kafka = new KafkaTopicProperties();

}
