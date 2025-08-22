package com.tivic.manager.prc;

import java.sql.Connection;
import java.util.GregorianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.tivic.sol.connection.Conexao;

public class Processo {

	private int cdProcesso;
	private int cdTipoProcesso;
	private int cdOrgaoJudicial;
	private int cdComarca;
	private int cdTipoSituacao;
	private int cdAdvogado;
	private int cdAdvogadoContrario;
	private int cdOrgao;
	private int cdGrupoProcesso;
	private int lgClienteAutor;
	private String txtObjeto;
	private String txtObservacao;
	private GregorianCalendar dtDistribuicao;
	private GregorianCalendar dtAutuacao;
	private String txtSentenca;
	private GregorianCalendar dtSentenca;
	private int stProcesso;
	private GregorianCalendar dtSituacao;
	private double vlProcesso;
	private double vlAcordo;
	private int lgTaxaPapel;
	private int lgTaxaPaga;
	private String nrAntigo;
	private GregorianCalendar dtUltimoAndamento;
	private int qtMaxDias;
	private double vlSentenca;
	private int tpInstancia;
	private GregorianCalendar dtAtualizacao;
	private int tpPerda;
	private int cdAdvogadoTitular;
	private int cdOficialJustica;
	private int cdTipoPedido;
	private int cdTipoObjeto;
	private int cdResponsavelArquivo;
	private GregorianCalendar dtCadastro;
	private int tpSentenca;
	private String nrJuizo;
	private int cdCidade;
	private int qtMediaDias;
	private GregorianCalendar dtPrazo;
	private int lgPrazo;
	private String nmConteiner1;
	private String nmConteiner2;
	private String nmConteiner3;
	private int stLiminar;
	private int stArquivo;
	private GregorianCalendar dtRepasse;
	private int cdCentroCusto;
	private GregorianCalendar dtAtualizacaoEdit;
	private GregorianCalendar dtAtualizacaoEdi;
	private int stAtualizacaoEdi;
	private int cdTribunal;
	private int cdJuizo;
	private String nrProcesso;
	private int cdUsuarioCadastro;
	private int tpAutos;
	private String nrInterno;
	private int cdGrupoTrabalho;
	private int cdProcessoPrincipal;
	private String idProcesso;
	private int cdJuiz;
	private int cdSistemaProcesso;
	private int tpRito;
	private int tpRepasse;
	private GregorianCalendar dtInativacao;
	private int lgImportante;

	public Processo(){ }

