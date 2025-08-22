package com.tivic.manager.prc;

public class GrupoProcesso {

	private int cdGrupoProcesso;
	private String nmGrupoProcesso;
	private String idGrupoProcesso;
	private String nmEmail;
	private int cdCategoriaEconomica;
	private int stGrupoProcesso;

	public GrupoProcesso() { }

	public GrupoProcesso(int cdGrupoProcesso,
			String nmGrupoProcesso,
			String idGrupoProcesso,
			String nmEmail,
			int cdCategoriaEconomica,
			int stGrupoProcesso) {
		setCdGrupoProcesso(cdGrupoProcesso);
		setNmGrupoProcesso(nmGrupoProcesso);
		setIdGrupoProcesso(idGrupoProcesso);
		setNmEmail(nmEmail);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setStGrupoProcesso(stGrupoProcesso);
	}
	public void setCdGrupoProcesso(int cdGrupoProcesso){
		this.cdGrupoProcesso=cdGrupoProcesso;
	}
	public int getCdGrupoProcesso(){
		return this.cdGrupoProcesso;
	}
	public void setNmGrupoProcesso(String nmGrupoProcesso){
		this.nmGrupoProcesso=nmGrupoProcesso;
	}
	public String getNmGrupoProcesso(){
		return this.nmGrupoProcesso;
	}
	public void setIdGrupoProcesso(String idGrupoProcesso){
		this.idGrupoProcesso=idGrupoProcesso;
	}
	public String getIdGrupoProcesso(){
		return this.idGrupoProcesso;
	}
	public void setNmEmail(String nmEmail){
		this.nmEmail=nmEmail;
	}
	public String getNmEmail(){
		return this.nmEmail;
	}
	public void setCdCategoriaEconomica(int cdCategoriaEconomica){
		this.cdCategoriaEconomica=cdCategoriaEconomica;
	}
	public int getCdCategoriaEconomica(){
		return this.cdCategoriaEconomica;
	}
	public void setStGrupoProcesso(int stGrupoProcesso){
		this.stGrupoProcesso=stGrupoProcesso;
	}
	public int getStGrupoProcesso(){
		return this.stGrupoProcesso;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupoProcesso: " +  getCdGrupoProcesso();
		valueToString += ", nmGrupoProcesso: " +  getNmGrupoProcesso();
		valueToString += ", idGrupoProcesso: " +  getIdGrupoProcesso();
		valueToString += ", nmEmail: " +  getNmEmail();
		valueToString += ", cdCategoriaEconomica: " +  getCdCategoriaEconomica();
		valueToString += ", stGrupoProcesso: " +  getStGrupoProcesso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GrupoProcesso(getCdGrupoProcesso(),
			getNmGrupoProcesso(),
			getIdGrupoProcesso(),
			getNmEmail(),
			getCdCategoriaEconomica(),
			getStGrupoProcesso());
	}

}