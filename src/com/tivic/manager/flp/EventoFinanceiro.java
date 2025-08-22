package com.tivic.manager.flp;

public class EventoFinanceiro extends com.tivic.manager.adm.EventoFinanceiro {

	private int tpContabilidade;
	private int lgRais;
	private int cdTabelaEvento;
	private int cdNaturezaEvento;
	private int tpEventoSistema;
	private int tpIncidenciaSalario;

	public EventoFinanceiro(int cdEventoFinanceiro,
			String nmEventoFinanceiro,
			int tpEventoFinanceiro,
			float vlEventoFinanceiro,
			String idEventoFinanceiro,
			int tpNaturezaDirf,
			int tpLancamento,
			int cdCategoriaEconomica,
			int tpContabilidade,
			int lgRais,
			int cdTabelaEvento,
			int cdNaturezaEvento,
			int tpEventoSistema,
			int tpIncidenciaSalario){
		super(cdEventoFinanceiro,
			nmEventoFinanceiro,
			tpEventoFinanceiro,
			vlEventoFinanceiro,
			idEventoFinanceiro,
			tpNaturezaDirf,
			tpLancamento,
			cdCategoriaEconomica);
		setTpContabilidade(tpContabilidade);
		setLgRais(lgRais);
		setCdTabelaEvento(cdTabelaEvento);
		setCdNaturezaEvento(cdNaturezaEvento);
		setTpEventoSistema(tpEventoSistema);
		setTpIncidenciaSalario(tpIncidenciaSalario);
	}
	public void setTpContabilidade(int tpContabilidade){
		this.tpContabilidade=tpContabilidade;
	}
	public int getTpContabilidade(){
		return this.tpContabilidade;
	}
	public void setLgRais(int lgRais){
		this.lgRais=lgRais;
	}
	public int getLgRais(){
		return this.lgRais;
	}
	public void setCdTabelaEvento(int cdTabelaEvento){
		this.cdTabelaEvento=cdTabelaEvento;
	}
	public int getCdTabelaEvento(){
		return this.cdTabelaEvento;
	}
	public void setCdNaturezaEvento(int cdNaturezaEvento){
		this.cdNaturezaEvento=cdNaturezaEvento;
	}
	public int getCdNaturezaEvento(){
		return this.cdNaturezaEvento;
	}
	public void setTpEventoSistema(int tpEventoSistema){
		this.tpEventoSistema=tpEventoSistema;
	}
	public int getTpEventoSistema(){
		return this.tpEventoSistema;
	}
	public void setTpIncidenciaSalario(int tpIncidenciaSalario){
		this.tpIncidenciaSalario=tpIncidenciaSalario;
	}
	public int getTpIncidenciaSalario(){
		return this.tpIncidenciaSalario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		valueToString += ", tpContabilidade: " +  getTpContabilidade();
		valueToString += ", lgRais: " +  getLgRais();
		valueToString += ", cdTabelaEvento: " +  getCdTabelaEvento();
		valueToString += ", cdNaturezaEvento: " +  getCdNaturezaEvento();
		valueToString += ", tpEventoSistema: " +  getTpEventoSistema();
		valueToString += ", tpIncidenciaSalario: " +  getTpIncidenciaSalario();
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
			getCdCategoriaEconomica(),
			getTpContabilidade(),
			getLgRais(),
			getCdTabelaEvento(),
			getCdNaturezaEvento(),
			getTpEventoSistema(),
			getTpIncidenciaSalario());
	}

}
