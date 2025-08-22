package com.tivic.manager.ptc.documento;

import java.util.List;

import com.tivic.manager.ptc.Documento;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface DocumentoRepository {
	public Documento insert(Documento documento, CustomConnection customConnection) throws Exception;
	public Documento get(int id) throws Exception;
	public Documento get(int id, CustomConnection customConnection) throws Exception;
	public Documento update(Documento documento, CustomConnection customConnection) throws Exception;
	public void insertCodeSync(Documento documento, CustomConnection customConnection) throws Exception;
	public List<Documento> find(SearchCriterios searchCriterios) throws Exception;
	public List<Documento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
