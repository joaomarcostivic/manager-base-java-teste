package com.tivic.manager.seg;

import java.util.GregorianCalendar;

public class ChaveLicenca {

	private int cdChave;
	private int cdLicenca;
	private String txtChave;
	private GregorianCalendar dtCriacao;
	private GregorianCalendar dtExpiracao;

	public ChaveLicenca(){ }

	public ChaveLicenca(int cdChave,
			int cdLicenca,
			String txtChave,
			GregorianCalendar dtCriacao,
			GregorianCalendar dtExpiracao){
		setCdChave(cdChave);
		setCdLicenca(cdLicenca);
		setTxtChave(txtChave);
		setDtCriacao(dtCriacao);
		setDtExpiracao(dtExpiracao);
		
//		System.out.println(this.toString());
	}
	public void setCdChave(int cdChave){
		this.cdChave=cdChave;
	}
	public int getCdChave(){
		return this.cdChave;
	}
	public void setCdLicenca(int cdLicenca){
		this.cdLicenca=cdLicenca;
	}
	public int getCdLicenca(){
		return this.cdLicenca;
	}
	public void setTxtChave(String txtChave){
		this.txtChave=txtChave;
	}
	public String getTxtChave(){
		return this.txtChave;
	}
	public void setDtCriacao(GregorianCalendar dtCriacao){
		this.dtCriacao=dtCriacao;
	}
	public GregorianCalendar getDtCriacao(){
		return this.dtCriacao;
	}
	public void setDtExpiracao(GregorianCalendar dtExpiracao){
		this.dtExpiracao=dtExpiracao;
	}
	public GregorianCalendar getDtExpiracao(){
		return this.dtExpiracao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdChave: " +  getCdChave();
		valueToString += ", cdLicenca: " +  getCdLicenca();
		valueToString += ", txtChave: " +  getTxtChave();
		valueToString += ", dtCriacao: " +  sol.util.Util.formatDateTime(getDtCriacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtExpiracao: " +  sol.util.Util.formatDateTime(getDtExpiracao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ChaveLicenca(getCdChave(),
			getCdLicenca(),
			getTxtChave(),
			getDtCriacao()==null ? null : (GregorianCalendar)getDtCriacao().clone(),
			getDtExpiracao()==null ? null : (GregorianCalendar)getDtExpiracao().clone());
	}

}
