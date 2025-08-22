package com.tivic.manager.grl;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IPessoaArquivoRepository {
	PessoaArquivo insert(PessoaArquivo pessoaArquivo, CustomConnection customConnection) throws Exception;
	PessoaArquivo update(PessoaArquivo pessoaArquivo, CustomConnection customConnection) throws Exception;
	void delete(int cdArquivo, int cdPessoa, CustomConnection customConnection) throws Exception;
	PessoaArquivo get(int cdArquivo, int cdPessoa) throws Exception;
	PessoaArquivo get(int cdArquivo, int cdPessoa, CustomConnection customConnection) throws Exception;
	List<PessoaArquivo> find(SearchCriterios searchCriterios) throws IllegalArgumentException, Exception;
	List<PessoaArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
