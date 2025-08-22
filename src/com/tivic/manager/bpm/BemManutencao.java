package com.tivic.manager.bpm;

import java.util.GregorianCalendar;

public class BemManutencao {

	private int cdManutencao;
	private int cdFornecedor;
	private int cdResponsavel;
	private int cdReferencia;
	private int cdDefeito;
	private GregorianCalendar dtManutencao;
	private float vlPrevisto;
	private GregorianCalendar dtPrevista;
	private String txtObservacao;
	private int stManutencao;
	private String nrOs;
	private GregorianCalendar dtNotificacao;
	private GregorianCalendar dtAtendimento;
	private String txtRelatorioTecnico;
	private String txtAvaliacao;
	private String txtProblema;
	private float vlTotal;
	private int tpManutencao;
	private int cdAgendamento;

	public BemManutencao(int cdManutencao,
			int cdFornecedor,
			int cdResponsavel,
			int cdReferencia,
			int cdDefeito,
			GregorianCalendar dtManutencao,
			float vlPrevisto,
			GregorianCalendar dtPrevista,
			String txtObservacao,
			int stManutencao,
			String nrOs,
			GregorianCalendar dtNotificacao,
			GregorianCalendar dtAtendimento,
			String txtRelatorioTecnico,
			String txtAvaliacao,
			String txtProblema,
			float vlTotal,
			int tpManutencao,
			int cdAgendamento){
		setCdManutencao(cdManutencao);
		setCdFornecedor(cdFornecedor);
		setCdResponsavel(cdResponsavel);
		setCdReferencia(cdReferencia);
		setCdDefeito(cdDefeito);
		setDtManutencao(dtManutencao);
		setVlPrevisto(vlPrevisto);
		setDtPrevista(dtPrevista);
		setTxtObservacao(txtObservacao);
		setStManutencao(stManutencao);
		setNrOs(nrOs);
		setDtNotificacao(dtNotificacao);
		setDtAtendimento(dtAtendimento);
		setTxtRelatorioTecnico(txtRelatorioTecnico);
		setTxtAvaliacao(txtAvaliacao);
		setTxtProblema(txtProblema);
		setVlTotal(vlTotal);
		setTpManutencao(tpManutencao);
		setCdAgendamento(cdAgendamento);
	}
	public void setCdManutencao(int cdManutencao){
		this.cdManutencao=cdManutencao;
	}
	public int getCdManutencao(){
		return this.cdManutencao;
	}
	public void setCdFornecedor(int cdFornecedor){
		this.cdFornecedor=cdFornecedor;
	}
	public int getCdFornecedor(){
		return this.cdFornecedor;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setCdDefeito(int cdDefeito){
		this.cdDefeito=cdDefeito;
	}
	public int getCdDefeito(){
		return this.cdDefeito;
	}
	public void setDtManutencao(GregorianCalendar dtManutencao){
		this.dtManutencao=dtManutencao;
	}
	public GregorianCalendar getDtManutencao(){
		return this.dtManutencao;
	}
	public void setVlPrevisto(float vlPrevisto){
		this.vlPrevisto=vlPrevisto;
	}
	public float getVlPrevisto(){
		return this.vlPrevisto;
	}
	public void setDtPrevista(GregorianCalendar dtPrevista){
		this.dtPrevista=dtPrevista;
	}
	public GregorianCalendar getDtPrevista(){
		return this.dtPrevista;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setStManutencao(int stManutencao){
		this.stManutencao=stManutencao;
	}
	public int getStManutencao(){
		return this.stManutencao;
	}
	public void setNrOs(String nrOs){
		this.nrOs=nrOs;
	}
	public String getNrOs(){
		return this.nrOs;
	}
	public void setDtNotificacao(GregorianCalendar dtNotificacao){
		this.dtNotificacao=dtNotificacao;
	}
	public GregorianCalendar getDtNotificacao(){
		return this.dtNotificacao;
	}
	public void setDtAtendimento(GregorianCalendar dtAtendimento){
		this.dtAtendimento=dtAtendimento;
	}
	public GregorianCalendar getDtAtendimento(){
		return this.dtAtendimento;
	}
	public void setTxtRelatorioTecnico(String txtRelatorioTecnico){
		this.txtRelatorioTecnico=txtRelatorioTecnico;
	}
	public String getTxtRelatorioTecnico(){
		return this.txtRelatorioTecnico;
	}
	public void setTxtAvaliacao(String txtAvaliacao){
		this.txtAvaliacao=txtAvaliacao;
	}
	public String getTxtAvaliacao(){
		return this.txtAvaliacao;
	}
	public void setTxtProblema(String txtProblema){
		this.txtProblema=txtProblema;
	}
	public String getTxtProblema(){
		return this.txtProblema;
	}
	public void setVlTotal(float vlTotal){
		this.vlTotal=vlTotal;
	}
	public float getVlTotal(){
		return this.vlTotal;
	}
	public void setTpManutencao(int tpManutencao){
		this.tpManutencao=tpManutencao;
	}
	public int getTpManutencao(){
		return this.tpManutencao;
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdManutencao: " +  getCdManutencao();
		valueToString += ", cdFornecedor: " +  getCdFornecedor();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		valueToString += ", cdDefeito: " +  getCdDefeito();
		valueToString += ", dtManutencao: " +  sol.util.Util.formatDateTime(getDtManutencao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlPrevisto: " +  getVlPrevisto();
		valueToString += ", dtPrevista: " +  sol.util.Util.formatDateTime(getDtPrevista(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", stManutencao: " +  getStManutencao();
		valueToString += ", nrOs: " +  getNrOs();
		valueToString += ", dtNotificacao: " +  sol.util.Util.formatDateTime(getDtNotificacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAtendimento: " +  sol.util.Util.formatDateTime(getDtAtendimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtRelatorioTecnico: " +  getTxtRelatorioTecnico();
		valueToString += ", txtAvaliacao: " +  getTxtAvaliacao();
		valueToString += ", txtProblema: " +  getTxtProblema();
		valueToString += ", vlTotal: " +  getVlTotal();
		valueToString += ", tpManutencao: " +  getTpManutencao();
		valueToString += ", cdAgendamento: " +  getCdAgendamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BemManutencao(getCdManutencao(),
			getCdFornecedor(),
			getCdResponsavel(),
			getCdReferencia(),
			getCdDefeito(),
			getDtManutencao()==null ? null : (GregorianCalendar)getDtManutencao().clone(),
			getVlPrevisto(),
			getDtPrevista()==null ? null : (GregorianCalendar)getDtPrevista().clone(),
			getTxtObservacao(),
			getStManutencao(),
			getNrOs(),
			getDtNotificacao()==null ? null : (GregorianCalendar)getDtNotificacao().clone(),
			getDtAtendimento()==null ? null : (GregorianCalendar)getDtAtendimento().clone(),
			getTxtRelatorioTecnico(),
			getTxtAvaliacao(),
			getTxtProblema(),
			getVlTotal(),
			getTpManutencao(),
			getCdAgendamento());
	}

}
