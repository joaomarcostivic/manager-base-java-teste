package com.tivic.manager.ptc.documento;

import java.util.List;

import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;


public class DocumentoRepositoryDAO implements DocumentoRepository {
	
	@Override
	public Documento insert(Documento documento, CustomConnection customConnection) throws Exception {
		int cdDocumento = DocumentoDAO.insert(documento, customConnection.getConnection());
		if (cdDocumento < 0)
			throw new Exception("Erro ao inserir Documento.");
		
		return documento;
	}

	@Override
	public void insertCodeSync(Documento documento, CustomConnection customConnection) throws Exception {
		DocumentoDAO.insert(documento, customConnection.getConnection(), true);
	}
	
	@Override
	public Documento get(int id) throws Exception {
		return get(id, new CustomConnection());
	}

	@Override
	public Documento get(int id, CustomConnection customConnection) throws Exception{
		return DocumentoDAO.get(id, customConnection.getConnection());
	}

	@Override
	public Documento update(Documento documento, CustomConnection customConnection) throws Exception {
		int cdUpdate = DocumentoDAO.update(documento, customConnection.getConnection());
		if (cdUpdate <= 0)
			throw new Exception("Erro ao atualizar Documento.");
		return documento;
	}

	@Override
	public List<Documento> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Documento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Documento> search = new SearchBuilder<Documento>("ptc_documento")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
			.build();
		return search.getList(Documento.class);
	}
}
