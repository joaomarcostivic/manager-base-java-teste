package com.tivic.manager.grl;

public class TipoDocumentacao {

	private int cdTipoDocumentacao;
	private String nmTipoDocumentacao;
	private String sgTipoDocumentacao;
	private int cdFormulario;
	private String idTipoDocumentacao;

	public TipoDocumentacao(){ }

	public TipoDocumentacao(int cdTipoDocumentacao,
			String nmTipoDocumentacao,
			String sgTipoDocumentacao,
			int cdFormulario,
			String idTipoDocumentacao){
		setCdTipoDocumentacao(cdTipoDocumentacao);
		setNmTipoDocumentacao(nmTipoDocumentacao);
		setSgTipoDocumentacao(sgTipoDocumentacao);
		setCdFormulario(cdFormulario);
		setIdTipoDocumentacao(idTipoDocumentacao);
	}
	public void setCdTipoDocumentacao(int cdTipoDocumentacao){
		this.cdTipoDocumentacao=cdTipoDocumentacao;
	}
	public int getCdTipoDocumentacao(){
		return this.cdTipoDocumentacao;
	}
	public void setNmTipoDocumentacao(String nmTipoDocumentacao){
		this.nmTipoDocumentacao=nmTipoDocumentacao;
	}
	public String getNmTipoDocumentacao(){
		return this.nmTipoDocumentacao;
	}
	public void setSgTipoDocumentacao(String sgTipoDocumentacao){
		this.sgTipoDocumentacao=sgTipoDocumentacao;
	}
	public String getSgTipoDocumentacao(){
		return this.sgTipoDocumentacao;
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public void setIdTipoDocumentacao(String idTipoDocumentacao){
		this.idTipoDocumentacao=idTipoDocumentacao;
	}
	public String getIdTipoDocumentacao(){
		return this.idTipoDocumentacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDocumentacao: " +  getCdTipoDocumentacao();
		valueToString += ", nmTipoDocumentacao: " +  getNmTipoDocumentacao();
		valueToString += ", sgTipoDocumentacao: " +  getSgTipoDocumentacao();
		valueToString += ", cdFormulario: " +  getCdFormulario();
		valueToString += ", idTipoDocumentacao: " +  getIdTipoDocumentacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoDocumentacao(getCdTipoDocumentacao(),
			getNmTipoDocumentacao(),
			getSgTipoDocumentacao(),
			getCdFormulario(),
			getIdTipoDocumentacao());
	}

}