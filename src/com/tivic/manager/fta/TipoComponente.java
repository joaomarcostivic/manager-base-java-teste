package com.tivic.manager.fta;

public class TipoComponente {

	private int cdTipoComponente;
	private String nmTipoComponente;
	private String txtTipoComponente;
	private float qtHodometroValidade;
	private float qtHodometroManutencao;
	private int tpRecorrenciaManutencao;
	private int qtIntervaloRecorrencia;

	public TipoComponente(int cdTipoComponente,
			String nmTipoComponente,
			String txtTipoComponente,
			float qtHodometroValidade,
			float qtHodometroManutencao,
			int tpRecorrenciaManutencao,
			int qtIntervaloRecorrencia){
		setCdTipoComponente(cdTipoComponente);
		setNmTipoComponente(nmTipoComponente);
		setTxtTipoComponente(txtTipoComponente);
		setQtHodometroValidade(qtHodometroValidade);
		setQtHodometroManutencao(qtHodometroManutencao);
		setTpRecorrenciaManutencao(tpRecorrenciaManutencao);
		setQtIntervaloRecorrencia(qtIntervaloRecorrencia);
	}
	public void setCdTipoComponente(int cdTipoComponente){
		this.cdTipoComponente=cdTipoComponente;
	}
	public int getCdTipoComponente(){
		return this.cdTipoComponente;
	}
	public void setNmTipoComponente(String nmTipoComponente){
		this.nmTipoComponente=nmTipoComponente;
	}
	public String getNmTipoComponente(){
		return this.nmTipoComponente;
	}
	public void setTxtTipoComponente(String txtTipoComponente){
		this.txtTipoComponente=txtTipoComponente;
	}
	public String getTxtTipoComponente(){
		return this.txtTipoComponente;
	}
	public void setQtHodometroValidade(float qtHodometroValidade){
		this.qtHodometroValidade=qtHodometroValidade;
	}
	public float getQtHodometroValidade(){
		return this.qtHodometroValidade;
	}
	public void setQtHodometroManutencao(float qtHodometroManutencao){
		this.qtHodometroManutencao=qtHodometroManutencao;
	}
	public float getQtHodometroManutencao(){
		return this.qtHodometroManutencao;
	}
	public void setTpRecorrenciaManutencao(int tpRecorrenciaManutencao){
		this.tpRecorrenciaManutencao=tpRecorrenciaManutencao;
	}
	public int getTpRecorrenciaManutencao(){
		return this.tpRecorrenciaManutencao;
	}
	public void setQtIntervaloRecorrencia(int qtIntervaloRecorrencia){
		this.qtIntervaloRecorrencia=qtIntervaloRecorrencia;
	}
	public int getQtIntervaloRecorrencia(){
		return this.qtIntervaloRecorrencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoComponente: " +  getCdTipoComponente();
		valueToString += ", nmTipoComponente: " +  getNmTipoComponente();
		valueToString += ", txtTipoComponente: " +  getTxtTipoComponente();
		valueToString += ", qtHodometroValidade: " +  getQtHodometroValidade();
		valueToString += ", qtHodometroManutencao: " +  getQtHodometroManutencao();
		valueToString += ", tpRecorrenciaManutencao: " +  getTpRecorrenciaManutencao();
		valueToString += ", qtIntervaloRecorrencia: " +  getQtIntervaloRecorrencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoComponente(getCdTipoComponente(),
			getNmTipoComponente(),
			getTxtTipoComponente(),
			getQtHodometroValidade(),
			getQtHodometroManutencao(),
			getTpRecorrenciaManutencao(),
			getQtIntervaloRecorrencia());
	}

}
