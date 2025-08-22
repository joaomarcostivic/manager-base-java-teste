package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AitMovimento {

	private int cdMovimento;
	private int cdAit;
	private int nrMovimento;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtMovimento;
	private int nrRemessa;
	private int tpStatus;
	private int tpArquivo;
	private String dsObservacao;
	private int cdOcorrencia;
	private int lgEnviadoDetran;
	private int stEntrega;
	private String nrProcesso;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtRegistroDetran;
	private int stRecurso;
	private int nrSequencial;
	private String nrErro;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtDigitacao;
	private int lgCancelaMovimento;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCancelamento;
	private int nrRemessaRegistro;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPrimeiroRegistro;
	private int stRegistroDetran;
	
	private int cdProcesso;
	private int cdUsuario;
	private int cdContaReceber;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPublicacaoDo;
	private int stPublicacaoDo;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtAdesaoSne;
	private int stAdesaoSne;
	private int cdMovimentoCancelamento;

	public AitMovimento() { }

	public AitMovimento(int cdMovimento,
			int cdAit,
			int nrMovimento,
			GregorianCalendar dtMovimento,
			int nrRemessa,
			int tpStatus,
			int tpArquivo,
			String dsObservacao,
			int cdOcorrencia,
			int lgEnviadoDetran,
			int stEntrega,
			String nrProcesso,
			GregorianCalendar dtRegistroDetran,
			int stRecurso,
			int nrSequencial,
			String nrErro,
			GregorianCalendar dtDigitacao,
			int lgCancelaMovimento,
			GregorianCalendar dtCancelamento,
			int nrRemessaRegistro,
			GregorianCalendar dtPrimeiroRegistro,
			int stRegistroDetran,
			int cdProcesso,
			int cdUsuario,
			int cdContaReceber,
			GregorianCalendar dtPublicacaoDo,
			int stPublicacaoDo,
			GregorianCalendar dtAdesaoSne,
			int stAdesaoSne,
			int cdMovimentoCancelamento) {
		setCdMovimento(cdMovimento);
		setCdAit(cdAit);
		setNrMovimento(nrMovimento);
		setDtMovimento(dtMovimento);
		setNrRemessa(nrRemessa);
		setTpStatus(tpStatus);
		setTpArquivo(tpArquivo);
		setDsObservacao(dsObservacao);
		setCdOcorrencia(cdOcorrencia);
		setLgEnviadoDetran(lgEnviadoDetran);
		setStEntrega(stEntrega);
		setNrProcesso(nrProcesso);
		setDtRegistroDetran(dtRegistroDetran);
		setStRecurso(stRecurso);
		setNrSequencial(nrSequencial);
		setNrErro(nrErro);
		setDtDigitacao(dtDigitacao);
		setLgCancelaMovimento(lgCancelaMovimento);
		setDtCancelamento(dtCancelamento);
		setNrRemessaRegistro(nrRemessaRegistro);
		setDtPrimeiroRegistro(dtPrimeiroRegistro);
		setStRegistroDetran(stRegistroDetran);
		setCdProcesso(cdProcesso);
		setCdUsuario(cdUsuario);
		setCdContaReceber(cdContaReceber);
		setDtPublicacaoDo(dtPublicacaoDo);
		setStPublicacaoDo(stPublicacaoDo);
		setDtAdesaoSne(dtAdesaoSne);
		setStAdesaoSne(stAdesaoSne);
		setCdMovimentoCancelamento(cdMovimentoCancelamento);
	}
	public void setCdMovimento(int cdMovimento){
		this.cdMovimento=cdMovimento;
	}
	public int getCdMovimento(){
		return this.cdMovimento;
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setNrMovimento(int nrMovimento){
		this.nrMovimento=nrMovimento;
	}
	public int getNrMovimento(){
		return this.nrMovimento;
	}
	public void setDtMovimento(GregorianCalendar dtMovimento){
		this.dtMovimento=dtMovimento;
	}
	public GregorianCalendar getDtMovimento(){
		return this.dtMovimento;
	}
	public void setNrRemessa(int nrRemessa){
		this.nrRemessa=nrRemessa;
	}
	public int getNrRemessa(){
		return this.nrRemessa;
	}
	public void setTpStatus(int tpStatus){
		this.tpStatus=tpStatus;
	}
	public int getTpStatus(){
		return this.tpStatus;
	}
	public void setTpArquivo(int tpArquivo){
		this.tpArquivo=tpArquivo;
	}
	public int getTpArquivo(){
		return this.tpArquivo;
	}
	public void setDsObservacao(String dsObservacao){
		this.dsObservacao=dsObservacao;
	}
	public String getDsObservacao(){
		return this.dsObservacao;
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public void setLgEnviadoDetran(int lgEnviadoDetran){
		this.lgEnviadoDetran=lgEnviadoDetran;
	}
	public int getLgEnviadoDetran(){
		return this.lgEnviadoDetran;
	}
	public void setStEntrega(int stEntrega){
		this.stEntrega=stEntrega;
	}
	public int getStEntrega(){
		return this.stEntrega;
	}
	public void setNrProcesso(String nrProcesso){
		this.nrProcesso=nrProcesso;
	}
	public String getNrProcesso(){
		return this.nrProcesso;
	}
	public void setDtRegistroDetran(GregorianCalendar dtRegistroDetran){
		this.dtRegistroDetran=dtRegistroDetran;
	}
	public GregorianCalendar getDtRegistroDetran(){
		return this.dtRegistroDetran;
	}
	public void setStRecurso(int stRecurso){
		this.stRecurso=stRecurso;
	}
	public int getStRecurso(){
		return this.stRecurso;
	}
	public void setNrSequencial(int nrSequencial){
		this.nrSequencial=nrSequencial;
	}
	public int getNrSequencial(){
		return this.nrSequencial;
	}
	public void setNrErro(String nrErro){
		this.nrErro=nrErro;
	}
	public String getNrErro(){
		return this.nrErro;
	}
	public void setDtDigitacao(GregorianCalendar dtDigitacao){
		this.dtDigitacao=dtDigitacao;
	}
	public GregorianCalendar getDtDigitacao(){
		return this.dtDigitacao;
	}
	public void setLgCancelaMovimento(int lgCancelaMovimento){
		this.lgCancelaMovimento=lgCancelaMovimento;
	}
	public int getLgCancelaMovimento(){
		return this.lgCancelaMovimento;
	}
	public void setDtCancelamento(GregorianCalendar dtCancelamento){
		this.dtCancelamento=dtCancelamento;
	}
	public GregorianCalendar getDtCancelamento(){
		return this.dtCancelamento;
	}
	public void setNrRemessaRegistro(int nrRemessaRegistro){
		this.nrRemessaRegistro=nrRemessaRegistro;
	}
	public int getNrRemessaRegistro(){
		return this.nrRemessaRegistro;
	}
	public void setDtPrimeiroRegistro(GregorianCalendar dtPrimeiroRegistro){
		this.dtPrimeiroRegistro=dtPrimeiroRegistro;
	}
	public GregorianCalendar getDtPrimeiroRegistro(){
		return this.dtPrimeiroRegistro;
	}
	public void setStRegistroDetran(int stRegistroDetran){
		this.stRegistroDetran=stRegistroDetran;
	}
	public int getStRegistroDetran(){
		return this.stRegistroDetran;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public void setDtPublicacaoDo(GregorianCalendar dtPublicacaoDo){
		this.dtPublicacaoDo=dtPublicacaoDo;
	}
	public GregorianCalendar getDtPublicacaoDo(){
		return this.dtPublicacaoDo;
	}
	public void setStPublicacaoDo(int stPublicacaoDo){
		this.stPublicacaoDo=stPublicacaoDo;
	}
	public int getStPublicacaoDo(){
		return this.stPublicacaoDo;
	}
	public GregorianCalendar getDtAdesaoSne() {
		return dtAdesaoSne;
	}
	public void setDtAdesaoSne(GregorianCalendar dtAdesaoSne) {
		this.dtAdesaoSne = dtAdesaoSne;
	}

	public int getStAdesaoSne() {
		return stAdesaoSne;
	}
	public void setStAdesaoSne(int stAdesaoSne) {
		this.stAdesaoSne = stAdesaoSne;
	}
	
	public int getCdMovimentoCancelamento() {
		return this.cdMovimentoCancelamento;
	}

	public void setCdMovimentoCancelamento(int cdMovimentoCancelamento) {
		this.cdMovimentoCancelamento = cdMovimentoCancelamento;
	}
	
	@Override
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } 
        catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
	}
	public Object clone() {
		return new AitMovimento(getCdMovimento(),
			getCdAit(),
			getNrMovimento(),
			getDtMovimento()==null ? null : (GregorianCalendar)getDtMovimento().clone(),
			getNrRemessa(),
			getTpStatus(),
			getTpArquivo(),
			getDsObservacao(),
			getCdOcorrencia(),
			getLgEnviadoDetran(),
			getStEntrega(),
			getNrProcesso(),
			getDtRegistroDetran()==null ? null : (GregorianCalendar)getDtRegistroDetran().clone(),
			getStRecurso(),
			getNrSequencial(),
			getNrErro(),
			getDtDigitacao()==null ? null : (GregorianCalendar)getDtDigitacao().clone(),
			getLgCancelaMovimento(),
			getDtCancelamento()==null ? null : (GregorianCalendar)getDtCancelamento().clone(),
			getNrRemessaRegistro(),
			getDtPrimeiroRegistro()==null ? null : (GregorianCalendar)getDtPrimeiroRegistro().clone(),
			getStRegistroDetran(),
			getCdProcesso(),
			getCdUsuario(),
			getCdContaReceber(),
			getDtPublicacaoDo()==null ? null : (GregorianCalendar)getDtPublicacaoDo().clone(),
			getStPublicacaoDo(),
			getDtAdesaoSne()==null ? null : (GregorianCalendar)getDtAdesaoSne().clone(),
			getStAdesaoSne(),
			getCdMovimentoCancelamento());
	}
}