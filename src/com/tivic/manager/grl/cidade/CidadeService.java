package com.tivic.manager.grl.cidade;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.Cidade;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class CidadeService implements ICidadeService{

	private CidadeRepository cidadeRepository;
	
	public CidadeService() throws Exception {
		this.cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
	}
	
	@Override
	public Cidade getByNome(String nmCidade) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_cidade", nmCidade, true);
		List<Cidade> cidades = this.cidadeRepository.find(searchCriterios);
		if(cidades.isEmpty())
			throw new NoContentException("Nenhuma cidade encontrada");
		return cidades.get(0);
	}

	@Override
	public Cidade getById(String idCidade) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_cidade", idCidade, true);
		List<Cidade> cidades = this.cidadeRepository.find(searchCriterios);
		if(cidades.isEmpty())
			throw new NoContentException("Nenhuma cidade encontrada");
		return cidades.get(0);
	}

	@Override
	public List<Cidade> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}
	
	@Override
	public List<Cidade> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		customConnection.initConnection(false);
		Search<Cidade> search = new SearchBuilder<Cidade>("grl_cidade A")
				.searchCriterios(searchCriterios)
			.build();
		customConnection.finishConnection();
		return search.getList(Cidade.class);
	}

	@Override
	public Cidade get(int cdCidade) throws Exception {
		return get(cdCidade, new CustomConnection());
	}

	@Override
	public Cidade get(int cdCidade, CustomConnection customConnection) throws Exception {
		Cidade cidade = this.cidadeRepository.get(cdCidade);
		return cidade;
	}

}