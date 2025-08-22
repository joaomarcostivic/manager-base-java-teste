package com.tivic.manager.acd;

public class InstituicaoEducacenso {

	private int cdInstituicao;
	private String nrInep;
	private String nrOrgaoRegional;
	private String nmOrgaoRegional;
	private int tpDependenciaAdministrativa;
	private int tpLocalizacao;
	private String nrCnpjExecutora;
	private int tpCategoriaPrivada;
	private int tpConvenio;
	private String nrCnas;
	private String nrCebas;
	private int stRegulamentacao;
	private int lgPredioCompartilhado;
	private int stAguaConsumida;
	private int tpAbastecimentoAgua;
	private int tpFornecimentoEnergia;
	private int tpEsgotoSanitario;
	private int tpDestinoLixo;
	private int qtSalaAula;
	private int qtSalaAulaExterna;
	private int qtComputadorAdministrativo;
	private int qtComputadorAluno;
	private int lgInternet;
	private int lgBandaLarga;
	private int lgAlimentacaoEscolar;
	private int tpAtendimentoEspecializado;
	private int tpAtividadeComplementar;
	private int tpModalidadeEnsino;
	private int lgEnsinoFundamentalCiclo;
	private int tpEducacaoInfantil;
	private int nrAnosEnsinoFundalmental;
	private int tpEnsinoMedio;
	private int tpEducacaoJovemAdulto;
	private int tpLocalizacaoDiferenciada;
	private int tpMaterialEspecifico;
	private int lgEducacaoIndigena;
	private int tpLingua;
	private String nrLinguaIndigena;
	private int stInstituicaoPublica;
	private int lgMaterialDidaticoIndigena;
	private int lgMaterialDidaticoQuilombola;
	private int lgBrasilAlfabetizado;
	private int lgEspacoComunidade;
	private int lgFormacaoAlternancia;
	private int tpFormaOcupacao;
	private int lgModalidadeRegular;
	private int lgModalidadeEspecial;
	private int lgModalidadeEja;
	private int cdLinguaIndigena;
	private int lgLinguaMinistradaIndigena;
	private int lgLinguaMinistradaPortuguesa;
	private int cdPeriodoLetivo;
	private int qtTotalFuncionarios;
	private String nrCodigoEscolaCompartilhada1;
	private String nrCodigoEscolaCompartilhada2;
	private String nrCodigoEscolaCompartilhada3;
	private String nrCodigoEscolaCompartilhada4;
	private String nrCodigoEscolaCompartilhada5;
	private String nrCodigoEscolaCompartilhada6;
	private int qtSalaAulaClimatizada;
	private int qtSalaAulaAcessibilidade;
	private int lgSite;
	private int lgEntorno;
	private int lgProjetoPoliticoPedagogico;
	
	public InstituicaoEducacenso() { }

