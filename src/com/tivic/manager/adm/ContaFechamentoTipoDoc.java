package com.tivic.manager.adm;

public class ContaFechamentoTipoDoc {

	private int cdConta;
	private int cdFechamento;
	private int cdTipoDocumento;
	private int vlTotalEntradas;
	private int vlTotalSaidas;
	private int vlSaldoFinal;
	private float vlSaldoAnterior;

	public ContaFechamentoTipoDoc(int cdConta,
			int cdFechamento,
			int cdTipoDocumento,
			int vlTotalEntradas,
			int vlTotalSaidas,
			int vlSaldoFinal,
			float vlSaldoAnterior){
		setCdConta(cdConta);
		setCdFechamento(cdFechamento);
		setCdTipoDocumento(cdTipoDocumento);
		setVlTotalEntradas(vlTotalEntradas);
		setVlTotalSaidas(vlTotalSaidas);
		setVlSaldoFinal(vlSaldoFinal);
		setVlSaldoAnterior(vlSaldoAnterior);
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdFechamento(int cdFechamento){
		this.cdFechamento=cdFechamento;
	}
	public int getCdFechamento(){
		return this.cdFechamento;
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setVlTotalEntradas(int vlTotalEntradas){
		this.vlTotalEntradas=vlTotalEntradas;
	}
	public int getVlTotalEntradas(){
		return this.vlTotalEntradas;
	}
	public void setVlTotalSaidas(int vlTotalSaidas){
		this.vlTotalSaidas=vlTotalSaidas;
	}
	public int getVlTotalSaidas(){
		return this.vlTotalSaidas;
	}
	public void setVlSaldoFinal(int vlSaldoFinal){
		this.vlSaldoFinal=vlSaldoFinal;
	}
	public int getVlSaldoFinal(){
		return this.vlSaldoFinal;
	}
	public void setVlSaldoAnterior(float vlSaldoAnterior){
		this.vlSaldoAnterior=vlSaldoAnterior;
	}
	public float getVlSaldoAnterior(){
		return this.vlSaldoAnterior;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConta: " +  getCdConta();
		valueToString += ", cdFechamento: " +  getCdFechamento();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", vlTotalEntradas: " +  getVlTotalEntradas();
		valueToString += ", vlTotalSaidas: " +  getVlTotalSaidas();
		valueToString += ", vlSaldoFinal: " +  getVlSaldoFinal();
		valueToString += ", vlSaldoAnterior: " +  getVlSaldoAnterior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaFechamentoTipoDoc(getCdConta(),
			getCdFechamento(),
			getCdTipoDocumento(),
			getVlTotalEntradas(),
			getVlTotalSaidas(),
			getVlSaldoFinal(),
			getVlSaldoAnterior());
	}

}
