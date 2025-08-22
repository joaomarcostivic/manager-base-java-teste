package com.tivic.manager.alm;

public class NivelLocal {

	private int cdNivelLocal;
	private String nmNivelLocal;
	private int lgArmazena;
	private int lgSetor;
	private int cdNivelLocalSuperior;
	
	public NivelLocal(){}
	
	public NivelLocal(int cdNivelLocal,
			String nmNivelLocal,
			int lgArmazena,
			int lgSetor,
			int cdNivelLocalSuperior){
		setCdNivelLocal(cdNivelLocal);
		setNmNivelLocal(nmNivelLocal);
		setLgArmazena(lgArmazena);
		setLgSetor(lgSetor);
		setCdNivelLocalSuperior(cdNivelLocalSuperior);
	}
	public void setCdNivelLocal(int cdNivelLocal){
		this.cdNivelLocal=cdNivelLocal;
	}
	public int getCdNivelLocal(){
		return this.cdNivelLocal;
	}
	public void setNmNivelLocal(String nmNivelLocal){
		this.nmNivelLocal=nmNivelLocal;
	}
	public String getNmNivelLocal(){
		return this.nmNivelLocal;
	}
	public void setLgArmazena(int lgArmazena){
		this.lgArmazena=lgArmazena;
	}
	public int getLgArmazena(){
		return this.lgArmazena;
	}
	public void setLgSetor(int lgSetor){
		this.lgSetor=lgSetor;
	}
	public int getLgSetor(){
		return this.lgSetor;
	}
	public void setCdNivelLocalSuperior(int cdNivelLocalSuperior){
		this.cdNivelLocalSuperior=cdNivelLocalSuperior;
	}
	public int getCdNivelLocalSuperior(){
		return this.cdNivelLocalSuperior;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNivelLocal: " +  getCdNivelLocal();
		valueToString += ", nmNivelLocal: " +  getNmNivelLocal();
		valueToString += ", lgArmazena: " +  getLgArmazena();
		valueToString += ", lgSetor: " +  getLgSetor();
		valueToString += ", cdNivelLocalSuperior: " +  getCdNivelLocalSuperior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NivelLocal(getCdNivelLocal(),
			getNmNivelLocal(),
			getLgArmazena(),
			getLgSetor(),
			getCdNivelLocalSuperior());
	}

}
