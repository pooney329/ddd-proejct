package org.example.common;



import java.util.Arrays;

public interface BaseEnum<E> {
    default Integer getValue() {
        return ((Enum<?>) this).ordinal();
    }

    default String getCode() {
        return ((Enum<?>) this).name();
    }

    String getText();

    default boolean isUseCode() {
        EnumType annotation = getDeclaringClass().getAnnotation(EnumType.class);
        return annotation == null || annotation.useCode();
    }

    default E[] getValues() {
        return getDeclaringClass().getEnumConstants();
    }

    @SuppressWarnings("unchecked")
    default Class<E> getDeclaringClass() {
        return (Class<E>) getClass();
    }

    static <E extends Enum<E> & BaseEnum<E>> E get(Class<E> enumClass, String code) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    static <E extends Enum<E> & BaseEnum<E>> E get(Class<E> enumClass, Integer value) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

    static <E extends Enum<E> & BaseEnum<E>> String getText(Class<E> enumClass, String code) {
        E enumValue = get(enumClass, code);
        return enumValue != null ? enumValue.getText() : null;
    }

    static <E extends Enum<E> & BaseEnum<E>> E getByText(Class<E> enumClass, String text) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getText().equalsIgnoreCase(text))
                .findFirst()
                .orElse(null);
    }

    static <E extends Enum<E> & BaseEnum<E>> E getByText(Class<E> enumClass, String text, E defaultValue) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getText().equalsIgnoreCase(text))
                .findFirst()
                .orElse(defaultValue);
    }

}
