package com.tivic.manager.acd;

public class Recurso {

	private int cdRecurso;
	private int cdTipoRecurso;
	private int idRecurso;
	private int stRecurso;
	private String txtObservacao;

	public Recurso() { }

	public Recurso(int cdRecurso,
			int cdTipoRecurso,
			int idRecurso,
			int stRecurso,
			String txtObservacao) {
		setCdRecurso(cdRecurso);
		setCdTipoRecurso(cdTipoRecurso);
		setIdRecurso(idRecurso);
		setStRecurso(stRecurso);
		setTxtObservacao(txtObservacao);
	}
	public void setCdRecurso(int cdRecurso){
		this.cdRecurso=cdRecurso;
	}
	public int getCdRecurso(){
		return this.cdRecurso;
	}
	public void setCdTipoRecurso(int cdTipoRecurso){
		this.cdTipoRecurso=cdTipoRecurso;
	}
	public int getCdTipoRecurso(){
		return this.cdTipoRecurso;
	}
	public void setIdRecurso(int idRecurso){
		this.idRecurso=idRecurso;
	}
	public int getIdRecurso(){
		return this.idRecurso;
	}
	public void setStRecurso(int stRecurso){
		this.stRecurso=stRecurso;
	}
	public int getStRecurso(){
		return this.stRecurso;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRecurso: " +  getCdRecurso();
		valueToString += ", cdTipoRecurso: " +  getCdTipoRecurso();
		valueToString += ", idRecurso: " +  getIdRecurso();
		valueToString += ", stRecurso: " +  getStRecurso();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Recurso(getCdRecurso(),
			getCdTipoRecurso(),
			getIdRecurso(),
			getStRecurso(),
			getTxtObservacao());
	}

}