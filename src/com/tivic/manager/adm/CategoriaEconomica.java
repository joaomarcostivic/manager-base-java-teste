package com.tivic.manager.adm;

public class CategoriaEconomica {

	private int cdCategoriaEconomica;
	private int cdCategoriaSuperior;
	private String nmCategoriaEconomica;
	private int tpCategoriaEconomica;
	private String nrCategoriaEconomica;
	private String idCategoriaEconomica;
	private int nrNivel;
	private int cdTabelaCatEconomica;
	private int lgAtivo;
	private Double vlAliquota;
	private int lgLancarFaturamento;

	public CategoriaEconomica() { }

	public CategoriaEconomica(int cdCategoriaEconomica,
			int cdCategoriaSuperior,
			String nmCategoriaEconomica,
			int tpCategoriaEconomica,
			String nrCategoriaEconomica,
			String idCategoriaEconomica,
			int nrNivel,
			int cdTabelaCatEconomica,
			int lgAtivo,
			Double vlAliquota,
			int lgLancarFaturamento) {
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setCdCategoriaSuperior(cdCategoriaSuperior);
		setNmCategoriaEconomica(nmCategoriaEconomica);
		setTpCategoriaEconomica(tpCategoriaEconomica);
		setNrCategoriaEconomica(nrCategoriaEconomica);
		setIdCategoriaEconomica(idCategoriaEconomica);
		setNrNivel(nrNivel);
		setCdTabelaCatEconomica(cdTabelaCatEconomica);
		setLgAtivo(lgAtivo);
		setVlAliquota(vlAliquota);
		setLgLancarFaturamento(lgLancarFaturamento);
	}
	public void setCdCategoriaEconomica(int cdCategoriaEconomica){
		this.cdCategoriaEconomica=cdCategoriaEconomica;
	}
	public int getCdCategoriaEconomica(){
		return this.cdCategoriaEconomica;
	}
	public void setCdCategoriaSuperior(int cdCategoriaSuperior){
		this.cdCategoriaSuperior=cdCategoriaSuperior;
	}
	public int getCdCategoriaSuperior(){
		return this.cdCategoriaSuperior;
	}
	public void setNmCategoriaEconomica(String nmCategoriaEconomica){
		this.nmCategoriaEconomica=nmCategoriaEconomica;
	}
	public String getNmCategoriaEconomica(){
		return this.nmCategoriaEconomica;
	}
	public void setTpCategoriaEconomica(int tpCategoriaEconomica){
		this.tpCategoriaEconomica=tpCategoriaEconomica;
	}
	public int getTpCategoriaEconomica(){
		return this.tpCategoriaEconomica;
	}
	public void setNrCategoriaEconomica(String nrCategoriaEconomica){
		this.nrCategoriaEconomica=nrCategoriaEconomica;
	}
	public String getNrCategoriaEconomica(){
		return this.nrCategoriaEconomica;
	}
	public void setIdCategoriaEconomica(String idCategoriaEconomica){
		this.idCategoriaEconomica=idCategoriaEconomica;
	}
	public String getIdCategoriaEconomica(){
		return this.idCategoriaEconomica;
	}
	public void setNrNivel(int nrNivel){
		this.nrNivel=nrNivel;
	}
	public int getNrNivel(){
		return this.nrNivel;
	}
	public void setCdTabelaCatEconomica(int cdTabelaCatEconomica){
		this.cdTabelaCatEconomica=cdTabelaCatEconomica;
	}
	public int getCdTabelaCatEconomica(){
		return this.cdTabelaCatEconomica;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public void setVlAliquota(Double vlAliquota){
		this.vlAliquota=vlAliquota;
	}
	public Double getVlAliquota(){
		return this.vlAliquota;
	}
	public void setLgLancarFaturamento(int lgLancarFaturamento){
		this.lgLancarFaturamento=lgLancarFaturamento;
	}
	public int getLgLancarFaturamento(){
		return this.lgLancarFaturamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCategoriaEconomica: " +  getCdCategoriaEconomica();
		valueToString += ", cdCategoriaSuperior: " +  getCdCategoriaSuperior();
		valueToString += ", nmCategoriaEconomica: " +  getNmCategoriaEconomica();
		valueToString += ", tpCategoriaEconomica: " +  getTpCategoriaEconomica();
		valueToString += ", nrCategoriaEconomica: " +  getNrCategoriaEconomica();
		valueToString += ", idCategoriaEconomica: " +  getIdCategoriaEconomica();
		valueToString += ", nrNivel: " +  getNrNivel();
		valueToString += ", cdTabelaCatEconomica: " +  getCdTabelaCatEconomica();
		valueToString += ", lgAtivo: " +  getLgAtivo();
		valueToString += ", vlAliquota: " +  getVlAliquota();
		valueToString += ", lgLancarFaturamento: " +  getLgLancarFaturamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CategoriaEconomica(getCdCategoriaEconomica(),
			getCdCategoriaSuperior(),
			getNmCategoriaEconomica(),
			getTpCategoriaEconomica(),
			getNrCategoriaEconomica(),
			getIdCategoriaEconomica(),
			getNrNivel(),
			getCdTabelaCatEconomica(),
			getLgAtivo(),
			getVlAliquota(),
			getLgLancarFaturamento());
	}

}