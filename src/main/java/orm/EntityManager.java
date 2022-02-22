package orm;

import annotations.Column;
import annotations.Entity;
import annotations.Id;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityManager<E> implements DbContext<E>{
    private Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean persist(E entity) throws IllegalAccessException, SQLException {
        Field idColumn = getIdColumn(entity.getClass());
        idColumn.setAccessible(true);
        //Стойността на ИД колоната
        Object idValue = idColumn.get(entity);

        //Ако нямам ИД, ще създадем
        if (idValue == null || (long) idValue <= 0) {
            return doInsert(entity, idColumn);
            //В противен случай ъпдейтваме
        } else {
            //doUpdate(entity, idColumn);
        }


        return false;
    }

    private boolean doInsert(E entity, Field idColumn) throws SQLException, IllegalAccessException {
        String tableName = getTableName(entity.getClass());
        String tableFields = getColumnsWithoutId(entity.getClass());
        String tableValues = getColumnsValuesWithoutId(entity);

        String insertQuery = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, tableFields, tableValues);

        return connection.prepareStatement(insertQuery).execute();
    }

    private String getColumnsValuesWithoutId(E entity) throws IllegalAccessException {
        Class<?> aClass = entity.getClass();

        List<Field> fields = Arrays.stream(aClass.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .filter(f -> f.isAnnotationPresent(Column.class))
                .collect(Collectors.toList());

        List<String> values = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            Object o = field.get(entity);
            values.add(o.toString());
        }
        return String.join(", ", values);
    }

    private String getColumnsWithoutId(Class<?> aClass) {


        return Arrays.stream(aClass.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .filter(f -> f.isAnnotationPresent(Column.class))
                .map(f -> f.getAnnotationsByType(Column.class))
                .map(a -> a[0].name())
                .collect(Collectors.joining(", "));
    }

    private String getTableName(Class<?> aClass) {
        Entity[] annotationsByType = aClass.getAnnotationsByType(Entity.class);

        if (annotationsByType.length == 0) {
            throw new UnsupportedOperationException("Class must be Entity");
        }
        //Id[] annotationsByType2 = aClass.getAnnotationsByType(Id.class);
        return annotationsByType[0].name();
    }

    @Override
    public Iterable<E> find(Class<E> table) {
        return null;
    }

    @Override
    public Iterable<E> find(Class<E> table, String where) {
        return null;
    }

    @Override
    public E findFirst(Class<E> table) {
        return null;
    }

    @Override
    public E findFirst(Class<E> table, String where) {
        return null;
    }
    //ИД Колоната ще ни трябва, за да проверим дали в базата записа има ИД или не, за да можем да използваме Insert или Update
    private Field getIdColumn(Class<?> clazz) {
       return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Entity doesn't have primary key"));
        //------------ FOR CYCLE VARIANT-----------------//
        //Field[] declaredFields = clazz.getDeclaredFields();
//        for (Field declaredField : declaredFields) {
//            boolean annotationPresent = declaredField.isAnnotationPresent(Id.class);
//
//            if (annotationPresent) {
//                return declaredField;
//            }
//        }
//            throw new UnsupportedOperationException("Entity doesn't have primary key");

    };
}
