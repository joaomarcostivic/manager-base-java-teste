package com.tivic.manager.agd;

public class AssuntoPosicionamento {

	private int cdAgendamento;
	private int cdAssunto;
	private int cdPosicionamento;
	private String txtObservacao;
	private int lgContrario;
	private int cdAgenda;
	private int cdParticipante;
	private int cdConvidado;

	public AssuntoPosicionamento(int cdAgendamento,
			int cdAssunto,
			int cdPosicionamento,
			String txtObservacao,
			int lgContrario,
			int cdAgenda,
			int cdParticipante,
			int cdConvidado){
		setCdAgendamento(cdAgendamento);
		setCdAssunto(cdAssunto);
		setCdPosicionamento(cdPosicionamento);
		setTxtObservacao(txtObservacao);
		setLgContrario(lgContrario);
		setCdAgenda(cdAgenda);
		setCdParticipante(cdParticipante);
		setCdConvidado(cdConvidado);
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public void setCdAssunto(int cdAssunto){
		this.cdAssunto=cdAssunto;
	}
	public int getCdAssunto(){
		return this.cdAssunto;
	}
	public void setCdPosicionamento(int cdPosicionamento){
		this.cdPosicionamento=cdPosicionamento;
	}
	public int getCdPosicionamento(){
		return this.cdPosicionamento;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setLgContrario(int lgContrario){
		this.lgContrario=lgContrario;
	}
	public int getLgContrario(){
		return this.lgContrario;
	}
	public void setCdAgenda(int cdAgenda){
		this.cdAgenda=cdAgenda;
	}
	public int getCdAgenda(){
		return this.cdAgenda;
	}
	public void setCdParticipante(int cdParticipante){
		this.cdParticipante=cdParticipante;
	}
	public int getCdParticipante(){
		return this.cdParticipante;
	}
	public void setCdConvidado(int cdConvidado){
		this.cdConvidado=cdConvidado;
	}
	public int getCdConvidado(){
		return this.cdConvidado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAgendamento: " +  getCdAgendamento();
		valueToString += ", cdAssunto: " +  getCdAssunto();
		valueToString += ", cdPosicionamento: " +  getCdPosicionamento();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", lgContrario: " +  getLgContrario();
		valueToString += ", cdAgenda: " +  getCdAgenda();
		valueToString += ", cdParticipante: " +  getCdParticipante();
		valueToString += ", cdConvidado: " +  getCdConvidado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AssuntoPosicionamento(getCdAgendamento(),
			getCdAssunto(),
			getCdPosicionamento(),
			getTxtObservacao(),
			getLgContrario(),
			getCdAgenda(),
			getCdParticipante(),
			getCdConvidado());
	}

}
