package com.tivic.manager.mob.edat.repositories;

import java.util.List;

import com.tivic.manager.mob.edat.TermosECondicoes;
import com.tivic.manager.mob.edat.dao.TermosECondicoesDAO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class TermosECondicoesRepositoryDAO implements ITermosECondicoesRepository {

	@Override
	public void update(TermosECondicoes termos, CustomConnection customConnection) throws Exception {
		int retorno = TermosECondicoesDAO.update(termos, customConnection.getConnection());
		if (retorno <= 0) {
			throw new ValidacaoException("Erro ao atualizar termo");
		}
	}
	
	@Override
	public void updateAll(List<TermosECondicoes> termosList, CustomConnection customConnection) throws Exception {
		int retorno = TermosECondicoesDAO.updateAll(termosList, customConnection.getConnection());
		if (retorno <= 0) {
			throw new ValidacaoException("Erro ao atualizar termos");
		}
	}

	@Override
	public TermosECondicoes get(int cdTermo, CustomConnection customConnection) throws Exception {
		return TermosECondicoesDAO.get(cdTermo, customConnection.getConnection());
	}

	@Override
	public void insert(TermosECondicoes termos, CustomConnection customConnection) throws Exception {
		int retorno = TermosECondicoesDAO.insert(termos, customConnection.getConnection());
		if (retorno < 0) {
			throw new Exception("Erro ao inserir Termos e Condições.");
		}
	}

	@Override
	public List<TermosECondicoes> find(SearchCriterios searchCriterios, CustomConnection customConnection)
			throws Exception {
		ResultSetMapper<TermosECondicoes> rsm = new ResultSetMapper<TermosECondicoes>(
				TermosECondicoesDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()),
				TermosECondicoes.class);
		return rsm.toList();
	}

	@Override
	public void delete(int cdTermo, CustomConnection customConnection) throws Exception {
		int retorno = TermosECondicoesDAO.delete(cdTermo, customConnection.getConnection());
		if (retorno <= 0)
			throw new ValidacaoException("Erro ao deletar termo");
	}
}
