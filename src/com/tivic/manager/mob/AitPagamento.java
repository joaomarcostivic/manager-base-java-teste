package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AitPagamento {

	private int cdAit;
	private int cdPagamento;
	private Double vlTarifa;
	private Double vlPago;
	private String nrBanco;
	private String nrAgencia;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPagamento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCredito;
	private int tpArrecadacao;
	private String nrContaCredito;
	private String nrAgenciaCredito;
	private int tpCondicionalidade;
	private int tpPagamento;
	private int tpModalidade;
	private String ufPagamento;
	private String nrDocumento;
	private Double vlRepasse;
	private int cdContaReceber;
	private Double vlDetranArrecadador;
	private Double vlFunset;
	private Double vlDenatran;
	private Double vlOrgao;
	private String nrErro;
	private int cdArquivo;
	private int stPagamento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCancelamento;
	private int cdMovimento;
	
	public AitPagamento() {}
	
	public AitPagamento(int cdAit, Double vlTarifa, Double vlPago, String nrBanco, String nrAgencia, 
			GregorianCalendar dtPagamento, GregorianCalendar dtCredito) {
		setCdAit(cdAit);
		setVlTarifa(vlTarifa);
		setVlPago(vlPago);
		setNrBanco(nrBanco);
		setNrAgencia(nrAgencia);
		setDtPagamento(dtPagamento);
		setDtCredito(dtCredito);
	}
	
	public AitPagamento(int cdAit, int cdPagamento, Double vlTarifa, String nrBanco, String nrAgencia, 
			GregorianCalendar dtPagamento, GregorianCalendar dtCredito, int tpArrecadacao, 
			String nrContaCredito, String nrAgenciaCredito, int tpCondicionalidade, int tpPagamento, 
			int tpModalidade, Double vlPago, String ufPagamento, String nrDocumento, Double vlRepasse, 
			int cdContaReceber, Double vlDetranArrecadador, Double vlFunset, Double vlDenatran, 
			Double vlOrgao, String nrErro, int cdArquivo, int stPagamento, GregorianCalendar dtCancelamento, int cdMovimento) {
		setCdAit(cdAit);	
		setCdPagamento(cdPagamento);	
		setVlTarifa(vlTarifa);	
		setVlPago(vlPago);	
		setNrBanco(nrBanco);	
		setNrAgencia(nrAgencia);	
		setDtPagamento(dtPagamento);	
		setDtCredito(dtCredito);	
		setTpArrecadacao(tpArrecadacao);	
		setNrContaCredito(nrContaCredito);	
		setNrAgenciaCredito(nrAgenciaCredito);	
		setTpCondicionalidade(tpCondicionalidade);	
		setTpPagamento(tpPagamento);	
		settpModalidade(tpModalidade);	
		setUfPagamento(ufPagamento);	
		setNrDocumento(nrDocumento);	
		setVlRepasse(vlRepasse);	
		setCdContaReceber(cdContaReceber);
		setVlDetranArrecadador(vlDetranArrecadador);	
		setVlFunset(vlFunset);	
		setVlDenatran(vlDenatran);	
		setVlOrgao(vlOrgao);	
		setNrErro(nrErro);
		setCdArquivo(cdArquivo);
		setStPagamento(stPagamento);
		setDtCancelamento(dtCancelamento);
		setCdMovimento(cdMovimento);
	}
		
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	
	public void setCdPagamento(int cdPagamento) {
		this.cdPagamento = cdPagamento;
	}
	
	public int getCdPagamento() {
		return this.cdPagamento;
	}
	
	public int getCdArquivo() {
		return cdArquivo;
	}

	public void setCdArquivo(int cdArquivo) {
		this.cdArquivo = cdArquivo;
	}

	public void setVlTarifa(Double vlTarifa){
		this.vlTarifa=vlTarifa;
	}
	public Double getVlTarifa(){
		return this.vlTarifa;
	}
	public void setNrBanco(String nrBanco){
		this.nrBanco=nrBanco;
	}
	public String getNrBanco(){
		return this.nrBanco;
	}
	public void setNrAgencia(String nrAgencia){
		this.nrAgencia=nrAgencia;
	}
	public String getNrAgencia(){
		return this.nrAgencia;
	}
	public void setDtPagamento(GregorianCalendar dtPagamento){
		this.dtPagamento=dtPagamento;
	}
	public GregorianCalendar getDtPagamento(){
		return this.dtPagamento;
	}
	public void setDtCredito(GregorianCalendar dtCredito){
		this.dtCredito=dtCredito;
	}
	public GregorianCalendar getDtCredito(){
		return this.dtCredito;
	}
	
	public int getTpArrecadacao() {
		return this.tpArrecadacao;
	}
	
	public void setTpArrecadacao(int tpArrecadacao) {
		this.tpArrecadacao = tpArrecadacao;
	}
	
	public String getNrContaCredito() {
		return this.nrContaCredito;
	}
	
	public void setNrContaCredito(String nrContaCredito) {
		this.nrContaCredito = nrContaCredito;
	}
	
	public String getNrAgenciaCredito() {
		return this.nrAgenciaCredito;
	}
	
	public void setNrAgenciaCredito(String nrAgenciaCredito) {
		this.nrAgenciaCredito = nrAgenciaCredito;
	}
	
	public int getTpCondicionalidade() {
		return this.tpCondicionalidade;
	}
	
	public void setTpCondicionalidade(int tpCondicionalidade) {
		this.tpCondicionalidade = tpCondicionalidade;
	}
	
	public int getTpPagamento() {
		return this.tpPagamento;
	}
	
	public void setTpPagamento(int tpPagamento) {
		this.tpPagamento = tpPagamento;
	}
	
	public int getTpModalidade() {
		return this.tpModalidade;
	}
	
	public void settpModalidade(int tpModalidade) {
		this.tpModalidade = tpModalidade;
	}
	
	public String getUfPagamento() {
		return this.ufPagamento;
	}
	
	public void setUfPagamento(String ufPagamento) {
		this.ufPagamento = ufPagamento;
	}
	
	public String getNrDocumento() {
		return this.nrDocumento;
	}
	
	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}
	
	public Double getVlRepasse() {
		return this.vlRepasse;
	}
	
	public void setVlRepasse(Double vlRepasse) {
		this.vlRepasse = vlRepasse;
	}
	
	public int getCdContaReceber() {
		return this.cdContaReceber;
	}
	
	public void setCdContaReceber(int cdContaReceber) {
		this.cdContaReceber = cdContaReceber;
	}

	public Double getVlDetranArrecadador() {
		return this.vlDetranArrecadador;
	}
	
	public void setVlDetranArrecadador(Double vlDetranArrecadador) {
		this.vlDetranArrecadador = vlDetranArrecadador;
	}
	
	public Double getVlFunset() {
		return this.vlFunset;
	}
	
	public void setVlFunset(Double vlFunset) {
		this.vlFunset = vlFunset;
	}
	
	public Double getVlDenatran() {
		return this.vlDenatran;
	}
	
	public void setVlDenatran(Double vlDenatran) {
		this.vlDenatran = vlDenatran;
	}
	
	public Double getVlOrgao() {
		return this.vlOrgao;
	}
	
	public void setVlOrgao(Double vlOrgao) {
		this.vlOrgao = vlOrgao;
	}
	
	public Double getVlPago() {
		return this.vlPago;
	}
	
	public void setVlPago(Double vlPago) {
		this.vlPago = vlPago;
	}
	
	public String getNrErro() {
		return this.nrErro;
	}
	
	public void setNrErro(String nrErro) {
		this.nrErro = nrErro;
	}
	
	public int getStPagamento() {
		return stPagamento;
	}
	
	public void setStPagamento(int stPagamento) {
		this.stPagamento = stPagamento;
	}
	
	public GregorianCalendar getDtCancelamento() {
		return dtCancelamento;
	}
	
	public void setDtCancelamento(GregorianCalendar dtCancelamento) {
		this.dtCancelamento = dtCancelamento;
	}
	
	public int getCdMovimento() {
		return cdMovimento;
	}
	
	public void setCdMovimento(int cdMovimento) {
		this.cdMovimento = cdMovimento;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}

	public Object clone() {
		return new AitPagamento(getCdAit(),
			getCdPagamento(),
			getVlTarifa(),

			getNrBanco(),
			getNrAgencia(),
			getDtPagamento()==null ? null : (GregorianCalendar)getDtPagamento().clone(),
			getDtCredito()==null ? null : (GregorianCalendar)getDtCredito().clone(),
			getTpArrecadacao(),
			getNrContaCredito(),
			getNrAgenciaCredito(),
			getTpCondicionalidade(),
			getTpPagamento(),
			getTpModalidade(),
			getVlPago(),
			getUfPagamento(),
			getNrDocumento(),
			getVlRepasse(),
			getCdContaReceber(),
			getVlDetranArrecadador(),
			getVlFunset(),
			getVlDenatran(),
			getVlOrgao(),
			getNrErro(),
			getCdArquivo(),
			getStPagamento(),
			getDtCancelamento(),
			getCdMovimento());
	}

}