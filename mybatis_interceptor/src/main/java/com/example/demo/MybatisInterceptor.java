/**
 * author: silvern1990
 * created: 24.01.04 13:20
 */

package com.example.demo;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        System.out.println("----------------interceptor--------------");

        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];

        // INSERT, SELECT, UPDATE 등 쿼리 문 타입 확인
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        BoundSql boundSql = mappedStatement.getBoundSql(invocation.getArgs()[1]);

        // 쿼리문 확인
        String originalSql = boundSql.getSql();


        // 쿼리 내용을 변경하여 Statement 객체 재생성
        String modifiedSql = "select 'abcd'";

        BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), modifiedSql, boundSql.getParameterMappings(), boundSql.getParameterObject());

        MappedStatement newMappedStatement = newMappedStatement(mappedStatement, new BoundSqlSource(newBoundSql));

        // 쿼리가 변경된 Statement 객체로 대체
        invocation.getArgs()[0] = newMappedStatement;

        Object result = invocation.proceed();

        return result;
    }

    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSql) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSql, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        builder.keyProperty(join(ms.getKeyProperties()));
        builder.keyColumn(join(ms.getKeyColumns()));
        builder.databaseId(ms.getDatabaseId());
        builder.lang(ms.getLang());
        builder.resultOrdered(ms.isResultOrdered());
        builder.resultSets(join(ms.getResultSets()));
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    private String join(String[] array){
        if(array == null || array.length == 0){
            return null;
        }

        return String.join(",", array);
    }

    private static class BoundSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSource(BoundSql boundSql){
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject){
            return boundSql;
        }

    }

}

