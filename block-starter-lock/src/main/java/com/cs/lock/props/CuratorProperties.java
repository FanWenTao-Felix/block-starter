package com.cs.lock.props;

import com.cs.core.launch.constant.ZookeeperConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置类
 *
 * @blame csz
 */
@Getter
@Setter
@ConfigurationProperties("block.lock")
public class CuratorProperties {
	private int retryCount = 5;
	private int elapsedTimeMs = 5000;
	private int sessionTimeoutMs = 60000;
	private int connectionTimeoutMs = 5000;
	private String connectString = ZookeeperConstant.ZOOKEEPER_CONNECT_STRING;
}
