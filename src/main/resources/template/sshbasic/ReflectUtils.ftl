package ${package}.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtils {

    public static Class<?> getGenericClass(Class<?> c) {
        Class<?> clazz = null;
        Type type = c.getGenericSuperclass();
        // 目的：把type接口转换成子接口
        if(type instanceof ParameterizedType){
            ParameterizedType ptype = (ParameterizedType) type;

            Type[] types = ptype.getActualTypeArguments();
            // 获取到 User.class
            clazz = (Class<?>) types[0];
        }
        return clazz;
    }

    public static void InjectionProperty(Class<?> clazz, Object object, String propName, String daoSuffix) throws NoSuchFieldException, IllegalAccessException {
        // 反射获取当前子类Service实例(UserService)的Dao引用(UserDao)
        String simpleName = clazz.getSimpleName(); // UserDao
        String daoClassName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + daoSuffix; // userDao
        Field daoFieldName = object.getClass().getDeclaredField(daoClassName);
        daoFieldName.setAccessible(true);
        Object obj = daoFieldName.get(object);

        // 将当前子类Service实例(UserService)的Dao引用(UserDao)注入到父类Service(BaseService)的baseDao属性中
        Class<?> aClass = object.getClass().getSuperclass();
        Field baseDaoFieldName = aClass.getDeclaredField(propName);
        baseDaoFieldName.setAccessible(true);
        baseDaoFieldName.set(object, obj);
    }

}
