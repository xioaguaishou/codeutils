package org.lam.code.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreemarkerUtil {

    /**
     * 生成三层架构文件
     * @param packageName
     * @param templatePath
     * @param destinationPath
     * @throws IOException
     * @throws TemplateException
     */
    public static void buildMVC(String packageName, String templatePath, String destinationPath) throws IOException, TemplateException {
        // 匹配模板前缀
        File[] listFiles = getFiles(templatePath, ".ftl");
        if (listFiles != null && listFiles.length >0) {
            String[] arrangements = {"action", "service", "dao"};

            // Map<Map.Entry<String, String>, String>: Map<Map.Entry<"Action", "Action.ftl">, "path">

            Map<String, String> templateMap = getTemplateMap(listFiles);
            Map<Map.Entry<String, String>, String> templatePathMap = getTemplatePathMap(destinationPath, arrangements, templateMap);

            List<String> tables = DBUtil.getTables(); // 查询数据库的数据表
            Map<String, String> classNameMap = getClassName(tables);
            Map<String, String> attrNameMap = getAttrName(classNameMap);

            createJavaFile(templatePath, packageName, templatePathMap, classNameMap, attrNameMap);
        }
    }

    /**
     * 生成base文件
     * @param packageName
     * @param baseTemplatePath
     * @param destinationPath
     * @throws IOException
     * @throws TemplateException
     */
    public static void buildBase(String packageName, String baseTemplatePath, String destinationPath) throws IOException, TemplateException {
        // base匹配模板前缀
        File[] baseListFiles = getFiles(baseTemplatePath, ".ftl"); // 获取base模板文件列表
        if (baseListFiles != null && baseListFiles.length > 0) {
            Map<String, String> baseMap = new HashMap<>();
            baseMap.put("package", packageName); // 源数据
            Configuration config = getFreemarkerConfiguration(baseTemplatePath); // 创建freemarker配置对象, 并设置base模板加载路径
            createBaseJavaFile(destinationPath, baseMap, baseListFiles, config); // 创建baseXxx.java相关文件
        }
    }

    /**
     * 生成base / page / reflectUtils java文件
     * @param destinationPath
     * @param baseMap
     * @param baseListFiles
     * @param config
     * @throws IOException
     * @throws TemplateException
     */
    private static void createBaseJavaFile(String destinationPath, Map<String, String> baseMap, File[] baseListFiles, Configuration config) throws IOException, TemplateException {
        for (File templateFile: baseListFiles) {
            String templateName = templateFile.getName();
            Template template = config.getTemplate(templateName);
            File file = new File(destinationPath + "base");
            if (!file.exists())
                file.mkdirs();
            file = new File(file.getAbsolutePath() + "\\" + templateName.replace(".ftl", ".java"));

            Writer out = new FileWriter(file);
            template.process(baseMap, out);
        }
    }

    /**
     * 获取文件夹下符合过滤条件的文件列表
     * @param templatePath
     * @param filterReg
     * @return
     */
    private static File[] getFiles(String templatePath, String filterReg) {
        File templateFile = new File(templatePath);
        return templateFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(filterReg);
            }
        });
    }

    /**
     * 根据类名, 类名小写, 模板, 保存路径,生成三层架构java文件
     * @param templatePath
     * @param packageName
     * @param templatePathMap
     * @param classNameMap
     * @param attrNameMap
     * @throws IOException
     * @throws TemplateException
     */
    private static void createJavaFile(String templatePath, String packageName, Map<Map.Entry<String, String>, String> templatePathMap, Map<String, String> classNameMap, Map<String, String> attrNameMap) throws IOException, TemplateException {
        Configuration config = getFreemarkerConfiguration(templatePath);
        for (Map.Entry<String, String> centry: classNameMap.entrySet()) {
            for (Map.Entry<Map.Entry<String, String>, String> entry: templatePathMap.entrySet()) {
                String filePath = entry.getValue() + "\\" + centry.getValue() + entry.getKey().getKey() + ".java";
                String templateName = entry.getKey().getValue();
                String attrName = attrNameMap.get(centry.getKey());
//                System.out.println(filePath + " >> " + templateName + " >> " + attrName);

                Template template = config.getTemplate(templateName);

                HashMap<String, Object> map = new HashMap<>();
                map.put("package", packageName);
                map.put("className", centry.getValue());
                map.put("attrName", attrName);
//                System.out.println(centry.getValue() + ": " + attrName);
                File file = new File(entry.getValue());

                if (!file.exists())
                    file.mkdirs();

                Writer out = new FileWriter(new File(filePath));
                template.process(map, out);
            }
        }
    }

    /**
     * 创建freemarker配置类, 并设置加载模板路径
     * @param templatePath
     * @return
     * @throws IOException
     */
    public static Configuration getFreemarkerConfiguration(String templatePath) throws IOException {
        Configuration config = new Configuration();
        config.setDirectoryForTemplateLoading(new File(templatePath));
        return config;
    }

    /**
     * 获取模板生成文件路径Map<Map.Entry<"Action", "Action.ftl">, "path">
     * @param destinationPath
     * @param arrangements
     * @param templateMap
     * @return
     */
    private static Map<Map.Entry<String, String>, String> getTemplatePathMap(String destinationPath, String[] arrangements, Map<String, String> templateMap) {
        Map<Map.Entry<String, String>, String> templatePathMap = new HashMap<>();

        for (Map.Entry<String, String> entry: templateMap.entrySet()) {
            for (String arrangement : arrangements) {
                if (entry.getKey().toLowerCase().contains(arrangement.toLowerCase())) {
                    templatePathMap.put(entry, destinationPath + arrangement.toLowerCase());
                }
            }
        }
        return templatePathMap;
    }

    /**
     * 根据模板文件list生成模板Map<String, String>: Action.ftl ==> Action: Action.ftl
     * @param listFiles
     * @return
     */
    private static Map<String, String> getTemplateMap(File[] listFiles) {
        // {"Action": "Action.ftl"}
        Map<String, String> templateMap = new HashMap<>();

        for (File listFile : listFiles) {
//            String modelType = listFile.getName().substring(0, listFile.getName().indexOf("Template.ftl"));
//            templateMap.put(modelType, listFile.getName());
            String modelType = listFile.getName().substring(0, listFile.getName().indexOf(".ftl"));
            templateMap.put(modelType, listFile.getName());
        }
        return templateMap;
    }

    /**
     * 根据类名Map生成相应类的小写Map: User ==> user
     * @param classNameMap
     * @return
     */
    private static Map<String, String> getAttrName(Map<String, String> classNameMap) {
        Map<String, String> attrName = new HashMap<>();
        for (Map.Entry<String, String> entry: classNameMap.entrySet()) {
            String className = entry.getValue();
            // UserInfo userInfo
            attrName.put(entry.getKey(), className.substring(0, 1).toLowerCase() + className.substring(1));
        }
        return attrName;
    }

    /**
     * 根据tables生成相应的类名Map<tableName, className>: user_p ==> User
     * @param tables
     * @return
     */
    private static Map<String, String> getClassName(List<String> tables) {
        Map<String, String> className = new HashMap<>();
        for (String tableName: tables) {
            String bigName = getBigName(tableName);
            className.put(tableName, bigName.toString()); // user_info_p UserInfo
        }

        return className;
    }

    public static String getBigName(String tableName) {
        String[] tableNameArr = tableName.split("_");
        StringBuilder classNameSB = new StringBuilder();

        for (String ele: tableNameArr) {
            if (ele.length() > 1) {
                ele = ele.substring(0, 1).toUpperCase() + ele.substring(1);
                classNameSB.append(ele);
            }
        }
        return classNameSB.toString();
    }

}
