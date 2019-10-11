package net.islandearth.languagy.extension;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.djrapitops.plan.extension.CallEvents;
import com.djrapitops.plan.extension.DataExtension;
import com.djrapitops.plan.extension.ExtensionService;
import com.djrapitops.plan.extension.annotation.NumberProvider;
import com.djrapitops.plan.extension.annotation.PluginInfo;
import com.djrapitops.plan.extension.icon.Color;
import com.djrapitops.plan.extension.icon.Family;

import net.islandearth.languagy.LanguagyPlugin;
import net.islandearth.languagy.api.HookedPlugin;
import net.islandearth.languagy.language.Language;

@PluginInfo(
	    name = "Languagy",
	    iconName = "language",
	    iconFamily = Family.SOLID,
	    color = Color.NONE
	)
public class PlanExtension extends AbstractExtension implements DataExtension {

	private LanguagyPlugin plugin;
	
	public PlanExtension(LanguagyPlugin plugin) {
		this.plugin = plugin;
		try {
			plugin.getLogger().info("Registering extension...");
		    ExtensionService.getInstance().register(this);
		} catch (NoClassDefFoundError planIsNotInstalled) {
		    plugin.getLogger().info("Plan is not installed.");
		} catch (IllegalStateException planIsNotEnabled) {
			plugin.getLogger().severe("Unable to hook into plan - not enabled yet. Did it fail to load?");
		} catch (IllegalArgumentException dataExtensionImplementationIsInvalid) {
			plugin.getLogger().severe("Unable to hook into plan - extension was invalid.");
			dataExtensionImplementationIsInvalid.printStackTrace();
		}
	}
	
	@Override
	public String getName() {
		return "Plan";
	}

	@Override
	public boolean isAvailable() {
		return true;
	}
	
	@Override
    public CallEvents[] callExtensionMethodsOn() {
        return new CallEvents[]{
        		CallEvents.SERVER_PERIODICAL
        };
    }
	
	@NumberProvider(
            text = "Translated messages",
            description = "How many messages have been translated",
            iconName = "bookmark",
            iconColor = Color.BLUE,
            priority = 100,
            showInPlayerTable = false
    )
    public long messagesTranslated() {
		int count = 0;
        for (HookedPlugin hplugin : plugin.getHookedPlugins()) {
        	for (Language language : Language.values()) {
        		String lang = hplugin.getFallback().getAbsoluteFile().getParentFile().toString();
				File file = new File(lang + "/" + language.getCode() + ".yml");
				if (file.exists()) {
					YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
					count = count + config.getInt("translate.count");
				}
        	}
        }
        return count;
    }
}
