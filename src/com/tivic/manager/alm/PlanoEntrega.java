package com.tivic.manager.alm;

import java.util.GregorianCalendar;

public class PlanoEntrega {

	private int cdPlano;
	private GregorianCalendar dtCadastro;
	private GregorianCalendar dtEntrega;
	private int cdMotorista;
	private int cdSupervisor;
	private String txtObservacao;
	private String idPlano;
	private int cdVeiculo;
	private int stPlano;
	private int cdUsuario;
	private int cdViagem;
	private int nrMesReferencia;

	public PlanoEntrega() { }

	public PlanoEntrega(int cdPlano,
			GregorianCalendar dtCadastro,
			GregorianCalendar dtEntrega,
			int cdMotorista,
			int cdSupervisor,
			String txtObservacao,
			String idPlano,
			int cdVeiculo,
			int stPlano,
			int cdUsuario,
			int cdViagem,
			int nrMesReferencia) {
		setCdPlano(cdPlano);
		setDtCadastro(dtCadastro);
		setDtEntrega(dtEntrega);
		setCdMotorista(cdMotorista);
		setCdSupervisor(cdSupervisor);
		setTxtObservacao(txtObservacao);
		setIdPlano(idPlano);
		setCdVeiculo(cdVeiculo);
		setStPlano(stPlano);
		setCdUsuario(cdUsuario);
		setCdViagem(cdViagem);
		setNrMesReferencia(nrMesReferencia);
	}
	public void setCdPlano(int cdPlano){
		this.cdPlano=cdPlano;
	}
	public int getCdPlano(){
		return this.cdPlano;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setDtEntrega(GregorianCalendar dtEntrega){
		this.dtEntrega=dtEntrega;
	}
	public GregorianCalendar getDtEntrega(){
		return this.dtEntrega;
	}
	public void setCdMotorista(int cdMotorista){
		this.cdMotorista=cdMotorista;
	}
	public int getCdMotorista(){
		return this.cdMotorista;
	}
	public void setCdSupervisor(int cdSupervisor){
		this.cdSupervisor=cdSupervisor;
	}
	public int getCdSupervisor(){
		return this.cdSupervisor;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setIdPlano(String idPlano){
		this.idPlano=idPlano;
	}
	public String getIdPlano(){
		return this.idPlano;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setStPlano(int stPlano){
		this.stPlano=stPlano;
	}
	public int getStPlano(){
		return this.stPlano;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdViagem(int cdViagem){
		this.cdViagem=cdViagem;
	}
	public int getCdViagem(){
		return this.cdViagem;
	}
	public void setNrMesReferencia(int nrMesReferencia){
		this.nrMesReferencia=nrMesReferencia;
	}
	public int getNrMesReferencia(){
		return this.nrMesReferencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlano: " +  getCdPlano();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtEntrega: " +  sol.util.Util.formatDateTime(getDtEntrega(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdMotorista: " +  getCdMotorista();
		valueToString += ", cdSupervisor: " +  getCdSupervisor();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", idPlano: " +  getIdPlano();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", stPlano: " +  getStPlano();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdViagem: " +  getCdViagem();
		valueToString += ", nrMesReferencia: " +  getNrMesReferencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoEntrega(getCdPlano(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getDtEntrega()==null ? null : (GregorianCalendar)getDtEntrega().clone(),
			getCdMotorista(),
			getCdSupervisor(),
			getTxtObservacao(),
			getIdPlano(),
			getCdVeiculo(),
			getStPlano(),
			getCdUsuario(),
			getCdViagem(),
			getNrMesReferencia());
	}

}