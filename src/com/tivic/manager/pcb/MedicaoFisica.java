package com.tivic.manager.pcb;

public class MedicaoFisica {

	private int cdConta;
	private int cdFechamento;
	private int cdTanque;
	private float vlRegua;
	private float qtVolume;
	private float qtEstoqueEscritural;
	private float qtCapacidade;
	private int cdCombustivel;

	public MedicaoFisica(int cdConta,
			int cdFechamento,
			int cdTanque,
			float vlRegua,
			float qtVolume,
			float qtEstoqueEscritural,
			float qtCapacidade,
			int cdCombustivel){
		setCdConta(cdConta);
		setCdFechamento(cdFechamento);
		setCdTanque(cdTanque);
		setVlRegua(vlRegua);
		setQtVolume(qtVolume);
		setQtEstoqueEscritural(qtEstoqueEscritural);
		setQtCapacidade(qtCapacidade);
		setCdCombustivel(cdCombustivel);
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
	public void setVlRegua(float vlRegua){
		this.vlRegua=vlRegua;
	}
	public float getVlRegua(){
		return this.vlRegua;
	}
	public void setQtVolume(float qtVolume){
		this.qtVolume=qtVolume;
	}
	public float getQtVolume()	{
		return this.qtVolume;
	}
	public void setQtEstoqueEscritural(float qtEstoqueEscritural){
		this.qtEstoqueEscritural=qtEstoqueEscritural;
	}
	public float getQtEstoqueEscritural()	{
		return this.qtEstoqueEscritural;
	}
	public void setQtCapacidade(float qtCapacidade){
		this.qtCapacidade=qtCapacidade;
	}
	public float getQtCapacidade(){
		return this.qtCapacidade;
	}
	public void setCdCombustivel(int cdCombustivel){
		this.cdCombustivel=cdCombustivel;
	}
	public int getCdCombustivel(){
		return this.cdCombustivel;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConta: " +  getCdConta();
		valueToString += ", cdFechamento: " +  getCdFechamento();
		valueToString += ", cdTanque: " +  getCdTanque();
		valueToString += ", vlRegua: " +  getVlRegua();
		valueToString += ", qtVolume: " +  getQtVolume();
		valueToString += ", qtEstoqueEscritural: " +  getQtEstoqueEscritural();
		valueToString += ", qtCapacidade: " +  getQtCapacidade();
		valueToString += ", cdCombustivel: " +  getCdCombustivel();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MedicaoFisica(getCdConta(),
			getCdFechamento(),
			getCdTanque(),
			getVlRegua(),
			getQtVolume(),
			getQtEstoqueEscritural(),
			getQtCapacidade(),
			getCdCombustivel());
	}

}
