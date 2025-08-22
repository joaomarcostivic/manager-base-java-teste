package com.tivic.manager.grl.pessoavinculohistorico.repository;

import java.util.List;

import com.tivic.manager.grl.pessoavinculohistorico.PessoaVinculoHistorico;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface PessoaVinculoHistoricoRepository {
	public void insert(PessoaVinculoHistorico pessoaVinculoHistorico, CustomConnection customConnection) throws Exception;
	public void update(PessoaVinculoHistorico pessoaVinculoHistorico, CustomConnection customConnection) throws Exception;
	public PessoaVinculoHistorico get(int cdVinculoHistorico) throws Exception;
	public PessoaVinculoHistorico get(int cdVinculoHistorico, CustomConnection customConnection) throws Exception;
	public List<PessoaVinculoHistorico> find(SearchCriterios searchCriterios) throws Exception;
	public List<PessoaVinculoHistorico> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;

}
