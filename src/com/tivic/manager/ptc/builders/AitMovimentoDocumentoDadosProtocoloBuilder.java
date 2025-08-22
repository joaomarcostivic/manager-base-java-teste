package com.tivic.manager.ptc.builders;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;

public class AitMovimentoDocumentoDadosProtocoloBuilder {
	private AitMovimentoDocumentoDTO documento;
	private DadosProtocoloDTO protocoloDto;

	public AitMovimentoDocumentoDadosProtocoloBuilder(AitMovimentoDocumentoDTO documento) throws Exception {
		this.documento = documento;
		protocoloDto = new DadosProtocoloDTO();
	}

	public AitMovimentoDocumentoDadosProtocoloBuilder documento() throws BadRequestException, Exception {
		protocoloDto.setCdArquivo(documento.getDocumento().getCdArquivo());
		protocoloDto.setCdSetor(documento.getDocumento().getCdSetor());
		protocoloDto.setCdUsuario(documento.getDocumento().getCdUsuario());
		protocoloDto.setNmLocalOrigem(documento.getDocumento().getNmLocalOrigem());
		protocoloDto.setDtProtocolo(documento.getDocumento().getDtProtocolo());
		protocoloDto.setTpDocumento(documento.getDocumento().getTpDocumento());
		protocoloDto.setTxtObservacao(documento.getDocumento().getTxtObservacao());
		protocoloDto.setIdDocumento(documento.getDocumento().getIdDocumento());
		protocoloDto.setNrDocumento(documento.getDocumento().getNrDocumento());
		protocoloDto.setCdTipoDocumento(documento.getDocumento().getCdTipoDocumento());
		protocoloDto.setCdFase(documento.getFase().getCdFase());
		protocoloDto.setCdSituacaoDocumento(documento.getDocumento().getCdSituacaoDocumento());
		protocoloDto.setTxtDocumento(documento.getDocumento().getTxtDocumento());
		protocoloDto.setCdDocumentoSuperior(documento.getDocumento().getCdDocumentoSuperior());
		protocoloDto.setNrDocumentoExterno(documento.getDocumento().getNrDocumentoExterno());
		protocoloDto.setNrProtocoloExterno(documento.getDocumento().getNrProtocoloExterno());
		protocoloDto.setLgNotificacao(documento.getDocumento().getLgNotificacao());
		protocoloDto.setNmRequerente(documento.getDocumento().getNmRequerente());
		return this;
	}
	
	public AitMovimentoDocumentoDadosProtocoloBuilder movimento() throws BadRequestException, Exception {
		protocoloDto.setMovimento(documento.getMovimento());
		return this;
	}
	
	public AitMovimentoDocumentoDadosProtocoloBuilder ait() throws BadRequestException, Exception {
		protocoloDto.setAit(documento.getAit());
		return this;
	}
	
	public AitMovimentoDocumentoDadosProtocoloBuilder fase() throws BadRequestException, Exception {
		protocoloDto.setFase(documento.getFase());
		return this;
	}
	
	public AitMovimentoDocumentoDadosProtocoloBuilder tipoDocumento() throws BadRequestException, Exception {
		protocoloDto.setTipoDocumento(documento.getTipoDocumento());
		return this;
	}
	
	public AitMovimentoDocumentoDadosProtocoloBuilder agente() throws BadRequestException, Exception {
		protocoloDto.setAgente(documento.getAgente());
		return this;
	}
	
	public AitMovimentoDocumentoDadosProtocoloBuilder usuario() throws BadRequestException, Exception {
		protocoloDto.setUsuario(documento.getUsuario());
		return this;
	}
	
	public AitMovimentoDocumentoDadosProtocoloBuilder apresentacaoCondutor() throws BadRequestException, Exception {
		protocoloDto.setApresentacaoCondutor(documento.getApresentacaoCondutor());
		return this;
	}
	
	public AitMovimentoDocumentoDadosProtocoloBuilder anexos() throws BadRequestException, Exception {
		protocoloDto.setArquivos(documento.getArquivos());
		return this;
	}
	
	public DadosProtocoloDTO build() {
		return protocoloDto;
	}
}
