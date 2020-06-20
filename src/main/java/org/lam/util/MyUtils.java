package org.lam.util;

import com.alibaba.fastjson.JSONObject;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MyUtils {

    // TODO FileUtils
    /**
     * 文件下载
     * @param response
     * @param request
     * @param baos
     * @param returnName
     * @throws IOException
     */
    public static void download(HttpServletResponse response, HttpServletRequest request, ByteArrayOutputStream baos, String returnName) throws IOException {
        response.setContentType("application/octet-stream;charset=utf-8"); // 设置响应类型
        returnName = encodeDownloadFilename(request, returnName); // 返回文件名
        response.addHeader("Content-Disposition", "attachment;filename=" + returnName); // 设置响应头
        response.setContentLength(baos.size()); // 设置响应长度

        ServletOutputStream os = response.getOutputStream(); // 获取输出流
        baos.writeTo(os); // 内容写入输出流
        baos.close(); // 关闭流
        os.flush(); // 输出数据
    }


    private static String encodeDownloadFilename(HttpServletRequest request, String filename) throws IOException {
        String agent = request.getHeader("user-agent");
        if (agent.contains("Firefox")) {
            filename = "=?UTF-8?B?" + new BASE64Encoder().encode(filename.getBytes(StandardCharsets.UTF_8))+"?=";
        } else {
            filename = URLEncoder.encode(filename,"utf-8");
        }
        return filename;
    }

    // TODO IOUtils
    /**
     * fastjson实现深克隆, 无需实现Serializable序列化接口
     * @param t
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T> T cloneObjectByFastJson(T t) {
        return (T) JSONObject.parseObject(JSONObject.toJSONString(t), t.getClass());
    }

    /**
     * IO流实现深克隆, class和class的属性都需要实现Serializable序列化接口
     * @param t
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T> T cloneObjectByIO(T t) {
        T obj = null;
        if (t != null) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(t);
                try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                     ObjectInputStream ois = new ObjectInputStream(bais)) {
                    obj = (T) ois.readObject();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return obj;
    }

    // TODO ReflectUtils

    /**
     * 获取接口泛型
     * @param c
     * @return
     */
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

    /**
     *
     * @param clazz: xxService.class
     * @param object: xxxService
     * @param propName: "baseDao"
     * @param daoSuffix: "Dao"
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Deprecated
    public static void InjectionProperty(Class<?> clazz, Object object, String propName, String daoSuffix) throws NoSuchFieldException, IllegalAccessException {
        // 反射获取当前子类Service实例(UserService)的Dao引用(UserDao)
        String simpleName = clazz.getSimpleName(); // UserDao
        String daoClassName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + daoSuffix; // userDao
        Field daoFieldName = object.getClass().getDeclaredField(daoClassName);
        daoFieldName.setAccessible(true);
        Object obj = daoFieldName.get(object);

        // 将当前子类Service实例(UserService)的Dao引用(UserDao)注入到(替换)父类Service(BaseService)的baseDao属性中
        Class<?> aClass = object.getClass().getSuperclass();
        Field baseDaoFieldName = aClass.getDeclaredField(propName);
        baseDaoFieldName.setAccessible(true);
        baseDaoFieldName.set(object, obj);
    }

    // TODO POIUtils


}
