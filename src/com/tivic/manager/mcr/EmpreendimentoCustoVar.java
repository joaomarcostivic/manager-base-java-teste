package com.tivic.manager.mcr;

public class EmpreendimentoCustoVar {

	private int cdEmpreendimento;
	private float vlTransporteVenda;
	private float vlComissaoVenda;
	private int lgIndustria;
	private float vlOutroCusto;
	private int lgComercio;
	private int lgServico;
	private float vlImposto;

	public EmpreendimentoCustoVar(int cdEmpreendimento,
			float vlTransporteVenda,
			float vlComissaoVenda,
			int lgIndustria,
			float vlOutroCusto,
			int lgComercio,
			int lgServico,
			float vlImposto){
		setCdEmpreendimento(cdEmpreendimento);
		setVlTransporteVenda(vlTransporteVenda);
		setVlComissaoVenda(vlComissaoVenda);
		setLgIndustria(lgIndustria);
		setVlOutroCusto(vlOutroCusto);
		setLgComercio(lgComercio);
		setLgServico(lgServico);
		setVlImposto(vlImposto);
	}
	public void setCdEmpreendimento(int cdEmpreendimento){
		this.cdEmpreendimento=cdEmpreendimento;
	}
	public int getCdEmpreendimento(){
		return this.cdEmpreendimento;
	}
	public void setVlTransporteVenda(float vlTransporteVenda){
		this.vlTransporteVenda=vlTransporteVenda;
	}
	public float getVlTransporteVenda(){
		return this.vlTransporteVenda;
	}
	public void setVlComissaoVenda(float vlComissaoVenda){
		this.vlComissaoVenda=vlComissaoVenda;
	}
	public float getVlComissaoVenda(){
		return this.vlComissaoVenda;
	}
	public void setLgIndustria(int lgIndustria){
		this.lgIndustria=lgIndustria;
	}
	public int getLgIndustria(){
		return this.lgIndustria;
	}
	public void setVlOutroCusto(float vlOutroCusto){
		this.vlOutroCusto=vlOutroCusto;
	}
	public float getVlOutroCusto(){
		return this.vlOutroCusto;
	}
	public void setLgComercio(int lgComercio){
		this.lgComercio=lgComercio;
	}
	public int getLgComercio(){
		return this.lgComercio;
	}
	public void setLgServico(int lgServico){
		this.lgServico=lgServico;
	}
	public int getLgServico(){
		return this.lgServico;
	}
	public void setVlImposto(float vlImposto){
		this.vlImposto=vlImposto;
	}
	public float getVlImposto(){
		return this.vlImposto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpreendimento: " +  getCdEmpreendimento();
		valueToString += ", vlTransporteVenda: " +  getVlTransporteVenda();
		valueToString += ", vlComissaoVenda: " +  getVlComissaoVenda();
		valueToString += ", lgIndustria: " +  getLgIndustria();
		valueToString += ", vlOutroCusto: " +  getVlOutroCusto();
		valueToString += ", lgComercio: " +  getLgComercio();
		valueToString += ", lgServico: " +  getLgServico();
		valueToString += ", vlImposto: " +  getVlImposto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EmpreendimentoCustoVar(cdEmpreendimento,
			vlTransporteVenda,
			vlComissaoVenda,
			lgIndustria,
			vlOutroCusto,
			lgComercio,
			lgServico,
			vlImposto);
	}

}