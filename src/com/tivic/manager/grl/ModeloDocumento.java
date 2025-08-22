package com.tivic.manager.grl;

public class ModeloDocumento {

	private int cdModelo;
	private String nmModelo;
	private String txtModelo;
	private int tpModelo;
	private byte[] blbConteudo;
	private String txtConteudo;
	private int stModelo;
	private String nmTitulo;
	private int cdTipoDocumento;
	private String idModelo;
	private String urlModelo;
	private String idRepositorio;
	private int cdEmpresa;

	public ModeloDocumento(){ }

	public ModeloDocumento(int cdModelo,
			String nmModelo,
			String txtModelo,
			int tpModelo,
			byte[] blbConteudo,
			String txtConteudo,
			int stModelo,
			String nmTitulo,
			int cdTipoDocumento,
			String idModelo,
			String urlModelo,
			String idRepositorio,
			int cdEmpresa){
		setCdModelo(cdModelo);
		setNmModelo(nmModelo);
		setTxtModelo(txtModelo);
		setTpModelo(tpModelo);
		setBlbConteudo(blbConteudo);
		setTxtConteudo(txtConteudo);
		setStModelo(stModelo);
		setNmTitulo(nmTitulo);
		setCdTipoDocumento(cdTipoDocumento);
		setIdModelo(idModelo);
		setUrlModelo(urlModelo);
		setIdRepositorio(idRepositorio);
		setCdEmpresa(cdEmpresa);
	}
	public void setCdModelo(int cdModelo){
		this.cdModelo=cdModelo;
	}
	public int getCdModelo(){
		return this.cdModelo;
	}
	public void setNmModelo(String nmModelo){
		this.nmModelo=nmModelo;
	}
	public String getNmModelo(){
		return this.nmModelo;
	}
	public void setTxtModelo(String txtModelo){
		this.txtModelo=txtModelo;
	}
	public String getTxtModelo(){
		return this.txtModelo;
	}
	public void setTpModelo(int tpModelo){
		this.tpModelo=tpModelo;
	}
	public int getTpModelo(){
		return this.tpModelo;
	}
	public void setBlbConteudo(byte[] blbConteudo){
		this.blbConteudo=blbConteudo;
	}
	public byte[] getBlbConteudo(){
		return this.blbConteudo;
	}
	public void setTxtConteudo(String txtConteudo){
		this.txtConteudo=txtConteudo;
	}
	public String getTxtConteudo(){
		return this.txtConteudo;
	}
	public void setStModelo(int stModelo){
		this.stModelo=stModelo;
	}
	public int getStModelo(){
		return this.stModelo;
	}
	public void setNmTitulo(String nmTitulo){
		this.nmTitulo=nmTitulo;
	}
	public String getNmTitulo(){
		return this.nmTitulo;
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setIdModelo(String idModelo){
		this.idModelo=idModelo;
	}
	public String getIdModelo(){
		return this.idModelo;
	}
	public void setUrlModelo(String urlModelo){
		this.urlModelo=urlModelo;
	}
	public String getUrlModelo(){
		return this.urlModelo;
	}
	public void setIdRepositorio(String idRepositorio){
		this.idRepositorio=idRepositorio;
	}
	public String getIdRepositorio(){
		return this.idRepositorio;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdModelo: " +  getCdModelo();
		valueToString += ", nmModelo: " +  getNmModelo();
		valueToString += ", txtModelo: " +  getTxtModelo();
		valueToString += ", tpModelo: " +  getTpModelo();
		valueToString += ", blbConteudo: " +  getBlbConteudo();
		valueToString += ", txtConteudo: " +  getTxtConteudo();
		valueToString += ", stModelo: " +  getStModelo();
		valueToString += ", nmTitulo: " +  getNmTitulo();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", idModelo: " +  getIdModelo();
		valueToString += ", urlModelo: " +  getUrlModelo();
		valueToString += ", idRepositorio: " +  getIdRepositorio();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ModeloDocumento(getCdModelo(),
			getNmModelo(),
			getTxtModelo(),
			getTpModelo(),
			getBlbConteudo(),
			getTxtConteudo(),
			getStModelo(),
			getNmTitulo(),
			getCdTipoDocumento(),
			getIdModelo(),
			getUrlModelo(),
			getIdRepositorio(),
			getCdEmpresa());
	}

}