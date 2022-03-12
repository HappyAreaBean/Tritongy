package com.convallyria.languagy.api.language.key;

import org.jetbrains.annotations.NotNull;

public class LanguageKey {

    private final @NotNull String code;

    private LanguageKey(@NotNull String code) {
        this.code = code;
    }

    public @NotNull String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LanguageKey) {
            LanguageKey languageKey = (LanguageKey) obj;
            return languageKey.code.equals(this.code);
        }
        return false;
    }

    public static LanguageKey of(String code) {
        return new LanguageKey(code);
    }
}
