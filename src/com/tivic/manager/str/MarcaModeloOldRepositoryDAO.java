package com.tivic.manager.str;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class MarcaModeloOldRepositoryDAO implements MarcaModeloOldRepository{
	
	@Override
	public void insert(MarcaModelo marcaModelo, CustomConnection customConnection) throws Exception {
		int cdMarca = MarcaModeloOldDAO.insert(marcaModelo, customConnection.getConnection());
		if (cdMarca <= 0)
			throw new Exception("Erro ao inserir MarcaModelo.");
		marcaModelo.setCdMarca(cdMarca);	
	}

	@Override
	public void update(MarcaModelo marcaModelo, CustomConnection customConnection) throws Exception {
		MarcaModeloOldDAO.update(marcaModelo, customConnection.getConnection());
	}

	@Override
	public MarcaModelo get(int cdMarca) throws Exception {
		return get(cdMarca, new CustomConnection());
	}

	@Override
	public MarcaModelo get(int cdMarca, CustomConnection customConnection) throws Exception {
		return MarcaModeloOldDAO.get(cdMarca, customConnection.getConnection());
	}

	@Override
	public List<MarcaModelo> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<MarcaModelo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<MarcaModelo> search = new SearchBuilder<MarcaModelo>("marca_modelo")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(MarcaModelo.class);
	} 

}
