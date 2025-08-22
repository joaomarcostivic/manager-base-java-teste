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

public class NipExistsValidator implements IProtocoloValidator {
	private IAitMovimentoService aitMovimentoService;
	private boolean EXISTS;
	
	public NipExistsValidator() throws Exception {
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}
	
	@Override
	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws Exception, ValidacaoException {
		List<AitMovimento> aitList = aitMovimentoService.getByAit(protocolo.getAit().getCdAit());
		if(!verificaNipEnviada(aitList)) {
			throw new ValidationException("Não existe uma NIP para este AIT.");
		}
	}

	private boolean verificaNipEnviada(List<AitMovimento> aitMovimentoList) {
		this.EXISTS = aitMovimentoList.stream().anyMatch(val -> val.getTpStatus() == TipoStatusEnum.NIP_ENVIADA.getKey() || val.getTpStatus() == TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
		return EXISTS;
	}
}
