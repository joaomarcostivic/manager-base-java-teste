package com.tivic.manager.mob.ait.sync;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ait.sync.entities.AitSyncResponse;
import com.tivic.manager.mob.ait.sync.entities.SyncResponse;
import com.tivic.sol.connection.CustomConnection;

public interface IAitSyncService {
	List<AitSyncResponse> syncReceive(List<Ait> aitList) throws Exception;
	List<AitSyncResponse> syncReceive(List<Ait> aitList, List<Integer> cdAitPersistedList, CustomConnection customConnection) throws Exception;
	List<SyncResponse<?>> sync(int cdAgente) throws Exception;
	List<SyncResponse<?>> sync(int cdAgente, CustomConnection customConnection) throws Exception;
}
