package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class EquipamentoHistorico {

	private int cdEquipamento;
	private int cdHistorico;
	private int cdUsuario;
	private GregorianCalendar dtHistorico;
	private String txtHistorico;
	private int tpHistorico;
	private double vlLatitude;
	private double vlLongitude;

	public EquipamentoHistorico(){ }

	public EquipamentoHistorico(int cdEquipamento,
			int cdHistorico,
			int cdUsuario,
			GregorianCalendar dtHistorico,
			String txtHistorico,
			int tpHistorico,
			double vlLatitude,
			double vlLongitude){
		setCdEquipamento(cdEquipamento);
		setCdHistorico(cdHistorico);
		setCdUsuario(cdUsuario);
		setDtHistorico(dtHistorico);
		setTxtHistorico(txtHistorico);
		setTpHistorico(tpHistorico);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
	}
	public void setCdEquipamento(int cdEquipamento){
		this.cdEquipamento=cdEquipamento;
	}
	public int getCdEquipamento(){
		return this.cdEquipamento;
	}
	public void setCdHistorico(int cdHistorico){
		this.cdHistorico=cdHistorico;
	}
	public int getCdHistorico(){
		return this.cdHistorico;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setDtHistorico(GregorianCalendar dtHistorico){
		this.dtHistorico=dtHistorico;
	}
	public GregorianCalendar getDtHistorico(){
		return this.dtHistorico;
	}
	public void setTxtHistorico(String txtHistorico){
		this.txtHistorico=txtHistorico;
	}
	public String getTxtHistorico(){
		return this.txtHistorico;
	}
	public void setTpHistorico(int tpHistorico){
		this.tpHistorico=tpHistorico;
	}
	public int getTpHistorico(){
		return this.tpHistorico;
	}
	public void setVlLatitude(double vlLatitude){
		this.vlLatitude=vlLatitude;
	}
	public double getVlLatitude(){
		return this.vlLatitude;
	}
	public void setVlLongitude(double vlLongitude){
		this.vlLongitude=vlLongitude;
	}
	public double getVlLongitude(){
		return this.vlLongitude;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEquipamento: " +  getCdEquipamento();
		valueToString += ", cdHistorico: " +  getCdHistorico();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", dtHistorico: " +  sol.util.Util.formatDateTime(getDtHistorico(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtHistorico: " +  getTxtHistorico();
		valueToString += ", tpHistorico: " +  getTpHistorico();
		valueToString += ", vlLatitude: " +  getVlLatitude();
		valueToString += ", vlLongitude: " +  getVlLongitude();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EquipamentoHistorico(getCdEquipamento(),
			getCdHistorico(),
			getCdUsuario(),
			getDtHistorico()==null ? null : (GregorianCalendar)getDtHistorico().clone(),
			getTxtHistorico(),
			getTpHistorico(),
			getVlLatitude(),
			getVlLongitude());
	}

}