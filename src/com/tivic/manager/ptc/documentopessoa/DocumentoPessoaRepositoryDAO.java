package com.tivic.manager.ptc.documentopessoa;

import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.sol.connection.CustomConnection;

public class DocumentoPessoaRepositoryDAO implements DocumentoPessoaRepository {

	@Override
	public void insert(DocumentoPessoa documentoPessoa, CustomConnection customConnection) throws Exception {
		DocumentoPessoaDAO.insert(documentoPessoa, customConnection.getConnection());
		
	}

	@Override
	public void update(DocumentoPessoa documentoPessoa, CustomConnection customConnection) throws Exception {
		DocumentoPessoaDAO.update(documentoPessoa, customConnection.getConnection());

		
	}

	@Override
	public DocumentoPessoa get(int cdDocumento, int cdPessoa, CustomConnection customConnection) throws Exception {
		DocumentoPessoa documentoPessoa = DocumentoPessoaDAO.get(cdDocumento, cdPessoa, customConnection.getConnection());
		return documentoPessoa;
	}
}
