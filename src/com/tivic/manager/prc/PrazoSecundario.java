package com.tivic.manager.prc;

public class PrazoSecundario {

	private int cdPrazoSecundario;
	private int cdTipoPrazo;
	private int cdTipoProcesso;
	private int qtDias;
	private int stLiminar;
	private int tpContagemPrazo;
	private int tpAcao;

	public PrazoSecundario() { }

	public PrazoSecundario(int cdPrazoSecundario,
			int cdTipoPrazo,
			int cdTipoProcesso,
			int qtDias,
			int stLiminar,
			int tpContagemPrazo,
			int tpAcao) {
		setCdPrazoSecundario(cdPrazoSecundario);
		setCdTipoPrazo(cdTipoPrazo);
		setCdTipoProcesso(cdTipoProcesso);
		setQtDias(qtDias);
		setStLiminar(stLiminar);
		setTpContagemPrazo(tpContagemPrazo);
		setTpAcao(tpAcao);
	}
	public void setCdPrazoSecundario(int cdPrazoSecundario){
		this.cdPrazoSecundario=cdPrazoSecundario;
	}
	public int getCdPrazoSecundario(){
		return this.cdPrazoSecundario;
	}
	public void setCdTipoPrazo(int cdTipoPrazo){
		this.cdTipoPrazo=cdTipoPrazo;
	}
	public int getCdTipoPrazo(){
		return this.cdTipoPrazo;
	}
	public void setCdTipoProcesso(int cdTipoProcesso){
		this.cdTipoProcesso=cdTipoProcesso;
	}
	public int getCdTipoProcesso(){
		return this.cdTipoProcesso;
	}
	public void setQtDias(int qtDias){
		this.qtDias=qtDias;
	}
	public int getQtDias(){
		return this.qtDias;
	}
	public void setStLiminar(int stLiminar){
		this.stLiminar=stLiminar;
	}
	public int getStLiminar(){
		return this.stLiminar;
	}
	public void setTpContagemPrazo(int tpContagemPrazo){
		this.tpContagemPrazo=tpContagemPrazo;
	}
	public int getTpContagemPrazo(){
		return this.tpContagemPrazo;
	}
	public void setTpAcao(int tpAcao){
		this.tpAcao=tpAcao;
	}
	public int getTpAcao(){
		return this.tpAcao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPrazoSecundario: " +  getCdPrazoSecundario();
		valueToString += ", cdTipoPrazo: " +  getCdTipoPrazo();
		valueToString += ", cdTipoProcesso: " +  getCdTipoProcesso();
		valueToString += ", qtDias: " +  getQtDias();
		valueToString += ", stLiminar: " +  getStLiminar();
		valueToString += ", tpContagemPrazo: " +  getTpContagemPrazo();
		valueToString += ", tpAcao: " +  getTpAcao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PrazoSecundario(getCdPrazoSecundario(),
			getCdTipoPrazo(),
			getCdTipoProcesso(),
			getQtDias(),
			getStLiminar(),
			getTpContagemPrazo(),
			getTpAcao());
	}

}