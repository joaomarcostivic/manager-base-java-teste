package com.tivic.manager.pcb;

public class TabelaMedicao {

	private int cdTabelaMedicao;
	private int vlCm;
	private int vlLitros;
	private int cdTipoTanque;

	public TabelaMedicao(int cdTabelaMedicao,
			int vlCm,
			int vlLitros,
			int cdTipoTanque){
		setCdTabelaMedicao(cdTabelaMedicao);
		setVlCm(vlCm);
		setVlLitros(vlLitros);
		setCdTipoTanque(cdTipoTanque);
	}
	public void setCdTabelaMedicao(int cdTabelaMedicao){
		this.cdTabelaMedicao=cdTabelaMedicao;
	}
	public int getCdTabelaMedicao(){
		return this.cdTabelaMedicao;
	}
	public void setVlCm(int vlCm){
		this.vlCm=vlCm;
	}
	public int getVlCm(){
		return this.vlCm;
	}
	public void setVlLitros(int vlLitros){
		this.vlLitros=vlLitros;
	}
	public int getVlLitros(){
		return this.vlLitros;
	}
	public void setCdTipoTanque(int cdTipoTanque){
		this.cdTipoTanque=cdTipoTanque;
	}
	public int getCdTipoTanque(){
		return this.cdTipoTanque;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaMedicao: " +  getCdTabelaMedicao();
		valueToString += ", vlCm: " +  getVlCm();
		valueToString += ", vlLitros: " +  getVlLitros();
		valueToString += ", cdTipoTanque: " +  getCdTipoTanque();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaMedicao(getCdTabelaMedicao(),
			getVlCm(),
			getVlLitros(),
			getCdTipoTanque());
	}

}
