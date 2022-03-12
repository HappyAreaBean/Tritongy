package net.islandearth.languagy.api.language.key;

import org.jetbrains.annotations.NotNull;

public class TranslationKey {

    private final @NotNull String key;

    private TranslationKey(@NotNull String key) {
        this.key = key;
    }

    public @NotNull String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TranslationKey) {
            TranslationKey languageKey = (TranslationKey) obj;
            return languageKey.key.equals(this.key);
        }
        return false;
    }

    public static TranslationKey of(String key) {
        return new TranslationKey(key);
    }
}
