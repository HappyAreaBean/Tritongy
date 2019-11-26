package net.islandearth.languagy.version;

import org.bukkit.Bukkit;

public class VersionChecker {

	private Version currentVersion;
	private Version latestVersion;
	
	public VersionChecker() {
		this.latestVersion = Version.values()[0];
	}
	
	/**
	 * Checks the current server version.
	 * @return true if server is up-to-date
	 */
	public boolean checkVersion() {
		for (Version version : Version.values()) {
			if (Bukkit.getVersion().contains(version.getId())) {
				this.currentVersion = version;
				return true;
			}
		}
		
		this.currentVersion = Version.UNSUPPORTED;
		return false;
	}

	public Version getCurrentVersion() {
		return currentVersion;
	}

	public Version getLatestVersion() {
		return latestVersion;
	}

	public enum Version {
		v1_14_R1("1.14"),
		v1_13_R2("1.13.2"),
		v1_13_R1("1.13.1"),
		v1_12_R2("1.12.2"),
		v1_11_R2("1.11.2"),
		v1_10_R2("1.10.2"),
		v1_9_R4("1.9.4"),
		v1_8_R9("1.8.9"),
		v1_8_R8("1.8.8"),
		UNSUPPORTED("Unsupported");

		private String id;

		Version(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}
	}
}
