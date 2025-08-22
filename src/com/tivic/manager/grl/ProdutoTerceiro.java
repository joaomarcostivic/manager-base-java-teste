package com.tivic.manager.grl;

public class ProdutoTerceiro {

	private int cdProdutoTerceiro;
	private int cdMarca;
	private int cdModelo;
	private String nmProdutoTerceiro;
	private String nrSerie;
	private String idProdutoTerceiro;

	public ProdutoTerceiro(int cdProdutoTerceiro,
			int cdMarca,
			int cdModelo,
			String nmProdutoTerceiro,
			String nrSerie,
			String idProdutoTerceiro){
		setCdProdutoTerceiro(cdProdutoTerceiro);
		setCdMarca(cdMarca);
		setCdModelo(cdModelo);
		setNmProdutoTerceiro(nmProdutoTerceiro);
		setNrSerie(nrSerie);
		setIdProdutoTerceiro(idProdutoTerceiro);
	}
	public void setCdProdutoTerceiro(int cdProdutoTerceiro){
		this.cdProdutoTerceiro=cdProdutoTerceiro;
	}
	public int getCdProdutoTerceiro(){
		return this.cdProdutoTerceiro;
	}
	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public void setCdModelo(int cdModelo){
		this.cdModelo=cdModelo;
	}
	public int getCdModelo(){
		return this.cdModelo;
	}
	public void setNmProdutoTerceiro(String nmProdutoTerceiro){
		this.nmProdutoTerceiro=nmProdutoTerceiro;
	}
	public String getNmProdutoTerceiro(){
		return this.nmProdutoTerceiro;
	}
	public void setNrSerie(String nrSerie){
		this.nrSerie=nrSerie;
	}
	public String getNrSerie(){
		return this.nrSerie;
	}
	public void setIdProdutoTerceiro(String idProdutoTerceiro){
		this.idProdutoTerceiro=idProdutoTerceiro;
	}
	public String getIdProdutoTerceiro(){
		return this.idProdutoTerceiro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProdutoTerceiro: " +  getCdProdutoTerceiro();
		valueToString += ", cdMarca: " +  getCdMarca();
		valueToString += ", cdModelo: " +  getCdModelo();
		valueToString += ", nmProdutoTerceiro: " +  getNmProdutoTerceiro();
		valueToString += ", nrSerie: " +  getNrSerie();
		valueToString += ", idProdutoTerceiro: " +  getIdProdutoTerceiro();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProdutoTerceiro(getCdProdutoTerceiro(),
			getCdMarca(),
			getCdModelo(),
			getNmProdutoTerceiro(),
			getNrSerie(),
			getIdProdutoTerceiro());
	}

}