	//Usada na importação do Educacenso
	public InstituicaoEducacenso(int cdInstituicao,
			String nrInep,
			String nrOrgaoRegional,
			int tpDependenciaAdministrativa,
			int tpLocalizacao,
			String nrCnpjExecutora,
			int tpCategoriaPrivada,
			int tpConvenio,
			String nrCnas,
			String nrCebas,
			int stRegulamentacao,
			int stInstituicaoPublica){
		setCdInstituicao(cdInstituicao);
		setNrInep(nrInep);
		setNrOrgaoRegional(nrOrgaoRegional);
		setTpDependenciaAdministrativa(tpDependenciaAdministrativa);
		setTpLocalizacao(tpLocalizacao);
		setNrCnpjExecutora(nrCnpjExecutora);
		setTpCategoriaPrivada(tpCategoriaPrivada);
		setTpConvenio(tpConvenio);
		setNrCnas(nrCnas);
		setNrCebas(nrCebas);
		setStRegulamentacao(stRegulamentacao);
		setStInstituicaoPublica(stInstituicaoPublica);
	}
	
	
	public InstituicaoEducacenso(int cdInstituicao,
			String nrInep,
			String nrOrgaoRegional,
			String nmOrgaoRegional,
			int tpDependenciaAdministrativa,
			int tpLocalizacao,
			String nrCnpjExecutora,
			int tpCategoriaPrivada,
			int tpConvenio,
			String nrCnas,
			String nrCebas,
			int stRegulamentacao,
			int lgPredioCompartilhado,
			int stAguaConsumida,
			int tpAbastecimentoAgua,
			int tpFornecimentoEnergia,
			int tpEsgotoSanitario,
			int tpDestinoLixo,
			int qtSalaAula,
			int qtSalaAulaExterna,
			int qtComputadorAdministrativo,
			int qtComputadorAluno,
			int lgInternet,
			int lgBandaLarga,
			int lgAlimentacaoEscolar,
			int tpAtendimentoEspecializado,
			int tpAtividadeComplementar,
			int tpModalidadeEnsino,
			int lgEnsinoFundamentalCiclo,
			int tpEducacaoInfantil,
			int nrAnosEnsinoFundalmental,
			int tpEnsinoMedio,
			int tpEducacaoJovemAdulto,
			int tpLocalizacaoDiferenciada,
			int tpMaterialEspecifico,
			int lgEducacaoIndigena,
			int tpLingua,
			String nrLinguaIndigena,
			int stInstituicaoPublica,
			int lgMaterialDidaticoIndigena,
			int lgMaterialDidaticoQuilombola,
			int lgBrasilAlfabetizado,
			int lgEspacoComunidade,
			int lgFormacaoAlternancia,
			int tpFormaOcupacao,
			int lgModalidadeRegular,
			int lgModalidadeEspecial,
			int lgModalidadeEja,
			int cdLinguaIndigena,
			int lgLinguaMinistradaIndigena,
			int lgLinguaMinistradaPortuguesa,
			int cdPeriodoLetivo,
			int qtTotalFuncionarios,
			String nrCodigoEscolaCompartilhada1,
			String nrCodigoEscolaCompartilhada2,
			String nrCodigoEscolaCompartilhada3,
			String nrCodigoEscolaCompartilhada4,
			String nrCodigoEscolaCompartilhada5,
			String nrCodigoEscolaCompartilhada6,
			int qtSalaAulaClimatizada,
			int qtSalaAulaAcessibilidade,
			int lgSite,
			int lgEntorno,
			int lgProjetoPoliticoPedagogico) {
		setCdInstituicao(cdInstituicao);
		setNrInep(nrInep);
		setNrOrgaoRegional(nrOrgaoRegional);
		setNmOrgaoRegional(nmOrgaoRegional);
		setTpDependenciaAdministrativa(tpDependenciaAdministrativa);
		setTpLocalizacao(tpLocalizacao);
		setNrCnpjExecutora(nrCnpjExecutora);
		setTpCategoriaPrivada(tpCategoriaPrivada);
		setTpConvenio(tpConvenio);
		setNrCnas(nrCnas);
		setNrCebas(nrCebas);
		setStRegulamentacao(stRegulamentacao);
		setLgPredioCompartilhado(lgPredioCompartilhado);
		setStAguaConsumida(stAguaConsumida);
		setTpAbastecimentoAgua(tpAbastecimentoAgua);
		setTpFornecimentoEnergia(tpFornecimentoEnergia);
		setTpEsgotoSanitario(tpEsgotoSanitario);
		setTpDestinoLixo(tpDestinoLixo);
		setQtSalaAula(qtSalaAula);
		setQtSalaAulaExterna(qtSalaAulaExterna);
		setQtComputadorAdministrativo(qtComputadorAdministrativo);
		setQtComputadorAluno(qtComputadorAluno);
		setLgInternet(lgInternet);
		setLgBandaLarga(lgBandaLarga);
		setLgAlimentacaoEscolar(lgAlimentacaoEscolar);
		setTpAtendimentoEspecializado(tpAtendimentoEspecializado);
		setTpAtividadeComplementar(tpAtividadeComplementar);
		setTpModalidadeEnsino(tpModalidadeEnsino);
		setLgEnsinoFundamentalCiclo(lgEnsinoFundamentalCiclo);
		setTpEducacaoInfantil(tpEducacaoInfantil);
		setNrAnosEnsinoFundalmental(nrAnosEnsinoFundalmental);
		setTpEnsinoMedio(tpEnsinoMedio);
		setTpEducacaoJovemAdulto(tpEducacaoJovemAdulto);
		setTpLocalizacaoDiferenciada(tpLocalizacaoDiferenciada);
		setTpMaterialEspecifico(tpMaterialEspecifico);
		setLgEducacaoIndigena(lgEducacaoIndigena);
		setTpLingua(tpLingua);
		setNrLinguaIndigena(nrLinguaIndigena);
		setStInstituicaoPublica(stInstituicaoPublica);
		setLgMaterialDidaticoIndigena(lgMaterialDidaticoIndigena);
		setLgMaterialDidaticoQuilombola(lgMaterialDidaticoQuilombola);
		setLgBrasilAlfabetizado(lgBrasilAlfabetizado);
		setLgEspacoComunidade(lgEspacoComunidade);
		setLgFormacaoAlternancia(lgFormacaoAlternancia);
		setTpFormaOcupacao(tpFormaOcupacao);
		setLgModalidadeRegular(lgModalidadeRegular);
		setLgModalidadeEspecial(lgModalidadeEspecial);
		setLgModalidadeEja(lgModalidadeEja);
		setCdLinguaIndigena(cdLinguaIndigena);
		setLgLinguaMinistradaIndigena(lgLinguaMinistradaIndigena);
		setLgLinguaMinistradaPortuguesa(lgLinguaMinistradaPortuguesa);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setQtTotalFuncionarios(qtTotalFuncionarios);
		setNrCodigoEscolaCompartilhada1(nrCodigoEscolaCompartilhada1);
		setNrCodigoEscolaCompartilhada2(nrCodigoEscolaCompartilhada2);
		setNrCodigoEscolaCompartilhada3(nrCodigoEscolaCompartilhada3);
		setNrCodigoEscolaCompartilhada4(nrCodigoEscolaCompartilhada4);
		setNrCodigoEscolaCompartilhada5(nrCodigoEscolaCompartilhada5);
		setNrCodigoEscolaCompartilhada6(nrCodigoEscolaCompartilhada6);	
		setQtSalaAulaClimatizada(qtSalaAulaClimatizada);
		setQtSalaAulaAcessibilidade(qtSalaAulaAcessibilidade);
		setLgSite(lgSite);
		setLgEntorno(lgEntorno);
		setLgProjetoPoliticoPedagogico(lgProjetoPoliticoPedagogico);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setNrInep(String nrInep){
		this.nrInep=nrInep;
	}
	public String getNrInep(){
		return this.nrInep;
	}
	public void setNrOrgaoRegional(String nrOrgaoRegional){
		this.nrOrgaoRegional=nrOrgaoRegional;
	}
	public String getNrOrgaoRegional(){
		return this.nrOrgaoRegional;
	}
	public void setNmOrgaoRegional(String nmOrgaoRegional){
		this.nmOrgaoRegional=nmOrgaoRegional;
	}
	public String getNmOrgaoRegional(){
		return this.nmOrgaoRegional;
	}
	public void setTpDependenciaAdministrativa(int tpDependenciaAdministrativa){
		this.tpDependenciaAdministrativa=tpDependenciaAdministrativa;
	}
	public int getTpDependenciaAdministrativa(){
		return this.tpDependenciaAdministrativa;
	}
	public void setTpLocalizacao(int tpLocalizacao){
		this.tpLocalizacao=tpLocalizacao;
	}
	public int getTpLocalizacao(){
		return this.tpLocalizacao;
	}
	public void setNrCnpjExecutora(String nrCnpjExecutora){
		this.nrCnpjExecutora=nrCnpjExecutora;
	}
	public String getNrCnpjExecutora(){
		return this.nrCnpjExecutora;
	}
	public void setTpCategoriaPrivada(int tpCategoriaPrivada){
		this.tpCategoriaPrivada=tpCategoriaPrivada;
	}
	public int getTpCategoriaPrivada(){
		return this.tpCategoriaPrivada;
	}
	public void setTpConvenio(int tpConvenio){
		this.tpConvenio=tpConvenio;
	}
	public int getTpConvenio(){
		return this.tpConvenio;
	}
	public void setNrCnas(String nrCnas){
		this.nrCnas=nrCnas;
	}
	public String getNrCnas(){
		return this.nrCnas;
	}
	public void setNrCebas(String nrCebas){
		this.nrCebas=nrCebas;
	}
	public String getNrCebas(){
		return this.nrCebas;
	}
	public void setStRegulamentacao(int stRegulamentacao){
		this.stRegulamentacao=stRegulamentacao;
	}
	public int getStRegulamentacao(){
		return this.stRegulamentacao;
	}
	public void setLgPredioCompartilhado(int lgPredioCompartilhado){
		this.lgPredioCompartilhado=lgPredioCompartilhado;
	}
	public int getLgPredioCompartilhado(){
		return this.lgPredioCompartilhado;
	}
	public void setStAguaConsumida(int stAguaConsumida){
		this.stAguaConsumida=stAguaConsumida;
	}
	public int getStAguaConsumida(){
		return this.stAguaConsumida;
	}
	public void setTpAbastecimentoAgua(int tpAbastecimentoAgua){
		this.tpAbastecimentoAgua=tpAbastecimentoAgua;
	}
	public int getTpAbastecimentoAgua(){
		return this.tpAbastecimentoAgua;
	}
	public void setTpFornecimentoEnergia(int tpFornecimentoEnergia){
		this.tpFornecimentoEnergia=tpFornecimentoEnergia;
	}
	public int getTpFornecimentoEnergia(){
		return this.tpFornecimentoEnergia;
	}
	public void setTpEsgotoSanitario(int tpEsgotoSanitario){
		this.tpEsgotoSanitario=tpEsgotoSanitario;
	}
	public int getTpEsgotoSanitario(){
		return this.tpEsgotoSanitario;
	}
	public void setTpDestinoLixo(int tpDestinoLixo){
		this.tpDestinoLixo=tpDestinoLixo;
	}
	public int getTpDestinoLixo(){
		return this.tpDestinoLixo;
	}
	public void setQtSalaAula(int qtSalaAula){
		this.qtSalaAula=qtSalaAula;
	}
	public int getQtSalaAula(){
		return this.qtSalaAula;
	}
	public void setQtSalaAulaExterna(int qtSalaAulaExterna){
		this.qtSalaAulaExterna=qtSalaAulaExterna;
	}
	public int getQtSalaAulaExterna(){
		return this.qtSalaAulaExterna;
	}
	public void setQtComputadorAdministrativo(int qtComputadorAdministrativo){
		this.qtComputadorAdministrativo=qtComputadorAdministrativo;
	}
	public int getQtComputadorAdministrativo(){
		return this.qtComputadorAdministrativo;
	}
	public void setQtComputadorAluno(int qtComputadorAluno){
		this.qtComputadorAluno=qtComputadorAluno;
	}
	public int getQtComputadorAluno(){
		return this.qtComputadorAluno;
	}
	public void setLgInternet(int lgInternet){
		this.lgInternet=lgInternet;
	}
	public int getLgInternet(){
		return this.lgInternet;
	}
	public void setLgBandaLarga(int lgBandaLarga){
		this.lgBandaLarga=lgBandaLarga;
	}
	public int getLgBandaLarga(){
		return this.lgBandaLarga;
	}
	public void setLgAlimentacaoEscolar(int lgAlimentacaoEscolar){
		this.lgAlimentacaoEscolar=lgAlimentacaoEscolar;
	}
	public int getLgAlimentacaoEscolar(){
		return this.lgAlimentacaoEscolar;
	}
	public void setTpAtendimentoEspecializado(int tpAtendimentoEspecializado){
		this.tpAtendimentoEspecializado=tpAtendimentoEspecializado;
	}
	public int getTpAtendimentoEspecializado(){
		return this.tpAtendimentoEspecializado;
	}
	public void setTpAtividadeComplementar(int tpAtividadeComplementar){
		this.tpAtividadeComplementar=tpAtividadeComplementar;
	}
	public int getTpAtividadeComplementar(){
		return this.tpAtividadeComplementar;
	}
	public void setTpModalidadeEnsino(int tpModalidadeEnsino){
		this.tpModalidadeEnsino=tpModalidadeEnsino;
	}
	public int getTpModalidadeEnsino(){
		return this.tpModalidadeEnsino;
	}
	public void setLgEnsinoFundamentalCiclo(int lgEnsinoFundamentalCiclo){
		this.lgEnsinoFundamentalCiclo=lgEnsinoFundamentalCiclo;
	}
	public int getLgEnsinoFundamentalCiclo(){
		return this.lgEnsinoFundamentalCiclo;
	}
	public void setTpEducacaoInfantil(int tpEducacaoInfantil){
		this.tpEducacaoInfantil=tpEducacaoInfantil;
	}
	public int getTpEducacaoInfantil(){
		return this.tpEducacaoInfantil;
	}
	public void setNrAnosEnsinoFundalmental(int nrAnosEnsinoFundalmental){
		this.nrAnosEnsinoFundalmental=nrAnosEnsinoFundalmental;
	}
	public int getNrAnosEnsinoFundalmental(){
		return this.nrAnosEnsinoFundalmental;
	}
	public void setTpEnsinoMedio(int tpEnsinoMedio){
		this.tpEnsinoMedio=tpEnsinoMedio;
	}
	public int getTpEnsinoMedio(){
		return this.tpEnsinoMedio;
	}
	public void setTpEducacaoJovemAdulto(int tpEducacaoJovemAdulto){
		this.tpEducacaoJovemAdulto=tpEducacaoJovemAdulto;
	}
	public int getTpEducacaoJovemAdulto(){
		return this.tpEducacaoJovemAdulto;
	}
	public void setTpLocalizacaoDiferenciada(int tpLocalizacaoDiferenciada){
		this.tpLocalizacaoDiferenciada=tpLocalizacaoDiferenciada;
	}
	public int getTpLocalizacaoDiferenciada(){
		return this.tpLocalizacaoDiferenciada;
	}
	public void setTpMaterialEspecifico(int tpMaterialEspecifico){
		this.tpMaterialEspecifico=tpMaterialEspecifico;
	}
	public int getTpMaterialEspecifico(){
		return this.tpMaterialEspecifico;
	}
	public void setLgEducacaoIndigena(int lgEducacaoIndigena){
		this.lgEducacaoIndigena=lgEducacaoIndigena;
	}
	public int getLgEducacaoIndigena(){
		return this.lgEducacaoIndigena;
	}
	public void setTpLingua(int tpLingua){
		this.tpLingua=tpLingua;
	}
	public int getTpLingua(){
		return this.tpLingua;
	}
	public void setNrLinguaIndigena(String nrLinguaIndigena){
		this.nrLinguaIndigena=nrLinguaIndigena;
	}
	public String getNrLinguaIndigena(){
		return this.nrLinguaIndigena;
	}
	public void setStInstituicaoPublica(int stInstituicaoPublica){
		this.stInstituicaoPublica=stInstituicaoPublica;
	}
	public int getStInstituicaoPublica(){
		return this.stInstituicaoPublica;
	}
	public void setLgMaterialDidaticoIndigena(int lgMaterialDidaticoIndigena){
		this.lgMaterialDidaticoIndigena=lgMaterialDidaticoIndigena;
	}
	public int getLgMaterialDidaticoIndigena(){
		return this.lgMaterialDidaticoIndigena;
	}
	public void setLgMaterialDidaticoQuilombola(int lgMaterialDidaticoQuilombola){
		this.lgMaterialDidaticoQuilombola=lgMaterialDidaticoQuilombola;
	}
	public int getLgMaterialDidaticoQuilombola(){
		return this.lgMaterialDidaticoQuilombola;
	}
	public void setLgBrasilAlfabetizado(int lgBrasilAlfabetizado){
		this.lgBrasilAlfabetizado=lgBrasilAlfabetizado;
	}
	public int getLgBrasilAlfabetizado(){
		return this.lgBrasilAlfabetizado;
	}
	public void setLgEspacoComunidade(int lgEspacoComunidade){
		this.lgEspacoComunidade=lgEspacoComunidade;
	}
	public int getLgEspacoComunidade(){
		return this.lgEspacoComunidade;
	}
	public void setLgFormacaoAlternancia(int lgFormacaoAlternancia){
		this.lgFormacaoAlternancia=lgFormacaoAlternancia;
	}
	public int getLgFormacaoAlternancia(){
		return this.lgFormacaoAlternancia;
	}
	public void setTpFormaOcupacao(int tpFormaOcupacao){
		this.tpFormaOcupacao=tpFormaOcupacao;
	}
	public int getTpFormaOcupacao(){
		return this.tpFormaOcupacao;
	}
	public void setLgModalidadeRegular(int lgModalidadeRegular){
		this.lgModalidadeRegular=lgModalidadeRegular;
	}
	public int getLgModalidadeRegular(){
		return this.lgModalidadeRegular;
	}
	public void setLgModalidadeEspecial(int lgModalidadeEspecial){
		this.lgModalidadeEspecial=lgModalidadeEspecial;
	}
	public int getLgModalidadeEspecial(){
		return this.lgModalidadeEspecial;
	}
	public void setLgModalidadeEja(int lgModalidadeEja){
		this.lgModalidadeEja=lgModalidadeEja;
	}
	public int getLgModalidadeEja(){
		return this.lgModalidadeEja;
	}
	public void setCdLinguaIndigena(int cdLinguaIndigena){
		this.cdLinguaIndigena=cdLinguaIndigena;
	}
	public int getCdLinguaIndigena(){
		return this.cdLinguaIndigena;
	}
	public void setLgLinguaMinistradaIndigena(int lgLinguaMinistradaIndigena){
		this.lgLinguaMinistradaIndigena=lgLinguaMinistradaIndigena;
	}
	public int getLgLinguaMinistradaIndigena(){
		return this.lgLinguaMinistradaIndigena;
	}
	public void setLgLinguaMinistradaPortuguesa(int lgLinguaMinistradaPortuguesa){
		this.lgLinguaMinistradaPortuguesa=lgLinguaMinistradaPortuguesa;
	}
	public int getLgLinguaMinistradaPortuguesa(){
		return this.lgLinguaMinistradaPortuguesa;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setQtTotalFuncionarios(int qtTotalFuncionarios){
		this.qtTotalFuncionarios=qtTotalFuncionarios;
	}
	public int getQtTotalFuncionarios(){
		return this.qtTotalFuncionarios;
	}
	public void setNrCodigoEscolaCompartilhada1(
			String nrCodigoEscolaCompartilhada1) {
		this.nrCodigoEscolaCompartilhada1 = nrCodigoEscolaCompartilhada1;
	}
	public String getNrCodigoEscolaCompartilhada1() {
		return nrCodigoEscolaCompartilhada1;
	}
	public void setNrCodigoEscolaCompartilhada2(
			String nrCodigoEscolaCompartilhada2) {
		this.nrCodigoEscolaCompartilhada2 = nrCodigoEscolaCompartilhada2;
	}
	public String getNrCodigoEscolaCompartilhada2() {
		return nrCodigoEscolaCompartilhada2;
	}
	public void setNrCodigoEscolaCompartilhada3(
			String nrCodigoEscolaCompartilhada3) {
		this.nrCodigoEscolaCompartilhada3 = nrCodigoEscolaCompartilhada3;
	}
	public String getNrCodigoEscolaCompartilhada3() {
		return nrCodigoEscolaCompartilhada3;
	}
	public void setNrCodigoEscolaCompartilhada4(
			String nrCodigoEscolaCompartilhada4) {
		this.nrCodigoEscolaCompartilhada4 = nrCodigoEscolaCompartilhada4;
	}
	public String getNrCodigoEscolaCompartilhada4() {
		return nrCodigoEscolaCompartilhada4;
	}
	public void setNrCodigoEscolaCompartilhada5(
			String nrCodigoEscolaCompartilhada5) {
		this.nrCodigoEscolaCompartilhada5 = nrCodigoEscolaCompartilhada5;
	}
	public String getNrCodigoEscolaCompartilhada5() {
		return nrCodigoEscolaCompartilhada5;
	}
	public void setNrCodigoEscolaCompartilhada6(
			String nrCodigoEscolaCompartilhada6) {
		this.nrCodigoEscolaCompartilhada6 = nrCodigoEscolaCompartilhada6;
	}
	public String getNrCodigoEscolaCompartilhada6() {
		return nrCodigoEscolaCompartilhada6;
	}
	public void setQtSalaAulaClimatizada(int qtSalaAulaClimatizada) {
		this.qtSalaAulaClimatizada = qtSalaAulaClimatizada;
	}
	public int getQtSalaAulaClimatizada() {
		return qtSalaAulaClimatizada;
	}
	public void setQtSalaAulaAcessibilidade(int qtSalaAulaAcessibilidade) {
		this.qtSalaAulaAcessibilidade = qtSalaAulaAcessibilidade;
	}
	public int getQtSalaAulaAcessibilidade() {
		return qtSalaAulaAcessibilidade;
	}
	public void setLgSite(int lgSite) {
		this.lgSite = lgSite;
	}
	public int getLgSite() {
		return lgSite;
	}
	public void setLgEntorno(int lgEntorno) {
		this.lgEntorno = lgEntorno;
	}
	public int getLgEntorno() {
		return lgEntorno;
	}
	public void setLgProjetoPoliticoPedagogico(int lgProjetoPoliticoPedagogico) {
		this.lgProjetoPoliticoPedagogico = lgProjetoPoliticoPedagogico;
	}
	public int getLgProjetoPoliticoPedagogico() {
		return lgProjetoPoliticoPedagogico;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInstituicao: " +  getCdInstituicao();
		valueToString += ", nrInep: " +  getNrInep();
		valueToString += ", nrOrgaoRegional: " +  getNrOrgaoRegional();
		valueToString += ", nmOrgaoRegional: " +  getNmOrgaoRegional();
		valueToString += ", tpDependenciaAdministrativa: " +  getTpDependenciaAdministrativa();
		valueToString += ", tpLocalizacao: " +  getTpLocalizacao();
		valueToString += ", nrCnpjExecutora: " +  getNrCnpjExecutora();
		valueToString += ", tpCategoriaPrivada: " +  getTpCategoriaPrivada();
		valueToString += ", tpConvenio: " +  getTpConvenio();
		valueToString += ", nrCnas: " +  getNrCnas();
		valueToString += ", nrCebas: " +  getNrCebas();
		valueToString += ", stRegulamentacao: " +  getStRegulamentacao();
		valueToString += ", lgPredioCompartilhado: " +  getLgPredioCompartilhado();
		valueToString += ", stAguaConsumida: " +  getStAguaConsumida();
		valueToString += ", tpAbastecimentoAgua: " +  getTpAbastecimentoAgua();
		valueToString += ", tpFornecimentoEnergia: " +  getTpFornecimentoEnergia();
		valueToString += ", tpEsgotoSanitario: " +  getTpEsgotoSanitario();
		valueToString += ", tpDestinoLixo: " +  getTpDestinoLixo();
		valueToString += ", qtSalaAula: " +  getQtSalaAula();
		valueToString += ", qtSalaAulaExterna: " +  getQtSalaAulaExterna();
		valueToString += ", qtComputadorAdministrativo: " +  getQtComputadorAdministrativo();
		valueToString += ", qtComputadorAluno: " +  getQtComputadorAluno();
		valueToString += ", lgInternet: " +  getLgInternet();
		valueToString += ", lgBandaLarga: " +  getLgBandaLarga();
		valueToString += ", lgAlimentacaoEscolar: " +  getLgAlimentacaoEscolar();
		valueToString += ", tpAtendimentoEspecializado: " +  getTpAtendimentoEspecializado();
		valueToString += ", tpAtividadeComplementar: " +  getTpAtividadeComplementar();
		valueToString += ", tpModalidadeEnsino: " +  getTpModalidadeEnsino();
		valueToString += ", lgEnsinoFundamentalCiclo: " +  getLgEnsinoFundamentalCiclo();
		valueToString += ", tpEducacaoInfantil: " +  getTpEducacaoInfantil();
		valueToString += ", nrAnosEnsinoFundalmental: " +  getNrAnosEnsinoFundalmental();
		valueToString += ", tpEnsinoMedio: " +  getTpEnsinoMedio();
		valueToString += ", tpEducacaoJovemAdulto: " +  getTpEducacaoJovemAdulto();
		valueToString += ", tpLocalizacaoDiferenciada: " +  getTpLocalizacaoDiferenciada();
		valueToString += ", tpMaterialEspecifico: " +  getTpMaterialEspecifico();
		valueToString += ", lgEducacaoIndigena: " +  getLgEducacaoIndigena();
		valueToString += ", tpLingua: " +  getTpLingua();
		valueToString += ", nrLinguaIndigena: " +  getNrLinguaIndigena();
		valueToString += ", stInstituicaoPublica: " +  getStInstituicaoPublica();
		valueToString += ", lgMaterialDidaticoIndigena: " +  getLgMaterialDidaticoIndigena();
		valueToString += ", lgMaterialDidaticoQuilombola: " +  getLgMaterialDidaticoQuilombola();
		valueToString += ", lgBrasilAlfabetizado: " +  getLgBrasilAlfabetizado();
		valueToString += ", lgEspacoComunidade: " +  getLgEspacoComunidade();
		valueToString += ", lgFormacaoAlternancia: " +  getLgFormacaoAlternancia();
		valueToString += ", tpFormaOcupacao: " +  getTpFormaOcupacao();
		valueToString += ", lgModalidadeRegular: " +  getLgModalidadeRegular();
		valueToString += ", lgModalidadeEspecial: " +  getLgModalidadeEspecial();
		valueToString += ", lgModalidadeEja: " +  getLgModalidadeEja();
		valueToString += ", cdLinguaIndigena: " +  getCdLinguaIndigena();
		valueToString += ", lgLinguaMinistradaIndigena: " +  getLgLinguaMinistradaIndigena();
		valueToString += ", lgLinguaMinistradaPortuguesa: " +  getLgLinguaMinistradaPortuguesa();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		valueToString += ", qtTotalFuncionarios: " +  getQtTotalFuncionarios();
		valueToString += ", qtSalaAulaClimatizada: " +  getQtSalaAulaClimatizada();
		valueToString += ", qtSalaAulaAcessibilidade: " +  getQtSalaAulaAcessibilidade();
		valueToString += ", lgSite: " +  getLgSite();
		valueToString += ", lgEntorno: " +  getLgEntorno();
		valueToString += ", lgProjetoPoliticoPedagogico: " +  getLgProjetoPoliticoPedagogico();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoEducacenso(getCdInstituicao(),
			getNrInep(),
			getNrOrgaoRegional(),
			getNmOrgaoRegional(),
			getTpDependenciaAdministrativa(),
			getTpLocalizacao(),
			getNrCnpjExecutora(),
			getTpCategoriaPrivada(),
			getTpConvenio(),
			getNrCnas(),
			getNrCebas(),
			getStRegulamentacao(),
			getLgPredioCompartilhado(),
			getStAguaConsumida(),
			getTpAbastecimentoAgua(),
			getTpFornecimentoEnergia(),
			getTpEsgotoSanitario(),
			getTpDestinoLixo(),
			getQtSalaAula(),
			getQtSalaAulaExterna(),
			getQtComputadorAdministrativo(),
			getQtComputadorAluno(),
			getLgInternet(),
			getLgBandaLarga(),
			getLgAlimentacaoEscolar(),
			getTpAtendimentoEspecializado(),
			getTpAtividadeComplementar(),
			getTpModalidadeEnsino(),
			getLgEnsinoFundamentalCiclo(),
			getTpEducacaoInfantil(),
			getNrAnosEnsinoFundalmental(),
			getTpEnsinoMedio(),
			getTpEducacaoJovemAdulto(),
			getTpLocalizacaoDiferenciada(),
			getTpMaterialEspecifico(),
			getLgEducacaoIndigena(),
			getTpLingua(),
			getNrLinguaIndigena(),
			getStInstituicaoPublica(),
			getLgMaterialDidaticoIndigena(),
			getLgMaterialDidaticoQuilombola(),
			getLgBrasilAlfabetizado(),
			getLgEspacoComunidade(),
			getLgFormacaoAlternancia(),
			getTpFormaOcupacao(),
			getLgModalidadeRegular(),
			getLgModalidadeEspecial(),
			getLgModalidadeEja(),
			getCdLinguaIndigena(),
			getLgLinguaMinistradaIndigena(),
			getLgLinguaMinistradaPortuguesa(),
			getCdPeriodoLetivo(),
			getQtTotalFuncionarios(),
			getNrCodigoEscolaCompartilhada1(),
			getNrCodigoEscolaCompartilhada2(),
			getNrCodigoEscolaCompartilhada3(),
			getNrCodigoEscolaCompartilhada4(),
			getNrCodigoEscolaCompartilhada5(),
			getNrCodigoEscolaCompartilhada6(),
			getQtSalaAulaClimatizada(),
			getQtSalaAulaAcessibilidade(),
			getLgSite(),
			getLgEntorno(),
			getLgProjetoPoliticoPedagogico());
	}

}