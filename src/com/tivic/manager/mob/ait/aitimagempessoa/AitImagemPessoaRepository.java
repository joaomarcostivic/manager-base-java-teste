package com.tivic.manager.mob.ait.aitimagempessoa;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface AitImagemPessoaRepository {

	void insert(AitImagemPessoa aitImagemPessoa, CustomConnection customConnection) throws Exception;
	void update(AitImagemPessoa aitImagemPessoa, CustomConnection customConnection) throws Exception;
	AitImagemPessoa get(int cdAitImagem, CustomConnection customConnection) throws Exception;
	List<AitImagemPessoa> find(SearchCriterios searchCriterios) throws Exception;
	List<AitImagemPessoa> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
