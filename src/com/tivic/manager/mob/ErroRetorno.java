package com.tivic.manager.mob;

public class ErroRetorno {

	private String nrErro;
	private int tpArquivo;
	private int tpRegistro;
	private String dsErro;
	private int tpRetorno;
	private String uf;
	private String dsSugestaoCorrecao;
	private int lgCorrecaoAutomatica;

	public ErroRetorno() { }

	public ErroRetorno(String nrErro,
			int tpArquivo,
			int tpRegistro,
			String dsErro,
			int tpRetorno,
			String uf,
			String dsSugestaoCorrecao,
			int lgCorrecaoAutomatica) {
		setNrErro(nrErro);
		setTpArquivo(tpArquivo);
		setTpRegistro(tpRegistro);
		setDsErro(dsErro);
		setTpRetorno(tpRetorno);
		setUf(uf);
		setDsSugestaoCorrecao(dsSugestaoCorrecao);
		setLgCorrecaoAutomatica(lgCorrecaoAutomatica);
	}
	public void setNrErro(String nrErro){
		this.nrErro=nrErro;
	}
	public String getNrErro(){
		return this.nrErro;
	}
	public void setTpArquivo(int tpArquivo){
		this.tpArquivo=tpArquivo;
	}
	public int getTpArquivo(){
		return this.tpArquivo;
	}
	public void setTpRegistro(int tpRegistro){
		this.tpRegistro=tpRegistro;
	}
	public int getTpRegistro(){
		return this.tpRegistro;
	}
	public void setDsErro(String dsErro){
		this.dsErro=dsErro;
	}
	public String getDsErro(){
		return this.dsErro;
	}
	public void setTpRetorno(int tpRetorno){
		this.tpRetorno=tpRetorno;
	}
	public int getTpRetorno(){
		return this.tpRetorno;
	}
	public void setUf(String uf){
		this.uf=uf;
	}
	public String getUf(){
		return this.uf;
	}
	public void setDsSugestaoCorrecao(String dsSugestaoCorrecao){
		this.dsSugestaoCorrecao=dsSugestaoCorrecao;
	}
	public String getDsSugestaoCorrecao(){
		return this.dsSugestaoCorrecao;
	}
	public void setLgCorrecaoAutomatica(int lgCorrecaoAutomatica){
		this.lgCorrecaoAutomatica=lgCorrecaoAutomatica;
	}
	public int getLgCorrecaoAutomatica(){
		return this.lgCorrecaoAutomatica;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "nrErro: " +  getNrErro();
		valueToString += ", tpArquivo: " +  getTpArquivo();
		valueToString += ", tpRegistro: " +  getTpRegistro();
		valueToString += ", dsErro: " +  getDsErro();
		valueToString += ", tpRetorno: " +  getTpRetorno();
		valueToString += ", uf: " +  getUf();
		valueToString += ", dsSugestaoCorrecao: " +  getDsSugestaoCorrecao();
		valueToString += ", lgCorrecaoAutomatica: " +  getLgCorrecaoAutomatica();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ErroRetorno(getNrErro(),
			getTpArquivo(),
			getTpRegistro(),
			getDsErro(),
			getTpRetorno(),
			getUf(),
			getDsSugestaoCorrecao(),
			getLgCorrecaoAutomatica());
	}

}