package com.tivic.manager.fsc;

public class NotaFiscalItem {

	private int cdNotaFiscal;
	private int cdItem;
	private int cdDocumentoSaida;
	private int cdProdutoServico;
	private int cdEmpresa;
	private int cdDocumentoEntrada;
	private int cdNaturezaOperacao;
	private float qtTributario;
	private float vlUnitario;
	private String txtInformacaoAdicional;
	private int cdItemDocumento;
	private float vlAcrescimo;
	private float vlDesconto;
	
	public NotaFiscalItem(int cdNotaFiscal,
			int cdItem,
			int cdDocumentoSaida,
			int cdProdutoServico,
			int cdEmpresa,
			int cdDocumentoEntrada,
			int cdNaturezaOperacao,
			float qtTributario,
			float vlUnitario,
			String txtInformacaoAdicional,
			int cdItemDocumento,
			float vlAcrescimo,
			float vlDesconto){
		setCdNotaFiscal(cdNotaFiscal);
		setCdItem(cdItem);
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setQtTributario(qtTributario);
		setVlUnitario(vlUnitario);
		setTxtInformacaoAdicional(txtInformacaoAdicional);
		setCdItemDocumento(cdItemDocumento);
		setVlAcrescimo(vlAcrescimo);
		setVlDesconto(vlDesconto);
	}
	public void setCdNotaFiscal(int cdNotaFiscal){
		this.cdNotaFiscal=cdNotaFiscal;
	}
	public int getCdNotaFiscal(){
		return this.cdNotaFiscal;
	}
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
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
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
	}
	public void setCdNaturezaOperacao(int cdNaturezaOperacao){
		this.cdNaturezaOperacao=cdNaturezaOperacao;
	}
	public int getCdNaturezaOperacao(){
		return this.cdNaturezaOperacao;
	}
	public void setQtTributario(float qtTributario){
		this.qtTributario=qtTributario;
	}
	public float getQtTributario(){
		return this.qtTributario;
	}
	public void setVlUnitario(float vlUnitario){
		this.vlUnitario=vlUnitario;
	}
	public float getVlUnitario(){
		return this.vlUnitario;
	}
	public void setTxtInformacaoAdicional(String txtInformacaoAdicional){
		this.txtInformacaoAdicional=txtInformacaoAdicional;
	}
	public String getTxtInformacaoAdicional(){
		return this.txtInformacaoAdicional;
	}
	public void setCdItemDocumento(int cdItemDocumento){
		this.cdItemDocumento=cdItemDocumento;
	}
	public int getCdItemDocumento(){
		return this.cdItemDocumento;
	}
	public void setVlAcrescimo(float vlAcrescimo) {
		this.vlAcrescimo = vlAcrescimo;
	}
	public float getVlAcrescimo() {
		return vlAcrescimo;
	}
	public void setVlDesconto(float vlDesconto) {
		this.vlDesconto = vlDesconto;
	}
	public float getVlDesconto() {
		return vlDesconto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNotaFiscal: " +  getCdNotaFiscal();
		valueToString += ", cdItem: " +  getCdItem();
		valueToString += ", cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", cdNaturezaOperacao: " +  getCdNaturezaOperacao();
		valueToString += ", qtTributario: " +  getQtTributario();
		valueToString += ", vlUnitario: " +  getVlUnitario();
		valueToString += ", txtInformacaoAdicional: " +  getTxtInformacaoAdicional();
		valueToString += ", cdItemDocumento: " +  getCdItemDocumento();
		valueToString += ", vlAcrescimo: " +  getVlAcrescimo();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NotaFiscalItem(getCdNotaFiscal(),
			getCdItem(),
			getCdDocumentoSaida(),
			getCdProdutoServico(),
			getCdEmpresa(),
			getCdDocumentoEntrada(),
			getCdNaturezaOperacao(),
			getQtTributario(),
			getVlUnitario(),
			getTxtInformacaoAdicional(),
			getCdItemDocumento(),
			getVlAcrescimo(),
			getVlDesconto());
	}

}