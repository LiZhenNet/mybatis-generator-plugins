package com.github.lizhen.mbg.plugin;

import com.github.lizhen.mbg.plugin.utiles.GeneratorUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;
import java.util.Optional;

/**
 * @author: lizhen
 * @since: 2019/12/9
 * @description:
 */
public class FluentPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    /**
     * Example 添加 Fluent Api
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        List<InnerClass> innerClasses = topLevelClass.getInnerClasses();
        Optional<InnerClass> innerClass = innerClasses.stream().filter(x -> "Criteria".equals(x.getType().getShortName())).findFirst();
        innerClass.ifPresent(
                clazz -> {
                    Field exampleField = new Field("example", topLevelClass.getType());
                    exampleField.setVisibility(JavaVisibility.PRIVATE);
                    clazz.addField(exampleField);
                    clazz.getMethods().stream().filter(Method::isConstructor).forEach(method -> {
                        method.addParameter(new Parameter(topLevelClass.getType(), "example"));
                        method.addBodyLine("this.example = example;");
                    });
                    Method method = GeneratorUtils.generateSimpleMethod("example", JavaVisibility.PUBLIC, topLevelClass.getType(), null);
                    method.addBodyLine("return this.example;");
                    clazz.addMethod(method);

                    Method end = GeneratorUtils.generateSimpleMethod("end", JavaVisibility.PUBLIC, topLevelClass.getType(), null);
                    end.addBodyLine("return this.example;");
                    clazz.addMethod(end);
                    Method begin = GeneratorUtils.generateSimpleMethod("begin", JavaVisibility.PUBLIC, clazz.getType(), null);
                    begin.setStatic(true);
                    begin.addBodyLine("return new " + topLevelClass.getType().getShortName() + "().createCriteria();");
                    topLevelClass.addMethod(begin);

                });
        topLevelClass.getMethods().stream().filter(x -> "createCriteriaInternal".equals(x.getName())).findFirst().ifPresent(
                method -> method.getBodyLines().set(0, "Criteria criteria = new Criteria(this);")
        );
        return true;
    }
}
