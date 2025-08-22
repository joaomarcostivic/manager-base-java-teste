package com.tivic.manager.ptc;

public class DocumentacaoObrigatoria {

	private int cdTipoDocumento;
	private int cdTipoDocumentacao;

	public DocumentacaoObrigatoria(){ }

	public DocumentacaoObrigatoria(int cdTipoDocumento,
			int cdTipoDocumentacao){
		setCdTipoDocumento(cdTipoDocumento);
		setCdTipoDocumentacao(cdTipoDocumentacao);
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setCdTipoDocumentacao(int cdTipoDocumentacao){
		this.cdTipoDocumentacao=cdTipoDocumentacao;
	}
	public int getCdTipoDocumentacao(){
		return this.cdTipoDocumentacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", cdTipoDocumentacao: " +  getCdTipoDocumentacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocumentacaoObrigatoria(getCdTipoDocumento(),
			getCdTipoDocumentacao());
	}

}