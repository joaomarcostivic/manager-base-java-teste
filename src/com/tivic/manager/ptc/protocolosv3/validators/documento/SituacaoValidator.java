package com.tivic.manager.ptc.protocolosv3.validators.documento;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.validators.IProtocoloValidator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class SituacaoValidator implements IProtocoloValidator {

	@Override
	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws Exception, ValidacaoException {
		int cdSituacaoPendente = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_PENDENTE", 0);
		
		if(protocolo.getAitMovimento().getCdOcorrencia() > 0)
			return;
		
		if(cdSituacaoPendente <= 0)
			throw new ValidationException("Parâmetro CD_SITUACAO_PENDENTE não configurado.");
		
		if(protocolo.getDocumento().getCdSituacaoDocumento() != cdSituacaoPendente) {
			throw new ValidationException("Situação do documento inválida.");
		}

	}

}
