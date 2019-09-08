package com.cs.mp.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs.core.secure.BlockUser;
import com.cs.core.secure.utils.SecureUtil;
import com.cs.core.tool.constant.BlockConstant;
import com.cs.core.tool.utils.BeanUtil;
import com.cs.core.tool.utils.DateUtil;
import com.cs.core.tool.utils.Func;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 业务封装基础类
 *
 * @param <M> mapper
 * @param <T> model
 *
 * @blame csz
 */
@Validated
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

	private Class<T> modelClass;

	public BaseServiceImpl() {
		Type type = this.getClass().getGenericSuperclass();
		this.modelClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[1];
	}

	@Override
	public boolean save(T entity) {
		BlockUser user = SecureUtil.getUser();
		if (user != null) {
			entity.setCreateUser(user.getUserId());
			entity.setCreateDept(Func.toLongList(user.getDeptId()).iterator().next());
			entity.setUpdateUser(user.getUserId());
		}
		Date now = DateUtil.now();
		entity.setCreateTime(now);
		entity.setUpdateTime(now);
		if (entity.getStatus() == null) {
			entity.setStatus(BlockConstant.DB_STATUS_NORMAL);
		}
		entity.setIsDeleted(BlockConstant.DB_NOT_DELETED);
		return super.save(entity);
	}

	@Override
	public boolean updateById(T entity) {
		BlockUser user = SecureUtil.getUser();
		if (user != null) {
			entity.setUpdateUser(user.getUserId());
		}
		entity.setUpdateTime(DateUtil.now());
		return super.updateById(entity);
	}

	@Override
	public boolean saveOrUpdate(T entity) {
		if (entity.getId() == null) {
			return this.save(entity);
		} else {
			return this.updateById(entity);
		}
	}

	@Override
	public boolean deleteLogic(@NotEmpty List<Long> ids) {
		BlockUser user = SecureUtil.getUser();
		List<T> list = new ArrayList<>();
		ids.forEach(id -> {
			T entity = BeanUtil.newInstance(modelClass);
			if (user != null) {
				entity.setUpdateUser(user.getUserId());
			}
			entity.setUpdateTime(DateUtil.now());
			entity.setId(id);
			list.add(entity);
		});
		return super.updateBatchById(list) && super.removeByIds(ids);
	}

}
