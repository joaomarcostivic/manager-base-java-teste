package com.tivic.manager.cae;

public class Modalidade {

	private int cdModalidade;
	private String nmModalidade;
	private int cdRecomendacaoNutricional;
	private int tpCardapioGrupo;

	public Modalidade(){ }

	public Modalidade(int cdModalidade,
			String nmModalidade,
			int cdRecomendacaoNutricional,
			int tpCardapioGrupo) {
		setCdModalidade(cdModalidade);
		setNmModalidade(nmModalidade);
		setCdRecomendacaoNutricional(cdRecomendacaoNutricional);
		setTpCardapioGrupo(tpCardapioGrupo);
	}
	public void setCdModalidade(int cdModalidade){
		this.cdModalidade=cdModalidade;
	}
	public int getCdModalidade(){
		return this.cdModalidade;
	}
	public void setNmModalidade(String nmModalidade){
		this.nmModalidade=nmModalidade;
	}
	public String getNmModalidade(){
		return this.nmModalidade;
	}
	public void setCdRecomendacaoNutricional(int cdRecomendacaoNutricional){
		this.cdRecomendacaoNutricional=cdRecomendacaoNutricional;
	}
	public int getCdRecomendacaoNutricional(){
		return this.cdRecomendacaoNutricional;
	}
	public void setTpCardapioGrupo(int tpCardapioGrupo){
		this.tpCardapioGrupo=tpCardapioGrupo;
	}
	public int getTpCardapioGrupo(){
		return this.tpCardapioGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdModalidade: " +  getCdModalidade();
		valueToString += ", nmModalidade: " +  getNmModalidade();
		valueToString += ", cdRecomendacaoNutricional: " +  getCdRecomendacaoNutricional();
		valueToString += ", tpCardapioGrupo: " +  getTpCardapioGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Modalidade(getCdModalidade(),
			getNmModalidade(),
			getCdRecomendacaoNutricional(),
			getTpCardapioGrupo());
	}

}