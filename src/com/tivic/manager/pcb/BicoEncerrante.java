package com.tivic.manager.pcb;

public class BicoEncerrante {

	private int cdBico;
	private float qtEncerranteInicial;
	private float qtEncerranteFinal;
	private float vlAfericao;
	private float qtLitros;
	private float vlPreco;
	private float vlTotal;
	private int cdConta;
	private int cdFechamento;
	private int cdTanque;
	private int cdCombustivel;

	public BicoEncerrante(int cdBico,
			float qtEncerranteInicial,
			float qtEncerranteFinal,
			float vlAfericao,
			float qtLitros,
			float vlPreco,
			float vlTotal,
			int cdConta,
			int cdFechamento,
			int cdTanque,
			int cdCombustivel){
		setCdBico(cdBico);
		setQtEncerranteInicial(qtEncerranteInicial);
		setQtEncerranteFinal(qtEncerranteFinal);
		setVlAfericao(vlAfericao);
		setQtLitros(qtLitros);
		setVlPreco(vlPreco);
		setVlTotal(vlTotal);
		setCdConta(cdConta);
		setCdFechamento(cdFechamento);
		setCdTanque(cdTanque);
		setCdCombustivel(cdCombustivel);
	}
	public void setCdBico(int cdBico){
		this.cdBico=cdBico;
	}
	public int getCdBico(){
		return this.cdBico;
	}
	public void setQtEncerranteInicial(float qtEncerranteInicial){
		this.qtEncerranteInicial=qtEncerranteInicial;
	}
	public float getQtEncerranteInicial(){
		return this.qtEncerranteInicial;
	}
	public void setQtEncerranteFinal(float qtEncerranteFinal){
		this.qtEncerranteFinal=qtEncerranteFinal;
	}
	public float getQtEncerranteFinal(){
		return this.qtEncerranteFinal;
	}
	public void setVlAfericao(float vlAfericao){
		this.vlAfericao=vlAfericao;
	}
	public float getVlAfericao(){
		return this.vlAfericao;
	}
	public void setQtLitros(float qtLitros){
		this.qtLitros=qtLitros;
	}
	public float getQtLitros(){
		return this.qtLitros;
	}
	public void setVlPreco(float vlPreco){
		this.vlPreco=vlPreco;
	}
	public float getVlPreco(){
		return this.vlPreco;
	}
	public void setVlTotal(float vlTotal){
		this.vlTotal=vlTotal;
	}
	public float getVlTotal(){
		return this.vlTotal;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdFechamento(int cdFechamento){
		this.cdFechamento=cdFechamento;
	}
	public int getCdFechamento(){
		return this.cdFechamento;
	}
	public void setCdTanque(int cdTanque){
		this.cdTanque=cdTanque;
	}
	public int getCdTanque(){
		return this.cdTanque;
	}
	public void setCdCombustivel(int cdCombustivel){
		this.cdCombustivel=cdCombustivel;
	}
	public int getCdCombustivel(){
		return this.cdCombustivel;
	}
	public String toString() {
		String valueToString = "";
		valueToString += ", cdBico: " +  getCdBico();
		valueToString += ", qtEncerranteInicial: " +  getQtEncerranteInicial();
		valueToString += ", qtEncerranteFinal: " +  getQtEncerranteFinal();
		valueToString += ", vlAfericao: " +  getVlAfericao();
		valueToString += ", qtLitros: " +  getQtLitros();
		valueToString += ", vlPreco: " +  getVlPreco();
		valueToString += ", vlTotal: " +  getVlTotal();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", cdFechamento: " +  getCdFechamento();
		valueToString += ", cdTanque: " +  getCdTanque();
		valueToString += ", cdCombustivel: " +  getCdCombustivel();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BicoEncerrante(getCdBico(),
			getQtEncerranteInicial(),
			getQtEncerranteFinal(),
			getVlAfericao(),
			getQtLitros(),
			getVlPreco(),
			getVlTotal(),
			getCdConta(),
			getCdFechamento(),
			getCdTanque(),
			getCdCombustivel());
	}

}
