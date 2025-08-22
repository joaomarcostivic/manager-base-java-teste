package com.tivic.manager.adm;

public class NivelCobranca {

	private int cdNivelCobranca;
	private int cdCobranca;
	private String idNivelCobranca;
	private String nmNivelCobranca;
	private String nmDescricao;
	private int qtDiasAposVencimento;
	private int qtDiasEntreAvisoCobranca;
	private int lgCobrarJuros;
	private float prTaxaJuros;
	private int lgEncargosMora;
	private float prEncargosMora;
	private int stNivelCobranca;
	private int lgSuspenderCredito;
	private int lgCancelarCredito;
	private String txtNota;

	public NivelCobranca(int cdNivelCobranca,
			int cdCobranca,
			String idNivelCobranca,
			String nmNivelCobranca,
			String nmDescricao,
			int qtDiasAposVencimento,
			int qtDiasEntreAvisoCobranca,
			int lgCobrarJuros,
			float prTaxaJuros,
			int lgEncargosMora,
			float prEncargosMora,
			int stNivelCobranca,
			int lgSuspenderCredito,
			int lgCancelarCredito,
			String txtNota){
		setCdNivelCobranca(cdNivelCobranca);
		setCdCobranca(cdCobranca);
		setIdNivelCobranca(idNivelCobranca);
		setNmNivelCobranca(nmNivelCobranca);
		setNmDescricao(nmDescricao);
		setQtDiasAposVencimento(qtDiasAposVencimento);
		setQtDiasEntreAvisoCobranca(qtDiasEntreAvisoCobranca);
		setLgCobrarJuros(lgCobrarJuros);
		setPrTaxaJuros(prTaxaJuros);
		setLgEncargosMora(lgEncargosMora);
		setPrEncargosMora(prEncargosMora);
		setStNivelCobranca(stNivelCobranca);
		setLgSuspenderCredito(lgSuspenderCredito);
		setLgCancelarCredito(lgCancelarCredito);
		setTxtNota(txtNota);
	}
	public void setCdNivelCobranca(int cdNivelCobranca){
		this.cdNivelCobranca=cdNivelCobranca;
	}
	public int getCdNivelCobranca(){
		return this.cdNivelCobranca;
	}
	public void setCdCobranca(int cdCobranca){
		this.cdCobranca=cdCobranca;
	}
	public int getCdCobranca(){
		return this.cdCobranca;
	}
	public void setIdNivelCobranca(String idNivelCobranca){
		this.idNivelCobranca=idNivelCobranca;
	}
	public String getIdNivelCobranca(){
		return this.idNivelCobranca;
	}
	public void setNmNivelCobranca(String nmNivelCobranca){
		this.nmNivelCobranca=nmNivelCobranca;
	}
	public String getNmNivelCobranca(){
		return this.nmNivelCobranca;
	}
	public void setNmDescricao(String nmDescricao){
		this.nmDescricao=nmDescricao;
	}
	public String getNmDescricao(){
		return this.nmDescricao;
	}
	public void setQtDiasAposVencimento(int qtDiasAposVencimento){
		this.qtDiasAposVencimento=qtDiasAposVencimento;
	}
	public int getQtDiasAposVencimento(){
		return this.qtDiasAposVencimento;
	}
	public void setQtDiasEntreAvisoCobranca(int qtDiasEntreAvisoCobranca){
		this.qtDiasEntreAvisoCobranca=qtDiasEntreAvisoCobranca;
	}
	public int getQtDiasEntreAvisoCobranca(){
		return this.qtDiasEntreAvisoCobranca;
	}
	public void setLgCobrarJuros(int lgCobrarJuros){
		this.lgCobrarJuros=lgCobrarJuros;
	}
	public int getLgCobrarJuros(){
		return this.lgCobrarJuros;
	}
	public void setPrTaxaJuros(float prTaxaJuros){
		this.prTaxaJuros=prTaxaJuros;
	}
	public float getPrTaxaJuros(){
		return this.prTaxaJuros;
	}
	public void setLgEncargosMora(int lgEncargosMora){
		this.lgEncargosMora=lgEncargosMora;
	}
	public int getLgEncargosMora(){
		return this.lgEncargosMora;
	}
	public void setPrEncargosMora(float prEncargosMora){
		this.prEncargosMora=prEncargosMora;
	}
	public float getPrEncargosMora(){
		return this.prEncargosMora;
	}
	public void setStNivelCobranca(int stNivelCobranca){
		this.stNivelCobranca=stNivelCobranca;
	}
	public int getStNivelCobranca(){
		return this.stNivelCobranca;
	}
	public void setLgSuspenderCredito(int lgSuspenderCredito){
		this.lgSuspenderCredito=lgSuspenderCredito;
	}
	public int getLgSuspenderCredito(){
		return this.lgSuspenderCredito;
	}
	public void setLgCancelarCredito(int lgCancelarCredito){
		this.lgCancelarCredito=lgCancelarCredito;
	}
	public int getLgCancelarCredito(){
		return this.lgCancelarCredito;
	}
	public void setTxtNota(String txtNota){
		this.txtNota=txtNota;
	}
	public String getTxtNota(){
		return this.txtNota;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNivelCobranca: " +  getCdNivelCobranca();
		valueToString += ", cdCobranca: " +  getCdCobranca();
		valueToString += ", idNivelCobranca: " +  getIdNivelCobranca();
		valueToString += ", nmNivelCobranca: " +  getNmNivelCobranca();
		valueToString += ", nmDescricao: " +  getNmDescricao();
		valueToString += ", qtDiasAposVencimento: " +  getQtDiasAposVencimento();
		valueToString += ", qtDiasEntreAvisoCobranca: " +  getQtDiasEntreAvisoCobranca();
		valueToString += ", lgCobrarJuros: " +  getLgCobrarJuros();
		valueToString += ", prTaxaJuros: " +  getPrTaxaJuros();
		valueToString += ", lgEncargosMora: " +  getLgEncargosMora();
		valueToString += ", prEncargosMora: " +  getPrEncargosMora();
		valueToString += ", stNivelCobranca: " +  getStNivelCobranca();
		valueToString += ", lgSuspenderCredito: " +  getLgSuspenderCredito();
		valueToString += ", lgCancelarCredito: " +  getLgCancelarCredito();
		valueToString += ", txtNota: " +  getTxtNota();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NivelCobranca(getCdNivelCobranca(),
			getCdCobranca(),
			getIdNivelCobranca(),
			getNmNivelCobranca(),
			getNmDescricao(),
			getQtDiasAposVencimento(),
			getQtDiasEntreAvisoCobranca(),
			getLgCobrarJuros(),
			getPrTaxaJuros(),
			getLgEncargosMora(),
			getPrEncargosMora(),
			getStNivelCobranca(),
			getLgSuspenderCredito(),
			getLgCancelarCredito(),
			getTxtNota());
	}

}