package com.tivic.manager.mcr;

public class ContaBancaria {

	private String nrAgencia;
	private String nmBanco;
	private int cdPessoa;
	private float vlLimiteCheque;
	private String txtObjetivoPoupanca;
	private float vlSaldoMedio;

	public ContaBancaria(String nrAgencia,
			String nmBanco,
			int cdPessoa,
			float vlLimiteCheque,
			String txtObjetivoPoupanca,
			float vlSaldoMedio){
		setNrAgencia(nrAgencia);
		setNmBanco(nmBanco);
		setCdPessoa(cdPessoa);
		setVlLimiteCheque(vlLimiteCheque);
		setTxtObjetivoPoupanca(txtObjetivoPoupanca);
		setVlSaldoMedio(vlSaldoMedio);
	}
	public void setNrAgencia(String nrAgencia){
		this.nrAgencia=nrAgencia;
	}
	public String getNrAgencia(){
		return this.nrAgencia;
	}
	public void setNmBanco(String nmBanco){
		this.nmBanco=nmBanco;
	}
	public String getNmBanco(){
		return this.nmBanco;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setVlLimiteCheque(float vlLimiteCheque){
		this.vlLimiteCheque=vlLimiteCheque;
	}
	public float getVlLimiteCheque(){
		return this.vlLimiteCheque;
	}
	public void setTxtObjetivoPoupanca(String txtObjetivoPoupanca){
		this.txtObjetivoPoupanca=txtObjetivoPoupanca;
	}
	public String getTxtObjetivoPoupanca(){
		return this.txtObjetivoPoupanca;
	}
	public void setVlSaldoMedio(float vlSaldoMedio){
		this.vlSaldoMedio=vlSaldoMedio;
	}
	public float getVlSaldoMedio(){
		return this.vlSaldoMedio;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "nrAgencia: " +  getNrAgencia();
		valueToString += ", nmBanco: " +  getNmBanco();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", vlLimiteCheque: " +  getVlLimiteCheque();
		valueToString += ", txtObjetivoPoupanca: " +  getTxtObjetivoPoupanca();
		valueToString += ", vlSaldoMedio: " +  getVlSaldoMedio();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaBancaria(nrAgencia,
			nmBanco,
			cdPessoa,
			vlLimiteCheque,
			txtObjetivoPoupanca,
			vlSaldoMedio);
	}

}