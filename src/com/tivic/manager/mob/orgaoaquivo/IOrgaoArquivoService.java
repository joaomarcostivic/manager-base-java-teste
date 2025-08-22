package com.tivic.manager.mob.orgaoaquivo;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;

public interface IOrgaoArquivoService {
	OrgaoArquivoDTO getCaminhoPorTipoDocumento(int tpDocumento) throws Exception;
	Search<OrgaoArquivoDTO> getCaminhoPorTipoDocumento(int tpDocumento, CustomConnection customConnection) throws Exception;
}
