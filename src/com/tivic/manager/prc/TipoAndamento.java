package com.tivic.manager.prc;

public class TipoAndamento {

	private int cdTipoAndamento;
	private String nmTipoAndamento;
	private String idTipoAndamento;
	private int stTipoAndamento;
	private Double vlPadrao;
	private int tpVisibilidade;
	private int cdTipoSituacao;
	private int tpFatoGerador;
	private int lgEmail;
	private int lgEmailCliente;
	private int lgEmailCorrespondente;
	private int lgEmailGrupoProcesso;
	private int stCadastro;
	private int cdTipoAndamentoSuperior;

	public TipoAndamento() { }

	public TipoAndamento(int cdTipoAndamento,
			String nmTipoAndamento,
			String idTipoAndamento,
			int stTipoAndamento,
			Double vlPadrao,
			int tpVisibilidade,
			int cdTipoSituacao,
			int tpFatoGerador,
			int lgEmail,
			int lgEmailCliente,
			int lgEmailCorrespondente,
			int lgEmailGrupoProcesso,
			int stCadastro,
			int cdTipoAndamentoSuperior) {
		setCdTipoAndamento(cdTipoAndamento);
		setNmTipoAndamento(nmTipoAndamento);
		setIdTipoAndamento(idTipoAndamento);
		setStTipoAndamento(stTipoAndamento);
		setVlPadrao(vlPadrao);
		setTpVisibilidade(tpVisibilidade);
		setCdTipoSituacao(cdTipoSituacao);
		setTpFatoGerador(tpFatoGerador);
		setLgEmail(lgEmail);
		setLgEmailCliente(lgEmailCliente);
		setLgEmailCorrespondente(lgEmailCorrespondente);
		setLgEmailGrupoProcesso(lgEmailGrupoProcesso);
		setStCadastro(stCadastro);
		setCdTipoAndamentoSuperior(cdTipoAndamentoSuperior);
	}
	public void setCdTipoAndamento(int cdTipoAndamento){
		this.cdTipoAndamento=cdTipoAndamento;
	}
	public int getCdTipoAndamento(){
		return this.cdTipoAndamento;
	}
	public void setNmTipoAndamento(String nmTipoAndamento){
		this.nmTipoAndamento=nmTipoAndamento;
	}
	public String getNmTipoAndamento(){
		return this.nmTipoAndamento;
	}
	public void setIdTipoAndamento(String idTipoAndamento){
		this.idTipoAndamento=idTipoAndamento;
	}
	public String getIdTipoAndamento(){
		return this.idTipoAndamento;
	}
	public void setStTipoAndamento(int stTipoAndamento){
		this.stTipoAndamento=stTipoAndamento;
	}
	public int getStTipoAndamento(){
		return this.stTipoAndamento;
	}
	public void setVlPadrao(Double vlPadrao){
		this.vlPadrao=vlPadrao;
	}
	public Double getVlPadrao(){
		return this.vlPadrao;
	}
	public void setTpVisibilidade(int tpVisibilidade){
		this.tpVisibilidade=tpVisibilidade;
	}
	public int getTpVisibilidade(){
		return this.tpVisibilidade;
	}
	public void setCdTipoSituacao(int cdTipoSituacao){
		this.cdTipoSituacao=cdTipoSituacao;
	}
	public int getCdTipoSituacao(){
		return this.cdTipoSituacao;
	}
	public void setTpFatoGerador(int tpFatoGerador){
		this.tpFatoGerador=tpFatoGerador;
	}
	public int getTpFatoGerador(){
		return this.tpFatoGerador;
	}
	public void setLgEmail(int lgEmail){
		this.lgEmail=lgEmail;
	}
	public int getLgEmail(){
		return this.lgEmail;
	}
	public void setLgEmailCliente(int lgEmailCliente){
		this.lgEmailCliente=lgEmailCliente;
	}
	public int getLgEmailCliente(){
		return this.lgEmailCliente;
	}
	public void setLgEmailCorrespondente(int lgEmailCorrespondente){
		this.lgEmailCorrespondente=lgEmailCorrespondente;
	}
	public int getLgEmailCorrespondente(){
		return this.lgEmailCorrespondente;
	}
	public void setLgEmailGrupoProcesso(int lgEmailGrupoProcesso){
		this.lgEmailGrupoProcesso=lgEmailGrupoProcesso;
	}
	public int getLgEmailGrupoProcesso(){
		return this.lgEmailGrupoProcesso;
	}
	public void setStCadastro(int stCadastro){
		this.stCadastro=stCadastro;
	}
	public int getStCadastro(){
		return this.stCadastro;
	}
	public void setCdTipoAndamentoSuperior(int cdTipoAndamentoSuperior){
		this.cdTipoAndamentoSuperior=cdTipoAndamentoSuperior;
	}
	public int getCdTipoAndamentoSuperior(){
		return this.cdTipoAndamentoSuperior;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoAndamento: " +  getCdTipoAndamento();
		valueToString += ", nmTipoAndamento: " +  getNmTipoAndamento();
		valueToString += ", idTipoAndamento: " +  getIdTipoAndamento();
		valueToString += ", stTipoAndamento: " +  getStTipoAndamento();
		valueToString += ", vlPadrao: " +  getVlPadrao();
		valueToString += ", tpVisibilidade: " +  getTpVisibilidade();
		valueToString += ", cdTipoSituacao: " +  getCdTipoSituacao();
		valueToString += ", tpFatoGerador: " +  getTpFatoGerador();
		valueToString += ", lgEmail: " +  getLgEmail();
		valueToString += ", lgEmailCliente: " +  getLgEmailCliente();
		valueToString += ", lgEmailCorrespondente: " +  getLgEmailCorrespondente();
		valueToString += ", lgEmailGrupoProcesso: " +  getLgEmailGrupoProcesso();
		valueToString += ", stCadastro: " +  getStCadastro();
		valueToString += ", cdTipoAndamentoSuperior: " +  getCdTipoAndamentoSuperior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoAndamento(getCdTipoAndamento(),
			getNmTipoAndamento(),
			getIdTipoAndamento(),
			getStTipoAndamento(),
			getVlPadrao(),
			getTpVisibilidade(),
			getCdTipoSituacao(),
			getTpFatoGerador(),
			getLgEmail(),
			getLgEmailCliente(),
			getLgEmailCorrespondente(),
			getLgEmailGrupoProcesso(),
			getStCadastro(),
			getCdTipoAndamentoSuperior());
	}

}