package com.tivic.manager.ptc.protocolosv3.documento.situacao;

import java.util.List;
import com.tivic.manager.ptc.SituacaoDocumento;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ISituacaoDocumentoRepository {
	SituacaoDocumento get(int cdSituacaoDocumento) throws Exception;
	SituacaoDocumento get(int cdSituacaoDocumento, CustomConnection customConnection);
	List<SituacaoDocumento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
