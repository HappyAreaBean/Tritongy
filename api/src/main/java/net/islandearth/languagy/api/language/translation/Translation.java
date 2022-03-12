package net.islandearth.languagy.api.language.translation;

import com.google.common.collect.Lists;
import net.islandearth.languagy.api.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Translation {

    private final Player player;
    private final Language language;
    private final List<String> translations;

    private Translation(final Player player, final Language language, final List<String> translations) {
        this.player = player;
        this.language = language;
        this.translations = translations;
    }

    public static Translation of(final Player player, final Language language, final String translation) {
        return new Translation(player, language, Lists.newArrayList(translation));
    }

    public static Translation of(final Player player, final Language language, final List<String> translations) {
        return new Translation(player, language, translations);
    }

    /**
     * Gets the language of this translation.
     * @return the translation's language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Sends a coloured message to the player.
     * @see #colour()
     */
    public void send() {
        colour().forEach(player::sendMessage);
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
     * Gets a coloured string representation, as formatted by {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @return string representation of coloured text
     */
    public List<String> colour() {
        List<String> colouredText = Lists.newArrayList();
        translations.forEach(translation -> {
            if (!translation.isEmpty()) colouredText.add(colour(translation));
        });
        return colouredText;
    }

    private String colour(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
