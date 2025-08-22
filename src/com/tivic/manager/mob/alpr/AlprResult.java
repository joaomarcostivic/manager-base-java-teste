package com.tivic.manager.mob.alpr;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class AlprResult implements Serializable {
	private static final long serialVersionUID = -8749711486654791142L;
	
	private Integer imgWidth;
	private Integer imgHeight;
	private Long processingTimeMillis;
	private String plate;
	
	private List<Point> coordinates;	
	private List<Plate> candidates;

	public AlprResult(Integer imgWidth, Integer imgHeight, Long processingTimeMillis, String plate, List<Plate> candidates, List<Point> coordinates) {
		super();
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
		this.processingTimeMillis = processingTimeMillis;
		this.plate = plate;
		this.candidates = candidates;
		this.coordinates = coordinates;
	}
	
	public AlprResult() {
		super();
	}

	public Integer getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(Integer imgWidth) {
		this.imgWidth = imgWidth;
	}

	public Integer getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(Integer imgHeight) {
		this.imgHeight = imgHeight;
	}

	public Long getProcessingTimeMillis() {
		return processingTimeMillis;
	}

	public void setProcessingTimeMillis(Long processingTimeMillis) {
		this.processingTimeMillis = processingTimeMillis;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public List<Plate> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<Plate> results) {
		this.candidates = results;
	}
	
	public List<Point> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Point> coordinates) {
		this.coordinates = coordinates;
	}

	public Boolean contains(String plate) {
		Boolean contains = false;
		
		for(Plate result : getCandidates()) {
			if(result.getPlate().equals(plate)) {
				contains = true;
				break;
			}
		}
		
		return contains;
	}
	
	public String toString() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}
	
	public File toFile(String filename) throws IOException {
		String content = this.toString();
		File file = new File(filename);
		
		FileUtils.writeStringToFile(file, content);
		
		return file;
	}

}
