package com.tivic.manager.srh;

public class TipoDesligamentoEvento {

	private int cdTipoDesligamento;
	private int cdEventoFinanceiro;
	private float prMultaFgts;
	private int qtMesesTrabalhados;

	public TipoDesligamentoEvento(){ }

	public TipoDesligamentoEvento(int cdTipoDesligamento,
			int cdEventoFinanceiro,
			float prMultaFgts,
			int qtMesesTrabalhados){
		setCdTipoDesligamento(cdTipoDesligamento);
		setCdEventoFinanceiro(cdEventoFinanceiro);
		setPrMultaFgts(prMultaFgts);
		setQtMesesTrabalhados(qtMesesTrabalhados);
	}
	public void setCdTipoDesligamento(int cdTipoDesligamento){
		this.cdTipoDesligamento=cdTipoDesligamento;
	}
	public int getCdTipoDesligamento(){
		return this.cdTipoDesligamento;
	}
	public void setCdEventoFinanceiro(int cdEventoFinanceiro){
		this.cdEventoFinanceiro=cdEventoFinanceiro;
	}
	public int getCdEventoFinanceiro(){
		return this.cdEventoFinanceiro;
	}
	public void setPrMultaFgts(float prMultaFgts){
		this.prMultaFgts=prMultaFgts;
	}
	public float getPrMultaFgts(){
		return this.prMultaFgts;
	}
	public void setQtMesesTrabalhados(int qtMesesTrabalhados){
		this.qtMesesTrabalhados=qtMesesTrabalhados;
	}
	public int getQtMesesTrabalhados(){
		return this.qtMesesTrabalhados;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDesligamento: " +  getCdTipoDesligamento();
		valueToString += ", cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		valueToString += ", prMultaFgts: " +  getPrMultaFgts();
		valueToString += ", qtMesesTrabalhados: " +  getQtMesesTrabalhados();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoDesligamentoEvento(getCdTipoDesligamento(),
			getCdEventoFinanceiro(),
			getPrMultaFgts(),
			getQtMesesTrabalhados());
	}

}
