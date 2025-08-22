package com.tivic.manager.adm;

public class OrcamentoCategoria {

	private int cdOrcamento;
	private int cdCategoriaEconomica;
	private int cdCompetencia;
	private int cdExercicio;
	private int vlOrcado;

	public OrcamentoCategoria() { }

	public OrcamentoCategoria(int cdOrcamento,
			int cdCategoriaEconomica,
			int cdCompetencia,
			int cdExercicio,
			int vlOrcado) {
		setCdOrcamento(cdOrcamento);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setCdCompetencia(cdCompetencia);
		setCdExercicio(cdExercicio);
		setVlOrcado(vlOrcado);
	}
	public void setCdOrcamento(int cdOrcamento){
		this.cdOrcamento=cdOrcamento;
	}
	public int getCdOrcamento(){
		return this.cdOrcamento;
	}
	public void setCdCategoriaEconomica(int cdCategoriaEconomica){
		this.cdCategoriaEconomica=cdCategoriaEconomica;
	}
	public int getCdCategoriaEconomica(){
		return this.cdCategoriaEconomica;
	}
	public void setCdCompetencia(int cdCompetencia){
		this.cdCompetencia=cdCompetencia;
	}
	public int getCdCompetencia(){
		return this.cdCompetencia;
	}
	public void setCdExercicio(int cdExercicio){
		this.cdExercicio=cdExercicio;
	}
	public int getCdExercicio(){
		return this.cdExercicio;
	}
	public void setVlOrcado(int vlOrcado){
		this.vlOrcado=vlOrcado;
	}
	public int getVlOrcado(){
		return this.vlOrcado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrcamento: " +  getCdOrcamento();
		valueToString += ", cdCategoriaEconomica: " +  getCdCategoriaEconomica();
		valueToString += ", cdCompetencia: " +  getCdCompetencia();
		valueToString += ", cdExercicio: " +  getCdExercicio();
		valueToString += ", vlOrcado: " +  getVlOrcado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OrcamentoCategoria(getCdOrcamento(),
			getCdCategoriaEconomica(),
			getCdCompetencia(),
			getCdExercicio(),
			getVlOrcado());
	}

}