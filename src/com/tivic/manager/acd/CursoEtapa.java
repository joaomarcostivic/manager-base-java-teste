package com.tivic.manager.acd;

public class CursoEtapa {

	private int cdCursoEtapa;
	private int cdCurso;
	private int cdCursoEtapaPosterior;
	private String idCursoEtapa;
	private int cdEtapa;

	public CursoEtapa(){ }

	public CursoEtapa(int cdCursoEtapa,
			int cdCurso,
			int cdCursoEtapaPosterior,
			String idCursoEtapa,
			int cdEtapa){
		setCdCursoEtapa(cdCursoEtapa);
		setCdCurso(cdCurso);
		setCdCursoEtapaPosterior(cdCursoEtapaPosterior);
		setIdCursoEtapa(idCursoEtapa);
		setCdEtapa(cdEtapa);
	}
	public void setCdCursoEtapa(int cdCursoEtapa){
		this.cdCursoEtapa=cdCursoEtapa;
	}
	public int getCdCursoEtapa(){
		return this.cdCursoEtapa;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdCursoEtapaPosterior(int cdCursoEtapaPosterior){
		this.cdCursoEtapaPosterior=cdCursoEtapaPosterior;
	}
	public int getCdCursoEtapaPosterior(){
		return this.cdCursoEtapaPosterior;
	}
	public void setIdCursoEtapa(String idCursoEtapa){
		this.idCursoEtapa=idCursoEtapa;
	}
	public String getIdCursoEtapa(){
		return this.idCursoEtapa;
	}
	public void setCdEtapa(int cdEtapa){
		this.cdEtapa=cdEtapa;
	}
	public int getCdEtapa(){
		return this.cdEtapa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCursoEtapa: " +  getCdCursoEtapa();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdCursoEtapaPosterior: " +  getCdCursoEtapaPosterior();
		valueToString += ", idCursoEtapa: " +  getIdCursoEtapa();
		valueToString += ", cdEtapa: " +  getCdEtapa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CursoEtapa(getCdCursoEtapa(),
			getCdCurso(),
			getCdCursoEtapaPosterior(),
			getIdCursoEtapa(),
			getCdEtapa());
	}

}