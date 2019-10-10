package net.islandearth.languagy.extension;

import com.djrapitops.plan.extension.ExtensionService;

import net.islandearth.languagy.LanguagyPlugin;

public class PlanHook {

	public void registerExtensions(PlanExtension e) {
		LanguagyPlugin plugin = LanguagyPlugin.getPlugin();
		try {
			plugin.getLogger().info("Registering extension...");
		    ExtensionService.getInstance().register(e);
		} catch (NoClassDefFoundError planIsNotInstalled) {
		    plugin.getLogger().info("Plan is not installed.");
		} catch (IllegalStateException planIsNotEnabled) {
			plugin.getLogger().severe("Unable to hook into plan - not enabled yet. Did it fail to load?");
		} catch (IllegalArgumentException dataExtensionImplementationIsInvalid) {
			plugin.getLogger().severe("Unable to hook into plan - extension was invalid.");
			dataExtensionImplementationIsInvalid.printStackTrace();
		}
	}
}
