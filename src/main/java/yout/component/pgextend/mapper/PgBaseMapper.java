package yout.component.pgextend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 针对PG的相关扩展方法
 *
 * @author yout0703
 */
public interface PgBaseMapper<T> extends BaseMapper<T> {

    /**
     * Upsert 方法。自动判断数据是否存在，存在更新，不存在插入。
     * <p>
     * 注意：
     * <li>1. 注意需要在确定 Upsert 的时候的主键，如果该表有多个唯一性主键，出现其他唯一性索引冲突会抛出异常。</li>
     * <li>2. 目前更新的策略是全量 Update，也就是说传入的是 null 也会被更新到数据库中。</li>
     * </p>
     *
     * @param entity 实体对象
     */
    void pgUpsert(T entity);

    /**
     * 重写了 updateById 方法，不忽略 null 值。
     * <p>
     * 说明：
     * pgUpsert 执行时会先根据唯一索引查询是否已经有数据，有则全量更新，没有则插入。
     * 但是 pgUpsert 只能查询单个唯一索引，当表结构存在多个唯一索引时，可能会发生冲突。
     * 此时如果依然想把 null 写入数据库，可以调用此方法。
     * 一般情况下，直接调用 pgUpsert 即可。
     *
     * @param entity 实体对象
     */
    void pgUpdateById(T entity);
}
