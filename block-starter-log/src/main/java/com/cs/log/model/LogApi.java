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

import java.io.Serializable;
import java.util.Date;

/**
 * 实体类
 *
 * @blame csz
 */
@Data
@TableName("block_log_api")
public class LogApi implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "id", type = IdType.ID_WORKER)
	private Long id;

	/**
	 * 日志类型
	 */
	private String type;
	/**
	 * 日志标题
	 */
	private String title;
	/**
	 * 服务ID
	 */
	private String serviceId;
	/**
	 * 服务器 ip
	 */
	private String serverIp;
	/**
	 * 服务器名
	 */
	private String serverHost;
	/**
	 * 环境
	 */
	private String env;
	/**
	 * 操作IP地址
	 */
	private String remoteIp;
	/**
	 * 用户代理
	 */
	private String userAgent;
	/**
	 * 请求URI
	 */
	private String requestUri;
	/**
	 * 操作方式
	 */
	private String method;
	/**
	 * 方法类
	 */
	private String methodClass;
	/**
	 * 方法名
	 */
	private String methodName;
	/**
	 * 操作提交的数据
	 */
	private String params;
	/**
	 * 执行时间
	 */
	private String time;

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
