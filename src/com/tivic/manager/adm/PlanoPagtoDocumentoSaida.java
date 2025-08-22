package com.tivic.manager.adm;

public class PlanoPagtoDocumentoSaida {

	private int cdPlanoPagamento;
	private int cdDocumentoSaida;
	private int cdFormaPagamento;
	private int cdUsuario;
	private float vlPagamento;
	private float vlDesconto;
	private String nrAutorizacao;
	private float vlAcrescimo;
	
	public PlanoPagtoDocumentoSaida(int cdPlanoPagamento,
			int cdDocumentoSaida,
			int cdFormaPagamento,
			int cdUsuario,
			float vlPagamento, 
			float vlDesconto,
			String nrAutorizacao,
			float vlAcrescimo){
		setCdPlanoPagamento(cdPlanoPagamento);
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdFormaPagamento(cdFormaPagamento);
		setCdUsuario(cdUsuario);
		setVlPagamento(vlPagamento);
		setVlDesconto(vlDesconto);
		setNrAutorizacao(nrAutorizacao);
		setVlAcrescimo(vlAcrescimo);
	}
	public void setCdPlanoPagamento(int cdPlanoPagamento){
		this.cdPlanoPagamento=cdPlanoPagamento;
	}
	public int getCdPlanoPagamento(){
		return this.cdPlanoPagamento;
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
	}
	public void setCdFormaPagamento(int cdFormaPagamento){
		this.cdFormaPagamento=cdFormaPagamento;
	}
	public int getCdFormaPagamento(){
		return this.cdFormaPagamento;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setVlPagamento(float vlPagamento){
		this.vlPagamento=vlPagamento;
	}
	public float getVlPagamento(){
		return this.vlPagamento;
	}
	public float getVlDesconto() {
		return vlDesconto;
	}
	public void setVlDesconto(float vlDesconto) {
		this.vlDesconto = vlDesconto;
	}
	public String getNrAutorizacao() {
		return nrAutorizacao;
	}
	public void setNrAutorizacao(String nrAutorizacao) {
		this.nrAutorizacao = nrAutorizacao;
	}
	public float getVlAcrescimo() {
		return vlAcrescimo;
	}
	public void setVlAcrescimo(float vlAcrescimo) {
		this.vlAcrescimo = vlAcrescimo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlanoPagamento: " +  getCdPlanoPagamento();
		valueToString += ", cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdFormaPagamento: " +  getCdFormaPagamento();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", vlPagamento: " +  getVlPagamento();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", nrAutorizacao: " +  getNrAutorizacao();
		valueToString += ", vlAcrescimo: " + getVlAcrescimo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoPagtoDocumentoSaida(getCdPlanoPagamento(),
			getCdDocumentoSaida(),
			getCdFormaPagamento(),
			getCdUsuario(),
			getVlPagamento(),
			getVlDesconto(),
			getNrAutorizacao(),
			getVlAcrescimo());
	}

}