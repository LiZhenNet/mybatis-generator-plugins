package com.github.lizhen.mbg.plugin;

import com.gotokeep.store.common.mybatis.mbg.utiles.GeneratorUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * @author: lizhen
 * @since: 2019/12/9
 * @description:
 */
public class LimitPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * 生成limit 方法
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        PrimitiveTypeWrapper integerWrapper = PrimitiveTypeWrapper.getIntegerInstance();
        GeneratorUtils.addFieldWithGetSet(topLevelClass, "offset", integerWrapper, JavaVisibility.PROTECTED);
        GeneratorUtils.addFieldWithGetSet(topLevelClass, "rows", integerWrapper, JavaVisibility.PROTECTED);

        Method limit = GeneratorUtils.generateMethod(
                "limit",
                JavaVisibility.PUBLIC,
                topLevelClass.getType(),
                new Parameter(integerWrapper, "rows")
        );
        limit.addBodyLine("this.rows = rows;");
        limit.addBodyLine("return this;");
        topLevelClass.addMethod(limit);

        Method limitWithOffset = GeneratorUtils.generateMethod(
                "limit",
                JavaVisibility.PUBLIC,
                topLevelClass.getType(),
                new Parameter(integerWrapper, "offset"),
                new Parameter(integerWrapper, "rows")
        );
        limitWithOffset.addBodyLine("this.offset = offset;");
        limitWithOffset.addBodyLine("this.rows = rows;");
        limitWithOffset.addBodyLine("return this;");
        topLevelClass.addMethod(limitWithOffset);

        Method page = GeneratorUtils.generateMethod(
                "page",
                JavaVisibility.PUBLIC,
                topLevelClass.getType(),
                new Parameter(integerWrapper, "page"),
                new Parameter(integerWrapper, "pageSize")
        );

        page.addBodyLine("this.offset = (page - 1) * pageSize;");
        page.addBodyLine("this.rows = pageSize;");
        page.addBodyLine("return this;");
        topLevelClass.addMethod(page);
        return true;
    }

    /**
     * limit sql 生成
     *
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        this.generateLimitElement(element, introspectedTable);
        return true;
    }

    /**
     * limit sql 生成
     *
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        this.generateLimitElement(element, introspectedTable);
        return true;
    }

    public void generateLimitElement(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement ifLimitNotNullElement = new XmlElement("if");
        ifLimitNotNullElement.addAttribute(new Attribute("test", "rows != null"));

        XmlElement ifOffsetNotNullElement = new XmlElement("if");
        ifOffsetNotNullElement.addAttribute(new Attribute("test", "offset != null"));
        ifOffsetNotNullElement.addElement(new TextElement("limit ${offset}, ${rows}"));
        ifLimitNotNullElement.addElement(ifOffsetNotNullElement);

        XmlElement ifOffsetNullElement = new XmlElement("if");
        ifOffsetNullElement.addAttribute(new Attribute("test", "offset == null"));
        ifOffsetNullElement.addElement(new TextElement("limit ${rows}"));
        ifLimitNotNullElement.addElement(ifOffsetNullElement);

        element.addElement(ifLimitNotNullElement);
    }
}
