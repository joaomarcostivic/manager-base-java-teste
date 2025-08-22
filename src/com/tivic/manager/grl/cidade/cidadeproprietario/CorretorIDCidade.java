package com.tivic.manager.grl.cidade.cidadeproprietario;

import java.util.List;

import com.tivic.manager.grl.Cidade;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class CorretorIDCidade implements ICorretorIDCidade {

	@Override
	public Cidade getCidadeById(String idCidade) throws Exception{ 
		List<Cidade> cidades = searchCidade(idCidade).getList(Cidade.class);
		if(cidades.isEmpty()) {
			throw new Exception("Nenhuma cidade encontrada.");
		}
		return cidades.get(0);
	}
	
	public Search<Cidade> searchCidade(String idCidade) throws Exception {
	    SearchCriterios searchCriterios = new SearchCriterios();
	    SearchBuilder<Cidade> searchBuilder = new SearchBuilder<Cidade>(" grl_cidade ")
	            .fields("*")
	            .searchCriterios(searchCriterios);
	    searchBuilder.additionalCriterias(buscaCondicional(idCidade));
	
	    Search<Cidade> search = searchBuilder.build();
	    return search;
	}

	private String buscaCondicional(String idCidade) {
	    idCidade = idCidade.replaceFirst("^0+", "");
	    String sql = "(id_cidade)::int = " + idCidade;
	    return sql;
	}

}

