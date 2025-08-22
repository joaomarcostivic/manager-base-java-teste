package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AfericaoCatraca {

	private int cdAfericaoCatraca;
	private int cdConcessaoVeiculo;
	private int cdLacre;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtAfericao;
	private int qtAferido;
	private double qtHodometro;
	private int lgHodometroIlegivel;
	private String txtObservacao;
	private int cdAgente;
	private int cdUsuario;
	private int tpLeitura;

	public AfericaoCatraca(){ }

	public AfericaoCatraca(int cdAfericaoCatraca,
			int cdConcessaoVeiculo,
			int cdLacre,
			GregorianCalendar dtAfericao,
			int qtAferido,
			double qtHodometro,
			int lgHodometroIlegivel,
			String txtObservacao,
			int cdAgente,
			int cdUsuario,
			int tpLeitura){
		setCdAfericaoCatraca(cdAfericaoCatraca);
		setCdConcessaoVeiculo(cdConcessaoVeiculo);
		setCdLacre(cdLacre);
		setDtAfericao(dtAfericao);
		setQtAferido(qtAferido);
		setQtHodometro(qtHodometro);
		setLgHodometroIlegivel(lgHodometroIlegivel);
		setTxtObservacao(txtObservacao);
		setCdAgente(cdAgente);
		setCdUsuario(cdUsuario);
		setTpLeitura(tpLeitura);
	}
	public void setCdAfericaoCatraca(int cdAfericaoCatraca){
		this.cdAfericaoCatraca=cdAfericaoCatraca;
	}
	public int getCdAfericaoCatraca(){
		return this.cdAfericaoCatraca;
	}
	public void setCdConcessaoVeiculo(int cdConcessaoVeiculo){
		this.cdConcessaoVeiculo=cdConcessaoVeiculo;
	}
	public int getCdConcessaoVeiculo(){
		return this.cdConcessaoVeiculo;
	}
	public void setCdLacre(int cdLacre){
		this.cdLacre=cdLacre;
	}
	public int getCdLacre(){
		return this.cdLacre;
	}
	public void setDtAfericao(GregorianCalendar dtAfericao){
		this.dtAfericao=dtAfericao;
	}
	public GregorianCalendar getDtAfericao(){
		return this.dtAfericao;
	}
	public void setQtAferido(int qtAferido){
		this.qtAferido=qtAferido;
	}
	public int getQtAferido(){
		return this.qtAferido;
	}
	public void setQtHodometro(double qtHodometro){
		this.qtHodometro=qtHodometro;
	}
	public double getQtHodometro(){
		return this.qtHodometro;
	}
	public void setLgHodometroIlegivel(int lgHodometroIlegivel){
		this.lgHodometroIlegivel=lgHodometroIlegivel;
	}
	public int getLgHodometroIlegivel(){
		return this.lgHodometroIlegivel;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setTpLeitura(int tpLeitura){
		this.tpLeitura=tpLeitura;
	}
	public int getTpLeitura(){
		return this.tpLeitura;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAfericaoCatraca: " +  getCdAfericaoCatraca();
		valueToString += ", cdConcessaoVeiculo: " +  getCdConcessaoVeiculo();
		valueToString += ", cdLacre: " +  getCdLacre();
		valueToString += ", dtAfericao: " +  sol.util.Util.formatDateTime(getDtAfericao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", qtAferido: " +  getQtAferido();
		valueToString += ", qtHodometro: " +  getQtHodometro();
		valueToString += ", lgHodometroIlegivel: " +  getLgHodometroIlegivel();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", tpLeitura: " +  getTpLeitura();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AfericaoCatraca(getCdAfericaoCatraca(),
			getCdConcessaoVeiculo(),
			getCdLacre(),
			getDtAfericao()==null ? null : (GregorianCalendar)getDtAfericao().clone(),
			getQtAferido(),
			getQtHodometro(),
			getLgHodometroIlegivel(),
			getTxtObservacao(),
			getCdAgente(),
			getCdUsuario(),
			getTpLeitura());
	}

}