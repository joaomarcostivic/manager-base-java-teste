package com.tivic.manager.acd;

public class InstituicaoTipoEquipamento {

	private int cdInstituicao;
	private int cdTipoEquipamento;
	private int qtEquipamento;
	private int cdPeriodoLetivo;

	public InstituicaoTipoEquipamento() { }

	public InstituicaoTipoEquipamento(int cdInstituicao,
			int cdTipoEquipamento,
			int qtEquipamento,
			int cdPeriodoLetivo) {
		setCdInstituicao(cdInstituicao);
		setCdTipoEquipamento(cdTipoEquipamento);
		setQtEquipamento(qtEquipamento);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdTipoEquipamento(int cdTipoEquipamento){
		this.cdTipoEquipamento=cdTipoEquipamento;
	}
	public int getCdTipoEquipamento(){
		return this.cdTipoEquipamento;
	}
	public void setQtEquipamento(int qtEquipamento){
		this.qtEquipamento=qtEquipamento;
	}
	public int getQtEquipamento(){
		return this.qtEquipamento;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdTipoEquipamento: " +  getCdTipoEquipamento();
		valueToString += ", qtEquipamento: " +  getQtEquipamento();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoTipoEquipamento(getCdInstituicao(),
			getCdTipoEquipamento(),
			getQtEquipamento(),
			getCdPeriodoLetivo());
	}

}