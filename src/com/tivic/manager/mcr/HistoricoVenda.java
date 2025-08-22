package com.tivic.manager.mcr;

public class HistoricoVenda {

	private int cdEmpreendimento;
	private int tpConceitoMes1;
	private int tpConceitoMes2;
	private int tpConceitoMes3;
	private int tpConceitoMes4;
	private int tpConceitoMes5;
	private int tpConceitoMes6;
	private int tpConceitoMes7;
	private int tpConceitoMes8;
	private int tpConceitoMes9;
	private int tpConceitoMes10;
	private int tpConceitoMes11;
	private int tpConceitoMes12;

	public HistoricoVenda(int cdEmpreendimento,
			int tpConceitoMes1,
			int tpConceitoMes2,
			int tpConceitoMes3,
			int tpConceitoMes4,
			int tpConceitoMes5,
			int tpConceitoMes6,
			int tpConceitoMes7,
			int tpConceitoMes8,
			int tpConceitoMes9,
			int tpConceitoMes10,
			int tpConceitoMes11,
			int tpConceitoMes12){
		setCdEmpreendimento(cdEmpreendimento);
		setTpConceitoMes1(tpConceitoMes1);
		setTpConceitoMes2(tpConceitoMes2);
		setTpConceitoMes3(tpConceitoMes3);
		setTpConceitoMes4(tpConceitoMes4);
		setTpConceitoMes5(tpConceitoMes5);
		setTpConceitoMes6(tpConceitoMes6);
		setTpConceitoMes7(tpConceitoMes7);
		setTpConceitoMes8(tpConceitoMes8);
		setTpConceitoMes9(tpConceitoMes9);
		setTpConceitoMes10(tpConceitoMes10);
		setTpConceitoMes11(tpConceitoMes11);
		setTpConceitoMes12(tpConceitoMes12);
	}
	public void setCdEmpreendimento(int cdEmpreendimento){
		this.cdEmpreendimento=cdEmpreendimento;
	}
	public int getCdEmpreendimento(){
		return this.cdEmpreendimento;
	}
	public void setTpConceitoMes1(int tpConceitoMes1){
		this.tpConceitoMes1=tpConceitoMes1;
	}
	public int getTpConceitoMes1(){
		return this.tpConceitoMes1;
	}
	public void setTpConceitoMes2(int tpConceitoMes2){
		this.tpConceitoMes2=tpConceitoMes2;
	}
	public int getTpConceitoMes2(){
		return this.tpConceitoMes2;
	}
	public void setTpConceitoMes3(int tpConceitoMes3){
		this.tpConceitoMes3=tpConceitoMes3;
	}
	public int getTpConceitoMes3(){
		return this.tpConceitoMes3;
	}
	public void setTpConceitoMes4(int tpConceitoMes4){
		this.tpConceitoMes4=tpConceitoMes4;
	}
	public int getTpConceitoMes4(){
		return this.tpConceitoMes4;
	}
	public void setTpConceitoMes5(int tpConceitoMes5){
		this.tpConceitoMes5=tpConceitoMes5;
	}
	public int getTpConceitoMes5(){
		return this.tpConceitoMes5;
	}
	public void setTpConceitoMes6(int tpConceitoMes6){
		this.tpConceitoMes6=tpConceitoMes6;
	}
	public int getTpConceitoMes6(){
		return this.tpConceitoMes6;
	}
	public void setTpConceitoMes7(int tpConceitoMes7){
		this.tpConceitoMes7=tpConceitoMes7;
	}
	public int getTpConceitoMes7(){
		return this.tpConceitoMes7;
	}
	public void setTpConceitoMes8(int tpConceitoMes8){
		this.tpConceitoMes8=tpConceitoMes8;
	}
	public int getTpConceitoMes8(){
		return this.tpConceitoMes8;
	}
	public void setTpConceitoMes9(int tpConceitoMes9){
		this.tpConceitoMes9=tpConceitoMes9;
	}
	public int getTpConceitoMes9(){
		return this.tpConceitoMes9;
	}
	public void setTpConceitoMes10(int tpConceitoMes10){
		this.tpConceitoMes10=tpConceitoMes10;
	}
	public int getTpConceitoMes10(){
		return this.tpConceitoMes10;
	}
	public void setTpConceitoMes11(int tpConceitoMes11){
		this.tpConceitoMes11=tpConceitoMes11;
	}
	public int getTpConceitoMes11(){
		return this.tpConceitoMes11;
	}
	public void setTpConceitoMes12(int tpConceitoMes12){
		this.tpConceitoMes12=tpConceitoMes12;
	}
	public int getTpConceitoMes12(){
		return this.tpConceitoMes12;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpreendimento: " +  getCdEmpreendimento();
		valueToString += ", tpConceitoMes1: " +  getTpConceitoMes1();
		valueToString += ", tpConceitoMes2: " +  getTpConceitoMes2();
		valueToString += ", tpConceitoMes3: " +  getTpConceitoMes3();
		valueToString += ", tpConceitoMes4: " +  getTpConceitoMes4();
		valueToString += ", tpConceitoMes5: " +  getTpConceitoMes5();
		valueToString += ", tpConceitoMes6: " +  getTpConceitoMes6();
		valueToString += ", tpConceitoMes7: " +  getTpConceitoMes7();
		valueToString += ", tpConceitoMes8: " +  getTpConceitoMes8();
		valueToString += ", tpConceitoMes9: " +  getTpConceitoMes9();
		valueToString += ", tpConceitoMes10: " +  getTpConceitoMes10();
		valueToString += ", tpConceitoMes11: " +  getTpConceitoMes11();
		valueToString += ", tpConceitoMes12: " +  getTpConceitoMes12();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new HistoricoVenda(cdEmpreendimento,
			tpConceitoMes1,
			tpConceitoMes2,
			tpConceitoMes3,
			tpConceitoMes4,
			tpConceitoMes5,
			tpConceitoMes6,
			tpConceitoMes7,
			tpConceitoMes8,
			tpConceitoMes9,
			tpConceitoMes10,
			tpConceitoMes11,
			tpConceitoMes12);
	}

}