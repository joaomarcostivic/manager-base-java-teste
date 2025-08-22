package com.tivic.manager.str;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tivic.manager.str.ait.NumeroAitDeserializer;

public class Ait {

	private int cdAit;
	private int cdInfracao;
	private int cdAgente;
	private int cdUsuario;
	private int cdEspecie;
	private int cdMarca;
	private int cdCor;
	private int cdTipo;
	private int cdOcorrencia;
	private GregorianCalendar dtInfracao;
	private int cdCategoria;
	private String dsObservacao;
	private String dsLocalInfracao;
	private String nrRenavan;
	private String dsAnoFabricacao;
	private String dsAnoModelo;
	private String nmProprietario;
	private int tpDocumento;
	private String nrDocumento;
	
	@JsonDeserialize(using = NumeroAitDeserializer.class)
	private int nrAit;
	
	private String nmCondutor;
	private String nrCnhCondutor;
	private String ufCnhCondutor;

	private String nrCpfCnpjProprietario;
	private String nrCpfCondutor;
	private float vlVelocidadePermitida;
	private float vlVelocidadeAferida;
	private float vlVelocidadePenalidade;
	private String nrPlaca;
	private String dsPontoReferencia;
	private int lgAutoAssinado;
	private float vlLongitude;
	private float vlLatitude;
	private int cdCidade;
	private int cdEquipamento;
	private int tpCnhCondutor;
	private int tpConvenio;
	
	private int stAit;
	
	private String idAit;
	private int cdTalao;

	private int cdLogradouroInfracao;
	
	private ArrayList<AitImagem> imagens;
	
	private GregorianCalendar dtPrazoDefesa;
	
	public Ait(){ }

	public Ait(int cdAit,
			int cdInfracao,
			int cdAgente,
			int cdUsuario,
			int cdEspecie,
			int cdMarca,
			int cdCor,
			int cdTipo,
			GregorianCalendar dtInfracao,
			int cdCategoria,
			String dsObservacao,
			String dsLocalInfracao,
			String nrRenavan,
			String dsAnoFabricacao,
			String dsAnoModelo,
			String nmProprietario,
			int tpDocumento,
			String nrDocumento,
			int nrAit,
			String nmCondutor,
			String nrCnhCondutor,
			String ufCnhCondutor,
			float vlVelocidadePermitida,
			float vlVelocidadeAferida,
			float vlVelocidadePenalidade,
			String nrPlaca,
			String dsPontoReferencia,
			int lgAutoAssinado,
			float vlLongitude,
			float vlLatitude,
			int cdCidade,
			int cdEquipamento,
			int tpCnhCondutor,
			int tpConvenio,
			int stAit,
			GregorianCalendar dtPrazoDefesa,
			int cdLogradouroInfracao,
			String nrCpfCnpjProprietario,
			String nrCpfCondutor,
			ArrayList<AitImagem> imagens){
		setCdAit(cdAit);
		setCdInfracao(cdInfracao);
		setCdAgente(cdAgente);
		setCdUsuario(cdUsuario);
		setCdEspecie(cdEspecie);
		setCdMarca(cdMarca);
		setCdCor(cdCor);
		setCdTipo(cdTipo);
		setDtInfracao(dtInfracao);
		setCdCategoria(cdCategoria);
		setDsObservacao(dsObservacao);
		setDsLocalInfracao(dsLocalInfracao);
		setNrRenavan(nrRenavan);
		setDsAnoFabricacao(dsAnoFabricacao);
		setDsAnoModelo(dsAnoModelo);
		setNmProprietario(nmProprietario);
		setTpDocumento(tpDocumento);
		setNrDocumento(nrDocumento);
		setNrAit(nrAit);
		setNmCondutor(nmCondutor);
		setNrCnhCondutor(nrCnhCondutor);
		setUfCnhCondutor(ufCnhCondutor);
		setVlVelocidadePermitida(vlVelocidadePermitida);
		setVlVelocidadeAferida(vlVelocidadeAferida);
		setVlVelocidadePenalidade(vlVelocidadePenalidade);
		setNrPlaca(nrPlaca);
		setDsPontoReferencia(dsPontoReferencia);
		setLgAutoAssinado(lgAutoAssinado);
		setVlLongitude(vlLongitude);
		setVlLatitude(vlLatitude);
		setCdCidade(cdCidade);
		setCdEquipamento(cdEquipamento);
		setTpCnhCondutor(tpCnhCondutor);
		setTpConvenio(tpConvenio);
		setImagens(imagens);
		setDtPrazoDefesa(dtPrazoDefesa);
		setCdLogradouroInfracao(cdLogradouroInfracao);
		setNrCpfCnpjProprietario(nrCpfCnpjProprietario);
		setNrCpfCondutor(nrCpfCondutor);
	}
	
