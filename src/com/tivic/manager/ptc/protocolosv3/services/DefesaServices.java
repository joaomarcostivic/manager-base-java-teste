package com.tivic.manager.ptc.protocolosv3.services;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.ptc.protocolosv3.IProtocoloDTOGet;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloService;
import com.tivic.manager.ptc.protocolosv3.builders.BaseProtocoloDTOSearchBuilder;
import com.tivic.manager.ptc.protocolosv3.builders.BaseProtocoloSearchBuilder;
import com.tivic.manager.ptc.protocolosv3.search.DadosProtocoloDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class DefesaServices extends ProtocoloService implements IProtocoloDTOGet{

	public DefesaServices() throws Exception {
		super();
	}

	@Override
	public ProtocoloDTO get(int cdDocumento) throws Exception, ValidacaoException {
		if(cdDocumento <= 0)
			throw new ValidacaoException("Código do Documento Inválido");
		
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("D.cd_documento", cdDocumento, cdDocumento > 0);
		SearchBuilder<DadosProtocoloDTO> searchBuilder = new BaseProtocoloSearchBuilder().getSearch(searchCriterios);
		Search<DadosProtocoloDTO> dadosProtocoloSearch = searchBuilder.build();
		List<DadosProtocoloDTO> dadosProtocolo = dadosProtocoloSearch.getList(DadosProtocoloDTO.class);	
		
		ProtocoloDTO protocolo = new BaseProtocoloDTOSearchBuilder(dadosProtocoloSearch, dadosProtocolo).build().get(0);
		if(protocolo == null)
			throw new NoContentException("Nenhum protocolo encontrado");
		return protocolo;
	}
}
