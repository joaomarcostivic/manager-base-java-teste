package com.tivic.manager.bdv;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.util.JsonToStringBuilder;

public class Veiculo {

	private int cdVeiculo;
	private String nrPlaca;
	private String nrRenavan;
	private int tpVeiculo;
	private int tpCarroceria;
	private int nrAnoModelo;
	private int nrAnoFabricacao;
	private int nrCodigoMunicipio;
	private String nmMunicipio;
	private String sgEstado;
	private int nrCodigoMarca;
	private String nmMarcaModelo;
	private int nrCodigoCor;
	private String nmCor;
	private int nrCodigoEspecie;
	private String nmEspecie;
	private String nmTipo;
	private String nmCategoria;
	private GregorianCalendar dtInformacao;
	private String nrChassi;
	
	private List<Restricao> restricoes;
	private List<Debito> debitos;
	private List<Proprietario> proprietarios;

	public Veiculo() { }

	public Veiculo(int cdVeiculo,
			String nrPlaca,
			String nrRenavan,
			int tpVeiculo,
			int tpCarroceria,
			int nrAnoModelo,
			int nrAnoFabricacao,
			int nrCodigoMunicipio,
			String nmMunicipio,
			String sgEstado,
			int nrCodigoMarca,
			String nmMarcaModelo,
			int nrCodigoCor,
			String nmCor,
			int nrCodigoEspecie,
			String nmEspecie,
			String nmTipo,
			String nmCategoria,
			GregorianCalendar dtInformacao,
			String nrChassi) {
		setCdVeiculo(cdVeiculo);
		setNrPlaca(nrPlaca);
		setNrRenavan(nrRenavan);
		setTpVeiculo(tpVeiculo);
		setTpCarroceria(tpCarroceria);
		setNrAnoModelo(nrAnoModelo);
		setNrAnoFabricacao(nrAnoFabricacao);
		setNrCodigoMunicipio(nrCodigoMunicipio);
		setNmMunicipio(nmMunicipio);
		setSgEstado(sgEstado);
		setNrCodigoMarca(nrCodigoMarca);
		setNmMarcaModelo(nmMarcaModelo);
		setNrCodigoCor(nrCodigoCor);
		setNmCor(nmCor);
		setNrCodigoEspecie(nrCodigoEspecie);
		setNmEspecie(nmEspecie);
		setNmTipo(nmTipo);
		setNmCategoria(nmCategoria);
		setDtInformacao(dtInformacao);
		setNrChassi(nrChassi);
	}
	
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setNrPlaca(String nrPlaca){
		this.nrPlaca=nrPlaca;
	}
	public String getNrPlaca(){
		return this.nrPlaca;
	}
	public void setNrRenavan(String nrRenavan){
		this.nrRenavan=nrRenavan;
	}
	public String getNrRenavan(){
		return this.nrRenavan;
	}
	public void setTpVeiculo(int tpVeiculo){
		this.tpVeiculo=tpVeiculo;
	}
	public int getTpVeiculo(){
		return this.tpVeiculo;
	}
	public void setTpCarroceria(int tpCarroceria){
		this.tpCarroceria=tpCarroceria;
	}
	public int getTpCarroceria(){
		return this.tpCarroceria;
	}
	public void setNrAnoModelo(int nrAnoModelo){
		this.nrAnoModelo=nrAnoModelo;
	}
	public int getNrAnoModelo(){
		return this.nrAnoModelo;
	}
	public void setNrAnoFabricacao(int nrAnoFabricacao){
		this.nrAnoFabricacao=nrAnoFabricacao;
	}
	public int getNrAnoFabricacao(){
		return this.nrAnoFabricacao;
	}
	public void setNrCodigoMunicipio(int nrCodigoMunicipio){
		this.nrCodigoMunicipio=nrCodigoMunicipio;
	}
	public int getNrCodigoMunicipio(){
		return this.nrCodigoMunicipio;
	}
	public void setNmMunicipio(String nmMunicipio){
		this.nmMunicipio=nmMunicipio;
	}
	public String getNmMunicipio(){
		return this.nmMunicipio;
	}
	public void setSgEstado(String sgEstado){
		this.sgEstado=sgEstado;
	}
	public String getSgEstado(){
		return this.sgEstado;
	}
	public void setNrCodigoMarca(int nrCodigoMarca){
		this.nrCodigoMarca=nrCodigoMarca;
	}
	public int getNrCodigoMarca(){
		return this.nrCodigoMarca;
	}
	public void setNmMarcaModelo(String nmMarcaModelo){
		this.nmMarcaModelo=nmMarcaModelo;
	}
	public String getNmMarcaModelo(){
		return this.nmMarcaModelo;
	}
	public void setNrCodigoCor(int nrCodigoCor){
		this.nrCodigoCor=nrCodigoCor;
	}
	public int getNrCodigoCor(){
		return this.nrCodigoCor;
	}
	public void setNmCor(String nmCor){
		this.nmCor=nmCor;
	}
	public String getNmCor(){
		return this.nmCor;
	}
	public void setNrCodigoEspecie(int nrCodigoEspecie){
		this.nrCodigoEspecie=nrCodigoEspecie;
	}
	public int getNrCodigoEspecie(){
		return this.nrCodigoEspecie;
	}
	public void setNmEspecie(String nmEspecie){
		this.nmEspecie=nmEspecie;
	}
	public String getNmEspecie(){
		return this.nmEspecie;
	}
	public void setNmTipo(String nmTipo){
		this.nmTipo=nmTipo;
	}
	public String getNmTipo(){
		return this.nmTipo;
	}
	public void setNmCategoria(String nmCategoria){
		this.nmCategoria=nmCategoria;
	}
	public String getNmCategoria(){
		return this.nmCategoria;
	}
	public void setDtInformacao(GregorianCalendar dtInformacao){
		this.dtInformacao=dtInformacao;
	}
	public GregorianCalendar getDtInformacao(){
		return this.dtInformacao;
	}
	public void setNrChassi(String nrChassi){
		this.nrChassi=nrChassi;
	}
	public String getNrChassi(){
		return this.nrChassi;
	}
	
