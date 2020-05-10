package ${package}.service.base;

import ${package}.vo.Page;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T> {

    T findById(Serializable id);

    void saveOrUpdate(T entity);

    Page<T> findPage(String hql, Object[] params, Page page);

    T findOne(String hql, Object params[]);

    List<T> find(String hql, Object params[]);
}
