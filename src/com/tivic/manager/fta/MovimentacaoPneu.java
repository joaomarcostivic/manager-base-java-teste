package com.tivic.manager.fta;

import java.util.GregorianCalendar;

public class MovimentacaoPneu {

	private int cdComponentePneu;
	private int cdReferencia;
	private int cdTipoMovimentacao;
	private int qtHodometro;
	private GregorianCalendar dtMovimentacao;

	public MovimentacaoPneu(int cdComponentePneu,
			int cdReferencia,
			int cdTipoMovimentacao,
			int qtHodometro,
			GregorianCalendar dtMovimentacao){
		setCdComponentePneu(cdComponentePneu);
		setCdReferencia(cdReferencia);
		setCdTipoMovimentacao(cdTipoMovimentacao);
		setQtHodometro(qtHodometro);
		setDtMovimentacao(dtMovimentacao);
	}
	public void setCdComponentePneu(int cdComponentePneu){
		this.cdComponentePneu=cdComponentePneu;
	}
	public int getCdComponentePneu(){
		return this.cdComponentePneu;
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setCdTipoMovimentacao(int cdTipoMovimentacao){
		this.cdTipoMovimentacao=cdTipoMovimentacao;
	}
	public int getCdTipoMovimentacao(){
		return this.cdTipoMovimentacao;
	}
	public void setQtHodometro(int qtHodometro){
		this.qtHodometro=qtHodometro;
	}
	public int getQtHodometro(){
		return this.qtHodometro;
	}
	public void setDtMovimentacao(GregorianCalendar dtMovimentacao){
		this.dtMovimentacao=dtMovimentacao;
	}
	public GregorianCalendar getDtMovimentacao(){
		return this.dtMovimentacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdComponentePneu: " +  getCdComponentePneu();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		valueToString += ", cdTipoMovimentacao: " +  getCdTipoMovimentacao();
		valueToString += ", qtHodometro: " +  getQtHodometro();
		valueToString += ", dtMovimentacao: " +  sol.util.Util.formatDateTime(getDtMovimentacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MovimentacaoPneu(getCdComponentePneu(),
			getCdReferencia(),
			getCdTipoMovimentacao(),
			getQtHodometro(),
			getDtMovimentacao()==null ? null : (GregorianCalendar)getDtMovimentacao().clone());
	}

}