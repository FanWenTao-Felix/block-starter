package com.cs.lock.config;

import com.cs.lock.locker.BlockLocker;
import com.cs.lock.props.CuratorProperties;
import lombok.AllArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 *
 * @blame csz
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({
	CuratorProperties.class
})
public class CuratorConfiguration {

	CuratorProperties curatorProperties;

	@Bean(initMethod = "start")
	public CuratorFramework curatorFramework() {
		return CuratorFrameworkFactory.newClient(
			curatorProperties.getConnectString(),
			curatorProperties.getSessionTimeoutMs(),
			curatorProperties.getConnectionTimeoutMs(),
			new RetryNTimes(curatorProperties.getRetryCount(), curatorProperties.getElapsedTimeMs()));
	}

	@Bean
	public BlockLocker blockLocker() {
		return new BlockLocker();
	}

}
