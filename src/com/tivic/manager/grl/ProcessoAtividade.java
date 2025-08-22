package com.tivic.manager.grl;

public class ProcessoAtividade {

	private int cdAtividade;
	private int cdProcesso;
	private int cdAtividadeRequisito;
	private String nmAtividade;
	private String txtAtividade;
	private int cdSetor;
	private int tpUnidadeTempo;
	private int qtDuracaoMaxima;
	private int lgObrigatorio;

	public ProcessoAtividade(int cdAtividade,
			int cdProcesso,
			int cdAtividadeRequisito,
			String nmAtividade,
			String txtAtividade,
			int cdSetor,
			int tpUnidadeTempo,
			int qtDuracaoMaxima,
			int lgObrigatorio){
		setCdAtividade(cdAtividade);
		setCdProcesso(cdProcesso);
		setCdAtividadeRequisito(cdAtividadeRequisito);
		setNmAtividade(nmAtividade);
		setTxtAtividade(txtAtividade);
		setCdSetor(cdSetor);
		setTpUnidadeTempo(tpUnidadeTempo);
		setQtDuracaoMaxima(qtDuracaoMaxima);
		setLgObrigatorio(lgObrigatorio);
	}
	public void setCdAtividade(int cdAtividade){
		this.cdAtividade=cdAtividade;
	}
	public int getCdAtividade(){
		return this.cdAtividade;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdAtividadeRequisito(int cdAtividadeRequisito){
		this.cdAtividadeRequisito=cdAtividadeRequisito;
	}
	public int getCdAtividadeRequisito(){
		return this.cdAtividadeRequisito;
	}
	public void setNmAtividade(String nmAtividade){
		this.nmAtividade=nmAtividade;
	}
	public String getNmAtividade(){
		return this.nmAtividade;
	}
	public void setTxtAtividade(String txtAtividade){
		this.txtAtividade=txtAtividade;
	}
	public String getTxtAtividade(){
		return this.txtAtividade;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setTpUnidadeTempo(int tpUnidadeTempo){
		this.tpUnidadeTempo=tpUnidadeTempo;
	}
	public int getTpUnidadeTempo(){
		return this.tpUnidadeTempo;
	}
	public void setQtDuracaoMaxima(int qtDuracaoMaxima){
		this.qtDuracaoMaxima=qtDuracaoMaxima;
	}
	public int getQtDuracaoMaxima(){
		return this.qtDuracaoMaxima;
	}
	public void setLgObrigatorio(int lgObrigatorio){
		this.lgObrigatorio=lgObrigatorio;
	}
	public int getLgObrigatorio(){
		return this.lgObrigatorio;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAtividade: " +  getCdAtividade();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", cdAtividadeRequisito: " +  getCdAtividadeRequisito();
		valueToString += ", nmAtividade: " +  getNmAtividade();
		valueToString += ", txtAtividade: " +  getTxtAtividade();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", tpUnidadeTempo: " +  getTpUnidadeTempo();
		valueToString += ", qtDuracaoMaxima: " +  getQtDuracaoMaxima();
		valueToString += ", lgObrigatorio: " +  getLgObrigatorio();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProcessoAtividade(getCdAtividade(),
			getCdProcesso(),
			getCdAtividadeRequisito(),
			getNmAtividade(),
			getTxtAtividade(),
			getCdSetor(),
			getTpUnidadeTempo(),
			getQtDuracaoMaxima(),
			getLgObrigatorio());
	}

}
