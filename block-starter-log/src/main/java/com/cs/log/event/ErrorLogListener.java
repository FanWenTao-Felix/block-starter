package com.cs.log.event;


import com.cs.core.launch.props.BlockProperties;
import com.cs.core.launch.server.ServerInfo;
import com.cs.core.secure.utils.SecureUtil;
import com.cs.core.tool.utils.DateUtil;
import com.cs.core.tool.utils.WebUtil;
import com.cs.log.constant.EventConstant;
import com.cs.log.feign.ILogClient;
import com.cs.log.model.LogError;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 异步监听错误日志事件
 *
 * @blame csz
 */
@Slf4j
@AllArgsConstructor
public class ErrorLogListener {

	private final ILogClient logService;
	private final ServerInfo serverInfo;
	private final BlockProperties blockProperties;

	@Async
	@Order
	@EventListener(ErrorLogEvent.class)
	public void saveErrorLog(ErrorLogEvent event) {
		Map<String, Object> source = (Map<String, Object>) event.getSource();
		LogError logError = (LogError) source.get(EventConstant.EVENT_LOG);
		HttpServletRequest request = (HttpServletRequest) source.get(EventConstant.EVENT_REQUEST);
		logError.setUserAgent(request.getHeader(WebUtil.USER_AGENT_HEADER));
		logError.setMethod(request.getMethod());
		logError.setParams(WebUtil.getRequestParamString(request));
		logError.setServiceId(blockProperties.getName());
		logError.setServerHost(serverInfo.getHostName());
		logError.setServerIp(serverInfo.getIpWithPort());
		logError.setEnv(blockProperties.getEnv());
		logError.setCreateBy(SecureUtil.getUserAccount(request));
		logError.setCreateTime(DateUtil.now());
		logService.saveErrorLog(logError);
	}

}
