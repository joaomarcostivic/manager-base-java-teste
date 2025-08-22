package com.tivic.manager.mob.alpr;

import java.io.Serializable;

public class Plate implements Serializable {
	
	private static final long serialVersionUID = -7741055894109520609L;
	
	private String plate;
	private Double confidence;
	private Boolean matchesPattern;
	private String pattern;
	
	public Plate() {
		super();
	}

	public Plate(String plate, Double confidence, String pattern, Boolean matchesPattern) {
		super();
		this.plate = plate;
		this.confidence = confidence;
		this.pattern = pattern;
		this.matchesPattern = matchesPattern;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public Double getConfidence() {
		return confidence;
	}

	public void setConfidence(Double confidence) {
		this.confidence = confidence;
	}

	public Boolean getMatchesPattern() {
		return matchesPattern;
	}

	public void setMatchesPattern(Boolean matchesPattern) {
		this.matchesPattern = matchesPattern;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}



}
