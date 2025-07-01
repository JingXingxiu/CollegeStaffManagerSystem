package com.java.collegeManager.utils;

import com.java.collegeManager.model.Staff;
import com.java.collegeManager.service.StaffManagerControllerService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TableViewUtil {
    private TableViewUtil(){
    }

    public static <T> void fillTableView(Class<T> handleClass, TableView<T> tableView) throws Exception {
        TableViewFiller.of(handleClass).fill(tableView);
    }

    public static <T> void searchStaff(Class<T> handleClass, TableView<T> tableView, String information) throws Exception {
        TableViewFiller.of(handleClass).searchStaffAndFillIt(tableView, information);
    }

    public static final class TableViewFiller<T> {
        private final Class<T> handleClass;
        private StaffManagerControllerService service = new StaffManagerControllerService();
        private final HashMap<String, String> map = new HashMap<>(Map.ofEntries(
                Map.entry("name", "姓名"),
                Map.entry("age", "年龄"),
                Map.entry("uniqueID", "ID"),
                Map.entry("entryDate", "入职时间"),
                Map.entry("gender", "性别"),
                Map.entry("title", "职称"),
                Map.entry("position", "职位"),
                Map.entry("laboratory", "实验室"),
                Map.entry("major", "专业"),
                Map.entry("faculty", "学院"),
                Map.entry("political", "政治面貌")
        ));

        private void searchStaffAndFillIt(TableView<T> tableView, String information) throws Exception {
            clearTableViewAll(tableView);
            String className = ReflectionUtil.getClassName(handleClass);
            List<T> staffs = ReflectionUtil.invokeMethodSearch(className, information);

            // 如果获取到数据，使用实际类型而非声明的handleClass
            Class<?> actualClass = !staffs.isEmpty() ? staffs.get(0).getClass() : handleClass;

            List<String> propertyNames = ReflectionUtil.getPropertyNames(actualClass);
            fillTable(propertyNames, tableView, staffs);
        }

        private void fillTable(List<String> propertyNames, TableView<T> tableView, List<T> data) {
            ObservableList<T> observableData = FXCollections.observableArrayList(data);
            tableView.setItems(observableData);

            for (String propertyName : propertyNames) {
                var tableColumn = ReflectionUtil.createColumnByProperty(map.get(propertyName), propertyName, handleClass);

                tableColumn.setCellValueFactory(cellData -> {
                    T item = cellData.getValue();
                    try {
                        // 使用实际运行时类获取方法
                        Class<?> actualClass = item != null ? item.getClass() : handleClass;
                        Method method = findGetterMethod(actualClass, propertyName);

                        if (method == null) {
                            throw new NoSuchMethodException("Getter method not found for: " + propertyName);
                        }

                        Object value = method.invoke(item);
                        if (value == null) {
                            return new SimpleObjectProperty<>("");
                        } else if (value instanceof Number) {
                            return new SimpleObjectProperty<>((Number) value);
                        } else if (value instanceof LocalDate) {
                            return new SimpleObjectProperty<>((LocalDate) value);
                        }
                        return new SimpleObjectProperty<>(value.toString());
                    } catch (Exception e) {
                        System.err.println("Error accessing property: " + propertyName);
                        e.printStackTrace();
                        return new SimpleObjectProperty<>("Error");
                    }
                });

                setupCellFactory(tableColumn, propertyName);

                tableColumn.setOnEditCommit(event -> {
                    T rowItem = event.getRowValue();
                    Object newValue = event.getNewValue();
                    updateProperty(rowItem, propertyName, newValue);
                });

                tableView.getColumns().add(tableColumn);
            }
        }

        // 添加查找getter方法的辅助方法
        private Method findGetterMethod(Class<?> clazz, String propertyName) {
            String[] methodNames = {
                    "get" + capitalize(propertyName),
                    "is" + capitalize(propertyName),
                    propertyName // Kotlin风格属性访问
            };

            for (String methodName : methodNames) {
                try {
                    return clazz.getMethod(methodName);
                } catch (NoSuchMethodException e) {
                    // 尝试在父类中查找
                    Class<?> superClass = clazz.getSuperclass();
                    if (superClass != null) {
                        Method superMethod = findGetterMethod(superClass, propertyName);
                        if (superMethod != null) {
                            return superMethod;
                        }
                    }
                }
            }
            return null;
        }

        private String capitalize(String str) {
            if (str == null || str.isEmpty()) {
                return str;
            }
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }

        private TableViewFiller(Class<T> handleClass) {
            this.handleClass = handleClass;
        }

        public static <T> TableViewFiller<T> of(Class<T> handleClass) {
            return new TableViewFiller<>(handleClass);
        }

        private void clearTableViewAll(TableView<T> tableView) {
            tableView.getColumns().clear();
            tableView.getItems().clear();
        }

        public void fill(TableView<T> tableView) throws Exception {
            clearTableViewAll(tableView);
            String className = ReflectionUtil.getClassName(handleClass);

            // 获取数据
            List<T> data = ReflectionUtil.createAndCallGetAll(className);

            // 如果获取到数据，使用实际类型而非声明的handleClass
            Class<?> actualClass = !data.isEmpty() ? data.get(0).getClass() : handleClass;

            List<String> propertyNames = ReflectionUtil.getPropertyNames(actualClass);
            fillTable(propertyNames, tableView, data);
        }

        private void updateProperty(T rowItem, String propertyName, Object newValue) {
            try {
                // 获取ID
                Method getIdMethod = findGetterMethod(rowItem.getClass(), "uniqueID");
                String id = (String) getIdMethod.invoke(rowItem);

                // 动态确定员工类型
                String employeeType = determineEmployeeType(rowItem);

                // 调用更新方法
                Method updateMethod = StaffManagerControllerService.class.getMethod(
                        "update" + employeeType, String.class, String.class, String.class
                );

                // 转换值为字符串格式
                String valueString = convertValueToString(newValue);

                // 执行更新
                updateMethod.invoke(service, id, propertyName, valueString);

            } catch (Exception e) {
                ShowAlert.show("错误", "更新失败", "属性更新时出错: ", Alert.AlertType.WARNING);
                e.printStackTrace();
            }
        }

        private String convertValueToString(Object value) {
            if (value == null) return "";
            // 添加其他类型转换逻辑
            return value.toString();
        }

        // 根据对象类型确定员工类别
        private String determineEmployeeType(T rowItem) {
            String className = rowItem.getClass().getSimpleName();

            // 更安全的映射
            switch (className) {
                case "Teacher":
                    return "Teacher";
                case "Researcher":
                    return "Researcher";
                case "AdministrationAndTeacher":
                    return "AdministrationAndTeacher";
                case "Administration":
                    return "Administration";
                default:
                    return "Staff";
            }
        }

        private void setupCellFactory(TableColumn<T, ?> column, String propertyName) {
            if (propertyName.equals("age")) {
                TableColumn<T, Integer> intColumn = (TableColumn<T, Integer>) column;
                intColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            } else if (propertyName.equals("entryDate")) {
                column.setEditable(false);
            } else {
                TableColumn<T, String> stringColumn = (TableColumn<T, String>) column;
                stringColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            }
        }
    }
}