package com.tivic.manager.ptc.protocolosv3.documento.situacao;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.SituacaoDocumento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.portal.credencialestacionamento.DatabaseConnectionManager;
import com.tivic.manager.ptc.protocolosv3.situacaodocumento.SituacaoDocumentoDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class SituacaoDocumentoService implements ISituacaoDocumentoService {

	private DocumentoRepository documentoRepository;
	
	public SituacaoDocumentoService() throws Exception {
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
	}
	
	@Override
	public List<SituacaoDocumento> getAll() throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<SituacaoDocumento> search = new SearchBuilder<SituacaoDocumento>("ptc_situacao_documento")
				.searchCriterios(searchCriterios)
				.build();
		return search.getList(SituacaoDocumento.class);
	}
	
	@Override
	public SituacaoDocumentoDTO getDocumentoByOcorrencia(int cdDocumento) throws Exception, ValidacaoException {
		int codigoFaseCancelado = ParametroServices.getValorOfParametroAsInteger("CD_FASE_CANCELADO", 0);
		Documento documento = documentoRepository.get(cdDocumento);
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("D.cd_documento", cdDocumento, cdDocumento > 0 );
		if(documento.getCdFase() == codigoFaseCancelado)
			return getFaseDocumento(searchCriterios);
		return getSituacaoDocumento(searchCriterios);
	}
	
	private SituacaoDocumentoDTO getFaseDocumento(SearchCriterios searchCriterios) throws Exception, ValidacaoException {
		Search<SituacaoDocumentoDTO> search = new SearchBuilder<SituacaoDocumentoDTO>("ptc_documento D")
				.fields("D.cd_documento, B.nm_fase AS nm_status_protocolo")
				.addJoinTable(" JOIN ptc_fase B ON (D.cd_fase = B.cd_fase)")
				.searchCriterios(searchCriterios)
				.build();
		if(search.getList(SituacaoDocumentoDTO.class).isEmpty())
			throw new NoContentException("Nenhuma situacao para este protocolo foi encontrada");
		SituacaoDocumentoDTO situacaoDocumentoDto = search.getList(SituacaoDocumentoDTO.class).get(0);
		return situacaoDocumentoDto;
	}

	private SituacaoDocumentoDTO getSituacaoDocumento(SearchCriterios searchCriterios) throws Exception, ValidacaoException {
		Search<SituacaoDocumentoDTO> search = new SearchBuilder<SituacaoDocumentoDTO>("mob_ait_movimento A")
				.fields("D.cd_documento, E.nm_situacao_documento AS nm_status_protocolo, G.id_ata")
				.addJoinTable(" JOIN mob_ait B ON (A.CD_AIT = B.CD_AIT) ")
				.addJoinTable(" JOIN mob_ait_movimento_documento C ON (C.CD_AIT = B.cd_ait AND A.cd_movimento = C.cd_movimento) ")
				.addJoinTable(" JOIN ptc_documento D ON (C.cd_documento = D.cd_documento) ")
				.addJoinTable(" JOIN ptc_situacao_documento E ON (D.cd_situacao_documento = E.cd_situacao_documento) ")
				.addJoinTable(" LEFT OUTER JOIN ptc_recurso F ON (D.cd_documento = F.cd_documento)")
				.addJoinTable(" LEFT OUTER JOIN ptc_ata G ON (F.cd_ata = G.cd_ata)")
				.searchCriterios(searchCriterios)
				.orderBy(" D.dt_protocolo DESC ")
				.build();
		if(search.getList(SituacaoDocumentoDTO.class).isEmpty())
			throw new NoContentException("Nenhuma situacao para este protocolo foi encontrada");
		SituacaoDocumentoDTO situacaoDocumentoDto = search.getList(SituacaoDocumentoDTO.class).get(0);
		return situacaoDocumentoDto;
	}

	@Override
	public SituacaoDocumentoDTO getSituacaoDocumento(int cdDocumento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);
			SituacaoDocumentoDTO situacaoDocumento = getSituacaoDocumento(cdDocumento, customConnection);
			customConnection.finishConnection();
			return situacaoDocumento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public SituacaoDocumentoDTO getSituacaoDocumento(int cdDocumento, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_documento", cdDocumento);
		Search<SituacaoDocumentoDTO> search = new SearchBuilder<SituacaoDocumentoDTO>("ptc_documento A")
				.fields("A.cd_documento, B.nm_situacao_documento AS nm_status_protocolo")
				.addJoinTable(" JOIN ptc_situacao_documento B ON (B.cd_situacao_documento = A.cd_situacao_documento) ")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		if(search.getList(SituacaoDocumentoDTO.class).isEmpty())
			throw new NoContentException("Nenhuma situacao para este protocolo foi encontrada");
		
		 return search.getList(SituacaoDocumentoDTO.class).get(0);
	}

	@Override
	public List<SituacaoDocumento> findAllSituacaoDocumento() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);
			List<SituacaoDocumento> situacoes = findAllSituacaoDocumento(customConnection);
			customConnection.finishConnection();
			return situacoes;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<SituacaoDocumento> findAllSituacaoDocumento(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<SituacaoDocumento> search = new SearchBuilder<SituacaoDocumento>("ptc_situacao_documento")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		if(search.getList(SituacaoDocumento.class).isEmpty())
			throw new NoContentException("Nenhuma situação foi encontrada");
		
		 return search.getList(SituacaoDocumento.class);
	}
}
