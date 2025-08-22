package com.tivic.manager.adm;

public class MovimentoContaReceber {

	private int cdConta;
	private int cdMovimentoConta;
	private int cdContaReceber;
	private double vlRecebido;
	private double vlJuros;
	private double vlMulta;
	private double vlAcrescimo;
	private double vlDesconto;
	private double vlTarifaCobranca;
	private int cdArquivo;
	private int cdRegistro;

	public MovimentoContaReceber(){}
	
	public MovimentoContaReceber(int cdConta,
			int cdMovimentoConta,
			int cdContaReceber,
			double vlRecebido,
			double vlJuros,
			double vlMulta,
			double vlDesconto,
			double vlTarifaCobranca,
			int cdArquivo,
			int cdRegistro){
		setCdConta(cdConta);
		setCdMovimentoConta(cdMovimentoConta);
		setCdContaReceber(cdContaReceber);
		setVlRecebido(vlRecebido);
		setVlJuros(vlJuros);
		setVlMulta(vlMulta);
		setVlDesconto(vlDesconto);
		setVlTarifaCobranca(vlTarifaCobranca);
		setCdArquivo(cdArquivo);
		setCdRegistro(cdRegistro);
	}
	public MovimentoContaReceber(int cdConta,
			int cdMovimentoConta,
			int cdContaReceber,
			double vlRecebido,
			double vlJuros,
			double vlMulta,
			double vlAcrescimo,
			double vlDesconto,
			double vlTarifaCobranca,
			int cdArquivo,
			int cdRegistro){
		setCdConta(cdConta);
		setCdMovimentoConta(cdMovimentoConta);
		setCdContaReceber(cdContaReceber);
		setVlRecebido(vlRecebido);
		setVlJuros(vlJuros);
		setVlMulta(vlMulta);
		setVlAcrescimo(vlAcrescimo);
		setVlDesconto(vlDesconto);
		setVlTarifaCobranca(vlTarifaCobranca);
		setCdArquivo(cdArquivo);
		setCdRegistro(cdRegistro);
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdMovimentoConta(int cdMovimentoConta){
		this.cdMovimentoConta=cdMovimentoConta;
	}
	public int getCdMovimentoConta(){
		return this.cdMovimentoConta;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public void setVlRecebido(Double vlRecebido){
		this.vlRecebido=vlRecebido;
	}
	public Double getVlRecebido(){
		return this.vlRecebido;
	}
	public void setVlJuros(Double vlJuros){
		this.vlJuros=vlJuros;
	}
	public Double getVlJuros(){
		return this.vlJuros;
	}
	public void setVlMulta(Double vlMulta){
		this.vlMulta=vlMulta;
	}
	public Double getVlMulta(){
		return this.vlMulta;
	}
	public void setVlAcrescimo(Double vlAcrescimo){
		this.vlAcrescimo=vlAcrescimo;
	}
	public Double getVlAcrescimo(){
		return this.vlAcrescimo;
	}
	public void setVlDesconto(Double vlDesconto){
		this.vlDesconto=vlDesconto;
	}
	public Double getVlDesconto(){
		return this.vlDesconto;
	}
	public void setVlTarifaCobranca(Double vlTarifaCobranca){
		this.vlTarifaCobranca=vlTarifaCobranca;
	}
	public Double getVlTarifaCobranca(){
		return this.vlTarifaCobranca;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdRegistro(int cdRegistro){
		this.cdRegistro=cdRegistro;
	}
	public int getCdRegistro(){
		return this.cdRegistro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConta: " +  getCdConta();
		valueToString += ", cdMovimentoConta: " +  getCdMovimentoConta();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		valueToString += ", vlRecebido: " +  getVlRecebido();
		valueToString += ", vlJuros: " +  getVlJuros();
		valueToString += ", vlMulta: " +  getVlMulta();
		valueToString += ", vlAcrescimo: " + getVlAcrescimo();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", vlTarifaCobranca: " +  getVlTarifaCobranca();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", cdRegistro: " +  getCdRegistro();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MovimentoContaReceber(getCdConta(),
			getCdMovimentoConta(),
			getCdContaReceber(),
			getVlRecebido(),
			getVlJuros(),
			getVlMulta(),
			getVlAcrescimo(),
			getVlDesconto(),
			getVlTarifaCobranca(),
			getCdArquivo(),
			getCdRegistro());
	}

}