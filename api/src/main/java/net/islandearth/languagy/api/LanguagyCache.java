package net.islandearth.languagy.api;

import java.util.ArrayList;
import java.util.List;

public class LanguagyCache {

    private static LanguagyCache instance;
    private List<HookedPlugin> hookedPlugins;

    public LanguagyCache() {
        LanguagyCache.instance = this;
        this.hookedPlugins = new ArrayList<>();
    }

    public List<HookedPlugin> getHookedPlugins() {
        return hookedPlugins;
    }

    public static LanguagyCache get() {
        return instance;
    }
}
