package com.tivic.manager.mcr;

public class Empreendedor {

	private int cdPessoa;
	private int tpMoradia;
	private int lgHipoteca;
	private int nrTempoResideLocal;
	private float vlMoradia;
	private String nmOutroTipo;
	private int nrTempoResideMunicipio;
	private String txtParecer;
	private int nrPessoasRenda;
	private float vlAlimentacao;
	private float vlSaude;
	private float vlEducacao;
	private float vlVestuario;
	private float vlAluguel;
	private float vlAgua;
	private float vlTransporte;
	private float vlDividas;
	private float vlDiversao;
	private float vlOutros;
	private String txtExperienciaAnterior;
	private String nmSuperior;
	private String txtCursoProfissionalizante;
	private String txtComportamento;
	private String txtObservacao;

	public Empreendedor(int cdPessoa,
			int tpMoradia,
			int lgHipoteca,
			int nrTempoResideLocal,
			float vlMoradia,
			String nmOutroTipo,
			int nrTempoResideMunicipio,
			String txtParecer,
			int nrPessoasRenda,
			float vlAlimentacao,
			float vlSaude,
			float vlEducacao,
			float vlVestuario,
			float vlAluguel,
			float vlAgua,
			float vlTransporte,
			float vlDividas,
			float vlDiversao,
			float vlOutros,
			String txtExperienciaAnterior,
			String nmSuperior,
			String txtCursoProfissionalizante,
			String txtComportamento,
			String txtObservacao){
		setCdPessoa(cdPessoa);
		setTpMoradia(tpMoradia);
		setLgHipoteca(lgHipoteca);
		setNrTempoResideLocal(nrTempoResideLocal);
		setVlMoradia(vlMoradia);
		setNmOutroTipo(nmOutroTipo);
		setNrTempoResideMunicipio(nrTempoResideMunicipio);
		setTxtParecer(txtParecer);
		setNrPessoasRenda(nrPessoasRenda);
		setVlAlimentacao(vlAlimentacao);
		setVlSaude(vlSaude);
		setVlEducacao(vlEducacao);
		setVlVestuario(vlVestuario);
		setVlAluguel(vlAluguel);
		setVlAgua(vlAgua);
		setVlTransporte(vlTransporte);
		setVlDividas(vlDividas);
		setVlDiversao(vlDiversao);
		setVlOutros(vlOutros);
		setTxtExperienciaAnterior(txtExperienciaAnterior);
		setNmSuperior(nmSuperior);
		setTxtCursoProfissionalizante(txtCursoProfissionalizante);
		setTxtComportamento(txtComportamento);
		setTxtObservacao(txtObservacao);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTpMoradia(int tpMoradia){
		this.tpMoradia=tpMoradia;
	}
	public int getTpMoradia(){
		return this.tpMoradia;
	}
	public void setLgHipoteca(int lgHipoteca){
		this.lgHipoteca=lgHipoteca;
	}
	public int getLgHipoteca(){
		return this.lgHipoteca;
	}
	public void setNrTempoResideLocal(int nrTempoResideLocal){
		this.nrTempoResideLocal=nrTempoResideLocal;
	}
	public int getNrTempoResideLocal(){
		return this.nrTempoResideLocal;
	}
	public void setVlMoradia(float vlMoradia){
		this.vlMoradia=vlMoradia;
	}
	public float getVlMoradia(){
		return this.vlMoradia;
	}
	public void setNmOutroTipo(String nmOutroTipo){
		this.nmOutroTipo=nmOutroTipo;
	}
	public String getNmOutroTipo(){
		return this.nmOutroTipo;
	}
	public void setNrTempoResideMunicipio(int nrTempoResideMunicipio){
		this.nrTempoResideMunicipio=nrTempoResideMunicipio;
	}
	public int getNrTempoResideMunicipio(){
		return this.nrTempoResideMunicipio;
	}
	public void setTxtParecer(String txtParecer){
		this.txtParecer=txtParecer;
	}
	public String getTxtParecer(){
		return this.txtParecer;
	}
	public void setNrPessoasRenda(int nrPessoasRenda){
		this.nrPessoasRenda=nrPessoasRenda;
	}
	public int getNrPessoasRenda(){
		return this.nrPessoasRenda;
	}
	public void setVlAlimentacao(float vlAlimentacao){
		this.vlAlimentacao=vlAlimentacao;
	}
	public float getVlAlimentacao(){
		return this.vlAlimentacao;
	}
	public void setVlSaude(float vlSaude){
		this.vlSaude=vlSaude;
	}
	public float getVlSaude(){
		return this.vlSaude;
	}
	public void setVlEducacao(float vlEducacao){
		this.vlEducacao=vlEducacao;
	}
	public float getVlEducacao(){
		return this.vlEducacao;
	}
	public void setVlVestuario(float vlVestuario){
		this.vlVestuario=vlVestuario;
	}
	public float getVlVestuario(){
		return this.vlVestuario;
	}
	public void setVlAluguel(float vlAluguel){
		this.vlAluguel=vlAluguel;
	}
	public float getVlAluguel(){
		return this.vlAluguel;
	}
	public void setVlAgua(float vlAgua){
		this.vlAgua=vlAgua;
	}
	public float getVlAgua(){
		return this.vlAgua;
	}
	public void setVlTransporte(float vlTransporte){
		this.vlTransporte=vlTransporte;
	}
	public float getVlTransporte(){
		return this.vlTransporte;
	}
	public void setVlDividas(float vlDividas){
		this.vlDividas=vlDividas;
	}
	public float getVlDividas(){
		return this.vlDividas;
	}
	public void setVlDiversao(float vlDiversao){
		this.vlDiversao=vlDiversao;
	}
	public float getVlDiversao(){
		return this.vlDiversao;
	}
	public void setVlOutros(float vlOutros){
		this.vlOutros=vlOutros;
	}
	public float getVlOutros(){
		return this.vlOutros;
	}
	public void setTxtExperienciaAnterior(String txtExperienciaAnterior){
		this.txtExperienciaAnterior=txtExperienciaAnterior;
	}
	public String getTxtExperienciaAnterior(){
		return this.txtExperienciaAnterior;
	}
	public void setNmSuperior(String nmSuperior){
		this.nmSuperior=nmSuperior;
	}
	public String getNmSuperior(){
		return this.nmSuperior;
	}
	public void setTxtCursoProfissionalizante(String txtCursoProfissionalizante){
		this.txtCursoProfissionalizante=txtCursoProfissionalizante;
	}
	public String getTxtCursoProfissionalizante(){
		return this.txtCursoProfissionalizante;
	}
	public void setTxtComportamento(String txtComportamento){
		this.txtComportamento=txtComportamento;
	}
	public String getTxtComportamento(){
		return this.txtComportamento;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", tpMoradia: " +  getTpMoradia();
		valueToString += ", lgHipoteca: " +  getLgHipoteca();
		valueToString += ", nrTempoResideLocal: " +  getNrTempoResideLocal();
		valueToString += ", vlMoradia: " +  getVlMoradia();
		valueToString += ", nmOutroTipo: " +  getNmOutroTipo();
		valueToString += ", nrTempoResideMunicipio: " +  getNrTempoResideMunicipio();
		valueToString += ", txtParecer: " +  getTxtParecer();
		valueToString += ", nrPessoasRenda: " +  getNrPessoasRenda();
		valueToString += ", vlAlimentacao: " +  getVlAlimentacao();
		valueToString += ", vlSaude: " +  getVlSaude();
		valueToString += ", vlEducacao: " +  getVlEducacao();
		valueToString += ", vlVestuario: " +  getVlVestuario();
		valueToString += ", vlAluguel: " +  getVlAluguel();
		valueToString += ", vlAgua: " +  getVlAgua();
		valueToString += ", vlTransporte: " +  getVlTransporte();
		valueToString += ", vlDividas: " +  getVlDividas();
		valueToString += ", vlDiversao: " +  getVlDiversao();
		valueToString += ", vlOutros: " +  getVlOutros();
		valueToString += ", txtExperienciaAnterior: " +  getTxtExperienciaAnterior();
		valueToString += ", nmSuperior: " +  getNmSuperior();
		valueToString += ", txtCursoProfissionalizante: " +  getTxtCursoProfissionalizante();
		valueToString += ", txtComportamento: " +  getTxtComportamento();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Empreendedor(cdPessoa,
			tpMoradia,
			lgHipoteca,
			nrTempoResideLocal,
			vlMoradia,
			nmOutroTipo,
			nrTempoResideMunicipio,
			txtParecer,
			nrPessoasRenda,
			vlAlimentacao,
			vlSaude,
			vlEducacao,
			vlVestuario,
			vlAluguel,
			vlAgua,
			vlTransporte,
			vlDividas,
			vlDiversao,
			vlOutros,
			txtExperienciaAnterior,
			nmSuperior,
			txtCursoProfissionalizante,
			txtComportamento,
			txtObservacao);
	}

}