package com.tivic.manager.ord;

public class OrdemServicoTecnico {

	private int cdOrdemServico;
	private int cdPessoa;
	private int lgResponsavel;

	public OrdemServicoTecnico() {
	}

	public OrdemServicoTecnico(int cdOrdemServico,
			int cdPessoa,
			int lgResponsavel) {
		setCdOrdemServico(cdOrdemServico);
		setCdPessoa(cdPessoa);
		setLgResponsavel(lgResponsavel);
	}
	public void setCdOrdemServico(int cdOrdemServico){
		this.cdOrdemServico=cdOrdemServico;
	}
	public int getCdOrdemServico(){
		return this.cdOrdemServico;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setLgResponsavel(int lgResponsavel){
		this.lgResponsavel=lgResponsavel;
	}
	public int getLgResponsavel(){
		return this.lgResponsavel;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrdemServico: " +  getCdOrdemServico();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", lgResponsavel: " +  getLgResponsavel();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OrdemServicoTecnico(getCdOrdemServico(),
			getCdPessoa(),
			getLgResponsavel());
	}

}