package com.tivic.manager.mob.boat;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class Boat implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int cdBoat;
	private int nrBoat;
	private GregorianCalendar dtOcorrencia;
	private int tpAcidente;
	private String nmTipoAcidenteOutro;
	private int cdUsuario;
	private int cdAgente;
	private int cdCidade;
	private String dsObservacao;
	private String dsLocalOcorrencia;
	private String dsPontoReferencia;
	private Double vlLatitude;
	private Double vlLongitude; 
	private int cdTipoAcidente;
	private int cdCondicaoVia;
	private int cdTipoPavimento;
	private int cdCondicaoClima;
	private int stSinalizacaoHorizontal;
	private int stSinalizacaoVertical;
	private int stSemaforo;
	private String nmDelegaciaComunicada;
	private GregorianCalendar dtComunicacao;
	private String nrOcorrenciaPolicial;
	private String nmAgentePolicial;
	private String nrMatriculaAgentePolicial;
	private String txtOcorrenciaPolicial;
	private int lgPoliciaTecnica;
	private String nmResponsavelPoliciaTecnica;
	private int lgIml;
	private String nmResponsavelIml;
	private int lgPericiaRealizada;
	private String nmResponsavelPericia;
	private String lgBombeiros;
	private String nmResponsavelBombeiros;
	private int lgResgate;
	private String nmResponsavelResgate;
	private String nmOutroOrgao;
	private String nmResponsavelOutroOrgao;
	private byte[] blbDiagrama;
	private byte[] imgDiagrama;
	private String txtDescricaoSumaria;
	private int tpCaracteristicaAcidente;
	private int tpCondicaoVia;
	private int tpPavimento;
	private int tpCondicaoClima;
	private int tpTracadoVia;
	private String nmOutraCondicaoVia;
	private String nmOutroPavimento;
	private String nmOutraCondicaoClima;

	private String txtDocumentosEntregues;

	private String nrProtocolo;
	private int stBoat;
	private int lgDanoPatrimonioPublico;
	private int lgDanoMeioAmbiente;
	private String txtJustificativa;
	
	public Boat() { }
	
	public Boat(int cdBoat,
			int nrBoat,
			GregorianCalendar dtOcorrencia,
			int tpAcidente,
			String nmTipoAcidenteOutro,
			int cdUsuario,
			int cdAgente,
			int cdCidade,
			String dsObservacao,
			String dsLocalOcorrencia,
			String dsPontoReferencia,
			Double vlLatitude,
			Double vlLongitude,
			int cdTipoAcidente,
			int cdCondicaoVia,
			int cdTipoPavimento,
			int cdCondicaoClima,
			int stSinalizacaoHorizontal,
			int stSinalizacaoVertical,
			int stSemaforo,
			String nmDelegaciaComunicada,
			GregorianCalendar dtComunicacao,
			String nrOcorrenciaPolicial,
			String nmAgentePolicial,
			String nrMatriculaAgentePolicial,
			String txtOcorrenciaPolicial,
			int lgPoliciaTecnica,
			String nmResponsavelPoliciaTecnica,
			int lgIml,
			String nmResponsavelIml,
			int lgPericiaRealizada,
			String nmResponsavelPericia,
			String lgBombeiros,
			String nmResponsavelBombeiros,
			int lgResgate,
			String nmResponsavelResgate,
			String nmOutroOrgao,
			String nmResponsavelOutroOrgao,
			byte[] blbDiagrama,
			byte[] imgDiagrama,
			String txtDescricaoSumaria,
			int tpCaracteristicaAcidente,
			int tpCondicaoVia,
			int tpPavimento,
			int tpCondicaoClima,
			int tpTracadoVia,
			String nmOutraCondicaoVia,
			String nmOutroPavimento,
			String nmOutraCondicaoClima,
			String txtDocumentosEntregues,
			String nrProtocolo,
			int stBoat,
			int lgDanoPatrimonioPublico,
			int lgDanoMeioAmbiente,
			String txtJustificativa) {
		setCdBoat(cdBoat);
		setNrBoat(nrBoat);
		setDtOcorrencia(dtOcorrencia);
		setTpAcidente(tpAcidente);
		setNmTipoAcidenteOutro(nmTipoAcidenteOutro);
		setCdUsuario(cdUsuario);
		setCdAgente(cdAgente);
		setCdCidade(cdCidade);
		setDsObservacao(dsObservacao);
		setDsLocalOcorrencia(dsLocalOcorrencia);
		setDsPontoReferencia(dsPontoReferencia);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
		setCdTipoAcidente(cdTipoAcidente);
		setCdCondicaoVia(cdCondicaoVia);
		setCdTipoPavimento(cdTipoPavimento);
		setCdCondicaoClima(cdCondicaoClima);
		setStSinalizacaoHorizontal(stSinalizacaoHorizontal);
		setStSinalizacaoVertical(stSinalizacaoVertical);
		setStSemaforo(stSemaforo);
		setNmDelegaciaComunicada(nmDelegaciaComunicada);
		setDtComunicacao(dtComunicacao);
		setNrOcorrenciaPolicial(nrOcorrenciaPolicial);
		setNmAgentePolicial(nmAgentePolicial);
		setNrMatriculaAgentePolicial(nrMatriculaAgentePolicial);
		setTxtOcorrenciaPolicial(txtOcorrenciaPolicial);
		setLgPoliciaTecnica(lgPoliciaTecnica);
		setNmResponsavelPoliciaTecnica(nmResponsavelPoliciaTecnica);
		setLgIml(lgIml);
		setNmResponsavelIml(nmResponsavelIml);
		setLgPericiaRealizada(lgPericiaRealizada);
		setNmResponsavelPericia(nmResponsavelPericia);
		setLgBombeiros(lgBombeiros);
		setNmResponsavelBombeiros(nmResponsavelBombeiros);
		setLgResgate(lgResgate);
		setNmResponsavelResgate(nmResponsavelResgate);
		setNmOutroOrgao(nmOutroOrgao);
		setNmResponsavelOutroOrgao(nmResponsavelOutroOrgao);
		setBlbDiagrama(blbDiagrama);
		setImgDiagrama(imgDiagrama);
		setTxtDescricaoSumaria(txtDescricaoSumaria);
		setTpCaracteristicaAcidente(tpCaracteristicaAcidente);
		setTpCondicaoVia(tpCondicaoVia);
		setTpPavimento(tpPavimento);
		setTpCondicaoClima(tpCondicaoClima);
		setNmOutraCondicaoVia(nmOutraCondicaoVia);
		setNmOutroPavimento(nmOutroPavimento);
		setNmOutraCondicaoClima(nmOutraCondicaoClima);
		setTxtDocumentosEntregues(txtDocumentosEntregues);
		setNrProtocolo(nrProtocolo);
		setStBoat(stBoat);
		setLgDanoPatrimonioPublico(lgDanoPatrimonioPublico);
		setLgDanoMeioAmbiente(lgDanoMeioAmbiente);
		setTxtJustificativa(txtJustificativa);
	}

	public Boat(int cdBoat,
			int nrBoat,
			GregorianCalendar dtOcorrencia,
			int tpAcidente,
			String nmTipoAcidenteOutro,
			int cdUsuario,
			int cdAgente,
			int cdCidade,
			String dsObservacao,
			String dsLocalOcorrencia,
			String dsPontoReferencia,
			Double vlLatitude,
			Double vlLongitude,
			int cdTipoAcidente,
			int cdCondicaoVia,
			int cdTipoPavimento,
			int cdCondicaoClima,
			int stSinalizacaoHorizontal,
			int stSinalizacaoVertical,
			int stSemaforo,
			String nmDelegaciaComunicada,
			GregorianCalendar dtComunicacao,
			String nrOcorrenciaPolicial,
			String nmAgentePolicial,
			String nrMatriculaAgentePolicial,
			String txtOcorrenciaPolicial,
			int lgPoliciaTecnica,
			String nmResponsavelPoliciaTecnica,
			int lgIml,
			String nmResponsavelIml,
			int lgPericiaRealizada,
			String nmResponsavelPericia,
			String lgBombeiros,
			String nmResponsavelBombeiros,
			int lgResgate,
			String nmResponsavelResgate,
			String nmOutroOrgao,
			String nmResponsavelOutroOrgao,
			byte[] blbDiagrama,
			byte[] imgDiagrama,
			String txtDescricaoSumaria,
			int tpCaracteristicaAcidente,
			int tpCondicaoVia,
			int tpPavimento,
			int tpCondicaoClima,
			String nmOutraCondicaoVia,
			String nmOutroPavimento,
			String nmOutraCondicaoClima,
			String txtDocumentosEntregues,
			int tpTracadoVia,
			int tpRelacao,
			String nrProtocolo,
			int stBoat,
			int lgDanoPatrimonioPublico,
			int lgDanoMeioAmbiente
		) {
		setCdBoat(cdBoat);
		setNrBoat(nrBoat);
		setDtOcorrencia(dtOcorrencia);
		setTpAcidente(tpAcidente);
		setNmTipoAcidenteOutro(nmTipoAcidenteOutro);
		setCdUsuario(cdUsuario);
		setCdAgente(cdAgente);
		setCdCidade(cdCidade);
		setDsObservacao(dsObservacao);
		setDsLocalOcorrencia(dsLocalOcorrencia);
		setDsPontoReferencia(dsPontoReferencia);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
		setCdTipoAcidente(cdTipoAcidente);
		setCdCondicaoVia(cdCondicaoVia);
		setCdTipoPavimento(cdTipoPavimento);
		setCdCondicaoClima(cdCondicaoClima);
		setStSinalizacaoHorizontal(stSinalizacaoHorizontal);
		setStSinalizacaoVertical(stSinalizacaoVertical);
		setStSemaforo(stSemaforo);
		setNmDelegaciaComunicada(nmDelegaciaComunicada);
		setDtComunicacao(dtComunicacao);
		setNrOcorrenciaPolicial(nrOcorrenciaPolicial);
		setNmAgentePolicial(nmAgentePolicial);
		setNrMatriculaAgentePolicial(nrMatriculaAgentePolicial);
		setTxtOcorrenciaPolicial(txtOcorrenciaPolicial);
		setLgPoliciaTecnica(lgPoliciaTecnica);
		setNmResponsavelPoliciaTecnica(nmResponsavelPoliciaTecnica);
		setLgIml(lgIml);
		setNmResponsavelIml(nmResponsavelIml);
		setLgPericiaRealizada(lgPericiaRealizada);
		setNmResponsavelPericia(nmResponsavelPericia);
		setLgBombeiros(lgBombeiros);
		setNmResponsavelBombeiros(nmResponsavelBombeiros);
		setLgResgate(lgResgate);
		setNmResponsavelResgate(nmResponsavelResgate);
		setNmOutroOrgao(nmOutroOrgao);
		setNmResponsavelOutroOrgao(nmResponsavelOutroOrgao);
		setBlbDiagrama(blbDiagrama);
		setImgDiagrama(imgDiagrama);
		setTxtDescricaoSumaria(txtDescricaoSumaria);
		setTpCaracteristicaAcidente(tpCaracteristicaAcidente);
		setTpCondicaoVia(tpCondicaoVia);
		setTpPavimento(tpPavimento);
		setTpCondicaoClima(tpCondicaoClima);
		setNmOutraCondicaoVia(nmOutraCondicaoVia);
		setNmOutroPavimento(nmOutroPavimento);
		setNmOutraCondicaoClima(nmOutraCondicaoClima);

		setTxtDocumentosEntregues(txtDocumentosEntregues);
		
		setTpTracadoVia(tpTracadoVia);
		setNrProtocolo(nrProtocolo);
		setStBoat(stBoat);
		
		setLgDanoPatrimonioPublico(lgDanoPatrimonioPublico);
		setLgDanoMeioAmbiente(lgDanoMeioAmbiente);
	}

	public void setCdBoat(int cdBoat){
		this.cdBoat=cdBoat;
	}
	public int getCdBoat(){
		return this.cdBoat;
	}
	public void setNrBoat(int nrBoat){
		this.nrBoat=nrBoat;
	}
	public int getNrBoat(){
		return this.nrBoat;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}
	public void setTpAcidente(int tpAcidente){
		this.tpAcidente=tpAcidente;
	}
	public int getTpAcidente(){
		return this.tpAcidente;
	}
	public void setNmTipoAcidenteOutro(String nmTipoAcidenteOutro){
		this.nmTipoAcidenteOutro=nmTipoAcidenteOutro;
	}
	public String getNmTipoAcidenteOutro(){
		return this.nmTipoAcidenteOutro;
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
	public void setCdTipoAcidente(int cdTipoAcidente){
		this.cdTipoAcidente=cdTipoAcidente;
	}
	public int getCdTipoAcidente(){
		return this.cdTipoAcidente;
	}
	public void setCdCondicaoVia(int cdCondicaoVia){
		this.cdCondicaoVia=cdCondicaoVia;
	}
	public int getCdCondicaoVia(){
		return this.cdCondicaoVia;
	}
	public void setCdTipoPavimento(int cdTipoPavimento){
		this.cdTipoPavimento=cdTipoPavimento;
	}
	public int getCdTipoPavimento(){
		return this.cdTipoPavimento;
	}
	public void setCdCondicaoClima(int cdCondicaoClima){
		this.cdCondicaoClima=cdCondicaoClima;
	}
	public int getCdCondicaoClima(){
		return this.cdCondicaoClima;
	}
	public void setStSinalizacaoHorizontal(int stSinalizacaoHorizontal){
		this.stSinalizacaoHorizontal=stSinalizacaoHorizontal;
	}
	public int getStSinalizacaoHorizontal(){
		return this.stSinalizacaoHorizontal;
	}
	public void setStSinalizacaoVertical(int stSinalizacaoVertical){
		this.stSinalizacaoVertical=stSinalizacaoVertical;
	}
	public int getStSinalizacaoVertical(){
		return this.stSinalizacaoVertical;
	}
	public void setStSemaforo(int stSemaforo){
		this.stSemaforo=stSemaforo;
	}
	public int getStSemaforo(){
		return this.stSemaforo;
	}
	public void setNmDelegaciaComunicada(String nmDelegaciaComunicada){
		this.nmDelegaciaComunicada=nmDelegaciaComunicada;
	}
	public String getNmDelegaciaComunicada(){
		return this.nmDelegaciaComunicada;
	}
	public void setDtComunicacao(GregorianCalendar dtComunicacao){
		this.dtComunicacao=dtComunicacao;
	}
	public GregorianCalendar getDtComunicacao(){
		return this.dtComunicacao;
	}
	public void setNrOcorrenciaPolicial(String nrOcorrenciaPolicial){
		this.nrOcorrenciaPolicial=nrOcorrenciaPolicial;
	}
	public String getNrOcorrenciaPolicial(){
		return this.nrOcorrenciaPolicial;
	}
	public void setNmAgentePolicial(String nmAgentePolicial){
		this.nmAgentePolicial=nmAgentePolicial;
	}
	public String getNmAgentePolicial(){
		return this.nmAgentePolicial;
	}
	public void setNrMatriculaAgentePolicial(String nrMatriculaAgentePolicial){
		this.nrMatriculaAgentePolicial=nrMatriculaAgentePolicial;
	}
	public String getNrMatriculaAgentePolicial(){
		return this.nrMatriculaAgentePolicial;
	}
	public void setTxtOcorrenciaPolicial(String txtOcorrenciaPolicial){
		this.txtOcorrenciaPolicial=txtOcorrenciaPolicial;
	}
	public String getTxtOcorrenciaPolicial(){
		return this.txtOcorrenciaPolicial;
	}
	public void setLgPoliciaTecnica(int lgPoliciaTecnica){
		this.lgPoliciaTecnica=lgPoliciaTecnica;
	}
	public int getLgPoliciaTecnica(){
		return this.lgPoliciaTecnica;
	}
	public void setNmResponsavelPoliciaTecnica(String nmResponsavelPoliciaTecnica){
		this.nmResponsavelPoliciaTecnica=nmResponsavelPoliciaTecnica;
	}
	public String getNmResponsavelPoliciaTecnica(){
		return this.nmResponsavelPoliciaTecnica;
	}
	public void setLgIml(int lgIml){
		this.lgIml=lgIml;
	}
	public int getLgIml(){
		return this.lgIml;
	}
	public void setNmResponsavelIml(String nmResponsavelIml){
		this.nmResponsavelIml=nmResponsavelIml;
	}
	public String getNmResponsavelIml(){
		return this.nmResponsavelIml;
	}
	public void setLgPericiaRealizada(int lgPericiaRealizada){
		this.lgPericiaRealizada=lgPericiaRealizada;
	}
	public int getLgPericiaRealizada(){
		return this.lgPericiaRealizada;
	}
	public void setNmResponsavelPericia(String nmResponsavelPericia){
		this.nmResponsavelPericia=nmResponsavelPericia;
	}
	public String getNmResponsavelPericia(){
		return this.nmResponsavelPericia;
	}
	public void setLgBombeiros(String lgBombeiros){
		this.lgBombeiros=lgBombeiros;
	}
	public String getLgBombeiros(){
		return this.lgBombeiros;
	}
	public void setNmResponsavelBombeiros(String nmResponsavelBombeiros){
		this.nmResponsavelBombeiros=nmResponsavelBombeiros;
	}
	public String getNmResponsavelBombeiros(){
		return this.nmResponsavelBombeiros;
	}
	public void setLgResgate(int lgResgate){
		this.lgResgate=lgResgate;
	}
	public int getLgResgate(){
		return this.lgResgate;
	}
	public void setNmResponsavelResgate(String nmResponsavelResgate){
		this.nmResponsavelResgate=nmResponsavelResgate;
	}
	public String getNmResponsavelResgate(){
		return this.nmResponsavelResgate;
	}
	public void setNmOutroOrgao(String nmOutroOrgao){
		this.nmOutroOrgao=nmOutroOrgao;
	}
	public String getNmOutroOrgao(){
		return this.nmOutroOrgao;
	}
	public void setNmResponsavelOutroOrgao(String nmResponsavelOutroOrgao){
		this.nmResponsavelOutroOrgao=nmResponsavelOutroOrgao;
	}
	public String getNmResponsavelOutroOrgao(){
		return this.nmResponsavelOutroOrgao;
	}
	public void setBlbDiagrama(byte[] blbDiagrama){
		this.blbDiagrama=blbDiagrama;
	}
	public byte[] getBlbDiagrama(){
		return this.blbDiagrama;
	}
	public void setImgDiagrama(byte[] imgDiagrama){
		this.imgDiagrama=imgDiagrama;
	}
	public byte[] getImgDiagrama(){
		return this.imgDiagrama;
	}
	public void setTxtDescricaoSumaria(String txtDescricaoSumaria){
		this.txtDescricaoSumaria=txtDescricaoSumaria;
	}
	public String getTxtDescricaoSumaria(){
		return this.txtDescricaoSumaria;
	}
	public void setTpCaracteristicaAcidente(int tpCaracteristicaAcidente){
		this.tpCaracteristicaAcidente=tpCaracteristicaAcidente;
	}
	public int getTpCaracteristicaAcidente(){
		return this.tpCaracteristicaAcidente;
	}
	public void setTpCondicaoVia(int tpCondicaoVia){
		this.tpCondicaoVia=tpCondicaoVia;
	}
	public int getTpCondicaoVia(){
		return this.tpCondicaoVia;
	}
	public void setTpPavimento(int tpPavimento){
		this.tpPavimento=tpPavimento;
	}
	public int getTpPavimento(){
		return this.tpPavimento;
	}
	public void setTpCondicaoClima(int tpCondicaoClima){
		this.tpCondicaoClima=tpCondicaoClima;
	}
	public int getTpCondicaoClima(){
		return this.tpCondicaoClima;
	}
	public void setNmOutraCondicaoVia(String nmOutraCondicaoVia){
		this.nmOutraCondicaoVia=nmOutraCondicaoVia;
	}
	public String getNmOutraCondicaoVia(){
		return this.nmOutraCondicaoVia;
	}
	public void setNmOutroPavimento(String nmOutroPavimento){
		this.nmOutroPavimento=nmOutroPavimento;
	}
	public String getNmOutroPavimento(){
		return this.nmOutroPavimento;
	}
	public void setNmOutraCondicaoClima(String nmOutraCondicaoClima){
		this.nmOutraCondicaoClima=nmOutraCondicaoClima;
	}
	public String getNmOutraCondicaoClima(){
		return this.nmOutraCondicaoClima;
	}	

	public void setTxtDocumentosEntregues(String txtDocumentosEntregues){
		this.txtDocumentosEntregues=txtDocumentosEntregues;
	}
	public String getTxtDocumentosEntregues(){
		return this.txtDocumentosEntregues;
	}
	
	public int getTpTracadoVia() {
		return tpTracadoVia;
	}
	public void setTpTracadoVia(int tpTracadoVia) {
		this.tpTracadoVia = tpTracadoVia;
	}

	public void setNrProtocolo(String nrProtocolo){
		this.nrProtocolo=nrProtocolo;
	}
	public String getNrProtocolo(){
		return this.nrProtocolo;
	}
	public void setStBoat(int stBoat){
		this.stBoat=stBoat;
	}
	public int getStBoat(){
		return this.stBoat;
	}

	public int getLgDanoPatrimonioPublico() {
		return lgDanoPatrimonioPublico;
	}

	public void setLgDanoPatrimonioPublico(int lgDanoPatrimonioPublico) {
		this.lgDanoPatrimonioPublico = lgDanoPatrimonioPublico;
	}

	public int getLgDanoMeioAmbiente() {
		return lgDanoMeioAmbiente;
	}

	public void setLgDanoMeioAmbiente(int lgDanoMeioAmbiente) {
		this.lgDanoMeioAmbiente = lgDanoMeioAmbiente;
	}

	public void setTxtJustificativa(String txtJustificativa){
		this.txtJustificativa=txtJustificativa;
	}
	public String getTxtJustificativa(){
		return this.txtJustificativa;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdBoat: " +  getCdBoat();
		valueToString += ", nrBoat: " +  getNrBoat();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpAcidente: " +  getTpAcidente();
		valueToString += ", nmTipoAcidenteOutro: " +  getNmTipoAcidenteOutro();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", dsObservacao: " +  getDsObservacao();
		valueToString += ", dsLocalOcorrencia: " +  getDsLocalOcorrencia();
		valueToString += ", dsPontoReferencia: " +  getDsPontoReferencia();
		valueToString += ", vlLatitude: " +  getVlLatitude();
		valueToString += ", vlLongitude: " +  getVlLongitude();
		valueToString += ", cdTipoAcidente: " +  getCdTipoAcidente();
		valueToString += ", cdCondicaoVia: " +  getCdCondicaoVia();
		valueToString += ", cdTipoPavimento: " +  getCdTipoPavimento();
		valueToString += ", cdCondicaoClima: " +  getCdCondicaoClima();
		valueToString += ", stSinalizacaoHorizontal: " +  getStSinalizacaoHorizontal();
		valueToString += ", stSinalizacaoVertical: " +  getStSinalizacaoVertical();
		valueToString += ", stSemaforo: " +  getStSemaforo();
		valueToString += ", nmDelegaciaComunicada: " +  getNmDelegaciaComunicada();
		valueToString += ", dtComunicacao: " +  sol.util.Util.formatDateTime(getDtComunicacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrOcorrenciaPolicial: " +  getNrOcorrenciaPolicial();
		valueToString += ", nmAgentePolicial: " +  getNmAgentePolicial();
		valueToString += ", nrMatriculaAgentePolicial: " +  getNrMatriculaAgentePolicial();
		valueToString += ", txtOcorrenciaPolicial: " +  getTxtOcorrenciaPolicial();
		valueToString += ", lgPoliciaTecnica: " +  getLgPoliciaTecnica();
		valueToString += ", nmResponsavelPoliciaTecnica: " +  getNmResponsavelPoliciaTecnica();
		valueToString += ", lgIml: " +  getLgIml();
		valueToString += ", nmResponsavelIml: " +  getNmResponsavelIml();
		valueToString += ", lgPericiaRealizada: " +  getLgPericiaRealizada();
		valueToString += ", nmResponsavelPericia: " +  getNmResponsavelPericia();
		valueToString += ", lgBombeiros: " +  getLgBombeiros();
		valueToString += ", nmResponsavelBombeiros: " +  getNmResponsavelBombeiros();
		valueToString += ", lgResgate: " +  getLgResgate();
		valueToString += ", nmResponsavelResgate: " +  getNmResponsavelResgate();
		valueToString += ", nmOutroOrgao: " +  getNmOutroOrgao();
		valueToString += ", nmResponsavelOutroOrgao: " +  getNmResponsavelOutroOrgao();
		valueToString += ", blbDiagrama: " +  getBlbDiagrama();
		valueToString += ", imgDiagrama: " +  getImgDiagrama();
		valueToString += ", txtDescricaoSumaria: " +  getTxtDescricaoSumaria();
		valueToString += ", tpCaracteristicaAcidente: " +  getTpCaracteristicaAcidente();
		valueToString += ", tpCondicaoVia: " +  getTpCondicaoVia();
		valueToString += ", tpPavimento: " +  getTpPavimento();
		valueToString += ", tpCondicaoClima: " +  getTpCondicaoClima();
		valueToString += ", nmOutraCondicaoVia: " +  getNmOutraCondicaoVia();
		valueToString += ", nmOutroPavimento: " +  getNmOutroPavimento();
		valueToString += ", nmOutraCondicaoClima: " +  getNmOutraCondicaoClima();
		valueToString += ", txtDocumentosEntregues: " +  getTxtDocumentosEntregues();
		valueToString += ", nrProtocolo: " +  getNrProtocolo();
		valueToString += ", stBoat: " +  getStBoat();
		valueToString += ", lgDanoPatrimonioPublico: " +  getLgDanoPatrimonioPublico();
		valueToString += ", lgDanoMeioAmbiente: " +  getLgDanoMeioAmbiente();
		valueToString += ", txtJustificativa: " +  getTxtJustificativa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Boat(getCdBoat(),
			getNrBoat(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone(),
			getTpAcidente(),
			getNmTipoAcidenteOutro(),
			getCdUsuario(),
			getCdAgente(),
			getCdCidade(),
			getDsObservacao(),
			getDsLocalOcorrencia(),
			getDsPontoReferencia(),
			getVlLatitude(),
			getVlLongitude(),
			getCdTipoAcidente(),
			getCdCondicaoVia(),
			getCdTipoPavimento(),
			getCdCondicaoClima(),
			getStSinalizacaoHorizontal(),
			getStSinalizacaoVertical(),
			getStSemaforo(),
			getNmDelegaciaComunicada(),
			getDtComunicacao()==null ? null : (GregorianCalendar)getDtComunicacao().clone(),
			getNrOcorrenciaPolicial(),
			getNmAgentePolicial(),
			getNrMatriculaAgentePolicial(),
			getTxtOcorrenciaPolicial(),
			getLgPoliciaTecnica(),
			getNmResponsavelPoliciaTecnica(),
			getLgIml(),
			getNmResponsavelIml(),
			getLgPericiaRealizada(),
			getNmResponsavelPericia(),
			getLgBombeiros(),
			getNmResponsavelBombeiros(),
			getLgResgate(),
			getNmResponsavelResgate(),
			getNmOutroOrgao(),
			getNmResponsavelOutroOrgao(),
			getBlbDiagrama(),
			getImgDiagrama(),
			getTxtDescricaoSumaria(),
			getTpCaracteristicaAcidente(),
			getTpCondicaoVia(),
			getTpPavimento(),
			getTpCondicaoClima(),
			getTpTracadoVia(),
			getNmOutraCondicaoVia(),
			getNmOutroPavimento(),
			getNmOutraCondicaoClima(),
			getTxtDocumentosEntregues(),
			getNrProtocolo(),
			getStBoat(),
			getLgDanoPatrimonioPublico(),
			getLgDanoMeioAmbiente(),
			getTxtJustificativa());
	}

}