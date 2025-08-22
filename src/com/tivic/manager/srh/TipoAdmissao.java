package com.tivic.manager.srh;

public class TipoAdmissao {

	private int cdTipoAdmissao;
	private String nmTipoAdmissao;
	private String idTipoAdmissao;
	private int cdTipoAdmissaoSuperior;
	private int stTipoAdmissao;

	public TipoAdmissao(int cdTipoAdmissao,
			String nmTipoAdmissao,
			String idTipoAdmissao, 
			int cdTipoAdmissaoSuperior,
			int stTipoAdmissao){
		setCdTipoAdmissao(cdTipoAdmissao);
		setNmTipoAdmissao(nmTipoAdmissao);
		setIdTipoAdmissao(idTipoAdmissao);
		setCdTipoAdmissaoSuperior(cdTipoAdmissaoSuperior);
		setStTipoAdmissao(stTipoAdmissao);
	}
	
	public TipoAdmissao() { }
	
	public void setCdTipoAdmissao(int cdTipoAdmissao){
		this.cdTipoAdmissao=cdTipoAdmissao;
	}
	public int getCdTipoAdmissao(){
		return this.cdTipoAdmissao;
	}
	public void setNmTipoAdmissao(String nmTipoAdmissao){
		this.nmTipoAdmissao=nmTipoAdmissao;
	}
	public String getNmTipoAdmissao(){
		return this.nmTipoAdmissao;
	}
	public void setIdTipoAdmissao(String idTipoAdmissao){
		this.idTipoAdmissao=idTipoAdmissao;
	}
	public String getIdTipoAdmissao(){
		return this.idTipoAdmissao;
	}
	public void setCdTipoAdmissaoSuperior(int cdTipoAdmissaoSuperior){
		this.cdTipoAdmissaoSuperior=cdTipoAdmissaoSuperior;
	}
	public int getCdTipoAdmissaoSuperior(){
		return this.cdTipoAdmissaoSuperior;
	}
	public void setStTipoAdmissao(int stTipoAdmissao){
		this.stTipoAdmissao=stTipoAdmissao;
	}
	public int getStTipoAdmissao(){
		return this.stTipoAdmissao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoAdmissao: " +  getCdTipoAdmissao();
		valueToString += ", nmTipoAdmissao: " +  getNmTipoAdmissao();
		valueToString += ", idTipoAdmissao: " +  getIdTipoAdmissao();
		valueToString += ", cdTipoAdmissaoSuperior: " +  getCdTipoAdmissaoSuperior();
		valueToString += ", stTipoAdmissao: " +  getStTipoAdmissao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoAdmissao(getCdTipoAdmissao(),
			getNmTipoAdmissao(),
			getIdTipoAdmissao(),
			getCdTipoAdmissaoSuperior(),
			getStTipoAdmissao());
	}

}