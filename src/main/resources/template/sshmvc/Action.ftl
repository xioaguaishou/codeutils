package ${package}.web.action.xxx;

import ${package}.domain.xxx.${className};
import ${package}.service.xxx.${className}Service;
import ${package}.vo.Page;
import ${package}.web.action.base.BaseAction;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.lang.RuntimeException;

@Controller
@Scope("prototype")
public class ${className}Action extends BaseAction<${className}> {
    private static final Logger logger = LoggerFactory.getLogger(${className}Action.class);

    @Autowired
    private ${className}Service ${attrName}Service;

    public String insert() {
        try {
            // 补全属性

            ${attrName}Service.saveOrUpdate(model);
            logger.debug("action insert successful...");
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.debug("action insert fail...", re.getStackTrace());
        }
        return "list";
    }

    public String tocreate() {
        // 添加关联属性

        return "create";
    }

    public String update() {
        try {
            // 补全属性

            ${attrName}Service.saveOrUpdate(model);

            logger.debug("action update successful...");
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.debug("action update fail...", re.getStackTrace());
        }
        return "list";
    }

    public String toupdate() {
        try {
            // 补全属性

            ${className} ${attrName} = ${attrName}Service.findById(model.getId());
            vsPush(${attrName});
            logger.debug("action toupdate successful...");
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.debug("action toupdate fail...", re.getStackTrace());
        }
        return "update";
    }

    public String get() {
        try {
            ${className} ${attrName} = ${attrName}Service.findById(model.getId());
            vsPush(${attrName});
            logger.debug("action get successful...");
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.debug("action get fail...", re.getStackTrace());
        }
        return "view";
    }

    public String delete() {
        try {
            // 根据ids批量删除
            String[] ids = parameters.get("ids");
            if (ids.length > 0) {
                for (String id: ids) {
                    ${attrName}Service.delete(id);
                }
            // 根据id删除一条数据
            } else {
                ${attrName}Service.delete(model.getId());
            }
            logger.debug("action delete successful...");
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.debug("action delete fail...", re.getStackTrace());
        }
        return "list";
    }

    public String list() {
        try {
            Page<${className}> page = new Page<>();
            if (pageNo != null && pageNo > 0) {
                page.setPageNo(pageNo);
            }
            if (pageSize != null && pageSize > 5) {
                page.setPageSize(pageSize);
            }
            String hql = "from ${className} where 1 = 1";

            page = ${attrName}Service.findPage(hql, params, page);

            page.setUrl("${attrName}Action_list.action");

            vsPush(page);

            logger.debug("action list successful...");
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.debug("action list fail...", re.getStackTrace());
        }
        return "list";
    }

}
