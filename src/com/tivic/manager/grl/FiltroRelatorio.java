package com.tivic.manager.grl;

public class FiltroRelatorio {

	private int cdFiltroRelatorio;
	private int cdFormulario;
	private String nmRelatorio;
	private String dsRelatorio;
	private int tpRelatorio;
	private int stRelatorio;
	private int cdPessoa;
	private int cdUsuario;
	private String jsonFiltroRelatorio;

	public FiltroRelatorio() { }

	public FiltroRelatorio(int cdFiltroRelatorio,
			int cdFormulario,
			String nmRelatorio,
			String dsRelatorio,
			int tpRelatorio,
			int stRelatorio,
			int cdPessoa,
			int cdUsuario,
			String jsonFiltroRelatorio) {
		setCdFiltroRelatorio(cdFiltroRelatorio);
		setCdFormulario(cdFormulario);
		setNmRelatorio(nmRelatorio);
		setDsRelatorio(dsRelatorio);
		setTpRelatorio(tpRelatorio);
		setStRelatorio(stRelatorio);
		setCdPessoa(cdPessoa);
		setCdUsuario(cdUsuario);
		setJsonFiltroRelatorio(jsonFiltroRelatorio);
	}
	public void setCdFiltroRelatorio(int cdFiltroRelatorio){
		this.cdFiltroRelatorio=cdFiltroRelatorio;
	}
	public int getCdFiltroRelatorio(){
		return this.cdFiltroRelatorio;
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public void setNmRelatorio(String nmRelatorio){
		this.nmRelatorio=nmRelatorio;
	}
	public String getNmRelatorio(){
		return this.nmRelatorio;
	}
	public void setDsRelatorio(String dsRelatorio){
		this.dsRelatorio=dsRelatorio;
	}
	public String getDsRelatorio(){
		return this.dsRelatorio;
	}
	public void setTpRelatorio(int tpRelatorio){
		this.tpRelatorio=tpRelatorio;
	}
	public int getTpRelatorio(){
		return this.tpRelatorio;
	}
	public void setStRelatorio(int stRelatorio){
		this.stRelatorio=stRelatorio;
	}
	public int getStRelatorio(){
		return this.stRelatorio;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setJsonFiltroRelatorio(String jsonFiltroRelatorio){
		this.jsonFiltroRelatorio=jsonFiltroRelatorio;
	}
	public String getJsonFiltroRelatorio(){
		return this.jsonFiltroRelatorio;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFiltroRelatorio: " +  getCdFiltroRelatorio();
		valueToString += ", cdFormulario: " +  getCdFormulario();
		valueToString += ", nmRelatorio: " +  getNmRelatorio();
		valueToString += ", dsRelatorio: " +  getDsRelatorio();
		valueToString += ", tpRelatorio: " +  getTpRelatorio();
		valueToString += ", stRelatorio: " +  getStRelatorio();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", jsonFiltroRelatorio: " +  getJsonFiltroRelatorio();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FiltroRelatorio(getCdFiltroRelatorio(),
			getCdFormulario(),
			getNmRelatorio(),
			getDsRelatorio(),
			getTpRelatorio(),
			getStRelatorio(),
			getCdPessoa(),
			getCdUsuario(),
			getJsonFiltroRelatorio());
	}

}