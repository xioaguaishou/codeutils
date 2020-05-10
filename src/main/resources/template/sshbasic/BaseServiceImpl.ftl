package ${package}.service.impl.base;

import ${package}.dao.base.BaseDao;
import ${package}.service.base.BaseService;
import ${package}.util.ReflectUtils;
import ${package}.vo.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

// 使用声明式事务,方便管理事务
//@Transactional
public class BaseServiceImpl<T> implements BaseService<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);


    private Class<?> clazz;

    public BaseServiceImpl() {
        try {
            this.clazz = ReflectUtils.getGenericClass(this.getClass());
            logger.debug("get generic class successful...");
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.debug("get generic class fail...", re.getStackTrace());
        }
    }

    @PostConstruct
    public void init() throws NoSuchFieldException, IllegalAccessException {
        try {
            ReflectUtils.InjectionProperty(this.clazz, this, "baseDao", "Dao");
            logger.debug("init method successful...");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            logger.debug("init method fail with noSuchFieldException...", e.getStackTrace());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.debug("init method fail with illegalAccessException...", e.getStackTrace());
        }
    }

    @Autowired
    private BaseDao<T> baseDao;

    @Override
    public T findById(Serializable id) {
        try {
            logger.debug("findById method successful...");
            return baseDao.findById(id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.debug("findById method fail...", re.getStackTrace());
        }
        return null;
    }

    @Override
    public void saveOrUpdate(T entity) {
        try {
            baseDao.saveOrUpdate(entity);
            logger.debug("saveOrUpdate method successful...");
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.debug("saveOrUpdate method fail...", re.getStackTrace());
        }
    }

    @Override
    public Page<T> findPage(String hql, Object[] params, Page page) {
        return baseDao.findPage(hql, params, page);
    }

    @Override
    public T findOne(String hql, Object[] params) {
        try {
            T t = baseDao.findOne(hql, params);
            logger.debug("findOne method successful...");
            return t;
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.debug("findPage method fail...", re.getStackTrace());
        }
        return null;
    }

    @Override
    public List<T> find(String hql, Object[] params) {
        return baseDao.find(hql, params);
    }
}
