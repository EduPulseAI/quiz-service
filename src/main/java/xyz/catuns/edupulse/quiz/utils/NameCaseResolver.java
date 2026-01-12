package xyz.catuns.edupulse.quiz.utils;

import java.util.Objects;
import java.util.function.Function;

public interface NameCaseResolver extends Function<String, String> {

    enum Casing {
        KEBAB((text) -> text.replaceAll("\\W+", "-")
            .toLowerCase()),
        SNAKE((text) -> text.replaceAll("\\W+", "_")
            .toLowerCase());

        private final NameCaseResolver resolver;

        Casing(NameCaseResolver resolver) {
            this.resolver = resolver;
        }
    }

    static NameCaseResolver resolveWith(Casing casing) {
        Objects.requireNonNull(casing);
        return casing.resolver;
    }

    static String toKebabCase(String name) {
        return resolveWith(Casing.KEBAB).apply(name);
    }

    static String toSnakeCase(String name) {
        return resolveWith(Casing.SNAKE).apply(name);
    }
}
