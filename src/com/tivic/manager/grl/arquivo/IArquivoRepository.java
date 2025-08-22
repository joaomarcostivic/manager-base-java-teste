package com.tivic.manager.grl.arquivo;

import java.util.List;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.grl.Arquivo;
import com.tivic.sol.search.SearchCriterios;

public interface IArquivoRepository {
	Arquivo insert(Arquivo arquivo, CustomConnection customConnection) throws Exception;
	Arquivo update(Arquivo arquivo, CustomConnection customConnection) throws Exception;
	void delete(Integer cdArquivo, CustomConnection customConnection) throws Exception;
	Arquivo get(int cdArquivo) throws Exception;
	Arquivo get(int cdArquivo, CustomConnection customConnection) throws Exception;
	List<Arquivo> find(SearchCriterios searchCriterios) throws IllegalArgumentException, Exception;
	List<Arquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	void insertCodeSync(Arquivo arquivo, CustomConnection customConnection) throws Exception;
}
