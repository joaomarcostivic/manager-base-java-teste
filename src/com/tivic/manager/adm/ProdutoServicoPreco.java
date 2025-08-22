package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ProdutoServicoPreco {

	private int cdTabelaPreco;
	private int cdProdutoServico;
	private int cdProdutoServicoPreco;
	private GregorianCalendar dtTerminoValidade;
	private float vlPreco;
	private float vlPrecoMinimo;
	private float vlPrecoMaximo;
	private float vlPrecoBase;
	private float vlDiferencaPrecoPadrao;
	private float vlDiferencaPrecoMinimo;
	private float vlDiferencaPrecoMaximo;
	private int cdTabelaPrecoBase;

	public ProdutoServicoPreco(int cdTabelaPreco,
			int cdProdutoServico,
			int cdProdutoServicoPreco,
			GregorianCalendar dtTerminoValidade,
			float vlPreco,
			float vlPrecoMinimo,
			float vlPrecoMaximo,
			float vlPrecoBase,
			float vlDiferencaPrecoPadrao,
			float vlDiferencaPrecoMinimo,
			float vlDiferencaPrecoMaximo,
			int cdTabelaPrecoBase){
		setCdTabelaPreco(cdTabelaPreco);
		setCdProdutoServico(cdProdutoServico);
		setCdProdutoServicoPreco(cdProdutoServicoPreco);
		setDtTerminoValidade(dtTerminoValidade);
		setVlPreco(vlPreco);
		setVlPrecoMinimo(vlPrecoMinimo);
		setVlPrecoMaximo(vlPrecoMaximo);
		setVlPrecoBase(vlPrecoBase);
		setVlDiferencaPrecoPadrao(vlDiferencaPrecoPadrao);
		setVlDiferencaPrecoMinimo(vlDiferencaPrecoMinimo);
		setVlDiferencaPrecoMaximo(vlDiferencaPrecoMaximo);
		setCdTabelaPrecoBase(cdTabelaPrecoBase);
	}
	
	public ProdutoServicoPreco(int cdTabelaPreco,
			int cdProdutoServico,
			int cdProdutoServicoPreco,
			GregorianCalendar dtTerminoValidade,
			float vlPreco){
		setCdTabelaPreco(cdTabelaPreco);
		setCdProdutoServico(cdProdutoServico);
		setCdProdutoServicoPreco(cdProdutoServicoPreco);
		setDtTerminoValidade(dtTerminoValidade);
		setVlPreco(vlPreco);
	}
	public void setCdTabelaPreco(int cdTabelaPreco){
		this.cdTabelaPreco=cdTabelaPreco;
	}
	public int getCdTabelaPreco(){
		return this.cdTabelaPreco;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdProdutoServicoPreco(int cdProdutoServicoPreco){
		this.cdProdutoServicoPreco=cdProdutoServicoPreco;
	}
	public int getCdProdutoServicoPreco(){
		return this.cdProdutoServicoPreco;
	}
	public void setDtTerminoValidade(GregorianCalendar dtTerminoValidade){
		this.dtTerminoValidade=dtTerminoValidade;
	}
	public GregorianCalendar getDtTerminoValidade(){
		return this.dtTerminoValidade;
	}
	public void setVlPreco(float vlPreco){
		this.vlPreco=vlPreco;
	}
	public float getVlPreco(){
		return this.vlPreco;
	}
	public void setVlPrecoMinimo(float vlPrecoMinimo){
		this.vlPrecoMinimo=vlPrecoMinimo;
	}
	public float getVlPrecoMinimo(){
		return this.vlPrecoMinimo;
	}
	public void setVlPrecoMaximo(float vlPrecoMaximo){
		this.vlPrecoMaximo=vlPrecoMaximo;
	}
	public float getVlPrecoMaximo(){
		return this.vlPrecoMaximo;
	}
	public void setVlPrecoBase(float vlPrecoBase){
		this.vlPrecoBase=vlPrecoBase;
	}
	public float getVlPrecoBase(){
		return this.vlPrecoBase;
	}
	public void setVlDiferencaPrecoPadrao(float vlDiferencaPrecoPadrao){
		this.vlDiferencaPrecoPadrao=vlDiferencaPrecoPadrao;
	}
	public float getVlDiferencaPrecoPadrao(){
		return this.vlDiferencaPrecoPadrao;
	}
	public void setVlDiferencaPrecoMinimo(float vlDiferencaPrecoMinimo){
		this.vlDiferencaPrecoMinimo=vlDiferencaPrecoMinimo;
	}
	public float getVlDiferencaPrecoMinimo(){
		return this.vlDiferencaPrecoMinimo;
	}
	public void setVlDiferencaPrecoMaximo(float vlDiferencaPrecoMaximo){
		this.vlDiferencaPrecoMaximo=vlDiferencaPrecoMaximo;
	}
	public float getVlDiferencaPrecoMaximo(){
		return this.vlDiferencaPrecoMaximo;
	}
	public void setCdTabelaPrecoBase(int cdTabelaPrecoBase){
		this.cdTabelaPrecoBase=cdTabelaPrecoBase;
	}
	public int getCdTabelaPrecoBase(){
		return this.cdTabelaPrecoBase;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaPreco: " +  getCdTabelaPreco();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdProdutoServicoPreco: " +  getCdProdutoServicoPreco();
		valueToString += ", dtTerminoValidade: " +  sol.util.Util.formatDateTime(getDtTerminoValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlPreco: " +  getVlPreco();
		valueToString += ", vlPrecoMinimo: " +  getVlPrecoMinimo();
		valueToString += ", vlPrecoMaximo: " +  getVlPrecoMaximo();
		valueToString += ", vlPrecoBase: " +  getVlPrecoBase();
		valueToString += ", vlDiferencaPrecoPadrao: " +  getVlDiferencaPrecoPadrao();
		valueToString += ", vlDiferencaPrecoMinimo: " +  getVlDiferencaPrecoMinimo();
		valueToString += ", vlDiferencaPrecoMaximo: " +  getVlDiferencaPrecoMaximo();
		valueToString += ", cdTabelaPrecoBase: " +  getCdTabelaPrecoBase();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProdutoServicoPreco(getCdTabelaPreco(),
			getCdProdutoServico(),
			getCdProdutoServicoPreco(),
			getDtTerminoValidade()==null ? null : (GregorianCalendar)getDtTerminoValidade().clone(),
			getVlPreco(),
			getVlPrecoMinimo(),
			getVlPrecoMaximo(),
			getVlPrecoBase(),
			getVlDiferencaPrecoPadrao(),
			getVlDiferencaPrecoMinimo(),
			getVlDiferencaPrecoMaximo(),
			getCdTabelaPrecoBase());
	}

}