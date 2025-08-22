package com.tivic.manager.bdv;

import java.util.GregorianCalendar;

public class Debito {

	private int cdDebito;
	private int cdVeiculo;
	private String txtDebito;
	private Double vlIpva;
	private Double vlLicenciamento;
	private Double vlMulta;
	private Double vlDpvat;
	private Double vlInfracao;
	private GregorianCalendar dtDebito;

	public Debito() { }

	public Debito(int cdDebito,
			int cdVeiculo,
			String txtDebito,
			Double vlIpva,
			Double vlLicenciamento,
			Double vlMulta,
			Double vlDpvat,
			Double vlInfracao,
			GregorianCalendar dtDebito) {
		setCdDebito(cdDebito);
		setCdVeiculo(cdVeiculo);
		setTxtDebito(txtDebito);
		setVlIpva(vlIpva);
		setVlLicenciamento(vlLicenciamento);
		setVlMulta(vlMulta);
		setVlDpvat(vlDpvat);
		setVlInfracao(vlInfracao);
		setDtDebito(dtDebito);
	}
	public void setCdDebito(int cdDebito){
		this.cdDebito=cdDebito;
	}
	public int getCdDebito(){
		return this.cdDebito;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setTxtDebito(String txtDebito){
		this.txtDebito=txtDebito;
	}
	public String getTxtDebito(){
		return this.txtDebito;
	}
	public void setVlIpva(Double vlIpva){
		this.vlIpva=vlIpva;
	}
	public Double getVlIpva(){
		return this.vlIpva;
	}
	public void setVlLicenciamento(Double vlLicenciamento){
		this.vlLicenciamento=vlLicenciamento;
	}
	public Double getVlLicenciamento(){
		return this.vlLicenciamento;
	}
	public void setVlMulta(Double vlMulta){
		this.vlMulta=vlMulta;
	}
	public Double getVlMulta(){
		return this.vlMulta;
	}
	public void setVlDpvat(Double vlDpvat){
		this.vlDpvat=vlDpvat;
	}
	public Double getVlDpvat(){
		return this.vlDpvat;
	}
	public void setVlInfracao(Double vlInfracao){
		this.vlInfracao=vlInfracao;
	}
	public Double getVlInfracao(){
		return this.vlInfracao;
	}
	public void setDtDebito(GregorianCalendar dtDebito){
		this.dtDebito=dtDebito;
	}
	public GregorianCalendar getDtDebito(){
		return this.dtDebito;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDebito: " +  getCdDebito();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", txtDebito: " +  getTxtDebito();
		valueToString += ", vlIpva: " +  getVlIpva();
		valueToString += ", vlLicenciamento: " +  getVlLicenciamento();
		valueToString += ", vlMulta: " +  getVlMulta();
		valueToString += ", vlDpvat: " +  getVlDpvat();
		valueToString += ", vlInfracao: " +  getVlInfracao();
		valueToString += ", dtDebito: " +  sol.util.Util.formatDateTime(getDtDebito(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Debito(getCdDebito(),
			getCdVeiculo(),
			getTxtDebito(),
			getVlIpva(),
			getVlLicenciamento(),
			getVlMulta(),
			getVlDpvat(),
			getVlInfracao(),
			getDtDebito()==null ? null : (GregorianCalendar)getDtDebito().clone());
	}

}