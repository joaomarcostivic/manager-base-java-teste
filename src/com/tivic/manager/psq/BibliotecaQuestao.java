package com.tivic.manager.psq;

public class BibliotecaQuestao {

	private int cdQuestao;
	private int cdQuestionario;
	private int cdEmpresa;
	private int cdPessoa;
	private int cdVinculo;

	public BibliotecaQuestao(int cdQuestao,
			int cdQuestionario,
			int cdEmpresa,
			int cdPessoa,
			int cdVinculo){
		setCdQuestao(cdQuestao);
		setCdQuestionario(cdQuestionario);
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setCdVinculo(cdVinculo);
	}
	public void setCdQuestao(int cdQuestao){
		this.cdQuestao=cdQuestao;
	}
	public int getCdQuestao(){
		return this.cdQuestao;
	}
	public void setCdQuestionario(int cdQuestionario){
		this.cdQuestionario=cdQuestionario;
	}
	public int getCdQuestionario(){
		return this.cdQuestionario;
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
	public void setCdVinculo(int cdVinculo){
		this.cdVinculo=cdVinculo;
	}
	public int getCdVinculo(){
		return this.cdVinculo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdQuestao: " +  getCdQuestao();
		valueToString += ", cdQuestionario: " +  getCdQuestionario();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdVinculo: " +  getCdVinculo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BibliotecaQuestao(getCdQuestao(),
			getCdQuestionario(),
			getCdEmpresa(),
			getCdPessoa(),
			getCdVinculo());
	}

}