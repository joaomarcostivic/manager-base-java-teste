package com.tivic.manager.ptc.protocolosv3.validators.documento;

import javax.validation.ValidationException;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.validators.IProtocoloValidator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class ExisteProtocoloTipoDocumento implements IProtocoloValidator {
	private IAitMovimentoService aitMovimentoService;

	public ExisteProtocoloTipoDocumento() throws Exception{
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		
	}
	@Override
	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws Exception, ValidacaoException {
		AitMovimento aitMovimento = this.aitMovimentoService.getStatusMovimento(protocolo.getAit().getCdAit(), protocolo.getAitMovimento().getTpStatus());
		if(aitMovimento.getCdMovimento() > 0 && aitMovimento.getTpStatus() == Integer.parseInt(protocolo.getTipoDocumento().getIdTipoDocumento())
				&& aitMovimento.getLgCancelaMovimento() == 0) {
			throw new ValidationException("Já existe um movimento de "+ getNmTipoDocumento(protocolo)+ " criado.");
		}
	}
	
	private String getNmTipoDocumento(ProtocoloDTO protocolo) {
		return protocolo.getTipoDocumento().getNmTipoDocumento();	
	}

}
