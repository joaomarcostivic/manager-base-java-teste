package com.tivic.manager.fta.marcamodelo;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.fta.MarcaModelo;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class MarcaModeloService implements IMarcaModeloService {

	private MarcaModeloRepository marcaModeloRepository;
	
	public MarcaModeloService() throws Exception{
		this.marcaModeloRepository = (MarcaModeloRepository) BeansFactory.get(MarcaModeloRepository.class);
	}
	
	@Override
	public MarcaModelo getByNrMarca(String nrMarca) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nr_marca", nrMarca, true);
		List<MarcaModelo> marcas = this.marcaModeloRepository.find(searchCriterios);
		if(marcas.isEmpty())
			throw new Exception("Nenhuma marca encontrada");
		return marcas.get(0);
	}
	
	@Override
	public MarcaModelo get(int cdMarca) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			MarcaModelo marcaModelo = get(cdMarca, customConnection);
			customConnection.finishConnection();
			return marcaModelo;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public MarcaModelo get(int cdMarca, CustomConnection customConnection) throws Exception {
		MarcaModelo marcaModelo = this.marcaModeloRepository.get(cdMarca, customConnection);
		
		if(marcaModelo == null)
			throw new NoContentException("Nenhuma marca modelo encontrada");
		
		return marcaModelo;
	}

}
