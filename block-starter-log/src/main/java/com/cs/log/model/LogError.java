package com.cs.log.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cs.core.tool.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务 异常
 *
 * @blame csz
 */
@Data
@TableName("block_log_error")
public class LogError implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "id", type = IdType.ID_WORKER)
	private Long id;
	/**
	 * 应用名
	 */
	private String serviceId;
	/**
	 * 环境
	 */
	private String env;
	/**
	 * 服务器 ip
	 */
	private String serverIp;
	/**
	 * 服务器名
	 */
	private String serverHost;
	/**
	 * 用户代理
	 */
	private String userAgent;
	/**
	 * 请求url
	 */
	@Nullable
	private String requestUri;
	/**
	 * 操作方式
	 */
	private String method;
	/**
	 * 堆栈信息
	 */
	private String stackTrace;
	/**
	 * 异常名
	 */
	private String exceptionName;
	/**
	 * 异常消息
	 */
	private String message;
	/**
	 * 类名
	 */
	private String methodClass;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 方法名
	 */
	private String methodName;
	/**
	 * 操作提交的数据
	 */
	private String params;
	/**
	 * 代码行数
	 */
	private Integer lineNumber;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	private Date createTime;
}
