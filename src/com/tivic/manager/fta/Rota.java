package com.tivic.manager.fta;

public class Rota {

	private int cdRota;
	private int cdTipoRota;
	private int cdCidadeOrigem;
	private int cdCidadeDestino;
	private int cdLogradouroOrigem;
	private int cdLogradouroDestino;
	private String nmLocalOrigem;
	private String nmLocalDestino;
	private float qtDistancia;
	private float vlFrete;
	private float vlFreteUnidade;
	private int lgPagamentoKm;
	private String nmRota;
	private int cdVendedor;

	public Rota(int cdRota,
			int cdTipoRota,
			int cdCidadeOrigem,
			int cdCidadeDestino,
			int cdLogradouroOrigem,
			int cdLogradouroDestino,
			String nmLocalOrigem,
			String nmLocalDestino,
			float qtDistancia,
			float vlFrete,
			float vlFreteUnidade,
			int lgPagamentoKm,
			String nmRota){
		setCdRota(cdRota);
		setCdTipoRota(cdTipoRota);
		setCdCidadeOrigem(cdCidadeOrigem);
		setCdCidadeDestino(cdCidadeDestino);
		setCdLogradouroOrigem(cdLogradouroOrigem);
		setCdLogradouroDestino(cdLogradouroDestino);
		setNmLocalOrigem(nmLocalOrigem);
		setNmLocalDestino(nmLocalDestino);
		setQtDistancia(qtDistancia);
		setVlFrete(vlFrete);
		setVlFreteUnidade(vlFreteUnidade);
		setLgPagamentoKm(lgPagamentoKm);
		setNmRota(nmRota);
	}
	
	public Rota(int cdRota,
			int cdTipoRota,
			int cdCidadeOrigem,
			int cdCidadeDestino,
			int cdLogradouroOrigem,
			int cdLogradouroDestino,
			String nmLocalOrigem,
			String nmLocalDestino,
			float qtDistancia,
			float vlFrete,
			float vlFreteUnidade,
			int lgPagamentoKm,
			String nmRota,
			int cdVendedor){
		setCdRota(cdRota);
		setCdTipoRota(cdTipoRota);
		setCdCidadeOrigem(cdCidadeOrigem);
		setCdCidadeDestino(cdCidadeDestino);
		setCdLogradouroOrigem(cdLogradouroOrigem);
		setCdLogradouroDestino(cdLogradouroDestino);
		setNmLocalOrigem(nmLocalOrigem);
		setNmLocalDestino(nmLocalDestino);
		setQtDistancia(qtDistancia);
		setVlFrete(vlFrete);
		setVlFreteUnidade(vlFreteUnidade);
		setLgPagamentoKm(lgPagamentoKm);
		setNmRota(nmRota);
		setCdVendedor(cdVendedor);
	}
	public void setCdRota(int cdRota){
		this.cdRota=cdRota;
	}
	public int getCdRota(){
		return this.cdRota;
	}
	public void setCdTipoRota(int cdTipoRota){
		this.cdTipoRota=cdTipoRota;
	}
	public int getCdTipoRota(){
		return this.cdTipoRota;
	}
	public void setCdCidadeOrigem(int cdCidadeOrigem){
		this.cdCidadeOrigem=cdCidadeOrigem;
	}
	public int getCdCidadeOrigem(){
		return this.cdCidadeOrigem;
	}
	public void setCdCidadeDestino(int cdCidadeDestino){
		this.cdCidadeDestino=cdCidadeDestino;
	}
	public int getCdCidadeDestino(){
		return this.cdCidadeDestino;
	}
	public void setCdLogradouroOrigem(int cdLogradouroOrigem){
		this.cdLogradouroOrigem=cdLogradouroOrigem;
	}
	public int getCdLogradouroOrigem(){
		return this.cdLogradouroOrigem;
	}
	public void setCdLogradouroDestino(int cdLogradouroDestino){
		this.cdLogradouroDestino=cdLogradouroDestino;
	}
	public int getCdLogradouroDestino(){
		return this.cdLogradouroDestino;
	}
	public void setNmLocalOrigem(String nmLocalOrigem){
		this.nmLocalOrigem=nmLocalOrigem;
	}
	public String getNmLocalOrigem(){
		return this.nmLocalOrigem;
	}
	public void setNmLocalDestino(String nmLocalDestino){
		this.nmLocalDestino=nmLocalDestino;
	}
	public String getNmLocalDestino(){
		return this.nmLocalDestino;
	}
	public void setQtDistancia(float qtDistancia){
		this.qtDistancia=qtDistancia;
	}
	public float getQtDistancia(){
		return this.qtDistancia;
	}
	public void setVlFrete(float vlFrete){
		this.vlFrete=vlFrete;
	}
	public float getVlFrete(){
		return this.vlFrete;
	}
	public void setVlFreteUnidade(float vlFreteUnidade){
		this.vlFreteUnidade=vlFreteUnidade;
	}
	public float getVlFreteUnidade(){
		return this.vlFreteUnidade;
	}
	public void setLgPagamentoKm(int lgPagamentoKm){
		this.lgPagamentoKm=lgPagamentoKm;
	}
	public int getLgPagamentoKm(){
		return this.lgPagamentoKm;
	}
	public void setNmRota(String nmRota){
		this.nmRota=nmRota;
	}
	public String getNmRota(){
		return this.nmRota;
	}
	public void setCdVendedor(int cdVendedor) {
		this.cdVendedor = cdVendedor;
	}
	public int getCdVendedor() {
		return cdVendedor;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRota: " +  getCdRota();
		valueToString += ", cdTipoRota: " +  getCdTipoRota();
		valueToString += ", cdCidadeOrigem: " +  getCdCidadeOrigem();
		valueToString += ", cdCidadeDestino: " +  getCdCidadeDestino();
		valueToString += ", cdLogradouroOrigem: " +  getCdLogradouroOrigem();
		valueToString += ", cdLogradouroDestino: " +  getCdLogradouroDestino();
		valueToString += ", nmLocalOrigem: " +  getNmLocalOrigem();
		valueToString += ", nmLocalDestino: " +  getNmLocalDestino();
		valueToString += ", qtDistancia: " +  getQtDistancia();
		valueToString += ", vlFrete: " +  getVlFrete();
		valueToString += ", vlFreteUnidade: " +  getVlFreteUnidade();
		valueToString += ", lgPagamentoKm: " +  getLgPagamentoKm();
		valueToString += ", nmRota: " +  getNmRota();
		valueToString += ", cdVendedor: " +  getCdVendedor();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Rota(getCdRota(),
			getCdTipoRota(),
			getCdCidadeOrigem(),
			getCdCidadeDestino(),
			getCdLogradouroOrigem(),
			getCdLogradouroDestino(),
			getNmLocalOrigem(),
			getNmLocalDestino(),
			getQtDistancia(),
			getVlFrete(),
			getVlFreteUnidade(),
			getLgPagamentoKm(),
			getNmRota(),
			getCdVendedor());
	}

}