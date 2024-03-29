package com.cs.tenant;

import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.cs.core.secure.utils.SecureUtil;
import com.cs.core.tool.utils.Func;
import com.cs.core.tool.utils.StringUtil;
import com.cs.tenant.constant.TenantConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

/**
 * 租户信息处理器
 *
 * @blame csz
 */
@Slf4j
@AllArgsConstructor
public class BlockTenantHandler implements TenantHandler {

	private final BlockTenantProperties properties;

	/**
	 * 获取租户ID
	 *
	 * @return 租户ID
	 */
	@Override
	public Expression getTenantId() {
		return new StringValue(Func.toStr(SecureUtil.getTenantId(), TenantConstant.DEFAULT_TENANT_ID));
	}

	/**
	 * 获取租户字段名称
	 *
	 * @return 租户字段名称
	 */
	@Override
	public String getTenantIdColumn() {
		return properties.getColumn();
	}

	/**
	 * 过滤租户表
	 *
	 * @param tableName 表名
	 * @return 是否进行过滤
	 */
	@Override
	public boolean doTableFilter(String tableName) {
		return !(
			(
				(properties.getTables().size() > 0 && properties.getTables().contains(tableName))
					|| properties.getBladeTables().contains(tableName)
			)
				&& StringUtil.isNotBlank(SecureUtil.getTenantId())
		);
	}
}
