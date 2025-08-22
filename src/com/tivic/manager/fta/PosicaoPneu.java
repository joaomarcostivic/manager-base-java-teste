package com.tivic.manager.fta;

public class PosicaoPneu {

	private int cdPosicao;
	private int tpLocal;
	private int nrEixo;
	private int tpLado;
	private int tpDisposicao;

	public PosicaoPneu(int cdPosicao,
			int tpLocal,
			int nrEixo,
			int tpLado,
			int tpDisposicao){
		setCdPosicao(cdPosicao);
		setTpLocal(tpLocal);
		setNrEixo(nrEixo);
		setTpLado(tpLado);
		setTpDisposicao(tpDisposicao);
	}
	public void setCdPosicao(int cdPosicao){
		this.cdPosicao=cdPosicao;
	}
	public int getCdPosicao(){
		return this.cdPosicao;
	}
	public void setTpLocal(int tpLocal){
		this.tpLocal=tpLocal;
	}
	public int getTpLocal(){
		return this.tpLocal;
	}
	public void setNrEixo(int nrEixo){
		this.nrEixo=nrEixo;
	}
	public int getNrEixo(){
		return this.nrEixo;
	}
	public void setTpLado(int tpLado){
		this.tpLado=tpLado;
	}
	public int getTpLado(){
		return this.tpLado;
	}
	public void setTpDisposicao(int tpDisposicao){
		this.tpDisposicao=tpDisposicao;
	}
	public int getTpDisposicao(){
		return this.tpDisposicao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPosicao: " +  getCdPosicao();
		valueToString += ", tpLocal: " +  getTpLocal();
		valueToString += ", nrEixo: " +  getNrEixo();
		valueToString += ", tpLado: " +  getTpLado();
		valueToString += ", tpDisposicao: " +  getTpDisposicao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PosicaoPneu(getCdPosicao(),
			getTpLocal(),
			getNrEixo(),
			getTpLado(),
			getTpDisposicao());
	}

}