package com.tivic.manager.grl;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PessoaJuridica extends Pessoa {

	private String nrCnpj;
	private String nmRazaoSocial;
	private String nrInscricaoEstadual;
	private String nrInscricaoMunicipal;
	private int nrFuncionarios;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtInicioAtividade;
	private int cdNaturezaJuridica;
	private int tpEmpresa;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtTerminoAtividade;
	private int cdNaturalidade;
	private String nrCnae;
	private String idServentia;
	private int lgDocumentoAusente;
	
	public PessoaJuridica() { }
	
	public PessoaJuridica(int cdPessoa,
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
			GregorianCalendar dtTerminoAtividade){
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
			dtChegadaPais);
		setNrCnpj(nrCnpj);
		setNmRazaoSocial(nmRazaoSocial);
		setNrInscricaoEstadual(nrInscricaoEstadual);
		setNrInscricaoMunicipal(nrInscricaoMunicipal);
		setNrFuncionarios(nrFuncionarios);
		setDtInicioAtividade(dtInicioAtividade);
		setCdNaturezaJuridica(cdNaturezaJuridica);
		setTpEmpresa(tpEmpresa);
		setDtTerminoAtividade(dtTerminoAtividade);
	}
	public PessoaJuridica(int cdPessoa,
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
			int cdNaturalidade,
			String nrCnae){
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
			dtChegadaPais);
		setNrCnpj(nrCnpj);
		setNmRazaoSocial(nmRazaoSocial);
		setNrInscricaoEstadual(nrInscricaoEstadual);
		setNrInscricaoMunicipal(nrInscricaoMunicipal);
		setNrFuncionarios(nrFuncionarios);
		setDtInicioAtividade(dtInicioAtividade);
		setCdNaturezaJuridica(cdNaturezaJuridica);
		setTpEmpresa(tpEmpresa);
		setDtTerminoAtividade(dtTerminoAtividade);
		setCdNaturalidade(cdNaturalidade);
		setNrCnae(nrCnae);
	}
	
	//Principal
	public PessoaJuridica(int cdPessoa,
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
			int cdNaturalidade,
			String nrCnae,
			String idServentia){
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
			dtChegadaPais);
		setNrCnpj(nrCnpj);
		setNmRazaoSocial(nmRazaoSocial);
		setNrInscricaoEstadual(nrInscricaoEstadual);
		setNrInscricaoMunicipal(nrInscricaoMunicipal);
		setNrFuncionarios(nrFuncionarios);
		setDtInicioAtividade(dtInicioAtividade);
		setCdNaturezaJuridica(cdNaturezaJuridica);
		setTpEmpresa(tpEmpresa);
		setDtTerminoAtividade(dtTerminoAtividade);
		setCdNaturalidade(cdNaturalidade);
		setNrCnae(nrCnae);
		setIdServentia(idServentia);
	}
	
	public PessoaJuridica(int cdPessoa,
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
			int cdUsuarioCadastro,
			String nrCnpj,
			String nmRazaoSocial,
			String nrInscricaoEstadual,
			String nrInscricaoMunicipal,
			int nrFuncionarios,
			GregorianCalendar dtInicioAtividade,
			int cdNaturezaJuridica,
			int tpEmpresa,
			GregorianCalendar dtTerminoAtividade,
			int cdNaturalidade,
			String nrCnae,
			String idServentia,
			int lgDocumentoAusente,
			String nrOab,
			String nmParceiro){
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
			cdUsuarioCadastro,
			nrOab,
			nmParceiro);
		setNrCnpj(nrCnpj);
		setNmRazaoSocial(nmRazaoSocial);
		setNrInscricaoEstadual(nrInscricaoEstadual);
		setNrInscricaoMunicipal(nrInscricaoMunicipal);
		setNrFuncionarios(nrFuncionarios);
		setDtInicioAtividade(dtInicioAtividade);
		setCdNaturezaJuridica(cdNaturezaJuridica);
		setTpEmpresa(tpEmpresa);
		setDtTerminoAtividade(dtTerminoAtividade);
		setCdNaturalidade(cdNaturalidade);
		setNrCnae(nrCnae);
		setIdServentia(idServentia);
		setLgDocumentoAusente(lgDocumentoAusente);
	}
	
	public PessoaJuridica(int cdPessoa,
			String nmPessoa,
			String nmEmail,
			GregorianCalendar dtCadastro,
			int gnPessoa,
			String nmApelido,
			String txtObservacao,
			String nrCnpj,
			String nmRazaoSocial){
		super(cdPessoa,
			nmPessoa,
			nmEmail,
			dtCadastro,
			gnPessoa,
			nmApelido,
			txtObservacao);
		setNrCnpj(nrCnpj);
		setNmRazaoSocial(nmRazaoSocial);
	}
	
	//Usado na importação do Educacenso
	public PessoaJuridica(int cdPessoa,
			String nmPessoa,
			String nrTelefone1,
			String nrTelefone2,
			String nrCelular,
			String nrFax,
			String nmEmail,
			GregorianCalendar dtCadastro,
			int gnPessoa,
			int stCadastro,
			int lgNotificacao,
			String nrCnpj,
			String nmRazaoSocial,
			int tpEmpresa){
		super(cdPessoa,
				nmPessoa,
				nrTelefone1,
				nrTelefone2,
				nrCelular,
				nrFax,
				nmEmail,
				dtCadastro,
				gnPessoa,
				stCadastro,
				lgNotificacao);
			setCdPessoa(cdPessoa);
			setNrCnpj(nrCnpj);
			setNmRazaoSocial(nmRazaoSocial);
			setTpEmpresa(tpEmpresa);
	}
	public void setNrCnpj(String nrCnpj){
		this.nrCnpj=nrCnpj;
	}
	public String getNrCnpj(){
		return this.nrCnpj;
	}
	public void setNmRazaoSocial(String nmRazaoSocial){
		this.nmRazaoSocial=nmRazaoSocial;
	}
	public String getNmRazaoSocial(){
		return this.nmRazaoSocial;
	}
	public void setNrInscricaoEstadual(String nrInscricaoEstadual){
		this.nrInscricaoEstadual=nrInscricaoEstadual;
	}
	public String getNrInscricaoEstadual(){
		return this.nrInscricaoEstadual;
	}
	public void setNrInscricaoMunicipal(String nrInscricaoMunicipal){
		this.nrInscricaoMunicipal=nrInscricaoMunicipal;
	}
	public String getNrInscricaoMunicipal(){
		return this.nrInscricaoMunicipal;
	}
	public void setNrFuncionarios(int nrFuncionarios){
		this.nrFuncionarios=nrFuncionarios;
	}
	public int getNrFuncionarios(){
		return this.nrFuncionarios;
	}
	public void setDtInicioAtividade(GregorianCalendar dtInicioAtividade){
		this.dtInicioAtividade=dtInicioAtividade;
	}
	public GregorianCalendar getDtInicioAtividade(){
		return this.dtInicioAtividade;
	}
	public void setCdNaturezaJuridica(int cdNaturezaJuridica){
		this.cdNaturezaJuridica=cdNaturezaJuridica;
	}
	public int getCdNaturezaJuridica(){
		return this.cdNaturezaJuridica;
	}
	public void setTpEmpresa(int tpEmpresa){
		this.tpEmpresa=tpEmpresa;
	}
	public int getTpEmpresa(){
		return this.tpEmpresa;
	}
	public void setDtTerminoAtividade(GregorianCalendar dtTerminoAtividade){
		this.dtTerminoAtividade=dtTerminoAtividade;
	}
	public GregorianCalendar getDtTerminoAtividade(){
		return this.dtTerminoAtividade;
	}
	public void setCdNaturalidade(int cdNaturalidade) {
		this.cdNaturalidade = cdNaturalidade;
	}
	public int getCdNaturalidade() {
		return cdNaturalidade;
	}
	public void setNrCnae(String nrCnae) {
		this.nrCnae = nrCnae;
	}
	public String getNrCnae() {
		return nrCnae;
	}
	public void setIdServentia(String idServentia) {
		this.idServentia = idServentia;
	}
	public String getIdServentia() {
		return idServentia;
	}
	public void setLgDocumentoAusente(int lgDocumentoAusente){
		this.lgDocumentoAusente=lgDocumentoAusente;
	}
	public int getLgDocumentoAusente(){
		return this.lgDocumentoAusente;
	}
	public String toString() {
		String valueToString = super.toString().replaceAll("\\{", "").replaceAll("\\}", "");
		valueToString += ", \"cdPessoa\": " +  getCdPessoa();
		valueToString += ", \"nrCnpj\": \"" +  getNrCnpj()+"\"";
		valueToString += ", \"nmRazaoSocial\": \"" +  getNmRazaoSocial()+"\"";
		valueToString += ", \"nrInscricaoEstadual\": \"" +  getNrInscricaoEstadual()+"\"";
		valueToString += ", \"nrInscricaoMunicipal\": \"" +  getNrInscricaoMunicipal()+"\"";
		valueToString += ", \"nrFuncionarios\": " +  getNrFuncionarios();
		valueToString += ", \"dtInicioAtividade\": \"" +  sol.util.Util.formatDateTime(getDtInicioAtividade(), "dd/MM/yyyy HH:mm:ss:SSS", "")+"\"";
		valueToString += ", \"cdNaturezaJuridica\": " +  getCdNaturezaJuridica();
		valueToString += ", \"tpEmpresa\": " +  getTpEmpresa();
		valueToString += ", \"dtTerminoAtividade\": \"" +  sol.util.Util.formatDateTime(getDtTerminoAtividade(), "dd/MM/yyyy HH:mm:ss:SSS", "")+"\"";
		valueToString += ", \"cdNaturalidade\": " +  getCdNaturalidade();
		valueToString += ", \"nrCnae\": \"" +  getNrCnae()+"\"";
		valueToString += ", \"idServentia\": \"" +  getIdServentia()+"\"";
		valueToString += ", \"lgDocumentoAusente\": " +getLgDocumentoAusente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaJuridica(getCdPessoa(),
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
			getCdUsuarioCadastro(),
			getNrCnpj(),
			getNmRazaoSocial(),
			getNrInscricaoEstadual(),
			getNrInscricaoMunicipal(),
			getNrFuncionarios(),
			getDtInicioAtividade()==null ? null : (GregorianCalendar)getDtInicioAtividade().clone(),
			getCdNaturezaJuridica(),
			getTpEmpresa(),
			getDtTerminoAtividade()==null ? null : (GregorianCalendar)getDtTerminoAtividade().clone(),
			getCdNaturalidade(),
			getNrCnae(),
			getIdServentia(),
			getLgDocumentoAusente(),
			getNrOab(),
			getNmParceiro());
	}

}
