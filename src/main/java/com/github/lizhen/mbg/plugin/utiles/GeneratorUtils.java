package com.github.lizhen.mbg.plugin.utiles;

import org.mybatis.generator.api.dom.java.*;

/**
 * @author: lizhen
 * @since: 2019/12/9
 * @description:
 */
public class GeneratorUtils {

    public static void addFieldWithGetSet(TopLevelClass topLevelClass, String name, FullyQualifiedJavaType type, JavaVisibility visibility) {
        Field field = new Field(name, type);
        field.setVisibility(visibility);
        topLevelClass.addField(field);
        topLevelClass.addMethod(generateGetMethod(field));
        topLevelClass.addMethod(generateSetMethod(field));
    }

    public static Method generateGetMethod(Field field) {
        Method method = generateSimpleMethod("get" + capitalizesFirst(field.getName()), JavaVisibility.PUBLIC, field.getType(), null);
        method.addBodyLine("return this." + field.getName() + ";");
        return method;
    }

    public static Method generateSetMethod(Field field) {
        Method method = generateSimpleMethod("set" + capitalizesFirst(field.getName()), JavaVisibility.PUBLIC, null, new Parameter(field.getType(), field.getName()));
        method.addBodyLine("this." + field.getName() + " = " + field.getName() + ";");
        return method;
    }

    public static Method generateMethod(String methodName, JavaVisibility visibility, FullyQualifiedJavaType returnType, Parameter... parameters) {
        Method method = new Method(methodName);
        method.setVisibility(visibility);
        method.setReturnType(returnType);
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                method.addParameter(parameter);
            }
        }

        return method;
    }

    private static String capitalizesFirst(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static Method generateSimpleMethod(String methodName, JavaVisibility visibility, FullyQualifiedJavaType returnType, Parameter parameter) {
        Method method = new Method(methodName);
        method.setVisibility(visibility);
        method.setReturnType(returnType);
        if (parameter != null) {
            method.addParameter(parameter);
        }

        return method;
    }

}
