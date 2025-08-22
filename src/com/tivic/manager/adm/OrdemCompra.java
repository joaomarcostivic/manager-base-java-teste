package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class OrdemCompra {

	private int cdOrdemCompra;
	private GregorianCalendar dtOrdemCompra;
	private int stOrdemCompra;
	private GregorianCalendar dtLimiteEntrega;
	private String idOrdemCompra;
	private String nrOrdemCompra;
	private int cdFornecedor;
	private int cdLocalEntrega;
	private int cdMoeda;
	private int cdTabelaPreco;
	private int cdComprador;
	private float vlDesconto;
	private float vlAcrescimo;
	private int tpMovimentoEstoque;
	private String txtObservacao;
	private float vlTotalDocumento;
	private int cdEmpresa;
	private int cdUsuarioAutorizacao;
	private int cdOrdemCompraOrigem;

	public OrdemCompra(){ }

	public OrdemCompra(int cdOrdemCompra,
			GregorianCalendar dtOrdemCompra,
			int stOrdemCompra,
			GregorianCalendar dtLimiteEntrega,
			String idOrdemCompra,
			String nrOrdemCompra,
			int cdFornecedor,
			int cdLocalEntrega,
			int cdMoeda,
			int cdTabelaPreco,
			int cdComprador,
			float vlDesconto,
			float vlAcrescimo,
			int tpMovimentoEstoque,
			String txtObservacao,
			float vlTotalDocumento,
			int cdEmpresa,
			int cdUsuarioAutorizacao,
			int cdOrdemCompraOrigem){
		setCdOrdemCompra(cdOrdemCompra);
		setDtOrdemCompra(dtOrdemCompra);
		setStOrdemCompra(stOrdemCompra);
		setDtLimiteEntrega(dtLimiteEntrega);
		setIdOrdemCompra(idOrdemCompra);
		setNrOrdemCompra(nrOrdemCompra);
		setCdFornecedor(cdFornecedor);
		setCdLocalEntrega(cdLocalEntrega);
		setCdMoeda(cdMoeda);
		setCdTabelaPreco(cdTabelaPreco);
		setCdComprador(cdComprador);
		setVlDesconto(vlDesconto);
		setVlAcrescimo(vlAcrescimo);
		setTpMovimentoEstoque(tpMovimentoEstoque);
		setTxtObservacao(txtObservacao);
		setVlTotalDocumento(vlTotalDocumento);
		setCdEmpresa(cdEmpresa);
		setCdUsuarioAutorizacao(cdUsuarioAutorizacao);
		setCdOrdemCompraOrigem(cdOrdemCompraOrigem);
	}
	public void setCdOrdemCompra(int cdOrdemCompra){
		this.cdOrdemCompra=cdOrdemCompra;
	}
	public int getCdOrdemCompra(){
		return this.cdOrdemCompra;
	}
	public void setDtOrdemCompra(GregorianCalendar dtOrdemCompra){
		this.dtOrdemCompra=dtOrdemCompra;
	}
	public GregorianCalendar getDtOrdemCompra(){
		return this.dtOrdemCompra;
	}
	public void setStOrdemCompra(int stOrdemCompra){
		this.stOrdemCompra=stOrdemCompra;
	}
	public int getStOrdemCompra(){
		return this.stOrdemCompra;
	}
	public void setDtLimiteEntrega(GregorianCalendar dtLimiteEntrega){
		this.dtLimiteEntrega=dtLimiteEntrega;
	}
	public GregorianCalendar getDtLimiteEntrega(){
		return this.dtLimiteEntrega;
	}
	public void setIdOrdemCompra(String idOrdemCompra){
		this.idOrdemCompra=idOrdemCompra;
	}
	public String getIdOrdemCompra(){
		return this.idOrdemCompra;
	}
	public void setNrOrdemCompra(String nrOrdemCompra){
		this.nrOrdemCompra=nrOrdemCompra;
	}
	public String getNrOrdemCompra(){
		return this.nrOrdemCompra;
	}
	public void setCdFornecedor(int cdFornecedor){
		this.cdFornecedor=cdFornecedor;
	}
	public int getCdFornecedor(){
		return this.cdFornecedor;
	}
	public void setCdLocalEntrega(int cdLocalEntrega){
		this.cdLocalEntrega=cdLocalEntrega;
	}
	public int getCdLocalEntrega(){
		return this.cdLocalEntrega;
	}
	public void setCdMoeda(int cdMoeda){
		this.cdMoeda=cdMoeda;
	}
	public int getCdMoeda(){
		return this.cdMoeda;
	}
	public void setCdTabelaPreco(int cdTabelaPreco){
		this.cdTabelaPreco=cdTabelaPreco;
	}
	public int getCdTabelaPreco(){
		return this.cdTabelaPreco;
	}
	public void setCdComprador(int cdComprador){
		this.cdComprador=cdComprador;
	}
	public int getCdComprador(){
		return this.cdComprador;
	}
	public void setVlDesconto(float vlDesconto){
		this.vlDesconto=vlDesconto;
	}
	public float getVlDesconto(){
		return this.vlDesconto;
	}
	public void setVlAcrescimo(float vlAcrescimo){
		this.vlAcrescimo=vlAcrescimo;
	}
	public float getVlAcrescimo(){
		return this.vlAcrescimo;
	}
	public void setTpMovimentoEstoque(int tpMovimentoEstoque){
		this.tpMovimentoEstoque=tpMovimentoEstoque;
	}
	public int getTpMovimentoEstoque(){
		return this.tpMovimentoEstoque;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setVlTotalDocumento(float vlTotalDocumento){
		this.vlTotalDocumento=vlTotalDocumento;
	}
	public float getVlTotalDocumento(){
		return this.vlTotalDocumento;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdUsuarioAutorizacao(int cdUsuarioAutorizacao){
		this.cdUsuarioAutorizacao=cdUsuarioAutorizacao;
	}
	public int getCdUsuarioAutorizacao(){
		return this.cdUsuarioAutorizacao;
	}
	public void setCdOrdemCompraOrigem(int cdOrdemCompraOrigem){
		this.cdOrdemCompraOrigem=cdOrdemCompraOrigem;
	}
	public int getCdOrdemCompraOrigem(){
		return this.cdOrdemCompraOrigem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrdemCompra: " +  getCdOrdemCompra();
		valueToString += ", dtOrdemCompra: " +  sol.util.Util.formatDateTime(getDtOrdemCompra(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stOrdemCompra: " +  getStOrdemCompra();
		valueToString += ", dtLimiteEntrega: " +  sol.util.Util.formatDateTime(getDtLimiteEntrega(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idOrdemCompra: " +  getIdOrdemCompra();
		valueToString += ", nrOrdemCompra: " +  getNrOrdemCompra();
		valueToString += ", cdFornecedor: " +  getCdFornecedor();
		valueToString += ", cdLocalEntrega: " +  getCdLocalEntrega();
		valueToString += ", cdMoeda: " +  getCdMoeda();
		valueToString += ", cdTabelaPreco: " +  getCdTabelaPreco();
		valueToString += ", cdComprador: " +  getCdComprador();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", vlAcrescimo: " +  getVlAcrescimo();
		valueToString += ", tpMovimentoEstoque: " +  getTpMovimentoEstoque();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", vlTotalDocumento: " +  getVlTotalDocumento();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdUsuarioAutorizacao: " +  getCdUsuarioAutorizacao();
		valueToString += ", cdOrdemCompraOrigem: " +  getCdOrdemCompraOrigem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OrdemCompra(getCdOrdemCompra(),
			getDtOrdemCompra()==null ? null : (GregorianCalendar)getDtOrdemCompra().clone(),
			getStOrdemCompra(),
			getDtLimiteEntrega()==null ? null : (GregorianCalendar)getDtLimiteEntrega().clone(),
			getIdOrdemCompra(),
			getNrOrdemCompra(),
			getCdFornecedor(),
			getCdLocalEntrega(),
			getCdMoeda(),
			getCdTabelaPreco(),
			getCdComprador(),
			getVlDesconto(),
			getVlAcrescimo(),
			getTpMovimentoEstoque(),
			getTxtObservacao(),
			getVlTotalDocumento(),
			getCdEmpresa(),
			getCdUsuarioAutorizacao(),
			getCdOrdemCompraOrigem());
	}

}