package com.tivic.manager.ctb;

public class LancamentoCredito {

	private int cdLancamento;
	private int cdContaCredito;
	private int cdHistorico;
	private String nrDocumento;
	private float vlLancamento;
	private String txtHistorico;
	private String txtObservacao;
	private int stLancamento;

	public LancamentoCredito(int cdLancamento,
			int cdContaCredito,
			int cdHistorico,
			String nrDocumento,
			float vlLancamento,
			String txtHistorico,
			String txtObservacao,
			int stLancamento){
		setCdLancamento(cdLancamento);
		setCdContaCredito(cdContaCredito);
		setCdHistorico(cdHistorico);
		setNrDocumento(nrDocumento);
		setVlLancamento(vlLancamento);
		setTxtHistorico(txtHistorico);
		setTxtObservacao(txtObservacao);
		setStLancamento(stLancamento);
	}
	public void setCdLancamento(int cdLancamento){
		this.cdLancamento=cdLancamento;
	}
	public int getCdLancamento(){
		return this.cdLancamento;
	}
	public void setCdContaCredito(int cdContaCredito){
		this.cdContaCredito=cdContaCredito;
	}
	public int getCdContaCredito(){
		return this.cdContaCredito;
	}
	public void setCdHistorico(int cdHistorico){
		this.cdHistorico=cdHistorico;
	}
	public int getCdHistorico(){
		return this.cdHistorico;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setVlLancamento(float vlLancamento){
		this.vlLancamento=vlLancamento;
	}
	public float getVlLancamento(){
		return this.vlLancamento;
	}
	public void setTxtHistorico(String txtHistorico){
		this.txtHistorico=txtHistorico;
	}
	public String getTxtHistorico(){
		return this.txtHistorico;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setStLancamento(int stLancamento){
		this.stLancamento=stLancamento;
	}
	public int getStLancamento(){
		return this.stLancamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLancamento: " +  getCdLancamento();
		valueToString += ", cdContaCredito: " +  getCdContaCredito();
		valueToString += ", cdHistorico: " +  getCdHistorico();
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", vlLancamento: " +  getVlLancamento();
		valueToString += ", txtHistorico: " +  getTxtHistorico();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", stLancamento: " +  getStLancamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LancamentoCredito(getCdLancamento(),
			getCdContaCredito(),
			getCdHistorico(),
			getNrDocumento(),
			getVlLancamento(),
			getTxtHistorico(),
			getTxtObservacao(),
			getStLancamento());
	}

}
