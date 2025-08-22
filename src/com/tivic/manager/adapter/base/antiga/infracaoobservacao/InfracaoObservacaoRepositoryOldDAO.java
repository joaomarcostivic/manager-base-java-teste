package com.tivic.manager.adapter.base.antiga.infracaoobservacao;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.InfracaoObservacao;
import com.tivic.manager.mob.infracao.observacao.InfracaoObservacaoRepository;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class InfracaoObservacaoRepositoryOldDAO implements InfracaoObservacaoRepository {
	
	private IAdapterService<InfracaoObservacaoOld, InfracaoObservacao> adapterService;
	
	public InfracaoObservacaoRepositoryOldDAO() throws Exception {
		this.adapterService = new AdapterInfracaoObservacaoService();
	}

	@Override
	public List<InfracaoObservacao> getAll() throws Exception {
    	return getAll(new CustomConnection());
	}

	@Override
	public List<InfracaoObservacao> getAll(CustomConnection customConnection) throws Exception {
		List<InfracaoObservacao> infracaoObservacaos = new ArrayList<InfracaoObservacao>();
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<InfracaoObservacaoOld> search = new SearchBuilder<InfracaoObservacaoOld>("str_infracao_observacao")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		List<InfracaoObservacaoOld> infracaoObservacaoOldList = search.getList(InfracaoObservacaoOld.class);
		
		for(InfracaoObservacaoOld infracaoObservacaoOld : infracaoObservacaoOldList) {
			InfracaoObservacao infracaoObservacao = this.adapterService.toBaseNova(infracaoObservacaoOld);
			infracaoObservacaos.add(infracaoObservacao);
		}
		
		return infracaoObservacaos;
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
		List<InfracaoObservacao> infracaoObservacaoList = new ArrayList<InfracaoObservacao>();
		SearchCriterios searchCriterios = new SearchCriterios(); 
		searchCriterios.addCriteriosEqualInteger("cod_infracao", cdInfracao);
		Search<InfracaoObservacaoOld> search = new SearchBuilder<InfracaoObservacaoOld>("str_infracao_observacao")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		List<InfracaoObservacaoOld> infracaoObservacaoOldList = search.getList(InfracaoObservacaoOld.class);
		
		for (InfracaoObservacaoOld infracaoObservacaoOld : infracaoObservacaoOldList) {
			InfracaoObservacao infracaoObservacao = adapterService.toBaseNova(infracaoObservacaoOld);
			infracaoObservacaoList.add(infracaoObservacao);
		}
			
		return infracaoObservacaoList;
	}
}
