package com.tivic.manager.fta;

import java.util.GregorianCalendar;

public class ComponenteVeiculo extends com.tivic.manager.bpm.ComponenteReferencia {

	private int cdTipoComponente;
	private int cdMarca;
	private float qtHodometroUltimaManutencao;
	private float qtHodometroValidade;
	private float qtHodometroManutencao;
	private int tpRecorrenciaManutencao;
	private int qtIntervaloRecorrencia;
	private GregorianCalendar dtInstalacao;
	private String txtObservacao;
	private GregorianCalendar dtInicioRecorrencia;

	public ComponenteVeiculo(int cdComponente,
			int cdReferencia,
			String nmComponente,
			GregorianCalendar dtGarantia,
			GregorianCalendar dtValidade,
			GregorianCalendar dtAquisicao,
			GregorianCalendar dtBaixa,
			String nrSerie,
			int stComponente,
			int cdTipoComponente,
			int cdMarca,
			float qtHodometroUltimaManutencao,
			float qtHodometroValidade,
			float qtHodometroManutencao,
			int tpRecorrenciaManutencao,
			int qtIntervaloRecorrencia,
			GregorianCalendar dtInstalacao,
			String txtObservacao,
			GregorianCalendar dtInicioRecorrencia){
		super(cdComponente,
			cdReferencia,
			nmComponente,
			dtGarantia,
			dtValidade,
			dtAquisicao,
			dtBaixa,
			nrSerie,
			stComponente);
		setCdTipoComponente(cdTipoComponente);
		setCdMarca(cdMarca);
		setQtHodometroUltimaManutencao(qtHodometroUltimaManutencao);
		setQtHodometroValidade(qtHodometroValidade);
		setQtHodometroManutencao(qtHodometroManutencao);
		setTpRecorrenciaManutencao(tpRecorrenciaManutencao);
		setQtIntervaloRecorrencia(qtIntervaloRecorrencia);
		setDtInstalacao(dtInstalacao);
		setTxtObservacao(txtObservacao);
		setDtInicioRecorrencia(dtInicioRecorrencia);
	}
	public void setCdTipoComponente(int cdTipoComponente){
		this.cdTipoComponente=cdTipoComponente;
	}
	public int getCdTipoComponente(){
		return this.cdTipoComponente;
	}
	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public void setQtHodometroUltimaManutencao(float qtHodometroUltimaManutencao){
		this.qtHodometroUltimaManutencao=qtHodometroUltimaManutencao;
	}
	public float getQtHodometroUltimaManutencao(){
		return this.qtHodometroUltimaManutencao;
	}
	public void setQtHodometroValidade(float qtHodometroValidade){
		this.qtHodometroValidade=qtHodometroValidade;
	}
	public float getQtHodometroValidade(){
		return this.qtHodometroValidade;
	}
	public void setQtHodometroManutencao(float qtHodometroManutencao){
		this.qtHodometroManutencao=qtHodometroManutencao;
	}
	public float getQtHodometroManutencao(){
		return this.qtHodometroManutencao;
	}
	public void setTpRecorrenciaManutencao(int tpRecorrenciaManutencao){
		this.tpRecorrenciaManutencao=tpRecorrenciaManutencao;
	}
	public int getTpRecorrenciaManutencao(){
		return this.tpRecorrenciaManutencao;
	}
	public void setQtIntervaloRecorrencia(int qtIntervaloRecorrencia){
		this.qtIntervaloRecorrencia=qtIntervaloRecorrencia;
	}
	public int getQtIntervaloRecorrencia(){
		return this.qtIntervaloRecorrencia;
	}
	public void setDtInstalacao(GregorianCalendar dtInstalacao){
		this.dtInstalacao=dtInstalacao;
	}
	public GregorianCalendar getDtInstalacao(){
		return this.dtInstalacao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setDtInicioRecorrencia(GregorianCalendar dtInicioRecorrencia){
		this.dtInicioRecorrencia=dtInicioRecorrencia;
	}
	public GregorianCalendar getDtInicioRecorrencia(){
		return this.dtInicioRecorrencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdComponente: " +  getCdComponente();
		valueToString += ", cdTipoComponente: " +  getCdTipoComponente();
		valueToString += ", cdMarca: " +  getCdMarca();
		valueToString += ", qtHodometroUltimaManutencao: " +  getQtHodometroUltimaManutencao();
		valueToString += ", qtHodometroValidade: " +  getQtHodometroValidade();
		valueToString += ", qtHodometroManutencao: " +  getQtHodometroManutencao();
		valueToString += ", tpRecorrenciaManutencao: " +  getTpRecorrenciaManutencao();
		valueToString += ", qtIntervaloRecorrencia: " +  getQtIntervaloRecorrencia();
		valueToString += ", dtInstalacao: " +  sol.util.Util.formatDateTime(getDtInstalacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", dtInicioRecorrencia: " +  sol.util.Util.formatDateTime(getDtInicioRecorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ComponenteVeiculo(getCdComponente(),
			getCdReferencia(),
			getNmComponente(),
			getDtGarantia(),
			getDtValidade(),
			getDtAquisicao(),
			getDtBaixa(),
			getNrSerie(),
			getStComponente(),
			getCdTipoComponente(),
			getCdMarca(),
			getQtHodometroUltimaManutencao(),
			getQtHodometroValidade(),
			getQtHodometroManutencao(),
			getTpRecorrenciaManutencao(),
			getQtIntervaloRecorrencia(),
			getDtInstalacao()==null ? null : (GregorianCalendar)getDtInstalacao().clone(),
			getTxtObservacao(),
			getDtInicioRecorrencia()==null ? null : (GregorianCalendar)getDtInicioRecorrencia().clone());
	}

}
