package net.islandearth.languagy.language;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Language {
	BRITISH_ENGLISH("en_gb"),
	AMERICAN_ENGLISH("en_us"),
	AUSTRALIAN_ENGLISH("en_au"),
	CANADIAN_ENGLISH("en_ca"),
	NEW_ZEALAND_ENGLISH("en_nz"),
	GERMAN("de_de"),
	FRENCH("fr_fr"),
	SPANISH("es_es"),
	ITALIAN("it_it"),
	DUTCH("nl_nl"),
	POLISH("pl_pl"),
	FINNISH("fi_fi"),
	SWEDISH("sv_se");
	
	@Getter private String code;
}
