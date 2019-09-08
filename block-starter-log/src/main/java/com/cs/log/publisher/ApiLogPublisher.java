package com.cs.log.publisher;


import com.cs.core.tool.constant.BlockConstant;
import com.cs.core.tool.utils.SpringUtil;
import com.cs.core.tool.utils.WebUtil;
import com.cs.log.annotation.ApiLog;
import com.cs.log.constant.EventConstant;
import com.cs.log.event.ApiLogEvent;
import com.cs.log.model.LogApi;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * API日志信息事件发送
 *
 * @blame csz
 */
public class ApiLogPublisher {

	public static void publishEvent(String methodName, String methodClass, ApiLog apiLog, long time) {
		HttpServletRequest request = WebUtil.getRequest();
		LogApi logApi = new LogApi();
		logApi.setType(BlockConstant.LOG_NORMAL_TYPE);
		logApi.setTitle(apiLog.value());
		logApi.setTime(String.valueOf(time));
		logApi.setMethodClass(methodClass);
		logApi.setMethodName(methodName);
		Map<String, Object> event = new HashMap<>(16);
		event.put(EventConstant.EVENT_LOG, logApi);
		event.put(EventConstant.EVENT_REQUEST, request);
		SpringUtil.publishEvent(new ApiLogEvent(event));
	}

}
