package com.tivic.manager.grl.bairro;

import java.util.List;

import com.tivic.manager.grl.Bairro;
import com.tivic.manager.grl.BairroDAO;
import com.tivic.manager.grl.bairro.BairroRepository;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class BairroRepositoryDAO implements BairroRepository{

	@Override
	public void insert(Bairro bairro, CustomConnection customConnection) throws Exception {
		int cdBairro = BairroDAO.insert(bairro, customConnection.getConnection());
		if (cdBairro < 0)
			throw new Exception("Erro ao inserir Bairro.");
		bairro.setCdBairro(cdBairro);	
	}

	@Override
	public void update(Bairro bairro, CustomConnection customConnection) throws Exception {
		BairroDAO.update(bairro, customConnection.getConnection());
	}

	@Override
	public Bairro get(int cdBairro) throws Exception {
		return get(cdBairro, new CustomConnection());
	}

	@Override
	public Bairro get(int cdBairro, CustomConnection customConnection) throws Exception {
		return BairroDAO.get(cdBairro, customConnection.getConnection());
	}

	@Override
	public List<Bairro> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Bairro> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Bairro> search = new SearchBuilder<Bairro>("grl_bairro")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(Bairro.class);
	} 
	
	@Override
	public List<Bairro> findForApp(int cdCidade, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_cidade", cdCidade);
		Search<Bairro> search = new SearchBuilder<Bairro>("grl_bairro")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(Bairro.class);
	}	
}
