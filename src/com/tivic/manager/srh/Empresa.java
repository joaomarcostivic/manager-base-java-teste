package com.tivic.manager.srh;

import java.util.GregorianCalendar;

public class Empresa extends com.tivic.manager.grl.Empresa {

	private int cdFpas;
	private int cdTerceiros;
	private int cdTabelaEvento;
	private int cdFolhaPagamento;
	private String idFgts;
	private String idGps;
	private String idSureg;
	private String idSat;
	private String idCei;
	private float prSat;
	private float prIsencaoFilantropia;
	private float prAnuidade;
	private float prProlaboreGps;
	private int nrMesDissidioColetivo;
	private int nrMesAdiantamentoDecimo;
	private int nrMesAntecipacaoDecimo;
	private int qtAnosIntervaloLicenca;
	private int qtMesesLicencaPremio;
	private int lgPat;
	private int lgRecolhePis;
	private int lgCagedDisco;
	private int lgRecolheGrps;
	private int lgVerificaVaga;
	private int lgCalculaAdicionalTempo;
	private int lgDependenteInformado;
	private int qtAnosAnuidade;
	private int tpDeducaoFalta;
	private int tpCategoriaFgts;
	private int tpCalculoFerias;
	private int tpPagamentoFerias;
	private int tpAdiantamentoDecimo;
	private float vlArredondamento;
	private String nmDepartamentoRh;
	private String nmChefeDepartamento;

