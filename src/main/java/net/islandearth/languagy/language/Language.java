package net.islandearth.languagy.language;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Language {
	ENGLISH("en_gb"),
	GERMAN("de_de"),
	FRENCH("fr_fr"),
	SPANISH("es_es"),
	ITALIAN("it_it"),
	DUTCH("nl_nl"),
	POLISH("pl_pl"),
	FINNISH("fi_fi"),
	SWEDISH("sv_se"),
	CZECH("cs_cz"),
	SLOVAKIAN("sk_sk");
	
	@Getter
	private String code;
}
