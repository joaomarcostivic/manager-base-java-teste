package com.tivic.manager.prc;

public class Tribunal {

	private int cdTribunal;
	private String nmTribunal;
	private int tpSegmento;
	private String idTribunal;
	private String sgTribunal;
	
	public Tribunal(){ }
	
	public Tribunal(int cdTribunal,
			String nmTribunal,
			int tpSegmento,
			String idTribunal,
			String sgTribunal){
		setCdTribunal(cdTribunal);
		setNmTribunal(nmTribunal);
		setTpSegmento(tpSegmento);
		setIdTribunal(idTribunal);
		setSgTribunal(sgTribunal);
	}
	public void setCdTribunal(int cdTribunal){
		this.cdTribunal=cdTribunal;
	}
	public int getCdTribunal(){
		return this.cdTribunal;
	}
	public void setNmTribunal(String nmTribunal){
		this.nmTribunal=nmTribunal;
	}
	public String getNmTribunal(){
		return this.nmTribunal;
	}
	public void setTpSegmento(int tpSegmento){
		this.tpSegmento=tpSegmento;
	}
	public int getTpSegmento(){
		return this.tpSegmento;
	}
	public void setIdTribunal(String idTribunal){
		this.idTribunal=idTribunal;
	}
	public String getIdTribunal(){
		return this.idTribunal;
	}
	public void setSgTribunal(String sgTribunal){
		this.sgTribunal=sgTribunal;
	}
	public String getSgTribunal(){
		return this.sgTribunal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTribunal: " +  getCdTribunal();
		valueToString += ", nmTribunal: " +  getNmTribunal();
		valueToString += ", tpSegmento: " +  getTpSegmento();
		valueToString += ", idTribunal: " +  getIdTribunal();
		valueToString += ", sgTribunal: " +  getSgTribunal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Tribunal(getCdTribunal(),
			getNmTribunal(),
			getTpSegmento(),
			getIdTribunal(),
			getSgTribunal());
	}

}