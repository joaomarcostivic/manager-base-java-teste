package com.tivic.manager.mcr;

public class ComiteCredito {

	private int cdAtaComite;
	private int cdPessoa;
	private int cdProposta;
	private float vlSugerido;

	public ComiteCredito(int cdAtaComite,
			int cdPessoa,
			int cdProposta,
			float vlSugerido){
		setCdAtaComite(cdAtaComite);
		setCdPessoa(cdPessoa);
		setCdProposta(cdProposta);
		setVlSugerido(vlSugerido);
	}
	public void setCdAtaComite(int cdAtaComite){
		this.cdAtaComite=cdAtaComite;
	}
	public int getCdAtaComite(){
		return this.cdAtaComite;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdProposta(int cdProposta){
		this.cdProposta=cdProposta;
	}
	public int getCdProposta(){
		return this.cdProposta;
	}
	public void setVlSugerido(float vlSugerido){
		this.vlSugerido=vlSugerido;
	}
	public float getVlSugerido(){
		return this.vlSugerido;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAtaComite: " +  getCdAtaComite();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdProposta: " +  getCdProposta();
		valueToString += ", vlSugerido: " +  getVlSugerido();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ComiteCredito(getCdAtaComite(),
			getCdPessoa(),
			getCdProposta(),
			getVlSugerido());
	}

}
