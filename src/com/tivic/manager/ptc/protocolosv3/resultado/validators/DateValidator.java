package com.tivic.manager.ptc.protocolosv3.resultado.validators;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.protocolos.validators.IValidator;
import com.tivic.manager.ptc.protocolosv3.resultado.ResultadoDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class DateValidator implements IValidator<ResultadoDTO>{
	
	DocumentoRepository documentoRepository;
	
	public DateValidator() throws Exception {
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
	}
	
	@Override
	public void validate(ResultadoDTO resultado, CustomConnection connection) throws ValidationException, Exception {
		GregorianCalendar dataAtual = new GregorianCalendar();
		dataAtual.set(Calendar.HOUR, 23);
		dataAtual.set(Calendar.MINUTE, 59);
		dataAtual.set(Calendar.SECOND, 59);
		
		Documento documento = documentoRepository.get(resultado.getCdDocumento());
		
		if(documento.getDtProtocolo().after(resultado.getDtOcorrencia())) {
			throw new ValidationException("A data do resultado não deve ser anterior a data do protocolo.");
		}
		
		if(resultado.getDtOcorrencia().after(dataAtual)) {
			throw new ValidationException("A data do resultado não deve ser posterior a data atual.");
		}
	}
}
