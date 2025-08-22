package com.tivic.manager.mcr;

public class EmpreendimentoFuncionario {

	private int cdEmpreendimento;
	private int cdFuncionario;
	private String nmFuncionario;
	private int lgFamiliar;
	private int lgRemunerado;
	private int lgCarteiraAssinada;
	private float vlSalario;
	private int tpSexo;

	public EmpreendimentoFuncionario(int cdEmpreendimento,
			int cdFuncionario,
			String nmFuncionario,
			int lgFamiliar,
			int lgRemunerado,
			int lgCarteiraAssinada,
			float vlSalario,
			int tpSexo){
		setCdEmpreendimento(cdEmpreendimento);
		setCdFuncionario(cdFuncionario);
		setNmFuncionario(nmFuncionario);
		setLgFamiliar(lgFamiliar);
		setLgRemunerado(lgRemunerado);
		setLgCarteiraAssinada(lgCarteiraAssinada);
		setVlSalario(vlSalario);
		setTpSexo(tpSexo);
	}
	public void setCdEmpreendimento(int cdEmpreendimento){
		this.cdEmpreendimento=cdEmpreendimento;
	}
	public int getCdEmpreendimento(){
		return this.cdEmpreendimento;
	}
	public void setCdFuncionario(int cdFuncionario){
		this.cdFuncionario=cdFuncionario;
	}
	public int getCdFuncionario(){
		return this.cdFuncionario;
	}
	public void setNmFuncionario(String nmFuncionario){
		this.nmFuncionario=nmFuncionario;
	}
	public String getNmFuncionario(){
		return this.nmFuncionario;
	}
	public void setLgFamiliar(int lgFamiliar){
		this.lgFamiliar=lgFamiliar;
	}
	public int getLgFamiliar(){
		return this.lgFamiliar;
	}
	public void setLgRemunerado(int lgRemunerado){
		this.lgRemunerado=lgRemunerado;
	}
	public int getLgRemunerado(){
		return this.lgRemunerado;
	}
	public void setLgCarteiraAssinada(int lgCarteiraAssinada){
		this.lgCarteiraAssinada=lgCarteiraAssinada;
	}
	public int getLgCarteiraAssinada(){
		return this.lgCarteiraAssinada;
	}
	public void setVlSalario(float vlSalario){
		this.vlSalario=vlSalario;
	}
	public float getVlSalario(){
		return this.vlSalario;
	}
	public void setTpSexo(int tpSexo){
		this.tpSexo=tpSexo;
	}
	public int getTpSexo(){
		return this.tpSexo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpreendimento: " +  getCdEmpreendimento();
		valueToString += ", cdFuncionario: " +  getCdFuncionario();
		valueToString += ", nmFuncionario: " +  getNmFuncionario();
		valueToString += ", lgFamiliar: " +  getLgFamiliar();
		valueToString += ", lgRemunerado: " +  getLgRemunerado();
		valueToString += ", lgCarteiraAssinada: " +  getLgCarteiraAssinada();
		valueToString += ", vlSalario: " +  getVlSalario();
		valueToString += ", tpSexo: " +  getTpSexo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EmpreendimentoFuncionario(cdEmpreendimento,
			cdFuncionario,
			nmFuncionario,
			lgFamiliar,
			lgRemunerado,
			lgCarteiraAssinada,
			vlSalario,
			tpSexo);
	}

}