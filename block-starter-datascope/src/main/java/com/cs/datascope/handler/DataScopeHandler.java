package com.cs.datascope.handler;

import com.cs.core.secure.BlockUser;
import com.cs.datascope.model.DataScopeModel;
import org.apache.ibatis.plugin.Invocation;

/**
 * 数据权限规则
 *
 * @blame csz
 */
public interface DataScopeHandler {

	/**
	 * 获取过滤sql
	 *
	 * @param invocation  过滤器信息
	 * @param mapperId    数据查询类
	 * @param dataScope   数据权限类
	 * @param blockUser   当前用户信息
	 * @param originalSql 原始Sql
	 * @return sql
	 */
	String sqlCondition(Invocation invocation, String mapperId, DataScopeModel dataScope, BlockUser blockUser, String originalSql);

}
