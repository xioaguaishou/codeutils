package ${package}.dao.base.impl;

import ${package}.dao.base.BaseDao;
import ${package}.util.ReflectUtils;
import ${package}.vo.Page;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseDaoImpl<T> implements BaseDao<T> {
    private static final Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

    protected Class<?> clazz;

    public BaseDaoImpl() {
        this.clazz = ReflectUtils.getGenericClass(this.getClass());
    }

    @Autowired
    protected SessionFactory sessionFactory;

    protected Session getCurSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Override
    public T findById(Serializable id) {
        return (T) this.getCurSession().get(clazz, id);
    }

    @Override
    public void saveOrUpdate(T entity) {
        this.getCurSession().saveOrUpdate(entity);
    }

    @Override
    public Page<T> findPage(String hql, Object[] params, Page page) {
        try {
            Long totalRecord = 0L;
            List<T> results = new ArrayList<>(0);

            Query query = this.getCurSession().createQuery(hql);
            if (params != null && params.length >0) {
                for (int i = 0; i < params.length; i++) {
                    query.setParameter(i, params[i]);
                }
                List list = query.list();
                if (list != null && list.size() >0) {
                    totalRecord = (long) list.size();
                    // 可优化
                    //results = list.subList((page.getPageNo() - 1) * page.getPageSize(), list.size() > page.getPageSize() ? page.getPageSize() : list.size());
                }
            } else {
                totalRecord = getTotalRecord(clazz.getSimpleName());

    //            query.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
    //            query.setMaxResults(page.getPageSize());
    //            results = query.list();
            }

            query.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
            query.setMaxResults(page.getPageSize());

            results = query.list();

            page.setResults(results);
            page.setTotalRecord(totalRecord);
            logger.debug("findPage(String hql, Object[] params, Page page) successful...");
            return page;
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.debug("findPage(String hql, Object[] params, Page page) fail...", re.getStackTrace());
        }
        return null;
    }

    @Override
    public T findOne(String hql, Object[] params) {
        Query query = this.getCurSession().createQuery(hql);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
        List list = query.list();
        if (list != null && list.size() > 0)
            return (T) list.get(0);
        return null;
    }

    @Override
    public List<T> find(String hql, Object[] params) {
        Query query = this.getCurSession().createQuery(hql);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }

        return query.list();
    }

    protected Long getTotalRecord(String entityName) {
        String hql = "select count(*) from " + entityName;
        Query query = this.getCurSession().createQuery(hql);
        List<Long> list = query.list();
        if (list != null && list.size() > 0)
            return list.get(0);
        return 0L;
    }
}
