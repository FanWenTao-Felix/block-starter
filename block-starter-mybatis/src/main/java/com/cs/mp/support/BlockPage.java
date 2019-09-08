package com.cs.mp.support;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页模型
 *
 * @blame csz
 */
@Data
public class BlockPage<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 查询数据列表
	 */
	private List<T> records = Collections.emptyList();

	/**
	 * 总数
	 */
	private long total = 0;
	/**
	 * 每页显示条数，默认 10
	 */
	private long size = 10;

	/**
	 * 当前页
	 */
	private long current = 1;

	/**
	 * mybatis-plus分页模型转化
	 *
	 * @param page 分页实体类
	 * @return BlockPage<T>
	 */
	public static <T> BlockPage<T> of(IPage<T> page) {
		BlockPage<T> blockPage = new BlockPage<>();
		blockPage.setRecords(page.getRecords());
		blockPage.setTotal(page.getTotal());
		blockPage.setSize(page.getSize());
		blockPage.setCurrent(page.getCurrent());
		return blockPage;
	}

}
