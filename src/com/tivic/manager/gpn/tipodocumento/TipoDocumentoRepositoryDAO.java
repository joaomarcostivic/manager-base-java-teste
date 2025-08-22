package com.tivic.manager.gpn.tipodocumento;

import com.tivic.manager.gpn.TipoDocumento;
import com.tivic.manager.gpn.TipoDocumentoDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class TipoDocumentoRepositoryDAO implements TipoDocumentoRepository {

	@Override
	public TipoDocumento get(int cdTipoDocumento) throws Exception {
		return get(cdTipoDocumento, new CustomConnection());
	}

	@Override
	public TipoDocumento get(int cdTipoDocumento, CustomConnection customConnection) throws Exception {
		return TipoDocumentoDAO.get(cdTipoDocumento, customConnection.getConnection());
	}

	@Override
	public void update(TipoDocumento tipoDocumento, CustomConnection customConnection) throws Exception {
		TipoDocumentoDAO.update(tipoDocumento, customConnection.getConnection());
	}

	@Override
	public TipoDocumento getTipoDocumento(SearchCriterios searchCriterios) throws Exception {
		return getTipoDocumento(searchCriterios, new CustomConnection());
	}

	@Override
	public TipoDocumento getTipoDocumento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		TipoDocumento tipoDocumento = new TipoDocumento();
		Search<TipoDocumento> documento = new SearchBuilder<TipoDocumento>("gpn_tipo_documento")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		if(!documento.getList(TipoDocumento.class).isEmpty()) {
			tipoDocumento = documento.getList(TipoDocumento.class).get(0);
		}
		return tipoDocumento;
	}
}
