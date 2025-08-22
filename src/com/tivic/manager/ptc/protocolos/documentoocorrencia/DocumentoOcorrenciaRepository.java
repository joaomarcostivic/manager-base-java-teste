package com.tivic.manager.ptc.protocolos.documentoocorrencia;

import java.util.List;

import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface DocumentoOcorrenciaRepository {
	void insert(DocumentoOcorrencia documentoOcorrencia, CustomConnection customConnection) throws Exception;
	void update(DocumentoOcorrencia documentoOcorrencia, CustomConnection customConnection) throws Exception;
	DocumentoOcorrencia get(int cdDocumento, int cdOcorrencia, int cdTipoOcorrencia) throws Exception;
	DocumentoOcorrencia get(int cdDocumento, int cdOcorrencia, int cdTipoOcorrencia, CustomConnection customConnection) throws Exception;
	List<DocumentoOcorrencia> find(SearchCriterios searchCriterios) throws Exception;
	List<DocumentoOcorrencia> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
