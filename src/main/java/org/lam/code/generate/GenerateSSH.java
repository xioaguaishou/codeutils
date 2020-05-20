package org.lam.code.generate;

import freemarker.template.TemplateException;
import org.lam.code.util.FreemarkerUtil;

import java.io.IOException;

public class GenerateSSH {

    public static void main(String[] args) throws IOException, TemplateException {
        System.out.println("正在生成中, 请耐心等待...");
        String destinationPath = "F:\\tiance\\";
        String packageName = "com.tiance.lam";

        String currentProjectPath = System.getProperty("user.dir");
        String templatePath = currentProjectPath + "\\src\\main\\resources\\template\\sshmvc";
        String baseTemplatePath = currentProjectPath + "\\src\\main\\resources\\template\\sshbasic";


        // 生成base, page, reflectUtils文件
        FreemarkerUtil.buildBase(packageName, baseTemplatePath, destinationPath);

        // 生成三层架构文件:action, service, dao
        FreemarkerUtil.buildMVC(packageName, templatePath, destinationPath);

        System.out.println("生成完毕, 文件保存在 " + destinationPath.substring(0, destinationPath.length() - 1));
    }

}
