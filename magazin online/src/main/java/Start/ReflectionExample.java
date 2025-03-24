package Start;

import Model.Client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionExample {

    public static String[] retrieveProperties(Object object) {
        List<String> propertyNames = new ArrayList<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(object);
                String property = field.getName() + "=" + value;

                propertyNames.add(field.getName());
                System.out.println(property);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return propertyNames.toArray(new String[0]);
    }

    public static void main(String[] args) {
        Client client = new Client(1, "John Doe", "john@example.com", "123456789");
        String[] properties = retrieveProperties(client);

        for (String property : properties) {
            System.out.println(property);
        }
    }
}
