package com.tivic.manager.srh;

import java.util.GregorianCalendar;

public class DadosFuncionais {

	private int cdMatricula;
	private int cdTabelaHorario;
	private int cdSetor;
	private int cdFuncao;
	private int cdTurma;
	private int cdEmpresa;
	private int cdGrupoPagamento;
	private int cdAgenteNocivo;
	private int cdTipoAdmissao;
	private int cdVinculoEmpregaticio;
	private int cdCategoriaFgts;
	private int cdTabelaSalario;
	private int cdPessoa;
	private int cdContaBancaria;
	private int tpSalario;
	private String nrMatricula;
	private GregorianCalendar dtMatricula;
	private GregorianCalendar dtDesligamento;
	private String nrCartao;
	private float vlPrevidenciaOutraFonte;
	private float vlSalarioContratual;
	private int qtLicencasGozadas;
	private int qtFeriasGozadas;
	private GregorianCalendar dtOpcaoFgts;
	private int stFuncional;
	private int tpStatusRais;
	private int tpProventoPrincipal;
	private String nrContaFgts;
	private int cdConvenio;
	private GregorianCalendar dtFinalContrato;
	private int tpPagamento;
	private int cdTipoDesligamento;
	private int cdBancoFgts;
	private String nrAgenciaFgts;
	private byte[] blbBiometria;
	private int qtDependenteIr;
	private int qtDependenteSalFam;
	private int lgValeTransporte;
	private int nrHorasMes;
	private int cdEstadoCtps;
	private String nrSerieCtps;
	private String nrPisPasep;
	private String nrConselhoProfissional;
	private int cdConselhoProfissional;
	private int cdNivelSalario;
	private int tpAcessoCargo;

	public DadosFuncionais() { }

