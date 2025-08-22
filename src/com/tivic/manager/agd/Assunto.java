package com.tivic.manager.agd;

import java.util.GregorianCalendar;

public class Assunto {

	private int cdAssunto;
	private String nmAssunto;
	private String txtAssunto;
	private int tpPrioridadeInicial;
	private GregorianCalendar dtCadastro;
	private int tpAssunto;
	private int cdAgenda;
	private int cdPessoa;

	public Assunto(int cdAssunto,
			String nmAssunto,
			String txtAssunto,
			int tpPrioridadeInicial,
			GregorianCalendar dtCadastro,
			int tpAssunto,
			int cdAgenda,
			int cdPessoa){
		setCdAssunto(cdAssunto);
		setNmAssunto(nmAssunto);
		setTxtAssunto(txtAssunto);
		setTpPrioridadeInicial(tpPrioridadeInicial);
		setDtCadastro(dtCadastro);
		setTpAssunto(tpAssunto);
		setCdAgenda(cdAgenda);
		setCdPessoa(cdPessoa);
	}
	public void setCdAssunto(int cdAssunto){
		this.cdAssunto=cdAssunto;
	}
	public int getCdAssunto(){
		return this.cdAssunto;
	}
	public void setNmAssunto(String nmAssunto){
		this.nmAssunto=nmAssunto;
	}
	public String getNmAssunto(){
		return this.nmAssunto;
	}
	public void setTxtAssunto(String txtAssunto){
		this.txtAssunto=txtAssunto;
	}
	public String getTxtAssunto(){
		return this.txtAssunto;
	}
	public void setTpPrioridadeInicial(int tpPrioridadeInicial){
		this.tpPrioridadeInicial=tpPrioridadeInicial;
	}
	public int getTpPrioridadeInicial(){
		return this.tpPrioridadeInicial;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setTpAssunto(int tpAssunto){
		this.tpAssunto=tpAssunto;
	}
	public int getTpAssunto(){
		return this.tpAssunto;
	}
	public void setCdAgenda(int cdAgenda){
		this.cdAgenda=cdAgenda;
	}
	public int getCdAgenda(){
		return this.cdAgenda;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAssunto: " +  getCdAssunto();
		valueToString += ", nmAssunto: " +  getNmAssunto();
		valueToString += ", txtAssunto: " +  getTxtAssunto();
		valueToString += ", tpPrioridadeInicial: " +  getTpPrioridadeInicial();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpAssunto: " +  getTpAssunto();
		valueToString += ", cdAgenda: " +  getCdAgenda();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Assunto(getCdAssunto(),
			getNmAssunto(),
			getTxtAssunto(),
			getTpPrioridadeInicial(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getTpAssunto(),
			getCdAgenda(),
			getCdPessoa());
	}

}
