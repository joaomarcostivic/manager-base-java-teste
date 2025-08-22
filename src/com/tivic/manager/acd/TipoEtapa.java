package com.tivic.manager.acd;

public class TipoEtapa {

	private int cdEtapa;
	private String nmEtapa;
	private String idEtapa;
	private int lgRegular;
	private int lgEspecial;
	private int lgEja;
	private int cdEtapaPosterior;
	private String sgTipoEtapa;

	public TipoEtapa(){ }

	public TipoEtapa(int cdEtapa,
			String nmEtapa,
			String idEtapa,
			int lgRegular,
			int lgEspecial,
			int lgEja,
			int cdEtapaPosterior,
			String sgTipoEtapa){
		setCdEtapa(cdEtapa);
		setNmEtapa(nmEtapa);
		setIdEtapa(idEtapa);
		setLgRegular(lgRegular);
		setLgEspecial(lgEspecial);
		setLgEja(lgEja);
		setCdEtapaPosterior(cdEtapaPosterior);
		setSgTipoEtapa(sgTipoEtapa);
	}
	public void setCdEtapa(int cdEtapa){
		this.cdEtapa=cdEtapa;
	}
	public int getCdEtapa(){
		return this.cdEtapa;
	}
	public void setNmEtapa(String nmEtapa){
		this.nmEtapa=nmEtapa;
	}
	public String getNmEtapa(){
		return this.nmEtapa;
	}
	public void setIdEtapa(String idEtapa){
		this.idEtapa=idEtapa;
	}
	public String getIdEtapa(){
		return this.idEtapa;
	}
	public void setLgRegular(int lgRegular){
		this.lgRegular=lgRegular;
	}
	public int getLgRegular(){
		return this.lgRegular;
	}
	public void setLgEspecial(int lgEspecial){
		this.lgEspecial=lgEspecial;
	}
	public int getLgEspecial(){
		return this.lgEspecial;
	}
	public void setLgEja(int lgEja){
		this.lgEja=lgEja;
	}
	public int getLgEja(){
		return this.lgEja;
	}
	public void setCdEtapaPosterior(int cdEtapaPosterior){
		this.cdEtapaPosterior=cdEtapaPosterior;
	}
	public int getCdEtapaPosterior(){
		return this.cdEtapaPosterior;
	}
	public void setSgTipoEtapa(String sgTipoEtapa){
		this.sgTipoEtapa=sgTipoEtapa;
	}
	public String getSgTipoEtapa(){
		return this.sgTipoEtapa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEtapa: " +  getCdEtapa();
		valueToString += ", nmEtapa: " +  getNmEtapa();
		valueToString += ", idEtapa: " +  getIdEtapa();
		valueToString += ", lgRegular: " +  getLgRegular();
		valueToString += ", lgEspecial: " +  getLgEspecial();
		valueToString += ", lgEja: " +  getLgEja();
		valueToString += ", cdEtapaPosterior: " +  getCdEtapaPosterior();
		valueToString += ", sgTipoEtapa: " +  getSgTipoEtapa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoEtapa(getCdEtapa(),
			getNmEtapa(),
			getIdEtapa(),
			getLgRegular(),
			getLgEspecial(),
			getLgEja(),
			getCdEtapaPosterior(),
			getSgTipoEtapa());
	}

}