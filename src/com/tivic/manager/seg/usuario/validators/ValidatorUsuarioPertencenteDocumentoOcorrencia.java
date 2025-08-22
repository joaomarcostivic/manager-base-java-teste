package com.tivic.manager.seg.usuario.validators;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.protocolos.documentoocorrencia.DocumentoOcorrenciaRepository;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ValidatorUsuarioPertencenteDocumentoOcorrencia implements Validator<Integer> {
	private DocumentoOcorrenciaRepository documentoOcorrenciaRepository;
	
	public ValidatorUsuarioPertencenteDocumentoOcorrencia() throws Exception{
		documentoOcorrenciaRepository = (DocumentoOcorrenciaRepository) BeansFactory.get(DocumentoOcorrenciaRepository.class);
	}

	@Override
	public void validate(Integer cdUsuario, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_usuario", cdUsuario);
		List<DocumentoOcorrencia> documentoOcorrencias = documentoOcorrenciaRepository.find(searchCriterios);
		if(!documentoOcorrencias.isEmpty()) {
			throw new BadRequestException("O usuário não pode ser excluído, pois está vinculado a ocorrências de documento.");
		}
	}
}
