package com.tivic.manager.ptc.protocolosv3.cancelamento;

import java.util.GregorianCalendar;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.protocolos.documentoocorrencia.DocumentoOcorrenciaRepository;
import com.tivic.manager.ptc.protocolosv3.DocumentoAtualizaStatusAIT;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.aitmovimento.CancelamentoAitMovimento;
import com.tivic.manager.ptc.protocolosv3.documento.ocorrencia.DocumentoOcorrenciaBuilder;
import com.tivic.manager.ptc.protocolosv3.documento.situacao.CancelamentoDocumento;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class CancelaEntradaProtocolo {
	
	private CancelamentoAitMovimento cancelaAitMovimento;
	private CancelamentoDocumento cancelaDocumento;
	private DocumentoAtualizaStatusAIT documentoAtualizaStatusAIT;
	private DocumentoOcorrenciaRepository documentoOcorrenciaRepository;
	private DocumentoRepository documentoRepository;
	
	public CancelaEntradaProtocolo() throws Exception {
		this.cancelaAitMovimento = new CancelamentoAitMovimento();
		this.cancelaDocumento = new CancelamentoDocumento();
		this.documentoAtualizaStatusAIT = new DocumentoAtualizaStatusAIT();
		this.documentoOcorrenciaRepository = (DocumentoOcorrenciaRepository) BeansFactory.get(DocumentoOcorrenciaRepository.class);
		this.documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
	}
	
	public void cancelar(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception {
		verificarCancelamento(protocolo, customConnection);
		this.cancelaAitMovimento.atualizaCancelamentoMovimento(protocolo, customConnection);
		this.cancelaDocumento.setDocumentoFaseCancelado(protocolo, customConnection);
		lancarOcorrenciaCancelamento(protocolo, customConnection);
		boolean isAtualizaStatusAIT = this.documentoAtualizaStatusAIT.verificar(protocolo.getDocumento().getCdTipoDocumento());
		if (isAtualizaStatusAIT) {
			this.documentoAtualizaStatusAIT.atualizarCancelado(protocolo, customConnection);
		}
	}
	
	private void verificarCancelamento(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception {
		Documento documento = documentoRepository.get(protocolo.getDocumento().getCdDocumento(), customConnection);
		int codigoCancelamento = ParametroServices.getValorOfParametroAsInteger("CD_FASE_CANCELADO", 0);
		if (documento.getCdFase() == codigoCancelamento)
			throw new ValidacaoException("Este documento já esta cancelado.");
	}
	
	private void lancarOcorrenciaCancelamento(ProtocoloDTO protocoloDto, CustomConnection customConnection) throws ValidacaoException, Exception {
		DocumentoOcorrencia documentoOcorrencia = new DocumentoOcorrenciaBuilder()
				.addCdDocumento(protocoloDto.getDocumento().getCdDocumento())
				.addCdTipoOcorrencia(ParametroServices.getValorOfParametroAsInteger("MOB_TIPO_OCORRENCIA_PROTOCOLO_CANCELADO", 0))
				.addCdUsuario(protocoloDto.getUsuario().getCdUsuario())
				.addDtOcorrencia(new GregorianCalendar())
				.build();
		documentoOcorrenciaRepository.insert(documentoOcorrencia, customConnection);
	}

}
