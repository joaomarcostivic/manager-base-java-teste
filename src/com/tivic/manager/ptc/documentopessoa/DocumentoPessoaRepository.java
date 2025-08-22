package com.tivic.manager.ptc.documentopessoa;

import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.sol.connection.CustomConnection;

public interface DocumentoPessoaRepository {

	void insert(DocumentoPessoa documentoPessoa, CustomConnection customConnection) throws Exception;
	void update(DocumentoPessoa documentoPessoa, CustomConnection customConnection) throws Exception;
	DocumentoPessoa get(int cdDocumento, int cdPessoa, CustomConnection customConnection) throws Exception;
}
