package com.tivic.manager.mob.edat.repositories;

import java.util.List;

import com.tivic.manager.mob.edat.TermosECondicoes;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ITermosECondicoesService {
	TermosECondicoes get(int cdTermo) throws Exception;
	TermosECondicoes get(int cdTermo, CustomConnection customConnection) throws Exception;
	List<TermosECondicoes> find(SearchCriterios searchCriterios) throws Exception;
	List<TermosECondicoes> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	void insert(TermosECondicoes termos) throws Exception;
	void insert(TermosECondicoes termos, CustomConnection customConnection) throws Exception;
	void update(TermosECondicoes termos) throws Exception;
	void update(TermosECondicoes termos, CustomConnection customConnection) throws Exception;
	List<TermosECondicoes> updateAll(List<TermosECondicoes> termosList) throws Exception;
	void updateAll(List<TermosECondicoes> termosList, CustomConnection customConnection) throws Exception;
	void delete(int cdTermo) throws Exception;
	void delete(int cdTermo, CustomConnection customConnection) throws Exception;
}
