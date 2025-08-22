package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class PedidoVenda {

	private int cdPedidoVenda;
	private int cdCliente;
	private GregorianCalendar dtPedido;
	private GregorianCalendar dtLimiteEntrega;
	private String idPedidoVenda;
	private float vlAcrescimo;
	private float vlDesconto;
	private int tpPedidoVenda;
	private int stPedidoVenda;
	private int lgWeb;
	private String txtObservacao;
	private int cdEnderecoEntrega;
	private int cdEnderecoCobranca;
	private int cdEmpresa;
	private int cdVendedor;
	private int cdTipoOperacao;
	private String nrPedidoVenda;
	private int cdPlanoPagamento;
	private GregorianCalendar dtAutorizacao;

	public PedidoVenda(int cdPedidoVenda,
			int cdCliente,
			GregorianCalendar dtPedido,
			GregorianCalendar dtLimiteEntrega,
			String idPedidoVenda,
			float vlAcrescimo,
			float vlDesconto,
			int tpPedidoVenda,
			int stPedidoVenda,
			int lgWeb,
			String txtObservacao,
			int cdEnderecoEntrega,
			int cdEnderecoCobranca,
			int cdEmpresa,
			int cdVendedor,
			int cdTipoOperacao,
			String nrPedidoVenda,
			int cdPlanoPagamento,
			GregorianCalendar dtAutorizacao){
		setCdPedidoVenda(cdPedidoVenda);
		setCdCliente(cdCliente);
		setDtPedido(dtPedido);
		setDtLimiteEntrega(dtLimiteEntrega);
		setIdPedidoVenda(idPedidoVenda);
		setVlAcrescimo(vlAcrescimo);
		setVlDesconto(vlDesconto);
		setTpPedidoVenda(tpPedidoVenda);
		setStPedidoVenda(stPedidoVenda);
		setLgWeb(lgWeb);
		setTxtObservacao(txtObservacao);
		setCdEnderecoEntrega(cdEnderecoEntrega);
		setCdEnderecoCobranca(cdEnderecoCobranca);
		setCdEmpresa(cdEmpresa);
		setCdVendedor(cdVendedor);
		setCdTipoOperacao(cdTipoOperacao);
		setNrPedidoVenda(nrPedidoVenda);
		setCdPlanoPagamento(cdPlanoPagamento);
		setDtAutorizacao(dtAutorizacao);
	}
	public void setCdPedidoVenda(int cdPedidoVenda){
		this.cdPedidoVenda=cdPedidoVenda;
	}
	public int getCdPedidoVenda(){
		return this.cdPedidoVenda;
	}
	public void setCdCliente(int cdCliente){
		this.cdCliente=cdCliente;
	}
	public int getCdCliente(){
		return this.cdCliente;
	}
	public void setDtPedido(GregorianCalendar dtPedido){
		this.dtPedido=dtPedido;
	}
	public GregorianCalendar getDtPedido(){
		return this.dtPedido;
	}
	public void setDtLimiteEntrega(GregorianCalendar dtLimiteEntrega){
		this.dtLimiteEntrega=dtLimiteEntrega;
	}
	public GregorianCalendar getDtLimiteEntrega(){
		return this.dtLimiteEntrega;
	}
	public void setIdPedidoVenda(String idPedidoVenda){
		this.idPedidoVenda=idPedidoVenda;
	}
	public String getIdPedidoVenda(){
		return this.idPedidoVenda;
	}
	public void setVlAcrescimo(float vlAcrescimo){
		this.vlAcrescimo=vlAcrescimo;
	}
	public float getVlAcrescimo(){
		return this.vlAcrescimo;
	}
	public void setVlDesconto(float vlDesconto){
		this.vlDesconto=vlDesconto;
	}
	public float getVlDesconto(){
		return this.vlDesconto;
	}
	public void setTpPedidoVenda(int tpPedidoVenda){
		this.tpPedidoVenda=tpPedidoVenda;
	}
	public int getTpPedidoVenda(){
		return this.tpPedidoVenda;
	}
	public void setStPedidoVenda(int stPedidoVenda){
		this.stPedidoVenda=stPedidoVenda;
	}
	public int getStPedidoVenda(){
		return this.stPedidoVenda;
	}
	public void setLgWeb(int lgWeb){
		this.lgWeb=lgWeb;
	}
	public int getLgWeb(){
		return this.lgWeb;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdEnderecoEntrega(int cdEnderecoEntrega){
		this.cdEnderecoEntrega=cdEnderecoEntrega;
	}
	public int getCdEnderecoEntrega(){
		return this.cdEnderecoEntrega;
	}
	public void setCdEnderecoCobranca(int cdEnderecoCobranca){
		this.cdEnderecoCobranca=cdEnderecoCobranca;
	}
	public int getCdEnderecoCobranca(){
		return this.cdEnderecoCobranca;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdVendedor(int cdVendedor){
		this.cdVendedor=cdVendedor;
	}
	public int getCdVendedor(){
		return this.cdVendedor;
	}
	public void setCdTipoOperacao(int cdTipoOperacao){
		this.cdTipoOperacao=cdTipoOperacao;
	}
	public int getCdTipoOperacao(){
		return this.cdTipoOperacao;
	}
	public void setNrPedidoVenda(String nrPedidoVenda){
		this.nrPedidoVenda=nrPedidoVenda;
	}
	public String getNrPedidoVenda(){
		return this.nrPedidoVenda;
	}
	public void setCdPlanoPagamento(int cdPlanoPagamento){
		this.cdPlanoPagamento=cdPlanoPagamento;
	}
	public int getCdPlanoPagamento(){
		return this.cdPlanoPagamento;
	}
	public void setDtAutorizacao(GregorianCalendar dtAutorizacao){
		this.dtAutorizacao=dtAutorizacao;
	}
	public GregorianCalendar getDtAutorizacao(){
		return this.dtAutorizacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPedidoVenda: " +  getCdPedidoVenda();
		valueToString += ", cdCliente: " +  getCdCliente();
		valueToString += ", dtPedido: " +  sol.util.Util.formatDateTime(getDtPedido(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLimiteEntrega: " +  sol.util.Util.formatDateTime(getDtLimiteEntrega(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idPedidoVenda: " +  getIdPedidoVenda();
		valueToString += ", vlAcrescimo: " +  getVlAcrescimo();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", tpPedidoVenda: " +  getTpPedidoVenda();
		valueToString += ", stPedidoVenda: " +  getStPedidoVenda();
		valueToString += ", lgWeb: " +  getLgWeb();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdEnderecoEntrega: " +  getCdEnderecoEntrega();
		valueToString += ", cdEnderecoCobranca: " +  getCdEnderecoCobranca();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdVendedor: " +  getCdVendedor();
		valueToString += ", cdTipoOperacao: " +  getCdTipoOperacao();
		valueToString += ", nrPedidoVenda: " +  getNrPedidoVenda();
		valueToString += ", cdPlanoPagamento: " +  getCdPlanoPagamento();
		valueToString += ", dtAutorizacao: " +  sol.util.Util.formatDateTime(getDtAutorizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PedidoVenda(getCdPedidoVenda(),
			getCdCliente(),
			getDtPedido()==null ? null : (GregorianCalendar)getDtPedido().clone(),
			getDtLimiteEntrega()==null ? null : (GregorianCalendar)getDtLimiteEntrega().clone(),
			getIdPedidoVenda(),
			getVlAcrescimo(),
			getVlDesconto(),
			getTpPedidoVenda(),
			getStPedidoVenda(),
			getLgWeb(),
			getTxtObservacao(),
			getCdEnderecoEntrega(),
			getCdEnderecoCobranca(),
			getCdEmpresa(),
			getCdVendedor(),
			getCdTipoOperacao(),
			getNrPedidoVenda(),
			getCdPlanoPagamento(),
			getDtAutorizacao()==null ? null : (GregorianCalendar)getDtAutorizacao().clone());
	}

}
