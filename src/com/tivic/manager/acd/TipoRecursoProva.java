package com.tivic.manager.acd;

public class TipoRecursoProva {

	private int cdTipoRecursoProva;
	private String nmTipoRecurso;
	private String idTipoRecurso;

	public TipoRecursoProva(){ }

	public TipoRecursoProva(int cdTipoRecursoProva,
			String nmTipoRecurso,
			String idTipoRecurso){
		setCdTipoRecursoProva(cdTipoRecursoProva);
		setNmTipoRecurso(nmTipoRecurso);
		setIdTipoRecurso(idTipoRecurso);
	}
	public void setCdTipoRecursoProva(int cdTipoRecursoProva){
		this.cdTipoRecursoProva=cdTipoRecursoProva;
	}
	public int getCdTipoRecursoProva(){
		return this.cdTipoRecursoProva;
	}
	public void setNmTipoRecurso(String nmTipoRecurso){
		this.nmTipoRecurso=nmTipoRecurso;
	}
	public String getNmTipoRecurso(){
		return this.nmTipoRecurso;
	}
	public void setIdTipoRecurso(String idTipoRecurso){
		this.idTipoRecurso=idTipoRecurso;
	}
	public String getIdTipoRecurso(){
		return this.idTipoRecurso;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoRecursoProva: " +  getCdTipoRecursoProva();
		valueToString += ", nmTipoRecurso: " +  getNmTipoRecurso();
		valueToString += ", idTipoRecurso: " +  getIdTipoRecurso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoRecursoProva(getCdTipoRecursoProva(),
			getNmTipoRecurso(),
			getIdTipoRecurso());
	}

}