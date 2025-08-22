package com.tivic.manager.mob.ait.aitArquivo;

public class AitArquivoBuilder {

	private AitArquivo aitArquivo;
	
	public AitArquivoBuilder() {
		this.aitArquivo = new AitArquivo();
	}
	
	public AitArquivoBuilder addCdArquivo(int cdArquivo) {
		aitArquivo.setCdArquivo(cdArquivo);
		return this;
	}
	
	public AitArquivoBuilder addCdAit(int cdAit) {
		aitArquivo.setCdAit(cdAit);
		return this;
	}
	
    public AitArquivo build() {
    	return this.aitArquivo;
    }
	
}
