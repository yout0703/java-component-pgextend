package yout.component.pgextend.enums;

/**
 * @description: 自定义MybatisPlus的PgBaseMapper方法枚举
 * @author: yout0703
 * @date: 2021-07-07
 */
public enum PgMapperMethodEnum {

    /**
     * 查询数据的第一条
     */
    PG_UPSERT("pgUpsert", "插入并在唯一性索引限制时更新", "<script>INSERT INTO %s %s VALUES %s ON CONFLICT %s DO UPDATE SET %s</script>");

    /**
     * 方法名
     */
    private final String method;

    /**
     * 方法描述
     */
    private final String desc;

    /**
     * 方法对应的sql语句
     */
    private final String sql;

    PgMapperMethodEnum(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }
}
