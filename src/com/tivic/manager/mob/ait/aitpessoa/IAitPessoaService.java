package com.tivic.manager.mob.ait.aitpessoa;

import com.tivic.sol.connection.CustomConnection;

public interface IAitPessoaService {
	
	void insert(AitPessoa aitPessoa) throws Exception;
	void insert(AitPessoa aitPessoa, CustomConnection customConnection) throws Exception;
	void update(AitPessoa aitPessoa) throws Exception;
	void update(AitPessoa aitPessoa, CustomConnection customConnection) throws Exception;
	boolean hasAitPessoa(String idAit) throws Exception;
	boolean hasAitPessoa(String idAit, CustomConnection customConnection) throws Exception;
}
