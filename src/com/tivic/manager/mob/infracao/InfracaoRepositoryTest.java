package com.tivic.manager.mob.infracao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.mob.Infracao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class InfracaoRepositoryTest implements InfracaoRepository {

	private List<Infracao> infracoes;
	private int cdInfracao;
	
	public InfracaoRepositoryTest() {
		this.infracoes = new ArrayList<Infracao>();
		this.cdInfracao = 1;
	}
	
	@Override
	public void insert(Infracao infracao, CustomConnection customConnection) throws Exception {
		infracao.setCdInfracao(this.cdInfracao++);
		this.infracoes.add(infracao);
	}

	@Override
	public void update(Infracao infracao, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < this.infracoes.size(); i++) {
			Infracao infracaoFromList = this.infracoes.get(i);
			if(infracaoFromList.getCdInfracao() == infracao.getCdInfracao()) {
				this.infracoes.set(i, infracao);
				break;
			}
		}
	}
	
	@Override
	public Infracao get(int cdInfracao) throws Exception {
		return get(cdInfracao, new CustomConnection());
	}

	@Override
	public Infracao get(int cdInfracao, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < this.infracoes.size(); i++) {
			Infracao infracaoFromList = this.infracoes.get(i);
			if(infracaoFromList.getCdInfracao() == cdInfracao) {
				return infracaoFromList;
			}
		}
		return null;
	}
	
	@Override
	public List<Infracao> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Infracao> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return this.infracoes;
	}

	@Override
	public List<Infracao> getInfracoesVigentes(CustomConnection customConnection) throws Exception {
		return Collections.emptyList();
	}

	@Override
	public List<Infracao> getAll(String keyword) throws Exception {
		return getAll(keyword, new CustomConnection());
	}

	@Override
	public List<Infracao> getAll(String keyword, CustomConnection customConnection) throws Exception {
	    if (keyword == null || keyword.isEmpty()) {
	        return new ArrayList<>(this.infracoes);
	    }
	    return this.infracoes.stream()
	            .filter(infracao -> infracao.getCdInfracao() > 0 &&
	                    infracao.getDsInfracao().toLowerCase().contains(keyword.toLowerCase()))
	            .collect(Collectors.toList());
	}
}
