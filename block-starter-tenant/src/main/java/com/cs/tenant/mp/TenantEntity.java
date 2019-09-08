package com.cs.tenant.mp;

import com.cs.mp.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 租户基础实体类
 *
 * @blame csz
 */
@Data
public class TenantEntity extends BaseEntity {

	/**
	 * 租户ID
	 */
	@ApiModelProperty(value = "租户ID")
	private String tenantId;

}
