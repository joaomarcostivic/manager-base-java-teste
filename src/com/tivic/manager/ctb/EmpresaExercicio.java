package com.tivic.manager.ctb;

import java.util.GregorianCalendar;

public class EmpresaExercicio {

	private int cdEmpresa;
	private String nrAnoExercicio;
	private int cdPlanoContas;
	private int cdContador;
	private int cdEstadoCrc;
	private GregorianCalendar dtInicio;
	private GregorianCalendar dtEncerramento;
	private float vlCapitalSocial;
	private int tpCalculoIrpj;
	private String nrCrcContador;
	private int nrLivroRazao;
	private int nrPaginaRazao;
	private int nrLivroDiario;
	private int nrPaginaDiario;
	private int nrLivroCaixa;
	private int nrPaginaCaixa;
	private int tpTermos;
	private int lgLote;
	private int cdPlanoCentroCusto;
	private int stExercicio;
	private int cdResponsavelEncerramento;
	private GregorianCalendar dtTermino;
	private int cdLancamentoResultado;

	public EmpresaExercicio(int cdEmpresa,
			String nrAnoExercicio,
			int cdPlanoContas,
			int cdContador,
			int cdEstadoCrc,
			GregorianCalendar dtInicio,
			GregorianCalendar dtEncerramento,
			float vlCapitalSocial,
			int tpCalculoIrpj,
			String nrCrcContador,
			int nrLivroRazao,
			int nrPaginaRazao,
			int nrLivroDiario,
			int nrPaginaDiario,
			int nrLivroCaixa,
			int nrPaginaCaixa,
			int tpTermos,
			int lgLote,
			int cdPlanoCentroCusto,
			int stExercicio,
			int cdResponsavelEncerramento,
			GregorianCalendar dtTermino,
			int cdLancamentoResultado){
		setCdEmpresa(cdEmpresa);
		setNrAnoExercicio(nrAnoExercicio);
		setCdPlanoContas(cdPlanoContas);
		setCdContador(cdContador);
		setCdEstadoCrc(cdEstadoCrc);
		setDtInicio(dtInicio);
		setDtEncerramento(dtEncerramento);
		setVlCapitalSocial(vlCapitalSocial);
		setTpCalculoIrpj(tpCalculoIrpj);
		setNrCrcContador(nrCrcContador);
		setNrLivroRazao(nrLivroRazao);
		setNrPaginaRazao(nrPaginaRazao);
		setNrLivroDiario(nrLivroDiario);
		setNrPaginaDiario(nrPaginaDiario);
		setNrLivroCaixa(nrLivroCaixa);
		setNrPaginaCaixa(nrPaginaCaixa);
		setTpTermos(tpTermos);
		setLgLote(lgLote);
		setCdPlanoCentroCusto(cdPlanoCentroCusto);
		setStExercicio(stExercicio);
		setCdResponsavelEncerramento(cdResponsavelEncerramento);
		setDtTermino(dtTermino);
		setCdLancamentoResultado(cdLancamentoResultado);
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setNrAnoExercicio(String nrAnoExercicio){
		this.nrAnoExercicio=nrAnoExercicio;
	}
	public String getNrAnoExercicio(){
		return this.nrAnoExercicio;
	}
	public void setCdPlanoContas(int cdPlanoContas){
		this.cdPlanoContas=cdPlanoContas;
	}
	public int getCdPlanoContas(){
		return this.cdPlanoContas;
	}
	public void setCdContador(int cdContador){
		this.cdContador=cdContador;
	}
	public int getCdContador(){
		return this.cdContador;
	}
	public void setCdEstadoCrc(int cdEstadoCrc){
		this.cdEstadoCrc=cdEstadoCrc;
	}
	public int getCdEstadoCrc(){
		return this.cdEstadoCrc;
	}
	public void setDtInicio(GregorianCalendar dtInicio){
		this.dtInicio=dtInicio;
	}
	public GregorianCalendar getDtInicio(){
		return this.dtInicio;
	}
	public void setDtEncerramento(GregorianCalendar dtEncerramento){
		this.dtEncerramento=dtEncerramento;
	}
	public GregorianCalendar getDtEncerramento(){
		return this.dtEncerramento;
	}
	public void setVlCapitalSocial(float vlCapitalSocial){
		this.vlCapitalSocial=vlCapitalSocial;
	}
	public float getVlCapitalSocial(){
		return this.vlCapitalSocial;
	}
	public void setTpCalculoIrpj(int tpCalculoIrpj){
		this.tpCalculoIrpj=tpCalculoIrpj;
	}
	public int getTpCalculoIrpj(){
		return this.tpCalculoIrpj;
	}
	public void setNrCrcContador(String nrCrcContador){
		this.nrCrcContador=nrCrcContador;
	}
	public String getNrCrcContador(){
		return this.nrCrcContador;
	}
	public void setNrLivroRazao(int nrLivroRazao){
		this.nrLivroRazao=nrLivroRazao;
	}
	public int getNrLivroRazao(){
		return this.nrLivroRazao;
	}
	public void setNrPaginaRazao(int nrPaginaRazao){
		this.nrPaginaRazao=nrPaginaRazao;
	}
	public int getNrPaginaRazao(){
		return this.nrPaginaRazao;
	}
	public void setNrLivroDiario(int nrLivroDiario){
		this.nrLivroDiario=nrLivroDiario;
	}
	public int getNrLivroDiario(){
		return this.nrLivroDiario;
	}
	public void setNrPaginaDiario(int nrPaginaDiario){
		this.nrPaginaDiario=nrPaginaDiario;
	}
	public int getNrPaginaDiario(){
		return this.nrPaginaDiario;
	}
	public void setNrLivroCaixa(int nrLivroCaixa){
		this.nrLivroCaixa=nrLivroCaixa;
	}
	public int getNrLivroCaixa(){
		return this.nrLivroCaixa;
	}
	public void setNrPaginaCaixa(int nrPaginaCaixa){
		this.nrPaginaCaixa=nrPaginaCaixa;
	}
	public int getNrPaginaCaixa(){
		return this.nrPaginaCaixa;
	}
	public void setTpTermos(int tpTermos){
		this.tpTermos=tpTermos;
	}
	public int getTpTermos(){
		return this.tpTermos;
	}
	public void setLgLote(int lgLote){
		this.lgLote=lgLote;
	}
	public int getLgLote(){
		return this.lgLote;
	}
	public void setCdPlanoCentroCusto(int cdPlanoCentroCusto){
		this.cdPlanoCentroCusto=cdPlanoCentroCusto;
	}
	public int getCdPlanoCentroCusto(){
		return this.cdPlanoCentroCusto;
	}
	public void setStExercicio(int stExercicio){
		this.stExercicio=stExercicio;
	}
	public int getStExercicio(){
		return this.stExercicio;
	}
	public void setCdResponsavelEncerramento(int cdResponsavelEncerramento){
		this.cdResponsavelEncerramento=cdResponsavelEncerramento;
	}
	public int getCdResponsavelEncerramento(){
		return this.cdResponsavelEncerramento;
	}
	public void setDtTermino(GregorianCalendar dtTermino){
		this.dtTermino=dtTermino;
	}
	public GregorianCalendar getDtTermino(){
		return this.dtTermino;
	}
	public void setCdLancamentoResultado(int cdLancamentoResultado){
		this.cdLancamentoResultado=cdLancamentoResultado;
	}
	public int getCdLancamentoResultado(){
		return this.cdLancamentoResultado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", nrAnoExercicio: " +  getNrAnoExercicio();
		valueToString += ", cdPlanoContas: " +  getCdPlanoContas();
		valueToString += ", cdContador: " +  getCdContador();
		valueToString += ", cdEstadoCrc: " +  getCdEstadoCrc();
		valueToString += ", dtInicio: " +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtEncerramento: " +  sol.util.Util.formatDateTime(getDtEncerramento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlCapitalSocial: " +  getVlCapitalSocial();
		valueToString += ", tpCalculoIrpj: " +  getTpCalculoIrpj();
		valueToString += ", nrCrcContador: " +  getNrCrcContador();
		valueToString += ", nrLivroRazao: " +  getNrLivroRazao();
		valueToString += ", nrPaginaRazao: " +  getNrPaginaRazao();
		valueToString += ", nrLivroDiario: " +  getNrLivroDiario();
		valueToString += ", nrPaginaDiario: " +  getNrPaginaDiario();
		valueToString += ", nrLivroCaixa: " +  getNrLivroCaixa();
		valueToString += ", nrPaginaCaixa: " +  getNrPaginaCaixa();
		valueToString += ", tpTermos: " +  getTpTermos();
		valueToString += ", lgLote: " +  getLgLote();
		valueToString += ", cdPlanoCentroCusto: " +  getCdPlanoCentroCusto();
		valueToString += ", stExercicio: " +  getStExercicio();
		valueToString += ", cdResponsavelEncerramento: " +  getCdResponsavelEncerramento();
		valueToString += ", dtTermino: " +  sol.util.Util.formatDateTime(getDtTermino(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdLancamentoResultado: " +  getCdLancamentoResultado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EmpresaExercicio(getCdEmpresa(),
			getNrAnoExercicio(),
			getCdPlanoContas(),
			getCdContador(),
			getCdEstadoCrc(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone(),
			getDtEncerramento()==null ? null : (GregorianCalendar)getDtEncerramento().clone(),
			getVlCapitalSocial(),
			getTpCalculoIrpj(),
			getNrCrcContador(),
			getNrLivroRazao(),
			getNrPaginaRazao(),
			getNrLivroDiario(),
			getNrPaginaDiario(),
			getNrLivroCaixa(),
			getNrPaginaCaixa(),
			getTpTermos(),
			getLgLote(),
			getCdPlanoCentroCusto(),
			getStExercicio(),
			getCdResponsavelEncerramento(),
			getDtTermino()==null ? null : (GregorianCalendar)getDtTermino().clone(),
			getCdLancamentoResultado());
	}

}
