package com.tivic.manager.prc;

public class AndamentoPrazo {

	private int cdAndamentoPrazo;
	private int cdTipoAndamento;
	private int cdTipoProcesso;
	private int cdTipoPrazo;
	private int qtDias;
	private int stLiminar;
	private int cdAreaDireito;
	private int tpPosicaoCliente;
	private int tpInstancia;
	private int tpContagemPrazo;

	public AndamentoPrazo(){ }

	public AndamentoPrazo(int cdAndamentoPrazo,
			int cdTipoAndamento,
			int cdTipoProcesso,
			int cdTipoPrazo,
			int qtDias,
			int stLiminar,
			int cdAreaDireito,
			int tpPosicaoCliente,
			int tpInstancia,
			int tpContagemPrazo){
		setCdAndamentoPrazo(cdAndamentoPrazo);
		setCdTipoAndamento(cdTipoAndamento);
		setCdTipoProcesso(cdTipoProcesso);
		setCdTipoPrazo(cdTipoPrazo);
		setQtDias(qtDias);
		setStLiminar(stLiminar);
		setCdAreaDireito(cdAreaDireito);
		setTpPosicaoCliente(tpPosicaoCliente);
		setTpInstancia(tpInstancia);
		setTpContagemPrazo(tpContagemPrazo);
	}
	public void setCdAndamentoPrazo(int cdAndamentoPrazo){
		this.cdAndamentoPrazo=cdAndamentoPrazo;
	}
	public int getCdAndamentoPrazo(){
		return this.cdAndamentoPrazo;
	}
	public void setCdTipoAndamento(int cdTipoAndamento){
		this.cdTipoAndamento=cdTipoAndamento;
	}
	public int getCdTipoAndamento(){
		return this.cdTipoAndamento;
	}
	public void setCdTipoProcesso(int cdTipoProcesso){
		this.cdTipoProcesso=cdTipoProcesso;
	}
	public int getCdTipoProcesso(){
		return this.cdTipoProcesso;
	}
	public void setCdTipoPrazo(int cdTipoPrazo){
		this.cdTipoPrazo=cdTipoPrazo;
	}
	public int getCdTipoPrazo(){
		return this.cdTipoPrazo;
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
	public void setCdAreaDireito(int cdAreaDireito){
		this.cdAreaDireito=cdAreaDireito;
	}
	public int getCdAreaDireito(){
		return this.cdAreaDireito;
	}
	public void setTpPosicaoCliente(int tpPosicaoCliente){
		this.tpPosicaoCliente=tpPosicaoCliente;
	}
	public int getTpPosicaoCliente(){
		return this.tpPosicaoCliente;
	}
	public void setTpInstancia(int tpInstancia){
		this.tpInstancia=tpInstancia;
	}
	public int getTpInstancia(){
		return this.tpInstancia;
	}
	public void setTpContagemPrazo(int tpContagemPrazo) {
		this.tpContagemPrazo = tpContagemPrazo;
	}
	public int getTpContagemPrazo() {
		return tpContagemPrazo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAndamentoPrazo: " +  getCdAndamentoPrazo();
		valueToString += ", cdTipoAndamento: " +  getCdTipoAndamento();
		valueToString += ", cdTipoProcesso: " +  getCdTipoProcesso();
		valueToString += ", cdTipoPrazo: " +  getCdTipoPrazo();
		valueToString += ", qtDias: " +  getQtDias();
		valueToString += ", stLiminar: " +  getStLiminar();
		valueToString += ", cdAreaDireito: " +  getCdAreaDireito();
		valueToString += ", tpPosicaoCliente: " +  getTpPosicaoCliente();
		valueToString += ", tpInstancia: " +  getTpInstancia();
		valueToString += ", tpContagemPrazo: " + getTpContagemPrazo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AndamentoPrazo(getCdAndamentoPrazo(),
			getCdTipoAndamento(),
			getCdTipoProcesso(),
			getCdTipoPrazo(),
			getQtDias(),
			getStLiminar(),
			getCdAreaDireito(),
			getTpPosicaoCliente(),
			getTpInstancia(),
			getTpContagemPrazo());
	}

}