package com.tivic.manager.mob.ait.imagemvideo.aitimagem;

public class AitImagensSearchBuilder {
	
	private AitImagemSearch aitImagensSearch;
	
	public AitImagensSearchBuilder() {
		this.aitImagensSearch = new AitImagemSearch();
	}
	
	public AitImagensSearchBuilder setCdAit(int cdAit) {
        this.aitImagensSearch.setCdAit(cdAit);
        return this;
    }
	
	public AitImagensSearchBuilder setCdImagem(int cdImagem) {
        this.aitImagensSearch.setCdImagem(cdImagem);
        return this;
    }
    
    public AitImagemSearch build() {
		return this.aitImagensSearch;
	}

}
