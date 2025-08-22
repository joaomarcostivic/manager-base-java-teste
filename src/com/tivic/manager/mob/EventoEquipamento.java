package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.mob.v2.ait.ConfirmacaoEvento;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;
import com.tivic.sol.util.date.DateUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventoEquipamento {
	private int cdEvento;
	private int cdEquipamento;
	private int cdTipoEvento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtEvento;
	private String nmOrgaoAutuador;
	private String nmEquipamento;
	private String dsLocal;
	private String idIdentificacaoInmetro;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtAfericao;
	private int nrPista;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtConclusao;
	private int vlLimite;
	private int vlVelocidadeTolerada;
	private int vlMedida;
	private int vlConsiderada;
	private String nrPlaca;
	private int lgTempoReal;
	private int tpVeiculo;
	private int vlComprimentoVeiculo;
	private int idMedida;
	private String nrSerial;
	private String nmModeloEquipamento;
	private String nmRodovia;
	private String nmUfRodovia;
	private String nmKmRodovia;
	private String nmMetrosRodovia;
	private String nmSentidoRodovia;
	private int idCidade;
	private String nmMarcaEquipamento;
	private int tpEquipamento;
	private int lgValidaFuncionamento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtExecucaoJob;
	private String idUuid;
	private int tpRestricao;
	private int tpClassificacao;
	private int vlPermanencia;
	private int vlSemaforoVermelho;
	private int stEvento;
	private int cdMotivoCancelamento;
	private String txtEvento;
	private int lgOlpr;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCancelamento;
	private int cdUsuarioCancelamento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtProcessamento;
	private int cdVeiculo;
	private int lgEnviado;
	private int cdUsuarioConfirmacao;
	private int vlTolerancia;

	public EventoEquipamento() { }

	public EventoEquipamento(int cdEvento,
			int cdEquipamento,
			int cdTipoEvento,
			GregorianCalendar dtEvento,
			String nmOrgaoAutuador,
			String nmEquipamento,
			String dsLocal,
			String idIdentificacaoInmetro,
			GregorianCalendar dtAfericao,
			int nrPista,
			GregorianCalendar dtConclusao,
			int vlLimite,
			int vlVelocidadeTolerada,
			int vlMedida,
			int vlConsiderada,
			String nrPlaca,
			int lgTempoReal,
			int tpVeiculo,
			int vlComprimentoVeiculo,
			int idMedida,
			String nrSerial,
			String nmModeloEquipamento,
			String nmRodovia,
			String nmUfRodovia,
			String nmKmRodovia,
			String nmMetrosRodovia,
			String nmSentidoRodovia,
			int idCidade,
			String nmMarcaEquipamento,
			int tpEquipamento,
			int lgValidaFuncionamento,
			GregorianCalendar dtExecucaoJob,
			String idUuid,
			int tpRestricao,
			int tpClassificacao,
			int vlPermanencia,
			int vlSemaforoVermelho,
			int stEvento,
			int cdMotivoCancelamento,
			String txtEvento,
			int lgOlpr,
			GregorianCalendar dtCancelamento,
			int cdUsuarioCancelamento,
			GregorianCalendar dtProcessamento,
			int cdVeiculo,
			int lgEnviado,
			int cdUsuarioConfirmacao,
			int vlTolerancia) {
		setCdEvento(cdEvento);
		setCdEquipamento(cdEquipamento);
		setCdTipoEvento(cdTipoEvento);
		setDtEvento(dtEvento);
		setNmOrgaoAutuador(nmOrgaoAutuador);
		setNmEquipamento(nmEquipamento);
		setDsLocal(dsLocal);
		setIdIdentificacaoInmetro(idIdentificacaoInmetro);
		setDtAfericao(dtAfericao);
		setNrPista(nrPista);
		setDtConclusao(dtConclusao);
		setVlLimite(vlLimite);
		setVlVelocidadeTolerada(vlVelocidadeTolerada);
		setVlMedida(vlMedida);
		setVlConsiderada(vlConsiderada);
		setNrPlaca(nrPlaca);
		setLgTempoReal(lgTempoReal);
		setTpVeiculo(tpVeiculo);
		setVlComprimentoVeiculo(vlComprimentoVeiculo);
		setIdMedida(idMedida);
		setNrSerial(nrSerial);
		setNmModeloEquipamento(nmModeloEquipamento);
		setNmRodovia(nmRodovia);
		setNmUfRodovia(nmUfRodovia);
		setNmKmRodovia(nmKmRodovia);
		setNmMetrosRodovia(nmMetrosRodovia);
		setNmSentidoRodovia(nmSentidoRodovia);
		setIdCidade(idCidade);
		setNmMarcaEquipamento(nmMarcaEquipamento);
		setTpEquipamento(tpEquipamento);
		setLgValidaFuncionamento(lgValidaFuncionamento);
		setDtExecucaoJob(dtExecucaoJob);
		setIdUuid(idUuid);
		setTpRestricao(tpRestricao);
		setTpClassificacao(tpClassificacao);
		setVlPermanencia(vlPermanencia);
		setVlSemaforoVermelho(vlSemaforoVermelho);
		setStEvento(stEvento);
		setCdMotivoCancelamento(cdMotivoCancelamento);
		setTxtEvento(txtEvento);
		setLgOlpr(lgOlpr);
		setDtCancelamento(dtCancelamento);
		setCdUsuarioCancelamento(cdUsuarioCancelamento);
		setDtProcessamento(dtProcessamento);
		setCdVeiculo(cdVeiculo);
		setLgEnviado(lgEnviado);
		setCdUsuarioConfirmacao(cdUsuarioConfirmacao);
		setVlTolerancia(vlTolerancia);
	}
	public void setCdEvento(int cdEvento){
		this.cdEvento=cdEvento;
	}
	public int getCdEvento(){
		return this.cdEvento;
	}
	public void setCdEquipamento(int cdEquipamento){
		this.cdEquipamento=cdEquipamento;
	}
	public int getCdEquipamento(){
		return this.cdEquipamento;
	}
	public void setCdTipoEvento(int cdTipoEvento){
		this.cdTipoEvento=cdTipoEvento;
	}
	public int getCdTipoEvento(){
		return this.cdTipoEvento;
	}
	public void setDtEvento(GregorianCalendar dtEvento){
		this.dtEvento=dtEvento;
	}
	public GregorianCalendar getDtEvento(){
		return this.dtEvento;
	}
	public void setNmOrgaoAutuador(String nmOrgaoAutuador){
		this.nmOrgaoAutuador=nmOrgaoAutuador;
	}
	public String getNmOrgaoAutuador(){
		return this.nmOrgaoAutuador;
	}
	public void setNmEquipamento(String nmEquipamento){
		this.nmEquipamento=nmEquipamento;
	}
	public String getNmEquipamento(){
		return this.nmEquipamento;
	}
	public void setDsLocal(String dsLocal){
		this.dsLocal=dsLocal;
	}
	public String getDsLocal(){
		return this.dsLocal;
	}
	public void setIdIdentificacaoInmetro(String idIdentificacaoInmetro){
		this.idIdentificacaoInmetro=idIdentificacaoInmetro;
	}
	public String getIdIdentificacaoInmetro(){
		return this.idIdentificacaoInmetro;
	}
	public void setDtAfericao(GregorianCalendar dtAfericao){
		this.dtAfericao=dtAfericao;
	}
	public GregorianCalendar getDtAfericao(){
		return this.dtAfericao;
	}
	public void setNrPista(int nrPista){
		this.nrPista=nrPista;
	}
	public int getNrPista(){
		return this.nrPista;
	}
	public void setDtConclusao(GregorianCalendar dtConclusao){
		this.dtConclusao=dtConclusao;
	}
	public GregorianCalendar getDtConclusao(){
		return this.dtConclusao;
	}
	public void setVlLimite(int vlLimite){
		this.vlLimite=vlLimite;
	}
	public int getVlLimite(){
		return this.vlLimite;
	}
	public void setVlVelocidadeTolerada(int vlVelocidadeTolerada){
		this.vlVelocidadeTolerada=vlVelocidadeTolerada;
	}
	public int getVlVelocidadeTolerada(){
		return this.vlVelocidadeTolerada;
	}
	public void setVlMedida(int vlMedida){
		this.vlMedida=vlMedida;
	}
	public int getVlMedida(){
		return this.vlMedida;
	}
	public void setVlConsiderada(int vlConsiderada){
		this.vlConsiderada=vlConsiderada;
	}
	public int getVlConsiderada(){
		return this.vlConsiderada;
	}
	public void setNrPlaca(String nrPlaca){
		this.nrPlaca=nrPlaca;
	}
	public String getNrPlaca(){
		return this.nrPlaca;
	}
	public void setLgTempoReal(int lgTempoReal){
		this.lgTempoReal=lgTempoReal;
	}
	public int getLgTempoReal(){
		return this.lgTempoReal;
	}
	public void setTpVeiculo(int tpVeiculo){
		this.tpVeiculo=tpVeiculo;
	}
	public int getTpVeiculo(){
		return this.tpVeiculo;
	}
	public void setVlComprimentoVeiculo(int vlComprimentoVeiculo){
		this.vlComprimentoVeiculo=vlComprimentoVeiculo;
	}
	public int getVlComprimentoVeiculo(){
		return this.vlComprimentoVeiculo;
	}
	public void setIdMedida(int idMedida){
		this.idMedida=idMedida;
	}
	public int getIdMedida(){
		return this.idMedida;
	}
	public void setNrSerial(String nrSerial){
		this.nrSerial=nrSerial;
	}
	public String getNrSerial(){
		return this.nrSerial;
	}
	public void setNmModeloEquipamento(String nmModeloEquipamento){
		this.nmModeloEquipamento=nmModeloEquipamento;
	}
	public String getNmModeloEquipamento(){
		return this.nmModeloEquipamento;
	}
	public void setNmRodovia(String nmRodovia){
		this.nmRodovia=nmRodovia;
	}
	public String getNmRodovia(){
		return this.nmRodovia;
	}
	public void setNmUfRodovia(String nmUfRodovia){
		this.nmUfRodovia=nmUfRodovia;
	}
	public String getNmUfRodovia(){
		return this.nmUfRodovia;
	}
	public void setNmKmRodovia(String nmKmRodovia){
		this.nmKmRodovia=nmKmRodovia;
	}
	public String getNmKmRodovia(){
		return this.nmKmRodovia;
	}
	public void setNmMetrosRodovia(String nmMetrosRodovia){
		this.nmMetrosRodovia=nmMetrosRodovia;
	}
	public String getNmMetrosRodovia(){
		return this.nmMetrosRodovia;
	}
	public void setNmSentidoRodovia(String nmSentidoRodovia){
		this.nmSentidoRodovia=nmSentidoRodovia;
	}
	public String getNmSentidoRodovia(){
		return this.nmSentidoRodovia;
	}
	public void setIdCidade(int idCidade){
		this.idCidade=idCidade;
	}
	public int getIdCidade(){
		return this.idCidade;
	}
	public void setNmMarcaEquipamento(String nmMarcaEquipamento){
		this.nmMarcaEquipamento=nmMarcaEquipamento;
	}
	public String getNmMarcaEquipamento(){
		return this.nmMarcaEquipamento;
	}
	public void setTpEquipamento(int tpEquipamento){
		this.tpEquipamento=tpEquipamento;
	}
	public int getTpEquipamento(){
		return this.tpEquipamento;
	}
	public void setLgValidaFuncionamento(int lgValidaFuncionamento){
		this.lgValidaFuncionamento=lgValidaFuncionamento;
	}
	public int getLgValidaFuncionamento(){
		return this.lgValidaFuncionamento;
	}
	public void setDtExecucaoJob(GregorianCalendar dtExecucaoJob){
		this.dtExecucaoJob=dtExecucaoJob;
	}
	public GregorianCalendar getDtExecucaoJob(){
		return this.dtExecucaoJob;
	}
	public void setIdUuid(String idUuid){
		this.idUuid=idUuid;
	}
	public String getIdUuid(){
		return this.idUuid;
	}
	public void setTpRestricao(int tpRestricao){
		this.tpRestricao=tpRestricao;
	}
	public int getTpRestricao(){
		return this.tpRestricao;
	}
	public void setTpClassificacao(int tpClassificacao){
		this.tpClassificacao=tpClassificacao;
	}
	public int getTpClassificacao(){
		return this.tpClassificacao;
	}
	public void setVlPermanencia(int vlPermanencia){
		this.vlPermanencia=vlPermanencia;
	}
	public int getVlPermanencia(){
		return this.vlPermanencia;
	}
	public void setVlSemaforoVermelho(int vlSemaforoVermelho){
		this.vlSemaforoVermelho=vlSemaforoVermelho;
	}
	public int getVlSemaforoVermelho(){
		return this.vlSemaforoVermelho;
	}
	public void setStEvento(int stEvento){
		this.stEvento=stEvento;
	}
	public int getStEvento(){
		return this.stEvento;
	}
	public void setCdMotivoCancelamento(int cdMotivoCancelamento){
		this.cdMotivoCancelamento=cdMotivoCancelamento;
	}
	public int getCdMotivoCancelamento(){
		return this.cdMotivoCancelamento;
	}
	public void setTxtEvento(String txtEvento){
		this.txtEvento=txtEvento;
	}
	public String getTxtEvento(){
		return this.txtEvento;
	}
	public void setLgOlpr(int lgOlpr){
		this.lgOlpr=lgOlpr;
	}
	public int getLgOlpr(){
		return this.lgOlpr;
	}
	public void setDtCancelamento(GregorianCalendar dtCancelamento){
		this.dtCancelamento=dtCancelamento;
	}
	public GregorianCalendar getDtCancelamento(){
		return this.dtCancelamento;
	}
	public void setCdUsuarioCancelamento(int cdUsuarioCancelamento){
		this.cdUsuarioCancelamento=cdUsuarioCancelamento;
	}
	public int getCdUsuarioCancelamento(){
		return this.cdUsuarioCancelamento;
	}
	public void setDtProcessamento(GregorianCalendar dtProcessamento){
		this.dtProcessamento=dtProcessamento;
	}
	public GregorianCalendar getDtProcessamento(){
		return this.dtProcessamento;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setLgEnviado(int lgEnviado){
		this.lgEnviado=lgEnviado;
	}
	public int getLgEnviado(){
		return this.lgEnviado;
	}
	public void setCdUsuarioConfirmacao(int cdUsuarioConfirmacao){
		this.cdUsuarioConfirmacao=cdUsuarioConfirmacao;
	}
	public int getCdUsuarioConfirmacao(){
		return this.cdUsuarioConfirmacao;
	}
	public void setVlTolerancia(int vlTolerancia){
		this.vlTolerancia=vlTolerancia;
	}
	public int getVlTolerancia(){
		return this.vlTolerancia;
	}
	
	public void confirmarEvento(ConfirmacaoEvento confirmacaoEvento) {
		this.cdUsuarioConfirmacao = confirmacaoEvento.getCdUsuarioConfirmacao();
		this.dtProcessamento = DateUtil.getDataAtual();
		this.nrPlaca = confirmacaoEvento.getNrPlaca() != null ? confirmacaoEvento.getNrPlaca() : this.nrPlaca;
		this.stEvento = EventoEquipamentoServices.ST_EVENTO_CONFIRMADO;
	}
	
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdEvento: " +  getCdEvento();
		valueToString += ", cdEquipamento: " +  getCdEquipamento();
		valueToString += ", cdTipoEvento: " +  getCdTipoEvento();
		valueToString += ", dtEvento: " +  sol.util.Util.formatDateTime(getDtEvento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nmOrgaoAutuador: " +  getNmOrgaoAutuador();
		valueToString += ", nmEquipamento: " +  getNmEquipamento();
		valueToString += ", dsLocal: " +  getDsLocal();
		valueToString += ", idIdentificacaoInmetro: " +  getIdIdentificacaoInmetro();
		valueToString += ", dtAfericao: " +  sol.util.Util.formatDateTime(getDtAfericao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrPista: " +  getNrPista();
		valueToString += ", dtConclusao: " +  sol.util.Util.formatDateTime(getDtConclusao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlLimite: " +  getVlLimite();
		valueToString += ", vlVelocidadeTolerada: " +  getVlVelocidadeTolerada();
		valueToString += ", vlMedida: " +  getVlMedida();
		valueToString += ", vlConsiderada: " +  getVlConsiderada();
		valueToString += ", nrPlaca: " +  getNrPlaca();
		valueToString += ", lgTempoReal: " +  getLgTempoReal();
		valueToString += ", tpVeiculo: " +  getTpVeiculo();
		valueToString += ", vlComprimentoVeiculo: " +  getVlComprimentoVeiculo();
		valueToString += ", idMedida: " +  getIdMedida();
		valueToString += ", nrSerial: " +  getNrSerial();
		valueToString += ", nmModeloEquipamento: " +  getNmModeloEquipamento();
		valueToString += ", nmRodovia: " +  getNmRodovia();
		valueToString += ", nmUfRodovia: " +  getNmUfRodovia();
		valueToString += ", nmKmRodovia: " +  getNmKmRodovia();
		valueToString += ", nmMetrosRodovia: " +  getNmMetrosRodovia();
		valueToString += ", nmSentidoRodovia: " +  getNmSentidoRodovia();
		valueToString += ", idCidade: " +  getIdCidade();
		valueToString += ", nmMarcaEquipamento: " +  getNmMarcaEquipamento();
		valueToString += ", tpEquipamento: " +  getTpEquipamento();
		valueToString += ", lgValidaFuncionamento: " +  getLgValidaFuncionamento();
		valueToString += ", dtExecucaoJob: " +  sol.util.Util.formatDateTime(getDtExecucaoJob(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idUuid: " +  getIdUuid();
		valueToString += ", tpRestricao: " +  getTpRestricao();
		valueToString += ", tpClassificacao: " +  getTpClassificacao();
		valueToString += ", vlPermanencia: " +  getVlPermanencia();
		valueToString += ", vlSemaforoVermelho: " +  getVlSemaforoVermelho();
		valueToString += ", stEvento: " +  getStEvento();
		valueToString += ", cdMotivoCancelamento: " +  getCdMotivoCancelamento();
		valueToString += ", txtEvento: " +  getTxtEvento();
		valueToString += ", lgOlpr: " +  getLgOlpr();
		valueToString += ", dtCancelamento: " +  sol.util.Util.formatDateTime(getDtCancelamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuarioCancelamento: " +  getCdUsuarioCancelamento();
		valueToString += ", dtProcessamento: " +  sol.util.Util.formatDateTime(getDtProcessamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", lgEnviado: " +  getLgEnviado();
		valueToString += ", cdUsuarioConfirmacao: " +  getCdUsuarioConfirmacao();
		valueToString += ", vlTolerancia: " +  getVlTolerancia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EventoEquipamento(getCdEvento(),
			getCdEquipamento(),
			getCdTipoEvento(),
			getDtEvento()==null ? null : (GregorianCalendar)getDtEvento().clone(),
			getNmOrgaoAutuador(),
			getNmEquipamento(),
			getDsLocal(),
			getIdIdentificacaoInmetro(),
			getDtAfericao()==null ? null : (GregorianCalendar)getDtAfericao().clone(),
			getNrPista(),
			getDtConclusao()==null ? null : (GregorianCalendar)getDtConclusao().clone(),
			getVlLimite(),
			getVlVelocidadeTolerada(),
			getVlMedida(),
			getVlConsiderada(),
			getNrPlaca(),
			getLgTempoReal(),
			getTpVeiculo(),
			getVlComprimentoVeiculo(),
			getIdMedida(),
			getNrSerial(),
			getNmModeloEquipamento(),
			getNmRodovia(),
			getNmUfRodovia(),
			getNmKmRodovia(),
			getNmMetrosRodovia(),
			getNmSentidoRodovia(),
			getIdCidade(),
			getNmMarcaEquipamento(),
			getTpEquipamento(),
			getLgValidaFuncionamento(),
			getDtExecucaoJob()==null ? null : (GregorianCalendar)getDtExecucaoJob().clone(),
			getIdUuid(),
			getTpRestricao(),
			getTpClassificacao(),
			getVlPermanencia(),
			getVlSemaforoVermelho(),
			getStEvento(),
			getCdMotivoCancelamento(),
			getTxtEvento(),
			getLgOlpr(),
			getDtCancelamento()==null ? null : (GregorianCalendar)getDtCancelamento().clone(),
			getCdUsuarioCancelamento(),
			getDtProcessamento()==null ? null : (GregorianCalendar)getDtProcessamento().clone(),
			getCdVeiculo(),
			getLgEnviado(),
			getCdUsuarioConfirmacao(),
			getVlTolerancia());
	}
}