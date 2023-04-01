package com.convallyria.languagy.api.language.translation;

import com.convallyria.languagy.api.adventure.AdventurePlatform;
import com.convallyria.languagy.api.language.Language;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

public class Translation {

    private final Player player;
    private final Language language;
    private final List<String> translations;
    private final @Nullable AdventurePlatform adventure;

    private Translation(final Player player, final Language language, final List<String> translations, @Nullable AdventurePlatform adventure) {
        this.player = player;
        this.language = language;
        this.translations = translations;
        this.adventure = adventure;
    }

    @Deprecated
    public static Translation of(final Player player, final Language language, final String translation) {
        return of(player, language, translation, null);
    }

    public static Translation of(final Player player, final Language language, final String translation, @Nullable AdventurePlatform adventure) {
        return of(player, language, Lists.newArrayList(translation), adventure);
    }

    @Deprecated
    public static Translation of(final Player player, final Language language, final List<String> translations) {
        return of(player, language, translations, null);
    }

    public static Translation of(final Player player, final Language language, final List<String> translations, @Nullable AdventurePlatform adventure) {
        return new Translation(player, language, translations, adventure);
    }

    /**
     * Gets the language of this translation.
     * @return the translation's language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Gets the adventure implementation used to colour messages.
     * @return the {@link AdventurePlatform} implementation, or null if none
     */
    public @Nullable AdventurePlatform getAdventure() {
        return adventure;
    }

    /**
     * Sends a coloured message to the player.
     * If no adventure implementation has been provided, this will use {@link #sendLegacy()}.
     */
    public void send() {
        if (adventure == null) {
            sendLegacy();
            return;
        }

        colour().forEach(component -> adventure.adventure().player(player).sendMessage(component));
    }

    /**
     * Sends a legacy coloured message to the player.
     * @deprecated Legacy text is deprecated and Mojang have stated it will be removed in the future, use {@link #send()} instead.
     * @see #legacyColour()
     */
    @Deprecated
    public void sendLegacy() {
        legacyColour().forEach(player::sendMessage);
    }

    /**
     * Formats the translation with the provided values, using {@link String#format(String, Object...)}
     * @param values formatting values
     */
    public void format(Object... values) {
        translations.replaceAll(s -> String.format(Locale.ROOT, s, values));
    }

    /**
     * Gets the raw, unformatted text for this translation.
     * A translation may contain multiple elements.
     * @return raw, unformatted translation text
     */
    public List<String> getTranslations() {
        return translations;
    }

    /**
     * Gets a coloured {@link Component} representation, as formatted by {@link MiniMessage#deserialize(Object)}
     * If no adventure implementation has been provided, this will use the {@link LegacyComponentSerializer}.
     * @return component representation of coloured text
     */
    public List<Component> colour() {
        List<Component> colouredText = Lists.newArrayList();
        translations.forEach(translation -> {
            if (!translation.isEmpty()) {
                if (adventure != null) {
                    final Component component = adventure.miniMessage().deserialize(translation);
                    colouredText.add(component);
                } else {
                    colouredText.add(LegacyComponentSerializer.legacyAmpersand().deserialize(translation));
                }
            }
        });
        return colouredText;
    }

    /**
     * Gets a coloured string representation, as formatted by {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @deprecated Legacy text is deprecated and Mojang have stated it will be removed in the future, use {@link #colour()} instead.
     * @return string representation of coloured text
     */
    @Deprecated
    public List<String> legacyColour() {
        List<String> colouredText = Lists.newArrayList();
        translations.forEach(translation -> {
            if (!translation.isEmpty()) {
                colouredText.add(legacyColour(translation));
            }
        });
        return colouredText;
    }

    @Deprecated
    private String legacyColour(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