	public DadosFuncionais(int cdMatricula,
			int cdTabelaHorario,
			int cdSetor,
			int cdFuncao,
			int cdTurma,
			int cdEmpresa,
			int cdGrupoPagamento,
			int cdAgenteNocivo,
			int cdTipoAdmissao,
			int cdVinculoEmpregaticio,
			int cdCategoriaFgts,
			int cdTabelaSalario,
			int cdPessoa,
			int cdContaBancaria,
			int tpSalario,
			String nrMatricula,
			GregorianCalendar dtMatricula,
			GregorianCalendar dtDesligamento,
			String nrCartao,
			float vlPrevidenciaOutraFonte,
			float vlSalarioContratual,
			int qtLicencasGozadas,
			int qtFeriasGozadas,
			GregorianCalendar dtOpcaoFgts,
			int stFuncional,
			int tpStatusRais,
			int tpProventoPrincipal,
			String nrContaFgts,
			int cdConvenio,
			GregorianCalendar dtFinalContrato,
			int tpPagamento,
			int cdTipoDesligamento,
			int cdBancoFgts,
			String nrAgenciaFgts,
			byte[] blbBiometria,
			int qtDependenteIr,
			int qtDependenteSalFam,
			int lgValeTransporte,
			int nrHorasMes,
			int cdEstadoCtps,
			String nrSerieCtps,
			String nrPisPasep,
			String nrConselhoProfissional,
			int cdConselhoProfissional,
			int cdNivelSalario,
			int tpAcessoCargo) {
		setCdMatricula(cdMatricula);
		setCdTabelaHorario(cdTabelaHorario);
		setCdSetor(cdSetor);
		setCdFuncao(cdFuncao);
		setCdTurma(cdTurma);
		setCdEmpresa(cdEmpresa);
		setCdGrupoPagamento(cdGrupoPagamento);
		setCdAgenteNocivo(cdAgenteNocivo);
		setCdTipoAdmissao(cdTipoAdmissao);
		setCdVinculoEmpregaticio(cdVinculoEmpregaticio);
		setCdCategoriaFgts(cdCategoriaFgts);
		setCdTabelaSalario(cdTabelaSalario);
		setCdPessoa(cdPessoa);
		setCdContaBancaria(cdContaBancaria);
		setTpSalario(tpSalario);
		setNrMatricula(nrMatricula);
		setDtMatricula(dtMatricula);
		setDtDesligamento(dtDesligamento);
		setNrCartao(nrCartao);
		setVlPrevidenciaOutraFonte(vlPrevidenciaOutraFonte);
		setVlSalarioContratual(vlSalarioContratual);
		setQtLicencasGozadas(qtLicencasGozadas);
		setQtFeriasGozadas(qtFeriasGozadas);
		setDtOpcaoFgts(dtOpcaoFgts);
		setStFuncional(stFuncional);
		setTpStatusRais(tpStatusRais);
		setTpProventoPrincipal(tpProventoPrincipal);
		setNrContaFgts(nrContaFgts);
		setCdConvenio(cdConvenio);
		setDtFinalContrato(dtFinalContrato);
		setTpPagamento(tpPagamento);
		setCdTipoDesligamento(cdTipoDesligamento);
		setCdBancoFgts(cdBancoFgts);
		setNrAgenciaFgts(nrAgenciaFgts);
		setBlbBiometria(blbBiometria);
		setQtDependenteIr(qtDependenteIr);
		setQtDependenteSalFam(qtDependenteSalFam);
		setLgValeTransporte(lgValeTransporte);
		setNrHorasMes(nrHorasMes);
		setCdEstadoCtps(cdEstadoCtps);
		setNrSerieCtps(nrSerieCtps);
		setNrPisPasep(nrPisPasep);
		setNrConselhoProfissional(nrConselhoProfissional);
		setCdConselhoProfissional(cdConselhoProfissional);
		setCdNivelSalario(cdNivelSalario);
		setTpAcessoCargo(tpAcessoCargo);
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdTabelaHorario(int cdTabelaHorario){
		this.cdTabelaHorario=cdTabelaHorario;
	}
	public int getCdTabelaHorario(){
		return this.cdTabelaHorario;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setCdFuncao(int cdFuncao){
		this.cdFuncao=cdFuncao;
	}
	public int getCdFuncao(){
		return this.cdFuncao;
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdGrupoPagamento(int cdGrupoPagamento){
		this.cdGrupoPagamento=cdGrupoPagamento;
	}
	public int getCdGrupoPagamento(){
		return this.cdGrupoPagamento;
	}
	public void setCdAgenteNocivo(int cdAgenteNocivo){
		this.cdAgenteNocivo=cdAgenteNocivo;
	}
	public int getCdAgenteNocivo(){
		return this.cdAgenteNocivo;
	}
	public void setCdTipoAdmissao(int cdTipoAdmissao){
		this.cdTipoAdmissao=cdTipoAdmissao;
	}
	public int getCdTipoAdmissao(){
		return this.cdTipoAdmissao;
	}
	public void setCdVinculoEmpregaticio(int cdVinculoEmpregaticio){
		this.cdVinculoEmpregaticio=cdVinculoEmpregaticio;
	}
	public int getCdVinculoEmpregaticio(){
		return this.cdVinculoEmpregaticio;
	}
	public void setCdCategoriaFgts(int cdCategoriaFgts){
		this.cdCategoriaFgts=cdCategoriaFgts;
	}
	public int getCdCategoriaFgts(){
		return this.cdCategoriaFgts;
	}
	public void setCdTabelaSalario(int cdTabelaSalario){
		this.cdTabelaSalario=cdTabelaSalario;
	}
	public int getCdTabelaSalario(){
		return this.cdTabelaSalario;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdContaBancaria(int cdContaBancaria){
		this.cdContaBancaria=cdContaBancaria;
	}
	public int getCdContaBancaria(){
		return this.cdContaBancaria;
	}
	public void setTpSalario(int tpSalario){
		this.tpSalario=tpSalario;
	}
	public int getTpSalario(){
		return this.tpSalario;
	}
	public void setNrMatricula(String nrMatricula){
		this.nrMatricula=nrMatricula;
	}
	public String getNrMatricula(){
		return this.nrMatricula;
	}
	public void setDtMatricula(GregorianCalendar dtMatricula){
		this.dtMatricula=dtMatricula;
	}
	public GregorianCalendar getDtMatricula(){
		return this.dtMatricula;
	}
	public void setDtDesligamento(GregorianCalendar dtDesligamento){
		this.dtDesligamento=dtDesligamento;
	}
	public GregorianCalendar getDtDesligamento(){
		return this.dtDesligamento;
	}
	public void setNrCartao(String nrCartao){
		this.nrCartao=nrCartao;
	}
	public String getNrCartao(){
		return this.nrCartao;
	}
	public void setVlPrevidenciaOutraFonte(float vlPrevidenciaOutraFonte){
		this.vlPrevidenciaOutraFonte=vlPrevidenciaOutraFonte;
	}
	public float getVlPrevidenciaOutraFonte(){
		return this.vlPrevidenciaOutraFonte;
	}
	public void setVlSalarioContratual(float vlSalarioContratual){
		this.vlSalarioContratual=vlSalarioContratual;
	}
	public float getVlSalarioContratual(){
		return this.vlSalarioContratual;
	}
	public void setQtLicencasGozadas(int qtLicencasGozadas){
		this.qtLicencasGozadas=qtLicencasGozadas;
	}
	public int getQtLicencasGozadas(){
		return this.qtLicencasGozadas;
	}
	public void setQtFeriasGozadas(int qtFeriasGozadas){
		this.qtFeriasGozadas=qtFeriasGozadas;
	}
	public int getQtFeriasGozadas(){
		return this.qtFeriasGozadas;
	}
	public void setDtOpcaoFgts(GregorianCalendar dtOpcaoFgts){
		this.dtOpcaoFgts=dtOpcaoFgts;
	}
	public GregorianCalendar getDtOpcaoFgts(){
		return this.dtOpcaoFgts;
	}
	public void setStFuncional(int stFuncional){
		this.stFuncional=stFuncional;
	}
	public int getStFuncional(){
		return this.stFuncional;
	}
	public void setTpStatusRais(int tpStatusRais){
		this.tpStatusRais=tpStatusRais;
	}
	public int getTpStatusRais(){
		return this.tpStatusRais;
	}
	public void setTpProventoPrincipal(int tpProventoPrincipal){
		this.tpProventoPrincipal=tpProventoPrincipal;
	}
	public int getTpProventoPrincipal(){
		return this.tpProventoPrincipal;
	}
	public void setNrContaFgts(String nrContaFgts){
		this.nrContaFgts=nrContaFgts;
	}
	public String getNrContaFgts(){
		return this.nrContaFgts;
	}
	public void setCdConvenio(int cdConvenio){
		this.cdConvenio=cdConvenio;
	}
	public int getCdConvenio(){
		return this.cdConvenio;
	}
	public void setDtFinalContrato(GregorianCalendar dtFinalContrato){
		this.dtFinalContrato=dtFinalContrato;
	}
	public GregorianCalendar getDtFinalContrato(){
		return this.dtFinalContrato;
	}
	public void setTpPagamento(int tpPagamento){
		this.tpPagamento=tpPagamento;
	}
	public int getTpPagamento(){
		return this.tpPagamento;
	}
	public void setCdTipoDesligamento(int cdTipoDesligamento){
		this.cdTipoDesligamento=cdTipoDesligamento;
	}
	public int getCdTipoDesligamento(){
		return this.cdTipoDesligamento;
	}
	public void setCdBancoFgts(int cdBancoFgts){
		this.cdBancoFgts=cdBancoFgts;
	}
	public int getCdBancoFgts(){
		return this.cdBancoFgts;
	}
	public void setNrAgenciaFgts(String nrAgenciaFgts){
		this.nrAgenciaFgts=nrAgenciaFgts;
	}
	public String getNrAgenciaFgts(){
		return this.nrAgenciaFgts;
	}
	public void setBlbBiometria(byte[] blbBiometria){
		this.blbBiometria=blbBiometria;
	}
	public byte[] getBlbBiometria(){
		return this.blbBiometria;
	}
	public void setQtDependenteIr(int qtDependenteIr){
		this.qtDependenteIr=qtDependenteIr;
	}
	public int getQtDependenteIr(){
		return this.qtDependenteIr;
	}
	public void setQtDependenteSalFam(int qtDependenteSalFam){
		this.qtDependenteSalFam=qtDependenteSalFam;
	}
	public int getQtDependenteSalFam(){
		return this.qtDependenteSalFam;
	}
	public void setLgValeTransporte(int lgValeTransporte){
		this.lgValeTransporte=lgValeTransporte;
	}
	public int getLgValeTransporte(){
		return this.lgValeTransporte;
	}
	public void setNrHorasMes(int nrHorasMes){
		this.nrHorasMes=nrHorasMes;
	}
	public int getNrHorasMes(){
		return this.nrHorasMes;
	}
	public void setCdEstadoCtps(int cdEstadoCtps){
		this.cdEstadoCtps=cdEstadoCtps;
	}
	public int getCdEstadoCtps(){
		return this.cdEstadoCtps;
	}
	public void setNrSerieCtps(String nrSerieCtps){
		this.nrSerieCtps=nrSerieCtps;
	}
	public String getNrSerieCtps(){
		return this.nrSerieCtps;
	}
	public void setNrPisPasep(String nrPisPasep){
		this.nrPisPasep=nrPisPasep;
	}
	public String getNrPisPasep(){
		return this.nrPisPasep;
	}
	public void setNrConselhoProfissional(String nrConselhoProfissional){
		this.nrConselhoProfissional=nrConselhoProfissional;
	}
	public String getNrConselhoProfissional(){
		return this.nrConselhoProfissional;
	}
	public void setCdConselhoProfissional(int cdConselhoProfissional){
		this.cdConselhoProfissional=cdConselhoProfissional;
	}
	public int getCdConselhoProfissional(){
		return this.cdConselhoProfissional;
	}
	public void setCdNivelSalario(int cdNivelSalario){
		this.cdNivelSalario=cdNivelSalario;
	}
	public int getCdNivelSalario(){
		return this.cdNivelSalario;
	}
	public void setTpAcessoCargo(int tpAcessoCargo){
		this.tpAcessoCargo=tpAcessoCargo;
	}
	public int getTpAcessoCargo(){
		return this.tpAcessoCargo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatricula: " +  getCdMatricula();
		valueToString += ", cdTabelaHorario: " +  getCdTabelaHorario();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", cdFuncao: " +  getCdFuncao();
		valueToString += ", cdTurma: " +  getCdTurma();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdGrupoPagamento: " +  getCdGrupoPagamento();
		valueToString += ", cdAgenteNocivo: " +  getCdAgenteNocivo();
		valueToString += ", cdTipoAdmissao: " +  getCdTipoAdmissao();
		valueToString += ", cdVinculoEmpregaticio: " +  getCdVinculoEmpregaticio();
		valueToString += ", cdCategoriaFgts: " +  getCdCategoriaFgts();
		valueToString += ", cdTabelaSalario: " +  getCdTabelaSalario();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdContaBancaria: " +  getCdContaBancaria();
		valueToString += ", tpSalario: " +  getTpSalario();
		valueToString += ", nrMatricula: " +  getNrMatricula();
		valueToString += ", dtMatricula: " +  sol.util.Util.formatDateTime(getDtMatricula(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtDesligamento: " +  sol.util.Util.formatDateTime(getDtDesligamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrCartao: " +  getNrCartao();
		valueToString += ", vlPrevidenciaOutraFonte: " +  getVlPrevidenciaOutraFonte();
		valueToString += ", vlSalarioContratual: " +  getVlSalarioContratual();
		valueToString += ", qtLicencasGozadas: " +  getQtLicencasGozadas();
		valueToString += ", qtFeriasGozadas: " +  getQtFeriasGozadas();
		valueToString += ", dtOpcaoFgts: " +  sol.util.Util.formatDateTime(getDtOpcaoFgts(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stFuncional: " +  getStFuncional();
		valueToString += ", tpStatusRais: " +  getTpStatusRais();
		valueToString += ", tpProventoPrincipal: " +  getTpProventoPrincipal();
		valueToString += ", nrContaFgts: " +  getNrContaFgts();
		valueToString += ", cdConvenio: " +  getCdConvenio();
		valueToString += ", dtFinalContrato: " +  sol.util.Util.formatDateTime(getDtFinalContrato(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpPagamento: " +  getTpPagamento();
		valueToString += ", cdTipoDesligamento: " +  getCdTipoDesligamento();
		valueToString += ", cdBancoFgts: " +  getCdBancoFgts();
		valueToString += ", nrAgenciaFgts: " +  getNrAgenciaFgts();
		valueToString += ", blbBiometria: " +  getBlbBiometria();
		valueToString += ", qtDependenteIr: " +  getQtDependenteIr();
		valueToString += ", qtDependenteSalFam: " +  getQtDependenteSalFam();
		valueToString += ", lgValeTransporte: " +  getLgValeTransporte();
		valueToString += ", nrHorasMes: " +  getNrHorasMes();
		valueToString += ", cdEstadoCtps: " +  getCdEstadoCtps();
		valueToString += ", nrSerieCtps: " +  getNrSerieCtps();
		valueToString += ", nrPisPasep: " +  getNrPisPasep();
		valueToString += ", nrConselhoProfissional: " +  getNrConselhoProfissional();
		valueToString += ", cdConselhoProfissional: " +  getCdConselhoProfissional();
		valueToString += ", cdNivelSalario: " +  getCdNivelSalario();
		valueToString += ", tpAcessoCargo: " +  getTpAcessoCargo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DadosFuncionais(getCdMatricula(),
			getCdTabelaHorario(),
			getCdSetor(),
			getCdFuncao(),
			getCdTurma(),
			getCdEmpresa(),
			getCdGrupoPagamento(),
			getCdAgenteNocivo(),
			getCdTipoAdmissao(),
			getCdVinculoEmpregaticio(),
			getCdCategoriaFgts(),
			getCdTabelaSalario(),
			getCdPessoa(),
			getCdContaBancaria(),
			getTpSalario(),
			getNrMatricula(),
			getDtMatricula()==null ? null : (GregorianCalendar)getDtMatricula().clone(),
			getDtDesligamento()==null ? null : (GregorianCalendar)getDtDesligamento().clone(),
			getNrCartao(),
			getVlPrevidenciaOutraFonte(),
			getVlSalarioContratual(),
			getQtLicencasGozadas(),
			getQtFeriasGozadas(),
			getDtOpcaoFgts()==null ? null : (GregorianCalendar)getDtOpcaoFgts().clone(),
			getStFuncional(),
			getTpStatusRais(),
			getTpProventoPrincipal(),
			getNrContaFgts(),
			getCdConvenio(),
			getDtFinalContrato()==null ? null : (GregorianCalendar)getDtFinalContrato().clone(),
			getTpPagamento(),
			getCdTipoDesligamento(),
			getCdBancoFgts(),
			getNrAgenciaFgts(),
			getBlbBiometria(),
			getQtDependenteIr(),
			getQtDependenteSalFam(),
			getLgValeTransporte(),
			getNrHorasMes(),
			getCdEstadoCtps(),
			getNrSerieCtps(),
			getNrPisPasep(),
			getNrConselhoProfissional(),
			getCdConselhoProfissional(),
			getCdNivelSalario(),
			getTpAcessoCargo());
	}

}