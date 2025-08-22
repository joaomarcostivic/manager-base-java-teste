package com.tivic.manager.fta;

public class ItensSaidaFrete {

	private int cdDocumentoEntrada;
	private int cdProdutoServico;
	private int cdEmpresa;
	private int cdFrete;
	private float qtTransportada;

	public ItensSaidaFrete(int cdDocumentoEntrada,
			int cdProdutoServico,
			int cdEmpresa,
			int cdFrete,
			float qtTransportada){
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setCdFrete(cdFrete);
		setQtTransportada(qtTransportada);
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
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
	public void setCdFrete(int cdFrete){
		this.cdFrete=cdFrete;
	}
	public int getCdFrete(){
		return this.cdFrete;
	}
	public void setQtTransportada(float qtTransportada){
		this.qtTransportada=qtTransportada;
	}
	public float getQtTransportada(){
		return this.qtTransportada;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdFrete: " +  getCdFrete();
		valueToString += ", qtTransportada: " +  getQtTransportada();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ItensSaidaFrete(getCdDocumentoEntrada(),
			getCdProdutoServico(),
			getCdEmpresa(),
			getCdFrete(),
			getQtTransportada());
	}

}