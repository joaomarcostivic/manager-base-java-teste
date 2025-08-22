package com.tivic.manager.adm;

public class ContratoPlanoPagto {

	private int cdContrato;
	private int cdPlanoPagamento;
	private int cdFormaPagamento;
	private int cdPagamento;
	private float vlPagamento;
	private int cdUsuario;
	private float cdMovimentoConta;
	private int cdConta;

	public ContratoPlanoPagto(int cdContrato,
			int cdPlanoPagamento,
			int cdFormaPagamento,
			int cdPagamento,
			float vlPagamento,
			int cdUsuario,
			float cdMovimentoConta,
			int cdConta){
		setCdContrato(cdContrato);
		setCdPlanoPagamento(cdPlanoPagamento);
		setCdFormaPagamento(cdFormaPagamento);
		setCdPagamento(cdPagamento);
		setVlPagamento(vlPagamento);
		setCdUsuario(cdUsuario);
		setCdMovimentoConta(cdMovimentoConta);
		setCdConta(cdConta);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdPlanoPagamento(int cdPlanoPagamento){
		this.cdPlanoPagamento=cdPlanoPagamento;
	}
	public int getCdPlanoPagamento(){
		return this.cdPlanoPagamento;
	}
	public void setCdFormaPagamento(int cdFormaPagamento){
		this.cdFormaPagamento=cdFormaPagamento;
	}
	public int getCdFormaPagamento(){
		return this.cdFormaPagamento;
	}
	public void setCdPagamento(int cdPagamento){
		this.cdPagamento=cdPagamento;
	}
	public int getCdPagamento(){
		return this.cdPagamento;
	}
	public void setVlPagamento(float vlPagamento){
		this.vlPagamento=vlPagamento;
	}
	public float getVlPagamento(){
		return this.vlPagamento;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdMovimentoConta(float cdMovimentoConta){
		this.cdMovimentoConta=cdMovimentoConta;
	}
	public float getCdMovimentoConta(){
		return this.cdMovimentoConta;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdPlanoPagamento: " +  getCdPlanoPagamento();
		valueToString += ", cdFormaPagamento: " +  getCdFormaPagamento();
		valueToString += ", cdPagamento: " +  getCdPagamento();
		valueToString += ", vlPagamento: " +  getVlPagamento();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdMovimentoConta: " +  getCdMovimentoConta();
		valueToString += ", cdConta: " +  getCdConta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContratoPlanoPagto(getCdContrato(),
			getCdPlanoPagamento(),
			getCdFormaPagamento(),
			getCdPagamento(),
			getVlPagamento(),
			getCdUsuario(),
			getCdMovimentoConta(),
			getCdConta());
	}

}
