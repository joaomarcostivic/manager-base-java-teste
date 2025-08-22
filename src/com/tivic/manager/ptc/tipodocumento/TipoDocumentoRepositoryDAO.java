package com.tivic.manager.ptc.tipodocumento;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import java.util.List;

import com.tivic.manager.ptc.TipoDocumento;
import com.tivic.manager.ptc.TipoDocumentoDAO;

public class TipoDocumentoRepositoryDAO implements TipoDocumentoRepository{

	@Override
	public void update(TipoDocumento tipoDocumento, CustomConnection customConnection) throws Exception {
		 TipoDocumentoDAO.update(tipoDocumento, customConnection.getConnection());
	}
	
	@Override
	public TipoDocumento get(int id) throws Exception {
		return get(id, new CustomConnection());
	}

	@Override
	public TipoDocumento get(int id, CustomConnection customConnection) throws Exception{
		return TipoDocumentoDAO.get(id, customConnection.getConnection());
	}

	@Override
	public TipoDocumento find(SearchCriterios searchCritreiros) throws Exception {
		return find(searchCritreiros, new CustomConnection());
	}

	@Override
	public TipoDocumento find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(false);
			Search<TipoDocumento> search = new SearchBuilder<TipoDocumento>("gpn_tipo_documento")
					.fields("*")
					.searchCriterios(searchCriterios)
					.customConnection(customConnection)
				.build();
			
			List<TipoDocumento> tipoDocumentoList = search.getList(TipoDocumento.class);
			
			if(tipoDocumentoList.isEmpty()) {
				throw new Exception("Nenhum documento encontrado");
			}
			
			customConnection.finishConnection();
			return tipoDocumentoList.get(0);
		} finally {
			customConnection.closeConnection();
		}
	}
	
	
	
}
