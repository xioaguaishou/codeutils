package ${package}.web.action.base;

import ${package}.dao.base.impl.BaseDaoImpl;
import ${package}.util.ReflectUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BaseAction<T> extends ActionSupport implements ParameterAware, RequestAware, SessionAware, ApplicationAware, ModelDriven<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseAction.class);

    private Class<?> clazz; // 当前action对应entity class

    protected T model; // 当前action对应entity
    protected String jsonList = null;
    protected String moduleName = null; // 当前action对应的页面模块

    protected Integer pageNo; // 页码
    protected Integer pageSize; // 分页

    protected Map<String, String[]> parameters;
    protected Map<String, Object> request;
    protected Map<String, Object> session;
    protected Map<String, Object> application;

    public BaseAction() {
        try {
            logger.debug("BaseAction constructor successful...");
            this.clazz = ReflectUtils.getGenericClass(this.getClass());
            model = (T) clazz.newInstance();
            logger.debug("BaseAction constructor successful...");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("BaseAction constructor fail...", e.getStackTrace());
        }
    }

    @Override
    public T getModel() {
        return this.model;
    }

    public String getJsonList() {
        return jsonList;
    }

    public void setJsonList(String jsonList) {
        this.jsonList = jsonList;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public void setApplication(Map<String, Object> application) {
        this.application = application;
    }

    @Override
    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    @Override
    public void setParameters(Map<String, String[]> parameters) {
        this.parameters = parameters;
    }

    private ActionContext getContext() {
        return ActionContext.getContext();
    }

    public void vsPush(Object o) {
        getContext().getValueStack().push(o);
    }

    public void ctPut(String key, Object value) {
        getContext().put(key, value);
    }

}
