package com.tivic.manager.pcb;

import java.util.GregorianCalendar;

import com.tivic.manager.util.Util;

public class Lacre {

	private int cdLacre;
	private String nrLacre;
	private String txtDescricao;
	private GregorianCalendar dtRegistro;
	private int cdBomba;
	
	public Lacre(int cdLacre,
			String nrLacre,
			String txtDescricao,
			GregorianCalendar dtRegistro,
			int cdBomba){
		setCdLacre(cdLacre);
		setNrLacre(nrLacre);
		setTxtDescricao(txtDescricao);
		setDtRegistro(dtRegistro);
		setCdBomba(cdBomba);
	}
	public Lacre(int cdLacre,
			String nrLacre,
			String txtDescricao,
			int cdBomba){
		setCdLacre(cdLacre);
		setNrLacre(nrLacre);
		setTxtDescricao(txtDescricao);
		setDtRegistro(Util.getDataAtual());
		setCdBomba(cdBomba);
	}
	public void setCdLacre(int cdLacre) {
		this.cdLacre = cdLacre;
	}
	public int getCdLacre() {
		return cdLacre;
	}
	public void setDtRegistro(GregorianCalendar dtRegistro) {
		this.dtRegistro = dtRegistro;
	}
	public GregorianCalendar getDtRegistro() {
		return dtRegistro;
	}
	public void setNrLacre(String nrLacre) {
		this.nrLacre = nrLacre;
	}
	public String getNrLacre() {
		return nrLacre;
	}
	public void setTxtDescricao(String txtDescricao) {
		this.txtDescricao = txtDescricao;
	}
	public String getTxtDescricao() {
		return txtDescricao;
	}
	public void setCdBomba(int cdBomba) {
		this.cdBomba = cdBomba;
	}
	public int getCdBomba() {
		return cdBomba;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLacre: " +  getCdLacre();
		valueToString += ", nrLacre: " +  getNrLacre();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		valueToString += ", dtRegistro: " +  sol.util.Util.formatDateTime(getDtRegistro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += "cdBomba: " +  getCdBomba();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Lacre(cdLacre,
				nrLacre,
				txtDescricao,
				dtRegistro,
				cdBomba);
	}

}