package ${package}.dao.base;

import ${package}.vo.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDao<T> {

    T findById(Serializable id);

    void saveOrUpdate(T entity);

    Page<T> findPage(String hql, Object params[], Page page);

    T findOne(String hql, Object params[]);

    List<T> find(String hql, Object[] params);
}
