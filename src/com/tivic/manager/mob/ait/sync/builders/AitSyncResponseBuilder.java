package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.mob.ait.sync.entities.AitSyncResponse;

public class AitSyncResponseBuilder {
	
	private AitSyncResponse aitSyncResponse;
	
	public AitSyncResponseBuilder() {
		this.aitSyncResponse = new AitSyncResponse();
	}
	
	public AitSyncResponseBuilder setIdAit(String idAit) {
		this.aitSyncResponse.setIdAit(idAit);
		return this;
	}
	
	public AitSyncResponseBuilder setStSync(int stSync) {
		this.aitSyncResponse.setStSync(stSync);
		return this;
	}
	
	public AitSyncResponseBuilder setStExist(int stExist) {
		this.aitSyncResponse.setStExist(stExist);
		return this;
	}
	
	public AitSyncResponse build() {
		return this.aitSyncResponse;
	}
}
