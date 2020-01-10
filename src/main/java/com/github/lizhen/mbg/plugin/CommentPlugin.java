package com.github.lizhen.mbg.plugin;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author: lizhen
 * @since: 2019/12/9
 * @description:
 */
public class CommentPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    /**
     * 生成类注释
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generatorClassDoc(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * 生成mapper 注释
     * @param interfaze
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generatorClassDoc(interfaze, introspectedTable);
        return true;
    }

    /**
     * 生成字段注释
     * @param field
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String remarks = introspectedColumn.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            field.addJavaDocLine("/**");
            field.addJavaDocLine(" * " + remarks);
            field.addJavaDocLine(" */");
        }
        return true;
    }

    private void generatorClassDoc(JavaElement element, IntrospectedTable introspectedTable) {
        element.addJavaDocLine("/**");
        String author = StringUtils.isNotBlank(this.getProperties().getProperty("author")) ? this.getProperties().getProperty("author") : "MybatisGenerator";
        element.addJavaDocLine(" * @author: " + author);
        element.addJavaDocLine(" * @since: " + new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        element.addJavaDocLine(" * @description: " + (StringUtils.isNotBlank(introspectedTable.getRemarks()) ? introspectedTable.getRemarks() : ""));
        element.addJavaDocLine(" */");
    }
}
