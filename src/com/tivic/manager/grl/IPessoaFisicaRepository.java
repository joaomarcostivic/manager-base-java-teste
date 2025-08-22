package com.tivic.manager.grl;

import java.util.List;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IPessoaFisicaRepository {
	public PessoaFisica insert(PessoaFisica pessoaFisica, CustomConnection customConnection) throws Exception;
	public PessoaFisica update(PessoaFisica pessoaFisica, CustomConnection customConnection) throws Exception;
	public PessoaFisica delete(PessoaFisica pessoaFisica, CustomConnection customConnection) throws Exception;
	public PessoaFisica get(int cdPessoaFisica) throws Exception;
	public PessoaFisica get(int cdPessoaFisica, CustomConnection customConnection) throws Exception;
	public List<PessoaFisica> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception;
	public void insertCodeSync(PessoaFisica pessoaFisica, CustomConnection customConnection) throws Exception;
	public PessoaFisica getByCpf(String nrCpfPessoaFisica, CustomConnection customConnection) throws Exception;
}