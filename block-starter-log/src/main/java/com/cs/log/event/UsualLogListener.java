package com.cs.log.event;


import com.cs.core.launch.props.BlockProperties;
import com.cs.core.launch.server.ServerInfo;
import com.cs.core.secure.utils.SecureUtil;
import com.cs.core.tool.utils.DateUtil;
import com.cs.core.tool.utils.UrlUtil;
import com.cs.core.tool.utils.WebUtil;
import com.cs.log.constant.EventConstant;
import com.cs.log.feign.ILogClient;
import com.cs.log.model.LogUsual;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 异步监听日志事件
 *
 * @blame csz
 */
@Slf4j
@AllArgsConstructor
public class UsualLogListener {

	private final ILogClient logService;
	private final ServerInfo serverInfo;
	private final BlockProperties blockProperties;

	@Async
	@Order
	@EventListener(UsualLogEvent.class)
	public void saveUsualLog(UsualLogEvent event) {
		Map<String, Object> source = (Map<String, Object>) event.getSource();
		LogUsual logUsual = (LogUsual) source.get(EventConstant.EVENT_LOG);
		HttpServletRequest request = (HttpServletRequest) source.get(EventConstant.EVENT_REQUEST);
		logUsual.setRequestUri(UrlUtil.getPath(request.getRequestURI()));
		logUsual.setUserAgent(request.getHeader(WebUtil.USER_AGENT_HEADER));
		logUsual.setMethod(request.getMethod());
		logUsual.setParams(WebUtil.getRequestParamString(request));
		logUsual.setServerHost(serverInfo.getHostName());
		logUsual.setServiceId(blockProperties.getName());
		logUsual.setEnv(blockProperties.getEnv());
		logUsual.setServerIp(serverInfo.getIpWithPort());
		logUsual.setCreateBy(SecureUtil.getUserAccount(request));
		logUsual.setCreateTime(DateUtil.now());
		logService.saveUsualLog(logUsual);
	}

}
