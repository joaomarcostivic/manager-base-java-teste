package com.tivic.manager.flp;

public class TabelaSalario {

	private int cdTabelaSalario;
	private String nmTabelaSalario;
	private float vlSalario;
	private int vlCargaHoraria;
	private int lgProporcionalCarga;
	private int cdEmpresa;
	private int stTabelaSalario;
	private String idTabelaSalario;

	public TabelaSalario(){ }

	public TabelaSalario(int cdTabelaSalario,
			String nmTabelaSalario,
			float vlSalario,
			int vlCargaHoraria,
			int lgProporcionalCarga,
			int cdEmpresa,
			int stTabelaSalario,
			String idTabelaSalario){
		setCdTabelaSalario(cdTabelaSalario);
		setNmTabelaSalario(nmTabelaSalario);
		setVlSalario(vlSalario);
		setVlCargaHoraria(vlCargaHoraria);
		setLgProporcionalCarga(lgProporcionalCarga);
		setCdEmpresa(cdEmpresa);
		setStTabelaSalario(stTabelaSalario);
		setIdTabelaSalario(idTabelaSalario);
	}
	public void setCdTabelaSalario(int cdTabelaSalario){
		this.cdTabelaSalario=cdTabelaSalario;
	}
	public int getCdTabelaSalario(){
		return this.cdTabelaSalario;
	}
	public void setNmTabelaSalario(String nmTabelaSalario){
		this.nmTabelaSalario=nmTabelaSalario;
	}
	public String getNmTabelaSalario(){
		return this.nmTabelaSalario;
	}
	public void setVlSalario(float vlSalario){
		this.vlSalario=vlSalario;
	}
	public float getVlSalario(){
		return this.vlSalario;
	}
	public void setVlCargaHoraria(int vlCargaHoraria){
		this.vlCargaHoraria=vlCargaHoraria;
	}
	public int getVlCargaHoraria(){
		return this.vlCargaHoraria;
	}
	public void setLgProporcionalCarga(int lgProporcionalCarga){
		this.lgProporcionalCarga=lgProporcionalCarga;
	}
	public int getLgProporcionalCarga(){
		return this.lgProporcionalCarga;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setStTabelaSalario(int stTabelaSalario){
		this.stTabelaSalario=stTabelaSalario;
	}
	public int getStTabelaSalario(){
		return this.stTabelaSalario;
	}
	public void setIdTabelaSalario(String idTabelaSalario){
		this.idTabelaSalario=idTabelaSalario;
	}
	public String getIdTabelaSalario(){
		return this.idTabelaSalario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaSalario: " +  getCdTabelaSalario();
		valueToString += ", nmTabelaSalario: " +  getNmTabelaSalario();
		valueToString += ", vlSalario: " +  getVlSalario();
		valueToString += ", vlCargaHoraria: " +  getVlCargaHoraria();
		valueToString += ", lgProporcionalCarga: " +  getLgProporcionalCarga();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", stTabelaSalario: " +  getStTabelaSalario();
		valueToString += ", idTabelaSalario: " +  getIdTabelaSalario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaSalario(getCdTabelaSalario(),
			getNmTabelaSalario(),
			getVlSalario(),
			getVlCargaHoraria(),
			getLgProporcionalCarga(),
			getCdEmpresa(),
			getStTabelaSalario(),
			getIdTabelaSalario());
	}

}
