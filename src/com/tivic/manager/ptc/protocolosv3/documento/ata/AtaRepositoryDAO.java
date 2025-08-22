package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.util.List;

import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class AtaRepositoryDAO implements IAtaRepository {

	@Override
	public int insert(Ata ata, CustomConnection customConnection) throws Exception {
		int retorno =  AtaDAO.insert(ata, customConnection.getConnection());
		if(retorno <= 0 ) 
			throw new Exception("Erro ao criar nova Ata");
		return retorno;
	}

	@Override
	public int update(Ata ata, CustomConnection customConnection) throws Exception {
		int retorno = AtaDAO.update(ata, customConnection.getConnection());
		if(retorno <= 0 ) 
			throw new Exception("Erro ao criar nova Ata");
		return retorno;
	}

	@Override
	public Ata get(int cdAta) throws Exception {
		return get(cdAta, new CustomConnection());
	}

	@Override
	public Ata get(int cdAta, CustomConnection customConnection) throws Exception {
		Ata ata = AtaDAO.get(cdAta, customConnection.getConnection());
		return ata;
	}

	@Override
	public List<Ata> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		ResultSetMapper<Ata> rsm = new ResultSetMapper<Ata>(AtaDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), Ata.class);
		return rsm.toList();
	}

	@Override
	public void delete(int cdAta, CustomConnection customConnection) throws Exception {
		int codigoRetorno = AtaDAO.delete(cdAta, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao deletar Ata");
	}


}
