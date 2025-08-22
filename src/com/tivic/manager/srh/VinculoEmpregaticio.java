package com.tivic.manager.srh;

public class VinculoEmpregaticio {

	private int cdVinculoEmpregaticio;
	private String nmVinculoEmpregaticio;
	private String idVinculoEmpregaticio;
	private int tpRegimeJuridico;
	private String nrSefip;
	private String nrRais;
	private int lgDecimoMensal;
	private int lgFeriasMensal;
	private int lgTercoFeriasMensal;
	private int lgTercoFerias;
	private int lgFeriasMenorAno;
	private int lgCaged;
	private int lgRais;
	private int lgSefip;
	private int lgNaoGerarVencimento;
	private int cdTipoDesligamento;
	private int cdEmpresa;
	private int cdEventoFinanceiro;

	public VinculoEmpregaticio(){}
	public VinculoEmpregaticio(int cdVinculoEmpregaticio,
			String nmVinculoEmpregaticio,
			String idVinculoEmpregaticio,
			int tpRegimeJuridico,
			String nrSefip,
			String nrRais,
			int lgDecimoMensal,
			int lgFeriasMensal,
			int lgTercoFeriasMensal,
			int lgTercoFerias,
			int lgFeriasMenorAno,
			int lgCaged,
			int lgRais,
			int lgSefip,
			int lgNaoGerarVencimento,
			int cdTipoDesligamento,
			int cdEmpresa,
			int cdEventoFinanceiro){
		setCdVinculoEmpregaticio(cdVinculoEmpregaticio);
		setNmVinculoEmpregaticio(nmVinculoEmpregaticio);
		setIdVinculoEmpregaticio(idVinculoEmpregaticio);
		setTpRegimeJuridico(tpRegimeJuridico);
		setNrSefip(nrSefip);
		setNrRais(nrRais);
		setLgDecimoMensal(lgDecimoMensal);
		setLgFeriasMensal(lgFeriasMensal);
		setLgTercoFeriasMensal(lgTercoFeriasMensal);
		setLgTercoFerias(lgTercoFerias);
		setLgFeriasMenorAno(lgFeriasMenorAno);
		setLgCaged(lgCaged);
		setLgRais(lgRais);
		setLgSefip(lgSefip);
		setLgNaoGerarVencimento(lgNaoGerarVencimento);
		setCdTipoDesligamento(cdTipoDesligamento);
		setCdEmpresa(cdEmpresa);
		setCdEventoFinanceiro(cdEventoFinanceiro);
	}
	public void setCdVinculoEmpregaticio(int cdVinculoEmpregaticio){
		this.cdVinculoEmpregaticio=cdVinculoEmpregaticio;
	}
	public int getCdVinculoEmpregaticio(){
		return this.cdVinculoEmpregaticio;
	}
	public void setNmVinculoEmpregaticio(String nmVinculoEmpregaticio){
		this.nmVinculoEmpregaticio=nmVinculoEmpregaticio;
	}
	public String getNmVinculoEmpregaticio(){
		return this.nmVinculoEmpregaticio;
	}
	public void setIdVinculoEmpregaticio(String idVinculoEmpregaticio){
		this.idVinculoEmpregaticio=idVinculoEmpregaticio;
	}
	public String getIdVinculoEmpregaticio(){
		return this.idVinculoEmpregaticio;
	}
	public void setTpRegimeJuridico(int tpRegimeJuridico){
		this.tpRegimeJuridico=tpRegimeJuridico;
	}
	public int getTpRegimeJuridico(){
		return this.tpRegimeJuridico;
	}
	public void setNrSefip(String nrSefip){
		this.nrSefip=nrSefip;
	}
	public String getNrSefip(){
		return this.nrSefip;
	}
	public void setNrRais(String nrRais){
		this.nrRais=nrRais;
	}
	public String getNrRais(){
		return this.nrRais;
	}
	public void setLgDecimoMensal(int lgDecimoMensal){
		this.lgDecimoMensal=lgDecimoMensal;
	}
	public int getLgDecimoMensal(){
		return this.lgDecimoMensal;
	}
	public void setLgFeriasMensal(int lgFeriasMensal){
		this.lgFeriasMensal=lgFeriasMensal;
	}
	public int getLgFeriasMensal(){
		return this.lgFeriasMensal;
	}
	public void setLgTercoFeriasMensal(int lgTercoFeriasMensal){
		this.lgTercoFeriasMensal=lgTercoFeriasMensal;
	}
	public int getLgTercoFeriasMensal(){
		return this.lgTercoFeriasMensal;
	}
	public void setLgTercoFerias(int lgTercoFerias){
		this.lgTercoFerias=lgTercoFerias;
	}
	public int getLgTercoFerias(){
		return this.lgTercoFerias;
	}
	public void setLgFeriasMenorAno(int lgFeriasMenorAno){
		this.lgFeriasMenorAno=lgFeriasMenorAno;
	}
	public int getLgFeriasMenorAno(){
		return this.lgFeriasMenorAno;
	}
	public void setLgCaged(int lgCaged){
		this.lgCaged=lgCaged;
	}
	public int getLgCaged(){
		return this.lgCaged;
	}
	public void setLgRais(int lgRais){
		this.lgRais=lgRais;
	}
	public int getLgRais(){
		return this.lgRais;
	}
	public void setLgSefip(int lgSefip){
		this.lgSefip=lgSefip;
	}
	public int getLgSefip(){
		return this.lgSefip;
	}
	public void setLgNaoGerarVencimento(int lgNaoGerarVencimento){
		this.lgNaoGerarVencimento=lgNaoGerarVencimento;
	}
	public int getLgNaoGerarVencimento(){
		return this.lgNaoGerarVencimento;
	}
	public void setCdTipoDesligamento(int cdTipoDesligamento){
		this.cdTipoDesligamento=cdTipoDesligamento;
	}
	public int getCdTipoDesligamento(){
		return this.cdTipoDesligamento;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdEventoFinanceiro(int cdEventoFinanceiro){
		this.cdEventoFinanceiro=cdEventoFinanceiro;
	}
	public int getCdEventoFinanceiro(){
		return this.cdEventoFinanceiro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVinculoEmpregaticio: " +  getCdVinculoEmpregaticio();
		valueToString += ", nmVinculoEmpregaticio: " +  getNmVinculoEmpregaticio();
		valueToString += ", idVinculoEmpregaticio: " +  getIdVinculoEmpregaticio();
		valueToString += ", tpRegimeJuridico: " +  getTpRegimeJuridico();
		valueToString += ", nrSefip: " +  getNrSefip();
		valueToString += ", nrRais: " +  getNrRais();
		valueToString += ", lgDecimoMensal: " +  getLgDecimoMensal();
		valueToString += ", lgFeriasMensal: " +  getLgFeriasMensal();
		valueToString += ", lgTercoFeriasMensal: " +  getLgTercoFeriasMensal();
		valueToString += ", lgTercoFerias: " +  getLgTercoFerias();
		valueToString += ", lgFeriasMenorAno: " +  getLgFeriasMenorAno();
		valueToString += ", lgCaged: " +  getLgCaged();
		valueToString += ", lgRais: " +  getLgRais();
		valueToString += ", lgSefip: " +  getLgSefip();
		valueToString += ", lgNaoGerarVencimento: " +  getLgNaoGerarVencimento();
		valueToString += ", cdTipoDesligamento: " +  getCdTipoDesligamento();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new VinculoEmpregaticio(getCdVinculoEmpregaticio(),
			getNmVinculoEmpregaticio(),
			getIdVinculoEmpregaticio(),
			getTpRegimeJuridico(),
			getNrSefip(),
			getNrRais(),
			getLgDecimoMensal(),
			getLgFeriasMensal(),
			getLgTercoFeriasMensal(),
			getLgTercoFerias(),
			getLgFeriasMenorAno(),
			getLgCaged(),
			getLgRais(),
			getLgSefip(),
			getLgNaoGerarVencimento(),
			getCdTipoDesligamento(),
			getCdEmpresa(),
			getCdEventoFinanceiro());
	}

}