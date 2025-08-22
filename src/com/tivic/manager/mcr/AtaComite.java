package com.tivic.manager.mcr;

import java.util.GregorianCalendar;

public class AtaComite {

	private int cdAtaComite;
	private GregorianCalendar dtAta;
	private String txtObservacao;

	public AtaComite(int cdAtaComite,
			GregorianCalendar dtAta,
			String txtObservacao){
		setCdAtaComite(cdAtaComite);
		setDtAta(dtAta);
		setTxtObservacao(txtObservacao);
	}
	public void setCdAtaComite(int cdAtaComite){
		this.cdAtaComite=cdAtaComite;
	}
	public int getCdAtaComite(){
		return this.cdAtaComite;
	}
	public void setDtAta(GregorianCalendar dtAta){
		this.dtAta=dtAta;
	}
	public GregorianCalendar getDtAta(){
		return this.dtAta;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAtaComite: " +  getCdAtaComite();
		valueToString += ", dtAta: " +  sol.util.Util.formatDateTime(getDtAta(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AtaComite(getCdAtaComite(),
			getDtAta()==null ? null : (GregorianCalendar)getDtAta().clone(),
			getTxtObservacao());
	}

}
