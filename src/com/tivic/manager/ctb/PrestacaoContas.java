package com.tivic.manager.ctb;

import java.util.GregorianCalendar;

public class PrestacaoContas {

	private int cdPrestacaoContas;
	private int cdPrestacaoContasConsolidada;
	private Double vlSaldoAnterior;
	private Double vlRecebido;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private int cdUnidadeExecutora;
	private int cdInstituicao;
	private int cdExercicio;
	private Double vlDevolvido;
	private Double vlRendimento;
	private Double vlRecursoProprio;
	private Double vlTotalReceita;
	private Double vlTotalDespesa;
	private int tpDestinacaoSaldo;
	private GregorianCalendar dtPrestacaoContas;
	private int stPrestacaoContas;
	private int cdResponsavel;
	private int cdSupervisor;
	private int cdPrograma;
	private String txtParecer;

	public PrestacaoContas(){ }

	public PrestacaoContas(int cdPrestacaoContas,
			int cdPrestacaoContasConsolidada,
			Double vlSaldoAnterior,
			Double vlRecebido,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			int cdUnidadeExecutora,
			int cdInstituicao,
			int cdExercicio,
			Double vlDevolvido,
			Double vlRendimento,
			Double vlRecursoProprio,
			Double vlTotalReceita,
			Double vlTotalDespesa,
			int tpDestinacaoSaldo,
			GregorianCalendar dtPrestacaoContas,
			int stPrestacaoContas,
			int cdResponsavel,
			int cdSupervisor,
			int cdPrograma,
			String txtParecer){
		setCdPrestacaoContas(cdPrestacaoContas);
		setCdPrestacaoContasConsolidada(cdPrestacaoContasConsolidada);
		setVlSaldoAnterior(vlSaldoAnterior);
		setVlRecebido(vlRecebido);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setCdUnidadeExecutora(cdUnidadeExecutora);
		setCdInstituicao(cdInstituicao);
		setCdExercicio(cdExercicio);
		setVlDevolvido(vlDevolvido);
		setVlRendimento(vlRendimento);
		setVlRecursoProprio(vlRecursoProprio);
		setVlTotalReceita(vlTotalReceita);
		setVlTotalDespesa(vlTotalDespesa);
		setTpDestinacaoSaldo(tpDestinacaoSaldo);
		setDtPrestacaoContas(dtPrestacaoContas);
		setStPrestacaoContas(stPrestacaoContas);
		setCdResponsavel(cdResponsavel);
		setCdSupervisor(cdSupervisor);
		setCdPrograma(cdPrograma);
		setTxtParecer(txtParecer);
	}
	public void setCdPrestacaoContas(int cdPrestacaoContas){
		this.cdPrestacaoContas=cdPrestacaoContas;
	}
	public int getCdPrestacaoContas(){
		return this.cdPrestacaoContas;
	}
	public void setCdPrestacaoContasConsolidada(int cdPrestacaoContasConsolidada){
		this.cdPrestacaoContasConsolidada=cdPrestacaoContasConsolidada;
	}
	public int getCdPrestacaoContasConsolidada(){
		return this.cdPrestacaoContasConsolidada;
	}
	public void setVlSaldoAnterior(Double vlSaldoAnterior){
		this.vlSaldoAnterior=vlSaldoAnterior;
	}
	public Double getVlSaldoAnterior(){
		return this.vlSaldoAnterior;
	}
	public void setVlRecebido(Double vlRecebido){
		this.vlRecebido=vlRecebido;
	}
	public Double getVlRecebido(){
		return this.vlRecebido;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public void setCdUnidadeExecutora(int cdUnidadeExecutora){
		this.cdUnidadeExecutora=cdUnidadeExecutora;
	}
	public int getCdUnidadeExecutora(){
		return this.cdUnidadeExecutora;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdExercicio(int cdExercicio){
		this.cdExercicio=cdExercicio;
	}
	public int getCdExercicio(){
		return this.cdExercicio;
	}
	public void setVlDevolvido(Double vlDevolvido){
		this.vlDevolvido=vlDevolvido;
	}
	public Double getVlDevolvido(){
		return this.vlDevolvido;
	}
	public void setVlRendimento(Double vlRendimento){
		this.vlRendimento=vlRendimento;
	}
	public Double getVlRendimento(){
		return this.vlRendimento;
	}
	public void setVlRecursoProprio(Double vlRecursoProprio){
		this.vlRecursoProprio=vlRecursoProprio;
	}
	public Double getVlRecursoProprio(){
		return this.vlRecursoProprio;
	}
	public void setVlTotalReceita(Double vlTotalReceita){
		this.vlTotalReceita=vlTotalReceita;
	}
	public Double getVlTotalReceita(){
		return this.vlTotalReceita;
	}
	public void setVlTotalDespesa(Double vlTotalDespesa){
		this.vlTotalDespesa=vlTotalDespesa;
	}
	public Double getVlTotalDespesa(){
		return this.vlTotalDespesa;
	}
	public void setTpDestinacaoSaldo(int tpDestinacaoSaldo){
		this.tpDestinacaoSaldo=tpDestinacaoSaldo;
	}
	public int getTpDestinacaoSaldo(){
		return this.tpDestinacaoSaldo;
	}
	public void setDtPrestacaoContas(GregorianCalendar dtPrestacaoContas){
		this.dtPrestacaoContas=dtPrestacaoContas;
	}
	public GregorianCalendar getDtPrestacaoContas(){
		return this.dtPrestacaoContas;
	}
	public void setStPrestacaoContas(int stPrestacaoContas){
		this.stPrestacaoContas=stPrestacaoContas;
	}
	public int getStPrestacaoContas(){
		return this.stPrestacaoContas;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setCdSupervisor(int cdSupervisor){
		this.cdSupervisor=cdSupervisor;
	}
	public int getCdSupervisor(){
		return this.cdSupervisor;
	}
	public void setCdPrograma(int cdPrograma){
		this.cdPrograma=cdPrograma;
	}
	public int getCdPrograma(){
		return this.cdPrograma;
	}
	public void setTxtParecer(String txtParecer){
		this.txtParecer=txtParecer;
	}
	public String getTxtParecer(){
		return this.txtParecer;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPrestacaoContas: " +  getCdPrestacaoContas();
		valueToString += ", cdPrestacaoContasConsolidada: " +  getCdPrestacaoContasConsolidada();
		valueToString += ", vlSaldoAnterior: " +  getVlSaldoAnterior();
		valueToString += ", vlRecebido: " +  getVlRecebido();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUnidadeExecutora: " +  getCdUnidadeExecutora();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdExercicio: " +  getCdExercicio();
		valueToString += ", vlDevolvido: " +  getVlDevolvido();
		valueToString += ", vlRendimento: " +  getVlRendimento();
		valueToString += ", vlRecursoProprio: " +  getVlRecursoProprio();
		valueToString += ", vlTotalReceita: " +  getVlTotalReceita();
		valueToString += ", vlTotalDespesa: " +  getVlTotalDespesa();
		valueToString += ", tpDestinacaoSaldo: " +  getTpDestinacaoSaldo();
		valueToString += ", dtPrestacaoContas: " +  sol.util.Util.formatDateTime(getDtPrestacaoContas(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stPrestacaoContas: " +  getStPrestacaoContas();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		valueToString += ", cdSupervisor: " +  getCdSupervisor();
		valueToString += ", cdPrograma: " +  getCdPrograma();
		valueToString += ", txtParecer: " +  getTxtParecer();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PrestacaoContas(getCdPrestacaoContas(),
			getCdPrestacaoContasConsolidada(),
			getVlSaldoAnterior(),
			getVlRecebido(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getCdUnidadeExecutora(),
			getCdInstituicao(),
			getCdExercicio(),
			getVlDevolvido(),
			getVlRendimento(),
			getVlRecursoProprio(),
			getVlTotalReceita(),
			getVlTotalDespesa(),
			getTpDestinacaoSaldo(),
			getDtPrestacaoContas()==null ? null : (GregorianCalendar)getDtPrestacaoContas().clone(),
			getStPrestacaoContas(),
			getCdResponsavel(),
			getCdSupervisor(),
			getCdPrograma(),
			getTxtParecer());
	}

}