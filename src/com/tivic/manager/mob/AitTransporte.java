package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AitTransporte {

	private int cdAit;
	private int cdInfracao;
	private String dsProvidencia;
	private GregorianCalendar dtPrazo;
	private int cdAgente;
	private String nmPreposto;
	private String nrAit;
	private String dsObservacao;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtInfracao;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtEmissaoNip;
	
	private int nrViaNip;
	private int stAit;
	private int cdMotivo;
	private int cdUsuarioCancelamento;
	private int cdOcorrencia;
	private Double vlLongitude;
	private Double vlLatitude;
	private int cdCidade;
	private int cdEquipamento;
	private String nrPonto;
	private String dsLocalInfracao;
	private int lgReincidencia;
	private String nmTestemunha1;
	private String nrRgTestemunha1;
	private String nmEnderecoTestemunha1;
	private String nmTestemunha2;
	private String nrRgTestemunha2;
	private String nmEnderecoTestemunha2;
	private int tpAit;
	private int cdTalao;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtNotificacaoInicial;
	
	private int cdConcessaoVeiculo;
	private int cdLinha;
	private int cdRecurso1;
	private int cdRecurso2;
	private int cdContaReceber;
	private int cdConcessao;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtLimiteAnalisarRecurso1;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtLimiteNotificarRecurso1;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtLimiteRecurso2;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtLimiteAnalisarRecurso2;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtLimiteNotificarRecurso2;
	
	private GregorianCalendar dtJulgamento1;
	private GregorianCalendar dtJulgamento2;
	private GregorianCalendar dtCancelamento;
	private GregorianCalendar dtNotificacao1;
	private GregorianCalendar dtNotificacao2;
	private int stAitCancelada;
	
	@JsonSerialize(converter = CalendarSerializer.class) 
	private GregorianCalendar dtRecebimento;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtLimiteEmissao;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtLimiteRecurso1;
	
	private int cdMotivoEncerramentoAit;
	private Double vlMulta;

	public AitTransporte() { }
	
	public AitTransporte(int cdAit,
			int cdInfracao,
			String dsProvidencia,
			GregorianCalendar dtPrazo,
			int cdAgente,
			String nmPreposto,
			String nrAit,
			String dsObservacao,
			GregorianCalendar dtInfracao,
			GregorianCalendar dtEmissaoNip,
			int nrViaNip,
			int stAit,
			int cdMotivo,
			int cdUsuarioCancelamento,
			int cdOcorrencia,
			Double vlLongitude,
			Double vlLatitude,
			int cdCidade,
			int cdEquipamento,
			String nrPonto,
			String dsLocalInfracao,
			int lgReincidencia,
			String nmTestemunha1,
			String nrRgTestemunha1,
			String nmEnderecoTestemunha1,
			String nmTestemunha2,
			String nrRgTestemunha2,
			String nmEnderecoTestemunha2,
			int tpAit,
			int cdTalao,
			GregorianCalendar dtNotificacaoInicial,
			int cdConcessaoVeiculo,
			int cdLinha,
			int cdRecurso1,
			int cdRecurso2,
			int cdContaReceber,
			int cdConcessao,
			GregorianCalendar dtLimiteAnalisarRecurso1,
			GregorianCalendar dtLimiteNotificarRecurso1,
			GregorianCalendar dtLimiteRecurso2,
			GregorianCalendar dtLimiteAnalisarRecurso2,
			GregorianCalendar dtLimiteNotificarRecurso2,
			GregorianCalendar dtJulgamento1,
			GregorianCalendar dtJulgamento2,
			GregorianCalendar dtCancelamento,
			GregorianCalendar dtNotificacao1,
			GregorianCalendar dtNotificacao2,
			int stAitCancelada,
			GregorianCalendar dtRecebimento,
			GregorianCalendar dtLimiteEmissao,
			GregorianCalendar dtLimiteRecurso1,
			int cdMotivoEncerramentoAit) {
		setCdAit(cdAit);
		setCdInfracao(cdInfracao);
		setDsProvidencia(dsProvidencia);
		setDtPrazo(dtPrazo);
		setCdAgente(cdAgente);
		setNmPreposto(nmPreposto);
		setNrAit(nrAit);
		setDsObservacao(dsObservacao);
		setDtInfracao(dtInfracao);
		setDtEmissaoNip(dtEmissaoNip);
		setNrViaNip(nrViaNip);
		setStAit(stAit);
		setCdMotivo(cdMotivo);
		setCdUsuarioCancelamento(cdUsuarioCancelamento);
		setCdOcorrencia(cdOcorrencia);
		setVlLongitude(vlLongitude);
		setVlLatitude(vlLatitude);
		setCdCidade(cdCidade);
		setCdEquipamento(cdEquipamento);
		setNrPonto(nrPonto);
		setDsLocalInfracao(dsLocalInfracao);
		setLgReincidencia(lgReincidencia);
		setNmTestemunha1(nmTestemunha1);
		setNrRgTestemunha1(nrRgTestemunha1);
		setNmEnderecoTestemunha1(nmEnderecoTestemunha1);
		setNmTestemunha2(nmTestemunha2);
		setNrRgTestemunha2(nrRgTestemunha2);
		setNmEnderecoTestemunha2(nmEnderecoTestemunha2);
		setTpAit(tpAit);
		setCdTalao(cdTalao);
		setDtNotificacaoInicial(dtNotificacaoInicial);
		setCdConcessaoVeiculo(cdConcessaoVeiculo);
		setCdLinha(cdLinha);
		setCdRecurso1(cdRecurso1);
		setCdRecurso2(cdRecurso2);
		setCdContaReceber(cdContaReceber);
		setCdConcessao(cdConcessao);
		setDtLimiteAnalisarRecurso1(dtLimiteAnalisarRecurso1);
		setDtLimiteNotificarRecurso1(dtLimiteNotificarRecurso1);
		setDtLimiteRecurso2(dtLimiteRecurso2);
		setDtLimiteAnalisarRecurso2(dtLimiteAnalisarRecurso2);
		setDtLimiteNotificarRecurso2(dtLimiteNotificarRecurso2);
		setDtJulgamento1(dtJulgamento1);
		setDtJulgamento2(dtJulgamento2);
		setDtCancelamento(dtCancelamento);
		setDtNotificacao1(dtNotificacao1);
		setDtNotificacao2(dtNotificacao2);
		setStAitCancelada(stAitCancelada);
		setDtRecebimento(dtRecebimento);
		setDtLimiteEmissao(dtLimiteEmissao);
		setDtLimiteRecurso1(dtLimiteRecurso1);
		setCdMotivoEncerramentoAit(cdMotivoEncerramentoAit);
	}

	public AitTransporte(int cdAit,
			int cdInfracao,
			String dsProvidencia,
			GregorianCalendar dtPrazo,
			int cdAgente,
			String nmPreposto,
			String nrAit,
			String dsObservacao,
			GregorianCalendar dtInfracao,
			GregorianCalendar dtEmissaoNip,
			int nrViaNip,
			int stAit,
			int cdMotivo,
			int cdUsuarioCancelamento,
			int cdOcorrencia,
			Double vlLongitude,
			Double vlLatitude,
			int cdCidade,
			int cdEquipamento,
			String nrPonto,
			String dsLocalInfracao,
			int lgReincidencia,
			String nmTestemunha1,
			String nrRgTestemunha1,
			String nmEnderecoTestemunha1,
			String nmTestemunha2,
			String nrRgTestemunha2,
			String nmEnderecoTestemunha2,
			int tpAit,
			int cdTalao,
			GregorianCalendar dtNotificacaoInicial,
			int cdConcessaoVeiculo,
			int cdLinha,
			int cdRecurso1,
			int cdRecurso2,
			int cdContaReceber,
			int cdConcessao,
			GregorianCalendar dtLimiteAnalisarRecurso1,
			GregorianCalendar dtLimiteNotificarRecurso1,
			GregorianCalendar dtLimiteRecurso2,
			GregorianCalendar dtLimiteAnalisarRecurso2,
			GregorianCalendar dtLimiteNotificarRecurso2,
			GregorianCalendar dtJulgamento1,
			GregorianCalendar dtJulgamento2,
			GregorianCalendar dtCancelamento,
			GregorianCalendar dtNotificacao1,
			GregorianCalendar dtNotificacao2,
			int stAitCancelada,
			GregorianCalendar dtRecebimento,
			GregorianCalendar dtLimiteEmissao,
			GregorianCalendar dtLimiteRecurso1,
			int cdMotivoEncerramentoAit,
			Double vlMulta) {
		setCdAit(cdAit);
		setCdInfracao(cdInfracao);
		setDsProvidencia(dsProvidencia);
		setDtPrazo(dtPrazo);
		setCdAgente(cdAgente);
		setNmPreposto(nmPreposto);
		setNrAit(nrAit);
		setDsObservacao(dsObservacao);
		setDtInfracao(dtInfracao);
		setDtEmissaoNip(dtEmissaoNip);
		setNrViaNip(nrViaNip);
		setStAit(stAit);
		setCdMotivo(cdMotivo);
		setCdUsuarioCancelamento(cdUsuarioCancelamento);
		setCdOcorrencia(cdOcorrencia);
		setVlLongitude(vlLongitude);
		setVlLatitude(vlLatitude);
		setCdCidade(cdCidade);
		setCdEquipamento(cdEquipamento);
		setNrPonto(nrPonto);
		setDsLocalInfracao(dsLocalInfracao);
		setLgReincidencia(lgReincidencia);
		setNmTestemunha1(nmTestemunha1);
		setNrRgTestemunha1(nrRgTestemunha1);
		setNmEnderecoTestemunha1(nmEnderecoTestemunha1);
		setNmTestemunha2(nmTestemunha2);
		setNrRgTestemunha2(nrRgTestemunha2);
		setNmEnderecoTestemunha2(nmEnderecoTestemunha2);
		setTpAit(tpAit);
		setCdTalao(cdTalao);
		setDtNotificacaoInicial(dtNotificacaoInicial);
		setCdConcessaoVeiculo(cdConcessaoVeiculo);
		setCdLinha(cdLinha);
		setCdRecurso1(cdRecurso1);
		setCdRecurso2(cdRecurso2);
		setCdContaReceber(cdContaReceber);
		setCdConcessao(cdConcessao);
		setDtLimiteAnalisarRecurso1(dtLimiteAnalisarRecurso1);
		setDtLimiteNotificarRecurso1(dtLimiteNotificarRecurso1);
		setDtLimiteRecurso2(dtLimiteRecurso2);
		setDtLimiteAnalisarRecurso2(dtLimiteAnalisarRecurso2);
		setDtLimiteNotificarRecurso2(dtLimiteNotificarRecurso2);
		setDtJulgamento1(dtJulgamento1);
		setDtJulgamento2(dtJulgamento2);
		setDtCancelamento(dtCancelamento);
		setDtNotificacao1(dtNotificacao1);
		setDtNotificacao2(dtNotificacao2);
		setStAitCancelada(stAitCancelada);
		setDtRecebimento(dtRecebimento);
		setDtLimiteEmissao(dtLimiteEmissao);
		setDtLimiteRecurso1(dtLimiteRecurso1);
		setCdMotivoEncerramentoAit(cdMotivoEncerramentoAit);
		setVlMulta(vlMulta);
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
	public void setDsProvidencia(String dsProvidencia){
		this.dsProvidencia=dsProvidencia;
	}
	public String getDsProvidencia(){
		return this.dsProvidencia;
	}
	public void setDtPrazo(GregorianCalendar dtPrazo){
		this.dtPrazo=dtPrazo;
	}
	public GregorianCalendar getDtPrazo(){
		return this.dtPrazo;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setNmPreposto(String nmPreposto){
		this.nmPreposto=nmPreposto;
	}
	public String getNmPreposto(){
		return this.nmPreposto;
	}
	public void setNrAit(String nrAit){
		this.nrAit=nrAit;
	}
	public String getNrAit(){
		return this.nrAit;
	}
	public void setDsObservacao(String dsObservacao){
		this.dsObservacao=dsObservacao;
	}
	public String getDsObservacao(){
		return this.dsObservacao;
	}
	public void setDtInfracao(GregorianCalendar dtInfracao){
		this.dtInfracao=dtInfracao;
	}
	public GregorianCalendar getDtInfracao(){
		return this.dtInfracao;
	}
	public void setDtEmissaoNip(GregorianCalendar dtEmissaoNip){
		this.dtEmissaoNip=dtEmissaoNip;
	}
	public GregorianCalendar getDtEmissaoNip(){
		return this.dtEmissaoNip;
	}
	public void setNrViaNip(int nrViaNip){
		this.nrViaNip=nrViaNip;
	}
	public int getNrViaNip(){
		return this.nrViaNip;
	}
	public void setStAit(int stAit){
		this.stAit=stAit;
	}
	public int getStAit(){
		return this.stAit;
	}
	public void setCdMotivo(int cdMotivo){
		this.cdMotivo=cdMotivo;
	}
	public int getCdMotivo(){
		return this.cdMotivo;
	}
	public void setCdUsuarioCancelamento(int cdUsuarioCancelamento){
		this.cdUsuarioCancelamento=cdUsuarioCancelamento;
	}
	public int getCdUsuarioCancelamento(){
		return this.cdUsuarioCancelamento;
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public void setVlLongitude(Double vlLongitude){
		this.vlLongitude=vlLongitude;
	}
	public Double getVlLongitude(){
		return this.vlLongitude;
	}
	public void setVlLatitude(Double vlLatitude){
		this.vlLatitude=vlLatitude;
	}
	public Double getVlLatitude(){
		return this.vlLatitude;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setCdEquipamento(int cdEquipamento){
		this.cdEquipamento=cdEquipamento;
	}
	public int getCdEquipamento(){
		return this.cdEquipamento;
	}
	public void setNrPonto(String nrPonto){
		this.nrPonto=nrPonto;
	}
	public String getNrPonto(){
		return this.nrPonto;
	}
	public void setDsLocalInfracao(String dsLocalInfracao){
		this.dsLocalInfracao=dsLocalInfracao;
	}
	public String getDsLocalInfracao(){
		return this.dsLocalInfracao;
	}
	public void setLgReincidencia(int lgReincidencia){
		this.lgReincidencia=lgReincidencia;
	}
	public int getLgReincidencia(){
		return this.lgReincidencia;
	}
	public void setNmTestemunha1(String nmTestemunha1){
		this.nmTestemunha1=nmTestemunha1;
	}
	public String getNmTestemunha1(){
		return this.nmTestemunha1;
	}
	public void setNrRgTestemunha1(String nrRgTestemunha1){
		this.nrRgTestemunha1=nrRgTestemunha1;
	}
	public String getNrRgTestemunha1(){
		return this.nrRgTestemunha1;
	}
	public void setNmEnderecoTestemunha1(String nmEnderecoTestemunha1){
		this.nmEnderecoTestemunha1=nmEnderecoTestemunha1;
	}
	public String getNmEnderecoTestemunha1(){
		return this.nmEnderecoTestemunha1;
	}
	public void setNmTestemunha2(String nmTestemunha2){
		this.nmTestemunha2=nmTestemunha2;
	}
	public String getNmTestemunha2(){
		return this.nmTestemunha2;
	}
	public void setNrRgTestemunha2(String nrRgTestemunha2){
		this.nrRgTestemunha2=nrRgTestemunha2;
	}
	public String getNrRgTestemunha2(){
		return this.nrRgTestemunha2;
	}
	public void setNmEnderecoTestemunha2(String nmEnderecoTestemunha2){
		this.nmEnderecoTestemunha2=nmEnderecoTestemunha2;
	}
	public String getNmEnderecoTestemunha2(){
		return this.nmEnderecoTestemunha2;
	}
	public void setTpAit(int tpAit){
		this.tpAit=tpAit;
	}
	public int getTpAit(){
		return this.tpAit;
	}
	public void setCdTalao(int cdTalao){
		this.cdTalao=cdTalao;
	}
	public int getCdTalao(){
		return this.cdTalao;
	}
	public void setDtNotificacaoInicial(GregorianCalendar dtNotificacaoInicial){
		this.dtNotificacaoInicial=dtNotificacaoInicial;
	}
	public GregorianCalendar getDtNotificacaoInicial(){
		return this.dtNotificacaoInicial;
	}
	public void setCdConcessaoVeiculo(int cdConcessaoVeiculo){
		this.cdConcessaoVeiculo=cdConcessaoVeiculo;
	}
	public int getCdConcessaoVeiculo(){
		return this.cdConcessaoVeiculo;
	}
	public void setCdLinha(int cdLinha){
		this.cdLinha=cdLinha;
	}
	public int getCdLinha(){
		return this.cdLinha;
	}
	public void setCdRecurso1(int cdRecurso1){
		this.cdRecurso1=cdRecurso1;
	}
	public int getCdRecurso1(){
		return this.cdRecurso1;
	}
	public void setCdRecurso2(int cdRecurso2){
		this.cdRecurso2=cdRecurso2;
	}
	public int getCdRecurso2(){
		return this.cdRecurso2;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public void setCdConcessao(int cdConcessao){
		this.cdConcessao=cdConcessao;
	}
	public int getCdConcessao(){
		return this.cdConcessao;
	}
	public void setDtLimiteAnalisarRecurso1(GregorianCalendar dtLimiteAnalisarRecurso1){
		this.dtLimiteAnalisarRecurso1=dtLimiteAnalisarRecurso1;
	}
	public GregorianCalendar getDtLimiteAnalisarRecurso1(){
		return this.dtLimiteAnalisarRecurso1;
	}
	public void setDtLimiteNotificarRecurso1(GregorianCalendar dtLimiteNotificarRecurso1){
		this.dtLimiteNotificarRecurso1=dtLimiteNotificarRecurso1;
	}
	public GregorianCalendar getDtLimiteNotificarRecurso1(){
		return this.dtLimiteNotificarRecurso1;
	}
	public void setDtLimiteRecurso2(GregorianCalendar dtLimiteRecurso2){
		this.dtLimiteRecurso2=dtLimiteRecurso2;
	}
	public GregorianCalendar getDtLimiteRecurso2(){
		return this.dtLimiteRecurso2;
	}
	public void setDtLimiteAnalisarRecurso2(GregorianCalendar dtLimiteAnalisarRecurso2){
		this.dtLimiteAnalisarRecurso2=dtLimiteAnalisarRecurso2;
	}
	public GregorianCalendar getDtLimiteAnalisarRecurso2(){
		return this.dtLimiteAnalisarRecurso2;
	}
	public void setDtLimiteNotificarRecurso2(GregorianCalendar dtLimiteNotificarRecurso2){
		this.dtLimiteNotificarRecurso2=dtLimiteNotificarRecurso2;
	}
	public GregorianCalendar getDtLimiteNotificarRecurso2(){
		return this.dtLimiteNotificarRecurso2;
	}
	public void setDtJulgamento1(GregorianCalendar dtJulgamento1){
		this.dtJulgamento1=dtJulgamento1;
	}
	public GregorianCalendar getDtJulgamento1(){
		return this.dtJulgamento1;
	}
	public void setDtJulgamento2(GregorianCalendar dtJulgamento2){
		this.dtJulgamento2=dtJulgamento2;
	}
	public GregorianCalendar getDtJulgamento2(){
		return this.dtJulgamento2;
	}
	public void setDtCancelamento(GregorianCalendar dtCancelamento){
		this.dtCancelamento=dtCancelamento;
	}
	public GregorianCalendar getDtCancelamento(){
		return this.dtCancelamento;
	}
	public void setDtNotificacao1(GregorianCalendar dtNotificacao1){
		this.dtNotificacao1=dtNotificacao1;
	}
	public GregorianCalendar getDtNotificacao1(){
		return this.dtNotificacao1;
	}
	public void setDtNotificacao2(GregorianCalendar dtNotificacao2){
		this.dtNotificacao2=dtNotificacao2;
	}
	public GregorianCalendar getDtNotificacao2(){
		return this.dtNotificacao2;
	}
	public void setStAitCancelada(int stAitCancelada){
		this.stAitCancelada=stAitCancelada;
	}
	public int getStAitCancelada(){
		return this.stAitCancelada;
	}
	public void setDtRecebimento(GregorianCalendar dtRecebimento){
		this.dtRecebimento=dtRecebimento;
	}
	public GregorianCalendar getDtRecebimento(){
		return this.dtRecebimento;
	}
	public void setDtLimiteEmissao(GregorianCalendar dtLimiteEmissao){
		this.dtLimiteEmissao=dtLimiteEmissao;
	}
	public GregorianCalendar getDtLimiteEmissao(){
		return this.dtLimiteEmissao;
	}
	public void setDtLimiteRecurso1(GregorianCalendar dtLimiteRecurso1){
		this.dtLimiteRecurso1=dtLimiteRecurso1;
	}
	public GregorianCalendar getDtLimiteRecurso1(){
		return this.dtLimiteRecurso1;
	}
	public void setCdMotivoEncerramentoAit(int cdMotivoEncerramentoAit){
		this.cdMotivoEncerramentoAit=cdMotivoEncerramentoAit;
	}
	public int getCdMotivoEncerramentoAit(){
		return this.cdMotivoEncerramentoAit;
	}
	public void setVlMulta(Double vlMulta){
		this.vlMulta=vlMulta;
	}
	public Double getVlMulta(){
		return this.vlMulta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAit: " +  getCdAit();
		valueToString += ", cdInfracao: " +  getCdInfracao();
		valueToString += ", dsProvidencia: " +  getDsProvidencia();
		valueToString += ", dtPrazo: " +  sol.util.Util.formatDateTime(getDtPrazo(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", nmPreposto: " +  getNmPreposto();
		valueToString += ", nrAit: " +  getNrAit();
		valueToString += ", dsObservacao: " +  getDsObservacao();
		valueToString += ", dtInfracao: " +  sol.util.Util.formatDateTime(getDtInfracao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtEmissaoNip: " +  sol.util.Util.formatDateTime(getDtEmissaoNip(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrViaNip: " +  getNrViaNip();
		valueToString += ", stAit: " +  getStAit();
		valueToString += ", cdMotivo: " +  getCdMotivo();
		valueToString += ", cdUsuarioCancelamento: " +  getCdUsuarioCancelamento();
		valueToString += ", cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", vlLongitude: " +  getVlLongitude();
		valueToString += ", vlLatitude: " +  getVlLatitude();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", cdEquipamento: " +  getCdEquipamento();
		valueToString += ", nrPonto: " +  getNrPonto();
		valueToString += ", dsLocalInfracao: " +  getDsLocalInfracao();
		valueToString += ", lgReincidencia: " +  getLgReincidencia();
		valueToString += ", nmTestemunha1: " +  getNmTestemunha1();
		valueToString += ", nrRgTestemunha1: " +  getNrRgTestemunha1();
		valueToString += ", nmEnderecoTestemunha1: " +  getNmEnderecoTestemunha1();
		valueToString += ", nmTestemunha2: " +  getNmTestemunha2();
		valueToString += ", nrRgTestemunha2: " +  getNrRgTestemunha2();
		valueToString += ", nmEnderecoTestemunha2: " +  getNmEnderecoTestemunha2();
		valueToString += ", tpAit: " +  getTpAit();
		valueToString += ", cdTalao: " +  getCdTalao();
		valueToString += ", dtNotificacaoInicial: " +  sol.util.Util.formatDateTime(getDtNotificacaoInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdConcessaoVeiculo: " +  getCdConcessaoVeiculo();
		valueToString += ", cdLinha: " +  getCdLinha();
		valueToString += ", cdRecurso1: " +  getCdRecurso1();
		valueToString += ", cdRecurso2: " +  getCdRecurso2();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		valueToString += ", cdConcessao: " +  getCdConcessao();
		valueToString += ", dtLimiteAnalisarRecurso1: " +  sol.util.Util.formatDateTime(getDtLimiteAnalisarRecurso1(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLimiteNotificarRecurso1: " +  sol.util.Util.formatDateTime(getDtLimiteNotificarRecurso1(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLimiteRecurso2: " +  sol.util.Util.formatDateTime(getDtLimiteRecurso2(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLimiteAnalisarRecurso2: " +  sol.util.Util.formatDateTime(getDtLimiteAnalisarRecurso2(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLimiteNotificarRecurso2: " +  sol.util.Util.formatDateTime(getDtLimiteNotificarRecurso2(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtJulgamento1: " +  sol.util.Util.formatDateTime(getDtJulgamento1(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtJulgamento2: " +  sol.util.Util.formatDateTime(getDtJulgamento2(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtCancelamento: " +  sol.util.Util.formatDateTime(getDtCancelamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtNotificacao1: " +  sol.util.Util.formatDateTime(getDtNotificacao1(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtNotificacao2: " +  sol.util.Util.formatDateTime(getDtNotificacao2(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stAitCancelada: " +  getStAitCancelada();
		valueToString += ", dtRecebimento: " +  sol.util.Util.formatDateTime(getDtRecebimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLimiteEmissao: " +  sol.util.Util.formatDateTime(getDtLimiteEmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLimiteRecurso1: " +  sol.util.Util.formatDateTime(getDtLimiteRecurso1(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdMotivoEncerramentoAit: " +  getCdMotivoEncerramentoAit();
		valueToString += ", vlMulta: " +  getVlMulta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitTransporte(getCdAit(),
			getCdInfracao(),
			getDsProvidencia(),
			getDtPrazo()==null ? null : (GregorianCalendar)getDtPrazo().clone(),
			getCdAgente(),
			getNmPreposto(),
			getNrAit(),
			getDsObservacao(),
			getDtInfracao()==null ? null : (GregorianCalendar)getDtInfracao().clone(),
			getDtEmissaoNip()==null ? null : (GregorianCalendar)getDtEmissaoNip().clone(),
			getNrViaNip(),
			getStAit(),
			getCdMotivo(),
			getCdUsuarioCancelamento(),
			getCdOcorrencia(),
			getVlLongitude(),
			getVlLatitude(),
			getCdCidade(),
			getCdEquipamento(),
			getNrPonto(),
			getDsLocalInfracao(),
			getLgReincidencia(),
			getNmTestemunha1(),
			getNrRgTestemunha1(),
			getNmEnderecoTestemunha1(),
			getNmTestemunha2(),
			getNrRgTestemunha2(),
			getNmEnderecoTestemunha2(),
			getTpAit(),
			getCdTalao(),
			getDtNotificacaoInicial()==null ? null : (GregorianCalendar)getDtNotificacaoInicial().clone(),
			getCdConcessaoVeiculo(),
			getCdLinha(),
			getCdRecurso1(),
			getCdRecurso2(),
			getCdContaReceber(),
			getCdConcessao(),
			getDtLimiteAnalisarRecurso1()==null ? null : (GregorianCalendar)getDtLimiteAnalisarRecurso1().clone(),
			getDtLimiteNotificarRecurso1()==null ? null : (GregorianCalendar)getDtLimiteNotificarRecurso1().clone(),
			getDtLimiteRecurso2()==null ? null : (GregorianCalendar)getDtLimiteRecurso2().clone(),
			getDtLimiteAnalisarRecurso2()==null ? null : (GregorianCalendar)getDtLimiteAnalisarRecurso2().clone(),
			getDtLimiteNotificarRecurso2()==null ? null : (GregorianCalendar)getDtLimiteNotificarRecurso2().clone(),
			getDtJulgamento1()==null ? null : (GregorianCalendar)getDtJulgamento1().clone(),
			getDtJulgamento2()==null ? null : (GregorianCalendar)getDtJulgamento2().clone(),
			getDtCancelamento()==null ? null : (GregorianCalendar)getDtCancelamento().clone(),
			getDtNotificacao1()==null ? null : (GregorianCalendar)getDtNotificacao1().clone(),
			getDtNotificacao2()==null ? null : (GregorianCalendar)getDtNotificacao2().clone(),
			getStAitCancelada(),
			getDtRecebimento()==null ? null : (GregorianCalendar)getDtRecebimento().clone(),
			getDtLimiteEmissao()==null ? null : (GregorianCalendar)getDtLimiteEmissao().clone(),
			getDtLimiteRecurso1()==null ? null : (GregorianCalendar)getDtLimiteRecurso1().clone(),
			getCdMotivoEncerramentoAit(),
			getVlMulta());
	}

}