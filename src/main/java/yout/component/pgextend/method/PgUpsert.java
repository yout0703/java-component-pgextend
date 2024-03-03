package yout.component.pgextend.method;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.springframework.util.CollectionUtils;
import yout.component.pgextend.annotaion.UpsertIndexColumns;
import yout.component.pgextend.enums.PgMapperMethodEnum;
import yout.component.pgextend.mapper.PgBaseMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yout0703
 */
public class PgUpsert extends AbstractMethod {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PgUpsert.class);

    public PgUpsert(String methodName) {
        super(methodName);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        if (!PgBaseMapper.class.isAssignableFrom(mapperClass)) {
            return null;
        }
        PgMapperMethodEnum pgMapperMethodEnum = PgMapperMethodEnum.PG_UPSERT;
        final List<String> upsertIndexColumns = getUpsertIndexColumns(modelClass);
        if (CollectionUtils.isEmpty(upsertIndexColumns)) {
            log.error("""
                    {} 没有配置 @UpsertIndexColumns 属性。需要进行 Upsert 操作的实体必须配置 @UpsertIndexColumns。 比如：
                    @Data
                    @TableName
                    @UpsertIndexColumns({"source","source_id"})
                    public class UserDemo implements Serializable {
                        private String name;
                        private Integer age;
                        private String source;
                        private String sourceId;
                    }
                    """, modelClass);
            return null;
        }

        final String fieldSql = prepareFieldSql(tableInfo);
        final String valueSql = prepareValuesSql(tableInfo);
        final String conflictSql = "(" + String.join(",", upsertIndexColumns) + ")";
        final String updateSql = prepareUpdateSql(tableInfo, upsertIndexColumns);

        final String sqlResult = String.format(pgMapperMethodEnum.getSql(), tableInfo.getTableName(), fieldSql,
                valueSql, conflictSql, updateSql);
//        log.debug(sqlResult);
        SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sqlResult, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, pgMapperMethodEnum.getMethod(), sqlSource,
                new NoKeyGenerator(), null, null);
    }

    private List<String> getUpsertIndexColumns(Class<?> modelClass) {
        List<String> upsertColumns = new ArrayList<>();
        var annotations = modelClass.getAnnotation(UpsertIndexColumns.class);
        if (annotations != null) {
            upsertColumns = Arrays.asList(annotations.value());
        }
        return upsertColumns;
    }

    /**
     * description:生成新增语句VALUES之前的数据库字段
     *
     * @param tableInfo 表字段信息
     * @return sql
     */
    private String prepareFieldSql(TableInfo tableInfo) {
        StringBuilder fieldSql = new StringBuilder();
        // 拼接主键列,如果没有定义主键则
        if (tableInfo.havePK() && !IdType.AUTO.equals(tableInfo.getIdType())) {
            fieldSql.append(tableInfo.getKeyColumn()).append(",");
        }
        // 拼接其他字段列
        tableInfo.getFieldList().forEach(x -> fieldSql.append(x.getColumn())
                .append(","));
        // 去除最后一个","
        fieldSql.delete(fieldSql.length() - 1, fieldSql.length());
        // 前后添加"()"
        fieldSql.insert(0, "(");
        fieldSql.append(")");
        return fieldSql.toString();
    }

    /**
     * description:生成拼接insert语句 VALUES后的多个值
     *
     * @param tableInfo 实体表信息
     * @return 拼接好的字符串
     */
    private String prepareValuesSql(TableInfo tableInfo) {
        final StringBuilder valueSql = new StringBuilder();

        if (tableInfo.havePK() && !IdType.AUTO.equals(tableInfo.getIdType())) {
            valueSql.append("#{").append(tableInfo.getKeyProperty()).append("},");
        }
        tableInfo.getFieldList().forEach(x -> valueSql.append("#{").append(x.getProperty()).append("},"));
        valueSql.delete(valueSql.length() - 1, valueSql.length());
        // 前后添加"()"
        valueSql.insert(0, "(");
        valueSql.append(")");
        return valueSql.toString();
    }

    /**
     * description:生成拼接VALUES后的多个值
     *
     * @param tableInfo 实体表信息
     * @return 拼接好的字符串
     */
    private String prepareUpdateSql(TableInfo tableInfo, List<String> upsertIndexColumns) {
        final StringBuilder valueSql = new StringBuilder();
        tableInfo.getFieldList().forEach(x -> {
            if (!upsertIndexColumns.contains(x.getColumn())) {
                valueSql.append(x.getColumn())
                        .append("=EXCLUDED.").append(x.getColumn()).append(",");
            }
        });
        valueSql.delete(valueSql.length() - 1, valueSql.length());
        return valueSql.toString();
    }
}
