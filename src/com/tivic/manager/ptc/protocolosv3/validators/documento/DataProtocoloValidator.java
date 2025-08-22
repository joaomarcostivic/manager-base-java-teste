package com.tivic.manager.ptc.protocolosv3.validators.documento;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.validators.IProtocoloValidator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class DataProtocoloValidator implements IProtocoloValidator {
	
	private AitRepository aitRepository;
	private IAitMovimentoService aitMovimentoServices;
	
	public DataProtocoloValidator() throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws ValidationException, Exception {
		dataPosterior(protocolo);
		dataAnteriorAInfracao(protocolo, connection);
		dataAnteriorANai(protocolo, connection);
	}
	
	private void dataPosterior(ProtocoloDTO protocolo) throws ValidationException {
		GregorianCalendar dataAtual = new GregorianCalendar();
		dataAtual.set(Calendar.HOUR, 23);
		dataAtual.set(Calendar.MINUTE, 59);
		dataAtual.set(Calendar.SECOND, 59);
		if(protocolo.getDocumento().getDtProtocolo().after(dataAtual)) {
			throw new ValidationException("A data do protocolo não deve ser posterior a data atual.");
		}
	}
	
	private void dataAnteriorAInfracao(ProtocoloDTO protocolo, CustomConnection connection) throws Exception {
		Ait ait = this.aitRepository.get(protocolo.getAit().getCdAit(), connection);
		if (protocolo.getDocumento().getDtProtocolo().before(ait.getDtInfracao())) {
			throw new ValidationException("A data do protocolo não deve ser anterior a data de infração.");
		}
	}
	
	private void dataAnteriorANai(ProtocoloDTO protocolo, CustomConnection connection) throws Exception {
		AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoTpStatus(protocolo.getAit().getCdAit(), TipoStatusEnum.NAI_ENVIADO.getKey());
		if (protocolo.getDocumento().getDtProtocolo().before(aitMovimento.getDtMovimento())) {
			throw new ValidationException("A data do protocolo não deve ser anterior a data da NAI.");
		}
	}

}
