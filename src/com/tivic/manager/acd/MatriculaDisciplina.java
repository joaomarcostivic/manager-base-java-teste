package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class MatriculaDisciplina {

	private int cdMatriculaDisciplina;
	private int cdMatricula;
	private int cdPeriodoLetivo;
	private int cdConceito;
	private GregorianCalendar dtInicio;
	private GregorianCalendar dtConclusao;
	private int nrFaltas;
	private int tpMatricula;
	private float vlConceito;
	private int qtChComplemento;
	private float vlConceitoAproveitamento;
	private String nmInstitiuicaoAproveitamento;
	private int stMatriculaDisciplina;
	private int cdProfessor;
	private int cdSupervisorPratica;
	private int cdInstituicaoPratica;
	private int cdMatriz;
	private int cdCurso;
	private int cdCursoModulo;
	private int cdDisciplina;
	private int cdOferta;
	private int lgAprovado;

	public MatriculaDisciplina(){ }

	public MatriculaDisciplina(int cdMatriculaDisciplina,
			int cdMatricula,
			int cdPeriodoLetivo,
			int cdConceito,
			GregorianCalendar dtInicio,
			GregorianCalendar dtConclusao,
			int nrFaltas,
			int tpMatricula,
			float vlConceito,
			int qtChComplemento,
			float vlConceitoAproveitamento,
			String nmInstitiuicaoAproveitamento,
			int stMatriculaDisciplina,
			int cdProfessor,
			int cdSupervisorPratica,
			int cdInstituicaoPratica,
			int cdMatriz,
			int cdCurso,
			int cdCursoModulo,
			int cdDisciplina,
			int cdOferta,
			int lgAprovado){
		setCdMatriculaDisciplina(cdMatriculaDisciplina);
		setCdMatricula(cdMatricula);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setCdConceito(cdConceito);
		setDtInicio(dtInicio);
		setDtConclusao(dtConclusao);
		setNrFaltas(nrFaltas);
		setTpMatricula(tpMatricula);
		setVlConceito(vlConceito);
		setQtChComplemento(qtChComplemento);
		setVlConceitoAproveitamento(vlConceitoAproveitamento);
		setNmInstitiuicaoAproveitamento(nmInstitiuicaoAproveitamento);
		setStMatriculaDisciplina(stMatriculaDisciplina);
		setCdProfessor(cdProfessor);
		setCdSupervisorPratica(cdSupervisorPratica);
		setCdInstituicaoPratica(cdInstituicaoPratica);
		setCdMatriz(cdMatriz);
		setCdCurso(cdCurso);
		setCdCursoModulo(cdCursoModulo);
		setCdDisciplina(cdDisciplina);
		setCdOferta(cdOferta);
		setLgAprovado(lgAprovado);
	}
	public void setCdMatriculaDisciplina(int cdMatriculaDisciplina){
		this.cdMatriculaDisciplina=cdMatriculaDisciplina;
	}
	public int getCdMatriculaDisciplina(){
		return this.cdMatriculaDisciplina;
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setCdConceito(int cdConceito){
		this.cdConceito=cdConceito;
	}
	public int getCdConceito(){
		return this.cdConceito;
	}
	public void setDtInicio(GregorianCalendar dtInicio){
		this.dtInicio=dtInicio;
	}
	public GregorianCalendar getDtInicio(){
		return this.dtInicio;
	}
	public void setDtConclusao(GregorianCalendar dtConclusao){
		this.dtConclusao=dtConclusao;
	}
	public GregorianCalendar getDtConclusao(){
		return this.dtConclusao;
	}
	public void setNrFaltas(int nrFaltas){
		this.nrFaltas=nrFaltas;
	}
	public int getNrFaltas(){
		return this.nrFaltas;
	}
	public void setTpMatricula(int tpMatricula){
		this.tpMatricula=tpMatricula;
	}
	public int getTpMatricula(){
		return this.tpMatricula;
	}
	public void setVlConceito(float vlConceito){
		this.vlConceito=vlConceito;
	}
	public float getVlConceito(){
		return this.vlConceito;
	}
	public void setQtChComplemento(int qtChComplemento){
		this.qtChComplemento=qtChComplemento;
	}
	public int getQtChComplemento(){
		return this.qtChComplemento;
	}
	public void setVlConceitoAproveitamento(float vlConceitoAproveitamento){
		this.vlConceitoAproveitamento=vlConceitoAproveitamento;
	}
	public float getVlConceitoAproveitamento(){
		return this.vlConceitoAproveitamento;
	}
	public void setNmInstitiuicaoAproveitamento(String nmInstitiuicaoAproveitamento){
		this.nmInstitiuicaoAproveitamento=nmInstitiuicaoAproveitamento;
	}
	public String getNmInstitiuicaoAproveitamento(){
		return this.nmInstitiuicaoAproveitamento;
	}
	public void setStMatriculaDisciplina(int stMatriculaDisciplina){
		this.stMatriculaDisciplina=stMatriculaDisciplina;
	}
	public int getStMatriculaDisciplina(){
		return this.stMatriculaDisciplina;
	}
	public void setCdProfessor(int cdProfessor){
		this.cdProfessor=cdProfessor;
	}
	public int getCdProfessor(){
		return this.cdProfessor;
	}
	public void setCdSupervisorPratica(int cdSupervisorPratica){
		this.cdSupervisorPratica=cdSupervisorPratica;
	}
	public int getCdSupervisorPratica(){
		return this.cdSupervisorPratica;
	}
	public void setCdInstituicaoPratica(int cdInstituicaoPratica){
		this.cdInstituicaoPratica=cdInstituicaoPratica;
	}
	public int getCdInstituicaoPratica(){
		return this.cdInstituicaoPratica;
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
	public void setCdOferta(int cdOferta){
		this.cdOferta=cdOferta;
	}
	public int getCdOferta(){
		return this.cdOferta;
	}
	public void setLgAprovado(int lgAprovado){
		this.lgAprovado=lgAprovado;
	}
	public int getLgAprovado(){
		return this.lgAprovado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatriculaDisciplina: " +  getCdMatriculaDisciplina();
		valueToString += ", cdMatricula: " +  getCdMatricula();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		valueToString += ", cdConceito: " +  getCdConceito();
		valueToString += ", dtInicio: " +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtConclusao: " +  sol.util.Util.formatDateTime(getDtConclusao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrFaltas: " +  getNrFaltas();
		valueToString += ", tpMatricula: " +  getTpMatricula();
		valueToString += ", vlConceito: " +  getVlConceito();
		valueToString += ", qtChComplemento: " +  getQtChComplemento();
		valueToString += ", vlConceitoAproveitamento: " +  getVlConceitoAproveitamento();
		valueToString += ", nmInstitiuicaoAproveitamento: " +  getNmInstitiuicaoAproveitamento();
		valueToString += ", stMatriculaDisciplina: " +  getStMatriculaDisciplina();
		valueToString += ", cdProfessor: " +  getCdProfessor();
		valueToString += ", cdSupervisorPratica: " +  getCdSupervisorPratica();
		valueToString += ", cdInstituicaoPratica: " +  getCdInstituicaoPratica();
		valueToString += ", cdMatriz: " +  getCdMatriz();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdCursoModulo: " +  getCdCursoModulo();
		valueToString += ", cdDisciplina: " +  getCdDisciplina();
		valueToString += ", cdOferta: " +  getCdOferta();
		valueToString += ", lgAprovado: " +  getLgAprovado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MatriculaDisciplina(getCdMatriculaDisciplina(),
			getCdMatricula(),
			getCdPeriodoLetivo(),
			getCdConceito(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone(),
			getDtConclusao()==null ? null : (GregorianCalendar)getDtConclusao().clone(),
			getNrFaltas(),
			getTpMatricula(),
			getVlConceito(),
			getQtChComplemento(),
			getVlConceitoAproveitamento(),
			getNmInstitiuicaoAproveitamento(),
			getStMatriculaDisciplina(),
			getCdProfessor(),
			getCdSupervisorPratica(),
			getCdInstituicaoPratica(),
			getCdMatriz(),
			getCdCurso(),
			getCdCursoModulo(),
			getCdDisciplina(),
			getCdOferta(),
			getLgAprovado());
	}

}