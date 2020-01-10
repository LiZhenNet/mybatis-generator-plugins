# MybatisGeneratorPlugin
自己常用的一些 mbg 的插件,包含 lombok支持、生成注释、Fluent Api的支持等等
## 使用
1. 升级Mybatis Generator 版本 到 1.3.7
2. 添加依赖

示例:
```xml
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.7</version>
                <configuration>
                    <configurationFile>src/main/resources/mybatis-generator-config.xml</configurationFile>
                    <verbose>true</verbose>
                    <overwrite>true</overwrite>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>com.github.lizhen</groupId>
                        <artifactId>mybatis-generator-plugins</artifactId>
                        <version>1.0.0</version>
                    </dependency>
                </dependencies>
            </plugin>

```

### LombokPlugin 添加lombok @Data注解
```
<plugin type="com.github.lizhen.mbg.plugin.LombokPlugin"/>
```
### CommentPlugin 添加注释
```
<plugin type="com.github.lizhen.mbg.plugin.CommentPlugin">
    <property name="author" value="lizhen"/> 
</plugin> 
````
> author 为类注释 @author 的值

### Limit 添加limit快捷方法
```
<plugin type="com.github.lizhen.mbg.plugin.LimitPlugin"/>
```
会在Criteria 生成三个方法:
>1.limit(Integer rows) 对应sql: limit rows  
>
>2.limit(Integer offset,Integer rows) 对应sql: limit offset rows    
>
>3.page(Integer page,Integer pageSize) 对应sql：limit (page - 1) * pageSize  pageSize  
>
### BatchInsertPlugin 批量插入
```
<plugin type="com.github.lizhen.mbg.plugin.BatchInsertPlugin"/>
```

### FluentPlugin Fluent Api 生成
```
<plugin type="com.github.lizhen.mbg.plugin.FluentPlugin"/>
```
效果: xxxxMapper.selectByExample(XxxxCriteria.begin().andXxxEqualTo(value).end());