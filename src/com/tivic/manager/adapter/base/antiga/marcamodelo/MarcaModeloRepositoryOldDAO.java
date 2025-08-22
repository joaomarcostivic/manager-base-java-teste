package com.tivic.manager.adapter.base.antiga.marcamodelo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloDAO;
import com.tivic.manager.fta.marcamodelo.MarcaModeloRepository;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class MarcaModeloRepositoryOldDAO implements MarcaModeloRepository {
	
	private IAdapterService<MarcaModeloOld, MarcaModelo> adapterService;
	
	public MarcaModeloRepositoryOldDAO() throws Exception {
		this.adapterService = new AdapterMarcaModeloService();
	}

	@Override
	public void insert(MarcaModelo marcaModelo, CustomConnection customConnection) throws Exception {
		MarcaModeloOld marcaModeloOld = this.adapterService.toBaseAntiga(marcaModelo);
		int codRetorno = MarcaModeloOldDAO.insert(marcaModeloOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new BadRequestException("Ocorreu um erro ao cadastrar a marcaModelo");
		marcaModelo.setCdMarca(codRetorno);
	}

	@Override
	public void update(MarcaModelo marcaModelo, CustomConnection customConnection) throws Exception {
		MarcaModeloOld marcaModeloOld = this.adapterService.toBaseAntiga(marcaModelo);
		int codRetorno = MarcaModeloOldDAO.update(marcaModeloOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new BadRequestException("Ocorreu um erro ao cadastrar marcaModelo");
	}

	@Override
	public MarcaModelo get(int cdMarcaModelo) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.finishConnection();
			MarcaModelo marcaModelo = get(cdMarcaModelo, customConnection);
			return marcaModelo;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public MarcaModelo get(int cdMarcaModelo, CustomConnection customConnection) throws Exception {
		MarcaModeloOld marcaModeloOld = MarcaModeloOldDAO.get(cdMarcaModelo, customConnection.getConnection());
		return this.adapterService.toBaseNova(marcaModeloOld);
	}

	@Override
	public List<MarcaModelo> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<MarcaModelo> marcaModeloList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return marcaModeloList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<MarcaModelo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<MarcaModelo> marcaModelos = new ArrayList<MarcaModelo>();
		ResultSetMapper<MarcaModeloOld> rsm = new ResultSetMapper<MarcaModeloOld>(MarcaModeloOldDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), MarcaModeloOld.class);
		List<MarcaModeloOld> MarcaModeloOldList = rsm.toList();
		for (MarcaModeloOld marcaModeloOld : MarcaModeloOldList) {
			MarcaModelo marcaModelo = this.adapterService.toBaseNova(marcaModeloOld);
			marcaModelos.add(marcaModelo);
		}
		return marcaModelos;
	}

	@Override
	public List<MarcaModelo> getAll() throws Exception {
    	return getAll(new CustomConnection());
	}

	@Override
	public List<MarcaModelo> getAll(CustomConnection customConnection) throws Exception {
		List<MarcaModelo> marcaModelos = new ArrayList<MarcaModelo>();
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<MarcaModeloOld> search = new SearchBuilder<MarcaModeloOld>("marcaModelo")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		List<MarcaModeloOld> marcaModeloOldList = search.getList(MarcaModeloOld.class);
		
		for(MarcaModeloOld marcaModeloOld : marcaModeloOldList) {
			MarcaModelo marcaModelo = this.adapterService.toBaseNova(marcaModeloOld);
			marcaModelos.add(marcaModelo);
		}
		
		return marcaModelos;
	}

	@Override
	public List<MarcaModelo> findForApp(SearchCriterios searchCriterios) throws Exception {
		return findForApp(searchCriterios, new CustomConnection());
	}

	@Override
	public List<MarcaModelo> findForApp(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<MarcaModelo> marcaModeloList = new ArrayList<MarcaModelo>();
		Search<MarcaModeloOld> search = new SearchBuilder<MarcaModeloOld>("marca_modelo")
				.additionalCriterias("(trim(nm_modelo) != '' and nm_modelo is not null) " + 
						             "or (trim(nm_marca) != '' and nm_marca is not null)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		List<MarcaModeloOld> marcaModeloOldList = search.getList(MarcaModeloOld.class);
		
		for (MarcaModeloOld marcaModeloOld : marcaModeloOldList) {
			MarcaModelo marcaModelo = this.adapterService.toBaseNova(marcaModeloOld);
			marcaModeloList.add(marcaModelo);
		}
		
		return marcaModeloList;
	}

	@Override
	public MarcaModelo getBaseNova(int cdMarca) throws Exception {
		return getBaseNova(cdMarca, new CustomConnection());
	}

	@Override
	public MarcaModelo getBaseNova(int cdMarca, CustomConnection customConnection) throws Exception {
		return MarcaModeloDAO.getBaseNova(cdMarca, customConnection.getConnection());
	}
}
