package com.tivic.manager.grl.banco.repository;

import com.tivic.manager.grl.Banco;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface BancoRepository {
	public void insert(Banco banco, CustomConnection customConnection) throws Exception;
	public void update(Banco banco, CustomConnection customConnection) throws Exception;
	public void delete(int cdBanco, CustomConnection customConnection) throws Exception;
	public Banco get(int cdEquipamento) throws Exception;
	public Banco get(int cdEquipamento, CustomConnection customConnection) throws Exception;
	public Search<Banco> find(SearchCriterios searchCriterios) throws Exception;
	public Search<Banco> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
