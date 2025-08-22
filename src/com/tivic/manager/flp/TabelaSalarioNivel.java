package com.tivic.manager.flp;

public class TabelaSalarioNivel {

	private int cdTabelaSalario;
	private int cdNivelSalario;
	private String nmNivelSalario;
	private String idNivelSalario;
	private int tpCalculo;
	private float vlAplicacao;

	public TabelaSalarioNivel(int cdTabelaSalario,
			int cdNivelSalario,
			String nmNivelSalario,
			String idNivelSalario,
			int tpCalculo,
			float vlAplicacao){
		setCdTabelaSalario(cdTabelaSalario);
		setCdNivelSalario(cdNivelSalario);
		setNmNivelSalario(nmNivelSalario);
		setIdNivelSalario(idNivelSalario);
		setTpCalculo(tpCalculo);
		setVlAplicacao(vlAplicacao);
	}
	public void setCdTabelaSalario(int cdTabelaSalario){
		this.cdTabelaSalario=cdTabelaSalario;
	}
	public int getCdTabelaSalario(){
		return this.cdTabelaSalario;
	}
	public void setCdNivelSalario(int cdNivelSalario){
		this.cdNivelSalario=cdNivelSalario;
	}
	public int getCdNivelSalario(){
		return this.cdNivelSalario;
	}
	public void setNmNivelSalario(String nmNivelSalario){
		this.nmNivelSalario=nmNivelSalario;
	}
	public String getNmNivelSalario(){
		return this.nmNivelSalario;
	}
	public void setIdNivelSalario(String idNivelSalario){
		this.idNivelSalario=idNivelSalario;
	}
	public String getIdNivelSalario(){
		return this.idNivelSalario;
	}
	public void setTpCalculo(int tpCalculo){
		this.tpCalculo=tpCalculo;
	}
	public int getTpCalculo(){
		return this.tpCalculo;
	}
	public void setVlAplicacao(float vlAplicacao){
		this.vlAplicacao=vlAplicacao;
	}
	public float getVlAplicacao(){
		return this.vlAplicacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaSalario: " +  getCdTabelaSalario();
		valueToString += ", cdNivelSalario: " +  getCdNivelSalario();
		valueToString += ", nmNivelSalario: " +  getNmNivelSalario();
		valueToString += ", idNivelSalario: " +  getIdNivelSalario();
		valueToString += ", tpCalculo: " +  getTpCalculo();
		valueToString += ", vlAplicacao: " +  getVlAplicacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaSalarioNivel(getCdTabelaSalario(),
			getCdNivelSalario(),
			getNmNivelSalario(),
			getIdNivelSalario(),
			getTpCalculo(),
			getVlAplicacao());
	}

}