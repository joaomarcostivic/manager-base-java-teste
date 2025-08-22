package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class Matricula {

	private int cdMatricula;
	private int cdMatriz;
	private int cdTurma;
	private int cdPeriodoLetivo;
	private GregorianCalendar dtMatricula;
	private GregorianCalendar dtConclusao;
	private int stMatricula;
	private int tpMatricula;
	private String nrMatricula;
	private int cdAluno;
	private int cdMatriculaOrigem;
	private int cdReserva;
	private int cdAreaInteresse;
	private String txtObservacao;
	private String txtBoletim;
	private int cdCurso;
	private int cdPreMatricula;
	private int tpEscolarizacaoOutroEspaco;
	private int lgTransportePublico;
	private int tpPoderResponsavel;
	private int tpFormaIngresso;
	private String txtDocumentoOficial;
	private GregorianCalendar dtInterrupcao;
	private int lgAutorizacaoRematricula;
	private int lgAtividadeComplementar;
	private int lgReprovacao;
	private int stMatriculaCentaurus;
	private int stAlunoCenso;
	private String nrMatriculaCenso;
	private int stCensoFinal;
	private String nmUltimaEscola;
	private int lgAutorizacao;
	private String nrAutorizacao;
	private int lgMatriculaEmCurso;
	private int lgPermissaoForaIdade;
	
	public Matricula(){ }

	public Matricula(int cdMatricula,
			int cdMatriz,
			int cdTurma,
			int cdPeriodoLetivo,
			GregorianCalendar dtMatricula,
			GregorianCalendar dtConclusao,
			int stMatricula,
			int tpMatricula,
			String nrMatricula,
			int cdAluno,
			int cdMatriculaOrigem,
			int cdReserva,
			int cdAreaInteresse,
			String txtObservacao,
			String txtBoletim,
			int cdCurso,
			int cdPreMatricula,
			int tpEscolarizacaoOutroEspaco,
			int lgTransportePublico,
			int tpPoderResponsavel,
			int tpFormaIngresso,
			String txtDocumentoOficial,
			GregorianCalendar dtInterrupcao,
			int lgAutorizacaoRematricula,
			int lgAtividadeComplementar,
			int lgReprovacao){
		setCdMatricula(cdMatricula);
		setCdMatriz(cdMatriz);
		setCdTurma(cdTurma);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setDtMatricula(dtMatricula);
		setDtConclusao(dtConclusao);
		setStMatricula(stMatricula);
		setTpMatricula(tpMatricula);
		setNrMatricula(nrMatricula);
		setCdAluno(cdAluno);
		setCdMatriculaOrigem(cdMatriculaOrigem);
		setCdReserva(cdReserva);
		setCdAreaInteresse(cdAreaInteresse);
		setTxtObservacao(txtObservacao);
		setTxtBoletim(txtBoletim);
		setCdCurso(cdCurso);
		setCdPreMatricula(cdPreMatricula);
		setTpEscolarizacaoOutroEspaco(tpEscolarizacaoOutroEspaco);
		setLgTransportePublico(lgTransportePublico);
		setTpPoderResponsavel(tpPoderResponsavel);
		setTpFormaIngresso(tpFormaIngresso);
		setTxtDocumentoOficial(txtDocumentoOficial);
		setDtInterrupcao(dtInterrupcao);
		setLgAutorizacaoRematricula(lgAutorizacaoRematricula);
		setLgAtividadeComplementar(lgAtividadeComplementar);
		setLgReprovacao(lgReprovacao);
	}
	
	public Matricula(int cdMatricula,
			int cdMatriz,
			int cdTurma,
			int cdPeriodoLetivo,
			GregorianCalendar dtMatricula,
			GregorianCalendar dtConclusao,
			int stMatricula,
			int tpMatricula,
			String nrMatricula,
			int cdAluno,
			int cdMatriculaOrigem,
			int cdReserva,
			int cdAreaInteresse,
			String txtObservacao,
			String txtBoletim,
			int cdCurso,
			int cdPreMatricula,
			int tpEscolarizacaoOutroEspaco,
			int lgTransportePublico,
			int tpPoderResponsavel,
			int tpFormaIngresso,
			String txtDocumentoOficial,
			GregorianCalendar dtInterrupcao,
			int lgAutorizacaoRematricula,
			int lgAtividadeComplementar,
			int lgReprovacao,
			int stMatriculaCentaurus){
		setCdMatricula(cdMatricula);
		setCdMatriz(cdMatriz);
		setCdTurma(cdTurma);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setDtMatricula(dtMatricula);
		setDtConclusao(dtConclusao);
		setStMatricula(stMatricula);
		setTpMatricula(tpMatricula);
		setNrMatricula(nrMatricula);
		setCdAluno(cdAluno);
		setCdMatriculaOrigem(cdMatriculaOrigem);
		setCdReserva(cdReserva);
		setCdAreaInteresse(cdAreaInteresse);
		setTxtObservacao(txtObservacao);
		setTxtBoletim(txtBoletim);
		setCdCurso(cdCurso);
		setCdPreMatricula(cdPreMatricula);
		setTpEscolarizacaoOutroEspaco(tpEscolarizacaoOutroEspaco);
		setLgTransportePublico(lgTransportePublico);
		setTpPoderResponsavel(tpPoderResponsavel);
		setTpFormaIngresso(tpFormaIngresso);
		setTxtDocumentoOficial(txtDocumentoOficial);
		setDtInterrupcao(dtInterrupcao);
		setLgAutorizacaoRematricula(lgAutorizacaoRematricula);
		setLgAtividadeComplementar(lgAtividadeComplementar);
		setLgReprovacao(lgReprovacao);
		setStMatriculaCentaurus(stMatriculaCentaurus);
	}
	public Matricula(int cdMatricula,
			int cdMatriz,
			int cdTurma,
			int cdPeriodoLetivo,
			GregorianCalendar dtMatricula,
			GregorianCalendar dtConclusao,
			int stMatricula,
			int tpMatricula,
			String nrMatricula,
			int cdAluno,
			int cdMatriculaOrigem,
			int cdReserva,
			int cdAreaInteresse,
			String txtObservacao,
			String txtBoletim,
			int cdCurso,
			int cdPreMatricula,
			int tpEscolarizacaoOutroEspaco,
			int lgTransportePublico,
			int tpPoderResponsavel,
			int tpFormaIngresso,
			String txtDocumentoOficial,
			GregorianCalendar dtInterrupcao,
			int lgAutorizacaoRematricula,
			int lgAtividadeComplementar,
			int lgReprovacao,
			int stMatriculaCentaurus,
			int stAlunoCenso){
		setCdMatricula(cdMatricula);
		setCdMatriz(cdMatriz);
		setCdTurma(cdTurma);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setDtMatricula(dtMatricula);
		setDtConclusao(dtConclusao);
		setStMatricula(stMatricula);
		setTpMatricula(tpMatricula);
		setNrMatricula(nrMatricula);
		setCdAluno(cdAluno);
		setCdMatriculaOrigem(cdMatriculaOrigem);
		setCdReserva(cdReserva);
		setCdAreaInteresse(cdAreaInteresse);
		setTxtObservacao(txtObservacao);
		setTxtBoletim(txtBoletim);
		setCdCurso(cdCurso);
		setCdPreMatricula(cdPreMatricula);
		setTpEscolarizacaoOutroEspaco(tpEscolarizacaoOutroEspaco);
		setLgTransportePublico(lgTransportePublico);
		setTpPoderResponsavel(tpPoderResponsavel);
		setTpFormaIngresso(tpFormaIngresso);
		setTxtDocumentoOficial(txtDocumentoOficial);
		setDtInterrupcao(dtInterrupcao);
		setLgAutorizacaoRematricula(lgAutorizacaoRematricula);
		setLgAtividadeComplementar(lgAtividadeComplementar);
		setLgReprovacao(lgReprovacao);
		setStMatriculaCentaurus(stMatriculaCentaurus);
		setStAlunoCenso(stAlunoCenso);
	}
	
	public Matricula(int cdMatricula,
			int cdMatriz,
			int cdTurma,
			int cdPeriodoLetivo,
			GregorianCalendar dtMatricula,
			GregorianCalendar dtConclusao,
			int stMatricula,
			int tpMatricula,
			String nrMatricula,
			int cdAluno,
			int cdMatriculaOrigem,
			int cdReserva,
			int cdAreaInteresse,
			String txtObservacao,
			String txtBoletim,
			int cdCurso,
			int cdPreMatricula,
			int tpEscolarizacaoOutroEspaco,
			int lgTransportePublico,
			int tpPoderResponsavel,
			int tpFormaIngresso,
			String txtDocumentoOficial,
			GregorianCalendar dtInterrupcao,
			int lgAutorizacaoRematricula,
			int lgAtividadeComplementar,
			int lgReprovacao,
			int stMatriculaCentaurus,
			int stAlunoCenso,
			String nrMatriculaCenso){
		setCdMatricula(cdMatricula);
		setCdMatriz(cdMatriz);
		setCdTurma(cdTurma);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setDtMatricula(dtMatricula);
		setDtConclusao(dtConclusao);
		setStMatricula(stMatricula);
		setTpMatricula(tpMatricula);
		setNrMatricula(nrMatricula);
		setCdAluno(cdAluno);
		setCdMatriculaOrigem(cdMatriculaOrigem);
		setCdReserva(cdReserva);
		setCdAreaInteresse(cdAreaInteresse);
		setTxtObservacao(txtObservacao);
		setTxtBoletim(txtBoletim);
		setCdCurso(cdCurso);
		setCdPreMatricula(cdPreMatricula);
		setTpEscolarizacaoOutroEspaco(tpEscolarizacaoOutroEspaco);
		setLgTransportePublico(lgTransportePublico);
		setTpPoderResponsavel(tpPoderResponsavel);
		setTpFormaIngresso(tpFormaIngresso);
		setTxtDocumentoOficial(txtDocumentoOficial);
		setDtInterrupcao(dtInterrupcao);
		setLgAutorizacaoRematricula(lgAutorizacaoRematricula);
		setLgAtividadeComplementar(lgAtividadeComplementar);
		setLgReprovacao(lgReprovacao);
		setStMatriculaCentaurus(stMatriculaCentaurus);
		setStAlunoCenso(stAlunoCenso);
		setNrMatriculaCenso(nrMatriculaCenso);
	}
	
	public Matricula(int cdMatricula,
			int cdMatriz,
			int cdTurma,
			int cdPeriodoLetivo,
			GregorianCalendar dtMatricula,
			GregorianCalendar dtConclusao,
			int stMatricula,
			int tpMatricula,
			String nrMatricula,
			int cdAluno,
			int cdMatriculaOrigem,
			int cdReserva,
			int cdAreaInteresse,
			String txtObservacao,
			String txtBoletim,
			int cdCurso,
			int cdPreMatricula,
			int tpEscolarizacaoOutroEspaco,
			int lgTransportePublico,
			int tpPoderResponsavel,
			int tpFormaIngresso,
			String txtDocumentoOficial,
			GregorianCalendar dtInterrupcao,
			int lgAutorizacaoRematricula,
			int lgAtividadeComplementar,
			int lgReprovacao,
			int stMatriculaCentaurus,
			int stAlunoCenso,
			String nrMatriculaCenso,
			int stCensoFinal){
		setCdMatricula(cdMatricula);
		setCdMatriz(cdMatriz);
		setCdTurma(cdTurma);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setDtMatricula(dtMatricula);
		setDtConclusao(dtConclusao);
		setStMatricula(stMatricula);
		setTpMatricula(tpMatricula);
		setNrMatricula(nrMatricula);
		setCdAluno(cdAluno);
		setCdMatriculaOrigem(cdMatriculaOrigem);
		setCdReserva(cdReserva);
		setCdAreaInteresse(cdAreaInteresse);
		setTxtObservacao(txtObservacao);
		setTxtBoletim(txtBoletim);
		setCdCurso(cdCurso);
		setCdPreMatricula(cdPreMatricula);
		setTpEscolarizacaoOutroEspaco(tpEscolarizacaoOutroEspaco);
		setLgTransportePublico(lgTransportePublico);
		setTpPoderResponsavel(tpPoderResponsavel);
		setTpFormaIngresso(tpFormaIngresso);
		setTxtDocumentoOficial(txtDocumentoOficial);
		setDtInterrupcao(dtInterrupcao);
		setLgAutorizacaoRematricula(lgAutorizacaoRematricula);
		setLgAtividadeComplementar(lgAtividadeComplementar);
		setLgReprovacao(lgReprovacao);
		setStMatriculaCentaurus(stMatriculaCentaurus);
		setStAlunoCenso(stAlunoCenso);
		setNrMatriculaCenso(nrMatriculaCenso);
		setStCensoFinal(stCensoFinal);
	}
	
	public Matricula(int cdMatricula,
			int cdMatriz,
			int cdTurma,
			int cdPeriodoLetivo,
			GregorianCalendar dtMatricula,
			GregorianCalendar dtConclusao,
			int stMatricula,
			int tpMatricula,
			String nrMatricula,
			int cdAluno,
			int cdMatriculaOrigem,
			int cdReserva,
			int cdAreaInteresse,
			String txtObservacao,
			String txtBoletim,
			int cdCurso,
			int cdPreMatricula,
			int tpEscolarizacaoOutroEspaco,
			int lgTransportePublico,
			int tpPoderResponsavel,
			int tpFormaIngresso,
			String txtDocumentoOficial,
			GregorianCalendar dtInterrupcao,
			int lgAutorizacaoRematricula,
			int lgAtividadeComplementar,
			int lgReprovacao,
			int stMatriculaCentaurus,
			int stAlunoCenso,
			String nrMatriculaCenso,
			int stCensoFinal,
			String nmUltimaEscola){
		setCdMatricula(cdMatricula);
		setCdMatriz(cdMatriz);
		setCdTurma(cdTurma);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setDtMatricula(dtMatricula);
		setDtConclusao(dtConclusao);
		setStMatricula(stMatricula);
		setTpMatricula(tpMatricula);
		setNrMatricula(nrMatricula);
		setCdAluno(cdAluno);
		setCdMatriculaOrigem(cdMatriculaOrigem);
		setCdReserva(cdReserva);
		setCdAreaInteresse(cdAreaInteresse);
		setTxtObservacao(txtObservacao);
		setTxtBoletim(txtBoletim);
		setCdCurso(cdCurso);
		setCdPreMatricula(cdPreMatricula);
		setTpEscolarizacaoOutroEspaco(tpEscolarizacaoOutroEspaco);
		setLgTransportePublico(lgTransportePublico);
		setTpPoderResponsavel(tpPoderResponsavel);
		setTpFormaIngresso(tpFormaIngresso);
		setTxtDocumentoOficial(txtDocumentoOficial);
		setDtInterrupcao(dtInterrupcao);
		setLgAutorizacaoRematricula(lgAutorizacaoRematricula);
		setLgAtividadeComplementar(lgAtividadeComplementar);
		setLgReprovacao(lgReprovacao);
		setStMatriculaCentaurus(stMatriculaCentaurus);
		setStAlunoCenso(stAlunoCenso);
		setNrMatriculaCenso(nrMatriculaCenso);
		setStCensoFinal(stCensoFinal);
		setNmUltimaEscola(nmUltimaEscola);
	}

	public Matricula(int cdMatricula,
			int cdMatriz,
			int cdTurma,
			int cdPeriodoLetivo,
			GregorianCalendar dtMatricula,
			GregorianCalendar dtConclusao,
			int stMatricula,
			int tpMatricula,
			String nrMatricula,
			int cdAluno,
			int cdMatriculaOrigem,
			int cdReserva,
			int cdAreaInteresse,
			String txtObservacao,
			String txtBoletim,
			int cdCurso,
			int cdPreMatricula,
			int tpEscolarizacaoOutroEspaco,
			int lgTransportePublico,
			int tpPoderResponsavel,
			int tpFormaIngresso,
			String txtDocumentoOficial,
			GregorianCalendar dtInterrupcao,
			int lgAutorizacaoRematricula,
			int lgAtividadeComplementar,
			int lgReprovacao,
			int stMatriculaCentaurus,
			int stAlunoCenso,
			String nrMatriculaCenso,
			int stCensoFinal,
			String nmUltimaEscola,
			int lgAutorizacao,
			String nrAutorizacao,
			int lgMatriculaEmCurso){
		setCdMatricula(cdMatricula);
		setCdMatriz(cdMatriz);
		setCdTurma(cdTurma);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setDtMatricula(dtMatricula);
		setDtConclusao(dtConclusao);
		setStMatricula(stMatricula);
		setTpMatricula(tpMatricula);
		setNrMatricula(nrMatricula);
		setCdAluno(cdAluno);
		setCdMatriculaOrigem(cdMatriculaOrigem);
		setCdReserva(cdReserva);
		setCdAreaInteresse(cdAreaInteresse);
		setTxtObservacao(txtObservacao);
		setTxtBoletim(txtBoletim);
		setCdCurso(cdCurso);
		setCdPreMatricula(cdPreMatricula);
		setTpEscolarizacaoOutroEspaco(tpEscolarizacaoOutroEspaco);
		setLgTransportePublico(lgTransportePublico);
		setTpPoderResponsavel(tpPoderResponsavel);
		setTpFormaIngresso(tpFormaIngresso);
		setTxtDocumentoOficial(txtDocumentoOficial);
		setDtInterrupcao(dtInterrupcao);
		setLgAutorizacaoRematricula(lgAutorizacaoRematricula);
		setLgAtividadeComplementar(lgAtividadeComplementar);
		setLgReprovacao(lgReprovacao);
		setStMatriculaCentaurus(stMatriculaCentaurus);
		setStAlunoCenso(stAlunoCenso);
		setNrMatriculaCenso(nrMatriculaCenso);
		setStCensoFinal(stCensoFinal);
		setNmUltimaEscola(nmUltimaEscola);
		setLgAutorizacao(lgAutorizacao);
		setNrAutorizacao(nrAutorizacao);
		setLgMatriculaEmCurso(lgMatriculaEmCurso);
	}
	public Matricula(int cdMatricula,
			int cdMatriz,
			int cdTurma,
			int cdPeriodoLetivo,
			GregorianCalendar dtMatricula,
			GregorianCalendar dtConclusao,
			int stMatricula,
			int tpMatricula,
			String nrMatricula,
			int cdAluno,
			int cdMatriculaOrigem,
			int cdReserva,
			int cdAreaInteresse,
			String txtObservacao,
			String txtBoletim,
			int cdCurso,
			int cdPreMatricula,
			int tpEscolarizacaoOutroEspaco,
			int lgTransportePublico,
			int tpPoderResponsavel,
			int tpFormaIngresso,
			String txtDocumentoOficial,
			GregorianCalendar dtInterrupcao,
			int lgAutorizacaoRematricula,
			int lgAtividadeComplementar,
			int lgReprovacao,
			int stMatriculaCentaurus,
			int stAlunoCenso,
			String nrMatriculaCenso,
			int stCensoFinal,
			String nmUltimaEscola,
			int lgAutorizacao,
			String nrAutorizacao,
			int lgMatriculaEmCurso,
			int lgPermissaoForaIdade){
		setCdMatricula(cdMatricula);
		setCdMatriz(cdMatriz);
		setCdTurma(cdTurma);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setDtMatricula(dtMatricula);
		setDtConclusao(dtConclusao);
		setStMatricula(stMatricula);
		setTpMatricula(tpMatricula);
		setNrMatricula(nrMatricula);
		setCdAluno(cdAluno);
		setCdMatriculaOrigem(cdMatriculaOrigem);
		setCdReserva(cdReserva);
		setCdAreaInteresse(cdAreaInteresse);
		setTxtObservacao(txtObservacao);
		setTxtBoletim(txtBoletim);
		setCdCurso(cdCurso);
		setCdPreMatricula(cdPreMatricula);
		setTpEscolarizacaoOutroEspaco(tpEscolarizacaoOutroEspaco);
		setLgTransportePublico(lgTransportePublico);
		setTpPoderResponsavel(tpPoderResponsavel);
		setTpFormaIngresso(tpFormaIngresso);
		setTxtDocumentoOficial(txtDocumentoOficial);
		setDtInterrupcao(dtInterrupcao);
		setLgAutorizacaoRematricula(lgAutorizacaoRematricula);
		setLgAtividadeComplementar(lgAtividadeComplementar);
		setLgReprovacao(lgReprovacao);
		setStMatriculaCentaurus(stMatriculaCentaurus);
		setStAlunoCenso(stAlunoCenso);
		setNrMatriculaCenso(nrMatriculaCenso);
		setStCensoFinal(stCensoFinal);
		setNmUltimaEscola(nmUltimaEscola);
		setLgAutorizacao(lgAutorizacao);
		setNrAutorizacao(nrAutorizacao);
		setLgMatriculaEmCurso(lgMatriculaEmCurso);
		setLgPermissaoForaIdade(lgPermissaoForaIdade);
	}
	
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdMatriz(int cdMatriz){
		this.cdMatriz=cdMatriz;
	}
	public int getCdMatriz(){
		return this.cdMatriz;
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setDtMatricula(GregorianCalendar dtMatricula){
		this.dtMatricula=dtMatricula;
	}
	public GregorianCalendar getDtMatricula(){
		return this.dtMatricula;
	}
	public void setDtConclusao(GregorianCalendar dtConclusao){
		this.dtConclusao=dtConclusao;
	}
	public GregorianCalendar getDtConclusao(){
		return this.dtConclusao;
	}
	public void setStMatricula(int stMatricula){
		this.stMatricula=stMatricula;
	}
	public int getStMatricula(){
		return this.stMatricula;
	}
	public void setTpMatricula(int tpMatricula){
		this.tpMatricula=tpMatricula;
	}
	public int getTpMatricula(){
		return this.tpMatricula;
	}
	public void setNrMatricula(String nrMatricula){
		this.nrMatricula=nrMatricula;
	}
	public String getNrMatricula(){
		return this.nrMatricula;
	}
	public void setCdAluno(int cdAluno){
		this.cdAluno=cdAluno;
	}
	public int getCdAluno(){
		return this.cdAluno;
	}
	public void setCdMatriculaOrigem(int cdMatriculaOrigem){
		this.cdMatriculaOrigem=cdMatriculaOrigem;
	}
	public int getCdMatriculaOrigem(){
		return this.cdMatriculaOrigem;
	}
	public void setCdReserva(int cdReserva){
		this.cdReserva=cdReserva;
	}
	public int getCdReserva(){
		return this.cdReserva;
	}
	public void setCdAreaInteresse(int cdAreaInteresse){
		this.cdAreaInteresse=cdAreaInteresse;
	}
	public int getCdAreaInteresse(){
		return this.cdAreaInteresse;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setTxtBoletim(String txtBoletim){
		this.txtBoletim=txtBoletim;
	}
	public String getTxtBoletim(){
		return this.txtBoletim;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdPreMatricula(int cdPreMatricula){
		this.cdPreMatricula=cdPreMatricula;
	}
	public int getCdPreMatricula(){
		return this.cdPreMatricula;
	}
	public void setTpEscolarizacaoOutroEspaco(int tpEscolarizacaoOutroEspaco){
		this.tpEscolarizacaoOutroEspaco=tpEscolarizacaoOutroEspaco;
	}
	public int getTpEscolarizacaoOutroEspaco(){
		return this.tpEscolarizacaoOutroEspaco;
	}
	public void setLgTransportePublico(int lgTransportePublico){
		this.lgTransportePublico=lgTransportePublico;
	}
	public int getLgTransportePublico(){
		return this.lgTransportePublico;
	}
	public void setTpPoderResponsavel(int tpPoderResponsavel){
		this.tpPoderResponsavel=tpPoderResponsavel;
	}
	public int getTpPoderResponsavel(){
		return this.tpPoderResponsavel;
	}
	public void setTpFormaIngresso(int tpFormaIngresso){
		this.tpFormaIngresso=tpFormaIngresso;
	}
	public int getTpFormaIngresso(){
		return this.tpFormaIngresso;
	}
	public void setTxtDocumentoOficial(String txtDocumentoOficial){
		this.txtDocumentoOficial=txtDocumentoOficial;
	}
	public String getTxtDocumentoOficial(){
		return this.txtDocumentoOficial;
	}
	public void setDtInterrupcao(GregorianCalendar dtInterrupcao){
		this.dtInterrupcao=dtInterrupcao;
	}
	public GregorianCalendar getDtInterrupcao(){
		return this.dtInterrupcao;
	}
	public void setLgAutorizacaoRematricula(int lgAutorizacaoRematricula) {
		this.lgAutorizacaoRematricula = lgAutorizacaoRematricula;
	}
	public int getLgAutorizacaoRematricula() {
		return lgAutorizacaoRematricula;
	}
	public void setLgAtividadeComplementar(int lgAtividadeComplementar) {
		this.lgAtividadeComplementar = lgAtividadeComplementar;
	}
	public int getLgAtividadeComplementar() {
		return lgAtividadeComplementar;
	}
	public void setLgReprovacao(int lgReprovacao) {
		this.lgReprovacao = lgReprovacao;
	}
	public int getLgReprovacao() {
		return lgReprovacao;
	}
	public void setStMatriculaCentaurus(int stMatriculaCentaurus) {
		this.stMatriculaCentaurus = stMatriculaCentaurus;
	}
	public int getStMatriculaCentaurus() {
		return stMatriculaCentaurus;
	}
	public void setStAlunoCenso(int stAlunoCenso) {
		this.stAlunoCenso = stAlunoCenso;
	}
	public int getStAlunoCenso() {
		return stAlunoCenso;
	}
	public void setNrMatriculaCenso(String nrMatriculaCenso) {
		this.nrMatriculaCenso = nrMatriculaCenso;
	}
	public String getNrMatriculaCenso() {
		return nrMatriculaCenso;
	}
	public void setStCensoFinal(int stCensoFinal) {
		this.stCensoFinal = stCensoFinal;
	}
	public int getStCensoFinal() {
		return stCensoFinal;
	}
	public void setNmUltimaEscola(String nmUltimaEscola) {
		this.nmUltimaEscola = nmUltimaEscola;
	}
	public String getNmUltimaEscola() {
		return nmUltimaEscola;
	}
	public void setLgAutorizacao(int lgAutorizacao) {
		this.lgAutorizacao = lgAutorizacao;
	}
	public int getLgAutorizacao() {
		return lgAutorizacao;
	}
	public void setNrAutorizacao(String nrAutorizacao) {
		this.nrAutorizacao = nrAutorizacao;
	}
	public String getNrAutorizacao() {
		return nrAutorizacao;
	}
	public void setLgMatriculaEmCurso(int lgMatriculaEmCurso) {
		this.lgMatriculaEmCurso = lgMatriculaEmCurso;
	}
	public int getLgMatriculaEmCurso() {
		return lgMatriculaEmCurso;
	}
	public void setLgPermissaoForaIdade(int lgPermissaoForaIdade) {
		this.lgPermissaoForaIdade = lgPermissaoForaIdade;
	}
	public int getLgPermissaoForaIdade() {
		return lgPermissaoForaIdade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatricula: " +  getCdMatricula();
		valueToString += ", cdMatriz: " +  getCdMatriz();
		valueToString += ", cdTurma: " +  getCdTurma();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		valueToString += ", dtMatricula: " +  sol.util.Util.formatDateTime(getDtMatricula(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtConclusao: " +  sol.util.Util.formatDateTime(getDtConclusao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stMatricula: " +  getStMatricula();
		valueToString += ", tpMatricula: " +  getTpMatricula();
		valueToString += ", nrMatricula: " +  getNrMatricula();
		valueToString += ", cdAluno: " +  getCdAluno();
		valueToString += ", cdMatriculaOrigem: " +  getCdMatriculaOrigem();
		valueToString += ", cdReserva: " +  getCdReserva();
		valueToString += ", cdAreaInteresse: " +  getCdAreaInteresse();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", txtBoletim: " +  getTxtBoletim();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdPreMatricula: " +  getCdPreMatricula();
		valueToString += ", tpEscolarizacaoOutroEspaco: " +  getTpEscolarizacaoOutroEspaco();
		valueToString += ", lgTransportePublico: " +  getLgTransportePublico();
		valueToString += ", tpPoderResponsavel: " +  getTpPoderResponsavel();
		valueToString += ", tpFormaIngresso: " +  getTpFormaIngresso();
		valueToString += ", txtDocumentoOficial: " +  getTxtDocumentoOficial();
		valueToString += ", dtInterrupcao: " +  sol.util.Util.formatDateTime(getDtInterrupcao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpFormaIngresso: " +  getTpFormaIngresso();
		valueToString += ", lgAutorizacaoRematricula: " +  getLgAutorizacaoRematricula();
		valueToString += ", lgAtividadeComplementar: " +  getLgAtividadeComplementar();
		valueToString += ", lgReprovacao: " +  getLgReprovacao();
		valueToString += ", stMatriculaCentaurus: " +  getStMatriculaCentaurus();
		valueToString += ", stAlunoCenso: " +  getStAlunoCenso();
		valueToString += ", nrMatriculaCenso: " +  getNrMatriculaCenso();
		valueToString += ", stCensoFinal: " +  getStCensoFinal();
		valueToString += ", nmUltimaEscola: " +  getNmUltimaEscola();
		valueToString += ", lgAutorizacao: " +  getLgAutorizacao();
		valueToString += ", nrAutorizacao: " +  getNrAutorizacao();
		valueToString += ", lgMatriculaEmCurso: " +  getLgMatriculaEmCurso();
		valueToString += ", lgPermissaoForaIdade: " +  getLgPermissaoForaIdade();
		return "{" + valueToString + "}";
	}
	
	public String toORM() {
		String valueToString = "";
		valueToString += "\"cdMatricula\": " +  getCdMatricula();
		valueToString += ", \"cdMatriz\": " +  getCdMatriz();
		valueToString += ", \"cdTurma\": " +  getCdTurma();
		valueToString += ", \"cdPeriodoLetivo\": " +  getCdPeriodoLetivo();
		valueToString += ", \"dtMatricula\": \"" +  sol.util.Util.formatDateTime(getDtMatricula(), "yyyy-MM-dd", "")+"\"";
		valueToString += ", \"dtConclusao\": \"" +  sol.util.Util.formatDateTime(getDtConclusao(), "yyyy-MM-dd", "")+"\"";
		valueToString += ", \"stMatricula\": " +  getStMatricula();
		valueToString += ", \"tpMatricula\": " +  getTpMatricula();
		valueToString += ", \"nrMatricula\": \"" +  getNrMatricula()+"\"";
		valueToString += ", \"cdAluno\": " +  getCdAluno();
		valueToString += ", \"cdMatriculaOrigem\": " +  getCdMatriculaOrigem();
		valueToString += ", \"cdReserva\": " +  getCdReserva();
		valueToString += ", \"cdAreaInteresse\": " +  getCdAreaInteresse();
		valueToString += ", \"txtObservacao\": \"" +  getTxtObservacao()+"\"";
		valueToString += ", \"txtBoletim\": \"" +  getTxtBoletim()+"\"";
		valueToString += ", \"cdCurso\": " +  getCdCurso();
		valueToString += ", \"cdPreMatricula\": " +  getCdPreMatricula();
		valueToString += ", \"tpEscolarizacaoOutroEspaco\": " +  getTpEscolarizacaoOutroEspaco();
		valueToString += ", \"lgTransportePublico\": " +  getLgTransportePublico();
		valueToString += ", \"tpPoderResponsavel\": " +  getTpPoderResponsavel();
		valueToString += ", \"tpFormaIngresso\": " +  getTpFormaIngresso();
		valueToString += ", \"txtDocumentoOficial\": \"" +  getTxtDocumentoOficial()+"\"";
		valueToString += ", \"dtInterrupcao\": \"" +  sol.util.Util.formatDateTime(getDtInterrupcao(), "yyyy-MM-dd", "")+"\"";
		valueToString += ", \"tpFormaIngresso\": " +  getTpFormaIngresso();
		valueToString += ", \"lgAutorizacaoRematricula\": " +  getLgAutorizacaoRematricula();
		valueToString += ", \"lgAtividadeComplementar\": " +  getLgAtividadeComplementar();
		valueToString += ", \"lgReprovacao\": " +  getLgReprovacao();
		valueToString += ", \"stMatriculaCentaurus\": " +  getStMatriculaCentaurus();
		valueToString += ", \"stAlunoCenso\": " +  getStAlunoCenso();
		valueToString += ", \"nrMatriculaCenso\": \"" +  getNrMatriculaCenso()+"\"";
		valueToString += ", \"stCensoFinal\": " +  getStCensoFinal();
		valueToString += ", \"nmUltimaEscola\": \"" +  getNmUltimaEscola()+"\"";
		valueToString += ", \"lgAutorizacao\": " +  getLgAutorizacao();
		valueToString += ", \"nrAutorizacao\": \"" +  getNrAutorizacao()+"\"";
		valueToString += ", \"lgMatriculaEmCurso\": " +  getLgMatriculaEmCurso();
		valueToString += ", \"lgPermissaoForaIdade\": " +  getLgPermissaoForaIdade();
		return "{" + valueToString + "}";
	}

	@Override
	public Object clone() {
		return new Matricula(getCdMatricula(),
			getCdMatriz(),
			getCdTurma(),
			getCdPeriodoLetivo(),
			getDtMatricula()==null ? null : (GregorianCalendar)getDtMatricula().clone(),
			getDtConclusao()==null ? null : (GregorianCalendar)getDtConclusao().clone(),
			getStMatricula(),
			getTpMatricula(),
			getNrMatricula(),
			getCdAluno(),
			getCdMatriculaOrigem(),
			getCdReserva(),
			getCdAreaInteresse(),
			getTxtObservacao(),
			getTxtBoletim(),
			getCdCurso(),
			getCdPreMatricula(),
			getTpEscolarizacaoOutroEspaco(),
			getLgTransportePublico(),
			getTpPoderResponsavel(),
			getTpFormaIngresso(),
			getTxtDocumentoOficial(),
			getDtInterrupcao()==null ? null : (GregorianCalendar)getDtInterrupcao().clone(),
			getLgAutorizacaoRematricula(),
			getLgAtividadeComplementar(),
			getLgReprovacao(),
			getStMatriculaCentaurus(),
			getStAlunoCenso(),
			getNrMatriculaCenso(),
			getStCensoFinal(),
			getNmUltimaEscola(),
			getLgAutorizacao(),
			getNrAutorizacao(),
			getLgMatriculaEmCurso(),
			getLgPermissaoForaIdade());
	}

}