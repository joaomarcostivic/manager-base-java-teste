package com.tivic.manager.ptc.protocolosv3.externo;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class ProtocoloExternoRepositoryDAO implements ProtocoloExternoRepository {

	@Override
	public ProtocoloExterno insert(ProtocoloExterno protocoloExterno, CustomConnection customConnection) throws Exception {
		int cdDocumentoExterno = ProtocoloExternoDAO.insert(protocoloExterno, customConnection.getConnection());
		if(cdDocumentoExterno <= 0)
			throw new ValidacaoException("Erro ao inserir protocolo externo");
		return protocoloExterno;
	}

	@Override
	public ProtocoloExterno update(ProtocoloExterno protocoloExterno, CustomConnection customConnection) throws Exception {
		int cdRetorno = ProtocoloExternoDAO.update(protocoloExterno, customConnection.getConnection());
		if(cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao atualizar protocolo externo");
		}
		return protocoloExterno;
	}

	@Override
	public ProtocoloExterno get(int cdDocumentoExterno) throws Exception {
		return get(cdDocumentoExterno, new CustomConnection());
	}

	@Override
	public ProtocoloExterno get(int cdDocumentoExterno, CustomConnection customConnection) throws Exception {
		ProtocoloExterno protocoloExterno = ProtocoloExternoDAO.get(cdDocumentoExterno, customConnection.getConnection());
		if(protocoloExterno.getCdDocumentoExterno() <= 0) {
			throw new ValidacaoException("Erro ao atualizar protocolo externo");
		}
		return protocoloExterno;
	}

}