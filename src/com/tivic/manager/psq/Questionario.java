package com.tivic.manager.psq;

import java.util.GregorianCalendar;

public class Questionario {

	private int cdQuestionario;
	private int cdGrupoQuestionario;
	private int cdEmpresa;
	private int cdProdutoServico;
	private int cdPessoa;
	private int cdEmpresaPessoa;
	private int cdVinculo;
	private int cdUsuario;
	private int cdConteudo;
	private String nmQuestionario;
	private String txtQuestionario;
	private String nmLocal;
	private GregorianCalendar dtInicio;
	private GregorianCalendar dtTermino;
	private int lgRandomico;
	private int nrRandomico;
	private int lgAjuda;
	private String txtAjuda;
	private String dsDica;
	private int lgCabecalho;
	private int lgRodape;
	private String txtCabecalho;
	private byte[] imgCabecalho;
	private String txtRodape;
	private String txtIntroducao;
	private String txtAgradecimento;
	private int qtTempoResposta;
	private String idQuestionario;

	public Questionario(int cdQuestionario,
			int cdGrupoQuestionario,
			int cdEmpresa,
			int cdProdutoServico,
			int cdPessoa,
			int cdEmpresaPessoa,
			int cdVinculo,
			int cdUsuario,
			int cdConteudo,
			String nmQuestionario,
			String txtQuestionario,
			String nmLocal,
			GregorianCalendar dtInicio,
			GregorianCalendar dtTermino,
			int lgRandomico,
			int nrRandomico,
			int lgAjuda,
			String txtAjuda,
			String dsDica,
			int lgCabecalho,
			int lgRodape,
			String txtCabecalho,
			byte[] imgCabecalho,
			String txtRodape,
			String txtIntroducao,
			String txtAgradecimento,
			int qtTempoResposta,
			String idQuestionario){
		setCdQuestionario(cdQuestionario);
		setCdGrupoQuestionario(cdGrupoQuestionario);
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdPessoa(cdPessoa);
		setCdEmpresaPessoa(cdEmpresaPessoa);
		setCdVinculo(cdVinculo);
		setCdUsuario(cdUsuario);
		setCdConteudo(cdConteudo);
		setNmQuestionario(nmQuestionario);
		setTxtQuestionario(txtQuestionario);
		setNmLocal(nmLocal);
		setDtInicio(dtInicio);
		setDtTermino(dtTermino);
		setLgRandomico(lgRandomico);
		setNrRandomico(nrRandomico);
		setLgAjuda(lgAjuda);
		setTxtAjuda(txtAjuda);
		setDsDica(dsDica);
		setLgCabecalho(lgCabecalho);
		setLgRodape(lgRodape);
		setTxtCabecalho(txtCabecalho);
		setImgCabecalho(imgCabecalho);
		setTxtRodape(txtRodape);
		setTxtIntroducao(txtIntroducao);
		setTxtAgradecimento(txtAgradecimento);
		setQtTempoResposta(qtTempoResposta);
		setIdQuestionario(idQuestionario);
	}
	public void setCdQuestionario(int cdQuestionario){
		this.cdQuestionario=cdQuestionario;
	}
	public int getCdQuestionario(){
		return this.cdQuestionario;
	}
	public void setCdGrupoQuestionario(int cdGrupoQuestionario){
		this.cdGrupoQuestionario=cdGrupoQuestionario;
	}
	public int getCdGrupoQuestionario(){
		return this.cdGrupoQuestionario;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdEmpresaPessoa(int cdEmpresaPessoa){
		this.cdEmpresaPessoa=cdEmpresaPessoa;
	}
	public int getCdEmpresaPessoa(){
		return this.cdEmpresaPessoa;
	}
	public void setCdVinculo(int cdVinculo){
		this.cdVinculo=cdVinculo;
	}
	public int getCdVinculo(){
		return this.cdVinculo;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdConteudo(int cdConteudo){
		this.cdConteudo=cdConteudo;
	}
	public int getCdConteudo(){
		return this.cdConteudo;
	}
	public void setNmQuestionario(String nmQuestionario){
		this.nmQuestionario=nmQuestionario;
	}
	public String getNmQuestionario(){
		return this.nmQuestionario;
	}
	public void setTxtQuestionario(String txtQuestionario){
		this.txtQuestionario=txtQuestionario;
	}
	public String getTxtQuestionario(){
		return this.txtQuestionario;
	}
	public void setNmLocal(String nmLocal){
		this.nmLocal=nmLocal;
	}
	public String getNmLocal(){
		return this.nmLocal;
	}
	public void setDtInicio(GregorianCalendar dtInicio){
		this.dtInicio=dtInicio;
	}
	public GregorianCalendar getDtInicio(){
		return this.dtInicio;
	}
	public void setDtTermino(GregorianCalendar dtTermino){
		this.dtTermino=dtTermino;
	}
	public GregorianCalendar getDtTermino(){
		return this.dtTermino;
	}
	public void setLgRandomico(int lgRandomico){
		this.lgRandomico=lgRandomico;
	}
	public int getLgRandomico(){
		return this.lgRandomico;
	}
	public void setNrRandomico(int nrRandomico){
		this.nrRandomico=nrRandomico;
	}
	public int getNrRandomico(){
		return this.nrRandomico;
	}
	public void setLgAjuda(int lgAjuda){
		this.lgAjuda=lgAjuda;
	}
	public int getLgAjuda(){
		return this.lgAjuda;
	}
	public void setTxtAjuda(String txtAjuda){
		this.txtAjuda=txtAjuda;
	}
	public String getTxtAjuda(){
		return this.txtAjuda;
	}
	public void setDsDica(String dsDica){
		this.dsDica=dsDica;
	}
	public String getDsDica(){
		return this.dsDica;
	}
	public void setLgCabecalho(int lgCabecalho){
		this.lgCabecalho=lgCabecalho;
	}
	public int getLgCabecalho(){
		return this.lgCabecalho;
	}
	public void setLgRodape(int lgRodape){
		this.lgRodape=lgRodape;
	}
	public int getLgRodape(){
		return this.lgRodape;
	}
	public void setTxtCabecalho(String txtCabecalho){
		this.txtCabecalho=txtCabecalho;
	}
	public String getTxtCabecalho(){
		return this.txtCabecalho;
	}
	public void setImgCabecalho(byte[] imgCabecalho){
		this.imgCabecalho=imgCabecalho;
	}
	public byte[] getImgCabecalho(){
		return this.imgCabecalho;
	}
	public void setTxtRodape(String txtRodape){
		this.txtRodape=txtRodape;
	}
	public String getTxtRodape(){
		return this.txtRodape;
	}
	public void setTxtIntroducao(String txtIntroducao){
		this.txtIntroducao=txtIntroducao;
	}
	public String getTxtIntroducao(){
		return this.txtIntroducao;
	}
	public void setTxtAgradecimento(String txtAgradecimento){
		this.txtAgradecimento=txtAgradecimento;
	}
	public String getTxtAgradecimento(){
		return this.txtAgradecimento;
	}
	public void setQtTempoResposta(int qtTempoResposta){
		this.qtTempoResposta=qtTempoResposta;
	}
	public int getQtTempoResposta(){
		return this.qtTempoResposta;
	}
	public void setIdQuestionario(String idQuestionario){
		this.idQuestionario=idQuestionario;
	}
	public String getIdQuestionario(){
		return this.idQuestionario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdQuestionario: " +  getCdQuestionario();
		valueToString += ", cdGrupoQuestionario: " +  getCdGrupoQuestionario();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdEmpresaPessoa: " +  getCdEmpresaPessoa();
		valueToString += ", cdVinculo: " +  getCdVinculo();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdConteudo: " +  getCdConteudo();
		valueToString += ", nmQuestionario: " +  getNmQuestionario();
		valueToString += ", txtQuestionario: " +  getTxtQuestionario();
		valueToString += ", nmLocal: " +  getNmLocal();
		valueToString += ", dtInicio: " +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtTermino: " +  sol.util.Util.formatDateTime(getDtTermino(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgRandomico: " +  getLgRandomico();
		valueToString += ", nrRandomico: " +  getNrRandomico();
		valueToString += ", lgAjuda: " +  getLgAjuda();
		valueToString += ", txtAjuda: " +  getTxtAjuda();
		valueToString += ", dsDica: " +  getDsDica();
		valueToString += ", lgCabecalho: " +  getLgCabecalho();
		valueToString += ", lgRodape: " +  getLgRodape();
		valueToString += ", txtCabecalho: " +  getTxtCabecalho();
		valueToString += ", imgCabecalho: " +  getImgCabecalho();
		valueToString += ", txtRodape: " +  getTxtRodape();
		valueToString += ", txtIntroducao: " +  getTxtIntroducao();
		valueToString += ", txtAgradecimento: " +  getTxtAgradecimento();
		valueToString += ", qtTempoResposta: " +  getQtTempoResposta();
		valueToString += ", idQuestionario: " +  getIdQuestionario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Questionario(getCdQuestionario(),
			getCdGrupoQuestionario(),
			getCdEmpresa(),
			getCdProdutoServico(),
			getCdPessoa(),
			getCdEmpresaPessoa(),
			getCdVinculo(),
			getCdUsuario(),
			getCdConteudo(),
			getNmQuestionario(),
			getTxtQuestionario(),
			getNmLocal(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone(),
			getDtTermino()==null ? null : (GregorianCalendar)getDtTermino().clone(),
			getLgRandomico(),
			getNrRandomico(),
			getLgAjuda(),
			getTxtAjuda(),
			getDsDica(),
			getLgCabecalho(),
			getLgRodape(),
			getTxtCabecalho(),
			getImgCabecalho(),
			getTxtRodape(),
			getTxtIntroducao(),
			getTxtAgradecimento(),
			getQtTempoResposta(),
			getIdQuestionario());
	}

}