package co.dhan.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class DhanResponse {

    private String responseBody;

    /**
     * Easy for simple object to object conversion
     * @param clazz
     * @return
     * @param <T>
     */
    public <T> T convertToType(Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(responseBody,clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param typeReference is helpful for complex data-types like List<Order> as type-references that can't be passed as class type
     * @return T
     * @param <T>
     */
    public <T> T convertToType(TypeReference<T> typeReference) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(responseBody,typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return responseBody;
    }
}
