package com.tivic.manager.adm;

public class ClassificacaoCliente {

	private int cdClassificacaoCliente;
	private String nmClassificacaoCliente;
	private float prTaxaPadraoFactoring;
	private float vlTaxaDevolucaoFactoring;
	private float prTaxaJurosFactoring;
	private float prTaxaProrrogacaoFactoring;
	private float vlLimiteFactoring;
	private float vlLimiteFactoringEmissor;
	private float vlLimiteFactoringUnitario;
	private int qtPrazoMinimoFactoring;
	private int qtPrazoMaximoFactoring;
	private int qtIdadeMinimaFactoring;
	private float vlGanhoMinimoFactoring;
	private float prTaxaMinimaFactoring;
	private int qtMaximoDocumento;

	public ClassificacaoCliente(int cdClassificacaoCliente,
			String nmClassificacaoCliente,
			float prTaxaPadraoFactoring,
			float vlTaxaDevolucaoFactoring,
			float prTaxaJurosFactoring,
			float prTaxaProrrogacaoFactoring,
			float vlLimiteFactoring,
			float vlLimiteFactoringEmissor,
			float vlLimiteFactoringUnitario,
			int qtPrazoMinimoFactoring,
			int qtPrazoMaximoFactoring,
			int qtIdadeMinimaFactoring,
			float vlGanhoMinimoFactoring,
			float prTaxaMinimaFactoring,
			int qtMaximoDocumento){
		setCdClassificacaoCliente(cdClassificacaoCliente);
		setNmClassificacaoCliente(nmClassificacaoCliente);
		setPrTaxaPadraoFactoring(prTaxaPadraoFactoring);
		setVlTaxaDevolucaoFactoring(vlTaxaDevolucaoFactoring);
		setPrTaxaJurosFactoring(prTaxaJurosFactoring);
		setPrTaxaProrrogacaoFactoring(prTaxaProrrogacaoFactoring);
		setVlLimiteFactoring(vlLimiteFactoring);
		setVlLimiteFactoringEmissor(vlLimiteFactoringEmissor);
		setVlLimiteFactoringUnitario(vlLimiteFactoringUnitario);
		setQtPrazoMinimoFactoring(qtPrazoMinimoFactoring);
		setQtPrazoMaximoFactoring(qtPrazoMaximoFactoring);
		setQtIdadeMinimaFactoring(qtIdadeMinimaFactoring);
		setVlGanhoMinimoFactoring(vlGanhoMinimoFactoring);
		setPrTaxaMinimaFactoring(prTaxaMinimaFactoring);
		setQtMaximoDocumento(qtMaximoDocumento);
	}
	public void setCdClassificacaoCliente(int cdClassificacaoCliente){
		this.cdClassificacaoCliente=cdClassificacaoCliente;
	}
	public int getCdClassificacaoCliente(){
		return this.cdClassificacaoCliente;
	}
	public void setNmClassificacaoCliente(String nmClassificacaoCliente){
		this.nmClassificacaoCliente=nmClassificacaoCliente;
	}
	public String getNmClassificacaoCliente(){
		return this.nmClassificacaoCliente;
	}
	public void setPrTaxaPadraoFactoring(float prTaxaPadraoFactoring){
		this.prTaxaPadraoFactoring=prTaxaPadraoFactoring;
	}
	public float getPrTaxaPadraoFactoring(){
		return this.prTaxaPadraoFactoring;
	}
	public void setVlTaxaDevolucaoFactoring(float vlTaxaDevolucaoFactoring){
		this.vlTaxaDevolucaoFactoring=vlTaxaDevolucaoFactoring;
	}
	public float getVlTaxaDevolucaoFactoring(){
		return this.vlTaxaDevolucaoFactoring;
	}
	public void setPrTaxaJurosFactoring(float prTaxaJurosFactoring){
		this.prTaxaJurosFactoring=prTaxaJurosFactoring;
	}
	public float getPrTaxaJurosFactoring(){
		return this.prTaxaJurosFactoring;
	}
	public void setPrTaxaProrrogacaoFactoring(float prTaxaProrrogacaoFactoring){
		this.prTaxaProrrogacaoFactoring=prTaxaProrrogacaoFactoring;
	}
	public float getPrTaxaProrrogacaoFactoring(){
		return this.prTaxaProrrogacaoFactoring;
	}
	public void setVlLimiteFactoring(float vlLimiteFactoring){
		this.vlLimiteFactoring=vlLimiteFactoring;
	}
	public float getVlLimiteFactoring(){
		return this.vlLimiteFactoring;
	}
	public void setVlLimiteFactoringEmissor(float vlLimiteFactoringEmissor){
		this.vlLimiteFactoringEmissor=vlLimiteFactoringEmissor;
	}
	public float getVlLimiteFactoringEmissor(){
		return this.vlLimiteFactoringEmissor;
	}
	public void setVlLimiteFactoringUnitario(float vlLimiteFactoringUnitario){
		this.vlLimiteFactoringUnitario=vlLimiteFactoringUnitario;
	}
	public float getVlLimiteFactoringUnitario(){
		return this.vlLimiteFactoringUnitario;
	}
	public void setQtPrazoMinimoFactoring(int qtPrazoMinimoFactoring){
		this.qtPrazoMinimoFactoring=qtPrazoMinimoFactoring;
	}
	public int getQtPrazoMinimoFactoring(){
		return this.qtPrazoMinimoFactoring;
	}
	public void setQtPrazoMaximoFactoring(int qtPrazoMaximoFactoring){
		this.qtPrazoMaximoFactoring=qtPrazoMaximoFactoring;
	}
	public int getQtPrazoMaximoFactoring(){
		return this.qtPrazoMaximoFactoring;
	}
	public void setQtIdadeMinimaFactoring(int qtIdadeMinimaFactoring){
		this.qtIdadeMinimaFactoring=qtIdadeMinimaFactoring;
	}
	public int getQtIdadeMinimaFactoring(){
		return this.qtIdadeMinimaFactoring;
	}
	public void setVlGanhoMinimoFactoring(float vlGanhoMinimoFactoring){
		this.vlGanhoMinimoFactoring=vlGanhoMinimoFactoring;
	}
	public float getVlGanhoMinimoFactoring(){
		return this.vlGanhoMinimoFactoring;
	}
	public void setPrTaxaMinimaFactoring(float prTaxaMinimaFactoring){
		this.prTaxaMinimaFactoring=prTaxaMinimaFactoring;
	}
	public float getPrTaxaMinimaFactoring(){
		return this.prTaxaMinimaFactoring;
	}
	public void setQtMaximoDocumento(int qtMaximoDocumento){
		this.qtMaximoDocumento=qtMaximoDocumento;
	}
	public int getQtMaximoDocumento(){
		return this.qtMaximoDocumento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdClassificacaoCliente: " +  getCdClassificacaoCliente();
		valueToString += ", nmClassificacaoCliente: " +  getNmClassificacaoCliente();
		valueToString += ", prTaxaPadraoFactoring: " +  getPrTaxaPadraoFactoring();
		valueToString += ", vlTaxaDevolucaoFactoring: " +  getVlTaxaDevolucaoFactoring();
		valueToString += ", prTaxaJurosFactoring: " +  getPrTaxaJurosFactoring();
		valueToString += ", prTaxaProrrogacaoFactoring: " +  getPrTaxaProrrogacaoFactoring();
		valueToString += ", vlLimiteFactoring: " +  getVlLimiteFactoring();
		valueToString += ", vlLimiteFactoringEmissor: " +  getVlLimiteFactoringEmissor();
		valueToString += ", vlLimiteFactoringUnitario: " +  getVlLimiteFactoringUnitario();
		valueToString += ", qtPrazoMinimoFactoring: " +  getQtPrazoMinimoFactoring();
		valueToString += ", qtPrazoMaximoFactoring: " +  getQtPrazoMaximoFactoring();
		valueToString += ", qtIdadeMinimaFactoring: " +  getQtIdadeMinimaFactoring();
		valueToString += ", vlGanhoMinimoFactoring: " +  getVlGanhoMinimoFactoring();
		valueToString += ", prTaxaMinimaFactoring: " +  getPrTaxaMinimaFactoring();
		valueToString += ", qtMaximoDocumento: " +  getQtMaximoDocumento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ClassificacaoCliente(getCdClassificacaoCliente(),
			getNmClassificacaoCliente(),
			getPrTaxaPadraoFactoring(),
			getVlTaxaDevolucaoFactoring(),
			getPrTaxaJurosFactoring(),
			getPrTaxaProrrogacaoFactoring(),
			getVlLimiteFactoring(),
			getVlLimiteFactoringEmissor(),
			getVlLimiteFactoringUnitario(),
			getQtPrazoMinimoFactoring(),
			getQtPrazoMaximoFactoring(),
			getQtIdadeMinimaFactoring(),
			getVlGanhoMinimoFactoring(),
			getPrTaxaMinimaFactoring(),
			getQtMaximoDocumento());
	}

}