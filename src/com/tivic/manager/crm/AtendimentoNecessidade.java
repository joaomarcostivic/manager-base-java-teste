package com.tivic.manager.crm;

public class AtendimentoNecessidade {

	private int cdAtendimento;
	private int cdTipoNecessidade;
	private String vlNecessidade;

	public AtendimentoNecessidade(int cdAtendimento,
			int cdTipoNecessidade,
			String vlNecessidade){
		setCdAtendimento(cdAtendimento);
		setCdTipoNecessidade(cdTipoNecessidade);
		setVlNecessidade(vlNecessidade);
	}
	public void setCdAtendimento(int cdAtendimento){
		this.cdAtendimento=cdAtendimento;
	}
	public int getCdAtendimento(){
		return this.cdAtendimento;
	}
	public void setCdTipoNecessidade(int cdTipoNecessidade){
		this.cdTipoNecessidade=cdTipoNecessidade;
	}
	public int getCdTipoNecessidade(){
		return this.cdTipoNecessidade;
	}
	public void setVlNecessidade(String vlNecessidade){
		this.vlNecessidade=vlNecessidade;
	}
	public String getVlNecessidade(){
		return this.vlNecessidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAtendimento: " +  getCdAtendimento();
		valueToString += ", cdTipoNecessidade: " +  getCdTipoNecessidade();
		valueToString += ", vlNecessidade: " +  getVlNecessidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AtendimentoNecessidade(getCdAtendimento(),
			getCdTipoNecessidade(),
			getVlNecessidade());
	}

}