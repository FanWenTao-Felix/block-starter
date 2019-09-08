package com.cs.tenant;

/**
 * 租户id生成器
 *
 * @blame csz
 */
public interface TenantId {

	/**
	 * 生成自定义租户id
	 * @return
	 */
	String generate();

}
