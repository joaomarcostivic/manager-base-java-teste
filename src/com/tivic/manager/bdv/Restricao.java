package com.tivic.manager.bdv;

import java.util.GregorianCalendar;

public class Restricao {

	private int cdRestricao;
	private int cdVeiculo;
	private String txtRestricao;
	private int tpRestricao;
	private int stRestricao;
	private GregorianCalendar dtRestricao;

	public Restricao() { }

	public Restricao(int cdRestricao,
			int cdVeiculo,
			String txtRestricao,
			int tpRestricao,
			int stRestricao,
			GregorianCalendar dtRestricao) {
		setCdRestricao(cdRestricao);
		setCdVeiculo(cdVeiculo);
		setTxtRestricao(txtRestricao);
		setTpRestricao(tpRestricao);
		setStRestricao(stRestricao);
		setDtRestricao(dtRestricao);
	}
	public void setCdRestricao(int cdRestricao){
		this.cdRestricao=cdRestricao;
	}
	public int getCdRestricao(){
		return this.cdRestricao;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setTxtRestricao(String txtRestricao){
		this.txtRestricao=txtRestricao;
	}
	public String getTxtRestricao(){
		return this.txtRestricao;
	}
	public void setTpRestricao(int tpRestricao){
		this.tpRestricao=tpRestricao;
	}
	public int getTpRestricao(){
		return this.tpRestricao;
	}
	public void setStRestricao(int stRestricao){
		this.stRestricao=stRestricao;
	}
	public int getStRestricao(){
		return this.stRestricao;
	}
	public void setDtRestricao(GregorianCalendar dtRestricao){
		this.dtRestricao=dtRestricao;
	}
	public GregorianCalendar getDtRestricao(){
		return this.dtRestricao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRestricao: " +  getCdRestricao();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", txtRestricao: " +  getTxtRestricao();
		valueToString += ", tpRestricao: " +  getTpRestricao();
		valueToString += ", stRestricao: " +  getStRestricao();
		valueToString += ", dtRestricao: " +  sol.util.Util.formatDateTime(getDtRestricao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Restricao(getCdRestricao(),
			getCdVeiculo(),
			getTxtRestricao(),
			getTpRestricao(),
			getStRestricao(),
			getDtRestricao()==null ? null : (GregorianCalendar)getDtRestricao().clone());
	}

}