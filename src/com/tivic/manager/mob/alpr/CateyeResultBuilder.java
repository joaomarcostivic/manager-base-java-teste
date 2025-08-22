package com.tivic.manager.mob.alpr;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class CateyeResultBuilder implements IAlprResultBuilder {
	
	private Integer imgWidth;
	private Integer imgHeight;
	private Long processingTimeMillis;
	private String plate;
	
	private List<Point> coordinates;	
	private List<Plate> candidates;
	

	public CateyeResultBuilder(String src) throws JSONException {
		super();
		
		System.out.println("\t\tcateye response: "+src);
				
		JSONObject json = new JSONObject(src);		
		
		if(!json.getString("number").equals("-------"))
			setPlate(json.getString("number"));
				
		setCandidates(new ArrayList<Plate>());
		candidates.add(new Plate(json.getString("number"), json.getDouble("confidence"), null, false));
	}

	public CateyeResultBuilder() {
		super();
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
