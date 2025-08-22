package com.tivic.manager.ord;

public class OrdemServicoAtendimento {

	private int cdAtendimento;
	private int cdOrdemServico;
	private int lgPrincipal;

	public OrdemServicoAtendimento(){ }

	public OrdemServicoAtendimento(int cdAtendimento,
			int cdOrdemServico,
			int lgPrincipal){
		setCdAtendimento(cdAtendimento);
		setCdOrdemServico(cdOrdemServico);
		setLgPrincipal(lgPrincipal);
	}
	public void setCdAtendimento(int cdAtendimento){
		this.cdAtendimento=cdAtendimento;
	}
	public int getCdAtendimento(){
		return this.cdAtendimento;
	}
	public void setCdOrdemServico(int cdOrdemServico){
		this.cdOrdemServico=cdOrdemServico;
	}
	public int getCdOrdemServico(){
		return this.cdOrdemServico;
	}
	public void setLgPrincipal(int lgPrincipal){
		this.lgPrincipal=lgPrincipal;
	}
	public int getLgPrincipal(){
		return this.lgPrincipal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAtendimento: " +  getCdAtendimento();
		valueToString += ", cdOrdemServico: " +  getCdOrdemServico();
		valueToString += ", lgPrincipal: " +  getLgPrincipal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OrdemServicoAtendimento(getCdAtendimento(),
			getCdOrdemServico(),
			getLgPrincipal());
	}

}
