package com.idgovern.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;


/**
 * Utility class for JSON serialization and deserialization.
 *
 * <p>
 * Provides simplified methods to convert Java objects to JSON strings and
 * JSON strings back to Java objects using Jackson {@link ObjectMapper}.
 * Handles common serialization configurations such as ignoring nulls and unknown properties.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>Serialization of empty beans is allowed without exceptions.</li>
 *     <li>Unknown JSON fields are ignored during deserialization.</li>
 *     <li>Non-empty fields only are serialized.</li>
 *     <li>All exceptions during conversion are logged and null is returned.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-17 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class JsonMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // Ignore unknown JSON properties
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Set up filters to ignore unknown IDs
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));

        // Include only non-empty fields
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }


    /**
     * Serialize a Java object to a JSON string.
     *
     * @param src the object to serialize
     * @param <T> the type of the object
     * @return JSON string representation of the object, or null if serialization fails
     */
    public static <T> String obj2String(T src) {
        if (src == null) {
            return null;
        }

        try {
            return src instanceof String ? (String) src : objectMapper.writeValueAsString(src);
        } catch (Exception e) {
            log.warn("Failed to serialize object to JSON string, error: {}", e);
            return null;
        }
    }


    /**
     * Deserialize a JSON string to a Java object.
     *
     * @param src           the JSON string
     * @param typeReference the type reference of the target object
     * @param <T>           the type of the target object
     * @return the deserialized object, or null if deserialization fails
     */
    public static <T> T string2Obj(String src, TypeReference<T> typeReference) {
        if (src == null || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? src : objectMapper.readValue(src, typeReference));
        } catch (Exception e) {
            log.warn("Failed to deserialize JSON string to object. JSON: {}, Type: {}, error: {}",
                    src, typeReference.getType(), e);
            return null;
        }
    }
}
