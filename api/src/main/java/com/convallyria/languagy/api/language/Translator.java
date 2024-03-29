package com.convallyria.languagy.api.language;

import com.convallyria.languagy.api.HookedPlugin;
import com.convallyria.languagy.api.adventure.AdventurePlatform;
import com.convallyria.languagy.api.event.AsyncPlayerTranslateEvent;
import com.convallyria.languagy.api.language.key.LanguageKey;
import com.convallyria.languagy.api.language.key.TranslationKey;
import com.convallyria.languagy.api.language.translation.Translation;
import com.convallyria.languagy.api.service.LanguageWatchService;
import com.rexcantor64.triton.api.TritonAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.logging.Level;

public class Translator {

    // Default to true, then try to resolve in translator constructor, if it throws this is set to false.
    public static boolean RUNNING_FOLIA = true;

    private final Plugin plugin;
    private final @Nullable AdventurePlatform adventure;
    private final Language defaultLanguage;

    private File fallback;
    private boolean debug;
    private HookedPlugin hook;
    private LanguageWatchService watchService;

    private Translator(@NotNull Plugin plugin, @NotNull String folderName, @NotNull Language defaultLanguage, boolean debug, @Nullable AdventurePlatform adventure) {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
        } catch (final ReflectiveOperationException e) {
            RUNNING_FOLIA = false;
        }

        this.plugin = plugin;
        this.adventure = adventure;
        this.defaultLanguage = getDefaultLanguage(defaultLanguage);
        this.debug = debug;

        if (adventure != null) {
            debug("Adventure initialized");
        }

        File fallback = new File(plugin.getDataFolder()
                + File.separator
                + folderName
                + File.separator
                + defaultLanguage.getKey().getCode() + ".yml");
        setup(plugin, fallback);

