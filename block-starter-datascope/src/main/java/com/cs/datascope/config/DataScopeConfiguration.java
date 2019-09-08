package com.cs.datascope.config;

import com.cs.datascope.handler.BlockDataScopeHandler;
import com.cs.datascope.handler.BlockScopeModelHandler;
import com.cs.datascope.handler.DataScopeHandler;
import com.cs.datascope.handler.ScopeModelHandler;
import com.cs.datascope.interceptor.DataScopeInterceptor;
import com.cs.datascope.props.DataScopeProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据权限配置类
 *
 * @blame csz
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(DataScopeProperties.class)
public class DataScopeConfiguration {

	private JdbcTemplate jdbcTemplate;

	@Bean
	@ConditionalOnMissingBean(ScopeModelHandler.class)
	public ScopeModelHandler scopeModelHandler() {
		return new BlockScopeModelHandler(jdbcTemplate);
	}

	@Bean
	@ConditionalOnBean(ScopeModelHandler.class)
	@ConditionalOnMissingBean(DataScopeHandler.class)
	public DataScopeHandler dataScopeHandler(ScopeModelHandler scopeModelHandler) {
		return new BlockDataScopeHandler(scopeModelHandler);
	}

	@Bean
	@ConditionalOnBean(DataScopeHandler.class)
	@ConditionalOnMissingBean(DataScopeInterceptor.class)
	public DataScopeInterceptor interceptor(DataScopeHandler dataScopeHandler, DataScopeProperties dataScopeProperties) {
		return new DataScopeInterceptor(dataScopeHandler, dataScopeProperties);
	}

}
