package com.tivic.manager.mob.lotes.repository.arquivo;

import java.util.List;

import com.tivic.manager.mob.lotes.model.arquivo.LoteImpressaoArquivo;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class LoteImpressaoArquivoRepositoryDAO implements ILoteImpressaoArquivoRepository {
	
	@Override
	public LoteImpressaoArquivo insert(LoteImpressaoArquivo loteImpressaoArquivo, CustomConnection customConnection) throws Exception {
		int codigoRetorno = LoteImpressaoArquivoDAO.insert(loteImpressaoArquivo, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao inserir arquivo do lote");
		return loteImpressaoArquivo;
	}

	@Override
	public LoteImpressaoArquivo update(LoteImpressaoArquivo loteImpressaoArquivo, CustomConnection customConnection) throws Exception {
		int codigoRetorno = LoteImpressaoArquivoDAO.update(loteImpressaoArquivo, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao atualizar arquivo do lote");
		return loteImpressaoArquivo;
	}

	@Override
	public LoteImpressaoArquivo delete(LoteImpressaoArquivo loteImpressaoArquivo, CustomConnection customConnection) throws Exception {
		int codigoRetorno = LoteImpressaoArquivoDAO.delete(loteImpressaoArquivo.getCdLoteImpressao(), loteImpressaoArquivo.getCdArquivo(), customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao deletar arquivo do lote");
		return loteImpressaoArquivo;
	}

	@Override
	public LoteImpressaoArquivo get(int cdLoteImpressao, int cdArquivo) throws Exception {
		return get(cdLoteImpressao, cdArquivo, new CustomConnection());
	}
	
	@Override
	public LoteImpressaoArquivo get(int cdLoteImpressao, int cdArquivo, CustomConnection customConnection) throws Exception {
		LoteImpressaoArquivo loteImpressaoArquivo = LoteImpressaoArquivoDAO.get(cdLoteImpressao, cdArquivo, customConnection.getConnection());
		if (loteImpressaoArquivo == null) {
			throw new ValidacaoException("Nenhum arquivo de lote foi encontrado.");
		}
		return loteImpressaoArquivo;
	}

	@Override
	public List<LoteImpressaoArquivo> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}
	
	@Override
	public List<LoteImpressaoArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<LoteImpressaoArquivo> search = new SearchBuilder<LoteImpressaoArquivo>("mob_lote_impressao_arquivo")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(LoteImpressaoArquivo.class);
	}

}
