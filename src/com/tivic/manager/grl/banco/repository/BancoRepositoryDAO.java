package com.tivic.manager.grl.banco.repository;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.Banco;
import com.tivic.manager.grl.BancoDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class BancoRepositoryDAO implements BancoRepository {

	@Override
	public void insert(Banco banco, CustomConnection customConnection) throws Exception {
		int cdBanco = BancoDAO.insert(banco, customConnection.getConnection());
		if(cdBanco < 0)
			throw new Exception("Erro ao inserir equipamento");
	}

	@Override
	public void update(Banco banco, CustomConnection customConnection) throws Exception {
		int cdBanco = BancoDAO.update(banco, customConnection.getConnection());
		if(cdBanco < 0)
			throw new Exception("Erro ao atualizar equipamento");
	}

	@Override
	public void delete(int cdBanco, CustomConnection customConnection) throws Exception {
		BancoDAO.delete(cdBanco, customConnection.getConnection());
	}
	
    @Override
	public Banco get(int cdBanco) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Banco banco = get(cdBanco, customConnection);
			if(banco == null)
				throw new NoContentException("Nenhum Banco encontrado");
			customConnection.finishConnection();
			return banco;
		} finally {
			customConnection.closeConnection();
		}
	}

    @Override
    public Banco get(int cdBanco, CustomConnection customConnection) throws Exception {
        return BancoDAO.get(cdBanco, customConnection.getConnection());
    }

    @Override
	public Search<Banco> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<Banco> bancos = find(searchCriterios, customConnection);
			if(bancos.getList(Banco.class).isEmpty())
				throw new NoContentException("Nenhum banco encontrado");
			customConnection.finishConnection();
			return bancos;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Search<Banco> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Banco> search = new SearchBuilder<Banco>("grl_banco")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.count()
				.build();
		return search;
	}

}
