package com.tivic.manager.alm;

public class LocalArmazenamento {

	private int cdLocalArmazenamento;
	private int cdSetor;
	private int cdNivelLocal;
	private int cdResponsavel;
	private String nmLocalArmazenamento;
	private String idLocalArmazenamento;
	private int cdLocalArmazenamentoSuperior;
	private int cdEmpresa;

	public LocalArmazenamento(){}
	
	public LocalArmazenamento(int cdLocalArmazenamento,
			int cdSetor,
			int cdNivelLocal,
			int cdResponsavel,
			String nmLocalArmazenamento,
			String idLocalArmazenamento,
			int cdLocalArmazenamentoSuperior,
			int cdEmpresa){
		setCdLocalArmazenamento(cdLocalArmazenamento);
		setCdSetor(cdSetor);
		setCdNivelLocal(cdNivelLocal);
		setCdResponsavel(cdResponsavel);
		setNmLocalArmazenamento(nmLocalArmazenamento);
		setIdLocalArmazenamento(idLocalArmazenamento);
		setCdLocalArmazenamentoSuperior(cdLocalArmazenamentoSuperior);
		setCdEmpresa(cdEmpresa);
	}
	public void setCdLocalArmazenamento(int cdLocalArmazenamento){
		this.cdLocalArmazenamento=cdLocalArmazenamento;
	}
	public int getCdLocalArmazenamento(){
		return this.cdLocalArmazenamento;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setCdNivelLocal(int cdNivelLocal){
		this.cdNivelLocal=cdNivelLocal;
	}
	public int getCdNivelLocal(){
		return this.cdNivelLocal;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setNmLocalArmazenamento(String nmLocalArmazenamento){
		this.nmLocalArmazenamento=nmLocalArmazenamento;
	}
	public String getNmLocalArmazenamento(){
		return this.nmLocalArmazenamento;
	}
	public void setIdLocalArmazenamento(String idLocalArmazenamento){
		this.idLocalArmazenamento=idLocalArmazenamento;
	}
	public String getIdLocalArmazenamento(){
		return this.idLocalArmazenamento;
	}
	public void setCdLocalArmazenamentoSuperior(int cdLocalArmazenamentoSuperior){
		this.cdLocalArmazenamentoSuperior=cdLocalArmazenamentoSuperior;
	}
	public int getCdLocalArmazenamentoSuperior(){
		return this.cdLocalArmazenamentoSuperior;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLocalArmazenamento: " +  getCdLocalArmazenamento();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", cdNivelLocal: " +  getCdNivelLocal();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		valueToString += ", nmLocalArmazenamento: " +  getNmLocalArmazenamento();
		valueToString += ", idLocalArmazenamento: " +  getIdLocalArmazenamento();
		valueToString += ", cdLocalArmazenamentoSuperior: " +  getCdLocalArmazenamentoSuperior();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LocalArmazenamento(getCdLocalArmazenamento(),
			getCdSetor(),
			getCdNivelLocal(),
			getCdResponsavel(),
			getNmLocalArmazenamento(),
			getIdLocalArmazenamento(),
			getCdLocalArmazenamentoSuperior(),
			getCdEmpresa());
	}

}
