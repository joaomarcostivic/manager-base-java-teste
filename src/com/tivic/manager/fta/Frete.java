package com.tivic.manager.fta;

import java.util.GregorianCalendar;

public class Frete {

	private int cdFrete;
	private int cdCliente;
	private int lgFreteDestinatario;
	private GregorianCalendar dtEntrega;
	private float vlTotal;
	private int cdViagem;

	public Frete(int cdFrete,
			int cdCliente,
			int lgFreteDestinatario,
			GregorianCalendar dtEntrega,
			float vlTotal,
			int cdViagem){
		setCdFrete(cdFrete);
		setCdCliente(cdCliente);
		setLgFreteDestinatario(lgFreteDestinatario);
		setDtEntrega(dtEntrega);
		setVlTotal(vlTotal);
		setCdViagem(cdViagem);
	}
	public void setCdFrete(int cdFrete){
		this.cdFrete=cdFrete;
	}
	public int getCdFrete(){
		return this.cdFrete;
	}
	public void setCdCliente(int cdCliente){
		this.cdCliente=cdCliente;
	}
	public int getCdCliente(){
		return this.cdCliente;
	}
	public void setLgFreteDestinatario(int lgFreteDestinatario){
		this.lgFreteDestinatario=lgFreteDestinatario;
	}
	public int getLgFreteDestinatario(){
		return this.lgFreteDestinatario;
	}
	public void setDtEntrega(GregorianCalendar dtEntrega){
		this.dtEntrega=dtEntrega;
	}
	public GregorianCalendar getDtEntrega(){
		return this.dtEntrega;
	}
	public void setVlTotal(float vlTotal){
		this.vlTotal=vlTotal;
	}
	public float getVlTotal(){
		return this.vlTotal;
	}
	public void setCdViagem(int cdViagem){
		this.cdViagem=cdViagem;
	}
	public int getCdViagem(){
		return this.cdViagem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFrete: " +  getCdFrete();
		valueToString += ", cdCliente: " +  getCdCliente();
		valueToString += ", lgFreteDestinatario: " +  getLgFreteDestinatario();
		valueToString += ", dtEntrega: " +  sol.util.Util.formatDateTime(getDtEntrega(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlTotal: " +  getVlTotal();
		valueToString += ", cdViagem: " +  getCdViagem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Frete(getCdFrete(),
			getCdCliente(),
			getLgFreteDestinatario(),
			getDtEntrega()==null ? null : (GregorianCalendar)getDtEntrega().clone(),
			getVlTotal(),
			getCdViagem());
	}

}