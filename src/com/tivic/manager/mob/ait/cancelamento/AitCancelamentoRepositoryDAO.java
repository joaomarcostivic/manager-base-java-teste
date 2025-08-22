package com.tivic.manager.mob.ait.cancelamento;

import java.util.List;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitCancelamentoRepositoryDAO implements IAitCancelamentoRepository {

	@Override
	public AitCancelamento insert(AitCancelamento aitCancelamento, CustomConnection connection) throws Exception {
		int cdArquivo = AitCancelamentoDAO.insert(aitCancelamento, connection.getConnection());
		if (cdArquivo <= 0) {
			throw new ValidacaoException("Erro ao inserir relação Ait Cancelamento.");
		}
		aitCancelamento.setCdArquivo(cdArquivo);
		return aitCancelamento;
	}
	
	@Override
	public void update(AitCancelamento aitCancelamento, CustomConnection customConnection) throws Exception {
		int result = AitCancelamentoDAO.update(aitCancelamento, customConnection.getConnection());
		if(result < 0)
			throw new ValidacaoException("Erro ao atualizar relacação Ait Cancelamento.");
	}
	
	@Override
	public void delete(int cdArquivo, CustomConnection connection) throws Exception {
		int result = AitCancelamentoDAO.delete(cdArquivo, connection.getConnection());
		if (result <= 0) {
			throw new ValidacaoException("Erro ao deletar uma relação de Ait Cancelamento.");
		}
	}
	
	@Override
	public AitCancelamento get(int cdArquivo) throws Exception {
		return get(cdArquivo, new CustomConnection());
	}

	@Override
	public AitCancelamento get(int cdArquivo, CustomConnection customConnection) throws Exception{
		return AitCancelamentoDAO.get(cdArquivo, customConnection.getConnection());
	}
	
	@Override
	public List<AitCancelamento> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<AitCancelamento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitCancelamento> search = new SearchBuilder<AitCancelamento>("mob_ait_cancelamento")
			.searchCriterios(searchCriterios)
			.customConnection(customConnection)
			.orderBy("cd_arquivo")
			.build();
		return search.getList(AitCancelamento.class);
	}
	
	@Override
	public Search<AitArquivoDTO> findArquivosCancelamento(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<AitArquivoDTO> aitArquivos = findArquivosCancelamento(searchCriterios, customConnection);
			customConnection.finishConnection();
			return aitArquivos;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Search<AitArquivoDTO> findArquivosCancelamento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitArquivoDTO> search = new SearchBuilder<AitArquivoDTO>("mob_ait_cancelamento A")
				.fields("A.*, B.cd_arquivo, B.nm_documento, B.nm_arquivo")
				.addJoinTable("JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo)")
        		.searchCriterios(searchCriterios)
				.orderBy(" A.cd_arquivo DESC ")
				.count()
				.customConnection(customConnection)
				.build();
		return search;	
	}
	
}
