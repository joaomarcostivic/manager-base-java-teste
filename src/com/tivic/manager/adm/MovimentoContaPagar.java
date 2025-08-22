package com.tivic.manager.adm;

public class MovimentoContaPagar {

	private int cdConta;
	private int cdMovimentoConta;
	private int cdContaPagar;
	private double vlPago;
	private double vlMulta;
	private double vlJuros;
	private double vlDesconto;

	public MovimentoContaPagar(){}
	
	public MovimentoContaPagar(int cdConta,
			int cdMovimentoConta,
			int cdContaPagar,
			double vlPago,
			double vlMulta,
			double vlJuros,
			double vlDesconto){
		setCdConta(cdConta);
		setCdMovimentoConta(cdMovimentoConta);
		setCdContaPagar(cdContaPagar);
		setVlPago(vlPago);
		setVlMulta(vlMulta);
		setVlJuros(vlJuros);
		setVlDesconto(vlDesconto);
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
	public void setCdContaPagar(int cdContaPagar){
		this.cdContaPagar=cdContaPagar;
	}
	public int getCdContaPagar(){
		return this.cdContaPagar;
	}
	public void setVlPago(Double vlPago){
		this.vlPago=vlPago;
	}
	public Double getVlPago(){
		return this.vlPago;
	}
	public void setVlMulta(Double vlMulta){
		this.vlMulta=vlMulta;
	}
	public Double getVlMulta(){
		return this.vlMulta;
	}
	public void setVlJuros(Double vlJuros){
		this.vlJuros=vlJuros;
	}
	public Double getVlJuros(){
		return this.vlJuros;
	}
	public void setVlDesconto(Double vlDesconto){
		this.vlDesconto=vlDesconto;
	}
	public Double getVlDesconto(){
		return this.vlDesconto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConta: " +  getCdConta();
		valueToString += ", cdMovimentoConta: " +  getCdMovimentoConta();
		valueToString += ", cdContaPagar: " +  getCdContaPagar();
		valueToString += ", vlPago: " +  getVlPago();
		valueToString += ", vlMulta: " +  getVlMulta();
		valueToString += ", vlJuros: " +  getVlJuros();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MovimentoContaPagar(getCdConta(),
			getCdMovimentoConta(),
			getCdContaPagar(),
			getVlPago(),
			getVlMulta(),
			getVlJuros(),
			getVlDesconto());
	}

}