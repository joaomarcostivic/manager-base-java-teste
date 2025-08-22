package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.util.List;

import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class AtaRelatorRepositoryDAO implements IAtaRelatorRepository{

	@Override
	public int insert(AtaRelator ataRelator, CustomConnection customConnection) throws Exception {
		int retorno =  AtaRelatorDAO.insert(ataRelator, customConnection.getConnection());
		if(retorno <= 0 ) 
			throw new Exception("Erro ao criar nova Ata");
		return retorno;
	}

	@Override
	public int update(AtaRelator ataRelator, CustomConnection customConnection) throws Exception {
		int retorno = AtaRelatorDAO.update(ataRelator, customConnection.getConnection());
		if(retorno <= 0 ) 
			throw new Exception("Erro ao criar nova Ata");
		return retorno;
	}

	@Override
	public AtaRelator get(int cdAta, int cdPessoa, CustomConnection customConnection) throws Exception {
		AtaRelator ata = AtaRelatorDAO.get(cdAta, cdPessoa, customConnection.getConnection());
		return ata;
	}

	@Override
	public List<AtaRelator> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		ResultSetMapper<AtaRelator> rsm = new ResultSetMapper<AtaRelator>(AtaRelatorDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), AtaRelator.class);
		return rsm.toList();
	}

	@Override
	public void delete(int cdAta, int cdPessoa, CustomConnection customConnection) throws Exception {
		int codigoRetorno = AtaRelatorDAO.delete(cdAta, cdPessoa, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao deletar relator");
	}

}