	public List<Restricao> getRestricoes() {
		return restricoes;
	}

	public void setRestricoes(List<Restricao> restricoes) {
		this.restricoes = restricoes;
	}

	public List<Debito> getDebitos() {
		return debitos;
	}

	public void setDebitos(List<Debito> debitos) {
		this.debitos = debitos;
	}

	public List<Proprietario> getProprietarios() {
		return proprietarios;
	}

	public void setProprietarios(List<Proprietario> proprietarios) {
		this.proprietarios = proprietarios;
	}

	public String toString() {
		JsonToStringBuilder builder2 = new JsonToStringBuilder(this);
		builder2.append("cdVeiculo", getCdVeiculo());
		builder2.append("nrPlaca", getNrPlaca());
		builder2.append("nrRenavan", getNrRenavan());
		builder2.append("tpVeiculo", getTpVeiculo());
		builder2.append("tpCarroceria", getTpCarroceria());
		builder2.append("nrAnoModelo", getNrAnoModelo());
		builder2.append("nrAnoFabricacao", getNrAnoFabricacao());
		builder2.append("nrCodigoMunicipio", getNrCodigoMunicipio());
		builder2.append("nmMunicipio", getNmMunicipio());
		builder2.append("sgEstado", getSgEstado());
		builder2.append("nrCodigoMarca", getNrCodigoMarca());
		builder2.append("nmMarcaModelo", getNmMarcaModelo());
		builder2.append("nrCodigoCor", getNrCodigoCor());
		builder2.append("nmCor", getNmCor());
		builder2.append("nrCodigoEspecie", getNrCodigoEspecie());
		builder2.append("nmEspecie", getNmEspecie());
		builder2.append("nmTipo", getNmTipo());
		builder2.append("nmCategoria", getNmCategoria());
		builder2.append("dtInformacao", sol.util.Util.formatDateTime(getDtInformacao(), "yyyy-MM-dd'T'HH:mm:ss.SSS", ""));
		return builder2.toString();
	}

	public Object clone() {
		return new Veiculo(getCdVeiculo(),
			getNrPlaca(),
			getNrRenavan(),
			getTpVeiculo(),
			getTpCarroceria(),
			getNrAnoModelo(),
			getNrAnoFabricacao(),
			getNrCodigoMunicipio(),
			getNmMunicipio(),
			getSgEstado(),
			getNrCodigoMarca(),
			getNmMarcaModelo(),
			getNrCodigoCor(),
			getNmCor(),
			getNrCodigoEspecie(),
			getNmEspecie(),
			getNmTipo(),
			getNmCategoria(),
			getDtInformacao()==null ? null : (GregorianCalendar)getDtInformacao().clone(),
			getNrChassi());
	}

}