package com.tivic.manager.mob.pessoa.juridica;

import java.util.List;

import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface PessoaJuridicaRepository {
	public PessoaJuridica insert(PessoaJuridica pessoaJuridica, CustomConnection customConnection) throws Exception;
	public PessoaJuridica update(PessoaJuridica pessoaJuridica, CustomConnection customConnection) throws Exception;
	public PessoaJuridica delete(PessoaJuridica pessoaJuridica, CustomConnection customConnection) throws Exception;
	public PessoaJuridica get(int cdPessoaJuridica) throws Exception;
	public PessoaJuridica get(int cdPessoaJuridica, CustomConnection customConnection) throws Exception;
	public List<PessoaJuridica> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception;
}
