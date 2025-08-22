package com.tivic.manager.adm;

public class ClienteProgramaFatura {

	private int cdEmpresa;
	private int cdPessoa;
	private int cdProgramaFatura;
	private int cdClassificacao;
	private int cdClienteProgramaFatura;

	public ClienteProgramaFatura(int cdEmpresa,
			int cdPessoa,
			int cdProgramaFatura,
			int cdClassificacao,
			int cdClienteProgramaFatura){
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setCdProgramaFatura(cdProgramaFatura);
		setCdClassificacao(cdClassificacao);
		setCdClienteProgramaFatura(cdClienteProgramaFatura);
	}
	
	public ClienteProgramaFatura(int cdEmpresa,
			int cdPessoa,
			int cdProgramaFatura,
			int cdClienteProgramaFatura){
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setCdProgramaFatura(cdProgramaFatura);
		setCdClienteProgramaFatura(cdClienteProgramaFatura);
	}
	
	
	public ClienteProgramaFatura(int cdProgramaFatura,
			int cdClassificacao,
			int cdClienteProgramaFatura){
		setCdProgramaFatura(cdProgramaFatura);
		setCdClassificacao(cdClassificacao);
		setCdClienteProgramaFatura(cdClienteProgramaFatura);
	}
	
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdProgramaFatura(int cdProgramaFatura){
		this.cdProgramaFatura=cdProgramaFatura;
	}
	public int getCdProgramaFatura(){
		return this.cdProgramaFatura;
	}
	public void setCdClassificacao(int cdClassificacao){
		this.cdClassificacao=cdClassificacao;
	}
	public int getCdClassificacao(){
		return this.cdClassificacao;
	}
	public void setCdClienteProgramaFatura(int cdClienteProgramaFatura){
		this.cdClienteProgramaFatura=cdClienteProgramaFatura;
	}
	public int getCdClienteProgramaFatura(){
		return this.cdClienteProgramaFatura;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdProgramaFatura: " +  getCdProgramaFatura();
		valueToString += ", cdClassificacao: " +  getCdClassificacao();
		valueToString += ", cdClienteProgramaFatura: " +  getCdClienteProgramaFatura();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ClienteProgramaFatura(getCdEmpresa(),
			getCdPessoa(),
			getCdProgramaFatura(),
			getCdClassificacao(),
			getCdClienteProgramaFatura());
	}

}