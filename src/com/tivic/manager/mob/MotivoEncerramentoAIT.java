package com.tivic.manager.mob;

public class MotivoEncerramentoAIT {

	private int cdMotivoEncerramento;
	private String nmMotivoEncerramento;

	public MotivoEncerramentoAIT(){ }

	public MotivoEncerramentoAIT(int cdMotivoEncerramento,
			String nmMotivoEncerramento){
		setCdMotivoEncerramento(cdMotivoEncerramento);
		setNmMotivoEncerramento(nmMotivoEncerramento);
	}
	public void setCdMotivoEncerramento(int cdMotivoEncerramento){
		this.cdMotivoEncerramento=cdMotivoEncerramento;
	}
	public int getCdMotivoEncerramento(){
		return this.cdMotivoEncerramento;
	}
	public void setNmMotivoEncerramento(String nmMotivoEncerramento){
		this.nmMotivoEncerramento=nmMotivoEncerramento;
	}
	public String getNmMotivoEncerramento(){
		return this.nmMotivoEncerramento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMotivoEncerramento: " +  getCdMotivoEncerramento();
		valueToString += ", nmMotivoEncerramento: " +  getNmMotivoEncerramento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MotivoEncerramentoAIT(getCdMotivoEncerramento(),
			getNmMotivoEncerramento());
	}

}