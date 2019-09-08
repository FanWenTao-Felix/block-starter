package com.cs.log.feign;

import com.cs.core.launch.constant.AppConstant;
import com.cs.core.tool.api.R;
import com.cs.log.model.LogApi;
import com.cs.log.model.LogError;
import com.cs.log.model.LogUsual;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign接口类
 *
 * @blame csz
 */
@FeignClient(
	value = AppConstant.APPLICATION_LOG_NAME
)
public interface ILogClient {

	String API_PREFIX = "/log";

	/**
	 * 保存错误日志
	 *
	 * @param log
	 * @return
	 */
	@PostMapping(API_PREFIX + "/saveUsualLog")
	R<Boolean> saveUsualLog(@RequestBody LogUsual log);

	/**
	 * 保存操作日志
	 *
	 * @param log
	 * @return
	 */
	@PostMapping(API_PREFIX + "/saveApiLog")
	R<Boolean> saveApiLog(@RequestBody LogApi log);

	/**
	 * 保存错误日志
	 *
	 * @param log
	 * @return
	 */
	@PostMapping(API_PREFIX + "/saveErrorLog")
	R<Boolean> saveErrorLog(@RequestBody LogError log);

}
