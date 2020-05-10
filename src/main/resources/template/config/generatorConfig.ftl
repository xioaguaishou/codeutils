<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <!--mysql 连接数据库jar 这里选择自己本地位置-->
    <classPathEntry location="${driverJarPath}"/>
    <context id="testTables" targetRuntime="MyBatis3">
        <!-- JavaBean 实现 序列化 接口 -->
        <plugin type="org.mybatis.generator.plugins.SerializablePlugin"/>
        <!-- genenat entity时,生成toString -->
        <plugin type="org.mybatis.generator.plugins.ToStringPlugin"/>
        <!-- 开启支持内存分页   可生成 支持内存分布的方法及参数
        <plugin type="org.mybatis.generator.plugins.RowBoundsPlugin" />
        -->
        <!-- generate entity时，生成hashcode和equals方法-->
        <plugin type="org.mybatis.generator.plugins.EqualsHashCodePlugin"/>
        <commentGenerator>
            <!-- 是否去除自动生成的时间 -->
            <property name="suppressDate" value="true"/>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true" />
        </commentGenerator>
        <!--数据库连接的信息：驱动类、连接地址、用户名、密码 -->
        <jdbcConnection driverClass="${fDriverClass}"
                        connectionURL="${fUrl}" userId="${fUser}"
                        password="${fPwd}">
        </jdbcConnection>
        <!-- 默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer，为 true时把JDBC DECIMAL 和
           NUMERIC 类型解析为java.math.BigDecimal -->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>

        <!-- targetProject:生成PO类的位置 -->
        <javaModelGenerator targetPackage="${domainPackage}"
                            targetProject="${domainProject}">
            <!-- enableSubPackages:是否让schema作为包的后缀 -->
            <property name="enableSubPackages" value="false" />
            <!-- 从数据库返回的值被清理前后的空格 -->
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        <!-- targetProject:mapper映射文件生成的位置
           如果maven工程只是单独的一个工程，targetProject="src/main/java"
           若果maven工程是分模块的工程，targetProject="所属模块的名称"，例如：
           targetProject="ecps-manager-mapper"，下同-->
        <sqlMapGenerator targetPackage="${mapperPackage}"
                         targetProject="${mapperProject}">
            <!-- enableSubPackages:是否让schema作为包的后缀 -->
            <property name="enableSubPackages" value="false" />
        </sqlMapGenerator>
        <!-- targetPackage：mapper接口生成的位置 -->
        <javaClientGenerator type="XMLMAPPER"
                             targetPackage="${mapperXmlPackage}"
                             targetProject="${mapperXmlProject}">
            <!-- enableSubPackages:是否让schema作为包的后缀 -->
            <property name="enableSubPackages" value="false" />
        </javaClientGenerator>
        <!-- 指定数据库表 -->
        ${tableXmlFragment}
    </context>
</generatorConfiguration>