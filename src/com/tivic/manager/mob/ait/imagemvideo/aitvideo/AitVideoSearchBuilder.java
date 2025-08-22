package com.tivic.manager.mob.ait.imagemvideo.aitvideo;

public class AitVideoSearchBuilder {
	
	private AitVideoSearch aitVideoSearch;
	
	public AitVideoSearchBuilder() {
		this.aitVideoSearch = new AitVideoSearch();
	}
	
	public AitVideoSearchBuilder setCdAit(int cdAit) {
        this.aitVideoSearch.setCdAit(cdAit);
        return this;
    }
	
	public AitVideoSearchBuilder setCdImagem(int cdImagem) {
        this.aitVideoSearch.setCdImagem(cdImagem);
        return this;
    }
    
    public AitVideoSearch build() {
		return this.aitVideoSearch;
	}

}
