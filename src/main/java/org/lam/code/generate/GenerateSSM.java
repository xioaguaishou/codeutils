package org.lam.code.generate;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.lam.code.util.DBUtil;
import org.lam.code.util.FreemarkerUtil;
import org.lam.code.util.XmlUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateSSM {

    public static void main(String[] args) throws IOException, TemplateException {
        System.out.println("正在生成generatorConfig.xml配置文件, 请耐心等待...");
        // 当前项目路径
        String currentProjectPath = System.getProperty("user.dir");
        // generatorConfig.xml输出目录
        String writeOutPath = currentProjectPath + "\\src\\main\\resources\\";
        // generatorConfig.xml模板目录
        String templatePath = currentProjectPath + "\\src\\main\\resources\\template\\config";
        // mysql驱动包
        String mysqlJarPath = currentProjectPath + "\\src\\main\\resources\\mysqldrive\\mysql-connector-java-5.1.49.jar";
        // generatorConfig.xml模板名称
        String generatorConfigTemplate = "generatorConfig.ftl";

        Map<String, String> map = new HashMap<>();

        List<String> tables = DBUtil.getTables();
        String tableXmlFragment = XmlUtil.getTableXmlFragment(tables);
        System.out.println(tableXmlFragment);


//        如果maven工程只是单独的一个工程，targetProject="src/main/java"
//        如果maven工程是分模块的工程，targetProject="所属模块的名称"，例如：
//        targetProject="ecps-manager-mapper"，下同
        // 自定义生成文件保存目录
        map.put("domainPackage", "com.tiance.lam.pojo");
        map.put("domainProject", "src/main/java");
        map.put("mapperPackage", "com.tiance.lam.mapper");
        map.put("mapperProject", "src/main/java");
        map.put("mapperXmlPackage", "com.tiance.lam.mapper");
        map.put("mapperXmlProject", "src/main/java");

        map.put("fDriverClass", DBUtil.getDriver());
        map.put("fUrl", DBUtil.getUrl());
        map.put("fUser", DBUtil.getUser());
        map.put("fPwd", DBUtil.getPassword());
        map.put("driverJarPath", mysqlJarPath);
        map.put("tableXmlFragment", tableXmlFragment);

        Configuration config = FreemarkerUtil.getFreemarkerConfiguration(templatePath);
        Template template = config.getTemplate(generatorConfigTemplate);

        File file = new File(writeOutPath + "generatorConfig.xml");
        if (file.exists()) {
            file.delete();
        }
        Writer out = new FileWriter(new File(writeOutPath + "generatorConfig.xml"));
        template.process(map, out);

        System.out.println("配置文件生成完毕...");
    }


}
