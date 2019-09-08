package com.cs.tenant;


import com.cs.core.tool.utils.RandomType;
import com.cs.core.tool.utils.StringUtil;

/**
 * blade租户id生成器
 *
 * @blame csz
 */
public class BlockTenantId implements TenantId {
	@Override
	public String generate() {
		return StringUtil.random(6, RandomType.INT);
	}
}
