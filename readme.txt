GenerateSSH:
    1. 根据模板生成相应base java
    2. 根据模板生成相应mvc java
    3. 文件保存在自定义目录下

GenerateSSM
    1. 修改resources/db.properties
    2. 修改GenerateSSM:
        domainPackage, domainProject,
        mapperPackage, mapperProject,
        mapperXmlPackage, mapperXmlProject
    3. 生成mybatis-generate所需的generatorConfig.xml文件

    生成方式1:
    1. 配置maven项目逆向生成mybatis domain、dao(文件保存在当前项目自定义目录下)
        2.1 Edit Configurations ==> add
        2.2 maven ==> input name
        2.3 command line: mybatis-generator:generate -e
    2. 运行

    生成方式2:
    1. 打开idea左侧maven ==> Plugins
    2. mybatis-generateor ==> 双击 ==> myabtis-generator:generate
