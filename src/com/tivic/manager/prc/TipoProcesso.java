package com.tivic.manager.prc;

public class TipoProcesso {

	private int cdTipoProcesso;
	private int cdAreaDireito;
	private String nmTipoProcesso;
	private String nmParte;
	private int tpContraParte;
	private String nmContraParte;
	private String nmOutroInteressado;
	private int lgSegredoJustica;
	private int tpSiteBusca;

	public TipoProcesso(){ }
	
	public TipoProcesso(int cdTipoProcesso,
			int cdAreaDireito,
			String nmTipoProcesso,
			String nmParte,
			int tpContraParte,
			String nmContraParte,
			String nmOutroInteressado,
			int lgSegredoJustica,
			int tpSiteBusca){
		setCdTipoProcesso(cdTipoProcesso);
		setCdAreaDireito(cdAreaDireito);
		setNmTipoProcesso(nmTipoProcesso);
		setNmParte(nmParte);
		setTpContraParte(tpContraParte);
		setNmContraParte(nmContraParte);
		setNmOutroInteressado(nmOutroInteressado);
		setLgSegredoJustica(lgSegredoJustica);
		setTpSiteBusca(tpSiteBusca);
	}
	public void setCdTipoProcesso(int cdTipoProcesso){
		this.cdTipoProcesso=cdTipoProcesso;
	}
	public int getCdTipoProcesso(){
		return this.cdTipoProcesso;
	}
	public void setCdAreaDireito(int cdAreaDireito){
		this.cdAreaDireito=cdAreaDireito;
	}
	public int getCdAreaDireito(){
		return this.cdAreaDireito;
	}
	public void setNmTipoProcesso(String nmTipoProcesso){
		this.nmTipoProcesso=nmTipoProcesso;
	}
	public String getNmTipoProcesso(){
		return this.nmTipoProcesso;
	}
	public void setNmParte(String nmParte){
		this.nmParte=nmParte;
	}
	public String getNmParte(){
		return this.nmParte;
	}
	public void setTpContraParte(int tpContraParte){
		this.tpContraParte=tpContraParte;
	}
	public int getTpContraParte(){
		return this.tpContraParte;
	}
	public void setNmContraParte(String nmContraParte){
		this.nmContraParte=nmContraParte;
	}
	public String getNmContraParte(){
		return this.nmContraParte;
	}
	public void setNmOutroInteressado(String nmOutroInteressado){
		this.nmOutroInteressado=nmOutroInteressado;
	}
	public String getNmOutroInteressado(){
		return this.nmOutroInteressado;
	}
	public void setLgSegredoJustica(int lgSegredoJustica){
		this.lgSegredoJustica=lgSegredoJustica;
	}
	public int getLgSegredoJustica(){
		return this.lgSegredoJustica;
	}
	public void setTpSiteBusca(int tpSiteBusca){
		this.tpSiteBusca=tpSiteBusca;
	}
	public int getTpSiteBusca(){
		return this.tpSiteBusca;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoProcesso: " +  getCdTipoProcesso();
		valueToString += ", cdAreaDireito: " +  getCdAreaDireito();
		valueToString += ", nmTipoProcesso: " +  getNmTipoProcesso();
		valueToString += ", nmParte: " +  getNmParte();
		valueToString += ", tpContraParte: " +  getTpContraParte();
		valueToString += ", nmContraParte: " +  getNmContraParte();
		valueToString += ", nmOutroInteressado: " +  getNmOutroInteressado();
		valueToString += ", lgSegredoJustica: " +  getLgSegredoJustica();
		valueToString += ", tpSiteBusca: " +  getTpSiteBusca();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoProcesso(getCdTipoProcesso(),
			getCdAreaDireito(),
			getNmTipoProcesso(),
			getNmParte(),
			getTpContraParte(),
			getNmContraParte(),
			getNmOutroInteressado(),
			getLgSegredoJustica(),
			getTpSiteBusca());
	}

}