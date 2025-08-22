package com.tivic.manager.acd;

public class EstatisticaItem {

	private int cdItem;
	private int cdApuracao;
	private int cdPeriodoLetivo;
	private int cdInstituicao;
	private int tpItem;
	private int tpQualificacao;
	private int cdCurso;
	private int cdTipoNecessidadeEspecial;
	private int qtApuracao;

	public EstatisticaItem() { }

	public EstatisticaItem(int cdItem,
			int cdApuracao,
			int cdPeriodoLetivo,
			int cdInstituicao,
			int tpItem,
			int tpQualificacao,
			int cdCurso,
			int cdTipoNecessidadeEspecial,
			int qtApuracao) {
		setCdItem(cdItem);
		setCdApuracao(cdApuracao);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setCdInstituicao(cdInstituicao);
		setTpItem(tpItem);
		setTpQualificacao(tpQualificacao);
		setCdCurso(cdCurso);
		setCdTipoNecessidadeEspecial(cdTipoNecessidadeEspecial);
		setQtApuracao(qtApuracao);
	}
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
	}
	public void setCdApuracao(int cdApuracao){
		this.cdApuracao=cdApuracao;
	}
	public int getCdApuracao(){
		return this.cdApuracao;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setTpItem(int tpItem){
		this.tpItem=tpItem;
	}
	public int getTpItem(){
		return this.tpItem;
	}
	public void setTpQualificacao(int tpQualificacao){
		this.tpQualificacao=tpQualificacao;
	}
	public int getTpQualificacao(){
		return this.tpQualificacao;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdTipoNecessidadeEspecial(int cdTipoNecessidadeEspecial){
		this.cdTipoNecessidadeEspecial=cdTipoNecessidadeEspecial;
	}
	public int getCdTipoNecessidadeEspecial(){
		return this.cdTipoNecessidadeEspecial;
	}
	public void setQtApuracao(int qtApuracao){
		this.qtApuracao=qtApuracao;
	}
	public int getQtApuracao(){
		return this.qtApuracao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdItem: " +  getCdItem();
		valueToString += ", cdApuracao: " +  getCdApuracao();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", tpItem: " +  getTpItem();
		valueToString += ", tpQualificacao: " +  getTpQualificacao();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdTipoNecessidadeEspecial: " +  getCdTipoNecessidadeEspecial();
		valueToString += ", qtApuracao: " +  getQtApuracao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EstatisticaItem(getCdItem(),
			getCdApuracao(),
			getCdPeriodoLetivo(),
			getCdInstituicao(),
			getTpItem(),
			getTpQualificacao(),
			getCdCurso(),
			getCdTipoNecessidadeEspecial(),
			getQtApuracao());
	}

}