package com.tivic.manager.ptc.protocolosv3.validators.ait;

import java.util.List;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.validators.IProtocoloValidator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class NaiExistsValidator implements IProtocoloValidator {
	private IAitMovimentoService aitMovimentoService;
	
	public NaiExistsValidator() throws Exception {
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}
	
	@Override
	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws Exception, ValidacaoException {
		long qtdNaiEnviado = 0;
		List<AitMovimento> listAitMovimento = aitMovimentoService.getByAit(protocolo.getAit().getCdAit());
		qtdNaiEnviado = listAitMovimento.stream()
										.filter(e -> e.getTpStatus() == TipoStatusEnum.NAI_ENVIADO.getKey())
										.count();
		if(qtdNaiEnviado == 0) {
			throw new ValidationException("Não existe registro de NAI atrelado a este AIT.");
		}
	}

}
