package com.tivic.manager.adm;

public class ContaFactoring {

	private int cdContaFactoring;
	private int cdContaPagar;
	private int cdContaReceberAntecipada;
	private int qtDias;
	private float prJuros;
	private float vlDesconto;
	private int cdContaReceber;
	
	public ContaFactoring(int cdContaFactoring,
			int cdContaPagar,
			int cdContaReceberAntecipada,
			int qtDias,
			float prJuros,
			float vlDesconto,
			int cdContaReceber){
		setCdContaFactoring(cdContaFactoring);
		setCdContaPagar(cdContaPagar);
		setCdContaReceberAntecipada(cdContaReceberAntecipada);
		setQtDias(qtDias);
		setPrJuros(prJuros);
		setVlDesconto(vlDesconto);
		setCdContaReceber(cdContaReceber);
	}
	
	public ContaFactoring(int cdContaFactoring,
			int cdContaPagar,
			int cdContaReceberAntecipada,
			int qtDias,
			float prJuros,
			float vlDesconto){
		setCdContaFactoring(cdContaFactoring);
		setCdContaPagar(cdContaPagar);
		setCdContaReceberAntecipada(cdContaReceberAntecipada);
		setQtDias(qtDias);
		setPrJuros(prJuros);
		setVlDesconto(vlDesconto);
	}
	public ContaFactoring(int cdContaFactoring,
			int cdContaReceberAntecipada,
			int qtDias,
			float prJuros,
			float vlDesconto, 
			int cdContaReceber){
		setCdContaFactoring(cdContaFactoring);
		setCdContaReceberAntecipada(cdContaReceberAntecipada);
		setQtDias(qtDias);
		setPrJuros(prJuros);
		setVlDesconto(vlDesconto);
		setCdContaReceber(cdContaReceber);
	}
	
	public void setCdContaFactoring(int cdContaFactoring){
		this.cdContaFactoring=cdContaFactoring;
	}
	public int getCdContaFactoring(){
		return this.cdContaFactoring;
	}
	public void setCdContaPagar(int cdContaPagar){
		this.cdContaPagar=cdContaPagar;
	}
	public int getCdContaPagar(){
		return this.cdContaPagar;
	}
	public void setCdContaReceberAntecipada(int cdContaReceberAntecipada){
		this.cdContaReceberAntecipada=cdContaReceberAntecipada;
	}
	public int getCdContaReceberAntecipada(){
		return this.cdContaReceberAntecipada;
	}
	public void setQtDias(int qtDias){
		this.qtDias=qtDias;
	}
	public int getQtDias(){
		return this.qtDias;
	}
	public void setPrJuros(float prJuros){
		this.prJuros=prJuros;
	}
	public float getPrJuros(){
		return this.prJuros;
	}
	public void setVlDesconto(float vlDesconto){
		this.vlDesconto=vlDesconto;
	}
	public float getVlDesconto(){
		return this.vlDesconto;
	}
	public void setCdContaReceber(int cdContaReceber) {
		this.cdContaReceber = cdContaReceber;
	}
	public int getCdContaReceber() {
		return cdContaReceber;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContaFactoring: " +  getCdContaFactoring();
		valueToString += ", cdContaPagar: " +  getCdContaPagar();
		valueToString += ", cdContaReceberAntecipada: " +  getCdContaReceberAntecipada();
		valueToString += ", qtDias: " +  getQtDias();
		valueToString += ", prJuros: " +  getPrJuros();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaFactoring(getCdContaFactoring(),
			getCdContaPagar(),
			getCdContaReceberAntecipada(),
			getQtDias(),
			getPrJuros(),
			getVlDesconto(),
			getCdContaReceber());
	}

}