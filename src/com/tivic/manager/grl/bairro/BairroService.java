package com.tivic.manager.grl.bairro;

import java.util.List;

import com.tivic.manager.grl.Bairro;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class BairroService implements IBairroService {

	private BairroRepository bairroRepository;
	
	public BairroService() throws Exception {
		this.bairroRepository = (BairroRepository) BeansFactory.get(BairroRepository.class);
	}

	@Override
	public void insert(Bairro bairro) throws Exception {
		insert(bairro, new CustomConnection());
	}
	
	@Override
	public void insert(Bairro bairro, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			this.bairroRepository.insert(bairro, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Bairro getBairroByNomeCidade(String nmBairro, int cdCidade) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_bairro", nmBairro, true);
		searchCriterios.addCriteriosEqualInteger("cd_cidade", cdCidade, true);
		List<Bairro> bairros = this.bairroRepository.find(searchCriterios);
		if(bairros.isEmpty())
			throw new Exception("Nenhum bairro encontrado");
		return bairros.get(0);
	}

}
