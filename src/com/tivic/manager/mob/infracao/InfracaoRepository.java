package com.tivic.manager.mob.infracao;

import java.util.List;

import com.tivic.manager.mob.Infracao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface InfracaoRepository {
	public void insert(Infracao infracao, CustomConnection customConnection) throws Exception;
	public void update(Infracao infracao, CustomConnection customConnection) throws Exception;
	public Infracao get(int cdInfracao) throws Exception;
	public Infracao get(int cdInfracao, CustomConnection customConnection) throws Exception;
	public List<Infracao> find(SearchCriterios searchCriterios) throws Exception;
	public List<Infracao> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<Infracao> getInfracoesVigentes(CustomConnection customConnection) throws Exception;
	public List<Infracao> getAll(String keyword) throws Exception;
	public List<Infracao> getAll(String keyword,CustomConnection customConnection) throws Exception;
}
