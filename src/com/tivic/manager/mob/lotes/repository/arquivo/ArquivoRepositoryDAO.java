package com.tivic.manager.mob.lotes.repository.arquivo;

import java.util.List;

import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.mob.lotes.model.arquivo.Arquivo;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ArquivoRepositoryDAO implements ArquivoRepository {
	
	@Override
	public void insert(Arquivo arquivo, CustomConnection customConnection) throws Exception {
		int cdArquivo = ArquivoDAO.insert(arquivo.toModel(), customConnection.getConnection());
		if (cdArquivo <= 0)
			throw new Exception("Erro ao inserir arquivo.");
		arquivo.setCdArquivo(cdArquivo);
	}

	@Override
	public void update(Arquivo arquivo, CustomConnection customConnection) throws Exception {
		ArquivoDAO.update(arquivo.toModel(), customConnection.getConnection());
	}
	
	@Override
	public void delete(int cdArquivo, CustomConnection customConnection) throws Exception {
		int codigoRetorno = ArquivoDAO.delete(cdArquivo, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao remover arquivo.");
	}

	@Override
	public Arquivo get(int cdArquivo, CustomConnection customConnection) throws Exception {
		return Arquivo.fromModel(ArquivoDAO.get(cdArquivo, customConnection.getConnection()));
	}

	@Override
	public List<Arquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Arquivo> search = new SearchBuilder<Arquivo>("grl_arquivo")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(Arquivo.class);
	}
}
