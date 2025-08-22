package com.tivic.manager.ptc.protocolosv3.documento.ocorrencia;

import java.util.GregorianCalendar;

import com.tivic.manager.ptc.DocumentoOcorrencia;

public class DocumentoOcorrenciaBuilder {
	private DocumentoOcorrencia documentoOcorrencia;
	
	public DocumentoOcorrenciaBuilder() {
		documentoOcorrencia = new DocumentoOcorrencia();
	}
	
	public DocumentoOcorrenciaBuilder addCdDocumento(int cdDocumento) {
		documentoOcorrencia.setCdDocumento(cdDocumento);
		return this;
	}
	
	public DocumentoOcorrenciaBuilder addCdTipoOcorrencia(int cdTipoOcorrencia) {
		documentoOcorrencia.setCdTipoOcorrencia(cdTipoOcorrencia);
		return this;
	}
	
	public DocumentoOcorrenciaBuilder addCdOcorrencia(int cdOcorrencia) {
		documentoOcorrencia.setCdOcorrencia(cdOcorrencia);
		return this;
	}
	
	public DocumentoOcorrenciaBuilder addCdUsuario(int cdUsuario) {
		documentoOcorrencia.setCdUsuario(cdUsuario);
		return this;
	}
	
	public DocumentoOcorrenciaBuilder addDtOcorrencia(GregorianCalendar  dtOcorrencia) {
		documentoOcorrencia.setDtOcorrencia(dtOcorrencia);
		return this;
	}
	
	public DocumentoOcorrenciaBuilder addTxtOcorrencia(String txtOcorrencia) {
		documentoOcorrencia.setTxtOcorrencia(txtOcorrencia);
		return this;
	}
	
	public DocumentoOcorrenciaBuilder addTpVisibilidade(int tpVisibilidade) {
		documentoOcorrencia.setCdDocumento(tpVisibilidade);
		return this;
	}
	
	public DocumentoOcorrenciaBuilder addTpConsistencia(int tpConsistencia) {
		documentoOcorrencia.setTpConsistencia(tpConsistencia);
		return this;
	}
	
	public DocumentoOcorrencia build() {
		return documentoOcorrencia;
	}
}
