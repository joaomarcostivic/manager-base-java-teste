package com.tivic.manager.adm;

public class FormaPagamentoEmpresa {

	private int cdFormaPagamento;
	private int cdEmpresa;
	private int cdAdministrador;
	private int cdTipoDocumento;
	private int qtDiasCredito;
	private Double vlTarifaTransacao;
	private Double prTaxaDesconto;
	private Double prDescontoRave;
	private int cdContaCarteira;
	private int cdConta;

	public FormaPagamentoEmpresa(){};
	
	public FormaPagamentoEmpresa(int cdFormaPagamento,
			int cdEmpresa,
			int cdAdministrador,
			int cdTipoDocumento,
			int qtDiasCredito,
			Double vlTarifaTransacao,
			Double prTaxaDesconto,
			Double prDescontoRave,
			int cdContaCarteira,
			int cdConta){
		setCdFormaPagamento(cdFormaPagamento);
		setCdEmpresa(cdEmpresa);
		setCdAdministrador(cdAdministrador);
		setCdTipoDocumento(cdTipoDocumento);
		setQtDiasCredito(qtDiasCredito);
		setVlTarifaTransacao(vlTarifaTransacao);
		setPrTaxaDesconto(prTaxaDesconto);
		setPrDescontoRave(prDescontoRave);
		setCdContaCarteira(cdContaCarteira);
		setCdConta(cdConta);
	}
	public void setCdFormaPagamento(int cdFormaPagamento){
		this.cdFormaPagamento=cdFormaPagamento;
	}
	public int getCdFormaPagamento(){
		return this.cdFormaPagamento;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdAdministrador(int cdAdministrador){
		this.cdAdministrador=cdAdministrador;
	}
	public int getCdAdministrador(){
		return this.cdAdministrador;
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setQtDiasCredito(int qtDiasCredito){
		this.qtDiasCredito=qtDiasCredito;
	}
	public int getQtDiasCredito(){
		return this.qtDiasCredito;
	}
	public void setVlTarifaTransacao(Double vlTarifaTransacao){
		this.vlTarifaTransacao=vlTarifaTransacao;
	}
	public Double getVlTarifaTransacao(){
		return this.vlTarifaTransacao;
	}
	public void setPrTaxaDesconto(Double prTaxaDesconto){
		this.prTaxaDesconto=prTaxaDesconto;
	}
	public Double getPrTaxaDesconto(){
		return this.prTaxaDesconto;
	}
	public void setPrDescontoRave(Double prDescontoRave){
		this.prDescontoRave=prDescontoRave;
	}
	public Double getPrDescontoRave(){
		return this.prDescontoRave;
	}
	public void setCdContaCarteira(int cdContaCarteira){
		this.cdContaCarteira=cdContaCarteira;
	}
	public int getCdContaCarteira(){
		return this.cdContaCarteira;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFormaPagamento: " +  getCdFormaPagamento();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdAdministrador: " +  getCdAdministrador();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", qtDiasCredito: " +  getQtDiasCredito();
		valueToString += ", vlTarifaTransacao: " +  getVlTarifaTransacao();
		valueToString += ", prTaxaDesconto: " +  getPrTaxaDesconto();
		valueToString += ", prDescontoRave: " +  getPrDescontoRave();
		valueToString += ", cdContaCarteira: " +  getCdContaCarteira();
		valueToString += ", cdConta: " +  getCdConta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FormaPagamentoEmpresa(getCdFormaPagamento(),
			getCdEmpresa(),
			getCdAdministrador(),
			getCdTipoDocumento(),
			getQtDiasCredito(),
			getVlTarifaTransacao(),
			getPrTaxaDesconto(),
			getPrDescontoRave(),
			getCdContaCarteira(),
			getCdConta());
	}

}
