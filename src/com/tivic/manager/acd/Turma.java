package com.tivic.manager.acd;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.Map;

import com.tivic.manager.util.Util;

public class Turma {

	private int cdTurma;
	private int cdMatriz;
	private int cdPeriodoLetivo;
	private String nmTurma;
	private GregorianCalendar dtAbertura;
	private GregorianCalendar dtConclusao;
	private int tpTurno;
	private int cdCategoriaMensalidade;
	private int cdCategoriaMatricula;
	private int stTurma;
	private int cdTabelaPreco;
	private int cdInstituicao;
	private int cdCurso;
	private int qtVagas;
	private int cdCursoModulo;
	private String nrInep;
	private int qtDiasSemanaAtividade;
	private int tpAtendimento;
	private int tpModalidadeEnsino;
	private String idTurma;
	private int tpEducacaoInfantil;
	private int lgMaisEduca;
	private int cdTurmaAnterior;
	private int tpTurnoAtividadeComplementar;
	private int tpLocalDiferenciado;
	
	public Turma(){ }

	public Turma(int cdTurma,
			int cdMatriz,
			int cdPeriodoLetivo,
			String nmTurma,
			GregorianCalendar dtAbertura,
			GregorianCalendar dtConclusao,
			int tpTurno,
			int cdCategoriaMensalidade,
			int cdCategoriaMatricula,
			int stTurma,
			int cdTabelaPreco,
			int cdInstituicao,
			int cdCurso,
			int qtVagas,
			int cdCursoModulo,
			String nrInep,
			int qtDiasSemanaAtividade,
			int tpAtendimento,
			int tpModalidadeEnsino,
			String idTurma,
			int tpEducacaoInfantil,
			int lgMaisEduca,
			int cdTurmaAnterior,
			int tpTurnoAtividadeComplementar,
			int tpLocalDiferenciado){
		setCdTurma(cdTurma);
		setCdMatriz(cdMatriz);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setNmTurma(nmTurma);
		setDtAbertura(dtAbertura);
		setDtConclusao(dtConclusao);
		setTpTurno(tpTurno);
		setCdCategoriaMensalidade(cdCategoriaMensalidade);
		setCdCategoriaMatricula(cdCategoriaMatricula);
		setStTurma(stTurma);
		setCdTabelaPreco(cdTabelaPreco);
		setCdInstituicao(cdInstituicao);
		setCdCurso(cdCurso);
		setQtVagas(qtVagas);
		setCdCursoModulo(cdCursoModulo);
		setNrInep(nrInep);
		setQtDiasSemanaAtividade(qtDiasSemanaAtividade);
		setTpAtendimento(tpAtendimento);
		setTpModalidadeEnsino(tpModalidadeEnsino);
		setIdTurma(idTurma);
		setTpEducacaoInfantil(tpEducacaoInfantil);
		setLgMaisEduca(lgMaisEduca);
		setCdTurmaAnterior(cdTurmaAnterior);
		setTpTurnoAtividadeComplementar(tpTurnoAtividadeComplementar);
		setTpLocalDiferenciado(tpLocalDiferenciado);
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public void setCdMatriz(int cdMatriz){
		this.cdMatriz=cdMatriz;
	}
	public int getCdMatriz(){
		return this.cdMatriz;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setNmTurma(String nmTurma){
		this.nmTurma=nmTurma;
	}
	public String getNmTurma(){
		return this.nmTurma;
	}
	public void setDtAbertura(GregorianCalendar dtAbertura){
		this.dtAbertura=dtAbertura;
	}
	public GregorianCalendar getDtAbertura(){
		return this.dtAbertura;
	}
	public void setDtConclusao(GregorianCalendar dtConclusao){
		this.dtConclusao=dtConclusao;
	}
	public GregorianCalendar getDtConclusao(){
		return this.dtConclusao;
	}
	public void setTpTurno(int tpTurno){
		this.tpTurno=tpTurno;
	}
	public int getTpTurno(){
		return this.tpTurno;
	}
	public void setCdCategoriaMensalidade(int cdCategoriaMensalidade){
		this.cdCategoriaMensalidade=cdCategoriaMensalidade;
	}
	public int getCdCategoriaMensalidade(){
		return this.cdCategoriaMensalidade;
	}
	public void setCdCategoriaMatricula(int cdCategoriaMatricula){
		this.cdCategoriaMatricula=cdCategoriaMatricula;
	}
	public int getCdCategoriaMatricula(){
		return this.cdCategoriaMatricula;
	}
	public void setStTurma(int stTurma){
		this.stTurma=stTurma;
	}
	public int getStTurma(){
		return this.stTurma;
	}
	public void setCdTabelaPreco(int cdTabelaPreco){
		this.cdTabelaPreco=cdTabelaPreco;
	}
	public int getCdTabelaPreco(){
		return this.cdTabelaPreco;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setQtVagas(int qtVagas){
		this.qtVagas=qtVagas;
	}
	public int getQtVagas(){
		return this.qtVagas;
	}
	public void setCdCursoModulo(int cdCursoModulo){
		this.cdCursoModulo=cdCursoModulo;
	}
	public int getCdCursoModulo(){
		return this.cdCursoModulo;
	}
	public void setNrInep(String nrInep){
		this.nrInep=nrInep;
	}
	public String getNrInep(){
		return this.nrInep;
	}
	public void setQtDiasSemanaAtividade(int qtDiasSemanaAtividade){
		this.qtDiasSemanaAtividade=qtDiasSemanaAtividade;
	}
	public int getQtDiasSemanaAtividade(){
		return this.qtDiasSemanaAtividade;
	}
	public void setTpAtendimento(int tpAtendimento){
		this.tpAtendimento=tpAtendimento;
	}
	public int getTpAtendimento(){
		return this.tpAtendimento;
	}
	public void setTpModalidadeEnsino(int tpModalidadeEnsino){
		this.tpModalidadeEnsino=tpModalidadeEnsino;
	}
	public int getTpModalidadeEnsino(){
		return this.tpModalidadeEnsino;
	}
	public void setIdTurma(String idTurma){
		this.idTurma=idTurma;
	}
	public String getIdTurma(){
		return this.idTurma;
	}
	public void setTpEducacaoInfantil(int tpEducacaoInfantil){
		this.tpEducacaoInfantil=tpEducacaoInfantil;
	}
	public int getTpEducacaoInfantil(){
		return this.tpEducacaoInfantil;
	}
	public void setLgMaisEduca(int lgMaisEduca){
		this.lgMaisEduca=lgMaisEduca;
	}
	public int getLgMaisEduca(){
		return this.lgMaisEduca;
	}
	public void setCdTurmaAnterior(int cdTurmaAnterior){
		this.cdTurmaAnterior=cdTurmaAnterior;
	}
	public int getCdTurmaAnterior(){
		return this.cdTurmaAnterior;
	}
	public void setTpTurnoAtividadeComplementar(int tpTurnoAtividadeComplementar) {
		this.tpTurnoAtividadeComplementar = tpTurnoAtividadeComplementar;
	}
	public int getTpTurnoAtividadeComplementar() {
		return tpTurnoAtividadeComplementar;
	}
	public void setTpLocalDiferenciado(int tpLocalDiferenciado) {
		this.tpLocalDiferenciado = tpLocalDiferenciado;
	}
	public int getTpLocalDiferenciado() {
		return tpLocalDiferenciado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "\"cdTurma\": " +  getCdTurma();
		valueToString += ", \"cdMatriz\": " +  getCdMatriz();
		valueToString += ", \"cdPeriodoLetivo\": " +  getCdPeriodoLetivo();
		valueToString += ", \"nmTurma\": \"" +  getNmTurma()+"\"";
		valueToString += ", \"dtAbertura\": " + (getDtAbertura() != null ? "\"" +  Util.convCalendarStringIso(getDtAbertura()) +"\"" : null);
		valueToString += ", \"dtConclusao\": " + (getDtConclusao() != null ? "\"" +  Util.convCalendarStringIso(getDtConclusao()) +"\"" : null);
		valueToString += ", \"tpTurno\": " +  getTpTurno();
		valueToString += ", \"cdCategoriaMensalidade\": " +  getCdCategoriaMensalidade();
		valueToString += ", \"cdCategoriaMatricula\": " +  getCdCategoriaMatricula();
		valueToString += ", \"stTurma\": " +  getStTurma();
		valueToString += ", \"cdTabelaPreco\": " +  getCdTabelaPreco();
		valueToString += ", \"cdInstituicao\": " +  getCdInstituicao();
		valueToString += ", \"cdCurso\": " +  getCdCurso();
		valueToString += ", \"qtVagas\": " +  getQtVagas();
		valueToString += ", \"cdCursoModulo\": " +  getCdCursoModulo();
		valueToString += ", \"nrInep\": \"" +  getNrInep()+"\"";
		valueToString += ", \"qtDiasSemanaAtividade\": " +  getQtDiasSemanaAtividade();
		valueToString += ", \"tpAtendimento\": " +  getTpAtendimento();
		valueToString += ", \"tpModalidadeEnsino\": " +  getTpModalidadeEnsino();
		valueToString += ", \"idTurma\": \"" +  getIdTurma()+"\"";
		valueToString += ", \"tpEducacaoInfantil\": " +  getTpEducacaoInfantil();
		valueToString += ", \"lgMaisEduca\": " +  getLgMaisEduca();
		valueToString += ", \"cdTurmaAnterior\": " +  getCdTurmaAnterior();
		valueToString += ", \"tpTurnoAtividadeComplementar\": " +  getTpTurnoAtividadeComplementar();
		valueToString += ", \"tpLocalDiferenciado\": " +  getTpLocalDiferenciado();
		return "{" + valueToString + "}";
	}
	
	public String toORM() {
		String valueToString = "";
		valueToString += "\"cdTurma\": " +  getCdTurma();
		valueToString += ", \"cdMatriz\": " +  getCdMatriz();
		valueToString += ", \"cdPeriodoLetivo\": " +  getCdPeriodoLetivo();
		valueToString += ", \"nmTurma\": \"" +  getNmTurma()+"\"";
		valueToString += ", \"dtAbertura\": \"" +  sol.util.Util.formatDateTime(getDtAbertura(), "yyyy-MM-dd", "")+"\"";
		valueToString += ", \"dtConclusao\": \"" +  sol.util.Util.formatDateTime(getDtConclusao(), "yyyy-MM-dd", "")+"\"";
		valueToString += ", \"tpTurno\": " +  getTpTurno();
		valueToString += ", \"cdCategoriaMensalidade\": " +  getCdCategoriaMensalidade();
		valueToString += ", \"cdCategoriaMatricula\": " +  getCdCategoriaMatricula();
		valueToString += ", \"stTurma\": " +  getStTurma();
		valueToString += ", \"cdTabelaPreco\": " +  getCdTabelaPreco();
		valueToString += ", \"cdInstituicao\": " +  getCdInstituicao();
		valueToString += ", \"cdCurso\": " +  getCdCurso();
		valueToString += ", \"qtVagas\": " +  getQtVagas();
		valueToString += ", \"cdCursoModulo\": " +  getCdCursoModulo();
		valueToString += ", \"nrInep\": \"" +  getNrInep()+"\"";
		valueToString += ", \"qtDiasSemanaAtividade\": " +  getQtDiasSemanaAtividade();
		valueToString += ", \"tpAtendimento\": " +  getTpAtendimento();
		valueToString += ", \"tpModalidadeEnsino\": " +  getTpModalidadeEnsino();
		valueToString += ", \"idTurma\": \"" +  getIdTurma()+"\"";
		valueToString += ", \"tpEducacaoInfantil\": " +  getTpEducacaoInfantil();
		valueToString += ", \"lgMaisEduca\": " +  getLgMaisEduca();
		valueToString += ", \"cdTurmaAnterior\": " +  getCdTurmaAnterior();
		valueToString += ", \"tpTurnoAtividadeComplementar\": " +  getTpTurnoAtividadeComplementar();
		valueToString += ", \"tpLocalDiferenciado\": " +  getTpLocalDiferenciado();
		return "{" + valueToString + "}";
	}
	
	public static String fromRegister(Map<String, Object> register) {
		String valueToString = "";
		valueToString += "\"cdTurma\": " + register.get("CD_TURMA");
		valueToString += ", \"cdMatriz\": " +  register.get("CD_MATRIZ");
		valueToString += ", \"cdPeriodoLetivo\": " + register.get("CD_PERIODO_LETIVO");
		valueToString += ", \"nmTurma\": \"" + register.get("NM_TURMA")+"\"";
		if(register.get("DT_ABERTURA") != null)
			valueToString += ", \"dtAbertura\": \"" + sol.util.Util.formatDateTime(Util.longToCalendar(((Timestamp)register.get("DT_ABERTURA")).getTime()), "yyyy-MM-dd", "") +"\"";
		if(register.get("DT_CONCLUSAO") != null)
			valueToString += ", \"dtConclusao\": \"" +  sol.util.Util.formatDateTime(Util.longToCalendar(((Timestamp)register.get("DT_CONCLUSAO")).getTime()), "yyyy-MM-dd", "")+"\"";
		valueToString += ", \"tpTurno\": " +register.get("TP_TURNO");
		valueToString += ", \"cdCategoriaMensalidade\": " + register.get("CD_CATEGORIA_MENSALIDADE");
		valueToString += ", \"cdCategoriaMatricula\": " + register.get("CD_CATEGORIA_MATRICULA");
		valueToString += ", \"stTurma\": " + register.get("ST_TURMA");
		valueToString += ", \"cdTabelaPreco\": " + register.get("CD_TABELA_PRECO");
		valueToString += ", \"cdInstituicao\": " + register.get("CD_INSTITUICAO");
		valueToString += ", \"cdCurso\": " + register.get("CD_CURSO");
		valueToString += ", \"qtVagas\": " + register.get("QT_VAGAS");
		valueToString += ", \"cdCursoModulo\": " + register.get("CD_CURSO_MODULO");
		valueToString += ", \"nrInep\": \"" + register.get("NR_INEP")+"\"";
		valueToString += ", \"qtDiasSemanaAtividade\": " + register.get("QT_DIAS_SEMANA_ATIVIDADE");
		valueToString += ", \"tpAtendimento\": " + register.get("TP_ATENDIMENTO");
		valueToString += ", \"tpModalidadeEnsino\": " + register.get("TP_MODALIDADE_ENSINO");
		valueToString += ", \"idTurma\": \"" + register.get("ID_TURMA")+"\"";
		valueToString += ", \"tpEducacaoInfantil\": " + register.get("TP_EDUCACAO_INFANTIL");
		valueToString += ", \"lgMaisEduca\": " + register.get("LG_MAIS_EDUCA");
		valueToString += ", \"cdTurmaAnterior\": " + register.get("CD_TURMA_ANTERIOR");
		valueToString += ", \"tpTurnoAtividadeComplementar\": " + register.get("TP_TURNO_ATIVIDADE_COMPLEMENTAR");
		valueToString += ", \"tpLocalDiferenciado\": " + register.get("TP_LOCAL_DIFERENCIADO");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Turma(getCdTurma(),
			getCdMatriz(),
			getCdPeriodoLetivo(),
			getNmTurma(),
			getDtAbertura()==null ? null : (GregorianCalendar)getDtAbertura().clone(),
			getDtConclusao()==null ? null : (GregorianCalendar)getDtConclusao().clone(),
			getTpTurno(),
			getCdCategoriaMensalidade(),
			getCdCategoriaMatricula(),
			getStTurma(),
			getCdTabelaPreco(),
			getCdInstituicao(),
			getCdCurso(),
			getQtVagas(),
			getCdCursoModulo(),
			getNrInep(),
			getQtDiasSemanaAtividade(),
			getTpAtendimento(),
			getTpModalidadeEnsino(),
			getIdTurma(),
			getTpEducacaoInfantil(),
			getLgMaisEduca(),
			getCdTurmaAnterior(),
			getTpTurnoAtividadeComplementar(),
			getTpLocalDiferenciado());
	}

}