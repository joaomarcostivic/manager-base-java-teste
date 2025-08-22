package com.tivic.manager.mob.ait.sync;

import java.util.List;

import com.tivic.manager.mob.ait.sync.entities.AitSyncResponse;

public interface IAitSyncTools<T> {
	
	public List<T> verificarAitsDuplicados(List<T> aitList) throws Exception;
	public void setSincronizados(String idAit, List<AitSyncResponse> aitSyncResponseList, boolean sync, boolean stExist);

}
