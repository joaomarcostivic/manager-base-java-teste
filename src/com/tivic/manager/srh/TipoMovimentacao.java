package com.tivic.manager.srh;

public class TipoMovimentacao {

	private int cdTipoMovimentacao;
	private String nmTipoMovimentacao;
	private int lgGerarEvento;
	private int lgDecimoTerceiro;
	private int lgFerias;
	private int lgDescontarDias;
	private int lgSalarioFamilia;
	private int lgValeTransporte;
	private int lgFatorCompensador;
	private int lgDescontaValeTransporte;
	private int cdEventoFinanceiro;
	private String idRaisSaida;
	private String idRaisRetorno;
	private String idTipoMovimentacao;

	public TipoMovimentacao(int cdTipoMovimentacao,
			String nmTipoMovimentacao,
			int lgGerarEvento,
			int lgDecimoTerceiro,
			int lgFerias,
			int lgDescontarDias,
			int lgSalarioFamilia,
			int lgValeTransporte,
			int lgFatorCompensador,
			int lgDescontaValeTransporte,
			int cdEventoFinanceiro,
			String idRaisSaida,
			String idRaisRetorno,
			String idTipoMovimentacao){
		setCdTipoMovimentacao(cdTipoMovimentacao);
		setNmTipoMovimentacao(nmTipoMovimentacao);
		setLgGerarEvento(lgGerarEvento);
		setLgDecimoTerceiro(lgDecimoTerceiro);
		setLgFerias(lgFerias);
		setLgDescontarDias(lgDescontarDias);
		setLgSalarioFamilia(lgSalarioFamilia);
		setLgValeTransporte(lgValeTransporte);
		setLgFatorCompensador(lgFatorCompensador);
		setLgDescontaValeTransporte(lgDescontaValeTransporte);
		setCdEventoFinanceiro(cdEventoFinanceiro);
		setIdRaisSaida(idRaisSaida);
		setIdRaisRetorno(idRaisRetorno);
		setIdTipoMovimentacao(idTipoMovimentacao);
	}
	public void setCdTipoMovimentacao(int cdTipoMovimentacao){
		this.cdTipoMovimentacao=cdTipoMovimentacao;
	}
	public int getCdTipoMovimentacao(){
		return this.cdTipoMovimentacao;
	}
	public void setNmTipoMovimentacao(String nmTipoMovimentacao){
		this.nmTipoMovimentacao=nmTipoMovimentacao;
	}
	public String getNmTipoMovimentacao(){
		return this.nmTipoMovimentacao;
	}
	public void setLgGerarEvento(int lgGerarEvento){
		this.lgGerarEvento=lgGerarEvento;
	}
	public int getLgGerarEvento(){
		return this.lgGerarEvento;
	}
	public void setLgDecimoTerceiro(int lgDecimoTerceiro){
		this.lgDecimoTerceiro=lgDecimoTerceiro;
	}
	public int getLgDecimoTerceiro(){
		return this.lgDecimoTerceiro;
	}
	public void setLgFerias(int lgFerias){
		this.lgFerias=lgFerias;
	}
	public int getLgFerias(){
		return this.lgFerias;
	}
	public void setLgDescontarDias(int lgDescontarDias){
		this.lgDescontarDias=lgDescontarDias;
	}
	public int getLgDescontarDias(){
		return this.lgDescontarDias;
	}
	public void setLgSalarioFamilia(int lgSalarioFamilia){
		this.lgSalarioFamilia=lgSalarioFamilia;
	}
	public int getLgSalarioFamilia(){
		return this.lgSalarioFamilia;
	}
	public void setLgValeTransporte(int lgValeTransporte){
		this.lgValeTransporte=lgValeTransporte;
	}
	public int getLgValeTransporte(){
		return this.lgValeTransporte;
	}
	public void setLgFatorCompensador(int lgFatorCompensador){
		this.lgFatorCompensador=lgFatorCompensador;
	}
	public int getLgFatorCompensador(){
		return this.lgFatorCompensador;
	}
	public void setLgDescontaValeTransporte(int lgDescontaValeTransporte){
		this.lgDescontaValeTransporte=lgDescontaValeTransporte;
	}
	public int getLgDescontaValeTransporte(){
		return this.lgDescontaValeTransporte;
	}
	public void setCdEventoFinanceiro(int cdEventoFinanceiro){
		this.cdEventoFinanceiro=cdEventoFinanceiro;
	}
	public int getCdEventoFinanceiro(){
		return this.cdEventoFinanceiro;
	}
	public void setIdRaisSaida(String idRaisSaida){
		this.idRaisSaida=idRaisSaida;
	}
	public String getIdRaisSaida(){
		return this.idRaisSaida;
	}
	public void setIdRaisRetorno(String idRaisRetorno){
		this.idRaisRetorno=idRaisRetorno;
	}
	public String getIdRaisRetorno(){
		return this.idRaisRetorno;
	}
	public void setIdTipoMovimentacao(String idTipoMovimentacao){
		this.idTipoMovimentacao=idTipoMovimentacao;
	}
	public String getIdTipoMovimentacao(){
		return this.idTipoMovimentacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoMovimentacao: " +  getCdTipoMovimentacao();
		valueToString += ", nmTipoMovimentacao: " +  getNmTipoMovimentacao();
		valueToString += ", lgGerarEvento: " +  getLgGerarEvento();
		valueToString += ", lgDecimoTerceiro: " +  getLgDecimoTerceiro();
		valueToString += ", lgFerias: " +  getLgFerias();
		valueToString += ", lgDescontarDias: " +  getLgDescontarDias();
		valueToString += ", lgSalarioFamilia: " +  getLgSalarioFamilia();
		valueToString += ", lgValeTransporte: " +  getLgValeTransporte();
		valueToString += ", lgFatorCompensador: " +  getLgFatorCompensador();
		valueToString += ", lgDescontaValeTransporte: " +  getLgDescontaValeTransporte();
		valueToString += ", cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		valueToString += ", idRaisSaida: " +  getIdRaisSaida();
		valueToString += ", idRaisRetorno: " +  getIdRaisRetorno();
		valueToString += ", idTipoMovimentacao: " +  getIdTipoMovimentacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoMovimentacao(getCdTipoMovimentacao(),
			getNmTipoMovimentacao(),
			getLgGerarEvento(),
			getLgDecimoTerceiro(),
			getLgFerias(),
			getLgDescontarDias(),
			getLgSalarioFamilia(),
			getLgValeTransporte(),
			getLgFatorCompensador(),
			getLgDescontaValeTransporte(),
			getCdEventoFinanceiro(),
			getIdRaisSaida(),
			getIdRaisRetorno(),
			getIdTipoMovimentacao());
	}

}