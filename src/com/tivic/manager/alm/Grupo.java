package com.tivic.manager.alm;

public class Grupo {

	private int cdGrupo;
	private int cdGrupoSuperior;
	private int cdCategoriaReceita;
	private int cdCategoriaDespesa;
	private String nmGrupo;
	private int cdFormulario;
	private int cdEventoAdesaoContrato;
	private int cdEventoContratacao;
	private int stGrupo;
	private String idGrupo;

	public Grupo() {
	}
	
	public Grupo(int cdGrupo,
			int cdGrupoSuperior,
			int cdCategoriaReceita,
			int cdCategoriaDespesa,
			String nmGrupo,
			int cdFormulario,
			int cdEventoAdesaoContrato,
			int cdEventoContratacao,
			int stGrupo,
			String idGrupo){
		setCdGrupo(cdGrupo);
		setCdGrupoSuperior(cdGrupoSuperior);
		setCdCategoriaReceita(cdCategoriaReceita);
		setCdCategoriaDespesa(cdCategoriaDespesa);
		setNmGrupo(nmGrupo);
		setCdFormulario(cdFormulario);
		setCdEventoAdesaoContrato(cdEventoAdesaoContrato);
		setCdEventoContratacao(cdEventoContratacao);
		setStGrupo(stGrupo);
		setIdGrupo(idGrupo);
		
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setCdGrupoSuperior(int cdGrupoSuperior){
		this.cdGrupoSuperior=cdGrupoSuperior;
	}
	public int getCdGrupoSuperior(){
		return this.cdGrupoSuperior;
	}
	public void setCdCategoriaReceita(int cdCategoriaReceita){
		this.cdCategoriaReceita=cdCategoriaReceita;
	}
	public int getCdCategoriaReceita(){
		return this.cdCategoriaReceita;
	}
	public void setCdCategoriaDespesa(int cdCategoriaDespesa){
		this.cdCategoriaDespesa=cdCategoriaDespesa;
	}
	public int getCdCategoriaDespesa(){
		return this.cdCategoriaDespesa;
	}
	public void setNmGrupo(String nmGrupo){
		this.nmGrupo=nmGrupo;
	}
	public String getNmGrupo(){
		return this.nmGrupo;
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public void setCdEventoAdesaoContrato(int cdEventoAdesaoContrato){
		this.cdEventoAdesaoContrato=cdEventoAdesaoContrato;
	}
	public int getCdEventoAdesaoContrato(){
		return this.cdEventoAdesaoContrato;
	}
	public void setCdEventoContratacao(int cdEventoContratacao){
		this.cdEventoContratacao=cdEventoContratacao;
	}
	public int getCdEventoContratacao(){
		return this.cdEventoContratacao;
	}
	public void setStGrupo(int stGrupo) {
		this.stGrupo = stGrupo;
	}
	public int getStGrupo() {
		return stGrupo;
	}
	public void setIdGrupo(String idGrupo) {
		this.idGrupo = idGrupo;
	}
	public String getIdGrupo() {
		return idGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupo: " +  getCdGrupo();
		valueToString += ", cdGrupoSuperior: " +  getCdGrupoSuperior();
		valueToString += ", cdCategoriaReceita: " +  getCdCategoriaReceita();
		valueToString += ", cdCategoriaDespesa: " +  getCdCategoriaDespesa();
		valueToString += ", nmGrupo: " +  getNmGrupo();
		valueToString += ", cdFormulario: " +  getCdFormulario();
		valueToString += ", cdEventoAdesaoContrato: " +  getCdEventoAdesaoContrato();
		valueToString += ", cdEventoContratacao: " +  getCdEventoContratacao();
		valueToString += ", stGrupo: " +  getStGrupo();
		valueToString += ", idGrupo: " +  getIdGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Grupo(getCdGrupo(),
			getCdGrupoSuperior(),
			getCdCategoriaReceita(),
			getCdCategoriaDespesa(),
			getNmGrupo(),
			getCdFormulario(),
			getCdEventoAdesaoContrato(),
			getCdEventoContratacao(),
			getStGrupo(),
			getIdGrupo());
	}

}
