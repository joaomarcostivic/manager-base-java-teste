package com.tivic.manager.crt;

public class Situacao {
	public static final int CONTRATO = 0;
	public static final int COMISSAO = 1;

	private int cdSituacao;
	private String nmSituacao;
	private int tpSituacao;
	private int lgIgnorar;

	public Situacao(int cdSituacao,
			String nmSituacao,
			int tpSituacao,
			int lgIgnorar){
		setCdSituacao(cdSituacao);
		setNmSituacao(nmSituacao);
		setTpSituacao(tpSituacao);
		setLgIgnorar(lgIgnorar);
	}
	public void setCdSituacao(int cdSituacao){
		this.cdSituacao=cdSituacao;
	}
	public int getCdSituacao(){
		return this.cdSituacao;
	}
	public void setNmSituacao(String nmSituacao){
		this.nmSituacao=nmSituacao;
	}
	public String getNmSituacao(){
		return this.nmSituacao;
	}
	public void setTpSituacao(int tpSituacao){
		this.tpSituacao=tpSituacao;
	}
	public int getTpSituacao(){
		return this.tpSituacao;
	}
	public void setLgIgnorar(int lgIgnorar){
		this.lgIgnorar=lgIgnorar;
	}
	public int getLgIgnorar(){
		return this.lgIgnorar;
	}
}