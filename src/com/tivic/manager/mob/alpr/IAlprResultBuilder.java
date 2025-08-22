package com.tivic.manager.mob.alpr;

import java.awt.Point;
import java.util.List;

public interface IAlprResultBuilder {
	
	public void setImgWidth(Integer imgWidth);
	public void setImgHeight(Integer imgHeight);
	public void setProcessingTimeMillis(Long processingTimeMillis);
	public void setPlate(String plate);
	
	public void setCoordinates(List<Point> coordinates);
	public void setCandidates(List<Plate> candidates);
	
	public AlprResult build();

}