        // Set up our file watch service
        try {
            debug("[Languagy] Setting up file watch service...");
            this.watchService = new LanguageWatchService(hook);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "[Languagy] Error whilst setting up file watch service", e);
        }
    }

    private Language getDefaultLanguage(Language defaultLanguage) {
        try {
            return Language.getFromKey(LanguageKey.of(TritonAPI.getInstance().getLanguageManager().getMainLanguage().getName().toLowerCase())).orElse(defaultLanguage);
        } catch (Throwable ex) {
            return defaultLanguage;
        }
    }

    /**
     * Properly cleans up and closes the file watch service.
     * This is very important, and you should place it in your onDisable method.
     */
    public void close() {
        watchService.shutdown();
    }

    /**
     * Create a new translator. Defaults the language to {@link Language#BRITISH_ENGLISH}
     * <br><br>
     * <b>Warning:</b> This behaviour may change in the future and change to the {@link Language#AMERICAN_ENGLISH} default.
     * <br><br>
     * It is important that you call {@link #close()} in your plugin's onDisable.
     * @deprecated Legacy text is deprecated and Mojang have stated it will be removed in the future, use {@link #of(Plugin, AdventurePlatform)} instead.
     * @param plugin your plugin instance
     * @see #of(Plugin, Language)
     * @see #of(Plugin, AdventurePlatform)
     * @see #of(Plugin, String, Language)
     * @return The {@link Translator} instance
     */
    @Deprecated
    public static Translator of(@NotNull Plugin plugin) {
        return of(plugin, Language.BRITISH_ENGLISH);
    }

    /**
     * Creates a new translator with adventure support.
     * @param plugin your plugin instance
     * @param adventure the adventure implementation to colour messages with
     * @see #of(Plugin)
     * @see #of(Plugin, Language, AdventurePlatform)
     * @return The {@link Translator} instance
     */
    public static Translator of(@NotNull Plugin plugin, @Nullable AdventurePlatform adventure) {
        return of(plugin, Language.BRITISH_ENGLISH, adventure);
    }

    /**
     * Create a new translator.
     * It is important that you call {@link #close()} in your plugin's onDisable.
     * @deprecated Legacy text is deprecated and Mojang have stated it will be removed in the future, use {@link #of(Plugin, Language, AdventurePlatform)} instead.
     * @param plugin your plugin instance
     * @param defaultLanguage the default language you wish to use
     * @see #of(Plugin)
     * @see #of(Plugin, String, Language)
     * @return The {@link Translator} instance
     */
    @Deprecated
    public static Translator of(@NotNull final Plugin plugin, @NotNull final Language defaultLanguage) {
        return of(plugin, "lang", defaultLanguage);
    }

    /**
     * Creates a new translator with adventure support.
     * @param plugin your plugin instance
     * @param defaultLanguage the default language you wish to use
     * @param adventure the adventure implementation to colour messages with
     * @see #of(Plugin, AdventurePlatform)
     * @see #of(Plugin, String, Language, AdventurePlatform)
     * @return The {@link Translator} instance
     */
    public static Translator of(@NotNull final Plugin plugin, @NotNull final Language defaultLanguage, @Nullable AdventurePlatform adventure) {
        return of(plugin, "lang", defaultLanguage, adventure);
    }

    /**
     * Create a new translator.
     * It is important that you call {@link #close()} in your plugin's onDisable.
     * @deprecated Legacy text is deprecated and Mojang have stated it will be removed in the future, use {@link #of(Plugin, String, Language, AdventurePlatform)} instead.
     * @param plugin your plugin instance
     * @param folderName the folder you wish to use for language files, this should match your resources folder
     * @param defaultLanguage the default language you wish to use
     * @see #of(Plugin)
     * @see #of(Plugin, String, Language)
     * @see #of(Plugin, String, Language, boolean)
     * @return The {@link Translator} instance
     */
    @Deprecated
    public static Translator of(@NotNull Plugin plugin, @NotNull String folderName, @NotNull Language defaultLanguage) {
        return of(plugin, folderName, defaultLanguage, false);
    }

    /**
     * Creates a new translator with adventure support.
     * @param plugin your plugin instance
     * @param folderName the folder you wish to use for language files, this should match your resources folder
     * @param defaultLanguage the default language you wish to use
     * @param adventure the adventure implementation to colour messages with
     * @return The {@link Translator} instance
     */
    public static Translator of(@NotNull Plugin plugin, @NotNull String folderName, @NotNull Language defaultLanguage, @Nullable AdventurePlatform adventure) {
        return of(plugin, folderName, defaultLanguage, false, adventure);
    }

    /**
     * Create a new translator.
     * @deprecated Legacy text is deprecated and Mojang have stated it will be removed in the future, use {@link #of(Plugin, String, Language, boolean, AdventurePlatform)} instead.
     * @param plugin your plugin instance
     * @param folderName the folder you wish to use for language files, this should match your resources folder
     * @param defaultLanguage the default language you wish to use
     * @param debug whether debug output should be shown or not
     * @see #of(Plugin)
     * @see #of(Plugin, Language)
     * @return The {@link Translator} instance
     */
    @Deprecated
    public static Translator of(@NotNull Plugin plugin, @NotNull String folderName, @NotNull Language defaultLanguage, boolean debug) {
        return of(plugin, folderName, defaultLanguage, debug, null);
    }

    /**
     * Creates a new translator with adventure support.
     * @param plugin your plugin instance
     * @param folderName the folder you wish to use for language files, this should match your resources folder
     * @param defaultLanguage the default language you wish to use
     * @param debug whether debug output should be shown or not
     * @param adventure the adventure implementation to colour messages with
     * @see #of(Plugin)
     * @see #of(Plugin, Language)
     * @return The {@link Translator} instance
     */
    public static Translator of(@NotNull Plugin plugin, @NotNull String folderName, @NotNull Language defaultLanguage, boolean debug, @Nullable AdventurePlatform adventure) {
        return new Translator(plugin, folderName, defaultLanguage, debug, adventure);
    }

    /**
     * Sets the fallback file
     * @param fallback fallback file
     * @return {@link Translator} instance
     */
    public Translator setFallback(File fallback) {
        this.fallback = fallback;
        return this;
    }

    @NotNull
    public HookedPlugin getHook() {
        return hook;
    }

    /**
     * Gets the translation for a player.
     * <br>
     * If the player's locale has no existing translation,
     *  then this will use the default language provided in the constructor.
     * <br>
     * This will also attempt to resolve the type within the config.
     * If it is a list, then that shall be used, otherwise it will be a normal string.
     * @param target the target player
     * @param key the translation key
     * @return a provided translation
     */
    public Translation getTranslationFor(@NotNull Player target, @NotNull TranslationKey key) {
        String lang = fallback.getAbsoluteFile().getParentFile().toString();
        final Language targetLanguage = getLanguage(target);
        File file = new File(lang + File.separator + targetLanguage.getKey().getCode() + ".yml");
        FileConfiguration config = file.exists()
                ? hook.getCachedLanguages().get(targetLanguage)
                : hook.getCachedLanguages().get(defaultLanguage);
        if (config.get(key.getKey()) == null) {
            if (hook.isDebug()) {
                String text = "[Languagy] Translation was requested, but path did not exist in '%s'! Try regenerating language files?";
                plugin.getLogger().warning(String.format(text, file.exists() ? targetLanguage : defaultLanguage));
            }
            return Translation.of(target, targetLanguage, TranslationKeys.NOT_FOUND.getKey(), adventure);
        }

        final Translation translation;
        if (config.isList(key.getKey())) {
            translation = Translation.of(target, targetLanguage, config.getStringList(key.getKey()), adventure);
        } else {
            translation = Translation.of(target, targetLanguage, config.getString(key.getKey()), adventure);
        }

        if (!RUNNING_FOLIA) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new AsyncPlayerTranslateEvent(target, translation, hook)));
        } else {
            // I don't care enough to hook into Folia's API and use their scheduler
            // Async events on Folia are deprecated anyway
            Bukkit.getPluginManager().callEvent(new AsyncPlayerTranslateEvent(target, translation, hook));
        }

        return translation;
    }

    public Language getLanguage(Player player) {
        return Language.getFromKey(LanguageKey.of(getLocale(player))).orElse(defaultLanguage);
    }

    /**
     * Gets the language config for the specified player.
     * This will use the default language provided if the player's locale has no translation file available.
     * @param target the target player
     * @return configuration file for the player's language
     */
    @Nullable
    public FileConfiguration getFileConfiguration(@NotNull Player target) {
        final Optional<Language> key = Language.getFromKey(LanguageKey.of(getLocale(target)));
        return key.map(language -> hook.getCachedLanguages().get(language)).orElse(hook.getCachedLanguages().get(defaultLanguage));
    }

    private void setup(Plugin plugin, File fallback) {
        if (!fallback.exists()) {
            plugin.getLogger().warning("[Languagy] Could not initiate new translator: Fallback does not exist!");
            return;
        }

        this.fallback = fallback;
        File folder = fallback.getAbsoluteFile().getParentFile();
        this.hook = new HookedPlugin(plugin, folder, fallback);

        if (hook.isDebug()) plugin.getLogger().info(folder.toString());
        for (Language language : Language.values()) {
            File file = new File(folder.toString() + File.separator + language.getKey().getCode() + ".yml");
            if (!file.exists() || !file.getName().contains(language.getKey().getCode())) {
                String reason = !file.exists() ? "Does not exist" : "File name is incorrect";
                if (hook.isDebug()) plugin.getLogger().warning("[Languagy] Language file could not be loaded: " + file.getName() + ". Reason: " + reason);
                continue;
            }
            hook.addCachedLanguage(language, YamlConfiguration.loadConfiguration(file));
            debug("[Languagy] Loaded language '" + language.getKey().getCode() + "'.");
        }
    }

    @NotNull
    public File getFallback() {
        return fallback;
    }

    public boolean isDebug() {
        return debug;
    }

    public Translator debug(boolean debug) {
        this.debug = debug;
        return this;
    }

    private void debug(String info) {
        if (isDebug()) plugin.getLogger().info("[Languagy] " + info);
    }

    private String getLocale(final Player player) {
        // must be lowercase to line up Language class
        return TritonAPI.getInstance().getPlayerManager().get(player.getUniqueId()).getLanguage().getName().toLowerCase();
    }

    private int getVersionNumber() {
        String[] split = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        return Integer.parseInt(split[1]);
    }
}
