package com.cs.datascope.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.cs.core.secure.BlockUser;
import com.cs.core.secure.utils.SecureUtil;
import com.cs.core.tool.utils.ClassUtil;
import com.cs.core.tool.utils.SpringUtil;
import com.cs.core.tool.utils.StringUtil;
import com.cs.datascope.annotation.DataAuth;
import com.cs.datascope.handler.DataScopeHandler;
import com.cs.datascope.model.DataScopeModel;
import com.cs.datascope.props.DataScopeProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * mybatis 数据权限拦截器
 *
 * @blame csz
 */
@Slf4j
@RequiredArgsConstructor
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopeInterceptor extends AbstractSqlParserHandler implements Interceptor {

	private ConcurrentMap<String, DataAuth> dataAuthMap = new ConcurrentHashMap<>(8);

	private final DataScopeHandler dataScopeHandler;
	private final DataScopeProperties dataScopeProperties;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		//未取到用户则放行
		BlockUser blockUser = SecureUtil.getUser();
		if (blockUser == null) {
			return invocation.proceed();
		}

		StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		this.sqlParser(metaObject);

		//非SELECT操作放行
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		if (SqlCommandType.SELECT != mappedStatement.getSqlCommandType()
			|| StatementType.CALLABLE == mappedStatement.getStatementType()) {
			return invocation.proceed();
		}

		BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
		String originalSql = boundSql.getSql();

		//查找注解中包含DataAuth类型的参数
		DataAuth dataAuth = findDataAuthAnnotation(mappedStatement);

		//注解为空并且数据权限方法名未匹配到,则放行
		String mapperId = mappedStatement.getId();
		String className = mapperId.substring(0, mapperId.lastIndexOf(StringPool.DOT));
		String mapperName = ClassUtil.getShortName(className);
		String methodName = mapperId.substring(mapperId.lastIndexOf(StringPool.DOT) + 1);
		boolean mapperSkip = dataScopeProperties.getMapperKey().stream().noneMatch(methodName::contains)
			|| dataScopeProperties.getMapperExclude().stream().anyMatch(mapperName::contains);
		if (dataAuth == null && mapperSkip) {
			return invocation.proceed();
		}

		//创建数据权限模型
		DataScopeModel dataScope = new DataScopeModel();

		//若注解不为空,则配置注解项
		if (dataAuth != null) {
			dataScope.setResourceCode(dataAuth.code());
			dataScope.setScopeColumn(dataAuth.column());
			dataScope.setScopeType(dataAuth.type().getType());
			dataScope.setScopeField(dataAuth.field());
			dataScope.setScopeValue(dataAuth.value());
		}

		//获取数据权限规则对应的筛选Sql
		String sqlCondition = dataScopeHandler.sqlCondition(invocation, mapperId, dataScope, blockUser, originalSql);
		if (StringUtil.isBlank(sqlCondition)) {
			return invocation.proceed();
		} else {
			metaObject.setValue("delegate.boundSql.sql", sqlCondition);
			return invocation.proceed();
		}
	}

	/**
	 * 生成拦截对象的代理
	 *
	 * @param target 目标对象
	 * @return 代理对象
	 */
	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	/**
	 * mybatis配置的属性
	 *
	 * @param properties mybatis配置的属性
	 */
	@Override
	public void setProperties(Properties properties) {

	}

	/**
	 * 获取数据权限注解信息
	 *
	 * @param mappedStatement mappedStatement
	 * @return DataAuth
	 */
	private DataAuth findDataAuthAnnotation(MappedStatement mappedStatement) {
		String id = mappedStatement.getId();
		return dataAuthMap.computeIfAbsent(id, (key) -> {
			String className = key.substring(0, key.lastIndexOf(StringPool.DOT));
			String mapperBean = StringUtil.firstCharToLower(ClassUtil.getShortName(className));
			Object mapper = SpringUtil.getBean(mapperBean);
			String methodName = key.substring(key.lastIndexOf(StringPool.DOT) + 1);
			Class<?>[] interfaces = ClassUtil.getAllInterfaces(mapper);
			for (Class<?> mapperInterface : interfaces) {
				for (Method method : mapperInterface.getDeclaredMethods()) {
					if (methodName.equals(method.getName()) && method.isAnnotationPresent(DataAuth.class)) {
						return method.getAnnotation(DataAuth.class);
					}
				}
			}
			return null;
		});
	}

}
