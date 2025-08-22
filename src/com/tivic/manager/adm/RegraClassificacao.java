package com.tivic.manager.adm;

public class RegraClassificacao {

	private int cdRegra;
	private int cdCriterio;
	private int tpOperadorRelacional;
	private float vlReferencia;
	private int lgRelativo;

	public RegraClassificacao(int cdRegra,
			int cdCriterio,
			int tpOperadorRelacional,
			float vlReferencia,
			int lgRelativo){
		setCdRegra(cdRegra);
		setCdCriterio(cdCriterio);
		setTpOperadorRelacional(tpOperadorRelacional);
		setVlReferencia(vlReferencia);
		setLgRelativo(lgRelativo);
	}
	public void setCdRegra(int cdRegra){
		this.cdRegra=cdRegra;
	}
	public int getCdRegra(){
		return this.cdRegra;
	}
	public void setCdCriterio(int cdCriterio){
		this.cdCriterio=cdCriterio;
	}
	public int getCdCriterio(){
		return this.cdCriterio;
	}
	public void setTpOperadorRelacional(int tpOperadorRelacional){
		this.tpOperadorRelacional=tpOperadorRelacional;
	}
	public int getTpOperadorRelacional(){
		return this.tpOperadorRelacional;
	}
	public void setVlReferencia(float vlReferencia){
		this.vlReferencia=vlReferencia;
	}
	public float getVlReferencia(){
		return this.vlReferencia;
	}
	public void setLgRelativo(int lgRelativo){
		this.lgRelativo=lgRelativo;
	}
	public int getLgRelativo(){
		return this.lgRelativo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegra: " +  getCdRegra();
		valueToString += ", cdCriterio: " +  getCdCriterio();
		valueToString += ", tpOperadorRelacional: " +  getTpOperadorRelacional();
		valueToString += ", vlReferencia: " +  getVlReferencia();
		valueToString += ", lgRelativo: " +  getLgRelativo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RegraClassificacao(getCdRegra(),
			getCdCriterio(),
			getTpOperadorRelacional(),
			getVlReferencia(),
			getLgRelativo());
	}

}
