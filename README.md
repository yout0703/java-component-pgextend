# java-component-pgextend
Java 针对 Postgresql 的扩展，基于 mybatis 和 mybatis-plus
目前扩展了 upsert 方法，实现存在则更新，不存在则插入的功能。

# 使用方法
1. 在 pom.xml 文件中引入依赖
```xml
<dependency>
    <groupId>com.github.zhangchunsheng</groupId>
    <artifactId>java-component-pgextend</artifactId>
    <version>1.0.0</version>
</dependency>
```
2. 在定义数据库 POJO 的时候加上注解 @UpsertIndexColumns，告诉 mybatis-plus 更新这个表的时候如何判断重复数据。比如：
```java
@TableName(value = "xxx", schema = "public")
@UpsertIndexColumns({"id"}) // 通过ID判断是否重复
class Xxx {
    // fields
}

```
3. 在原先使用 BaseMapper 的地方替换成 PgBaseMapper，比如：
```java
@Mapper
public interface XxxMapper extends PgBaseMapper<Xxx> {
}
```
3. 可以在代码中使用 PgUpsert，比如：
```java
@Resource
private XxxMapper xxxMapper;
// ...
Xxx xxx = new Xxx();
// set fields
xxxMapper.pgUpsert(xxx);
```
