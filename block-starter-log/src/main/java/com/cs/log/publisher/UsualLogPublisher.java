package com.cs.log.publisher;

import com.cs.core.tool.utils.SpringUtil;
import com.cs.core.tool.utils.WebUtil;
import com.cs.log.constant.EventConstant;
import com.cs.log.event.UsualLogEvent;
import com.cs.log.model.LogUsual;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * BLOCK日志信息事件发送
 *
 * @blame csz
 */
public class UsualLogPublisher {

	public static void publishEvent(String level, String id, String data) {
		HttpServletRequest request = WebUtil.getRequest();
		LogUsual logUsual = new LogUsual();
		logUsual.setLogLevel(level);
		logUsual.setLogId(id);
		logUsual.setLogData(data);
		Map<String, Object> event = new HashMap<>(16);
		event.put(EventConstant.EVENT_LOG, logUsual);
		event.put(EventConstant.EVENT_REQUEST, request);
		SpringUtil.publishEvent(new UsualLogEvent(event));
	}

}
