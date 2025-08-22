package com.tivic.manager.mob.orgaoaquivo;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.orgaoaquivo.repository.OrgaoArquivoRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public class OrgaoArquivoService implements IOrgaoArquivoService {
	private OrgaoArquivoRepository orgaoArquivoRepository;
	
	public  OrgaoArquivoService() throws Exception {
		orgaoArquivoRepository = (OrgaoArquivoRepository) BeansFactory.get(OrgaoArquivoRepository.class);
	}

	@Override
	public OrgaoArquivoDTO getCaminhoPorTipoDocumento(int tpDocumento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<OrgaoArquivoDTO> orgaoArquivoDTO = getCaminhoPorTipoDocumento(tpDocumento, customConnection);
			if(orgaoArquivoDTO.getList(OrgaoArquivoDTO.class).isEmpty()) {
				throw new BadRequestException("Nenhum caminho encontrado!");
			}
			OrgaoArquivoDTO caminhoArquivo = orgaoArquivoDTO.getList(OrgaoArquivoDTO.class).get(0);
			customConnection.finishConnection();
			return caminhoArquivo;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Search<OrgaoArquivoDTO> getCaminhoPorTipoDocumento(int tpDocumento, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("tp_arquivo_documento", tpDocumento);
		return orgaoArquivoRepository.getCaminho(searchCriterios, customConnection);		
	}
}
