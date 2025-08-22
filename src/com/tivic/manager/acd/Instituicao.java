package com.tivic.manager.acd;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.PessoaDAO;

public class Instituicao extends com.tivic.manager.grl.Empresa {

	private int cdInstituicao;
	private String nrSecretario;
	private String nrDiretor;
	private String nmDiarioOficial;
	private String nmResolucao;
	private String nmParecer;
	private int qtMinutosMatutino;
	private int qtMinutosVespertino;
	private int qtMinutosNoturno;
	private int nrVagasTeorica;
	private int nrVagasPratica;
	private float vlHoraAula;
	private int cdDiretor;
	private int cdCoordenador;
	private int cdViceDiretor;
	private int cdSecretario;
	private int cdTesoureiro;
	private int cdAdministrador;
	private String txtDiarioClasse1;
	private String txtDiarioClasse2;
	private int qtLimiteFaltas;
	private int lgRede;
	private String nrInep;
	private int cdFormulario;
	private int tpInstituicao;
	private int lgOffline;
	
	public Instituicao() { }
	
	public Instituicao(int cdPessoa,
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
			String nrSecretario,
			String nrDiretor,
			String nmDiarioOficial,
			String nmResolucao,
			String nmParecer,
			int qtMinutosMatutino,
			int qtMinutosVespertino,
			int qtMinutosNoturno,
			int nrVagasTeorica,
			int nrVagasPratica,
			float vlHoraAula,
			int cdDiretor,
			int cdCoordenador,
			int cdViceDiretor,
			int cdSecretario,
			int cdTesoureiro,
			int cdAdministrador,
			String txtDiarioClasse1,
			String txtDiarioClasse2,
			int qtLimiteFaltas,
			String nrInep){
		super(cdPessoa,
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
		setCdInstituicao(cdPessoa);
		setNrSecretario(nrSecretario);
		setNrDiretor(nrDiretor);
		setNmDiarioOficial(nmDiarioOficial);
		setNmResolucao(nmResolucao);
		setNmParecer(nmParecer);
		setQtMinutosMatutino(qtMinutosMatutino);
		setQtMinutosVespertino(qtMinutosVespertino);
		setQtMinutosNoturno(qtMinutosNoturno);
		setNrVagasTeorica(nrVagasTeorica);
		setNrVagasPratica(nrVagasPratica);
		setVlHoraAula(vlHoraAula);
		setCdDiretor(cdDiretor);
		setCdCoordenador(cdCoordenador);
		setCdViceDiretor(cdViceDiretor);
		setCdSecretario(cdSecretario);
		setCdTesoureiro(cdTesoureiro);
		setCdAdministrador(cdAdministrador);
		setTxtDiarioClasse1(txtDiarioClasse1);
		setTxtDiarioClasse2(txtDiarioClasse2);
		setQtLimiteFaltas(qtLimiteFaltas);
		setNrInep(nrInep);
	}
	
	public Instituicao(int cdPessoa,
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
			String nrSecretario,
			String nrDiretor,
			String nmDiarioOficial,
			String nmResolucao,
			String nmParecer,
			int qtMinutosMatutino,
			int qtMinutosVespertino,
			int qtMinutosNoturno,
			int nrVagasTeorica,
			int nrVagasPratica,
			float vlHoraAula,
			int cdDiretor,
			int cdCoordenador,
			int cdViceDiretor,
			int cdSecretario,
			int cdTesoureiro,
			int cdAdministrador,
			String txtDiarioClasse1,
			String txtDiarioClasse2,
			int qtLimiteFaltas,
			String nrInep,
			int cdFormulario){
		super(cdPessoa,
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
		setCdInstituicao(cdPessoa);
		setNrSecretario(nrSecretario);
		setNrDiretor(nrDiretor);
		setNmDiarioOficial(nmDiarioOficial);
		setNmResolucao(nmResolucao);
		setNmParecer(nmParecer);
		setQtMinutosMatutino(qtMinutosMatutino);
		setQtMinutosVespertino(qtMinutosVespertino);
		setQtMinutosNoturno(qtMinutosNoturno);
		setNrVagasTeorica(nrVagasTeorica);
		setNrVagasPratica(nrVagasPratica);
		setVlHoraAula(vlHoraAula);
		setCdDiretor(cdDiretor);
		setCdCoordenador(cdCoordenador);
		setCdViceDiretor(cdViceDiretor);
		setCdSecretario(cdSecretario);
		setCdTesoureiro(cdTesoureiro);
		setCdAdministrador(cdAdministrador);
		setTxtDiarioClasse1(txtDiarioClasse1);
		setTxtDiarioClasse2(txtDiarioClasse2);
		setQtLimiteFaltas(qtLimiteFaltas);
		setNrInep(nrInep);
		setCdFormulario(cdFormulario);
	}
	
	public Instituicao(int cdPessoa,
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
			String nrSecretario,
			String nrDiretor,
			String nmDiarioOficial,
			String nmResolucao,
			String nmParecer,
			int qtMinutosMatutino,
			int qtMinutosVespertino,
			int qtMinutosNoturno,
			int nrVagasTeorica,
			int nrVagasPratica,
			float vlHoraAula,
			int cdDiretor,
			int cdCoordenador,
			int cdViceDiretor,
			int cdSecretario,
			int cdTesoureiro,
			int cdAdministrador,
			String txtDiarioClasse1,
			String txtDiarioClasse2,
			int qtLimiteFaltas,
			int lgRede,
			String nrInep,
			int cdFormulario){
		super(cdPessoa,
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
		setCdInstituicao(cdPessoa);
		setNrSecretario(nrSecretario);
		setNrDiretor(nrDiretor);
		setNmDiarioOficial(nmDiarioOficial);
		setNmResolucao(nmResolucao);
		setNmParecer(nmParecer);
		setQtMinutosMatutino(qtMinutosMatutino);
		setQtMinutosVespertino(qtMinutosVespertino);
		setQtMinutosNoturno(qtMinutosNoturno);
		setNrVagasTeorica(nrVagasTeorica);
		setNrVagasPratica(nrVagasPratica);
		setVlHoraAula(vlHoraAula);
		setCdDiretor(cdDiretor);
		setCdCoordenador(cdCoordenador);
		setCdViceDiretor(cdViceDiretor);
		setCdSecretario(cdSecretario);
		setCdTesoureiro(cdTesoureiro);
		setCdAdministrador(cdAdministrador);
		setTxtDiarioClasse1(txtDiarioClasse1);
		setTxtDiarioClasse2(txtDiarioClasse2);
		setQtLimiteFaltas(qtLimiteFaltas);
		setLgRede(lgRede);
		setNrInep(nrInep);
		setCdFormulario(cdFormulario);
	}
	
	public Instituicao(int cdPessoa,
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
			String nrSecretario,
			String nrDiretor,
			String nmDiarioOficial,
			String nmResolucao,
			String nmParecer,
			int qtMinutosMatutino,
			int qtMinutosVespertino,
			int qtMinutosNoturno,
			int nrVagasTeorica,
			int nrVagasPratica,
			float vlHoraAula,
			int cdDiretor,
			int cdCoordenador,
			int cdViceDiretor,
			int cdSecretario,
			int cdTesoureiro,
			int cdAdministrador,
			String txtDiarioClasse1,
			String txtDiarioClasse2,
			int qtLimiteFaltas,
			int lgRede,
			String nrInep,
			int cdFormulario,
			int tpInstituicao){
		super(cdPessoa,
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
		setCdInstituicao(cdPessoa);
		setNrSecretario(nrSecretario);
		setNrDiretor(nrDiretor);
		setNmDiarioOficial(nmDiarioOficial);
		setNmResolucao(nmResolucao);
		setNmParecer(nmParecer);
		setQtMinutosMatutino(qtMinutosMatutino);
		setQtMinutosVespertino(qtMinutosVespertino);
		setQtMinutosNoturno(qtMinutosNoturno);
		setNrVagasTeorica(nrVagasTeorica);
		setNrVagasPratica(nrVagasPratica);
		setVlHoraAula(vlHoraAula);
		setCdDiretor(cdDiretor);
		setCdCoordenador(cdCoordenador);
		setCdViceDiretor(cdViceDiretor);
		setCdSecretario(cdSecretario);
		setCdTesoureiro(cdTesoureiro);
		setCdAdministrador(cdAdministrador);
		setTxtDiarioClasse1(txtDiarioClasse1);
		setTxtDiarioClasse2(txtDiarioClasse2);
		setQtLimiteFaltas(qtLimiteFaltas);
		setLgRede(lgRede);
		setNrInep(nrInep);
		setCdFormulario(cdFormulario);
		setTpInstituicao(tpInstituicao);
	}

	public Instituicao(int cdPessoa,
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
			String nrSecretario,
			String nrDiretor,
			String nmDiarioOficial,
			String nmResolucao,
			String nmParecer,
			int qtMinutosMatutino,
			int qtMinutosVespertino,
			int qtMinutosNoturno,
			int nrVagasTeorica,
			int nrVagasPratica,
			float vlHoraAula,
			int cdDiretor,
			int cdCoordenador,
			int cdViceDiretor,
			int cdSecretario,
			int cdTesoureiro,
			int cdAdministrador,
			String txtDiarioClasse1,
			String txtDiarioClasse2,
			int qtLimiteFaltas,
			int lgRede,
			String nrInep,
			int cdFormulario,
			int tpInstituicao,
			int lgOffline){
		super(cdPessoa,
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
		setCdInstituicao(cdPessoa);
		setNrSecretario(nrSecretario);
		setNrDiretor(nrDiretor);
		setNmDiarioOficial(nmDiarioOficial);
		setNmResolucao(nmResolucao);
		setNmParecer(nmParecer);
		setQtMinutosMatutino(qtMinutosMatutino);
		setQtMinutosVespertino(qtMinutosVespertino);
		setQtMinutosNoturno(qtMinutosNoturno);
		setNrVagasTeorica(nrVagasTeorica);
		setNrVagasPratica(nrVagasPratica);
		setVlHoraAula(vlHoraAula);
		setCdDiretor(cdDiretor);
		setCdCoordenador(cdCoordenador);
		setCdViceDiretor(cdViceDiretor);
		setCdSecretario(cdSecretario);
		setCdTesoureiro(cdTesoureiro);
		setCdAdministrador(cdAdministrador);
		setTxtDiarioClasse1(txtDiarioClasse1);
		setTxtDiarioClasse2(txtDiarioClasse2);
		setQtLimiteFaltas(qtLimiteFaltas);
		setLgRede(lgRede);
		setNrInep(nrInep);
		setCdFormulario(cdFormulario);
		setTpInstituicao(tpInstituicao);
		setLgOffline(lgOffline);
	}

	
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setNrSecretario(String nrSecretario){
		this.nrSecretario=nrSecretario;
	}
	public String getNrSecretario(){
		return this.nrSecretario;
	}
	public void setNrDiretor(String nrDiretor){
		this.nrDiretor=nrDiretor;
	}
	public String getNrDiretor(){
		return this.nrDiretor;
	}
	public void setNmDiarioOficial(String nmDiarioOficial){
		this.nmDiarioOficial=nmDiarioOficial;
	}
	public String getNmDiarioOficial(){
		return this.nmDiarioOficial;
	}
	public void setNmResolucao(String nmResolucao){
		this.nmResolucao=nmResolucao;
	}
	public String getNmResolucao(){
		return this.nmResolucao;
	}
	public void setNmParecer(String nmParecer){
		this.nmParecer=nmParecer;
	}
	public String getNmParecer(){
		return this.nmParecer;
	}
	public void setQtMinutosMatutino(int qtMinutosMatutino){
		this.qtMinutosMatutino=qtMinutosMatutino;
	}
	public int getQtMinutosMatutino(){
		return this.qtMinutosMatutino;
	}
	public void setQtMinutosVespertino(int qtMinutosVespertino){
		this.qtMinutosVespertino=qtMinutosVespertino;
	}
	public int getQtMinutosVespertino(){
		return this.qtMinutosVespertino;
	}
	public void setQtMinutosNoturno(int qtMinutosNoturno){
		this.qtMinutosNoturno=qtMinutosNoturno;
	}
	public int getQtMinutosNoturno(){
		return this.qtMinutosNoturno;
	}
	public void setNrVagasTeorica(int nrVagasTeorica){
		this.nrVagasTeorica=nrVagasTeorica;
	}
	public int getNrVagasTeorica(){
		return this.nrVagasTeorica;
	}
	public void setNrVagasPratica(int nrVagasPratica){
		this.nrVagasPratica=nrVagasPratica;
	}
	public int getNrVagasPratica(){
		return this.nrVagasPratica;
	}
	public void setVlHoraAula(float vlHoraAula){
		this.vlHoraAula=vlHoraAula;
	}
	public float getVlHoraAula(){
		return this.vlHoraAula;
	}
	public void setCdDiretor(int cdDiretor){
		this.cdDiretor=cdDiretor;
	}
	public int getCdDiretor(){
		return this.cdDiretor;
	}
	public void setCdCoordenador(int cdCoordenador){
		this.cdCoordenador=cdCoordenador;
	}
	public int getCdCoordenador(){
		return this.cdCoordenador;
	}
	public void setCdViceDiretor(int cdViceDiretor){
		this.cdViceDiretor=cdViceDiretor;
	}
	public int getCdViceDiretor(){
		return this.cdViceDiretor;
	}
	public void setCdSecretario(int cdSecretario){
		this.cdSecretario=cdSecretario;
	}
	public int getCdSecretario(){
		return this.cdSecretario;
	}
	public void setCdTesoureiro(int cdTesoureiro){
		this.cdTesoureiro=cdTesoureiro;
	}
	public int getCdTesoureiro(){
		return this.cdTesoureiro;
	}
	public void setCdAdministrador(int cdAdministrador){
		this.cdAdministrador=cdAdministrador;
	}
	public int getCdAdministrador(){
		return this.cdAdministrador;
	}
	public void setTxtDiarioClasse1(String txtDiarioClasse1){
		this.txtDiarioClasse1=txtDiarioClasse1;
	}
	public String getTxtDiarioClasse1(){
		return this.txtDiarioClasse1;
	}
	public void setTxtDiarioClasse2(String txtDiarioClasse2){
		this.txtDiarioClasse2=txtDiarioClasse2;
	}
	public String getTxtDiarioClasse2(){
		return this.txtDiarioClasse2;
	}	
	public void setQtLimiteFaltas(int qtLimiteFaltas){
		this.qtLimiteFaltas=qtLimiteFaltas;
	}
	public int getQtLimiteFaltas(){
		return this.qtLimiteFaltas;
	}
	public void setLgRede(int lgRede){
		this.lgRede=lgRede;
	}
	public int getLgRede(){
		return this.lgRede;
	}
	public void setNrInep(String nrInep) {
		this.nrInep = nrInep;
	}
	public String getNrInep() {
		return nrInep;
	}
	public void setCdFormulario(int cdFormulario) {
		this.cdFormulario = cdFormulario;
	}
	public int getCdFormulario() {
		return cdFormulario;
	}
	public void setTpInstituicao(int tpInstituicao) {
		this.tpInstituicao = tpInstituicao;
	}
	public int getTpInstituicao() {
		return tpInstituicao;
	}
	public void setLgOffline(int lgOffline) {
		this.lgOffline = lgOffline;
	}
	public int getLgOffline() {
		return lgOffline;
	}
	public String toString() {
		String valueToString = super.toString().replaceAll("\\{", "").replaceAll("\\}", "");
		valueToString += ", \"cdInstituicao\": " +  getCdInstituicao();
		valueToString += ", \"nrSecretario\": \"" +  getNrSecretario()+"\"";
		valueToString += ", \"nrDiretor\": \"" +  getNrDiretor()+"\"";
		valueToString += ", \"nmDiarioOficial\": \"" +  getNmDiarioOficial()+"\"";
		valueToString += ", \"nmResolucao\": \"" +  getNmResolucao()+"\"";
		valueToString += ", \"nmParecer\": \"" +  getNmParecer()+"\"";
		valueToString += ", \"qtMinutosMatutino\": " +  getQtMinutosMatutino();
		valueToString += ", \"qtMinutosVespertino\": " +  getQtMinutosVespertino();
		valueToString += ", \"qtMinutosNoturno\": " +  getQtMinutosNoturno();
		valueToString += ", \"nrVagasTeorica\": " +  getNrVagasTeorica();
		valueToString += ", \"nrVagasPratica\": " +  getNrVagasPratica();
		valueToString += ", \"vlHoraAula\": " +  getVlHoraAula();
		valueToString += ", \"cdDiretor\": " +  getCdDiretor();
		valueToString += ", \"cdCoordenador\": " +  getCdCoordenador();
		valueToString += ", \"cdViceDiretor\": " +  getCdViceDiretor();
		valueToString += ", \"cdSecretario\": " +  getCdSecretario();
		valueToString += ", \"cdTesoureiro\": " +  getCdTesoureiro();
		valueToString += ", \"cdAdministrador\": " +  getCdAdministrador();
		valueToString += ", \"txtDiarioClasse1\": \"" +  getTxtDiarioClasse1()+"\"";
		valueToString += ", \"txtDiarioClasse2\": \"" +  getTxtDiarioClasse2()+"\"";
		valueToString += ", \"qtLimiteFaltas\": " +  getQtLimiteFaltas();
		valueToString += ", \"lgRede\": " +  getLgRede();
		valueToString += ", \"nrInep\": \"" +  getNrInep()+"\"";
		valueToString += ", \"cdFormulario\": " +  getCdFormulario();
		valueToString += ", \"tpInstituicao\": " +  getTpInstituicao();
		valueToString += ", \"lgOffline\": " +  getLgOffline();
		return "{" + valueToString + "}";
	}
	
	public String toORM() {
		String valueToString = "";
		valueToString += "\"cdInstituicao\": " +  getCdInstituicao();
		valueToString += ", \"nrSecretario\": \"" +  getNrSecretario()+"\"";
		valueToString += ", \"nrDiretor\": \"" +  getNrDiretor()+"\"";
		valueToString += ", \"nmDiarioOficial\": \"" +  getNmDiarioOficial()+"\"";
		valueToString += ", \"nmResolucao\": \"" +  getNmResolucao()+"\"";
		valueToString += ", \"nmParecer\": \"" +  getNmParecer()+"\"";
		valueToString += ", \"qtMinutosMatutino\": " +  getQtMinutosMatutino();
		valueToString += ", \"qtMinutosVespertino\": " +  getQtMinutosVespertino();
		valueToString += ", \"qtMinutosNoturno\": " +  getQtMinutosNoturno();
		valueToString += ", \"nrVagasTeorica\": " +  getNrVagasTeorica();
		valueToString += ", \"nrVagasPratica\": " +  getNrVagasPratica();
		valueToString += ", \"vlHoraAula\": " +  getVlHoraAula();
		valueToString += ", \"cdDiretor\": " +  getCdDiretor();
		valueToString += ", \"cdCoordenador\": " +  getCdCoordenador();
		valueToString += ", \"cdViceDiretor\": " +  getCdViceDiretor();
		valueToString += ", \"cdSecretario\": " +  getCdSecretario();
		valueToString += ", \"cdTesoureiro\": " +  getCdTesoureiro();
		valueToString += ", \"cdAdministrador\": " +  getCdAdministrador();
		valueToString += ", \"txtDiarioClasse1\": \"" +  getTxtDiarioClasse1()+"\"";
		valueToString += ", \"txtDiarioClasse2\": \"" +  getTxtDiarioClasse2()+"\"";
		valueToString += ", \"qtLimiteFaltas\": " +  getQtLimiteFaltas();
		valueToString += ", \"lgRede\": " +  getLgRede();
		valueToString += ", \"nrInep\": \"" +  getNrInep()+"\"";
		valueToString += ", \"cdFormulario\": " +  getCdFormulario();
		valueToString += ", \"tpInstituicao\": " +  getTpInstituicao();
		valueToString += ", \"lgOffline\": " +  getLgOffline();
		valueToString += ", \"empresa\": " + EmpresaDAO.get(getCdPessoa()).toORM();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Instituicao(getCdEmpresa(),
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
				getNrSecretario(),
				getNrDiretor(),
				getNmDiarioOficial(),
				getNmResolucao(),
				getNmParecer(),
				getQtMinutosMatutino(),
				getQtMinutosVespertino(),
				getQtMinutosNoturno(),
				getNrVagasTeorica(),
				getNrVagasPratica(),
				getVlHoraAula(),
				getCdDiretor(),
				getCdCoordenador(),
				getCdViceDiretor(),
				getCdSecretario(),
				getCdTesoureiro(),
				getCdAdministrador(),
				getTxtDiarioClasse1(),
				getTxtDiarioClasse2(),
				getQtLimiteFaltas(),
				getLgRede(),
				getNrInep(),
				getCdFormulario(),
				getTpInstituicao(),
				getLgOffline());
	}
}
