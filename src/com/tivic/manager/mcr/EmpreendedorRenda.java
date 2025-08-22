package com.tivic.manager.mcr;

public class EmpreendedorRenda {

	private int cdPessoa;
	private int cdRenda;
	private String nmPessoa;
	private String nmFonteRenda;
	private float vlMensal;

	public EmpreendedorRenda(int cdPessoa,
			int cdRenda,
			String nmPessoa,
			String nmFonteRenda,
			float vlMensal){
		setCdPessoa(cdPessoa);
		setCdRenda(cdRenda);
		setNmPessoa(nmPessoa);
		setNmFonteRenda(nmFonteRenda);
		setVlMensal(vlMensal);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdRenda(int cdRenda){
		this.cdRenda=cdRenda;
	}
	public int getCdRenda(){
		return this.cdRenda;
	}
	public void setNmPessoa(String nmPessoa){
		this.nmPessoa=nmPessoa;
	}
	public String getNmPessoa(){
		return this.nmPessoa;
	}
	public void setNmFonteRenda(String nmFonteRenda){
		this.nmFonteRenda=nmFonteRenda;
	}
	public String getNmFonteRenda(){
		return this.nmFonteRenda;
	}
	public void setVlMensal(float vlMensal){
		this.vlMensal=vlMensal;
	}
	public float getVlMensal(){
		return this.vlMensal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdRenda: " +  getCdRenda();
		valueToString += ", nmPessoa: " +  getNmPessoa();
		valueToString += ", nmFonteRenda: " +  getNmFonteRenda();
		valueToString += ", vlMensal: " +  getVlMensal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EmpreendedorRenda(cdPessoa,
			cdRenda,
			nmPessoa,
			nmFonteRenda,
			vlMensal);
	}

}