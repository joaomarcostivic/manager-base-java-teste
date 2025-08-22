package com.tivic.manager.mob.alpr;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OpenALPRResultBuilder implements IAlprResultBuilder {

	private Integer imgWidth;
	private Integer imgHeight;
	private Long processingTimeMillis;
	private String plate;
	
	private List<Point> coordinates;	
	private List<Plate> candidates;
	
	public OpenALPRResultBuilder(String src) {
		super();
		
		try {
			JSONObject json = new JSONObject(src);
			
			this.imgWidth = json.getInt("img_width");
			this.imgHeight = json.getInt("img_height");
			this.processingTimeMillis = json.getLong("processing_time_ms");
			this.candidates = new ArrayList<Plate>();
			this.coordinates = new ArrayList<Point>();
			
			if(json.getJSONArray("results").length() > 0) {
				this.plate = json.getJSONArray("results").getJSONObject(0).getString("plate").replaceAll("\n", "");
				
				JSONArray array = json.getJSONArray("results").getJSONObject(0).getJSONArray("candidates");
				for(int i = 0; i < array.length(); i++) {
					JSONObject candidate = (JSONObject) array.get(i);
					candidates.add(
							new Plate(
								candidate.getString("plate").replaceAll("\n", ""), 
								candidate.getDouble("confidence"), 
								json.getJSONArray("results").getJSONObject(0).getString("region"),
								candidate.getInt("matches_template")==1));
				}
				
				JSONArray coordinates = json.getJSONArray("results").getJSONObject(0).getJSONArray("coordinates");
				for(int i = 0; i < coordinates.length(); i++) {
					JSONObject coordinate = (JSONObject) coordinates.get(i);
					this.coordinates.add(new Point(coordinate.getInt("x"), coordinate.getInt("y")));
				}
			}
		} catch (JSONException e) {
			setImgWidth(0);
			setImgHeight(0);
			setProcessingTimeMillis(0l);
			setPlate(null);
			setCoordinates(new ArrayList<Point>());
			setCandidates(new ArrayList<Plate>());
		}
	}
	

	@Override
	public AlprResult build() {
		return new AlprResult(imgWidth, imgHeight, processingTimeMillis, plate, candidates, coordinates);
	}

	@Override
	public void setImgWidth(Integer imgWidth) {
		this.imgWidth = imgWidth;
	}

	@Override
	public void setImgHeight(Integer imgHeight) {
		this.imgHeight = imgHeight;
	}

	@Override
	public void setProcessingTimeMillis(Long processingTimeMillis) {
		this.processingTimeMillis = processingTimeMillis;
	}

	@Override
	public void setPlate(String plate) {
		this.plate = plate;
	}

	@Override
	public void setCoordinates(List<Point> coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public void setCandidates(List<Plate> candidates) {
		this.candidates = candidates;
	}	

}
