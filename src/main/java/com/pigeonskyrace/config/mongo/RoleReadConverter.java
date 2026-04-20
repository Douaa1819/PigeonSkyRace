package com.pigeonskyrace.config.mongo;

import com.pigeonskyrace.model.enums.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Maps legacy enum strings stored in MongoDB to the current {@link Role} values.
 */
@ReadingConverter
public class RoleReadConverter implements Converter<String, Role> {

    @Override
    public Role convert(String source) {
        if (source == null || source.isBlank()) {
            return Role.BREEDER;
        }
        return switch (source) {
            case "ORGANISATEUR" -> Role.ORGANIZER;
            case "ELEVEUR" -> Role.BREEDER;
            default -> {
                try {
                    yield Role.valueOf(source);
                } catch (IllegalArgumentException e) {
                    yield Role.BREEDER;
                }
            }
        };
    }
}
