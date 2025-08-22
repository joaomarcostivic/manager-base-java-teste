package com.tivic.manager.fta;

public class ViagemPessoa {

	private int cdPessoa;
	private int cdViagem;
	private int lgMotorista;
	private int tpViagemPessoa;
	
	public ViagemPessoa(int cdPessoa,
			int cdViagem,
			int lgMotorista,
			int tpViagemPessoa){
		setCdPessoa(cdPessoa);
		setCdViagem(cdViagem);
		setLgMotorista(lgMotorista);
		setTpViagemPessoa(tpViagemPessoa);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdViagem(int cdViagem){
		this.cdViagem=cdViagem;
	}
	public int getCdViagem(){
		return this.cdViagem;
	}
	public void setLgMotorista(int lgMotorista){
		this.lgMotorista=lgMotorista;
	}
	public int getLgMotorista(){
		return this.lgMotorista;
	}
	public void setTpViagemPessoa(int tpViagemPessoa) {
		this.tpViagemPessoa = tpViagemPessoa;
	}
	public int getTpViagemPessoa() {
		return tpViagemPessoa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdViagem: " +  getCdViagem();
		valueToString += ", lgMotorista: " +  getLgMotorista();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ViagemPessoa(getCdPessoa(),
			getCdViagem(),
			getLgMotorista(),
			getTpViagemPessoa());
	}

}
