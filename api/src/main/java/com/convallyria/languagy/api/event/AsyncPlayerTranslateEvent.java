package com.convallyria.languagy.api.event;

import com.convallyria.languagy.api.HookedPlugin;
import com.convallyria.languagy.api.language.translation.Translation;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AsyncPlayerTranslateEvent extends Event {

    private final Player player;
    private final Translation translation;
    private final HookedPlugin plugin;

    public AsyncPlayerTranslateEvent(Player player, Translation translation, HookedPlugin plugin) {
        super(true);
        this.player = player;
        this.translation = translation;
        this.plugin = plugin;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the translation.
     * @return the translation
     */
    public Translation getTranslation() {
        return translation;
    }

    public HookedPlugin getHookedPlugin() {
        return plugin;
    }
}