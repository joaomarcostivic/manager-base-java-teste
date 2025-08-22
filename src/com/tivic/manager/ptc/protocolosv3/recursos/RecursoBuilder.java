package com.tivic.manager.ptc.protocolosv3.recursos;

public class RecursoBuilder {
	protected Recurso recurso;
	
	public RecursoBuilder() {
		this.recurso = new Recurso();
	}
	
	public RecursoBuilder addCdAta(int cdAta) {
		this.recurso.setCdAta(cdAta);
		return this;
	}
	
	public RecursoBuilder addCdDocumento(int cdDocumento) {
		this.recurso.setCdDocumento(cdDocumento);
		return this;
	}
	
	public Recurso build() {
		return this.recurso;
	}
}
