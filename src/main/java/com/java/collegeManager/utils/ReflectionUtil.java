package com.java.collegeManager.utils;

import com.java.collegeManager.model.Staff;
import javafx.scene.control.TableColumn;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

    /**
     * 递归获取类及其父类（直到Staff类）的所有属性名
     */
    public static <T> List<String> getPropertyNames(Class<T> clazz) {
        List<String> propertyNames = new ArrayList<>();

        // 递归获取所有父类的字段，直到 Staff 类
        Class<?> current = clazz;
        while (current != null && !current.equals(Object.class)) {
            Field[] fields = current.getDeclaredFields();

            for (Field field : fields) {
                // 跳过瞬态和静态字段
                if ((field.getModifiers() & java.lang.reflect.Modifier.TRANSIENT) == 0 &&
                        (field.getModifiers() & java.lang.reflect.Modifier.STATIC) == 0) {
                    propertyNames.add(field.getName());
                }
            }

            // 如果到达 Staff 类就停止
            if (current.equals(Staff.class)) {
                break;
            }

            // 继续向上查找父类
            current = current.getSuperclass();
        }

        return propertyNames;
    }

    /**
     * 创建表格列 - 使用反射获取属性值
     */
    public static <T, S> TableColumn<T, S> createColumnByProperty(
            String title, String propertyName, Class<T> clazz) {
        TableColumn<T, S> column = new TableColumn<>(title);

        // 设置单元格值工厂 - 通过反射调用getter方法
        column.setCellValueFactory(cellData -> {
            T item = cellData.getValue();
            if (item == null) return null;

            try {
                // 构建getter方法名
                String getterMethod = "get" +
                        Character.toUpperCase(propertyName.charAt(0)) +
                        propertyName.substring(1);

                // 使用实际对象的类来查找方法
                Method method = findGetterMethod(item.getClass(), getterMethod);

                if (method == null) {
                    // 尝试在父类中查找
                    method = findGetterMethod(clazz, getterMethod);
                }

                if (method != null) {
                    return (javafx.beans.value.ObservableValue<S>) method.invoke(item);
                }

                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });

        return column;
    }

    // 辅助方法：递归查找getter方法
    private static Method findGetterMethod(Class<?> clazz, String methodName) {
        try {
            return clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            // 尝试在父类中查找
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !superClass.equals(Object.class)) {
                return findGetterMethod(superClass, methodName);
            }
            return null;
        }
    }

    public static <T> List<T> createAndCallGetAll(String className) throws Exception {
        // 拼接完整类名
        String fullClassName = "com.java.collegeManager.service."+className + "Service";

        // 加载类
        Class<?> clazz = Class.forName(fullClassName);

        // 创建实例
        Object instance = clazz.getDeclaredConstructor().newInstance();

        // 构建方法名
        String methodName = "getAllStaff";

        // 获取方法
        Method method = clazz.getMethod(methodName);

        // 调用方法并返回结果
        return (List<T>) method.invoke(instance);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> invokeMethodSearch(String className, String searchParam) throws Exception {
        /// 1. 构建完整服务类名
        String fullServiceName = "com.java.collegeManager.service."+className + "Service";

        /// 2. 加载服务类
        Class<?> serviceClass;
        try {
            serviceClass = Class.forName(fullServiceName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("找不到服务类: " + fullServiceName, e);
        }

        /// 3. 创建服务实例（使用无参构造函数）
        Object serviceInstance;
        try {
            serviceInstance = serviceClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("服务类缺少无参构造函数: " + fullServiceName, e);
        }

        // 4. 获取目标方法
        Method getStaffMethod;
        try {
            /// 方法签名是 findStaff(String param)
            getStaffMethod = serviceClass.getMethod("findStaff", String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("服务类缺少方法: findStaff", e);
        }

        // 5. 调用方法
        try {
            return (List<T>) getStaffMethod.invoke(serviceInstance, searchParam);
        } catch (Exception e) {
            // 这里可以添加更具体的异常处理
            throw new RuntimeException("方法调用失败", e);
        }
    }

    public static <T> String getClassName(Class<T> handleClass) {
        return handleClass.getSimpleName();
    }

    // 辅助方法：首字母大写
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}