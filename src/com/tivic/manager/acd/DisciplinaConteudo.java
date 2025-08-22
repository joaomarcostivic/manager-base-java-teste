package com.tivic.manager.acd;

public class DisciplinaConteudo {

	private int cdConteudo;
	private int cdConteudoSuperior;
	private String txtConteudo;
	private int tpConteudo;
	private int cdMatriz;
	private int cdCurso;
	private int cdCursoPeriodo;
	private int cdDisciplina;

	public DisciplinaConteudo(){ }

	public DisciplinaConteudo(int cdConteudo,
			int cdConteudoSuperior,
			String txtConteudo,
			int tpConteudo,
			int cdMatriz,
			int cdCurso,
			int cdCursoPeriodo,
			int cdDisciplina){
		setCdConteudo(cdConteudo);
		setCdConteudoSuperior(cdConteudoSuperior);
		setTxtConteudo(txtConteudo);
		setTpConteudo(tpConteudo);
		setCdMatriz(cdMatriz);
		setCdCurso(cdCurso);
		setCdCursoPeriodo(cdCursoPeriodo);
		setCdDisciplina(cdDisciplina);
	}
	public void setCdConteudo(int cdConteudo){
		this.cdConteudo=cdConteudo;
	}
	public int getCdConteudo(){
		return this.cdConteudo;
	}
	public void setCdConteudoSuperior(int cdConteudoSuperior){
		this.cdConteudoSuperior=cdConteudoSuperior;
	}
	public int getCdConteudoSuperior(){
		return this.cdConteudoSuperior;
	}
	public void setTxtConteudo(String txtConteudo){
		this.txtConteudo=txtConteudo;
	}
	public String getTxtConteudo(){
		return this.txtConteudo;
	}
	public void setTpConteudo(int tpConteudo){
		this.tpConteudo=tpConteudo;
	}
	public int getTpConteudo(){
		return this.tpConteudo;
	}
	public void setCdMatriz(int cdMatriz){
		this.cdMatriz=cdMatriz;
	}
	public int getCdMatriz(){
		return this.cdMatriz;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdCursoPeriodo(int cdCursoPeriodo){
		this.cdCursoPeriodo=cdCursoPeriodo;
	}
	public int getCdCursoPeriodo(){
		return this.cdCursoPeriodo;
	}
	public void setCdDisciplina(int cdDisciplina){
		this.cdDisciplina=cdDisciplina;
	}
	public int getCdDisciplina(){
		return this.cdDisciplina;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConteudo: " +  getCdConteudo();
		valueToString += ", cdConteudoSuperior: " +  getCdConteudoSuperior();
		valueToString += ", txtConteudo: " +  getTxtConteudo();
		valueToString += ", tpConteudo: " +  getTpConteudo();
		valueToString += ", cdMatriz: " +  getCdMatriz();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdCursoPeriodo: " +  getCdCursoPeriodo();
		valueToString += ", cdDisciplina: " +  getCdDisciplina();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DisciplinaConteudo(getCdConteudo(),
			getCdConteudoSuperior(),
			getTxtConteudo(),
			getTpConteudo(),
			getCdMatriz(),
			getCdCurso(),
			getCdCursoPeriodo(),
			getCdDisciplina());
	}

}