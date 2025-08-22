package com.tivic.manager.str;

import java.util.GregorianCalendar;

public class MarcaModelo {

	private int cdMarca;
	private String nmMarca;
	private String nmModelo;
	private GregorianCalendar dtAtualizacao;

	public MarcaModelo(){ }

	public MarcaModelo(int cdMarca,
			String nmMarca,
			String nmModelo,
			GregorianCalendar dtAtualizacao){
		setCdMarca(cdMarca);
		setNmMarca(nmMarca);
		setNmModelo(nmModelo);
		setDtAtualizacao(dtAtualizacao);
	}
	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public void setNmMarca(String nmMarca){
		this.nmMarca=nmMarca;
	}
	public String getNmMarca(){
		return this.nmMarca;
	}
	public void setNmModelo(String nmModelo){
		this.nmModelo=nmModelo;
	}
	public String getNmModelo(){
		return this.nmModelo;
	}
	public GregorianCalendar getDtAtualizacao() {
		return dtAtualizacao;
	}
	public void setDtAtualizacao(GregorianCalendar dtAtualizacao) {
		this.dtAtualizacao = dtAtualizacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMarca: " +  getCdMarca();
		valueToString += ", nmMarca: " +  getNmMarca();
		valueToString += ", nmModelo: " +  getNmModelo();
		valueToString += ", dtAtualizacao: " +  sol.util.Util.formatDateTime(getDtAtualizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MarcaModelo(getCdMarca(),
			getNmMarca(),
			getNmModelo(),
			getDtAtualizacao());
	}

}