package com.tivic.manager.acd;

public class Formacao {

	private int cdPessoa;
	private int cdFormacao;
	private int cdAgenciaFinanciadora;
	private int cdInstituicao;
	private int stFormacao;
	private String nrAnoInicio;
	private String nrAnoTermino;
	private int tpNivel;
	private String nmCurso;
	private int qtCargaHoraria;
	private String nmTituloTrabalho;
	private String nmOrientador;
	private int nrMesObtencaoTitulo;
	private String nrAnoObtencaoTitulo;
	private int tpDoutorado;
	private int tpFormacaoComplementar;
	private int tpMestrado;
	private int tpEspecializacao;
	private String nmInstituicao;
	private int lgComplementacaoPedagogica;
	private int cdFormacaoCurso;
	private int cdInstituicaoSuperior;
	private int tpInstituicao;

	public Formacao(){ }

	public Formacao(int cdPessoa,
			int cdFormacao,
			int cdAgenciaFinanciadora,
			int cdInstituicao,
			int stFormacao,
			String nrAnoInicio,
			String nrAnoTermino,
			int tpNivel,
			String nmCurso,
			int qtCargaHoraria,
			String nmTituloTrabalho,
			String nmOrientador,
			int nrMesObtencaoTitulo,
			String nrAnoObtencaoTitulo,
			int tpDoutorado,
			int tpFormacaoComplementar,
			int tpMestrado,
			int tpEspecializacao,
			String nmInstituicao,
			int lgComplementacaoPedagogica,
			int cdFormacaoCurso,
			int cdInstituicaoSuperior,
			int tpInstituicao){
		setCdPessoa(cdPessoa);
		setCdFormacao(cdFormacao);
		setCdAgenciaFinanciadora(cdAgenciaFinanciadora);
		setCdInstituicao(cdInstituicao);
		setStFormacao(stFormacao);
		setNrAnoInicio(nrAnoInicio);
		setNrAnoTermino(nrAnoTermino);
		setTpNivel(tpNivel);
		setNmCurso(nmCurso);
		setQtCargaHoraria(qtCargaHoraria);
		setNmTituloTrabalho(nmTituloTrabalho);
		setNmOrientador(nmOrientador);
		setNrMesObtencaoTitulo(nrMesObtencaoTitulo);
		setNrAnoObtencaoTitulo(nrAnoObtencaoTitulo);
		setTpDoutorado(tpDoutorado);
		setTpFormacaoComplementar(tpFormacaoComplementar);
		setTpMestrado(tpMestrado);
		setTpEspecializacao(tpEspecializacao);
		setNmInstituicao(nmInstituicao);
		setLgComplementacaoPedagogica(lgComplementacaoPedagogica);
		setCdFormacaoCurso(cdFormacaoCurso);
		setCdInstituicaoSuperior(cdInstituicaoSuperior);
		setTpInstituicao(tpInstituicao);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdFormacao(int cdFormacao){
		this.cdFormacao=cdFormacao;
	}
	public int getCdFormacao(){
		return this.cdFormacao;
	}
	public void setCdAgenciaFinanciadora(int cdAgenciaFinanciadora){
		this.cdAgenciaFinanciadora=cdAgenciaFinanciadora;
	}
	public int getCdAgenciaFinanciadora(){
		return this.cdAgenciaFinanciadora;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setStFormacao(int stFormacao){
		this.stFormacao=stFormacao;
	}
	public int getStFormacao(){
		return this.stFormacao;
	}
	public void setNrAnoInicio(String nrAnoInicio){
		this.nrAnoInicio=nrAnoInicio;
	}
	public String getNrAnoInicio(){
		return this.nrAnoInicio;
	}
	public void setNrAnoTermino(String nrAnoTermino){
		this.nrAnoTermino=nrAnoTermino;
	}
	public String getNrAnoTermino(){
		return this.nrAnoTermino;
	}
	public void setTpNivel(int tpNivel){
		this.tpNivel=tpNivel;
	}
	public int getTpNivel(){
		return this.tpNivel;
	}
	public void setNmCurso(String nmCurso){
		this.nmCurso=nmCurso;
	}
	public String getNmCurso(){
		return this.nmCurso;
	}
	public void setQtCargaHoraria(int qtCargaHoraria){
		this.qtCargaHoraria=qtCargaHoraria;
	}
	public int getQtCargaHoraria(){
		return this.qtCargaHoraria;
	}
	public void setNmTituloTrabalho(String nmTituloTrabalho){
		this.nmTituloTrabalho=nmTituloTrabalho;
	}
	public String getNmTituloTrabalho(){
		return this.nmTituloTrabalho;
	}
	public void setNmOrientador(String nmOrientador){
		this.nmOrientador=nmOrientador;
	}
	public String getNmOrientador(){
		return this.nmOrientador;
	}
	public void setNrMesObtencaoTitulo(int nrMesObtencaoTitulo){
		this.nrMesObtencaoTitulo=nrMesObtencaoTitulo;
	}
	public int getNrMesObtencaoTitulo(){
		return this.nrMesObtencaoTitulo;
	}
	public void setNrAnoObtencaoTitulo(String nrAnoObtencaoTitulo){
		this.nrAnoObtencaoTitulo=nrAnoObtencaoTitulo;
	}
	public String getNrAnoObtencaoTitulo(){
		return this.nrAnoObtencaoTitulo;
	}
	public void setTpDoutorado(int tpDoutorado){
		this.tpDoutorado=tpDoutorado;
	}
	public int getTpDoutorado(){
		return this.tpDoutorado;
	}
	public void setTpFormacaoComplementar(int tpFormacaoComplementar){
		this.tpFormacaoComplementar=tpFormacaoComplementar;
	}
	public int getTpFormacaoComplementar(){
		return this.tpFormacaoComplementar;
	}
	public void setTpMestrado(int tpMestrado){
		this.tpMestrado=tpMestrado;
	}
	public int getTpMestrado(){
		return this.tpMestrado;
	}
	public void setTpEspecializacao(int tpEspecializacao){
		this.tpEspecializacao=tpEspecializacao;
	}
	public int getTpEspecializacao(){
		return this.tpEspecializacao;
	}
	public void setNmInstituicao(String nmInstituicao){
		this.nmInstituicao=nmInstituicao;
	}
	public String getNmInstituicao(){
		return this.nmInstituicao;
	}
	public void setLgComplementacaoPedagogica(int lgComplementacaoPedagogica){
		this.lgComplementacaoPedagogica=lgComplementacaoPedagogica;
	}
	public int getLgComplementacaoPedagogica(){
		return this.lgComplementacaoPedagogica;
	}
	public void setCdFormacaoCurso(int cdFormacaoCurso){
		this.cdFormacaoCurso=cdFormacaoCurso;
	}
	public int getCdFormacaoCurso(){
		return this.cdFormacaoCurso;
	}
	public void setCdInstituicaoSuperior(int cdInstituicaoSuperior){
		this.cdInstituicaoSuperior=cdInstituicaoSuperior;
	}
	public int getCdInstituicaoSuperior(){
		return this.cdInstituicaoSuperior;
	}
	public void setTpInstituicao(int tpInstituicao){
		this.tpInstituicao=tpInstituicao;
	}
	public int getTpInstituicao(){
		return this.tpInstituicao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdFormacao: " +  getCdFormacao();
		valueToString += ", cdAgenciaFinanciadora: " +  getCdAgenciaFinanciadora();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", stFormacao: " +  getStFormacao();
		valueToString += ", nrAnoInicio: " +  getNrAnoInicio();
		valueToString += ", nrAnoTermino: " +  getNrAnoTermino();
		valueToString += ", tpNivel: " +  getTpNivel();
		valueToString += ", nmCurso: " +  getNmCurso();
		valueToString += ", qtCargaHoraria: " +  getQtCargaHoraria();
		valueToString += ", nmTituloTrabalho: " +  getNmTituloTrabalho();
		valueToString += ", nmOrientador: " +  getNmOrientador();
		valueToString += ", nrMesObtencaoTitulo: " +  getNrMesObtencaoTitulo();
		valueToString += ", nrAnoObtencaoTitulo: " +  getNrAnoObtencaoTitulo();
		valueToString += ", tpDoutorado: " +  getTpDoutorado();
		valueToString += ", tpFormacaoComplementar: " +  getTpFormacaoComplementar();
		valueToString += ", tpMestrado: " +  getTpMestrado();
		valueToString += ", tpEspecializacao: " +  getTpEspecializacao();
		valueToString += ", nmInstituicao: " +  getNmInstituicao();
		valueToString += ", lgComplementacaoPedagogica: " +  getLgComplementacaoPedagogica();
		valueToString += ", cdFormacaoCurso: " +  getCdFormacaoCurso();
		valueToString += ", cdInstituicaoSuperior: " +  getCdInstituicaoSuperior();
		valueToString += ", tpInstituicao: " +  getTpInstituicao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Formacao(getCdPessoa(),
			getCdFormacao(),
			getCdAgenciaFinanciadora(),
			getCdInstituicao(),
			getStFormacao(),
			getNrAnoInicio(),
			getNrAnoTermino(),
			getTpNivel(),
			getNmCurso(),
			getQtCargaHoraria(),
			getNmTituloTrabalho(),
			getNmOrientador(),
			getNrMesObtencaoTitulo(),
			getNrAnoObtencaoTitulo(),
			getTpDoutorado(),
			getTpFormacaoComplementar(),
			getTpMestrado(),
			getTpEspecializacao(),
			getNmInstituicao(),
			getLgComplementacaoPedagogica(),
			getCdFormacaoCurso(),
			getCdInstituicaoSuperior(),
			getTpInstituicao());
	}

}