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

public class DataCetranValidator implements IProtocoloValidator {

	private IAitMovimentoService aitMovimentoServices;
	
	public DataCetranValidator() throws Exception {
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}
	
	@Override
	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws Exception, ValidacaoException {
		dataAnteriorAJari(protocolo, connection);
	}
	
	private void dataAnteriorAJari(ProtocoloDTO protocolo, CustomConnection connection) throws Exception {
		AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoTpStatus(protocolo.getAit().getCdAit(), TipoStatusEnum.RECURSO_JARI.getKey());
		if (protocolo.getDocumento().getDtProtocolo().before(aitMovimento.getDtMovimento())) {
			throw new ValidationException("A data do CETRAN não deve ser anterior a data da JARI.");
		}
	}

}
