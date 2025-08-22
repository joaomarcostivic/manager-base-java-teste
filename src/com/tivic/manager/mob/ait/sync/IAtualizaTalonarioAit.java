package com.tivic.manager.mob.ait.sync;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.sol.connection.CustomConnection;

public interface IAtualizaTalonarioAit {

	void atualizar(List<Ait> aitList, CustomConnection customConnection) throws Exception;
}
