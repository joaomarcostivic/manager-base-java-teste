package com.tivic.manager.mob;

public class ConcessaoLoteNotaFiscal {

	private int cdConcessaoLote;
	private int cdConcessao;
	private int cdNotaFiscal;
	private int nrDiasFrequencia;
	private float vlMensalLote;

	public ConcessaoLoteNotaFiscal() { }

	public ConcessaoLoteNotaFiscal(int cdConcessaoLote,
			int cdConcessao,
			int cdNotaFiscal,
			int nrDiasFrequencia,
			float vlMensalLote) {
		setCdConcessaoLote(cdConcessaoLote);
		setCdConcessao(cdConcessao);
		setCdNotaFiscal(cdNotaFiscal);
		setNrDiasFrequencia(nrDiasFrequencia);
		setVlMensalLote(vlMensalLote);
	}
	public void setCdConcessaoLote(int cdConcessaoLote){
		this.cdConcessaoLote=cdConcessaoLote;
	}
	public int getCdConcessaoLote(){
		return this.cdConcessaoLote;
	}
	public void setCdConcessao(int cdConcessao){
		this.cdConcessao=cdConcessao;
	}
	public int getCdConcessao(){
		return this.cdConcessao;
	}
	public void setCdNotaFiscal(int cdNotaFiscal){
		this.cdNotaFiscal=cdNotaFiscal;
	}
	public int getCdNotaFiscal(){
		return this.cdNotaFiscal;
	}
	public void setNrDiasFrequencia(int nrDiasFrequencia){
		this.nrDiasFrequencia=nrDiasFrequencia;
	}
	public int getNrDiasFrequencia(){
		return this.nrDiasFrequencia;
	}
	public void setVlMensalLote(float vlMensalLote){
		this.vlMensalLote=vlMensalLote;
	}
	public float getVlMensalLote(){
		return this.vlMensalLote;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConcessaoLote: " +  getCdConcessaoLote();
		valueToString += ", cdConcessao: " +  getCdConcessao();
		valueToString += ", cdNotaFiscal: " +  getCdNotaFiscal();
		valueToString += ", nrDiasFrequencia: " +  getNrDiasFrequencia();
		valueToString += ", vlMensalLote: " +  getVlMensalLote();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ConcessaoLoteNotaFiscal(getCdConcessaoLote(),
			getCdConcessao(),
			getCdNotaFiscal(),
			getNrDiasFrequencia(),
			getVlMensalLote());
	}

}