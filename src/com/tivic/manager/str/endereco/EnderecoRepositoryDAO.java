package com.tivic.manager.str.endereco;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class EnderecoRepositoryDAO implements EnderecoRepository {

	@Override
	public void insert(Endereco endereco, CustomConnection customConnection) throws Exception {
		int insert = EnderecoDAO.insert(endereco, customConnection.getConnection());
		if(insert <= 0) {
			throw new Exception("Não foi possível inserir o Endereço.");
		}
	}

	@Override
	public void update(Endereco endereco, CustomConnection customConnection) throws Exception {
		int update = EnderecoDAO.update(endereco, customConnection.getConnection());
		if(update <= 0) {
			throw new Exception("Não foi possível atualizar o Endereço.");
		}
	}

	@Override
	public Endereco get(int cdEndereco, CustomConnection customConnection) throws Exception {
		Endereco endereco = EnderecoDAO.get(cdEndereco, customConnection.getConnection());
		return endereco;
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
		Search<Endereco> search = new SearchBuilder<Endereco>("grl_endereco")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		if(search.getList(Endereco.class).isEmpty()) {
			throw new Exception("Nenhum endereço foi encontrado.");
		}
		return search.getList(Endereco.class);
	}
}
