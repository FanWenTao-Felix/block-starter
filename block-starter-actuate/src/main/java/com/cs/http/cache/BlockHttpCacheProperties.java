package com.cs.http.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Http Cache 配置
 *
 * @blame csz
 */
@ConfigurationProperties("block.http.cache")
public class BlockHttpCacheProperties {
	/**
	 * Http-cache 的 spring cache名，默认：blockHttpCache
	 */
	@Getter
	@Setter
	private String cacheName = "blockHttpCache";

	/**
	 * 默认拦截/**
	 */
	@Getter
	private final List<String> includePatterns = new ArrayList<String>() {{
		add("/**");
	}};

	/**
	 * 默认排除静态文件目录
	 */
	@Getter
	private final List<String> excludePatterns = new ArrayList<>();
}
