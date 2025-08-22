package com.tivic.manager.mob.ait.sync;

import java.util.List;

import com.tivic.manager.mob.ait.aitpessoa.AitPessoa;
import com.tivic.manager.mob.ait.sync.entities.AitSyncResponse;
import com.tivic.sol.connection.CustomConnection;

public interface IAitPessoaSyncService {
	List<AitSyncResponse> syncReceive(List<AitPessoa> aitPessoaList) throws Exception;
	List<AitSyncResponse> syncReceive(List<AitPessoa> aitPessoaList, CustomConnection customConnection) throws Exception;
}