	public Ait(int cdAit,
			int cdInfracao,
			int cdAgente,
			int cdUsuario,
			int cdEspecie,
			int cdMarca,
			int cdCor,
			int cdTipo,
			GregorianCalendar dtInfracao,
			int cdCategoria,
			String dsObservacao,
			String dsLocalInfracao,
			String nrRenavan,
			String dsAnoFabricacao,
			String dsAnoModelo,
			String nmProprietario,
			int tpDocumento,
			String nrDocumento,
			int nrAit,
			String nmCondutor,
			String nrCnhCondutor,
			String ufCnhCondutor,
			float vlVelocidadePermitida,
			float vlVelocidadeAferida,
			float vlVelocidadePenalidade,
			String nrPlaca,
			String dsPontoReferencia,
			int lgAutoAssinado,
			float vlLongitude,
			float vlLatitude,
			int cdCidade,
			int cdEquipamento,
			int tpCnhCondutor,
			int tpConvenio,
			String idAit,
			int cdTalao,
			String nrCpfCnpjProprietario,
			String nrCpfCondutor,
			ArrayList<AitImagem> imagens){
		setCdAit(cdAit);
		setCdInfracao(cdInfracao);
		setCdAgente(cdAgente);
		setCdUsuario(cdUsuario);
		setCdEspecie(cdEspecie);
		setCdMarca(cdMarca);
		setCdCor(cdCor);
		setCdTipo(cdTipo);
		setDtInfracao(dtInfracao);
		setCdCategoria(cdCategoria);
		setDsObservacao(dsObservacao);
		setDsLocalInfracao(dsLocalInfracao);
		setNrRenavan(nrRenavan);
		setDsAnoFabricacao(dsAnoFabricacao);
		setDsAnoModelo(dsAnoModelo);
		setNmProprietario(nmProprietario);
		setTpDocumento(tpDocumento);
		setNrDocumento(nrDocumento);
		setNrAit(nrAit);
		setNmCondutor(nmCondutor);
		setNrCnhCondutor(nrCnhCondutor);
		setUfCnhCondutor(ufCnhCondutor);
		setVlVelocidadePermitida(vlVelocidadePermitida);
		setVlVelocidadeAferida(vlVelocidadeAferida);
		setVlVelocidadePenalidade(vlVelocidadePenalidade);
		setNrPlaca(nrPlaca);
		setDsPontoReferencia(dsPontoReferencia);
		setLgAutoAssinado(lgAutoAssinado);
		setVlLongitude(vlLongitude);
		setVlLatitude(vlLatitude);
		setCdCidade(cdCidade);
		setCdEquipamento(cdEquipamento);
		setTpCnhCondutor(tpCnhCondutor);
		setTpConvenio(tpConvenio);
		setNrCpfCnpjProprietario(nrCpfCnpjProprietario);
		setNrCpfCondutor(nrCpfCondutor);
		
		setIdAit(idAit);
		setCdTalao(cdTalao);
		
		setImagens(imagens);
	}
	
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setCdInfracao(int cdInfracao){
		this.cdInfracao=cdInfracao;
	}
	public int getCdInfracao(){
		return this.cdInfracao;
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
	public void setCdEspecie(int cdEspecie){
		this.cdEspecie=cdEspecie;
	}
	public int getCdEspecie(){
		return this.cdEspecie;
	}
	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public void setCdCor(int cdCor){
		this.cdCor=cdCor;
	}
	public int getCdCor(){
		return this.cdCor;
	}
	public void setCdTipo(int cdTipo){
		this.cdTipo=cdTipo;
	}
	public int getCdTipo(){
		return this.cdTipo;
	}
	public void setDtInfracao(GregorianCalendar dtInfracao){
		this.dtInfracao=dtInfracao;
	}
	public GregorianCalendar getDtInfracao(){
		return this.dtInfracao;
	}
	public void setCdCategoria(int cdCategoria){
		this.cdCategoria=cdCategoria;
	}
	public int getCdCategoria(){
		return this.cdCategoria;
	}
	public void setDsObservacao(String dsObservacao){
		this.dsObservacao=dsObservacao;
	}
	public String getDsObservacao(){
		return this.dsObservacao;
	}
	public void setDsLocalInfracao(String dsLocalInfracao){
		this.dsLocalInfracao=dsLocalInfracao;
	}
	public String getDsLocalInfracao(){
		return this.dsLocalInfracao;
	}
	public void setNrRenavan(String nrRenavan){
		this.nrRenavan=nrRenavan;
	}
	public String getNrRenavan(){
		return this.nrRenavan;
	}
	public void setDsAnoFabricacao(String dsAnoFabricacao){
		this.dsAnoFabricacao=dsAnoFabricacao;
	}
	public String getDsAnoFabricacao(){
		return this.dsAnoFabricacao;
	}
	public void setDsAnoModelo(String dsAnoModelo){
		this.dsAnoModelo=dsAnoModelo;
	}
	public String getDsAnoModelo(){
		return this.dsAnoModelo;
	}
	public void setNmProprietario(String nmProprietario){
		this.nmProprietario=nmProprietario;
	}
	public String getNmProprietario(){
		return this.nmProprietario;
	}
	public void setTpDocumento(int tpDocumento){
		this.tpDocumento=tpDocumento;
	}
	public int getTpDocumento(){
		return this.tpDocumento;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setNrAit(int nrAit){
		this.nrAit=nrAit;
	}
	public int getNrAit(){
		return this.nrAit;
	}
	public void setNmCondutor(String nmCondutor){
		this.nmCondutor=nmCondutor;
	}
	public String getNmCondutor(){
		return this.nmCondutor;
	}
	public void setNrCnhCondutor(String nrCnhCondutor){
		this.nrCnhCondutor=nrCnhCondutor;
	}
	public String getNrCnhCondutor(){
		return this.nrCnhCondutor;
	}
	public void setUfCnhCondutor(String ufCnhCondutor){
		this.ufCnhCondutor=ufCnhCondutor;
	}
	public String getUfCnhCondutor(){
		return this.ufCnhCondutor;
	}
	public void setVlVelocidadePermitida(float vlVelocidadePermitida){
		this.vlVelocidadePermitida=vlVelocidadePermitida;
	}
	public float getVlVelocidadePermitida(){
		return this.vlVelocidadePermitida;
	}
	public void setVlVelocidadeAferida(float vlVelocidadeAferida){
		this.vlVelocidadeAferida=vlVelocidadeAferida;
	}
	public float getVlVelocidadeAferida(){
		return this.vlVelocidadeAferida;
	}
	public void setVlVelocidadePenalidade(float vlVelocidadePenalidade){
		this.vlVelocidadePenalidade=vlVelocidadePenalidade;
	}
	public float getVlVelocidadePenalidade(){
		return this.vlVelocidadePenalidade;
	}
	public void setNrPlaca(String nrPlaca){
		this.nrPlaca=nrPlaca;
	}
	public String getNrPlaca(){
		return this.nrPlaca;
	}
	public void setDsPontoReferencia(String dsPontoReferencia){
		this.dsPontoReferencia=dsPontoReferencia;
	}
	public String getDsPontoReferencia(){
		return this.dsPontoReferencia;
	}
	public void setLgAutoAssinado(int lgAutoAssinado){
		this.lgAutoAssinado=lgAutoAssinado;
	}
	public int getLgAutoAssinado(){
		return this.lgAutoAssinado;
	}
	public void setVlLongitude(float vlLongitude){
		this.vlLongitude=vlLongitude;
	}
	public float getVlLongitude(){
		return this.vlLongitude;
	}
	public void setVlLatitude(float vlLatitude){
		this.vlLatitude=vlLatitude;
	}
	public float getVlLatitude(){
		return this.vlLatitude;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public int getCdEquipamento() {
		return cdEquipamento;
	}
	public void setCdEquipamento(int cdEquipamento) {
		this.cdEquipamento = cdEquipamento;
	}
	public int getTpCnhCondutor() {
		return tpCnhCondutor;
	}
	public void setTpCnhCondutor(int tpCnhCondutor) {
		this.tpCnhCondutor = tpCnhCondutor;
	}	
	public int getTpConvenio() {
		return tpConvenio;
	}
	public void setTpConvenio(int tpConvenio) {
		this.tpConvenio = tpConvenio;
	}
	public int getStAit() {
		return stAit;
	}
	public void setStAit(int stAit) {
		this.stAit = stAit;
	}
	public String getIdAit() {
		return idAit;
	}
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	public int getCdTalao() {
		return cdTalao;
	}
	public void setCdTalao(int cdTalao) {
		this.cdTalao = cdTalao;
	}

	public ArrayList<AitImagem> getImagens() {
		return imagens;
	}
	public void setImagens(ArrayList<AitImagem> imagens) {
		this.imagens = imagens;
	}
	public void setDtPrazoDefesa(GregorianCalendar dtPrazoDefesa){
		this.dtPrazoDefesa=dtPrazoDefesa;
	}
	public GregorianCalendar getDtPrazoDefesa(){
		return this.dtPrazoDefesa;
	}
	public void setCdLogradouroInfracao(int cdLogradouroInfracao) {
		this.cdLogradouroInfracao = cdLogradouroInfracao;
	}
	public int getCdLogradouroInfracao() {
		return cdLogradouroInfracao;
	}

	public String getNrCpfCnpjProprietario() {
		return nrCpfCnpjProprietario;
	}

	public void setNrCpfCnpjProprietario(String nrCpfCnpjProprietario) {
		this.nrCpfCnpjProprietario = nrCpfCnpjProprietario;
	}

	public String getNrCpfCondutor() {
		return nrCpfCondutor;
	}

	public void setNrCpfCondutor(String nrCpfCondutor) {
		this.nrCpfCondutor = nrCpfCondutor;
	}

	public int getCdOcorrencia() {
		return cdOcorrencia;
	}

	public void setCdOcorrencia(int cdOcorrencia) {
		this.cdOcorrencia = cdOcorrencia;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdAit: " +  getCdAit();
		valueToString += ", cdInfracao: " +  getCdInfracao();
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdEspecie: " +  getCdEspecie();
		valueToString += ", cdMarca: " +  getCdMarca();
		valueToString += ", cdCor: " +  getCdCor();
		valueToString += ", cdTipo: " +  getCdTipo();
		valueToString += ", cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", dtInfracao: " +  sol.util.Util.formatDateTime(getDtInfracao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdCategoria: " +  getCdCategoria();
		valueToString += ", dsObservacao: " +  getDsObservacao();
		valueToString += ", dsLocalInfracao: " +  getDsLocalInfracao();
		valueToString += ", nrRenavan: " +  getNrRenavan();
		valueToString += ", dsAnoFabricacao: " +  getDsAnoFabricacao();
		valueToString += ", dsAnoModelo: " +  getDsAnoModelo();
		valueToString += ", nmProprietario: " +  getNmProprietario();
		valueToString += ", tpDocumento: " +  getTpDocumento();
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", nrAit: " +  getNrAit();
		valueToString += ", nmCondutor: " +  getNmCondutor();
		valueToString += ", nrCnhCondutor: " +  getNrCnhCondutor();
		valueToString += ", ufCnhCondutor: " +  getUfCnhCondutor();
		valueToString += ", vlVelocidadePermitida: " +  getVlVelocidadePermitida();
		valueToString += ", vlVelocidadeAferida: " +  getVlVelocidadeAferida();
		valueToString += ", vlVelocidadePenalidade: " +  getVlVelocidadePenalidade();
		valueToString += ", nrPlaca: " +  getNrPlaca();
		valueToString += ", dsPontoReferencia: " +  getDsPontoReferencia();
		valueToString += ", lgAutoAssinado: " +  getLgAutoAssinado();
		valueToString += ", vlLongitude: " +  getVlLongitude();
		valueToString += ", vlLatitude: " +  getVlLatitude();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", cdEquipamento: " +  getCdEquipamento();
		valueToString += ", tpCnhCondutor: " +  getTpCnhCondutor();
		valueToString += ", tpConvenio: " +  getTpConvenio();
		valueToString += ", stAit: " +  getStAit();
		valueToString += ", cdLogradouroInfracao: " +  getCdLogradouroInfracao();
		valueToString += ", nrCpfCnpjProprietario: " +  getNrCpfCnpjProprietario();
		valueToString += ", nrCpfCondutor: " +  getNrCpfCondutor();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Ait(getCdAit(),
			getCdInfracao(),
			getCdAgente(),
			getCdUsuario(),
			getCdEspecie(),
			getCdMarca(),
			getCdCor(),
			getCdTipo(),
			getDtInfracao()==null ? null : (GregorianCalendar)getDtInfracao().clone(),
			getCdCategoria(),
			getDsObservacao(),
			getDsLocalInfracao(),
			getNrRenavan(),
			getDsAnoFabricacao(),
			getDsAnoModelo(),
			getNmProprietario(),
			getTpDocumento(),
			getNrDocumento(),
			getNrAit(),
			getNmCondutor(),
			getNrCnhCondutor(),
			getUfCnhCondutor(),
			getVlVelocidadePermitida(),
			getVlVelocidadeAferida(),
			getVlVelocidadePenalidade(),
			getNrPlaca(),
			getDsPontoReferencia(),
			getLgAutoAssinado(),
			getVlLongitude(),
			getVlLatitude(),
			getCdCidade(),
			getCdEquipamento(),
			getTpCnhCondutor(),
			getTpConvenio(),
			getStAit(),
			getDtPrazoDefesa(),
			getCdLogradouroInfracao(),
			getNrCpfCnpjProprietario(),
			getNrCpfCondutor(),
			getImagens());
	}

}