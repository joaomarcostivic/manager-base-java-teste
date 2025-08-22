package com.tivic.manager.ptc.protocolosv3.resultado.validators;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.protocolos.validators.IValidator;
import com.tivic.manager.ptc.protocolosv3.resultado.ResultadoDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class DocumentoJulgadoValidator implements IValidator<ResultadoDTO>{
	DocumentoRepository documentoRepository;
	
	public DocumentoJulgadoValidator() throws Exception {
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
	}
	
	@Override
	public void validate(ResultadoDTO resultado, CustomConnection connection) throws ValidationException, Exception {

		Documento documento = documentoRepository.get(resultado.getCdDocumento());
		int cdParametro = ParametroServices.getValorOfParametroAsInteger("CD_FASE_JULGADO", 0);
		
		if(cdParametro == 0)
			throw new BadRequestException("O parâmetro CD_FASE_JULGADO não foi configurado.");
		
		if(documento.getCdFase() == cdParametro) {
			throw new ValidationException("Não é possível julgar um documento com resultado.");
		}
	}
}
