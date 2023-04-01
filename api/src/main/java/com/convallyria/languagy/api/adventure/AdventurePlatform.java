package com.convallyria.languagy.api.adventure;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class AdventurePlatform {

    private final @NotNull MiniMessage miniMessage;
    private final @NotNull BukkitAudiences adventure;

    AdventurePlatform(@NotNull MiniMessage miniMessage, @NotNull BukkitAudiences adventure) {
        this.miniMessage = miniMessage;
        this.adventure = adventure;
    }

    /**
     * Creates a new adventure platform for a translator to use to colour messages.<br>
     * - <a href="https://docs.advntr.dev/minimessage/api.html">Creating a MiniMessage instance</a><br>
     * - <a href="https://docs.advntr.dev/platform/bukkit.html">Creating an adventure instance</a>
     * @param miniMessage the {@link MiniMessage} instance to colour messages
     * @param adventure the {@link BukkitAudiences} instance to send messages
     * @return the resulting {@link AdventurePlatform}
     */
    public static AdventurePlatform create(@NotNull MiniMessage miniMessage, @NotNull BukkitAudiences adventure) {
        return new AdventurePlatform(miniMessage, adventure);
    }

    public MiniMessage miniMessage() {
        return miniMessage;
    }

    public BukkitAudiences adventure() {
        return adventure;
    }
}
