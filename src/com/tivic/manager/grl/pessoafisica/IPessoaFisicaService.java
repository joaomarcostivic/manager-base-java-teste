package com.tivic.manager.grl.pessoafisica;

import java.util.List;

import com.tivic.manager.grl.PessoaFisica;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IPessoaFisicaService {
	public PessoaFisica insert(PessoaFisica pessoaFisica) throws Exception;
	public PessoaFisica insert(PessoaFisica pessoaFisica, CustomConnection customConnection) throws Exception;
	public PessoaFisica update(PessoaFisica pessoaFisica) throws Exception;
	public PessoaFisica update(PessoaFisica pessoaFisica, CustomConnection customConnection) throws Exception;
	public PessoaFisica get(int cdPessoaFisica) throws Exception;
	public PessoaFisica get(int cdPessoaFisica, CustomConnection customConnection) throws Exception;
	public List<PessoaFisica> find(SearchCriterios searchCriterios) throws Exception;
	public List<PessoaFisica> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	PessoaFisica getPessoaFisica(int cdPessoaFisica) throws Exception;
	PessoaFisica getPessoaFisica(int cdPessoaFisica, CustomConnection customConnection) throws Exception;
}
