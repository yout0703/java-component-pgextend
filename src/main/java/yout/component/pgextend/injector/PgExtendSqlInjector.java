package yout.component.pgextend.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import yout.component.pgextend.method.PgUpsert;

import java.util.List;

/**
 * @author yout0703
 */
public class PgExtendSqlInjector extends DefaultSqlInjector {

    /**
     * 如果只需增加方法，保留MP自带方法 可以super.getMethodList() 再add
     *
     * @return
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new PgUpsert("pgUpsert"));
        return methodList;
    }
}
