package com.tivic.manager.ord;

import java.util.GregorianCalendar;

public class PlanoTrabalho {

	private int cdPlanoTrabalho;
	private GregorianCalendar dtCadastro;
	private String idPlano;
	private GregorianCalendar dtTrabalho;
	private int stPlano;
	private String txtObservacao;
	private int cdUsuario;
	private int cdSupervisor;
	private int cdMotorista;
	private int cdVeiculo;

	public PlanoTrabalho() { }

	public PlanoTrabalho(int cdPlanoTrabalho,
			GregorianCalendar dtCadastro,
			String idPlano,
			GregorianCalendar dtTrabalho,
			int stPlano,
			String txtObservacao,
			int cdUsuario,
			int cdSupervisor,
			int cdMotorista,
			int cdVeiculo) {
		setCdPlanoTrabalho(cdPlanoTrabalho);
		setDtCadastro(dtCadastro);
		setIdPlano(idPlano);
		setDtTrabalho(dtTrabalho);
		setStPlano(stPlano);
		setTxtObservacao(txtObservacao);
		setCdUsuario(cdUsuario);
		setCdSupervisor(cdSupervisor);
		setCdMotorista(cdMotorista);
		setCdVeiculo(cdVeiculo);
	}
	public void setCdPlanoTrabalho(int cdPlanoTrabalho){
		this.cdPlanoTrabalho=cdPlanoTrabalho;
	}
	public int getCdPlanoTrabalho(){
		return this.cdPlanoTrabalho;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setIdPlano(String idPlano){
		this.idPlano=idPlano;
	}
	public String getIdPlano(){
		return this.idPlano;
	}
	public void setDtTrabalho(GregorianCalendar dtTrabalho){
		this.dtTrabalho=dtTrabalho;
	}
	public GregorianCalendar getDtTrabalho(){
		return this.dtTrabalho;
	}
	public void setStPlano(int stPlano){
		this.stPlano=stPlano;
	}
	public int getStPlano(){
		return this.stPlano;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdSupervisor(int cdSupervisor){
		this.cdSupervisor=cdSupervisor;
	}
	public int getCdSupervisor(){
		return this.cdSupervisor;
	}
	public void setCdMotorista(int cdMotorista){
		this.cdMotorista=cdMotorista;
	}
	public int getCdMotorista(){
		return this.cdMotorista;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlanoTrabalho: " +  getCdPlanoTrabalho();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idPlano: " +  getIdPlano();
		valueToString += ", dtTrabalho: " +  sol.util.Util.formatDateTime(getDtTrabalho(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stPlano: " +  getStPlano();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdSupervisor: " +  getCdSupervisor();
		valueToString += ", cdMotorista: " +  getCdMotorista();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoTrabalho(getCdPlanoTrabalho(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getIdPlano(),
			getDtTrabalho()==null ? null : (GregorianCalendar)getDtTrabalho().clone(),
			getStPlano(),
			getTxtObservacao(),
			getCdUsuario(),
			getCdSupervisor(),
			getCdMotorista(),
			getCdVeiculo());
	}

}