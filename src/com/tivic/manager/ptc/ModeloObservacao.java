package com.tivic.manager.ptc;

public class ModeloObservacao {

	private int cdObservacao;
	private int cdTipoDocumento;
	private String nmObservacao;
	private String txtObservacao;

	public ModeloObservacao(){ }

	public ModeloObservacao(int cdObservacao,
			int cdTipoDocumento,
			String nmObservacao,
			String txtObservacao){
		setCdObservacao(cdObservacao);
		setCdTipoDocumento(cdTipoDocumento);
		setNmObservacao(nmObservacao);
		setTxtObservacao(txtObservacao);
	}
	public void setCdObservacao(int cdObservacao){
		this.cdObservacao=cdObservacao;
	}
	public int getCdObservacao(){
		return this.cdObservacao;
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setNmObservacao(String nmObservacao){
		this.nmObservacao=nmObservacao;
	}
	public String getNmObservacao(){
		return this.nmObservacao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdObservacao: " +  getCdObservacao();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", nmObservacao: " +  getNmObservacao();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ModeloObservacao(getCdObservacao(),
			getCdTipoDocumento(),
			getNmObservacao(),
			getTxtObservacao());
	}

}
