package com.tivic.manager.ptc.tipodocumento;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.manager.ptc.TipoDocumento;

public interface TipoDocumentoRepository {
	public void update(TipoDocumento tipoDocumento, CustomConnection customConnection) throws Exception;
	public TipoDocumento get(int id) throws Exception;
	public TipoDocumento get(int id, CustomConnection customConnection) throws Exception;
	public TipoDocumento find(SearchCriterios searchCriteiros) throws Exception;
	public TipoDocumento find(SearchCriterios searchCriteiros, CustomConnection customConnection) throws Exception;
}
