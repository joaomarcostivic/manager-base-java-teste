package com.tivic.manager.mob.infracao.observacao;

import java.util.List;

import com.tivic.manager.mob.InfracaoObservacao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class InfracaoObservacaoRepositoryDAO implements InfracaoObservacaoRepository {

	@Override
	public List<InfracaoObservacao> getAll() throws Exception {
		return getAll(new CustomConnection());
	}

	@Override
	public List<InfracaoObservacao> getAll(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<InfracaoObservacao> search = new SearchBuilder<InfracaoObservacao>("mob_infracao_observacao")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(InfracaoObservacao.class);
	}
	
	@Override
	public List<InfracaoObservacao> getObservacaoByCdInfracao(int cdInfracao) throws Exception {
		CustomConnection customConncetion = new CustomConnection();
		try  {
			customConncetion.initConnection(false);
			List<InfracaoObservacao> infracaoObservacao = getObservacaoByCdInfracao(cdInfracao, customConncetion);
			customConncetion.finishConnection();
			return infracaoObservacao;
		} finally {
			customConncetion.closeConnection();
		}
	}

	@Override
	public List<InfracaoObservacao> getObservacaoByCdInfracao(int cdInfracao, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios(); 
		searchCriterios.addCriteriosEqualInteger("cd_infracao", cdInfracao);
		Search<InfracaoObservacao> search = new SearchBuilder<InfracaoObservacao>("mob_infracao_observacao")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		
		return search.getList(InfracaoObservacao.class);
	}

}
