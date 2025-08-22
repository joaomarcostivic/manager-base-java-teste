package com.tivic.manager.grl;

public class OcorrenciaArquivo {

	private int cdOcorrencia;
	private int cdArquivo;
	private String txtObservacao;

	public OcorrenciaArquivo(int cdOcorrencia,
			int cdArquivo,
			String txtObservacao){
		setCdOcorrencia(cdOcorrencia);
		setCdArquivo(cdArquivo);
		setTxtObservacao(txtObservacao);
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OcorrenciaArquivo(getCdOcorrencia(),
			getCdArquivo(),
			getTxtObservacao());
	}

}
