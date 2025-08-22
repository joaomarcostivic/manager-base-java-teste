package com.tivic.manager.acd;

public class DisciplinaAvaliacao {

	private int cdDisciplinaAvaliacao;
	private int cdCurso;
	private int cdCursoModulo;
	private int cdDisciplina;
	private int cdMatriz;
	private int cdUnidade;
	private int cdTipoAvaliacao;
	private String nmAvaliacao;
	private String txtObservacao;
	private float vlPeso;
	private String idAvaliacao;
	private int cdFormulario;

	public DisciplinaAvaliacao(){ }

	public DisciplinaAvaliacao(int cdDisciplinaAvaliacao,
			int cdCurso,
			int cdCursoModulo,
			int cdDisciplina,
			int cdMatriz,
			int cdUnidade,
			int cdTipoAvaliacao,
			String nmAvaliacao,
			String txtObservacao,
			float vlPeso,
			String idAvaliacao,
			int cdFormulario){
		setCdDisciplinaAvaliacao(cdDisciplinaAvaliacao);
		setCdCurso(cdCurso);
		setCdCursoModulo(cdCursoModulo);
		setCdDisciplina(cdDisciplina);
		setCdMatriz(cdMatriz);
		setCdUnidade(cdUnidade);
		setCdTipoAvaliacao(cdTipoAvaliacao);
		setNmAvaliacao(nmAvaliacao);
		setTxtObservacao(txtObservacao);
		setVlPeso(vlPeso);
		setIdAvaliacao(idAvaliacao);
		setCdFormulario(cdFormulario);
	}
	public void setCdDisciplinaAvaliacao(int cdDisciplinaAvaliacao){
		this.cdDisciplinaAvaliacao=cdDisciplinaAvaliacao;
	}
	public int getCdDisciplinaAvaliacao(){
		return this.cdDisciplinaAvaliacao;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdCursoModulo(int cdCursoModulo){
		this.cdCursoModulo=cdCursoModulo;
	}
	public int getCdCursoModulo(){
		return this.cdCursoModulo;
	}
	public void setCdDisciplina(int cdDisciplina){
		this.cdDisciplina=cdDisciplina;
	}
	public int getCdDisciplina(){
		return this.cdDisciplina;
	}
	public void setCdMatriz(int cdMatriz){
		this.cdMatriz=cdMatriz;
	}
	public int getCdMatriz(){
		return this.cdMatriz;
	}
	public void setCdUnidade(int cdUnidade){
		this.cdUnidade=cdUnidade;
	}
	public int getCdUnidade(){
		return this.cdUnidade;
	}
	public void setCdTipoAvaliacao(int cdTipoAvaliacao){
		this.cdTipoAvaliacao=cdTipoAvaliacao;
	}
	public int getCdTipoAvaliacao(){
		return this.cdTipoAvaliacao;
	}
	public void setNmAvaliacao(String nmAvaliacao){
		this.nmAvaliacao=nmAvaliacao;
	}
	public String getNmAvaliacao(){
		return this.nmAvaliacao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setVlPeso(float vlPeso){
		this.vlPeso=vlPeso;
	}
	public float getVlPeso(){
		return this.vlPeso;
	}
	public void setIdAvaliacao(String idAvaliacao){
		this.idAvaliacao=idAvaliacao;
	}
	public String getIdAvaliacao(){
		return this.idAvaliacao;
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDisciplinaAvaliacao: " +  getCdDisciplinaAvaliacao();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdCursoModulo: " +  getCdCursoModulo();
		valueToString += ", cdDisciplina: " +  getCdDisciplina();
		valueToString += ", cdMatriz: " +  getCdMatriz();
		valueToString += ", cdUnidade: " +  getCdUnidade();
		valueToString += ", cdTipoAvaliacao: " +  getCdTipoAvaliacao();
		valueToString += ", nmAvaliacao: " +  getNmAvaliacao();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", vlPeso: " +  getVlPeso();
		valueToString += ", idAvaliacao: " +  getIdAvaliacao();
		valueToString += ", cdFormulario: " +  getCdFormulario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DisciplinaAvaliacao(getCdDisciplinaAvaliacao(),
			getCdCurso(),
			getCdCursoModulo(),
			getCdDisciplina(),
			getCdMatriz(),
			getCdUnidade(),
			getCdTipoAvaliacao(),
			getNmAvaliacao(),
			getTxtObservacao(),
			getVlPeso(),
			getIdAvaliacao(),
			getCdFormulario());
	}

}