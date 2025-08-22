package com.tivic.manager.acd;

public class MotivoTrancamento {

	private int cdMotivoTrancamento;
	private String nmMotivoTrancamento;

	public MotivoTrancamento() { }
			
	public MotivoTrancamento(int cdMotivoTrancamento,
			String nmMotivoTrancamento){
		setCdMotivoTrancamento(cdMotivoTrancamento);
		setNmMotivoTrancamento(nmMotivoTrancamento);
	}
	public void setCdMotivoTrancamento(int cdMotivoTrancamento){
		this.cdMotivoTrancamento=cdMotivoTrancamento;
	}
	public int getCdMotivoTrancamento(){
		return this.cdMotivoTrancamento;
	}
	public void setNmMotivoTrancamento(String nmMotivoTrancamento){
		this.nmMotivoTrancamento=nmMotivoTrancamento;
	}
	public String getNmMotivoTrancamento(){
		return this.nmMotivoTrancamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMotivoTrancamento: " +  getCdMotivoTrancamento();
		valueToString += ", nmMotivoTrancamento: " +  getNmMotivoTrancamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MotivoTrancamento(getCdMotivoTrancamento(),
			getNmMotivoTrancamento());
	}

}
