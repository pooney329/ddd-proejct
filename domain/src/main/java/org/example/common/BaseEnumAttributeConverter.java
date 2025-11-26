package org.example.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.common.exception.ClientException;

import java.util.Arrays;

@Converter
public class BaseEnumAttributeConverter<T extends BaseEnum<?>, R> implements AttributeConverter<T, R> {

    private final Class<T> clazz;

    public BaseEnumAttributeConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public R convertToDatabaseColumn(T attribute) {

        return attribute != null ? (attribute.isUseCode() ? (R) attribute.getCode() : (R) attribute.getValue()) : null;
    }

    @Override
    public T convertToEntityAttribute(Object dbData) {
        if (dbData == null) {
            return null;
        }

        return Arrays.stream(clazz.getEnumConstants())
                .filter(e -> (e.isUseCode() ? e.getCode() : e.getValue()).equals(dbData))
                .findFirst()
                .orElseThrow(() -> new ClientException(
                        ExceptionType.ENUM_NOT_FOUND, String.format("%s: Unknown code:%s", clazz.getSimpleName(), dbData)));
    }
}