	public Empresa(int cdEmpresa,
			int cdPessoaSuperior,
			int cdPais,
			String nmPessoa,
			String nrTelefone1,
			String nrTelefone2,
			String nrCelular,
			String nrFax,
			String nmEmail,
			GregorianCalendar dtCadastro,
			int gnPessoa,
			byte[] imgFoto,
			int stCadastro,
			String nmUrl,
			String nmApelido,
			String txtObservacao,
			int lgNotificacao,
			String idPessoa,
			int cdClassificacao,
			int cdFormaDivulgacao,
			GregorianCalendar dtChegadaPais,
			String nrCnpj,
			String nmRazaoSocial,
			String nrInscricaoEstadual,
			String nrInscricaoMunicipal,
			int nrFuncionarios,
			GregorianCalendar dtInicioAtividade,
			int cdNaturezaJuridica,
			int tpEmpresa,
			GregorianCalendar dtTerminoAtividade,
			int lgMatriz,
			byte[] imgLogomarca,
			String idEmpresa,
			int cdTabelaCatEconomica,
			int cdFpas,
			int cdTerceiros,
			int cdTabelaEvento,
			int cdFolhaPagamento,
			String idFgts,
			String idGps,
			String idSureg,
			String idSat,
			String idCei,
			float prSat,
			float prIsencaoFilantropia,
			float prAnuidade,
			float prProlaboreGps,
			int nrMesDissidioColetivo,
			int nrMesAdiantamentoDecimo,
			int nrMesAntecipacaoDecimo,
			int qtAnosIntervaloLicenca,
			int qtMesesLicencaPremio,
			int lgPat,
			int lgRecolhePis,
			int lgCagedDisco,
			int lgRecolheGrps,
			int lgVerificaVaga,
			int lgCalculaAdicionalTempo,
			int lgDependenteInformado,
			int qtAnosAnuidade,
			int tpDeducaoFalta,
			int tpCategoriaFgts,
			int tpCalculoFerias,
			int tpPagamentoFerias,
			int tpAdiantamentoDecimo,
			float vlArredondamento,
			String nmDepartamentoRh,
			String nmChefeDepartamento){
		super(cdEmpresa,
				cdPessoaSuperior,
				cdPais,
				nmPessoa,
				nrTelefone1,
				nrTelefone2,
				nrCelular,
				nrFax,
				nmEmail,
				dtCadastro,
				gnPessoa,
				imgFoto,
				stCadastro,
				nmUrl,
				nmApelido,
				txtObservacao,
				lgNotificacao,
				idPessoa,
				cdClassificacao,
				cdFormaDivulgacao,
				dtChegadaPais,
				nrCnpj,
				nmRazaoSocial,
				nrInscricaoEstadual,
				nrInscricaoMunicipal,
				nrFuncionarios,
				dtInicioAtividade,
				cdNaturezaJuridica,
				tpEmpresa,
				dtTerminoAtividade,
				lgMatriz,
				imgLogomarca,
				idEmpresa, 
				cdTabelaCatEconomica);
		setCdFpas(cdFpas);
		setCdTerceiros(cdTerceiros);
		setCdTabelaEvento(cdTabelaEvento);
		setCdFolhaPagamento(cdFolhaPagamento);
		setIdFgts(idFgts);
		setIdGps(idGps);
		setIdSureg(idSureg);
		setIdSat(idSat);
		setIdCei(idCei);
		setPrSat(prSat);
		setPrIsencaoFilantropia(prIsencaoFilantropia);
		setPrAnuidade(prAnuidade);
		setPrProlaboreGps(prProlaboreGps);
		setNrMesDissidioColetivo(nrMesDissidioColetivo);
		setNrMesAdiantamentoDecimo(nrMesAdiantamentoDecimo);
		setNrMesAntecipacaoDecimo(nrMesAntecipacaoDecimo);
		setQtAnosIntervaloLicenca(qtAnosIntervaloLicenca);
		setQtMesesLicencaPremio(qtMesesLicencaPremio);
		setLgPat(lgPat);
		setLgRecolhePis(lgRecolhePis);
		setLgCagedDisco(lgCagedDisco);
		setLgRecolheGrps(lgRecolheGrps);
		setLgVerificaVaga(lgVerificaVaga);
		setLgCalculaAdicionalTempo(lgCalculaAdicionalTempo);
		setLgDependenteInformado(lgDependenteInformado);
		setQtAnosAnuidade(qtAnosAnuidade);
		setTpDeducaoFalta(tpDeducaoFalta);
		setTpCategoriaFgts(tpCategoriaFgts);
		setTpCalculoFerias(tpCalculoFerias);
		setTpPagamentoFerias(tpPagamentoFerias);
		setTpAdiantamentoDecimo(tpAdiantamentoDecimo);
		setVlArredondamento(vlArredondamento);
		setNmDepartamentoRh(nmDepartamentoRh);
		setNmChefeDepartamento(nmChefeDepartamento);
	}
	public void setCdFpas(int cdFpas){
		this.cdFpas=cdFpas;
	}
	public int getCdFpas(){
		return this.cdFpas;
	}
	public void setCdTerceiros(int cdTerceiros){
		this.cdTerceiros=cdTerceiros;
	}
	public int getCdTerceiros(){
		return this.cdTerceiros;
	}
	public void setCdTabelaEvento(int cdTabelaEvento){
		this.cdTabelaEvento=cdTabelaEvento;
	}
	public int getCdTabelaEvento(){
		return this.cdTabelaEvento;
	}
	public void setCdFolhaPagamento(int cdFolhaPagamento){
		this.cdFolhaPagamento=cdFolhaPagamento;
	}
	public int getCdFolhaPagamento(){
		return this.cdFolhaPagamento;
	}
	public void setIdFgts(String idFgts){
		this.idFgts=idFgts;
	}
	public String getIdFgts(){
		return this.idFgts;
	}
	public void setIdGps(String idGps){
		this.idGps=idGps;
	}
	public String getIdGps(){
		return this.idGps;
	}
	public void setIdSureg(String idSureg){
		this.idSureg=idSureg;
	}
	public String getIdSureg(){
		return this.idSureg;
	}
	public void setIdSat(String idSat){
		this.idSat=idSat;
	}
	public String getIdSat(){
		return this.idSat;
	}
	public void setIdCei(String idCei){
		this.idCei=idCei;
	}
	public String getIdCei(){
		return this.idCei;
	}
	public void setPrSat(float prSat){
		this.prSat=prSat;
	}
	public float getPrSat(){
		return this.prSat;
	}
	public void setPrIsencaoFilantropia(float prIsencaoFilantropia){
		this.prIsencaoFilantropia=prIsencaoFilantropia;
	}
	public float getPrIsencaoFilantropia(){
		return this.prIsencaoFilantropia;
	}
	public void setPrAnuidade(float prAnuidade){
		this.prAnuidade=prAnuidade;
	}
	public float getPrAnuidade(){
		return this.prAnuidade;
	}
	public void setPrProlaboreGps(float prProlaboreGps){
		this.prProlaboreGps=prProlaboreGps;
	}
	public float getPrProlaboreGps(){
		return this.prProlaboreGps;
	}
	public void setNrMesDissidioColetivo(int nrMesDissidioColetivo){
		this.nrMesDissidioColetivo=nrMesDissidioColetivo;
	}
	public int getNrMesDissidioColetivo(){
		return this.nrMesDissidioColetivo;
	}
	public void setNrMesAdiantamentoDecimo(int nrMesAdiantamentoDecimo){
		this.nrMesAdiantamentoDecimo=nrMesAdiantamentoDecimo;
	}
	public int getNrMesAdiantamentoDecimo(){
		return this.nrMesAdiantamentoDecimo;
	}
	public void setNrMesAntecipacaoDecimo(int nrMesAntecipacaoDecimo){
		this.nrMesAntecipacaoDecimo=nrMesAntecipacaoDecimo;
	}
	public int getNrMesAntecipacaoDecimo(){
		return this.nrMesAntecipacaoDecimo;
	}
	public void setQtAnosIntervaloLicenca(int qtAnosIntervaloLicenca){
		this.qtAnosIntervaloLicenca=qtAnosIntervaloLicenca;
	}
	public int getQtAnosIntervaloLicenca(){
		return this.qtAnosIntervaloLicenca;
	}
	public void setQtMesesLicencaPremio(int qtMesesLicencaPremio){
		this.qtMesesLicencaPremio=qtMesesLicencaPremio;
	}
	public int getQtMesesLicencaPremio(){
		return this.qtMesesLicencaPremio;
	}
	public void setLgPat(int lgPat){
		this.lgPat=lgPat;
	}
	public int getLgPat(){
		return this.lgPat;
	}
	public void setLgRecolhePis(int lgRecolhePis){
		this.lgRecolhePis=lgRecolhePis;
	}
	public int getLgRecolhePis(){
		return this.lgRecolhePis;
	}
	public void setLgCagedDisco(int lgCagedDisco){
		this.lgCagedDisco=lgCagedDisco;
	}
	public int getLgCagedDisco(){
		return this.lgCagedDisco;
	}
	public void setLgRecolheGrps(int lgRecolheGrps){
		this.lgRecolheGrps=lgRecolheGrps;
	}
	public int getLgRecolheGrps(){
		return this.lgRecolheGrps;
	}
	public void setLgVerificaVaga(int lgVerificaVaga){
		this.lgVerificaVaga=lgVerificaVaga;
	}
	public int getLgVerificaVaga(){
		return this.lgVerificaVaga;
	}
	public void setLgCalculaAdicionalTempo(int lgCalculaAdicionalTempo){
		this.lgCalculaAdicionalTempo=lgCalculaAdicionalTempo;
	}
	public int getLgCalculaAdicionalTempo(){
		return this.lgCalculaAdicionalTempo;
	}
	public void setLgDependenteInformado(int lgDependenteInformado){
		this.lgDependenteInformado=lgDependenteInformado;
	}
	public int getLgDependenteInformado(){
		return this.lgDependenteInformado;
	}
	public void setQtAnosAnuidade(int qtAnosAnuidade){
		this.qtAnosAnuidade=qtAnosAnuidade;
	}
	public int getQtAnosAnuidade(){
		return this.qtAnosAnuidade;
	}
	public void setTpDeducaoFalta(int tpDeducaoFalta){
		this.tpDeducaoFalta=tpDeducaoFalta;
	}
	public int getTpDeducaoFalta(){
		return this.tpDeducaoFalta;
	}
	public void setTpCategoriaFgts(int tpCategoriaFgts){
		this.tpCategoriaFgts=tpCategoriaFgts;
	}
	public int getTpCategoriaFgts(){
		return this.tpCategoriaFgts;
	}
	public void setTpCalculoFerias(int tpCalculoFerias){
		this.tpCalculoFerias=tpCalculoFerias;
	}
	public int getTpCalculoFerias(){
		return this.tpCalculoFerias;
	}
	public void setTpPagamentoFerias(int tpPagamentoFerias){
		this.tpPagamentoFerias=tpPagamentoFerias;
	}
	public int getTpPagamentoFerias(){
		return this.tpPagamentoFerias;
	}
	public void setTpAdiantamentoDecimo(int tpAdiantamentoDecimo){
		this.tpAdiantamentoDecimo=tpAdiantamentoDecimo;
	}
	public int getTpAdiantamentoDecimo(){
		return this.tpAdiantamentoDecimo;
	}
	public void setVlArredondamento(float vlArredondamento){
		this.vlArredondamento=vlArredondamento;
	}
	public float getVlArredondamento(){
		return this.vlArredondamento;
	}
	public void setNmDepartamentoRh(String nmDepartamentoRh){
		this.nmDepartamentoRh=nmDepartamentoRh;
	}
	public String getNmDepartamentoRh(){
		return this.nmDepartamentoRh;
	}
	public void setNmChefeDepartamento(String nmChefeDepartamento){
		this.nmChefeDepartamento=nmChefeDepartamento;
	}
	public String getNmChefeDepartamento(){
		return this.nmChefeDepartamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdFpas: " +  getCdFpas();
		valueToString += ", cdTerceiros: " +  getCdTerceiros();
		valueToString += ", cdTabelaEvento: " +  getCdTabelaEvento();
		valueToString += ", cdFolhaPagamento: " +  getCdFolhaPagamento();
		valueToString += ", idFgts: " +  getIdFgts();
		valueToString += ", idGps: " +  getIdGps();
		valueToString += ", idSureg: " +  getIdSureg();
		valueToString += ", idSat: " +  getIdSat();
		valueToString += ", idCei: " +  getIdCei();
		valueToString += ", prSat: " +  getPrSat();
		valueToString += ", prIsencaoFilantropia: " +  getPrIsencaoFilantropia();
		valueToString += ", prAnuidade: " +  getPrAnuidade();
		valueToString += ", prProlaboreGps: " +  getPrProlaboreGps();
		valueToString += ", nrMesDissidioColetivo: " +  getNrMesDissidioColetivo();
		valueToString += ", nrMesAdiantamentoDecimo: " +  getNrMesAdiantamentoDecimo();
		valueToString += ", nrMesAntecipacaoDecimo: " +  getNrMesAntecipacaoDecimo();
		valueToString += ", qtAnosIntervaloLicenca: " +  getQtAnosIntervaloLicenca();
		valueToString += ", qtMesesLicencaPremio: " +  getQtMesesLicencaPremio();
		valueToString += ", lgPat: " +  getLgPat();
		valueToString += ", lgRecolhePis: " +  getLgRecolhePis();
		valueToString += ", lgCagedDisco: " +  getLgCagedDisco();
		valueToString += ", lgRecolheGrps: " +  getLgRecolheGrps();
		valueToString += ", lgVerificaVaga: " +  getLgVerificaVaga();
		valueToString += ", lgCalculaAdicionalTempo: " +  getLgCalculaAdicionalTempo();
		valueToString += ", lgDependenteInformado: " +  getLgDependenteInformado();
		valueToString += ", qtAnosAnuidade: " +  getQtAnosAnuidade();
		valueToString += ", tpDeducaoFalta: " +  getTpDeducaoFalta();
		valueToString += ", tpCategoriaFgts: " +  getTpCategoriaFgts();
		valueToString += ", tpCalculoFerias: " +  getTpCalculoFerias();
		valueToString += ", tpPagamentoFerias: " +  getTpPagamentoFerias();
		valueToString += ", tpAdiantamentoDecimo: " +  getTpAdiantamentoDecimo();
		valueToString += ", vlArredondamento: " +  getVlArredondamento();
		valueToString += ", nmDepartamentoRh: " +  getNmDepartamentoRh();
		valueToString += ", nmChefeDepartamento: " +  getNmChefeDepartamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Empresa(getCdPessoa(),
			getCdPessoaSuperior(),
			getCdPais(),
			getNmPessoa(),
			getNrTelefone1(),
			getNrTelefone2(),
			getNrCelular(),
			getNrFax(),
			getNmEmail(),
			getDtCadastro(),
			getGnPessoa(),
			getImgFoto(),
			getStCadastro(),
			getNmUrl(),
			getNmApelido(),
			getTxtObservacao(),
			getLgNotificacao(),
			getIdPessoa(),
			getCdClassificacao(),
			getCdFormaDivulgacao(),
			getDtChegadaPais(),
			getNrCnpj(),
			getNmRazaoSocial(),
			getNrInscricaoEstadual(),
			getNrInscricaoMunicipal(),
			getNrFuncionarios(),
			getDtInicioAtividade()==null ? null : (GregorianCalendar)getDtInicioAtividade().clone(),
			getCdNaturezaJuridica(),
			getTpEmpresa(),
			getDtTerminoAtividade()==null ? null : (GregorianCalendar)getDtTerminoAtividade().clone(),
			getLgMatriz(),
			getImgLogomarca(),
			getIdEmpresa(),
			getCdTabelaCatEconomica(),
			getCdFpas(),
			getCdTerceiros(),
			getCdTabelaEvento(),
			getCdFolhaPagamento(),
			getIdFgts(),
			getIdGps(),
			getIdSureg(),
			getIdSat(),
			getIdCei(),
			getPrSat(),
			getPrIsencaoFilantropia(),
			getPrAnuidade(),
			getPrProlaboreGps(),
			getNrMesDissidioColetivo(),
			getNrMesAdiantamentoDecimo(),
			getNrMesAntecipacaoDecimo(),
			getQtAnosIntervaloLicenca(),
			getQtMesesLicencaPremio(),
			getLgPat(),
			getLgRecolhePis(),
			getLgCagedDisco(),
			getLgRecolheGrps(),
			getLgVerificaVaga(),
			getLgCalculaAdicionalTempo(),
			getLgDependenteInformado(),
			getQtAnosAnuidade(),
			getTpDeducaoFalta(),
			getTpCategoriaFgts(),
			getTpCalculoFerias(),
			getTpPagamentoFerias(),
			getTpAdiantamentoDecimo(),
			getVlArredondamento(),
			getNmDepartamentoRh(),
			getNmChefeDepartamento());
	}

}
