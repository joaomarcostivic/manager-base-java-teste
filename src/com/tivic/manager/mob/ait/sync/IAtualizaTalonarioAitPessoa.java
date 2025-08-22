package com.tivic.manager.mob.ait.sync;

import java.util.List;

import com.tivic.manager.mob.ait.aitpessoa.AitPessoa;
import com.tivic.sol.connection.CustomConnection;

public interface IAtualizaTalonarioAitPessoa {
	void atualizar(List<AitPessoa> aitPessoaList, CustomConnection customConnection) throws Exception;
}
