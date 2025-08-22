package com.tivic.manager.cae;

import java.util.GregorianCalendar;

public class Cardapio {

	private int cdCardapio;
	private String nmCardapio;
	private GregorianCalendar dtInicialValidade;
	private GregorianCalendar dtFinalValidade;
	private int cdCurso;
	private int cdRecomendacaoNutricional;
	private int cdModalidade;
	private int cdCardapioGrupo;
	private String idCardapio;

	public Cardapio(){ }

	public Cardapio(int cdCardapio,
			String nmCardapio,
			GregorianCalendar dtInicialValidade,
			GregorianCalendar dtFinalValidade,
			int cdCurso,
			int cdRecomendacaoNutricional,
			int cdModalidade,
			int cdCardapioGrupo,
			String idCardapio){
		setCdCardapio(cdCardapio);
		setNmCardapio(nmCardapio);
		setDtInicialValidade(dtInicialValidade);
		setDtFinalValidade(dtFinalValidade);
		setCdCurso(cdCurso);
		setCdRecomendacaoNutricional(cdRecomendacaoNutricional);
		setCdModalidade(cdModalidade);
		setCdCardapioGrupo(cdCardapioGrupo);
		setIdCardapio(idCardapio);
	}
	public void setCdCardapio(int cdCardapio){
		this.cdCardapio=cdCardapio;
	}
	public int getCdCardapio(){
		return this.cdCardapio;
	}
	public void setNmCardapio(String nmCardapio){
		this.nmCardapio=nmCardapio;
	}
	public String getNmCardapio(){
		return this.nmCardapio;
	}
	public void setDtInicialValidade(GregorianCalendar dtInicialValidade){
		this.dtInicialValidade=dtInicialValidade;
	}
	public GregorianCalendar getDtInicialValidade(){
		return this.dtInicialValidade;
	}
	public void setDtFinalValidade(GregorianCalendar dtFinalValidade){
		this.dtFinalValidade=dtFinalValidade;
	}
	public GregorianCalendar getDtFinalValidade(){
		return this.dtFinalValidade;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdRecomendacaoNutricional(int cdRecomendacaoNutricional){
		this.cdRecomendacaoNutricional=cdRecomendacaoNutricional;
	}
	public int getCdRecomendacaoNutricional(){
		return this.cdRecomendacaoNutricional;
	}
	public void setCdModalidade(int cdModalidade){
		this.cdModalidade=cdModalidade;
	}
	public int getCdModalidade(){
		return this.cdModalidade;
	}
	public void setCdCardapioGrupo(int cdCardapioGrupo){
		this.cdCardapioGrupo=cdCardapioGrupo;
	}
	public int getCdCardapioGrupo(){
		return this.cdCardapioGrupo;
	}
	public void setIdCardapio(String idCardapio){
		this.idCardapio=idCardapio;
	}
	public String getIdCardapio(){
		return this.idCardapio;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCardapio: " +  getCdCardapio();
		valueToString += ", nmCardapio: " +  getNmCardapio();
		valueToString += ", dtInicialValidade: " +  sol.util.Util.formatDateTime(getDtInicialValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalValidade: " +  sol.util.Util.formatDateTime(getDtFinalValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdRecomendacaoNutricional: " +  getCdRecomendacaoNutricional();
		valueToString += ", cdModalidade: " +  getCdModalidade();
		valueToString += ", cdCardapioGrupo: " +  getCdCardapioGrupo();
		valueToString += ", idCardapio: " +  getIdCardapio();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Cardapio(getCdCardapio(),
			getNmCardapio(),
			getDtInicialValidade()==null ? null : (GregorianCalendar)getDtInicialValidade().clone(),
			getDtFinalValidade()==null ? null : (GregorianCalendar)getDtFinalValidade().clone(),
			getCdCurso(),
			getCdRecomendacaoNutricional(),
			getCdModalidade(),
			getCdCardapioGrupo(),
			getIdCardapio());
	}

}