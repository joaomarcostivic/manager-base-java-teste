package com.tivic.manager.ptc.protocolosv3.ocorrencia;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ProtocoloOcorrenciaService implements IProtocoloOcorrenciaService {

	@Override
	public List<ProtocoloOcorrenciaDTO> find(Integer cdDocumento) throws Exception, ValidacaoException {
		if(cdDocumento <= 0)
			throw new ValidacaoException("Código do Documento Inválido");
		
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_documento", cdDocumento, cdDocumento > 0);
		
		Search<ProtocoloOcorrenciaDTO> search = new SearchBuilder<ProtocoloOcorrenciaDTO>("ptc_documento_ocorrencia A")
				.fields(" A.CD_DOCUMENTO, B.NM_TIPO_OCORRENCIA, A.TXT_OCORRENCIA, D.NM_PESSOA, A.DT_OCORRENCIA")
				.addJoinTable(" JOIN grl_tipo_ocorrencia B ON (A.cd_tipo_ocorrencia = B.cd_tipo_ocorrencia) ")
				.addJoinTable(" JOIN seg_usuario C ON (A.cd_usuario = C.cd_usuario) ")
				.addJoinTable(" JOIN grl_pessoa D ON (C.cd_pessoa = D.cd_pessoa) ")
				.searchCriterios(searchCriterios)
				.orderBy(" A.DT_OCORRENCIA DESC")
				.build();
		
		List<ProtocoloOcorrenciaDTO> ocorrencias = search.getList(ProtocoloOcorrenciaDTO.class);	
		if(ocorrencias == null)
			throw new NoContentException("Nenhuma ocorrencia encontrada");
		
		return ocorrencias;
	}

}
