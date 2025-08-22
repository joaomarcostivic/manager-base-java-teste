package com.tivic.manager.acd;

public class AulaTopico {

	private int cdPlano;
	private int cdSecao;
	private int cdTopico;
	private int cdAula;
	private int lgExecutado;
	private String txtObservacao;

	public AulaTopico() { }

	public AulaTopico(int cdPlano,
			int cdSecao,
			int cdTopico,
			int cdAula,
			int lgExecutado,
			String txtObservacao) {
		setCdPlano(cdPlano);
		setCdSecao(cdSecao);
		setCdTopico(cdTopico);
		setCdAula(cdAula);
		setLgExecutado(lgExecutado);
		setTxtObservacao(txtObservacao);
	}
	public void setCdPlano(int cdPlano){
		this.cdPlano=cdPlano;
	}
	public int getCdPlano(){
		return this.cdPlano;
	}
	public void setCdSecao(int cdSecao){
		this.cdSecao=cdSecao;
	}
	public int getCdSecao(){
		return this.cdSecao;
	}
	public void setCdTopico(int cdTopico){
		this.cdTopico=cdTopico;
	}
	public int getCdTopico(){
		return this.cdTopico;
	}
	public void setCdAula(int cdAula){
		this.cdAula=cdAula;
	}
	public int getCdAula(){
		return this.cdAula;
	}
	public void setLgExecutado(int lgExecutado){
		this.lgExecutado=lgExecutado;
	}
	public int getLgExecutado(){
		return this.lgExecutado;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlano: " +  getCdPlano();
		valueToString += ", cdSecao: " +  getCdSecao();
		valueToString += ", cdTopico: " +  getCdTopico();
		valueToString += ", cdAula: " +  getCdAula();
		valueToString += ", lgExecutado: " +  getLgExecutado();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AulaTopico(getCdPlano(),
			getCdSecao(),
			getCdTopico(),
			getCdAula(),
			getLgExecutado(),
			getTxtObservacao());
	}

}