package com.tivic.manager.ptc.builders;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.sol.cdi.BeansFactory;

public class DocumentoBuilder {
	private Documento documento;
	private DocumentoRepository documentoRepository;

	public DocumentoBuilder(Documento documento) throws Exception {
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		this.documento = documento;
	}

	public DocumentoBuilder documento() throws BadRequestException, Exception {
		Documento documentoUpdate =  documentoRepository.get(documento.getCdDocumento());
		documentoUpdate.setNmRequerente(documento.getNmRequerente());
		documentoUpdate.setCdFase(documento.getCdFase());
		documentoUpdate.setTxtObservacao(documento.getTxtObservacao());
		documentoUpdate.setNrDocumento(documento.getNrDocumento());
		documentoUpdate.setDtProtocolo(documento.getDtProtocolo());
		documento = documentoUpdate;
		return this;
	}
	
	public Documento build() {
		return documento;
	}
}
