package com.tivic.manager.mob.ait.aitimagempessoa;

import com.tivic.manager.mob.ait.aitpessoa.AitPessoa;
import com.tivic.sol.connection.CustomConnection;

public interface IAitImagemPessoaService {
	
	void insert(AitImagemPessoa aitImagemPessoa) throws Exception;
	void insert(AitImagemPessoa aitImagemPessoa, CustomConnection customConnection) throws Exception;
	void update(AitImagemPessoa aitImagemPessoa) throws Exception;
	void update(AitImagemPessoa aitImagemPessoa, CustomConnection customConnection) throws Exception;
	public void insertImagePessoaSync(AitPessoa aitPessoa) throws Exception;
	public void insertImagePessoaSync(AitPessoa aitPessoa, CustomConnection customConnection) throws Exception;
}
