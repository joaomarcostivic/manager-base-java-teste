package com.tivic.manager.mob;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Trrav {

	private int cdTrrav;
	private int nrTrrav;
	private GregorianCalendar dtOcorrencia;
	private int cdUsuario;
	private int cdAgente;
	private int cdCidade;
	private String dsObservacao;
	private String dsLocalOcorrencia;
	private String dsPontoReferencia;
	private Double vlLatitude;
	private Double vlLongitude;
	private int cdVeiculo;
	private String nrPlaca;
	private int tpDocumento;
	private String nrDocumento;
	private String nmCondutor;
	private String nrCnhCondutor;
	private String nmProprietario;
	private int cdLocalRemocao;
	private int cdMotivoRemocao;
	private String txtObjetos;
	private int cdBoat;
	private int cdTipoRemocao;
	private String nmRecebedor;
	private String rgRecebedor;
	private GregorianCalendar dtRecebimento;
	private int cdCidadeCondutor;
	private int cdCidadeProprietario;
	private String ufCondutor;
	private String ufProprietario;
	private String enderecoCondutor;
	private String enderecoProprietario;
	private String bairroCondutor;
	private String bairroProprietario;
	private String vlHodometro;
	private String nrCnhProrietario;
	private int cdCategoriaCnhProprietario;
	private int lgCnhVencidaProprietario;
	private int cdCategoriaCnhCondutor;
	private int lgCnhVencidaCondutor;
	private String nrGuiaExame;
	private String nmDelegaciaPolicia;
	private String dsMotivoRecolhimentoDocumentos;
	private String nmPatioDestino;
	private int cdMeioRemocao;
	private String dsMotivoRecolhimento;
		
	private ArrayList<TrravImagem> imagens;
	
	private ArrayList<RecolhimentoDocumentacao> documentacao;
	private ArrayList<Exame> exame;
	private ArrayList<TrravAit> aitvinculadas;

	public Trrav() { }

	public Trrav(int cdTrrav,
			int nrTrrav,
			GregorianCalendar dtOcorrencia,
			int cdUsuario,
			int cdAgente,
			int cdCidade,
			String dsObservacao,
			String dsLocalOcorrencia,
			String dsPontoReferencia,
			Double vlLatitude,
			Double vlLongitude,
			int cdVeiculo,
			String nrPlaca,
			int tpDocumento,
			String nrDocumento,
			String nmCondutor,
			String nrCnhCondutor,
			String nmProprietario,
			int cdLocalRemocao,
			int cdMotivoRemocao,
			String txtObjetos,
			int cdBoat,
			int cdTipoRemocao,
			String nmRecebedor,
			String rgRecebedor,
			GregorianCalendar dtRecebimento,
			int cdCidadeCondutor,
			int cdCidadeProprietario,
			String ufCondutor,
			String ufProprietario,
			String enderecoCondutor,
			String enderecoProprietario,
			String bairroCondutor,
			String bairroProprietario,
			String vlHodometro,
			String nrCnhProrietario,
			int cdCategoriaCnhProprietario,
			int lgCnhVencidaProprietario,
			int cdCategoriaCnhCondutor,
			int lgCnhVencidaCondutor,
			String nrGuiaExame,
			String nmDelegaciaPolicia,
			String dsMotivoRecolhimentoDocumentos,
			String dsMotivoRecolhimento,
			String nmPatioDestino,
			int cdMeioRemocao,
			ArrayList<RecolhimentoDocumentacao> documentacao,
			ArrayList<Exame> exame,
			ArrayList<TrravAit> aitvinculadas,
			ArrayList<TrravImagem> imagens) {
		setCdTrrav(cdTrrav);
		setNrTrrav(nrTrrav);
		setDtOcorrencia(dtOcorrencia);
		setCdUsuario(cdUsuario);
		setCdAgente(cdAgente);
		setCdCidade(cdCidade);
		setDsObservacao(dsObservacao);
		setDsLocalOcorrencia(dsLocalOcorrencia);
		setDsPontoReferencia(dsPontoReferencia);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
		setCdVeiculo(cdVeiculo);
		setNrPlaca(nrPlaca);
		setTpDocumento(tpDocumento);
		setNrDocumento(nrDocumento);
		setNmCondutor(nmCondutor);
		setNrCnhCondutor(nrCnhCondutor);
		setNmProprietario(nmProprietario);
		setCdLocalRemocao(cdLocalRemocao);
		setCdMotivoRemocao(cdMotivoRemocao);
		setTxtObjetos(txtObjetos);
		setCdBoat(cdBoat);
		setCdTipoRemocao(cdTipoRemocao);
		setNmRecebedor(nmRecebedor);
		setRgRecebedor(rgRecebedor);
		setDtRecebimento(dtRecebimento);
		setCdCidadeCondutor(cdCidadeCondutor);
		setCdCidadeProprietario(cdCidadeProprietario);
		setUfCondutor(ufCondutor);
		setUfProprietario(ufProprietario);
		setEnderecoCondutor(enderecoCondutor);
		setEnderecoProprietario(enderecoProprietario);
		setBairroCondutor(bairroCondutor);
		setBairroProprietario(bairroProprietario);
		setVlHodometro(vlHodometro);
		setNrCnhProrietario(nrCnhProrietario);
		setCdCategoriaCnhProprietario(cdCategoriaCnhProprietario);
		setLgCnhVencidaProprietario(lgCnhVencidaProprietario);
		setCdCategoriaCnhCondutor(cdCategoriaCnhCondutor);
		setLgCnhVencidaCondutor(lgCnhVencidaCondutor);
		setNrGuiaExame(nrGuiaExame);
		setNmDelegaciaPolicia(nmDelegaciaPolicia);
		setDsMotivoRecolhimentoDocumentos(dsMotivoRecolhimentoDocumentos);
		setDsMotivoRecolhimento(dsMotivoRecolhimento);
		setNmPatioDestino(nmPatioDestino);
		setCdMeioRemocao(cdMeioRemocao);
		setDocumentacao(documentacao);
		setExame(exame);
		setAitvinculadas(aitvinculadas);
		
	}
	public ArrayList<RecolhimentoDocumentacao> getDocumentacao() {
		return documentacao;
	}
	public void setDocumentacao(ArrayList<RecolhimentoDocumentacao> documentacao) {
		this.documentacao = documentacao;
	}
	public ArrayList<Exame> getExame() {
		return exame;
	}
	public void setExame(ArrayList<Exame> exame) {
		this.exame = exame;
	}
	public ArrayList<TrravAit> getAitvinculadas() {
		return aitvinculadas;
	}
	public void setAitvinculadas(ArrayList<TrravAit> aitvinculadas) {
		this.aitvinculadas = aitvinculadas;
	}	
	public void setCdTrrav(int cdTrrav){
		this.cdTrrav=cdTrrav;
	}
	public int getCdTrrav(){
		return this.cdTrrav;
	}
	public void setNrTrrav(int nrTrrav){
		this.nrTrrav=nrTrrav;
	}
	public int getNrTrrav(){
		return this.nrTrrav;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setDsObservacao(String dsObservacao){
		this.dsObservacao=dsObservacao;
	}
	public String getDsObservacao(){
		return this.dsObservacao;
	}
	public void setDsLocalOcorrencia(String dsLocalOcorrencia){
		this.dsLocalOcorrencia=dsLocalOcorrencia;
	}
	public String getDsLocalOcorrencia(){
		return this.dsLocalOcorrencia;
	}
	public void setDsPontoReferencia(String dsPontoReferencia){
		this.dsPontoReferencia=dsPontoReferencia;
	}
	public String getDsPontoReferencia(){
		return this.dsPontoReferencia;
	}
	public void setVlLatitude(Double vlLatitude){
		this.vlLatitude=vlLatitude;
	}
	public Double getVlLatitude(){
		return this.vlLatitude;
	}
	public void setVlLongitude(Double vlLongitude){
		this.vlLongitude=vlLongitude;
	}
	public Double getVlLongitude(){
		return this.vlLongitude;
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
	public void setNmProprietario(String nmProprietario){
		this.nmProprietario=nmProprietario;
	}
	public String getNmProprietario(){
		return this.nmProprietario;
	}
	public void setCdLocalRemocao(int cdLocalRemocao){
		this.cdLocalRemocao=cdLocalRemocao;
	}
	public int getCdLocalRemocao(){
		return this.cdLocalRemocao;
	}
	public void setCdMotivoRemocao(int cdMotivoRemocao){
		this.cdMotivoRemocao=cdMotivoRemocao;
	}
	public int getCdMotivoRemocao(){
		return this.cdMotivoRemocao;
	}
	public void setTxtObjetos(String txtObjetos){
		this.txtObjetos=txtObjetos;
	}
	public String getTxtObjetos(){
		return this.txtObjetos;
	}
	public void setCdBoat(int cdBoat){
		this.cdBoat=cdBoat;
	}
	public int getCdBoat(){
		return this.cdBoat;
	}
	public void setCdTipoRemocao(int cdTipoRemocao){
		this.cdTipoRemocao=cdTipoRemocao;
	}
	public int getCdTipoRemocao(){
		return this.cdTipoRemocao;
	}
	public void setNmRecebedor(String nmRecebedor){
		this.nmRecebedor=nmRecebedor;
	}
	public String getNmRecebedor(){
		return this.nmRecebedor;
	}
	public void setRgRecebedor(String rgRecebedor){
		this.rgRecebedor=rgRecebedor;
	}
	public String getRgRecebedor(){
		return this.rgRecebedor;
	}
	public void setDtRecebimento(GregorianCalendar dtRecebimento){
		this.dtRecebimento=dtRecebimento;
	}
	public GregorianCalendar getDtRecebimento(){
		return this.dtRecebimento;
	}
	public void setCdCidadeCondutor(int cdCidadeCondutor){
		this.cdCidadeCondutor=cdCidadeCondutor;
	}
	public int getCdCidadeCondutor(){
		return this.cdCidadeCondutor;
	}
	public void setCdCidadeProprietario(int cdCidadeProprietario){
		this.cdCidadeProprietario=cdCidadeProprietario;
	}
	public int getCdCidadeProprietario(){
		return this.cdCidadeProprietario;
	}
	public void setUfCondutor(String ufCondutor){
		this.ufCondutor=ufCondutor;
	}
	public String getUfCondutor(){
		return this.ufCondutor;
	}
	public void setUfProprietario(String ufProprietario){
		this.ufProprietario=ufProprietario;
	}
	public String getUfProprietario(){
		return this.ufProprietario;
	}
	public void setEnderecoCondutor(String enderecoCondutor){
		this.enderecoCondutor=enderecoCondutor;
	}
	public String getEnderecoCondutor(){
		return this.enderecoCondutor;
	}
	public void setEnderecoProprietario(String enderecoProprietario){
		this.enderecoProprietario=enderecoProprietario;
	}
	public String getEnderecoProprietario(){
		return this.enderecoProprietario;
	}
	public void setBairroCondutor(String bairroCondutor){
		this.bairroCondutor=bairroCondutor;
	}
	public String getBairroCondutor(){
		return this.bairroCondutor;
	}
	public void setBairroProprietario(String bairroProprietario){
		this.bairroProprietario=bairroProprietario;
	}
	public String getBairroProprietario(){
		return this.bairroProprietario;
	}
	public void setVlHodometro(String vlHodometro){
		this.vlHodometro=vlHodometro;
	}
	public String getVlHodometro(){
		return this.vlHodometro;
	}
	public void setNrCnhProrietario(String nrCnhProrietario){
		this.nrCnhProrietario=nrCnhProrietario;
	}
	public String getNrCnhProrietario(){
		return this.nrCnhProrietario;
	}
	public void setCdCategoriaCnhProprietario(int cdCategoriaCnhProprietario){
		this.cdCategoriaCnhProprietario=cdCategoriaCnhProprietario;
	}
	public int getCdCategoriaCnhProprietario(){
		return this.cdCategoriaCnhProprietario;
	}
	public void setLgCnhVencidaProprietario(int lgCnhVencidaProprietario){
		this.lgCnhVencidaProprietario=lgCnhVencidaProprietario;
	}
	public int getLgCnhVencidaProprietario(){
		return this.lgCnhVencidaProprietario;
	}
	public void setCdCategoriaCnhCondutor(int cdCategoriaCnhCondutor){
		this.cdCategoriaCnhCondutor=cdCategoriaCnhCondutor;
	}
	public int getCdCategoriaCnhCondutor(){
		return this.cdCategoriaCnhCondutor;
	}
	public void setLgCnhVencidaCondutor(int lgCnhVencidaCondutor){
		this.lgCnhVencidaCondutor=lgCnhVencidaCondutor;
	}
	public int getLgCnhVencidaCondutor(){
		return this.lgCnhVencidaCondutor;
	}
	public void setNrGuiaExame(String nrGuiaExame){
		this.nrGuiaExame=nrGuiaExame;
	}
	public String getNrGuiaExame(){
		return this.nrGuiaExame;
	}
	public void setNmDelegaciaPolicia(String nmDelegaciaPolicia){
		this.nmDelegaciaPolicia=nmDelegaciaPolicia;
	}
	public String getNmDelegaciaPolicia(){
		return this.nmDelegaciaPolicia;
	}
	public void setDsMotivoRecolhimentoDocumentos(String dsMotivoRecolhimentoDocumentos){
		this.dsMotivoRecolhimentoDocumentos=dsMotivoRecolhimentoDocumentos;
	}
	public String getDsMotivoRecolhimentoDocumentos(){
		return this.dsMotivoRecolhimentoDocumentos;
	}
	
	public void setDsMotivoRecolhimento(String dsMotivoRecolhimento){
		this.dsMotivoRecolhimento=dsMotivoRecolhimento;
	}
	public String getDsMotivoRecolhimento(){
		return this.dsMotivoRecolhimento;
	}
	public void setNmPatioDestino(String nmPatioDestino){
		this.nmPatioDestino=nmPatioDestino;
	}
	public String getNmPatioDestino(){
		return this.nmPatioDestino;
	}
	public void setCdMeioRemocao(int cdMeioRemocao){
		this.cdMeioRemocao=cdMeioRemocao;
	}
	public int getCdMeioRemocao(){
		return this.cdMeioRemocao;
	}
	
	public ArrayList<TrravImagem> getImagens() {
		return imagens;
	}
	public void setImagens(ArrayList<TrravImagem> imagens) {
		this.imagens = imagens;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdTrrav: " +  getCdTrrav();
		valueToString += ", nrTrrav: " +  getNrTrrav();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", dsObservacao: " +  getDsObservacao();
		valueToString += ", dsLocalOcorrencia: " +  getDsLocalOcorrencia();
		valueToString += ", dsPontoReferencia: " +  getDsPontoReferencia();
		valueToString += ", vlLatitude: " +  getVlLatitude();
		valueToString += ", vlLongitude: " +  getVlLongitude();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", nrPlaca: " +  getNrPlaca();
		valueToString += ", tpDocumento: " +  getTpDocumento();
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", nmCondutor: " +  getNmCondutor();
		valueToString += ", nrCnhCondutor: " +  getNrCnhCondutor();
		valueToString += ", nmProprietario: " +  getNmProprietario();
		valueToString += ", cdLocalRemocao: " +  getCdLocalRemocao();
		valueToString += ", cdMotivoRemocao: " +  getCdMotivoRemocao();
		valueToString += ", txtObjetos: " +  getTxtObjetos();
		valueToString += ", cdBoat: " +  getCdBoat();
		valueToString += ", cdTipoRemocao: " +  getCdTipoRemocao();
		valueToString += ", nmRecebedor: " +  getNmRecebedor();
		valueToString += ", rgRecebedor: " +  getRgRecebedor();
		valueToString += ", dtRecebimento: " +  sol.util.Util.formatDateTime(getDtRecebimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdCidadeCondutor: " +  getCdCidadeCondutor();
		valueToString += ", cdCidadeProprietario: " +  getCdCidadeProprietario();
		valueToString += ", ufCondutor: " +  getUfCondutor();
		valueToString += ", ufProprietario: " +  getUfProprietario();
		valueToString += ", enderecoCondutor: " +  getEnderecoCondutor();
		valueToString += ", enderecoProprietario: " +  getEnderecoProprietario();
		valueToString += ", bairroCondutor: " +  getBairroCondutor();
		valueToString += ", bairroProprietario: " +  getBairroProprietario();
		valueToString += ", vlHodometro: " +  getVlHodometro();
		valueToString += ", nrCnhProrietario: " +  getNrCnhProrietario();
		valueToString += ", cdCategoriaCnhProprietario: " +  getCdCategoriaCnhProprietario();
		valueToString += ", lgCnhVencidaProprietario: " +  getLgCnhVencidaProprietario();
		valueToString += ", cdCategoriaCnhCondutor: " +  getCdCategoriaCnhCondutor();
		valueToString += ", lgCnhVencidaCondutor: " +  getLgCnhVencidaCondutor();
		valueToString += ", nrGuiaExame: " +  getNrGuiaExame();
		valueToString += ", nmDelegaciaPolicia: " +  getNmDelegaciaPolicia();
		valueToString += ", dsMotivoRecolhimentoDocumentos: " +  getDsMotivoRecolhimentoDocumentos();
		valueToString += ", dsMotivoRecolhimento: " +  getDsMotivoRecolhimento();
		valueToString += ", nmPatioDestino: " +  getNmPatioDestino();
		valueToString += ", cdMeioRemocao: " +  getCdMeioRemocao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Trrav(getCdTrrav(),
			getNrTrrav(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone(),
			getCdUsuario(),
			getCdAgente(),
			getCdCidade(),
			getDsObservacao(),
			getDsLocalOcorrencia(),
			getDsPontoReferencia(),
			getVlLatitude(),
			getVlLongitude(),
			getCdVeiculo(),
			getNrPlaca(),
			getTpDocumento(),
			getNrDocumento(),
			getNmCondutor(),
			getNrCnhCondutor(),
			getNmProprietario(),
			getCdLocalRemocao(),
			getCdMotivoRemocao(),
			getTxtObjetos(),
			getCdBoat(),
			getCdTipoRemocao(),
			getNmRecebedor(),
			getRgRecebedor(),
			getDtRecebimento()==null ? null : (GregorianCalendar)getDtRecebimento().clone(),
			getCdCidadeCondutor(),
			getCdCidadeProprietario(),
			getUfCondutor(),
			getUfProprietario(),
			getEnderecoCondutor(),
			getEnderecoProprietario(),
			getBairroCondutor(),
			getBairroProprietario(),
			getVlHodometro(),
			getNrCnhProrietario(),
			getCdCategoriaCnhProprietario(),
			getLgCnhVencidaProprietario(),
			getCdCategoriaCnhCondutor(),
			getLgCnhVencidaCondutor(),
			getNrGuiaExame(),
			getNmDelegaciaPolicia(),
			getDsMotivoRecolhimentoDocumentos(),
			getDsMotivoRecolhimento(),
			getNmPatioDestino(),
			getCdMeioRemocao(),
			getDocumentacao(),
			getExame(),
			getAitvinculadas(),
			getImagens());
	}

}