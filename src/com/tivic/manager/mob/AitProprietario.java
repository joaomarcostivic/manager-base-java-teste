package com.tivic.manager.mob;

public class AitProprietario {

	private int cdAitProprietario;
	private int cdAit;
	private String nmProprietario;
	private String dsLogradouro;
	private String dsNrImovel;
	private String nrCep;
	private String nrDdd;
	private String nrTelefone;
	private String nrCpfProprietario;
	private int tpPessoaProprietario;
	private String nrCpfCnpjProprietario;
	private String nmComplemento;
	private int cdEndereco;
	private int cdProprietario;
	private String nmProprietarioAutuacao;
	private int cdPessoa;

	public AitProprietario(){ }

	public AitProprietario(int cdAitProprietario,
			int cdAit,
			String nmProprietario,
			String dsLogradouro,
			String dsNrImovel,
			String nrCep,
			String nrDdd,
			String nrTelefone,
			String nrCpfProprietario,
			int tpPessoaProprietario,
			String nrCpfCnpjProprietario,
			String nmComplemento,
			int cdEndereco,
			int cdProprietario,
			String nmProprietarioAutuacao,
			int cdPessoa){
		setCdAitProprietario(cdAitProprietario);
		setCdAit(cdAit);
		setNmProprietario(nmProprietario);
		setDsLogradouro(dsLogradouro);
		setDsNrImovel(dsNrImovel);
		setNrCep(nrCep);
		setNrDdd(nrDdd);
		setNrTelefone(nrTelefone);
		setNrCpfProprietario(nrCpfProprietario);
		setTpPessoaProprietario(tpPessoaProprietario);
		setNrCpfCnpjProprietario(nrCpfCnpjProprietario);
		setNmComplemento(nmComplemento);
		setCdEndereco(cdEndereco);
		setCdProprietario(cdProprietario);
		setNmProprietarioAutuacao(nmProprietarioAutuacao);
		setCdPessoa(cdPessoa);
	}
	public void setCdAitProprietario(int cdAitProprietario){
		this.cdAitProprietario=cdAitProprietario;
	}
	public int getCdAitProprietario(){
		return this.cdAitProprietario;
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setNmProprietario(String nmProprietario){
		this.nmProprietario=nmProprietario;
	}
	public String getNmProprietario(){
		return this.nmProprietario;
	}
	public void setDsLogradouro(String dsLogradouro){
		this.dsLogradouro=dsLogradouro;
	}
	public String getDsLogradouro(){
		return this.dsLogradouro;
	}
	public void setDsNrImovel(String dsNrImovel){
		this.dsNrImovel=dsNrImovel;
	}
	public String getDsNrImovel(){
		return this.dsNrImovel;
	}
	public void setNrCep(String nrCep){
		this.nrCep=nrCep;
	}
	public String getNrCep(){
		return this.nrCep;
	}
	public void setNrDdd(String nrDdd){
		this.nrDdd=nrDdd;
	}
	public String getNrDdd(){
		return this.nrDdd;
	}
	public void setNrTelefone(String nrTelefone){
		this.nrTelefone=nrTelefone;
	}
	public String getNrTelefone(){
		return this.nrTelefone;
	}
	public void setNrCpfProprietario(String nrCpfProprietario){
		this.nrCpfProprietario=nrCpfProprietario;
	}
	public String getNrCpfProprietario(){
		return this.nrCpfProprietario;
	}
	public void setTpPessoaProprietario(int tpPessoaProprietario){
		this.tpPessoaProprietario=tpPessoaProprietario;
	}
	public int getTpPessoaProprietario(){
		return this.tpPessoaProprietario;
	}
	public void setNrCpfCnpjProprietario(String nrCpfCnpjProprietario){
		this.nrCpfCnpjProprietario=nrCpfCnpjProprietario;
	}
	public String getNrCpfCnpjProprietario(){
		return this.nrCpfCnpjProprietario;
	}
	public void setNmComplemento(String nmComplemento){
		this.nmComplemento=nmComplemento;
	}
	public String getNmComplemento(){
		return this.nmComplemento;
	}
	public void setCdEndereco(int cdEndereco){
		this.cdEndereco=cdEndereco;
	}
	public int getCdEndereco(){
		return this.cdEndereco;
	}
	public void setCdProprietario(int cdProprietario){
		this.cdProprietario=cdProprietario;
	}
	public int getCdProprietario(){
		return this.cdProprietario;
	}
	public void setNmProprietarioAutuacao(String nmProprietarioAutuacao){
		this.nmProprietarioAutuacao=nmProprietarioAutuacao;
	}
	public String getNmProprietarioAutuacao(){
		return this.nmProprietarioAutuacao;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAitProprietario: " +  getCdAitProprietario();
		valueToString += ", cdAit: " +  getCdAit();
		valueToString += ", nmProprietario: " +  getNmProprietario();
		valueToString += ", dsLogradouro: " +  getDsLogradouro();
		valueToString += ", dsNrImovel: " +  getDsNrImovel();
		valueToString += ", nrCep: " +  getNrCep();
		valueToString += ", nrDdd: " +  getNrDdd();
		valueToString += ", nrTelefone: " +  getNrTelefone();
		valueToString += ", nrCpfProprietario: " +  getNrCpfProprietario();
		valueToString += ", tpPessoaProprietario: " +  getTpPessoaProprietario();
		valueToString += ", nrCpfCnpjProprietario: " +  getNrCpfCnpjProprietario();
		valueToString += ", nmComplemento: " +  getNmComplemento();
		valueToString += ", cdEndereco: " +  getCdEndereco();
		valueToString += ", cdProprietario: " +  getCdProprietario();
		valueToString += ", nmProprietarioAutuacao: " +  getNmProprietarioAutuacao();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitProprietario(getCdAitProprietario(),
			getCdAit(),
			getNmProprietario(),
			getDsLogradouro(),
			getDsNrImovel(),
			getNrCep(),
			getNrDdd(),
			getNrTelefone(),
			getNrCpfProprietario(),
			getTpPessoaProprietario(),
			getNrCpfCnpjProprietario(),
			getNmComplemento(),
			getCdEndereco(),
			getCdProprietario(),
			getNmProprietarioAutuacao(),
			getCdPessoa());
	}

}