	public Processo(int cdProcesso,
			int cdTipoProcesso,
			int cdOrgaoJudicial,
			int cdComarca,
			int cdTipoSituacao,
			int cdAdvogado,
			int cdAdvogadoContrario,
			int cdOrgao,
			int cdGrupoProcesso,
			int lgClienteAutor,
			String txtObjeto,
			String txtObservacao,
			GregorianCalendar dtDistribuicao,
			GregorianCalendar dtAutuacao,
			String txtSentenca,
			GregorianCalendar dtSentenca,
			int stProcesso,
			GregorianCalendar dtSituacao,
			double vlProcesso,
			double vlAcordo,
			int lgTaxaPapel,
			int lgTaxaPaga,
			String nrAntigo,
			GregorianCalendar dtUltimoAndamento,
			int qtMaxDias,
			double vlSentenca,
			int tpInstancia,
			GregorianCalendar dtAtualizacao,
			int tpPerda,
			int cdAdvogadoTitular,
			int cdOficialJustica,
			int cdTipoPedido,
			int cdTipoObjeto,
			int cdResponsavelArquivo,
			GregorianCalendar dtCadastro,
			int tpSentenca,
			String nrJuizo,
			int cdCidade,
			int qtMediaDias,
			GregorianCalendar dtPrazo,
			int lgPrazo,
			String nmConteiner1,
			String nmConteiner2,
			String nmConteiner3,
			int stLiminar,
			int stArquivo,
			GregorianCalendar dtRepasse,
			int cdCentroCusto,
			GregorianCalendar dtAtualizacaoEdit,
			GregorianCalendar dtAtualizacaoEdi,
			int stAtualizacaoEdi,
			int cdTribunal,
			int cdJuizo,
			String nrProcesso,
			int cdUsuarioCadastro,
			int tpAutos,
			String nrInterno,
			int cdGrupoTrabalho,
			int cdProcessoPrincipal,
			String idProcesso,
			int cdJuiz,
			int cdSistemaProcesso,
			int tpRito,
			int tpRepasse,
			GregorianCalendar dtInativacao,
			int lgImportante){
		setCdProcesso(cdProcesso);
		setCdTipoProcesso(cdTipoProcesso);
		setCdOrgaoJudicial(cdOrgaoJudicial);
		setCdComarca(cdComarca);
		setCdTipoSituacao(cdTipoSituacao);
		setCdAdvogado(cdAdvogado);
		setCdAdvogadoContrario(cdAdvogadoContrario);
		setCdOrgao(cdOrgao);
		setCdGrupoProcesso(cdGrupoProcesso);
		setLgClienteAutor(lgClienteAutor);
		setTxtObjeto(txtObjeto);
		setTxtObservacao(txtObservacao);
		setDtDistribuicao(dtDistribuicao);
		setDtAutuacao(dtAutuacao);
		setTxtSentenca(txtSentenca);
		setDtSentenca(dtSentenca);
		setStProcesso(stProcesso);
		setDtSituacao(dtSituacao);
		setVlProcesso(vlProcesso);
		setVlAcordo(vlAcordo);
		setLgTaxaPapel(lgTaxaPapel);
		setLgTaxaPaga(lgTaxaPaga);
		setNrAntigo(nrAntigo);
		setDtUltimoAndamento(dtUltimoAndamento);
		setQtMaxDias(qtMaxDias);
		setVlSentenca(vlSentenca);
		setTpInstancia(tpInstancia);
		setDtAtualizacao(dtAtualizacao);
		setTpPerda(tpPerda);
		setCdAdvogadoTitular(cdAdvogadoTitular);
		setCdOficialJustica(cdOficialJustica);
		setCdTipoPedido(cdTipoPedido);
		setCdTipoObjeto(cdTipoObjeto);
		setCdResponsavelArquivo(cdResponsavelArquivo);
		setDtCadastro(dtCadastro);
		setTpSentenca(tpSentenca);
		setNrJuizo(nrJuizo);
		setCdCidade(cdCidade);
		setQtMediaDias(qtMediaDias);
		setDtPrazo(dtPrazo);
		setLgPrazo(lgPrazo);
		setNmConteiner1(nmConteiner1);
		setNmConteiner2(nmConteiner2);
		setNmConteiner3(nmConteiner3);
		setStLiminar(stLiminar);
		setStArquivo(stArquivo);
		setDtRepasse(dtRepasse);
		setCdCentroCusto(cdCentroCusto);
		setDtAtualizacaoEdit(dtAtualizacaoEdit);
		setDtAtualizacaoEdi(dtAtualizacaoEdi);
		setStAtualizacaoEdi(stAtualizacaoEdi);
		setCdTribunal(cdTribunal);
		setCdJuizo(cdJuizo);
		setTpAutos(tpAutos);
		setNrProcesso(nrProcesso);
		setCdUsuarioCadastro(cdUsuarioCadastro);
		setNrInterno(nrInterno);
		setCdGrupoTrabalho(cdGrupoTrabalho);
		setCdProcessoPrincipal(cdProcessoPrincipal);
		setIdProcesso(idProcesso);
		setCdJuiz(cdJuiz);
		setCdSistemaProcesso(cdSistemaProcesso);
		setTpRito(tpRito);
		setTpRepasse(tpRepasse);
		setDtInativacao(dtInativacao);
		setLgImportante(lgImportante);
	}

	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setIdProcesso(String idProcesso){
		this.idProcesso=idProcesso;
	}
	public String getIdProcesso(){
		return this.idProcesso;
	}
	public void setCdTipoProcesso(int cdTipoProcesso){
		this.cdTipoProcesso=cdTipoProcesso;
	}
	public int getCdTipoProcesso(){
		return this.cdTipoProcesso;
	}
	public void setCdOrgaoJudicial(int cdOrgaoJudicial){
		this.cdOrgaoJudicial=cdOrgaoJudicial;
	}
	public int getCdOrgaoJudicial(){
		return this.cdOrgaoJudicial;
	}
	public void setCdComarca(int cdComarca){
		this.cdComarca=cdComarca;
	}
	public int getCdComarca(){
		return this.cdComarca;
	}
	public void setCdTipoSituacao(int cdTipoSituacao){
		this.cdTipoSituacao=cdTipoSituacao;
	}
	public int getCdTipoSituacao(){
		return this.cdTipoSituacao;
	}
	public void setCdAdvogado(int cdAdvogado){
		this.cdAdvogado=cdAdvogado;
	}
	public int getCdAdvogado(){
		return this.cdAdvogado;
	}
	public void setCdAdvogadoContrario(int cdAdvogadoContrario){
		this.cdAdvogadoContrario=cdAdvogadoContrario;
	}
	public int getCdAdvogadoContrario(){
		return this.cdAdvogadoContrario;
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setCdGrupoProcesso(int cdGrupoProcesso){
		this.cdGrupoProcesso=cdGrupoProcesso;
	}
	public int getCdGrupoProcesso(){
		return this.cdGrupoProcesso;
	}
	public void setLgClienteAutor(int lgClienteAutor){
		this.lgClienteAutor=lgClienteAutor;
	}
	public int getLgClienteAutor(){
		return this.lgClienteAutor;
	}
	public void setTxtObjeto(String txtObjeto){
		this.txtObjeto=txtObjeto;
	}
	public String getTxtObjeto(){
		return this.txtObjeto;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setDtDistribuicao(GregorianCalendar dtDistribuicao){
		this.dtDistribuicao=dtDistribuicao;
	}
	public GregorianCalendar getDtDistribuicao(){
		return this.dtDistribuicao;
	}
	public void setDtAutuacao(GregorianCalendar dtAutuacao){
		this.dtAutuacao=dtAutuacao;
	}
	public GregorianCalendar getDtAutuacao(){
		return this.dtAutuacao;
	}
	public void setTxtSentenca(String txtSentenca){
		this.txtSentenca=txtSentenca;
	}
	public String getTxtSentenca(){
		return this.txtSentenca;
	}
	public void setDtSentenca(GregorianCalendar dtSentenca){
		this.dtSentenca=dtSentenca;
	}
	public GregorianCalendar getDtSentenca(){
		return this.dtSentenca;
	}
	public void setStProcesso(int stProcesso){
		this.stProcesso=stProcesso;
	}
	public int getStProcesso(){
		return this.stProcesso;
	}
	public void setDtSituacao(GregorianCalendar dtSituacao){
		this.dtSituacao=dtSituacao;
	}
	public GregorianCalendar getDtSituacao(){
		return this.dtSituacao;
	}
	public void setVlProcesso(double vlProcesso){
		this.vlProcesso=vlProcesso;
	}
	public double getVlProcesso(){
		return this.vlProcesso;
	}
	public void setVlAcordo(double vlAcordo){
		this.vlAcordo=vlAcordo;
	}
	public double getVlAcordo(){
		return this.vlAcordo;
	}
	public void setLgTaxaPapel(int lgTaxaPapel){
		this.lgTaxaPapel=lgTaxaPapel;
	}
	public int getLgTaxaPapel(){
		return this.lgTaxaPapel;
	}
	public void setLgTaxaPaga(int lgTaxaPaga){
		this.lgTaxaPaga=lgTaxaPaga;
	}
	public int getLgTaxaPaga(){
		return this.lgTaxaPaga;
	}
	public void setNrAntigo(String nrAntigo){
		this.nrAntigo=nrAntigo;
	}
	public String getNrAntigo(){
		return this.nrAntigo;
	}
	public void setDtUltimoAndamento(GregorianCalendar dtUltimoAndamento){
		this.dtUltimoAndamento=dtUltimoAndamento;
	}
	public GregorianCalendar getDtUltimoAndamento(){
		return this.dtUltimoAndamento;
	}
	public void setQtMaxDias(int qtMaxDias){
		this.qtMaxDias=qtMaxDias;
	}
	public int getQtMaxDias(){
		return this.qtMaxDias;
	}
	public void setVlSentenca(double vlSentenca){
		this.vlSentenca=vlSentenca;
	}
	public double getVlSentenca(){
		return this.vlSentenca;
	}
	public void setTpInstancia(int tpInstancia){
		this.tpInstancia=tpInstancia;
	}
	public int getTpInstancia(){
		return this.tpInstancia;
	}
	public void setDtAtualizacao(GregorianCalendar dtAtualizacao){
		this.dtAtualizacao=dtAtualizacao;
	}
	public GregorianCalendar getDtAtualizacao(){
		return this.dtAtualizacao;
	}
	public void setTpPerda(int tpPerda){
		this.tpPerda=tpPerda;
	}
	public int getTpPerda(){
		return this.tpPerda;
	}
	public void setCdAdvogadoTitular(int cdAdvogadoTitular){
		this.cdAdvogadoTitular=cdAdvogadoTitular;
	}
	public int getCdAdvogadoTitular(){
		return this.cdAdvogadoTitular;
	}
	public void setCdOficialJustica(int cdOficialJustica){
		this.cdOficialJustica=cdOficialJustica;
	}
	public int getCdOficialJustica(){
		return this.cdOficialJustica;
	}
	public void setCdTipoPedido(int cdTipoPedido){
		this.cdTipoPedido=cdTipoPedido;
	}
	public int getCdTipoPedido(){
		return this.cdTipoPedido;
	}
	public void setCdTipoObjeto(int cdTipoObjeto){
		this.cdTipoObjeto=cdTipoObjeto;
	}
	public int getCdTipoObjeto(){
		return this.cdTipoObjeto;
	}
	public void setCdResponsavelArquivo(int cdResponsavelArquivo){
		this.cdResponsavelArquivo=cdResponsavelArquivo;
	}
	public int getCdResponsavelArquivo(){
		return this.cdResponsavelArquivo;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setTpSentenca(int tpSentenca){
		this.tpSentenca=tpSentenca;
	}
	public int getTpSentenca(){
		return this.tpSentenca;
	}
	public void setNrJuizo(String nrJuizo){
		this.nrJuizo=nrJuizo;
	}
	public String getNrJuizo(){
		return this.nrJuizo;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setQtMediaDias(int qtMediaDias){
		this.qtMediaDias=qtMediaDias;
	}
	public int getQtMediaDias(){
		return this.qtMediaDias;
	}
	public void setDtPrazo(GregorianCalendar dtPrazo){
		this.dtPrazo=dtPrazo;
	}
	public GregorianCalendar getDtPrazo(){
		return this.dtPrazo;
	}
	public void setLgPrazo(int lgPrazo){
		this.lgPrazo=lgPrazo;
	}
	public int getLgPrazo(){
		return this.lgPrazo;
	}
	public void setNmConteiner1(String nmConteiner1){
		this.nmConteiner1=nmConteiner1;
	}
	public String getNmConteiner1(){
		return this.nmConteiner1;
	}
	public void setNmConteiner2(String nmConteiner2){
		this.nmConteiner2=nmConteiner2;
	}
	public String getNmConteiner2(){
		return this.nmConteiner2;
	}
	public void setNmConteiner3(String nmConteiner3){
		this.nmConteiner3=nmConteiner3;
	}
	public String getNmConteiner3(){
		return this.nmConteiner3;
	}
	public void setStLiminar(int stLiminar){
		this.stLiminar=stLiminar;
	}
	public int getStLiminar(){
		return this.stLiminar;
	}
	public void setStArquivo(int stArquivo){
		this.stArquivo=stArquivo;
	}
	public int getStArquivo(){
		return this.stArquivo;
	}
	public void setDtRepasse(GregorianCalendar dtRepasse){
		this.dtRepasse=dtRepasse;
	}
	public GregorianCalendar getDtRepasse(){
		return this.dtRepasse;
	}
	public void setCdCentroCusto(int cdCentroCusto){
		this.cdCentroCusto=cdCentroCusto;
	}
	public int getCdCentroCusto(){
		return this.cdCentroCusto;
	}
	public void setDtAtualizacaoEdit(GregorianCalendar dtAtualizacaoEdit){
		this.dtAtualizacaoEdit=dtAtualizacaoEdit;
	}
	public GregorianCalendar getDtAtualizacaoEdit(){
		return this.dtAtualizacaoEdit;
	}
	public void setDtAtualizacaoEdi(GregorianCalendar dtAtualizacaoEdi){
		this.dtAtualizacaoEdi=dtAtualizacaoEdi;
	}
	public GregorianCalendar getDtAtualizacaoEdi(){
		return this.dtAtualizacaoEdi;
	}
	public void setStAtualizacaoEdi(int stAtualizacaoEdi){
		this.stAtualizacaoEdi=stAtualizacaoEdi;
	}
	public int getStAtualizacaoEdi(){
		return this.stAtualizacaoEdi;
	}
	public void setCdTribunal(int cdTribunal){
		this.cdTribunal=cdTribunal;
	}
	public int getCdTribunal(){
		return this.cdTribunal;
	}
	public void setCdJuizo(int cdJuizo){
		this.cdJuizo=cdJuizo;
	}
	public int getCdJuizo(){
		return this.cdJuizo;
	}
	public void setNrProcesso(String nrProcesso){
		this.nrProcesso=nrProcesso;
	}
	public String getNrProcesso(){
		return this.nrProcesso;
	}
	public void setCdUsuarioCadastro(int cdUsuarioCadastro){
		this.cdUsuarioCadastro=cdUsuarioCadastro;
	}
	public int getCdUsuarioCadastro(){
		return this.cdUsuarioCadastro;
	}
	public int getTpAutos() {
		return tpAutos;
	}
	public void setTpAutos(int tpAutos) {
		this.tpAutos = tpAutos;
	}
	public void setNrInterno(String nrInterno){
		this.nrInterno=nrInterno;
	}
	public String getNrInterno(){
		return this.nrInterno;
	}
	public void setCdGrupoTrabalho(int cdGrupoTrabalho){
		this.cdGrupoTrabalho=cdGrupoTrabalho;
	}
	public int getCdGrupoTrabalho(){
		return this.cdGrupoTrabalho;
	}
	public void setCdProcessoPrincipal(int cdProcessoPrincipal){
		this.cdProcessoPrincipal=cdProcessoPrincipal;
	}
	public int getCdProcessoPrincipal(){
		return this.cdProcessoPrincipal;
	}	
	public void setCdJuiz(int cdJuiz){
		this.cdJuiz=cdJuiz;
	}
	public int getCdJuiz(){
		return this.cdJuiz;
	}
	public void setCdSistemaProcesso(int cdSistemaProcesso){
		this.cdSistemaProcesso=cdSistemaProcesso;
	}
	public int getCdSistemaProcesso(){
		return this.cdSistemaProcesso;
	}
	public void setTpRito(int tpRito){
		this.tpRito=tpRito;
	}
	public int getTpRito(){
		return this.tpRito;
	}
	public void setTpRepasse(int tpRepasse){
		this.tpRepasse=tpRepasse;
	}
	public int getTpRepasse(){
		return this.tpRepasse;
	}
	public void setDtInativacao(GregorianCalendar dtInativacao){
		this.dtInativacao=dtInativacao;
	}
	public GregorianCalendar getDtInativacao(){
		return this.dtInativacao;
	}
	public void setLgImportante(int lgImportante){
		this.lgImportante=lgImportante;
	}
	public int getLgImportante(){
		return this.lgImportante;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdProcesso: " +  getCdProcesso();
		valueToString += ", cdTipoProcesso: " +  getCdTipoProcesso();
		valueToString += ", cdOrgaoJudicial: " +  getCdOrgaoJudicial();
		valueToString += ", cdComarca: " +  getCdComarca();
		valueToString += ", cdTipoSituacao: " +  getCdTipoSituacao();
		valueToString += ", cdAdvogado: " +  getCdAdvogado();
		valueToString += ", cdAdvogadoContrario: " +  getCdAdvogadoContrario();
		valueToString += ", cdOrgao: " +  getCdOrgao();
		valueToString += ", cdGrupoProcesso: " +  getCdGrupoProcesso();
		valueToString += ", lgClienteAutor: " +  getLgClienteAutor();
		valueToString += ", txtObjeto: " +  getTxtObjeto();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", dtDistribuicao: " +  sol.util.Util.formatDateTime(getDtDistribuicao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAutuacao: " +  sol.util.Util.formatDateTime(getDtAutuacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtSentenca: " +  getTxtSentenca();
		valueToString += ", dtSentenca: " +  sol.util.Util.formatDateTime(getDtSentenca(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stProcesso: " +  getStProcesso();
		valueToString += ", dtSituacao: " +  sol.util.Util.formatDateTime(getDtSituacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlProcesso: " +  getVlProcesso();
		valueToString += ", vlAcordo: " +  getVlAcordo();
		valueToString += ", lgTaxaPapel: " +  getLgTaxaPapel();
		valueToString += ", lgTaxaPaga: " +  getLgTaxaPaga();
		valueToString += ", nrAntigo: " +  getNrAntigo();
		valueToString += ", dtUltimoAndamento: " +  sol.util.Util.formatDateTime(getDtUltimoAndamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", qtMaxDias: " +  getQtMaxDias();
		valueToString += ", vlSentenca: " +  getVlSentenca();
		valueToString += ", tpInstancia: " +  getTpInstancia();
		valueToString += ", dtAtualizacao: " +  sol.util.Util.formatDateTime(getDtAtualizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpPerda: " +  getTpPerda();
		valueToString += ", cdAdvogadoTitular: " +  getCdAdvogadoTitular();
		valueToString += ", cdOficialJustica: " +  getCdOficialJustica();
		valueToString += ", cdTipoPedido: " +  getCdTipoPedido();
		valueToString += ", cdTipoObjeto: " +  getCdTipoObjeto();
		valueToString += ", cdResponsavelArquivo: " +  getCdResponsavelArquivo();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpSentenca: " +  getTpSentenca();
		valueToString += ", nrJuizo: " +  getNrJuizo();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", qtMediaDias: " +  getQtMediaDias();
		valueToString += ", dtPrazo: " +  sol.util.Util.formatDateTime(getDtPrazo(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgPrazo: " +  getLgPrazo();
		valueToString += ", nmConteiner1: " +  getNmConteiner1();
		valueToString += ", nmConteiner2: " +  getNmConteiner2();
		valueToString += ", nmConteiner3: " +  getNmConteiner3();
		valueToString += ", stLiminar: " +  getStLiminar();
		valueToString += ", stArquivo: " +  getStArquivo();
		valueToString += ", dtRepasse: " +  sol.util.Util.formatDateTime(getDtRepasse(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdCentroCusto: " +  getCdCentroCusto();
		valueToString += ", dtAtualizacaoEdit: " +  sol.util.Util.formatDateTime(getDtAtualizacaoEdit(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAtualizacaoEdi: " +  sol.util.Util.formatDateTime(getDtAtualizacaoEdi(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stAtualizacaoEdi: " +  getStAtualizacaoEdi();
		valueToString += ", cdTribunal: " +  getCdTribunal();
		valueToString += ", cdJuizo: " +  getCdJuizo();
		valueToString += ", nrProcesso: " +  getNrProcesso();
		valueToString += ", cdUsuarioCadastro: " +  getCdUsuarioCadastro();
		valueToString += ", tpAutos: " +  getTpAutos();
		valueToString += ", nrInterno: " + getNrInterno();
		valueToString += ", cdGrupoTrabalho: " + getCdGrupoTrabalho();
		valueToString += ", cdProcessoPrincipal: " + getCdProcessoPrincipal();
		valueToString += ", idProcesso: " + getIdProcesso();
		valueToString += ", cdJuiz: " + getCdJuiz();
		valueToString += ", cdSistemaProcesso: " + getCdSistemaProcesso();
		valueToString += ", tpRito: " +  getTpRito();
		valueToString += ", tpRepasse: " +  getTpRepasse();
		valueToString += ", getLgImportante: " +  getLgImportante();
		return "{" + valueToString + "}";
	}
	
	public JSONObject toJson() throws JSONException {
		String valueToString = "{\"processo\":{";
		valueToString += "\"cdProcesso\":\"" +  getCdProcesso();
		valueToString += "\",\"cdTipoProcesso\":\"" +  getCdTipoProcesso();
		valueToString += "\",\"cdOrgaoJudicia\":\"" +  getCdOrgaoJudicial();
		valueToString += "\",\"cdComarca\":\"" +  getCdComarca();
		valueToString += "\",\"cdTipoSituacao\":\"" +  getCdTipoSituacao();
		valueToString += "\",\"cdAdvogado\":\"" +  getCdAdvogado();
		valueToString += "\",\"cdAdvogadoContrario\":\"" +  getCdAdvogadoContrario();
		valueToString += "\",\"cdOrgao\":\"" +  getCdOrgao();
		valueToString += "\",\"cdGrupoProcesso\":\"" +  getCdGrupoProcesso();
		valueToString += "\",\"lgClienteAutor\":\"" +  getLgClienteAutor();
//		valueToString += "\",\"txtObjeto\":\"" +  getTxtObjeto();
//		valueToString += "\",\"txtObservacao\":\"" +  getTxtObservacao();
		valueToString += "\",\"dtDistribuicao\":\"" +  sol.util.Util.formatDateTime(getDtDistribuicao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += "\",\"dtAutuacao\":\"" +  sol.util.Util.formatDateTime(getDtAutuacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
//		valueToString += "\",\"txtSentenca\":\"" +  getTxtSentenca();
		valueToString += "\",\"dtSentenca\":\"" +  sol.util.Util.formatDateTime(getDtSentenca(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += "\",\"stProcesso\":\"" +  getStProcesso();
		valueToString += "\",\"dtSituacao\":\"" +  sol.util.Util.formatDateTime(getDtSituacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += "\",\"vlProcesso\":\"" +  getVlProcesso();
		valueToString += "\",\"vlAcordo\":\"" +  getVlAcordo();
		valueToString += "\",\"lgTaxaPapel\":\"" +  getLgTaxaPapel();
		valueToString += "\",\"lgTaxaPaga\":\"" +  getLgTaxaPaga();
		valueToString += "\",\"nrAntigo\":\"" +  getNrAntigo();
		valueToString += "\",\"dtUltimoAndamento\":\"" +  sol.util.Util.formatDateTime(getDtUltimoAndamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += "\",\"qtMaxDias\":\"" +  getQtMaxDias();
		valueToString += "\",\"vlSentenca\":\"" +  getVlSentenca();
		valueToString += "\",\"tpInstancia\":\"" +  getTpInstancia();
		valueToString += "\",\"dtAtualizacao\":\"" +  sol.util.Util.formatDateTime(getDtAtualizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += "\",\"tpPerda\":\"" +  getTpPerda();
		valueToString += "\",\"cdAdvogadoTitular\":\"" +  getCdAdvogadoTitular();
		valueToString += "\",\"cdOficialJustica\":\"" +  getCdOficialJustica();
		valueToString += "\",\"cdTipoPedido\":\"" +  getCdTipoPedido();
		valueToString += "\",\"cdTipoObjeto\":\"" +  getCdTipoObjeto();
		valueToString += "\",\"cdResponsavelArquivo\":\"" +  getCdResponsavelArquivo();
		valueToString += "\",\"dtCadastro\":\"" +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += "\",\"tpSentenca\":\"" +  getTpSentenca();
		valueToString += "\",\"nrJuizo\":\"" +  getNrJuizo();
		valueToString += "\",\"cdCidade\":\"" +  getCdCidade();
		valueToString += "\",\"qtMediaDias\":\"" +  getQtMediaDias();
		valueToString += "\",\"dtPrazo\":\"" +  sol.util.Util.formatDateTime(getDtPrazo(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += "\",\"lgPrazo\":\"" +  getLgPrazo();
		valueToString += "\",\"nmConteiner1\":\"" +  getNmConteiner1();
		valueToString += "\",\"nmConteiner2\":\"" +  getNmConteiner2();
		valueToString += "\",\"nmConteiner3\":\"" +  getNmConteiner3();
		valueToString += "\",\"stLiminar\":\"" +  getStLiminar();
		valueToString += "\",\"stArquivo\":\"" +  getStArquivo();
		valueToString += "\",\"dtRepasse\":\"" +  sol.util.Util.formatDateTime(getDtRepasse(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += "\",\"cdCentroCusto\":\"" +  getCdCentroCusto();
		valueToString += "\",\"dtAtualizacaoEdit\":\"" +  sol.util.Util.formatDateTime(getDtAtualizacaoEdit(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += "\",\"dtAtualizacaoEdi\":\"" +  sol.util.Util.formatDateTime(getDtAtualizacaoEdi(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += "\",\"stAtualizacaoEdi\":\"" +  getStAtualizacaoEdi();
		valueToString += "\",\"cdTribunal\":\"" +  getCdTribunal();
		valueToString += "\",\"cdJuizo\":\"" +  getCdJuizo();
		valueToString += "\",\"nrProcesso\":\"" +  getNrProcesso();
		valueToString += "\",\"cdUsuarioCadastro\":\"" +  getCdUsuarioCadastro();
		valueToString += "\",\"tpAutos\":\"" +  getTpAutos();
		valueToString += "\",\"nrInterno\":\"" + getNrInterno();
		valueToString += "\",\"cdGrupoTrabalho\":\"" + getCdGrupoTrabalho();
		valueToString += "\",\"cdProcessoPrincipal\":\"" + getCdProcessoPrincipal();
		valueToString += "\",\"idProcesso\":\"" + getIdProcesso();
		valueToString += "\",\"cdJuiz\":\"" + getCdJuiz();
		valueToString += "\",\"cdSistemaProcesso\":\"" + getCdSistemaProcesso();
		valueToString += "\",\"tpRito\":\"" +  getTpRito();
		valueToString += "\",\"tpRepasse\":\"" +  getTpRepasse();
		valueToString += "\",\"getLgImportante\":\"" +  getLgImportante();
		valueToString += "\"}}";
		
		return new JSONObject(valueToString); 
	}

	public Object clone() {
		return new Processo(getCdProcesso(),
			getCdTipoProcesso(),
			getCdOrgaoJudicial(),
			getCdComarca(),
			getCdTipoSituacao(),
			getCdAdvogado(),
			getCdAdvogadoContrario(),
			getCdOrgao(),
			getCdGrupoProcesso(),
			getLgClienteAutor(),
			getTxtObjeto(),
			getTxtObservacao(),
			getDtDistribuicao()==null ? null : (GregorianCalendar)getDtDistribuicao().clone(),
			getDtAutuacao()==null ? null : (GregorianCalendar)getDtAutuacao().clone(),
			getTxtSentenca(),
			getDtSentenca()==null ? null : (GregorianCalendar)getDtSentenca().clone(),
			getStProcesso(),
			getDtSituacao()==null ? null : (GregorianCalendar)getDtSituacao().clone(),
			getVlProcesso(),
			getVlAcordo(),
			getLgTaxaPapel(),
			getLgTaxaPaga(),
			getNrAntigo(),
			getDtUltimoAndamento()==null ? null : (GregorianCalendar)getDtUltimoAndamento().clone(),
			getQtMaxDias(),
			getVlSentenca(),
			getTpInstancia(),
			getDtAtualizacao()==null ? null : (GregorianCalendar)getDtAtualizacao().clone(),
			getTpPerda(),
			getCdAdvogadoTitular(),
			getCdOficialJustica(),
			getCdTipoPedido(),
			getCdTipoObjeto(),
			getCdResponsavelArquivo(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getTpSentenca(),
			getNrJuizo(),
			getCdCidade(),
			getQtMediaDias(),
			getDtPrazo()==null ? null : (GregorianCalendar)getDtPrazo().clone(),
			getLgPrazo(),
			getNmConteiner1(),
			getNmConteiner2(),
			getNmConteiner3(),
			getStLiminar(),
			getStArquivo(),
			getDtRepasse()==null ? null : (GregorianCalendar)getDtRepasse().clone(),
			getCdCentroCusto(),
			getDtAtualizacaoEdit()==null ? null : (GregorianCalendar)getDtAtualizacaoEdit().clone(),
			getDtAtualizacaoEdi()==null ? null : (GregorianCalendar)getDtAtualizacaoEdi().clone(),
			getStAtualizacaoEdi(),
			getCdTribunal(),
			getCdJuizo(),
			getNrProcesso(),
			getCdUsuarioCadastro(),
			getTpAutos(),
			getNrInterno(),
			getCdGrupoTrabalho(),
			getCdProcessoPrincipal(),
			getIdProcesso(),
			getCdJuiz(),
			getCdSistemaProcesso(),
			getTpRito(),
			getTpRepasse(),
			getDtInativacao()==null ? null : (GregorianCalendar)getDtInativacao().clone(),
			getLgImportante());
	}
	
	public String getResumo(Connection connect) {
		connect = connect==null ? Conexao.conectar() : connect;
		TipoProcesso tipo = TipoProcessoDAO.get(getCdTipoProcesso(), connect);
		return (getNrProcesso()!=null?getNrProcesso():getNrInterno())+
			   (tipo!=null ? " - "+tipo.getNmTipoProcesso() : "")+"\n"+
			   ProcessoServices.getClientes(getCdProcesso(), connect);
	}

}