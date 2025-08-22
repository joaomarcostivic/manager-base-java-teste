package com.tivic.manager.ptc.protocolosv3.resultado.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.protocolosv3.resultado.ResultadoDTO;

public class DocumentoOcorrenciaBuilder {
	private DocumentoOcorrencia documentoOcorrencia;
	
	public DocumentoOcorrenciaBuilder() {
		documentoOcorrencia = new DocumentoOcorrencia();
	}
	
	public DocumentoOcorrenciaBuilder dadosBase(ResultadoDTO resultado) {
		documentoOcorrencia.setCdDocumento(resultado.getCdDocumento());
		documentoOcorrencia.setCdUsuario(resultado.getCdUsuario());
		documentoOcorrencia.setDtOcorrencia(resultado.getDtOcorrencia());
		documentoOcorrencia.setTxtOcorrencia(resultado.getTxtOcorrencia());
		documentoOcorrencia.setCdTipoOcorrencia(resultado.getCdTipoOcorrencia());
		documentoOcorrencia.setTpConsistencia(resultado.getTpConsistencia());
		return this;
	}
	
	public DocumentoOcorrenciaBuilder setDocumento(int cdDocumento) {
		this.documentoOcorrencia.setCdDocumento(cdDocumento);
		return this;
	}
	
	public DocumentoOcorrenciaBuilder setCdUsuario(int cdUsuario) {
		this.documentoOcorrencia.setCdUsuario(cdUsuario);
		return this;
	}
	
	public DocumentoOcorrenciaBuilder setDtOcorrencia(GregorianCalendar dtOcorrencia) {
		this.documentoOcorrencia.setDtOcorrencia(dtOcorrencia);
		return this;
	}
	
	public DocumentoOcorrenciaBuilder setTxtOcorrencia(String txtOcorrencia) {
		this.documentoOcorrencia.setTxtOcorrencia(txtOcorrencia);
		return this;
	}
	
	public DocumentoOcorrenciaBuilder setCdTipoOcorrencia(int cdTipoOcorrencia) {
		this.documentoOcorrencia.setCdTipoOcorrencia(cdTipoOcorrencia);
		return this;
	}
	
	public DocumentoOcorrenciaBuilder setTpConsistencia(int tpConsistencia) {
		this.documentoOcorrencia.setTpConsistencia(tpConsistencia);
		return this;
	}

	public DocumentoOcorrencia build() {
		return documentoOcorrencia;
	}
}
