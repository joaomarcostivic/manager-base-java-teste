package com.tivic.manager.alm;

public class DocSaidaReferencia {

	private int cdDocSaidaReferencia;
	private int cdProdutoServico;
	private int cdEmpresa;
	private int cdSaida;
	private int cdDocumentoSaida;
	private int cdLocalArmazenamento;
	private int cdItem;
	private int cdReferencia;

	public DocSaidaReferencia(int cdDocSaidaReferencia,
			int cdProdutoServico,
			int cdEmpresa,
			int cdSaida,
			int cdDocumentoSaida,
			int cdLocalArmazenamento,
			int cdItem,
			int cdReferencia){
		setCdDocSaidaReferencia(cdDocSaidaReferencia);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setCdSaida(cdSaida);
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdLocalArmazenamento(cdLocalArmazenamento);
		setCdItem(cdItem);
		setCdReferencia(cdReferencia);
	}
	public void setCdDocSaidaReferencia(int cdDocSaidaReferencia){
		this.cdDocSaidaReferencia=cdDocSaidaReferencia;
	}
	public int getCdDocSaidaReferencia(){
		return this.cdDocSaidaReferencia;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdSaida(int cdSaida){
		this.cdSaida=cdSaida;
	}
	public int getCdSaida(){
		return this.cdSaida;
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
	}
	public void setCdLocalArmazenamento(int cdLocalArmazenamento){
		this.cdLocalArmazenamento=cdLocalArmazenamento;
	}
	public int getCdLocalArmazenamento(){
		return this.cdLocalArmazenamento;
	}
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocSaidaReferencia: " +  getCdDocSaidaReferencia();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdSaida: " +  getCdSaida();
		valueToString += ", cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdLocalArmazenamento: " +  getCdLocalArmazenamento();
		valueToString += ", cdItem: " +  getCdItem();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocSaidaReferencia(getCdDocSaidaReferencia(),
			getCdProdutoServico(),
			getCdEmpresa(),
			getCdSaida(),
			getCdDocumentoSaida(),
			getCdLocalArmazenamento(),
			getCdItem(),
			getCdReferencia());
	}

}
