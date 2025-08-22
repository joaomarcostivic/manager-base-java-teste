package com.tivic.manager.acd;

public class TipoRecursoInterligacaoComputadores {

	private int cdTipoRecursoInterligacaoComputadores;
	private String nmTipoRecursoInterligacaoComputadores;
	private String idTipoRecursoInterligacaoComputadores;
	private int stTipoRecursoInterligacaoComputadores;

	public TipoRecursoInterligacaoComputadores() { }

	public TipoRecursoInterligacaoComputadores(int cdTipoRecursoInterligacaoComputadores,
			String nmTipoRecursoInterligacaoComputadores,
			String idTipoRecursoInterligacaoComputadores,
			int stTipoRecursoInterligacaoComputadores) {
		setCdTipoRecursoInterligacaoComputadores(cdTipoRecursoInterligacaoComputadores);
		setNmTipoRecursoInterligacaoComputadores(nmTipoRecursoInterligacaoComputadores);
		setIdTipoRecursoInterligacaoComputadores(idTipoRecursoInterligacaoComputadores);
		setStTipoRecursoInterligacaoComputadores(stTipoRecursoInterligacaoComputadores);
	}
	public void setCdTipoRecursoInterligacaoComputadores(int cdTipoRecursoInterligacaoComputadores){
		this.cdTipoRecursoInterligacaoComputadores=cdTipoRecursoInterligacaoComputadores;
	}
	public int getCdTipoRecursoInterligacaoComputadores(){
		return this.cdTipoRecursoInterligacaoComputadores;
	}
	public void setNmTipoRecursoInterligacaoComputadores(String nmTipoRecursoInterligacaoComputadores){
		this.nmTipoRecursoInterligacaoComputadores=nmTipoRecursoInterligacaoComputadores;
	}
	public String getNmTipoRecursoInterligacaoComputadores(){
		return this.nmTipoRecursoInterligacaoComputadores;
	}
	public void setIdTipoRecursoInterligacaoComputadores(String idTipoRecursoInterligacaoComputadores){
		this.idTipoRecursoInterligacaoComputadores=idTipoRecursoInterligacaoComputadores;
	}
	public String getIdTipoRecursoInterligacaoComputadores(){
		return this.idTipoRecursoInterligacaoComputadores;
	}
	public void setStTipoRecursoInterligacaoComputadores(int stTipoRecursoInterligacaoComputadores){
		this.stTipoRecursoInterligacaoComputadores=stTipoRecursoInterligacaoComputadores;
	}
	public int getStTipoRecursoInterligacaoComputadores(){
		return this.stTipoRecursoInterligacaoComputadores;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoRecursoInterligacaoComputadores: " +  getCdTipoRecursoInterligacaoComputadores();
		valueToString += ", nmTipoRecursoInterligacaoComputadores: " +  getNmTipoRecursoInterligacaoComputadores();
		valueToString += ", idTipoRecursoInterligacaoComputadores: " +  getIdTipoRecursoInterligacaoComputadores();
		valueToString += ", stTipoRecursoInterligacaoComputadores: " +  getStTipoRecursoInterligacaoComputadores();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoRecursoInterligacaoComputadores(getCdTipoRecursoInterligacaoComputadores(),
			getNmTipoRecursoInterligacaoComputadores(),
			getIdTipoRecursoInterligacaoComputadores(),
			getStTipoRecursoInterligacaoComputadores());
	}

}