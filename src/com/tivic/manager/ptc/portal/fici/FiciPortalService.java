package com.tivic.manager.ptc.portal.fici;

import com.tivic.manager.ptc.portal.builders.DocumentoPortalResponseBuilder;
import com.tivic.manager.ptc.portal.builders.ProtocoloInsertDTODirector;
import com.tivic.manager.ptc.portal.comprovante.IComprovanteService;
import com.tivic.manager.ptc.portal.request.DocumentoPortalRequest;
import com.tivic.manager.ptc.portal.response.DocumentoPortalResponse;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.arquivos.repository.IFileSystemRepository;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorRepository;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloService;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTOBuilder;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.ApresentacaoCondutorValidatorBuilder;
import com.tivic.manager.ptc.protocolosv3.builders.BasePtcDTOPortalBuilder;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDiretorioEnum;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class FiciPortalService extends ProtocoloService {
	
	private ApresentacaoCondutorRepository apresentacaoCondutorRepository;
	private IComprovanteService comprovanteService;
	private IFileSystemRepository fileSystemRepository;
	
	public FiciPortalService() throws Exception {
		super();
		this.apresentacaoCondutorRepository = (ApresentacaoCondutorRepository) BeansFactory.get(ApresentacaoCondutorRepository.class);
		this.comprovanteService = (IComprovanteService) BeansFactory.get(IComprovanteService.class);
		fileSystemRepository = (IFileSystemRepository) BeansFactory.get(IFileSystemRepository.class);
	}

	public DocumentoPortalResponse solicitar(DocumentoPortalRequest documentoRecurso, CustomConnection customConnection) throws Exception {
		ProtocoloInsertDTO protocoInsertloDTO = new ProtocoloInsertDTODirector(documentoRecurso).setProtocoloInsertDTO().build();
		ProtocoloDTO fici = insert(protocoInsertloDTO, customConnection);
		ApresentacaoCondutorDTO apresentacaoCondutorDTO = setApresentacaoCondutorDTO(fici, documentoRecurso);
		new ApresentacaoCondutorValidatorBuilder().validate(apresentacaoCondutorDTO, customConnection);
		this.apresentacaoCondutorRepository.insert(apresentacaoCondutorDTO.getApresentacaoCondutor(), customConnection);
		fici.setEmailSolicitante(documentoRecurso.getEmailSolicitante());
		fici.setCpfSolicitante(documentoRecurso.getNrCpfSolicitante());
		byte[] comprovante = this.comprovanteService.imprimirComprovante(fici, documentoRecurso.getReferer(), customConnection);
		return setdocumentoPortalResponse(fici.getDocumento().getNrDocumento(), comprovante); 
	}
	
	@Override
	 public ProtocoloDTO insert(ProtocoloInsertDTO dadosProtocolo, CustomConnection customConnection) throws Exception {
		ProtocoloDTO protocoloDTO = new BasePtcDTOPortalBuilder(dadosProtocolo, customConnection)
				.ait()
				.tipoDocumento()
				.fase(dadosProtocolo.getCdSituacaoDocumento())
				.movimento()
			.build();	
		this.validarDocumento(protocoloDTO, customConnection);
		this.aitMovimentoRepository.insert(protocoloDTO.getAitMovimento(), customConnection);
		this.documentoRepository.insert(protocoloDTO.getDocumento(), customConnection); 
		this.inserirAitMovimentoDocumento(protocoloDTO, customConnection);
		boolean isAtualizaStatusAIT = this.documentoAtualizaStatusAIT.verificar(protocoloDTO.getDocumento().getCdTipoDocumento());
		if (isAtualizaStatusAIT) {
			this.documentoAtualizaStatusAIT.atualizarInserido(protocoloDTO, customConnection);
		}
		this.inserirProtocoloRecurso(protocoloDTO, customConnection);
		for (Arquivo arquivo : dadosProtocolo.getArquivos()) {
			fileSystemRepository.insert(arquivo, TipoDiretorioEnum.PROTOCOLOS.getValue(), protocoloDTO.getDocumento().getCdDocumento(), customConnection);
		}
		this.saveArquivos(protocoloDTO, customConnection);
		return protocoloDTO;
	}
	
	private ApresentacaoCondutorDTO setApresentacaoCondutorDTO(ProtocoloDTO protocolo, DocumentoPortalRequest documentoRecurso) {
		return  new ApresentacaoCondutorDTOBuilder()
				.protocolo(protocolo)
				.apresentacaoCondutor(documentoRecurso)
			.build();
	}
	
	private DocumentoPortalResponse setdocumentoPortalResponse(String nrDocumento, byte[] protocoloRecebimento) {
		return new DocumentoPortalResponseBuilder()
				.setNrDocumento(nrDocumento)
				.setProtocoloRecebimento(protocoloRecebimento)
			.build();
	}
}
