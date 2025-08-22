package com.tivic.manager.adm;

public class EventoFinanceiro {

	private int cdEventoFinanceiro;
	private String nmEventoFinanceiro;
	private int tpEventoFinanceiro;
	private float vlEventoFinanceiro;
	private String idEventoFinanceiro;
	private int tpNaturezaDirf;
	private int tpLancamento;
	private int stEvento;
	private int cdCategoriaEconomica;

	public EventoFinanceiro(){ }

	public EventoFinanceiro(int cdEventoFinanceiro,
			String nmEventoFinanceiro,
			int tpEventoFinanceiro,
			float vlEventoFinanceiro,
			String idEventoFinanceiro,
			int tpNaturezaDirf,
			int tpLancamento,
			int cdCategoriaEconomica){
		setCdEventoFinanceiro(cdEventoFinanceiro);
		setNmEventoFinanceiro(nmEventoFinanceiro);
		setTpEventoFinanceiro(tpEventoFinanceiro);
		setVlEventoFinanceiro(vlEventoFinanceiro);
		setIdEventoFinanceiro(idEventoFinanceiro);
		setTpNaturezaDirf(tpNaturezaDirf);
		setTpLancamento(tpLancamento);
		setCdCategoriaEconomica(cdCategoriaEconomica);
	}
	
	public EventoFinanceiro(int cdEventoFinanceiro,
			String nmEventoFinanceiro,
			int tpEventoFinanceiro,
			float vlEventoFinanceiro,
			String idEventoFinanceiro,
			int tpNaturezaDirf,
			int tpLancamento,
			int stEvento,
			int cdCategoriaEconomica){
		setCdEventoFinanceiro(cdEventoFinanceiro);
		setNmEventoFinanceiro(nmEventoFinanceiro);
		setTpEventoFinanceiro(tpEventoFinanceiro);
		setVlEventoFinanceiro(vlEventoFinanceiro);
		setIdEventoFinanceiro(idEventoFinanceiro);
		setTpNaturezaDirf(tpNaturezaDirf);
		setTpLancamento(tpLancamento);
		setStEvento(stEvento);
		setCdCategoriaEconomica(cdCategoriaEconomica);
	}
	
	public void setCdEventoFinanceiro(int cdEventoFinanceiro){
		this.cdEventoFinanceiro=cdEventoFinanceiro;
	}
	public int getCdEventoFinanceiro(){
		return this.cdEventoFinanceiro;
	}
	public void setNmEventoFinanceiro(String nmEventoFinanceiro){
		this.nmEventoFinanceiro=nmEventoFinanceiro;
	}
	public String getNmEventoFinanceiro(){
		return this.nmEventoFinanceiro;
	}
	public void setTpEventoFinanceiro(int tpEventoFinanceiro){
		this.tpEventoFinanceiro=tpEventoFinanceiro;
	}
	public int getTpEventoFinanceiro(){
		return this.tpEventoFinanceiro;
	}
	public void setVlEventoFinanceiro(float vlEventoFinanceiro){
		this.vlEventoFinanceiro=vlEventoFinanceiro;
	}
	public float getVlEventoFinanceiro(){
		return this.vlEventoFinanceiro;
	}
	public void setIdEventoFinanceiro(String idEventoFinanceiro){
		this.idEventoFinanceiro=idEventoFinanceiro;
	}
	public String getIdEventoFinanceiro(){
		return this.idEventoFinanceiro;
	}
	public void setTpNaturezaDirf(int tpNaturezaDirf){
		this.tpNaturezaDirf=tpNaturezaDirf;
	}
	public int getTpNaturezaDirf(){
		return this.tpNaturezaDirf;
	}
	public void setTpLancamento(int tpLancamento){
		this.tpLancamento=tpLancamento;
	}
	public int getTpLancamento(){
		return this.tpLancamento;
	}
	public void setStEvento(int stEvento){
		this.stEvento=stEvento;
	}
	public int getStEvento(){
		return this.stEvento;
	}
	public void setCdCategoriaEconomica(int cdCategoriaEconomica){
		this.cdCategoriaEconomica=cdCategoriaEconomica;
	}
	public int getCdCategoriaEconomica(){
		return this.cdCategoriaEconomica;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		valueToString += ", nmEventoFinanceiro: " +  getNmEventoFinanceiro();
		valueToString += ", tpEventoFinanceiro: " +  getTpEventoFinanceiro();
		valueToString += ", vlEventoFinanceiro: " +  getVlEventoFinanceiro();
		valueToString += ", idEventoFinanceiro: " +  getIdEventoFinanceiro();
		valueToString += ", tpNaturezaDirf: " +  getTpNaturezaDirf();
		valueToString += ", tpLancamento: " +  getTpLancamento();
		valueToString += ", stEvento: " +  getStEvento();
		valueToString += ", cdCategoriaEconomica: " +  getCdCategoriaEconomica();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EventoFinanceiro(getCdEventoFinanceiro(),
			getNmEventoFinanceiro(),
			getTpEventoFinanceiro(),
			getVlEventoFinanceiro(),
			getIdEventoFinanceiro(),
			getTpNaturezaDirf(),
			getTpLancamento(),
			getStEvento(),
			getCdCategoriaEconomica());
	}

}