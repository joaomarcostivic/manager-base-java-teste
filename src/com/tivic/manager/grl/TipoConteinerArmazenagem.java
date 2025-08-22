package com.tivic.manager.grl;

public class TipoConteinerArmazenagem {

	private int cdTipoConteinerPai;
	private int cdTipoConteiner;
	private float vlCapacidade;

	public TipoConteinerArmazenagem(int cdTipoConteinerPai,
			int cdTipoConteiner,
			float vlCapacidade){
		setCdTipoConteinerPai(cdTipoConteinerPai);
		setCdTipoConteiner(cdTipoConteiner);
		setVlCapacidade(vlCapacidade);
	}
	public void setCdTipoConteinerPai(int cdTipoConteinerPai){
		this.cdTipoConteinerPai=cdTipoConteinerPai;
	}
	public int getCdTipoConteinerPai(){
		return this.cdTipoConteinerPai;
	}
	public void setCdTipoConteiner(int cdTipoConteiner){
		this.cdTipoConteiner=cdTipoConteiner;
	}
	public int getCdTipoConteiner(){
		return this.cdTipoConteiner;
	}
	public void setVlCapacidade(float vlCapacidade){
		this.vlCapacidade=vlCapacidade;
	}
	public float getVlCapacidade(){
		return this.vlCapacidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoConteinerPai: " +  getCdTipoConteinerPai();
		valueToString += ", cdTipoConteiner: " +  getCdTipoConteiner();
		valueToString += ", vlCapacidade: " +  getVlCapacidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoConteinerArmazenagem(getCdTipoConteinerPai(),
			getCdTipoConteiner(),
			getVlCapacidade());
	}

}
