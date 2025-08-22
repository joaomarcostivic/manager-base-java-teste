package com.tivic.manager.ptc.portal.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.portal.TipoSistemaEnum;
import com.tivic.manager.ptc.portal.request.DocumentoPortalRequest;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;

public class ProtocoloInsertDTODirector {
	
	private DocumentoPortalRequest documentoRecurso;
	private ProtocoloInsertDTO protocoloInsertDTO;
	
	public ProtocoloInsertDTODirector(DocumentoPortalRequest documentoRecurso) {
		this.documentoRecurso = documentoRecurso;
	}

	public ProtocoloInsertDTODirector setProtocoloInsertDTO() {
		this.protocoloInsertDTO = new ProtocoloInsertDTO();
		protocoloInsertDTO.setCdAit(this.documentoRecurso.getCdAit());
		protocoloInsertDTO.setNmRequerente(this.documentoRecurso.getNmRequerente());
		protocoloInsertDTO.setCdUsuario(ParametroServices.getValorOfParametroAsInteger("MOB_USER_PORTAL", 1));
		protocoloInsertDTO.setArquivos(this.documentoRecurso.getArquivo());
		protocoloInsertDTO.setDtProtocolo(new GregorianCalendar());
		protocoloInsertDTO.setCdTipoDocumento(this.documentoRecurso.getTpDocumento());
		protocoloInsertDTO.setTpDocumento(TipoSistemaEnum.PORTAL.getKey());
		protocoloInsertDTO.setCdSituacaoDocumento(ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_PENDENTE", 0));
		protocoloInsertDTO.setTxtObservacao(this.documentoRecurso.getDsMotivoRecurso());
		return this;
	}
	
	public ProtocoloInsertDTO build() {
		return this.protocoloInsertDTO;
	}
}
