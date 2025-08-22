package com.tivic.manager.fta;

import java.util.GregorianCalendar;

public class Ocorrencia extends com.tivic.manager.grl.Ocorrencia {

	private int cdVeiculo;
	private int cdComponente;
	private int lgDefeito;
	private int cdCheckupItem;
	private int cdCheckup;
	
	public Ocorrencia() {}
	
	public Ocorrencia(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int cdVeiculo,
			int cdComponente,
			int lgDefeito,
			int cdCheckupItem,
			int cdCheckup){
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setCdVeiculo(cdVeiculo);
		setCdComponente(cdComponente);
		setLgDefeito(lgDefeito);
		setCdCheckupItem(cdCheckupItem);
		setCdCheckup(cdCheckup);
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setCdComponente(int cdComponente){
		this.cdComponente=cdComponente;
	}
	public int getCdComponente(){
		return this.cdComponente;
	}
	public void setLgDefeito(int lgDefeito){
		this.lgDefeito=lgDefeito;
	}
	public int getLgDefeito(){
		return this.lgDefeito;
	}
	public void setCdCheckupItem(int cdCheckupItem){
		this.cdCheckupItem=cdCheckupItem;
	}
	public int getCdCheckupItem(){
		return this.cdCheckupItem;
	}
	public void setCdCheckup(int cdCheckup){
		this.cdCheckup=cdCheckup;
	}
	public int getCdCheckup(){
		return this.cdCheckup;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", cdComponente: " +  getCdComponente();
		valueToString += ", lgDefeito: " +  getLgDefeito();
		valueToString += ", cdCheckupItem: " +  getCdCheckupItem();
		valueToString += ", cdCheckup: " +  getCdCheckup();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Ocorrencia(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario(),
			getCdVeiculo(),
			getCdComponente(),
			getLgDefeito(),
			getCdCheckupItem(),
			getCdCheckup());
	}

}
