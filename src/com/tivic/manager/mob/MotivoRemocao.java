package com.tivic.manager.mob;

public class MotivoRemocao {

	private int cdMotivoRemocao;
	private String nmMotivoRemocao;
	private String idMotivoRemocao;

	public MotivoRemocao() { }

	public MotivoRemocao(int cdMotivoRemocao,
			String nmMotivoRemocao,
			String idMotivoRemocao) {
		setCdMotivoRemocao(cdMotivoRemocao);
		setNmMotivoRemocao(nmMotivoRemocao);
		setIdMotivoRemocao(idMotivoRemocao);
	}
	public void setCdMotivoRemocao(int cdMotivoRemocao){
		this.cdMotivoRemocao=cdMotivoRemocao;
	}
	public int getCdMotivoRemocao(){
		return this.cdMotivoRemocao;
	}
	public void setNmMotivoRemocao(String nmMotivoRemocao){
		this.nmMotivoRemocao=nmMotivoRemocao;
	}
	public String getNmMotivoRemocao(){
		return this.nmMotivoRemocao;
	}
	public void setIdMotivoRemocao(String idMotivoRemocao){
		this.idMotivoRemocao=idMotivoRemocao;
	}
	public String getIdMotivoRemocao(){
		return this.idMotivoRemocao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMotivoRemocao: " +  getCdMotivoRemocao();
		valueToString += ", nmMotivoRemocao: " +  getNmMotivoRemocao();
		valueToString += ", idMotivoRemocao: " +  getIdMotivoRemocao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MotivoRemocao(getCdMotivoRemocao(),
			getNmMotivoRemocao(),
			getIdMotivoRemocao());
	}

}