package com.tivic.manager.gpn.tipodocumento;

import com.tivic.manager.gpn.TipoDocumento;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface TipoDocumentoRepository {
	public TipoDocumento get(int cdTipoDocumento) throws Exception;
	public TipoDocumento get(int cdTipoDocumento, CustomConnection customConnection) throws Exception;
	public void update(TipoDocumento tipoDocumento, CustomConnection customConnection) throws Exception;
	public TipoDocumento getTipoDocumento(SearchCriterios searchCriterios) throws Exception;
	public TipoDocumento getTipoDocumento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
