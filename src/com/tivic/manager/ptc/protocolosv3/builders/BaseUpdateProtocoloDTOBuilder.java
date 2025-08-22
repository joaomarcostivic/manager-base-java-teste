package com.tivic.manager.ptc.protocolosv3.builders;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.sol.cdi.BeansFactory;

public class BaseUpdateProtocoloDTOBuilder {
	private ProtocoloDTO protocolo;
	private DocumentoRepository documentoRepository;

	public BaseUpdateProtocoloDTOBuilder(ProtocoloDTO protocolo) throws Exception {
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		this.protocolo = protocolo;
	}

	public BaseUpdateProtocoloDTOBuilder documento() throws BadRequestException, Exception {
		Documento documentoUpdate =  documentoRepository.get(protocolo.getDocumento().getCdDocumento());
		documentoUpdate.setNmRequerente(protocolo.getDocumento().getNmRequerente());
		documentoUpdate.setCdFase(protocolo.getDocumento().getCdFase());
		documentoUpdate.setTxtObservacao(protocolo.getDocumento().getTxtObservacao());
		documentoUpdate.setNrDocumento(protocolo.getDocumento().getNrDocumento());
		documentoUpdate.setDtProtocolo(protocolo.getDocumento().getDtProtocolo());
		protocolo.setDocumento(documentoUpdate);
		return this;
	}
	
	public BaseUpdateProtocoloDTOBuilder documentoFase() throws BadRequestException, Exception {
		protocolo.getDocumento().setCdFase(protocolo.getFase().getCdFase());
		return this;
	}
		
	public ProtocoloDTO build() {
		return protocolo;
	}
}
