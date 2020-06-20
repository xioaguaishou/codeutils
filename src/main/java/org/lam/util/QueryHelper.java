package org.lam.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DB查询工具
 */
public class QueryHelper {

    private String fromClause; // from语句
    private String whereClause = ""; // where语句
    private String orderByClause = ""; // OrderBy语句
    private String alias; // 表别名
	
	public QueryHelper(Class<?> clazz, String alias) {
		this.alias = alias + ".";
		this.fromClause = "FROM " + clazz.getSimpleName() + " " + alias;
	}
	
	private List<Object> parameters = new ArrayList<>();

    /**
     * 拼接where查询字符串
     * @param condition
     * @param params
     * @return
     */
	public QueryHelper addCondition(String condition, Object... params) {
		if (this.whereClause.length() == 0) {
            this.whereClause = " WHERE " + this.alias + condition;
		} else {
            this.whereClause += " AND " + this.alias + condition;
		}
		
		if (params != null) {
            this.parameters.addAll(Arrays.asList(params));
		}
		
		return this;
	}

    /**
     * 拼接where查询字符串
     * @param append
     * @param condition
     * @param params
     * @return
     */
	public QueryHelper addCondition(boolean append, String condition, Object... params) {
		if (append) {
			if (this.whereClause.length() == 0) {
                this.whereClause = " WHERE " + this.alias + condition;
			} else {
                this.whereClause += " AND " + this.alias + condition;
			}
			if (params != null) {
                this.parameters.addAll(Arrays.asList(params));
			}
		}

		return this;
	}

    /**
     * 拼接ORDER BY排序语句
     * @param orderName
     * @param asc
     * @return
     */
	public QueryHelper addOrderProperty(String orderName, boolean asc) {
		if (this.orderByClause.length() == 0) {
            this.orderByClause = " ORDER BY " + this.alias + orderName + " " + (asc? "ASC": "DESC");
		} else {
            this.orderByClause += ", " + this.alias + orderName + " " + (asc? "ASC": "DESC");
		}
		
		return this;
	}

    /**
     * 拼接ORDER BY排序语句
     * @param orderName
     * @param asc
     * @return
     */
	public QueryHelper addOrderProperty(boolean append, String orderName, boolean asc) {
		if (append) {
			if (this.orderByClause.length() == 0) {
                this.orderByClause = " ORDER BY " + this.alias + orderName + " " + (asc ? "ASC" : "DESC");
			} else {
                this.orderByClause += ", " + this.alias + orderName + " " + (asc ? "ASC" : "DESC");
			}
		}

		return this;
	}

	public String getQueryListHQL() {
		return this.fromClause + this.whereClause + this.orderByClause;
	}
	
	public String getCountHQL() {
		return "SELECT COUNT(*) " + this.fromClause + this.whereClause;
	}
	
	public List<Object> getParameters() {
		return this.parameters;
	}

	public static void main(String[] args) {
		QueryHelper qh = new QueryHelper(QueryHelper.class, "q");
		qh.addCondition("age = ?", 11)
		.addCondition("name = ?", "qewe")
		.addOrderProperty("name", false)
		.addOrderProperty("address", true);
		System.out.println(qh.getQueryListHQL());
		System.out.println(qh.getCountHQL());
		System.out.println(qh.getParameters());
	}
}
