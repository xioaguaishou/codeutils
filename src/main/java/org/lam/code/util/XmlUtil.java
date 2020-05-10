package org.lam.code.util;

import java.util.List;

public class XmlUtil {

    public static String getTableXmlFragment(List<String> tables) {
        StringBuilder sb = new StringBuilder();
        for (String tableName: tables) {
            String className = FreemarkerUtil.getBigName(tableName);

            sb.append("<table schema=\"\" tableName=\"" + tableName + "\" domainObjectName=\"" + className + "\"/>");
            sb.append(System.lineSeparator());
            sb.append("\t\t");
        }
        return sb.toString();
    }

}
