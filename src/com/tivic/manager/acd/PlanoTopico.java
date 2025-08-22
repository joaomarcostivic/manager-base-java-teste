package com.tivic.manager.acd;

public class PlanoTopico {

	private int cdPlano;
	private int cdSecao;
	private int cdTopico;
	private int cdTopicoSuperior;
	private String txtTopico;
	private int nrOrdem;
	private String nmTitulo;

	public PlanoTopico(){ }

	public PlanoTopico(int cdPlano,
			int cdSecao,
			int cdTopico,
			int cdTopicoSuperior,
			String txtTopico,
			int nrOrdem,
			String nmTitulo){
		setCdPlano(cdPlano);
		setCdSecao(cdSecao);
		setCdTopico(cdTopico);
		setCdTopicoSuperior(cdTopicoSuperior);
		setTxtTopico(txtTopico);
		setNrOrdem(nrOrdem);
		setNmTitulo(nmTitulo);
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
	public void setCdTopicoSuperior(int cdTopicoSuperior){
		this.cdTopicoSuperior=cdTopicoSuperior;
	}
	public int getCdTopicoSuperior(){
		return this.cdTopicoSuperior;
	}
	public void setTxtTopico(String txtTopico){
		this.txtTopico=txtTopico;
	}
	public String getTxtTopico(){
		return this.txtTopico;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setNmTitulo(String nmTitulo) {
		this.nmTitulo = nmTitulo;
	}
	public String getNmTitulo() {
		return nmTitulo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlano: " +  getCdPlano();
		valueToString += ", cdSecao: " +  getCdSecao();
		valueToString += ", cdTopico: " +  getCdTopico();
		valueToString += ", cdTopicoSuperior: " +  getCdTopicoSuperior();
		valueToString += ", txtTopico: " +  getTxtTopico();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", nmTitulo: " +  getNmTitulo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoTopico(getCdPlano(),
			getCdSecao(),
			getCdTopico(),
			getCdTopicoSuperior(),
			getTxtTopico(),
			getNrOrdem(),
			getNmTitulo());
	}

}