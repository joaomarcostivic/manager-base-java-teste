package com.tivic.manager.ptc.protocolosv3.documento.ata;

public class AtaRelatorBuilder {
	protected AtaRelator ataRelator;
	
	public AtaRelatorBuilder() {
		this.ataRelator = new AtaRelator();
	}
	
	public AtaRelatorBuilder addCdAta(int cdAta) {
		this.ataRelator.setCdAta(cdAta);
		return this;
	}
	
	public AtaRelatorBuilder addCdPessoa(int cdPessoa) {
		this.ataRelator.setCdPessoa(cdPessoa);
		return this;
	}
	
	public AtaRelator build() {
		return this.ataRelator;
	}
}
