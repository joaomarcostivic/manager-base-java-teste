package com.tivic.manager.mob.aitimagem;

import com.tivic.manager.mob.AitImagem;

public class AitImagemBuilder {
	private AitImagem aitImagem;
	
	public AitImagemBuilder() {
		this.aitImagem = new AitImagem();
	}
	
	public AitImagemBuilder setCdImagem(int cdImagem) {
		this.aitImagem.setCdImagem(cdImagem);
		return this;
	}
	
	public AitImagemBuilder setCdAit(int cdAit) {
		this.aitImagem.setCdAit(cdAit);
		return this;
	}
	
	public AitImagemBuilder setBlbImagem(byte[] blbImagem) {
		this.aitImagem.setBlbImagem(blbImagem);
		return this;
	}
	
	public AitImagemBuilder setTpImagem(int tpImagem) {
		this.aitImagem.setTpImagem(tpImagem);
		return this;
	}
	
	public AitImagemBuilder setLgImpressao(int lgImpressao) {
		this.aitImagem.setLgImpressao(lgImpressao);
		return this;
	}
	
	public AitImagem build() {
		return this.aitImagem;
	}
}
