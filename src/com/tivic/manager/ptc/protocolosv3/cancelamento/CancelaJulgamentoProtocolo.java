package com.tivic.manager.ptc.protocolosv3.cancelamento;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.protocolos.documentoocorrencia.DocumentoOcorrenciaRepository;
import com.tivic.manager.ptc.protocolosv3.DocumentoAtualizaStatusAIT;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.aitmovimento.CancelamentoAitMovimento;
import com.tivic.manager.ptc.protocolosv3.documento.ocorrencia.DocumentoOcorrenciaBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class CancelaJulgamentoProtocolo {
	
	private CancelamentoAitMovimento cancelaAitMovimento;
	private DocumentoAtualizaStatusAIT documentoAtualizaStatusAIT;
	private DocumentoOcorrenciaRepository documentoOcorrenciaRepository;
	private DocumentoRepository documentoRepository; 
	
	public CancelaJulgamentoProtocolo() throws Exception {
		this.cancelaAitMovimento = new CancelamentoAitMovimento();
		this.documentoAtualizaStatusAIT = new DocumentoAtualizaStatusAIT();
		this.documentoOcorrenciaRepository = (DocumentoOcorrenciaRepository) BeansFactory.get(DocumentoOcorrenciaRepository.class);
		this.documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class); 
	}
	
	public void cancelar(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception {
		new BuscaJulgamentoCancelado(protocolo).pegarDados();
		verificarCancelamento(protocolo, customConnection);
		this.cancelaAitMovimento.atualizaCancelamentoMovimento(protocolo, customConnection);
		lancarOcorrenciaCancelamento(protocolo, customConnection);
		retornarProtocoloPendente(protocolo, customConnection);
		boolean isAtualizaStatusAIT = this.documentoAtualizaStatusAIT.verificar(protocolo.getDocumento().getCdTipoDocumento());
		if (isAtualizaStatusAIT) {
			this.documentoAtualizaStatusAIT.atualizarCancelado(protocolo, customConnection);
		}
		new AtaDocumentoCancelado(protocolo, customConnection).verificar().removerDadosAta();
	}
	
	private void verificarCancelamento(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception {
		int codigoSituacaoPendente = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_PENDENTE", 0);
		Documento documento = documentoRepository.get(protocolo.getDocumento().getCdDocumento());
		SearchCriterios searchCriterios = criteriosDocumentoCancelado(documento.getCdDocumento());
		List<DocumentoOcorrencia> documentoOcorrenciaCancelamentoList = documentoOcorrenciaRepository.find(searchCriterios, customConnection);
		if (!documentoOcorrenciaCancelamentoList.isEmpty() && documento.getCdSituacaoDocumento() == codigoSituacaoPendente) {
			throw new ValidacaoException("O resultado deste julgamento já esta cancelado.");
		}
	}
	
	private SearchCriterios criteriosDocumentoCancelado(int cdDocumento) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_documento", cdDocumento, true);
		searchCriterios.addCriteriosEqualInteger("cd_tipo_ocorrencia", ParametroServices.getValorOfParametroAsInteger("MOB_TIPO_OCORRENCIA_JULGAMENTO_CANCELADO", 0), true);
		return searchCriterios;
	}
	
	private void lancarOcorrenciaCancelamento(ProtocoloDTO protocoloDto, CustomConnection customConnection) throws ValidacaoException, Exception {
		DocumentoOcorrencia documentoOcorrencia = new DocumentoOcorrenciaBuilder()
				.addCdDocumento(protocoloDto.getDocumento().getCdDocumento())
				.addCdTipoOcorrencia(ParametroServices.getValorOfParametroAsInteger("MOB_TIPO_OCORRENCIA_JULGAMENTO_CANCELADO", 0))
				.addDtOcorrencia(new GregorianCalendar())
				.addCdUsuario(protocoloDto.getUsuario().getCdUsuario())
				.build();
		documentoOcorrenciaRepository.insert(documentoOcorrencia, customConnection);
	}
	
	private void retornarProtocoloPendente(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception, ValidacaoException {
		Documento documento = documentoRepository.get(protocolo.getDocumento().getCdDocumento());
		documento.setCdFase(ParametroServices.getValorOfParametroAsInteger("CD_FASE_PENDENTE", 0));
		documento.setCdSituacaoDocumento(ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_PENDENTE", 0));
		documentoRepository.update(documento, customConnection);
	}
}
