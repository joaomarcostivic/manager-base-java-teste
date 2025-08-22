package com.tivic.manager.srh;

import java.util.GregorianCalendar;

public class Lotacao {

	private int cdLotacao;
	private int cdMatricula;
	private int cdSetor;
	private int cdFuncao;
	private GregorianCalendar dtLotacao;
	private GregorianCalendar dtRemocao;
	private int vlCargaHoraria;
	private String idLotacao;
	
	public Lotacao(){ }

	public Lotacao(int cdLotacao,
			int cdMatricula,
			int cdSetor,
			int cdFuncao,
			GregorianCalendar dtLotacao,
			GregorianCalendar dtRemocao,
			int vlCargaHoraria,
			String idLotacao){
		setCdLotacao(cdLotacao);
		setCdMatricula(cdMatricula);
		setCdSetor(cdSetor);
		setCdFuncao(cdFuncao);
		setDtLotacao(dtLotacao);
		setDtRemocao(dtRemocao);
		setVlCargaHoraria(vlCargaHoraria);
		setIdLotacao(idLotacao);
	}
	public void setCdLotacao(int cdLotacao){
		this.cdLotacao=cdLotacao;
	}
	public int getCdLotacao(){
		return this.cdLotacao;
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setCdFuncao(int cdFuncao){
		this.cdFuncao=cdFuncao;
	}
	public int getCdFuncao(){
		return this.cdFuncao;
	}
	public void setDtLotacao(GregorianCalendar dtLotacao){
		this.dtLotacao=dtLotacao;
	}
	public GregorianCalendar getDtLotacao(){
		return this.dtLotacao;
	}
	public void setDtRemocao(GregorianCalendar dtRemocao){
		this.dtRemocao=dtRemocao;
	}
	public GregorianCalendar getDtRemocao(){
		return this.dtRemocao;
	}
	public void setVlCargaHoraria(int vlCargaHoraria){
		this.vlCargaHoraria=vlCargaHoraria;
	}
	public int getVlCargaHoraria(){
		return this.vlCargaHoraria;
	}
	public void setIdLotacao(String idLotacao) {
		this.idLotacao = idLotacao;
	}
	public String getIdLotacao() {
		return idLotacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLotacao: " +  getCdLotacao();
		valueToString += ", cdMatricula: " +  getCdMatricula();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", cdFuncao: " +  getCdFuncao();
		valueToString += ", dtLotacao: " +  sol.util.Util.formatDateTime(getDtLotacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtRemocao: " +  sol.util.Util.formatDateTime(getDtRemocao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlCargaHoraria: " +  getVlCargaHoraria();
		valueToString += ", idLotacao: " +  getIdLotacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Lotacao(getCdLotacao(),
			getCdMatricula(),
			getCdSetor(),
			getCdFuncao(),
			getDtLotacao()==null ? null : (GregorianCalendar)getDtLotacao().clone(),
			getDtRemocao()==null ? null : (GregorianCalendar)getDtRemocao().clone(),
			getVlCargaHoraria(),
			getIdLotacao());
	}

}
