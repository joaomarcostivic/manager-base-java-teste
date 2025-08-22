package com.tivic.manager.fta;

import java.util.GregorianCalendar;

public class Manutencao extends com.tivic.manager.bpm.BemManutencao {

	private int cdCheckup;
	private int cdComponente;
	private int cdCheckupItem;

	public Manutencao(int cdManutencao,
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
			int cdAgendamento,
			int cdCheckup,
			int cdComponente,
			int cdCheckupItem){
		super(cdManutencao,
			cdFornecedor,
			cdResponsavel,
			cdReferencia,
			cdDefeito,
			dtManutencao,
			vlPrevisto,
			dtPrevista,
			txtObservacao,
			stManutencao,
			nrOs,
			dtNotificacao,
			dtAtendimento,
			txtRelatorioTecnico,
			txtAvaliacao,
			txtProblema,
			vlTotal,
			tpManutencao,
			cdAgendamento);
		setCdCheckup(cdCheckup);
		setCdComponente(cdComponente);
		setCdCheckupItem(cdCheckupItem);
	}
	public void setCdCheckup(int cdCheckup){
		this.cdCheckup=cdCheckup;
	}
	public int getCdCheckup(){
		return this.cdCheckup;
	}
	public void setCdComponente(int cdComponente){
		this.cdComponente=cdComponente;
	}
	public int getCdComponente(){
		return this.cdComponente;
	}
	public void setCdCheckupItem(int cdCheckupItem){
		this.cdCheckupItem=cdCheckupItem;
	}
	public int getCdCheckupItem(){
		return this.cdCheckupItem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdManutencao: " +  getCdManutencao();
		valueToString += ", cdCheckup: " +  getCdCheckup();
		valueToString += ", cdComponente: " +  getCdComponente();
		valueToString += ", cdCheckupItem: " +  getCdCheckupItem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Manutencao(getCdManutencao(),
			getCdFornecedor(),
			getCdResponsavel(),
			getCdReferencia(),
			getCdDefeito(),
			getDtManutencao(),
			getVlPrevisto(),
			getDtPrevista(),
			getTxtObservacao(),
			getStManutencao(),
			getNrOs(),
			getDtNotificacao(),
			getDtAtendimento(),
			getTxtRelatorioTecnico(),
			getTxtAvaliacao(),
			getTxtProblema(),
			getVlTotal(),
			getTpManutencao(),
			getCdAgendamento(),
			getCdCheckup(),
			getCdComponente(),
			getCdCheckupItem());
	}

}
