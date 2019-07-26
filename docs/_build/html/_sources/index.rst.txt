.. Languagy documentation master file, created by
   sphinx-quickstart on Fri Jul 26 11:54:37 2019.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Languagy
========

Languagy is a Spigot API for providing player-individual languages for plugins. **It supports over __90__ languages**.

In-game editor
^^^^^^^^^^^^^^
You can easily edit all translations of any plugin using the command */lgy edit* which will bring up a GUI.

.. warning::
	This only supports 1.14. Don't bother trying in 1.13 or less! You should just update anyway... there are some really cool features!

No lag
^^^^^^
Languagy is designed to have minimal impact on your server performance. It will likely never show up in your timings.

Automatically sync translations
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Languagy will automatically download custom translations for a plugin when a change is detected, keeping any translation files up-to-date.

.. note::
	Languagy won't download anything other than a .yml file which must also match translation file format for security reasons.

Dependencies
------------

None! Languagy runs by itself and allowing other plugins to hook into it. You're probably here because of another plugin!
   
Developers
==========

This section is for developers looking to hook into the Languagy API.

Creating a translator instance
------------------------------

Languagy first requires a fallback language file to use when a language is not found/supported.

.. tip::
	This will be the default translation file a user will see if their Language is supported. en_gb is commonly used (and recommended!)



Server Owners
=============

This section is for server owners looking to use Languagy with a plugin.

Usage
-----


.. toctree::
   :maxdepth: 2
   :caption: Contents: