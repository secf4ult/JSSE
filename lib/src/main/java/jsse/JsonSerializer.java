package jsse;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class JsonSerializer {
    public String convertToJson(Object object) throws Exception {
        try {
            checkIfSerializable(object);
            initializeObject(object);
            return getJsonString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkIfSerializable(Object object) {
        if (Objects.isNull(object)) {
            throw new RuntimeException("Object is not serializable");
        }

        Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(JsonSerializable.class)) {
            throw new RuntimeException("Object is not annotated with JsonSerializable");
        }
    }

    private void initializeObject(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Init.class)) {
                method.setAccessible(true);
                method.invoke(object);
            }
        }
    }

    private String getJsonString(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        Map<String, String> jsonElementsMap = new HashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(JsonElement.class)) {
                jsonElementsMap.put(getKey(field), (String) field.get(object));
            }
        }

        String jsonString = jsonElementsMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                .reduce((a, b) -> a + "," + b).orElse("");

        return "{" + jsonString + "}";
    }

    private String getKey(Field field) {
        if (field.isAnnotationPresent(JsonElement.class)) {
            return field.getAnnotation(JsonElement.class).key().equals("") ? field.getName()
                    : field.getAnnotation(JsonElement.class).key();
        }
        return field.getName();
    }
}
