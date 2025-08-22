package com.tivic.manager.grl.cidade;

import java.util.List;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class CidadeRepositoryDAO implements CidadeRepository {
	
	@Override
    public Cidade insert(Cidade cidade, CustomConnection customConnection) throws Exception {
        int cdCidade = CidadeDAO.insert(cidade, customConnection.getConnection());
        if(cdCidade <= 0)
            throw new Exception("erro ao inserir Cidade");
        cidade.setCdCidade(cdCidade);
        return cidade;
    }

    @Override
    public Cidade update(Cidade cidade, CustomConnection customConnection) throws Exception {
    	int resultAtualizacao = CidadeDAO.update(cidade, customConnection.getConnection());
    	if(resultAtualizacao <= 0) 
    		throw new Exception("Erro ao Atualizar cidade");
        return cidade;
    }
    
    @Override
	public Cidade get(int cdCidade) throws Exception {
		return CidadeDAO.get(cdCidade);
	}

    @Override
    public Cidade get(int cdCidade, CustomConnection customConnection) throws Exception {
        return CidadeDAO.get(cdCidade, customConnection.getConnection());
    }
    
    @Override
    public List<Cidade> getAll() throws Exception {
    	return getAll(new CustomConnection());
    }
    
    @Override
    public List<Cidade> getAll(CustomConnection customConnection) throws Exception {
    	SearchCriterios searchCriterios = new SearchCriterios();
		Search<Cidade> search = new SearchBuilder<Cidade>("grl_cidade")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(Cidade.class);
    }

    @Override
	public List<Cidade> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Cidade> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Cidade> search = new SearchBuilder<Cidade>("grl_cidade")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(Cidade.class);
	}
}
