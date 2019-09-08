package com.cs.log.event;

import com.cs.core.launch.props.BlockProperties;
import com.cs.core.launch.server.ServerInfo;
import com.cs.core.secure.utils.SecureUtil;
import com.cs.core.tool.utils.DateUtil;
import com.cs.core.tool.utils.UrlUtil;
import com.cs.core.tool.utils.WebUtil;
import com.cs.log.constant.EventConstant;
import com.cs.log.feign.ILogClient;
import com.cs.log.model.LogApi;
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
public class ApiLogListener {

	private final ILogClient logService;
	private final ServerInfo serverInfo;
	private final BlockProperties blockProperties;


	@Async
	@Order
	@EventListener(ApiLogEvent.class)
	public void saveApiLog(ApiLogEvent event) {
		Map<String, Object> source = (Map<String, Object>) event.getSource();
		LogApi logApi = (LogApi) source.get(EventConstant.EVENT_LOG);
		HttpServletRequest request = (HttpServletRequest) source.get(EventConstant.EVENT_REQUEST);
		logApi.setServiceId(blockProperties.getName());
		logApi.setServerHost(serverInfo.getHostName());
		logApi.setServerIp(serverInfo.getIpWithPort());
		logApi.setEnv(blockProperties.getEnv());
		logApi.setRemoteIp(WebUtil.getIP(request));
		logApi.setUserAgent(request.getHeader(WebUtil.USER_AGENT_HEADER));
		logApi.setRequestUri(UrlUtil.getPath(request.getRequestURI()));
		logApi.setMethod(request.getMethod());
		logApi.setParams(WebUtil.getRequestParamString(request));
		logApi.setCreateBy(SecureUtil.getUserAccount(request));
		logApi.setCreateTime(DateUtil.now());
		logService.saveApiLog(logApi);
	}

}
