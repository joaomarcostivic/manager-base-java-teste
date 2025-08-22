package com.tivic.manager.ptc.protocolosv3.documento.situacao;

import java.util.List;

import com.tivic.manager.ptc.SituacaoDocumento;
import com.tivic.manager.ptc.SituacaoDocumentoDAO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class SituacaoDocumentoRepositoryDAO implements ISituacaoDocumentoRepository {

	@Override
	public SituacaoDocumento get(int cdSituacaoDocumento) throws Exception {
		return get(cdSituacaoDocumento, new CustomConnection());
	}

	@Override
	public SituacaoDocumento get(int cdSituacaoDocumento, CustomConnection customConnection) {
		return SituacaoDocumentoDAO.get(cdSituacaoDocumento, customConnection.getConnection());
	}
	
	@Override
	public List<SituacaoDocumento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		ResultSetMapper<SituacaoDocumento> rsm = new ResultSetMapper<SituacaoDocumento>(SituacaoDocumentoDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), SituacaoDocumento.class);
		return rsm.toList();
	}

}
