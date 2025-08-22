package com.tivic.manager.str.endereco;

import java.util.List;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class EnderecoService implements IEnderecoService {
	
	private EnderecoRepository enderecoRepository;
	
	public EnderecoService() throws Exception {
		enderecoRepository = (EnderecoRepository) BeansFactory.get(EnderecoRepository.class);
	}

	@Override
	public void insert(Endereco endereco) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			insert(endereco, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void insert(Endereco endereco, CustomConnection customConnection) throws Exception {
		enderecoRepository.insert(endereco, customConnection);
	}

	@Override
	public void update(Endereco endereco) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(endereco, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void update(Endereco endereco, CustomConnection customConnection) throws Exception {
		enderecoRepository.update(endereco, customConnection);
		
	}

	@Override
	public Endereco get(int cdEndereco) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Endereco endereco = get(cdEndereco, customConnection);
			customConnection.finishConnection();
			return endereco;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Endereco get(int cdEndereco, CustomConnection customConnection) throws Exception {
		return enderecoRepository.get(cdEndereco, customConnection);
	}

	@Override
	public List<Endereco> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Endereco> enderecoList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return enderecoList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Endereco> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return enderecoRepository.find(searchCriterios, customConnection);
	}
}
