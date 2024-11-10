package jsse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class JsonSerializerTest {

    @Test
    void testJsonSerializer() {
        // Given
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAge("30");
        person.setAddress("123 Main St");

        // When
        JsonSerializer jsonSerializer = new JsonSerializer();
        String json = "";
        try {
            json = jsonSerializer.convertToJson(person);
            System.out.println(json);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Then
        assertEquals("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"personAge\":\"30\"}", json);
    }

}

