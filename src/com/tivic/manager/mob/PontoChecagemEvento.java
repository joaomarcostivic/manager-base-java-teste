package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class PontoChecagemEvento {

	private int cdEventoChecagem;
	private int cdPontoChecagem;
	private int cdPessoa;
	private int cdVeiculo;
	private int tpEventoChecagem;
	private GregorianCalendar dtEventoChecagem;

	public PontoChecagemEvento() { }

	public PontoChecagemEvento(int cdEventoChecagem,
			int cdPontoChecagem,
			int cdPessoa,
			int cdVeiculo,
			int tpEventoChecagem,
			GregorianCalendar dtEventoChecagem) {
		setCdEventoChecagem(cdEventoChecagem);
		setCdPontoChecagem(cdPontoChecagem);
		setCdPessoa(cdPessoa);
		setCdVeiculo(cdVeiculo);
		setTpEventoChecagem(tpEventoChecagem);
		setDtEventoChecagem(dtEventoChecagem);
	}
	public void setCdEventoChecagem(int cdEventoChecagem){
		this.cdEventoChecagem=cdEventoChecagem;
	}
	public int getCdEventoChecagem(){
		return this.cdEventoChecagem;
	}
	public void setCdPontoChecagem(int cdPontoChecagem){
		this.cdPontoChecagem=cdPontoChecagem;
	}
	public int getCdPontoChecagem(){
		return this.cdPontoChecagem;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setTpEventoChecagem(int tpEventoChecagem){
		this.tpEventoChecagem=tpEventoChecagem;
	}
	public int getTpEventoChecagem(){
		return this.tpEventoChecagem;
	}
	public void setDtEventoChecagem(GregorianCalendar dtEventoChecagem){
		this.dtEventoChecagem=dtEventoChecagem;
	}
	public GregorianCalendar getDtEventoChecagem(){
		return this.dtEventoChecagem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEventoChecagem: " +  getCdEventoChecagem();
		valueToString += ", cdPontoChecagem: " +  getCdPontoChecagem();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", tpEventoChecagem: " +  getTpEventoChecagem();
		valueToString += ", dtEventoChecagem: " +  sol.util.Util.formatDateTime(getDtEventoChecagem(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PontoChecagemEvento(getCdEventoChecagem(),
			getCdPontoChecagem(),
			getCdPessoa(),
			getCdVeiculo(),
			getTpEventoChecagem(),
			getDtEventoChecagem()==null ? null : (GregorianCalendar)getDtEventoChecagem().clone());
	}

}