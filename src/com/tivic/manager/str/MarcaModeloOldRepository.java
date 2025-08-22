package com.tivic.manager.str;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface MarcaModeloOldRepository {

	public void insert(MarcaModelo marcaModelo, CustomConnection customConnection) throws Exception;
	public void update(MarcaModelo marcaModelo, CustomConnection customConnection) throws Exception;
	public MarcaModelo get(int cdMarca) throws Exception;
	public MarcaModelo get(int cdMarca, CustomConnection customConnection) throws Exception;
	public List<MarcaModelo> find(SearchCriterios searchCriterios) throws Exception;
	public List<MarcaModelo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;

}
