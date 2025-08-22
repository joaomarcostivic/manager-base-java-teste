package com.tivic.manager.mob.ait.sync.entities;

public class AitSyncResponse {

	private String idAit;
	private int stSync;
	private int stExist;

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}

	public int getStSync() {
		return stSync;
	}

	public void setStSync(int stSync) {
		this.stSync = stSync;
	}
	
	public int getStExist() {
		return stExist;
	}
	
	public void setStExist(int stExist) {
		this.stExist = stExist;
	}
}
