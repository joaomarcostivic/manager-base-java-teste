package com.tivic.manager.mob;

public class ConcessaoLoteTurnoPassageiros {

	private int cdConcessaoTurnoPassageiros;
	private int cdConcessao;
	private int tpTurno;
	private int qtPassageiros;
	private int cdConcessaoLote;

	public ConcessaoLoteTurnoPassageiros() { }

	public ConcessaoLoteTurnoPassageiros(int cdConcessaoTurnoPassageiros,
			int cdConcessao,
			int tpTurno,
			int qtPassageiros,
			int cdConcessaoLote) {
		setCdConcessaoTurnoPassageiros(cdConcessaoTurnoPassageiros);
		setCdConcessao(cdConcessao);
		setTpTurno(tpTurno);
		setQtPassageiros(qtPassageiros);
		setCdConcessaoLote(cdConcessaoLote);
	}
	public void setCdConcessaoTurnoPassageiros(int cdConcessaoTurnoPassageiros){
		this.cdConcessaoTurnoPassageiros=cdConcessaoTurnoPassageiros;
	}
	public int getCdConcessaoTurnoPassageiros(){
		return this.cdConcessaoTurnoPassageiros;
	}
	public void setCdConcessao(int cdConcessao){
		this.cdConcessao=cdConcessao;
	}
	public int getCdConcessao(){
		return this.cdConcessao;
	}
	public void setTpTurno(int tpTurno){
		this.tpTurno=tpTurno;
	}
	public int getTpTurno(){
		return this.tpTurno;
	}
	public void setQtPassageiros(int qtPassageiros){
		this.qtPassageiros=qtPassageiros;
	}
	public int getQtPassageiros(){
		return this.qtPassageiros;
	}
	public void setCdConcessaoLote(int cdConcessaoLote){
		this.cdConcessaoLote=cdConcessaoLote;
	}
	public int getCdConcessaoLote(){
		return this.cdConcessaoLote;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConcessaoTurnoPassageiros: " +  getCdConcessaoTurnoPassageiros();
		valueToString += ", cdConcessao: " +  getCdConcessao();
		valueToString += ", tpTurno: " +  getTpTurno();
		valueToString += ", qtPassageiros: " +  getQtPassageiros();
		valueToString += ", cdConcessaoLote: " +  getCdConcessaoLote();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ConcessaoLoteTurnoPassageiros(getCdConcessaoTurnoPassageiros(),
			getCdConcessao(),
			getTpTurno(),
			getQtPassageiros(),
			getCdConcessaoLote());
	}

}