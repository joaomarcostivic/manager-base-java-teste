package com.tivic.manager.mob.ait.aitpessoa;

import java.util.List;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class AitPessoaService implements IAitPessoaService {
	
	private AitPessoaRepository aitPessoaRepository;
	
	public AitPessoaService() throws Exception {
		aitPessoaRepository = (AitPessoaRepository) BeansFactory.get(AitPessoaRepository.class);
	}

	@Override
	public void insert(AitPessoa aitPessoa) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			insert(aitPessoa, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void insert(AitPessoa aitPessoa, CustomConnection customConnection) throws Exception {
		aitPessoaRepository.insert(aitPessoa, customConnection);
	}

	@Override
	public void update(AitPessoa aitPessoa) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(aitPessoa, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void update(AitPessoa aitPessoa, CustomConnection customConnection) throws Exception {
		aitPessoaRepository.update(aitPessoa, customConnection);
	}

	@Override
	public boolean hasAitPessoa(String idAit) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			boolean hasAit = hasAitPessoa(idAit, customConnection);
			customConnection.finishConnection();
			return hasAit;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public boolean hasAitPessoa(String idAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_ait", idAit, true);
		List<AitPessoa> aitPessoaList = aitPessoaRepository.find(searchCriterios, customConnection);
		return !aitPessoaList.isEmpty();
	}
}
