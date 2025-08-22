package com.tivic.manager.mob.lotes.repository.arquivo;

import java.util.List;

import com.tivic.manager.mob.lotes.model.arquivo.Arquivo;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ArquivoRepository {
	public void insert(Arquivo arquivo, CustomConnection customConnection) throws Exception;
	public void update(Arquivo arquivo, CustomConnection customConnection) throws Exception;
	public void delete(int cdArquivo, CustomConnection customConnection) throws Exception;
	public Arquivo get(int cdLoteDividaAtiva, CustomConnection customConnection) throws Exception;
	public List<Arquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
