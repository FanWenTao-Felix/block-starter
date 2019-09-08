package com.cs.log.config;

import com.cs.core.launch.props.BlockProperties;
import com.cs.core.launch.server.ServerInfo;
import com.cs.log.aspect.ApiLogAspect;
import com.cs.log.event.ApiLogListener;
import com.cs.log.event.ErrorLogListener;
import com.cs.log.event.UsualLogListener;
import com.cs.log.feign.ILogClient;
import com.cs.log.logger.BlockLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志工具自动配置
 *
 * @blame csz
 */
@Configuration
@ConditionalOnWebApplication
public class BlockLogToolAutoConfiguration {

	@Bean
	public ApiLogAspect apiLogAspect() {
		return new ApiLogAspect();
	}

	@Bean
	public BlockLogger blockLogger() {
		return new BlockLogger();
	}

	@Bean
	@ConditionalOnMissingBean(name = "apiLogListener")
	public ApiLogListener apiLogListener(ILogClient logService, ServerInfo serverInfo, BlockProperties blockProperties) {
		return new ApiLogListener(logService, serverInfo, blockProperties);
	}

	@Bean
	@ConditionalOnMissingBean(name = "errorEventListener")
	public ErrorLogListener errorEventListener(ILogClient logService, ServerInfo serverInfo, BlockProperties blockProperties) {
		return new ErrorLogListener(logService, serverInfo, blockProperties);
	}

	@Bean
	@ConditionalOnMissingBean(name = "usualEventListener")
	public UsualLogListener usualEventListener(ILogClient logService, ServerInfo serverInfo, BlockProperties blockProperties) {
		return new UsualLogListener(logService, serverInfo, blockProperties);
	}

}
