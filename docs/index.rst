.. Languagy documentation master file, created by
   sphinx-quickstart on Fri Jul 26 11:54:37 2019.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Languagy
========

Languagy is a Spigot API for providing player-individual languages for plugins. **It supports over __90__ languages**.

Dependencies
------------

None! Languagy runs by itself and allowing other plugins to hook into it. You're probably here because of another plugin!

Table of Contents
-----------------
.. toctree::
   :maxdepth: 2
   :caption: Contents:
   
Developers
==========

This section is for developers looking to hook into the Languagy API.

Creating a translator instance
------------------------------

Languagy first requires a fallback language file to use when a language is not found/supported.

.. important::
	This will be the default translation file a user will see if their Language is supported. en_gb is commonly used (and recommended!)
Server Owners
=============

This section is for server owners looking to use Languagy with a plugin.
