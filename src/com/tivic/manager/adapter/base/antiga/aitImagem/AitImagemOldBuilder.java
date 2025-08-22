package com.tivic.manager.adapter.base.antiga.aitImagem;

public class AitImagemOldBuilder {
	private AitImagemOld aitImagemOld;
	
	public AitImagemOldBuilder() {
		this.aitImagemOld = new AitImagemOld();
	}
	
	public AitImagemOldBuilder setCdImagem(int cdImagem) {
		this.aitImagemOld.setCdImagem(cdImagem);
		return this;
	}
	
	public AitImagemOldBuilder setCdAit(int cdAit) {
		this.aitImagemOld.setCdAit(cdAit);
		return this;
	}
	
	public AitImagemOldBuilder setBlbImagem(byte[] blbImagem) {
		this.aitImagemOld.setBlbImagem(blbImagem);
		return this;
	}
	
	public AitImagemOldBuilder setTpImagem(int tpImagem) {
		this.aitImagemOld.setTpImagem(tpImagem);
		return this;
	}
	
	public AitImagemOldBuilder setLgImpressao(int lgImpressao) {
		this.aitImagemOld.setLgImpressao(lgImpressao);
		return this;
	}
	
	public AitImagemOld build() {
		return this.aitImagemOld;
	}
	
}
