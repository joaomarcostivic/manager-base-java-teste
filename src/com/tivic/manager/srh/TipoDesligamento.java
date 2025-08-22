package com.tivic.manager.srh;

public class TipoDesligamento {

	private int cdTipoDesligamento;
	private String nmTipoDesligamento;
	private String idTipoDesligamento;
	private int tpAvisoPrevio;
	private String nrSefip;

	public TipoDesligamento(){}
	public TipoDesligamento(int cdTipoDesligamento,
			String nmTipoDesligamento,
			String idTipoDesligamento,
			int tpAvisoPrevio,
			String nrSefip){
		setCdTipoDesligamento(cdTipoDesligamento);
		setNmTipoDesligamento(nmTipoDesligamento);
		setIdTipoDesligamento(idTipoDesligamento);
		setTpAvisoPrevio(tpAvisoPrevio);
		setNrSefip(nrSefip);
	}
	public void setCdTipoDesligamento(int cdTipoDesligamento){
		this.cdTipoDesligamento=cdTipoDesligamento;
	}
	public int getCdTipoDesligamento(){
		return this.cdTipoDesligamento;
	}
	public void setNmTipoDesligamento(String nmTipoDesligamento){
		this.nmTipoDesligamento=nmTipoDesligamento;
	}
	public String getNmTipoDesligamento(){
		return this.nmTipoDesligamento;
	}
	public void setIdTipoDesligamento(String idTipoDesligamento){
		this.idTipoDesligamento=idTipoDesligamento;
	}
	public String getIdTipoDesligamento(){
		return this.idTipoDesligamento;
	}
	public void setTpAvisoPrevio(int tpAvisoPrevio){
		this.tpAvisoPrevio=tpAvisoPrevio;
	}
	public int getTpAvisoPrevio(){
		return this.tpAvisoPrevio;
	}
	public void setNrSefip(String nrSefip){
		this.nrSefip=nrSefip;
	}
	public String getNrSefip(){
		return this.nrSefip;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDesligamento: " +  getCdTipoDesligamento();
		valueToString += ", nmTipoDesligamento: " +  getNmTipoDesligamento();
		valueToString += ", idTipoDesligamento: " +  getIdTipoDesligamento();
		valueToString += ", tpAvisoPrevio: " +  getTpAvisoPrevio();
		valueToString += ", nrSefip: " +  getNrSefip();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoDesligamento(getCdTipoDesligamento(),
			getNmTipoDesligamento(),
			getIdTipoDesligamento(),
			getTpAvisoPrevio(),
			getNrSefip());
	}

}