package com.tivic.manager.ptc.protocolos.documentoocorrencia;

import java.util.List;

import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.DocumentoOcorrenciaDAO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class DocumentoOcorrenciaRepositoryDAO implements DocumentoOcorrenciaRepository{

	@Override
	public void insert(DocumentoOcorrencia documentoOcorrencia, CustomConnection customConnection) throws Exception {
		int result = DocumentoOcorrenciaDAO.insert(documentoOcorrencia, customConnection.getConnection());
		if (result < 0)
			throw new Exception("Erro ao inserir DocumentoOcorrencia.");	
	}

	@Override
	public void update(DocumentoOcorrencia documentoOcorrencia, CustomConnection customConnection) throws Exception {
		int codigoRetorno = DocumentoOcorrenciaDAO.update(documentoOcorrencia, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao atualizar Documento Ocorrencia.");
	}
	
	@Override
	public DocumentoOcorrencia get(int cdDocumento, int cdOcorrencia, int cdTipoOcorrencia) throws Exception {
		return get(cdDocumento, cdOcorrencia, cdTipoOcorrencia, new CustomConnection());
	}

	@Override
	public DocumentoOcorrencia get(int cdDocumento, int cdOcorrencia, int cdTipoOcorrencia, CustomConnection customConnection) throws Exception {
		return DocumentoOcorrenciaDAO.get(cdDocumento, cdOcorrencia, cdTipoOcorrencia, customConnection.getConnection());
	}
	
	@Override
	public List<DocumentoOcorrencia> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<DocumentoOcorrencia> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<DocumentoOcorrencia> search = new SearchBuilder<DocumentoOcorrencia>("ptc_documento_ocorrencia")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		List<DocumentoOcorrencia> documentoOcorrenciaList = search.getList(DocumentoOcorrencia.class);
		return documentoOcorrenciaList;
	}
}
