Languagy
=============

Languagy is a Spigot API for providing player-individual languages for plugins.

Usage
-------
Simply drag and drop the plugin into your Plugins folder. Languages can be disabled/enabled in config.yml. Run /lgy test in-game with the language English, then run with another language to make sure the plugin is working correctly.

Supported Languages
-------
You can view a list of the supported langagues [here](https://gitlab.com/SamB440/languagy/blob/master/src/main/java/net/islandearth/languagy/language/Language.java). Different English languages have been added for the sake of different grammar and spellings.

Developer Resources
-------
API Usage:
Firstly create a default language file that the API will fallback to when the player's language is not found/supported. Generally, en_gb should be preferred.

Your language files should be located in a **single** folder, preferably named "lang". Fallback file should look like this (in your onEnable):
`File fallback = new File(getDataFolder() + "/lang/" + "en_gb.yml");`

Next, loop through the values of the Language enum using Language#values(). In your for loop, create a new language file by getting the language code Language#getCode():
```
File file = new File(getDataFolder() + "/lang/" + language.getCode() + ".yml");
if (!file.exists()) {
	try {
		file.createNewFile();
	} catch (IOException e1) {
		e1.printStackTrace();
	}
}
```
Load the configuration of that file using YamlConfiguration#loadConfiguration(file) and then add your defaults to the file, example:
```
FileConfiguration config = YamlConfiguration.loadConfiguration(file);
config.options().copyDefaults(true);
config.addDefault("warn", "&cUnfair advantages on our server are not allowed. Continue using unfair advantages and you will be banned from our server.");
config.addDefault("banned", "&c{player} &fhas been banned &a7 days for &b{cheat}.");
config.addDefault("blacklisted", "&cBlacklisted modifications");
```
Save the configuration using FileConfiguration#save(file).

Finally, create a new translator:
`new Translator(JavaPlugin, File)`
The first argument being your plugin (usually "this") and the second being the default language file, such as "en_gb".

Create a getter for the translator in your main class.

Now you can reference this translator from other classes using Plugin#getTranslator.

Getting a string for a player
------------

Getting a string for a player is simple;
`
plugin.getTranslator().getTranslationFor(arg0, arg1);
`
arg0 is your player, and arg1 is the path to the string in your language file, just like configuration files!
There is no need to translate chat colours, the API does that for you.

Bug Reporting/Support/Questions
------------

Issues should be filed by hitting the [issues](https://gitlab.com/SamB440/Languagy/issues) tab above. Please make sure you search your issue before opening a new ticket.

All opened tickets that are bug reports require a debug log that includes logs of the issue in question.

Resources
-------
Spigot: 

Development Builds: 

Latest release download: 

Latest dev download: 

Compiling
-------

Languagy builds on Java JDK 1.8 and uses [Maven](http://maven.apache.org/download.cgi) to manage dependencies. Lombok is also used and you will need to install it: http://www.projectlombok.org


