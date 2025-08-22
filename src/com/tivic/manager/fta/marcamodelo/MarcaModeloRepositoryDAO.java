package com.tivic.manager.fta.marcamodelo;

import java.util.List;

import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class MarcaModeloRepositoryDAO implements MarcaModeloRepository {

	@Override
	public void insert(MarcaModelo marcaModelo, CustomConnection customConnection) throws Exception {
		int cdMarca = MarcaModeloDAO.insert(marcaModelo, customConnection.getConnection());
		if (cdMarca <= 0)
			throw new Exception("Erro ao inserir MarcaModelo.");
		marcaModelo.setCdMarca(cdMarca);	
	}

	@Override
	public void update(MarcaModelo marcaModelo, CustomConnection customConnection) throws Exception {
		MarcaModeloDAO.update(marcaModelo, customConnection.getConnection());
	}

	@Override
	public MarcaModelo get(int cdMarca) throws Exception {
		return get(cdMarca, new CustomConnection());
	}

	@Override
	public MarcaModelo get(int cdMarca, CustomConnection customConnection) throws Exception {
		return MarcaModeloDAO.get(cdMarca, customConnection.getConnection());
	}
	
	@Override
	public MarcaModelo getBaseNova(int cdMarca) throws Exception {
		return get(cdMarca, new CustomConnection());
	}

	@Override
	public MarcaModelo getBaseNova(int cdMarca, CustomConnection customConnection) throws Exception {
		return MarcaModeloDAO.getBaseNova(cdMarca, customConnection.getConnection());
	}
	
	@Override
    public List<MarcaModelo> getAll() throws Exception {
    	return getAll(new CustomConnection());
    }
	
	@Override
	public List<MarcaModelo> getAll(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<MarcaModelo> search = new SearchBuilder<MarcaModelo>("fta_especie_veiculo")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(MarcaModelo.class);
	}

	@Override
	public List<MarcaModelo> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<MarcaModelo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<MarcaModelo> search = new SearchBuilder<MarcaModelo>("fta_marca_modelo")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(MarcaModelo.class);
	}

	@Override
	public List<MarcaModelo> findForApp(SearchCriterios searchCriterios) throws Exception {
		return findForApp(searchCriterios, new CustomConnection());
	}

	@Override
	public List<MarcaModelo> findForApp(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<MarcaModelo> search = new SearchBuilder<MarcaModelo>("fta_marca_modelo")
				.additionalCriterias("(trim(nm_modelo) != '' and nm_modelo is not null) " + 
						             "or (trim(nm_marca) != '' and nm_marca is not null)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(MarcaModelo.class);
	} 
}
