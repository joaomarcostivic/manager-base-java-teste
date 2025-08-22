package com.tivic.manager.ctb;

import java.util.GregorianCalendar;

public class ContaPlanoContas {

	private int cdContaPlanoContas;
	private int cdConta;
	private int cdPlanoContas;
	private int cdContaSuperior;
	private String nrConta;
	private int nrDigito;
	private int tpConta;
	private int tpNatureza;
	private GregorianCalendar dtInativacao;
	private String txtObservacao;
	private String nrContaExterna;
	private GregorianCalendar dtCadastro;
	private float prDepreciacao;
	private int lgOrcamentaria;
	private String idConta;

	public ContaPlanoContas(int cdContaPlanoContas,
			int cdConta,
			int cdPlanoContas,
			int cdContaSuperior,
			String nrConta,
			int nrDigito,
			int tpConta,
			int tpNatureza,
			GregorianCalendar dtInativacao,
			String txtObservacao,
			String nrContaExterna,
			GregorianCalendar dtCadastro,
			float prDepreciacao,
			int lgOrcamentaria,
			String idConta){
		setCdContaPlanoContas(cdContaPlanoContas);
		setCdConta(cdConta);
		setCdPlanoContas(cdPlanoContas);
		setCdContaSuperior(cdContaSuperior);
		setNrConta(nrConta);
		setNrDigito(nrDigito);
		setTpConta(tpConta);
		setTpNatureza(tpNatureza);
		setDtInativacao(dtInativacao);
		setTxtObservacao(txtObservacao);
		setNrContaExterna(nrContaExterna);
		setDtCadastro(dtCadastro);
		setPrDepreciacao(prDepreciacao);
		setLgOrcamentaria(lgOrcamentaria);
		setIdConta(idConta);
	}
	public void setCdContaPlanoContas(int cdContaPlanoContas){
		this.cdContaPlanoContas=cdContaPlanoContas;
	}
	public int getCdContaPlanoContas(){
		return this.cdContaPlanoContas;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdPlanoContas(int cdPlanoContas){
		this.cdPlanoContas=cdPlanoContas;
	}
	public int getCdPlanoContas(){
		return this.cdPlanoContas;
	}
	public void setCdContaSuperior(int cdContaSuperior){
		this.cdContaSuperior=cdContaSuperior;
	}
	public int getCdContaSuperior(){
		return this.cdContaSuperior;
	}
	public void setNrConta(String nrConta){
		this.nrConta=nrConta;
	}
	public String getNrConta(){
		return this.nrConta;
	}
	public void setNrDigito(int nrDigito){
		this.nrDigito=nrDigito;
	}
	public int getNrDigito(){
		return this.nrDigito;
	}
	public void setTpConta(int tpConta){
		this.tpConta=tpConta;
	}
	public int getTpConta(){
		return this.tpConta;
	}
	public void setTpNatureza(int tpNatureza){
		this.tpNatureza=tpNatureza;
	}
	public int getTpNatureza(){
		return this.tpNatureza;
	}
	public void setDtInativacao(GregorianCalendar dtInativacao){
		this.dtInativacao=dtInativacao;
	}
	public GregorianCalendar getDtInativacao(){
		return this.dtInativacao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setNrContaExterna(String nrContaExterna){
		this.nrContaExterna=nrContaExterna;
	}
	public String getNrContaExterna(){
		return this.nrContaExterna;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setPrDepreciacao(float prDepreciacao){
		this.prDepreciacao=prDepreciacao;
	}
	public float getPrDepreciacao(){
		return this.prDepreciacao;
	}
	public void setLgOrcamentaria(int lgOrcamentaria){
		this.lgOrcamentaria=lgOrcamentaria;
	}
	public int getLgOrcamentaria(){
		return this.lgOrcamentaria;
	}
	public void setIdConta(String idConta){
		this.idConta=idConta;
	}
	public String getIdConta(){
		return this.idConta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContaPlanoContas: " +  getCdContaPlanoContas();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", cdPlanoContas: " +  getCdPlanoContas();
		valueToString += ", cdContaSuperior: " +  getCdContaSuperior();
		valueToString += ", nrConta: " +  getNrConta();
		valueToString += ", nrDigito: " +  getNrDigito();
		valueToString += ", tpConta: " +  getTpConta();
		valueToString += ", tpNatureza: " +  getTpNatureza();
		valueToString += ", dtInativacao: " +  sol.util.Util.formatDateTime(getDtInativacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", nrContaExterna: " +  getNrContaExterna();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", prDepreciacao: " +  getPrDepreciacao();
		valueToString += ", lgOrcamentaria: " +  getLgOrcamentaria();
		valueToString += ", idConta: " +  getIdConta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaPlanoContas(getCdContaPlanoContas(),
			getCdConta(),
			getCdPlanoContas(),
			getCdContaSuperior(),
			getNrConta(),
			getNrDigito(),
			getTpConta(),
			getTpNatureza(),
			getDtInativacao()==null ? null : (GregorianCalendar)getDtInativacao().clone(),
			getTxtObservacao(),
			getNrContaExterna(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getPrDepreciacao(),
			getLgOrcamentaria(),
			getIdConta());
	}

}
