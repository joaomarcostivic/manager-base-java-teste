package com.tivic.manager.ptc.protocolosv3.validators.ait;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.validators.IProtocoloValidator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class DataJariValidator implements IProtocoloValidator {

	private IAitMovimentoService aitMovimentoServices;
	
	public DataJariValidator() throws Exception {
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}
	
	@Override
	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws Exception, ValidacaoException {
		dataAnteriorANip(protocolo, connection);
		dataAnteriorAdvertencia(protocolo, connection);
		
	}
	
	private void dataAnteriorANip(ProtocoloDTO protocolo, CustomConnection connection) throws Exception {
		AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoTpStatus(protocolo.getAit().getCdAit(), TipoStatusEnum.NIP_ENVIADA.getKey());
		if (protocolo.getDocumento().getDtProtocolo().before(aitMovimento.getDtMovimento())) {
			throw new ValidationException("A data do JARI não deve ser anterior a data da NIP.");
		}
	}
	
	private void dataAnteriorAdvertencia(ProtocoloDTO protocolo, CustomConnection connection) throws Exception {
		AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoTpStatus(protocolo.getAit().getCdAit(), TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
		if (protocolo.getDocumento().getDtProtocolo().before(aitMovimento.getDtMovimento())) {
			throw new ValidationException("A data do JARI não deve ser anterior a data da Notificação de Advertência.");
		}
	}

}
