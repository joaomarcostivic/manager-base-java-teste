package com.tivic.manager.psq;

public class BibliotecaRecurso {

	private int cdBibliotecaRecurso;
	private String nmObjeto;
	private String idObjeto;
	private int tpObjeto;
	private byte[] blbObjeto;
	private int cdEmpresa;
	private int cdPessoa;
	private int cdVinculo;

	public BibliotecaRecurso(int cdBibliotecaRecurso,
			String nmObjeto,
			String idObjeto,
			int tpObjeto,
			byte[] blbObjeto,
			int cdEmpresa,
			int cdPessoa,
			int cdVinculo){
		setCdBibliotecaRecurso(cdBibliotecaRecurso);
		setNmObjeto(nmObjeto);
		setIdObjeto(idObjeto);
		setTpObjeto(tpObjeto);
		setBlbObjeto(blbObjeto);
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setCdVinculo(cdVinculo);
	}
	public void setCdBibliotecaRecurso(int cdBibliotecaRecurso){
		this.cdBibliotecaRecurso=cdBibliotecaRecurso;
	}
	public int getCdBibliotecaRecurso(){
		return this.cdBibliotecaRecurso;
	}
	public void setNmObjeto(String nmObjeto){
		this.nmObjeto=nmObjeto;
	}
	public String getNmObjeto(){
		return this.nmObjeto;
	}
	public void setIdObjeto(String idObjeto){
		this.idObjeto=idObjeto;
	}
	public String getIdObjeto(){
		return this.idObjeto;
	}
	public void setTpObjeto(int tpObjeto){
		this.tpObjeto=tpObjeto;
	}
	public int getTpObjeto(){
		return this.tpObjeto;
	}
	public void setBlbObjeto(byte[] blbObjeto){
		this.blbObjeto=blbObjeto;
	}
	public byte[] getBlbObjeto(){
		return this.blbObjeto;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdVinculo(int cdVinculo){
		this.cdVinculo=cdVinculo;
	}
	public int getCdVinculo(){
		return this.cdVinculo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBibliotecaRecurso: " +  getCdBibliotecaRecurso();
		valueToString += ", nmObjeto: " +  getNmObjeto();
		valueToString += ", idObjeto: " +  getIdObjeto();
		valueToString += ", tpObjeto: " +  getTpObjeto();
		valueToString += ", blbObjeto: " +  getBlbObjeto();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdVinculo: " +  getCdVinculo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BibliotecaRecurso(getCdBibliotecaRecurso(),
			getNmObjeto(),
			getIdObjeto(),
			getTpObjeto(),
			getBlbObjeto(),
			getCdEmpresa(),
			getCdPessoa(),
			getCdVinculo());
	}

}