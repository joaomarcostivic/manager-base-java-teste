package com.tivic.manager.fta.cor;

import java.util.List;

import com.tivic.manager.fta.Cor;
import com.tivic.manager.fta.CorDAO;
import com.tivic.manager.fta.cor.CorRepository;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class CorRepositoryDAO implements CorRepository {

	@Override
	public void insert(Cor cor, CustomConnection customConnection) throws Exception {
		int cdCor = CorDAO.insert(cor, customConnection.getConnection());
		if (cdCor <= 0)
			throw new Exception("Erro ao inserir Cor.");
		cor.setCdCor(cdCor);	
	}

	@Override
	public void update(Cor cor, CustomConnection customConnection) throws Exception {
		CorDAO.update(cor, customConnection.getConnection());
	}

	@Override
	public Cor get(int cdCor) throws Exception {
		return get(cdCor, new CustomConnection());
	}

	@Override
	public Cor get(int cdCor, CustomConnection customConnection) throws Exception {
		return CorDAO.get(cdCor, customConnection.getConnection());
	}

	@Override
	public List<Cor> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Cor> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Cor> search = new SearchBuilder<Cor>("fta_cor")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(Cor.class);
	} 
}
