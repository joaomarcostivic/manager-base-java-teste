package com.tivic.manager.agd;

public class AgendamentoArquivo {

	private int cdAgendamento;
	private int cdArquivo;
	private int cdUsuario;

	public AgendamentoArquivo(int cdAgendamento,
			int cdArquivo,
			int cdUsuario){
		setCdAgendamento(cdAgendamento);
		setCdArquivo(cdArquivo);
		setCdUsuario(cdUsuario);
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAgendamento: " +  getCdAgendamento();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AgendamentoArquivo(getCdAgendamento(),
			getCdArquivo(),
			getCdUsuario());
	}

}
