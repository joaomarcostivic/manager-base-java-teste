package com.tivic.manager.adm;

public class ProgramaFaturaPlano {

	private int cdProgramaFatura;
	private int cdPlanoPagamento;

	public ProgramaFaturaPlano(){ }

	public ProgramaFaturaPlano(int cdProgramaFatura,
			int cdPlanoPagamento){
		setCdProgramaFatura(cdProgramaFatura);
		setCdPlanoPagamento(cdPlanoPagamento);
	}
	public void setCdProgramaFatura(int cdProgramaFatura){
		this.cdProgramaFatura=cdProgramaFatura;
	}
	public int getCdProgramaFatura(){
		return this.cdProgramaFatura;
	}
	public void setCdPlanoPagamento(int cdPlanoPagamento){
		this.cdPlanoPagamento=cdPlanoPagamento;
	}
	public int getCdPlanoPagamento(){
		return this.cdPlanoPagamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProgramaFatura: " +  getCdProgramaFatura();
		valueToString += ", cdPlanoPagamento: " +  getCdPlanoPagamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProgramaFaturaPlano(getCdProgramaFatura(),
			getCdPlanoPagamento());
	}

}