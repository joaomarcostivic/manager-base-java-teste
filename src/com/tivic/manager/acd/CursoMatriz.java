package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class CursoMatriz {

	private int cdMatriz;
	private int cdCurso;
	private int cdPeriodoLetivo;
	private String nmMatriz;
	private int tpConceito;
	private String nmDiarioOficial;
	private String nmParecer;
	private String nmResolucao;
	private float vlConceitoMaximo;
	private float vlConceitoMinimo;
	private float prConceitoMinimo;
	private float prPresencaMinima;
	private int qtDecimalConceito;
	private GregorianCalendar dtVigenciaInicial;
	private GregorianCalendar dtVigenciaFinal;

	public CursoMatriz(){ }

	public CursoMatriz(int cdMatriz,
			int cdCurso,
			int cdPeriodoLetivo,
			String nmMatriz,
			int tpConceito,
			String nmDiarioOficial,
			String nmParecer,
			String nmResolucao,
			float vlConceitoMaximo,
			float vlConceitoMinimo,
			float prConceitoMinimo,
			float prPresencaMinima,
			int qtDecimalConceito,
			GregorianCalendar dtVigenciaInicial,
			GregorianCalendar dtVigenciaFinal){
		setCdMatriz(cdMatriz);
		setCdCurso(cdCurso);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setNmMatriz(nmMatriz);
		setTpConceito(tpConceito);
		setNmDiarioOficial(nmDiarioOficial);
		setNmParecer(nmParecer);
		setNmResolucao(nmResolucao);
		setVlConceitoMaximo(vlConceitoMaximo);
		setVlConceitoMinimo(vlConceitoMinimo);
		setPrConceitoMinimo(prConceitoMinimo);
		setPrPresencaMinima(prPresencaMinima);
		setQtDecimalConceito(qtDecimalConceito);
		setDtVigenciaInicial(dtVigenciaInicial);
		setDtVigenciaFinal(dtVigenciaFinal);
	}
	public void setCdMatriz(int cdMatriz){
		this.cdMatriz=cdMatriz;
	}
	public int getCdMatriz(){
		return this.cdMatriz;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setNmMatriz(String nmMatriz){
		this.nmMatriz=nmMatriz;
	}
	public String getNmMatriz(){
		return this.nmMatriz;
	}
	public void setTpConceito(int tpConceito){
		this.tpConceito=tpConceito;
	}
	public int getTpConceito(){
		return this.tpConceito;
	}
	public void setNmDiarioOficial(String nmDiarioOficial){
		this.nmDiarioOficial=nmDiarioOficial;
	}
	public String getNmDiarioOficial(){
		return this.nmDiarioOficial;
	}
	public void setNmParecer(String nmParecer){
		this.nmParecer=nmParecer;
	}
	public String getNmParecer(){
		return this.nmParecer;
	}
	public void setNmResolucao(String nmResolucao){
		this.nmResolucao=nmResolucao;
	}
	public String getNmResolucao(){
		return this.nmResolucao;
	}
	public void setVlConceitoMaximo(float vlConceitoMaximo){
		this.vlConceitoMaximo=vlConceitoMaximo;
	}
	public float getVlConceitoMaximo(){
		return this.vlConceitoMaximo;
	}
	public void setVlConceitoMinimo(float vlConceitoMinimo){
		this.vlConceitoMinimo=vlConceitoMinimo;
	}
	public float getVlConceitoMinimo(){
		return this.vlConceitoMinimo;
	}
	public void setPrConceitoMinimo(float prConceitoMinimo){
		this.prConceitoMinimo=prConceitoMinimo;
	}
	public float getPrConceitoMinimo(){
		return this.prConceitoMinimo;
	}
	public void setPrPresencaMinima(float prPresencaMinima){
		this.prPresencaMinima=prPresencaMinima;
	}
	public float getPrPresencaMinima(){
		return this.prPresencaMinima;
	}
	public void setQtDecimalConceito(int qtDecimalConceito){
		this.qtDecimalConceito=qtDecimalConceito;
	}
	public int getQtDecimalConceito(){
		return this.qtDecimalConceito;
	}
	public void setDtVigenciaInicial(GregorianCalendar dtVigenciaInicial){
		this.dtVigenciaInicial=dtVigenciaInicial;
	}
	public GregorianCalendar getDtVigenciaInicial(){
		return this.dtVigenciaInicial;
	}
	public void setDtVigenciaFinal(GregorianCalendar dtVigenciaFinal){
		this.dtVigenciaFinal=dtVigenciaFinal;
	}
	public GregorianCalendar getDtVigenciaFinal(){
		return this.dtVigenciaFinal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatriz: " +  getCdMatriz();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		valueToString += ", nmMatriz: " +  getNmMatriz();
		valueToString += ", tpConceito: " +  getTpConceito();
		valueToString += ", nmDiarioOficial: " +  getNmDiarioOficial();
		valueToString += ", nmParecer: " +  getNmParecer();
		valueToString += ", nmResolucao: " +  getNmResolucao();
		valueToString += ", vlConceitoMaximo: " +  getVlConceitoMaximo();
		valueToString += ", vlConceitoMinimo: " +  getVlConceitoMinimo();
		valueToString += ", prConceitoMinimo: " +  getPrConceitoMinimo();
		valueToString += ", prPresencaMinima: " +  getPrPresencaMinima();
		valueToString += ", qtDecimalConceito: " +  getQtDecimalConceito();
		valueToString += ", dtVigenciaInicial: " +  sol.util.Util.formatDateTime(getDtVigenciaInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtVigenciaFinal: " +  sol.util.Util.formatDateTime(getDtVigenciaFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CursoMatriz(getCdMatriz(),
			getCdCurso(),
			getCdPeriodoLetivo(),
			getNmMatriz(),
			getTpConceito(),
			getNmDiarioOficial(),
			getNmParecer(),
			getNmResolucao(),
			getVlConceitoMaximo(),
			getVlConceitoMinimo(),
			getPrConceitoMinimo(),
			getPrPresencaMinima(),
			getQtDecimalConceito(),
			getDtVigenciaInicial()==null ? null : (GregorianCalendar)getDtVigenciaInicial().clone(),
			getDtVigenciaFinal()==null ? null : (GregorianCalendar)getDtVigenciaFinal().clone());
	}

}