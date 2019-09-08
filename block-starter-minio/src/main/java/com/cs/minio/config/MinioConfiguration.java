package com.cs.minio.config;

import com.cs.core.oss.config.OssConfiguration;
import com.cs.core.oss.props.OssProperties;
import com.cs.core.oss.rule.BlockOssRule;
import com.cs.core.oss.rule.OssRule;
import com.cs.minio.MinioTemplate;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio配置类
 *
 * @blame csz
 */
@Configuration
@AllArgsConstructor
@AutoConfigureAfter(OssConfiguration.class)
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(value = "oss.name", havingValue = "minio")
public class MinioConfiguration {

	private OssProperties ossProperties;

	@Bean
	@ConditionalOnMissingBean(OssRule.class)
	public OssRule ossRule() {
		return new BlockOssRule(ossProperties.getTenantMode());
	}

	@Bean
	@SneakyThrows
	@ConditionalOnMissingBean(MinioClient.class)
	public MinioClient minioClient() {
		return new MinioClient(
			ossProperties.getEndpoint(),
			ossProperties.getAccessKey(),
			ossProperties.getSecretKey()
		);
	}

	@Bean
	@ConditionalOnBean({MinioClient.class, OssRule.class})
	@ConditionalOnMissingBean(MinioTemplate.class)
	public MinioTemplate minioTemplate(MinioClient minioClient, OssRule ossRule) {
		return new MinioTemplate(minioClient, ossRule, ossProperties);
	}

}
