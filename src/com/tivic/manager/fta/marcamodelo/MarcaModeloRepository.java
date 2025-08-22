package com.tivic.manager.fta.marcamodelo;

import java.util.List;

import com.tivic.manager.fta.MarcaModelo;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface MarcaModeloRepository {
	public void insert(MarcaModelo marcaModelo, CustomConnection customConnection) throws Exception;
	public void update(MarcaModelo marcaModelo, CustomConnection customConnection) throws Exception;
	public MarcaModelo get(int cdMarca) throws Exception;
	public MarcaModelo get(int cdMarca, CustomConnection customConnection) throws Exception;
	public List<MarcaModelo> getAll() throws Exception;
	public List<MarcaModelo> getAll(CustomConnection customConnection) throws Exception;
	public List<MarcaModelo> find(SearchCriterios searchCriterios) throws Exception;
	public List<MarcaModelo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<MarcaModelo> findForApp(SearchCriterios searchCriterios) throws Exception;
	public List<MarcaModelo> findForApp(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public MarcaModelo getBaseNova(int cdMarca) throws Exception;
	public MarcaModelo getBaseNova(int cdMarca, CustomConnection customConnection) throws Exception;
}
