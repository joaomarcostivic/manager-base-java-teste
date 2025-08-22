package com.tivic.manager.agd;

public class AgendamentoUsuario {

	private int cdUsuario;
	private int cdAgendamento;
	private int tpNivelUsuario;

	public AgendamentoUsuario(int cdUsuario,
			int cdAgendamento,
			int tpNivelUsuario){
		setCdUsuario(cdUsuario);
		setCdAgendamento(cdAgendamento);
		setTpNivelUsuario(tpNivelUsuario);
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public void setTpNivelUsuario(int tpNivelUsuario){
		this.tpNivelUsuario=tpNivelUsuario;
	}
	public int getTpNivelUsuario(){
		return this.tpNivelUsuario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdUsuario: " +  getCdUsuario();
		valueToString += ", cdAgendamento: " +  getCdAgendamento();
		valueToString += ", tpNivelUsuario: " +  getTpNivelUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AgendamentoUsuario(getCdUsuario(),
			getCdAgendamento(),
			getTpNivelUsuario());
	}

}
