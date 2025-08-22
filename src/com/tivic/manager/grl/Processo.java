package com.tivic.manager.grl;

public class Processo {

	private int cdProcesso;
	private String nmProcesso;
	private int stProcesso;
	private int cdEmpresa;
	private String txtProcesso;
	private int cdTipoProcesso;
	private int tpNumeracao;
	private String idPrefixoNumeracao;
	private String txtMascaraNumeracao;
	private int nrUltimaNumeracao;
	private int tpProcesso;

	public Processo(int cdProcesso,
			String nmProcesso,
			int stProcesso,
			int cdEmpresa,
			String txtProcesso,
			int cdTipoProcesso,
			int tpNumeracao,
			String idPrefixoNumeracao,
			String txtMascaraNumeracao,
			int nrUltimaNumeracao,
			int tpProcesso){
		setCdProcesso(cdProcesso);
		setNmProcesso(nmProcesso);
		setStProcesso(stProcesso);
		setCdEmpresa(cdEmpresa);
		setTxtProcesso(txtProcesso);
		setCdTipoProcesso(cdTipoProcesso);
		setTpNumeracao(tpNumeracao);
		setIdPrefixoNumeracao(idPrefixoNumeracao);
		setTxtMascaraNumeracao(txtMascaraNumeracao);
		setNrUltimaNumeracao(nrUltimaNumeracao);
		setTpProcesso(tpProcesso);
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setNmProcesso(String nmProcesso){
		this.nmProcesso=nmProcesso;
	}
	public String getNmProcesso(){
		return this.nmProcesso;
	}
	public void setStProcesso(int stProcesso){
		this.stProcesso=stProcesso;
	}
	public int getStProcesso(){
		return this.stProcesso;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setTxtProcesso(String txtProcesso){
		this.txtProcesso=txtProcesso;
	}
	public String getTxtProcesso(){
		return this.txtProcesso;
	}
	public void setCdTipoProcesso(int cdTipoProcesso){
		this.cdTipoProcesso=cdTipoProcesso;
	}
	public int getCdTipoProcesso(){
		return this.cdTipoProcesso;
	}
	public void setTpNumeracao(int tpNumeracao){
		this.tpNumeracao=tpNumeracao;
	}
	public int getTpNumeracao(){
		return this.tpNumeracao;
	}
	public void setIdPrefixoNumeracao(String idPrefixoNumeracao){
		this.idPrefixoNumeracao=idPrefixoNumeracao;
	}
	public String getIdPrefixoNumeracao(){
		return this.idPrefixoNumeracao;
	}
	public void setTxtMascaraNumeracao(String txtMascaraNumeracao){
		this.txtMascaraNumeracao=txtMascaraNumeracao;
	}
	public String getTxtMascaraNumeracao(){
		return this.txtMascaraNumeracao;
	}
	public void setNrUltimaNumeracao(int nrUltimaNumeracao){
		this.nrUltimaNumeracao=nrUltimaNumeracao;
	}
	public int getNrUltimaNumeracao(){
		return this.nrUltimaNumeracao;
	}
	public void setTpProcesso(int tpProcesso){
		this.tpProcesso=tpProcesso;
	}
	public int getTpProcesso(){
		return this.tpProcesso;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProcesso: " +  getCdProcesso();
		valueToString += ", nmProcesso: " +  getNmProcesso();
		valueToString += ", stProcesso: " +  getStProcesso();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", txtProcesso: " +  getTxtProcesso();
		valueToString += ", cdTipoProcesso: " +  getCdTipoProcesso();
		valueToString += ", tpNumeracao: " +  getTpNumeracao();
		valueToString += ", idPrefixoNumeracao: " +  getIdPrefixoNumeracao();
		valueToString += ", txtMascaraNumeracao: " +  getTxtMascaraNumeracao();
		valueToString += ", nrUltimaNumeracao: " +  getNrUltimaNumeracao();
		valueToString += ", tpProcesso: " +  getTpProcesso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Processo(getCdProcesso(),
			getNmProcesso(),
			getStProcesso(),
			getCdEmpresa(),
			getTxtProcesso(),
			getCdTipoProcesso(),
			getTpNumeracao(),
			getIdPrefixoNumeracao(),
			getTxtMascaraNumeracao(),
			getNrUltimaNumeracao(),
			getTpProcesso());
	}

}