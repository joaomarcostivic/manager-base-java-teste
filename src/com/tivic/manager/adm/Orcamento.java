package com.tivic.manager.adm;

public class Orcamento {

	private int cdOrcamento;
	private String nmOrcamento;
	private int cdEmpresa;
	private int cdExercicio;
	private int cdCentroCusto;

	public Orcamento() { }

	public Orcamento(int cdOrcamento,
			String nmOrcamento,
			int cdEmpresa,
			int cdExercicio,
			int cdCentroCusto) {
		setCdOrcamento(cdOrcamento);
		setNmOrcamento(nmOrcamento);
		setCdEmpresa(cdEmpresa);
		setCdExercicio(cdExercicio);
		setCdCentroCusto(cdCentroCusto);
	}
	public void setCdOrcamento(int cdOrcamento){
		this.cdOrcamento=cdOrcamento;
	}
	public int getCdOrcamento(){
		return this.cdOrcamento;
	}
	public void setNmOrcamento(String nmOrcamento){
		this.nmOrcamento=nmOrcamento;
	}
	public String getNmOrcamento(){
		return this.nmOrcamento;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdExercicio(int cdExercicio){
		this.cdExercicio=cdExercicio;
	}
	public int getCdExercicio(){
		return this.cdExercicio;
	}
	public void setCdCentroCusto(int cdCentroCusto){
		this.cdCentroCusto=cdCentroCusto;
	}
	public int getCdCentroCusto(){
		return this.cdCentroCusto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrcamento: " +  getCdOrcamento();
		valueToString += ", nmOrcamento: " +  getNmOrcamento();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdExercicio: " +  getCdExercicio();
		valueToString += ", cdCentroCusto: " +  getCdCentroCusto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Orcamento(getCdOrcamento(),
			getNmOrcamento(),
			getCdEmpresa(),
			getCdExercicio(),
			getCdCentroCusto());
	}

}