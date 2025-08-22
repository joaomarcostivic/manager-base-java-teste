package com.tivic.manager.ptc;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class Documento {

	private int cdDocumento;
	private int cdArquivo;
	private int cdSetor;
	private int cdUsuario;
	private String nmLocalOrigem;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtProtocolo;
	
	private int tpDocumento;
	private String txtObservacao;
	private String idDocumento;
	private String nrDocumento;
	private int cdTipoDocumento;
	private int cdFase;
	private int cdSetorAtual;
	private int cdSituacaoDocumento;
	private int cdServico;
	private int cdAtendimento;
	private String txtDocumento;
	private int cdEmpresa;
	private int cdProcesso;
	private int tpPrioridade;
	private int cdDocumentoSuperior;
	private String dsAssunto;
	private String nrAtendimento;
	private int lgNotificacao;
	private int cdTipoDocumentoAnterior;
	private String cdDocumentoExterno;
	private String nrAssunto;
	private String nrDocumentoExterno;
	private String nrProtocoloExterno;
	private String nrAnoExterno;
	private int tpDocumentoExterno;
	private int tpInternoExterno;
	private String nmRequerente;

	public Documento() { }

	public Documento(int cdDocumento,
		int cdArquivo,
		int cdSetor,
		int cdUsuario,
		String nmLocalOrigem,
		GregorianCalendar dtProtocolo,
		int tpDocumento,
		String txtObservacao,
		String idDocumento,
		String nrDocumento,
		int cdTipoDocumento,
		int cdServico,
		int cdAtendimento,
		String txtDocumento,
		int cdSetorAtual,
		int cdSituacaoDocumento,
		int cdFase,
		int cdEmpresa,
		int cdProcesso,
		int tpPrioridade,
		int cdDocumentoSuperior,
		String dsAssunto,
		String nrAtendimento,
		int lgNotificacao,
		int cdTipoDocumentoAnterior,
		String nrDocumentoExterno,
		String nrAssunto,
		String nrProtocoloExterno,
		String nrAnoExterno,
		int tpDocumentoExterno,
		int tpInternoExterno){
			setCdDocumento(cdDocumento);
			setCdArquivo(cdArquivo);
			setCdSetor(cdSetor);
			setCdUsuario(cdUsuario);
			setNmLocalOrigem(nmLocalOrigem);
			setDtProtocolo(dtProtocolo);
			setTpDocumento(tpDocumento);
			setTxtObservacao(txtObservacao);
			setIdDocumento(idDocumento);
			setNrDocumento(nrDocumento);
			setCdTipoDocumento(cdTipoDocumento);
			setCdFase(cdFase);
			setCdSetorAtual(cdSetorAtual);
			setCdSituacaoDocumento(cdSituacaoDocumento);
			setCdServico(cdServico);
			setCdAtendimento(cdAtendimento);
			setTxtDocumento(txtDocumento);
			setCdEmpresa(cdEmpresa);
			setCdProcesso(cdProcesso);
			setTpPrioridade(tpPrioridade);
			setCdDocumentoSuperior(cdDocumentoSuperior);
			setDsAssunto(dsAssunto);
			setNrAtendimento(nrAtendimento);
			setLgNotificacao(lgNotificacao);
			setCdTipoDocumentoAnterior(cdTipoDocumentoAnterior);
			setNrAssunto(nrAssunto);
			setNrDocumentoExterno(nrDocumentoExterno);
			setNrProtocoloExterno(nrProtocoloExterno);
			setNrAnoExterno(nrAnoExterno);
			setTpDocumentoExterno(tpDocumentoExterno);
			setTpInternoExterno(tpInternoExterno);
		
	}
	
	public Documento(int cdDocumento,
			int cdArquivo,
			int cdSetor,
			int cdUsuario,
			String nmLocalOrigem,
			GregorianCalendar dtProtocolo,
			int tpDocumento,
			String txtObservacao,
			String idDocumento,
			String nrDocumento,
			int cdTipoDocumento,
			int cdFase,
			int cdSetorAtual,
			int cdSituacaoDocumento,
			int cdServico,
			int cdAtendimento,
			String txtDocumento,
			int cdEmpresa,
			int cdProcesso,
			int tpPrioridade,
			int cdDocumentoSuperior,
			String dsAssunto,
			String nrAtendimento,
			int lgNotificacao,
			int cdTipoDocumentoAnterior,
			String cdDocumentoExterno,
			String nrAssunto,
			String nrDocumentoExterno,
			String nrProtocoloExterno,
			String nrAnoExterno,
			int tpDocumentoExterno,
			int tpInternoExterno,
			String nmRequerente) {
		setCdDocumento(cdDocumento);
		setCdArquivo(cdArquivo);
		setCdSetor(cdSetor);
		setCdUsuario(cdUsuario);
		setNmLocalOrigem(nmLocalOrigem);
		setDtProtocolo(dtProtocolo);
		setTpDocumento(tpDocumento);
		setTxtObservacao(txtObservacao);
		setIdDocumento(idDocumento);
		setNrDocumento(nrDocumento);
		setCdTipoDocumento(cdTipoDocumento);
		setCdFase(cdFase);
		setCdSetorAtual(cdSetorAtual);
		setCdSituacaoDocumento(cdSituacaoDocumento);
		setCdServico(cdServico);
		setCdAtendimento(cdAtendimento);
		setTxtDocumento(txtDocumento);
		setCdEmpresa(cdEmpresa);
		setCdProcesso(cdProcesso);
		setTpPrioridade(tpPrioridade);
		setCdDocumentoSuperior(cdDocumentoSuperior);
		setDsAssunto(dsAssunto);
		setNrAtendimento(nrAtendimento);
		setLgNotificacao(lgNotificacao);
		setCdTipoDocumentoAnterior(cdTipoDocumentoAnterior);
		setCdDocumentoExterno(cdDocumentoExterno);
		setNrAssunto(nrAssunto);
		setNrDocumentoExterno(nrDocumentoExterno);
		setNrProtocoloExterno(nrProtocoloExterno);
		setNrAnoExterno(nrAnoExterno);
		setTpDocumentoExterno(tpDocumentoExterno);
		setTpInternoExterno(tpInternoExterno);
		setNmRequerente(nmRequerente);
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setNmLocalOrigem(String nmLocalOrigem){
		this.nmLocalOrigem=nmLocalOrigem;
	}
	public String getNmLocalOrigem(){
		return this.nmLocalOrigem;
	}
	public void setDtProtocolo(GregorianCalendar dtProtocolo){
		this.dtProtocolo=dtProtocolo;
	}
	public GregorianCalendar getDtProtocolo(){
		return this.dtProtocolo;
	}
	public void setTpDocumento(int tpDocumento){
		this.tpDocumento=tpDocumento;
	}
	public int getTpDocumento(){
		return this.tpDocumento;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setIdDocumento(String idDocumento){
		this.idDocumento=idDocumento;
	}
	public String getIdDocumento(){
		return this.idDocumento;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setCdFase(int cdFase){
		this.cdFase=cdFase;
	}
	public int getCdFase(){
		return this.cdFase;
	}
	public void setCdSetorAtual(int cdSetorAtual){
		this.cdSetorAtual=cdSetorAtual;
	}
	public int getCdSetorAtual(){
		return this.cdSetorAtual;
	}
	public void setCdSituacaoDocumento(int cdSituacaoDocumento){
		this.cdSituacaoDocumento=cdSituacaoDocumento;
	}
	public int getCdSituacaoDocumento(){
		return this.cdSituacaoDocumento;
	}
	public void setCdServico(int cdServico){
		this.cdServico=cdServico;
	}
	public int getCdServico(){
		return this.cdServico;
	}
	public void setCdAtendimento(int cdAtendimento){
		this.cdAtendimento=cdAtendimento;
	}
	public int getCdAtendimento(){
		return this.cdAtendimento;
	}
	public void setTxtDocumento(String txtDocumento){
		this.txtDocumento=txtDocumento;
	}
	public String getTxtDocumento(){
		return this.txtDocumento;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setTpPrioridade(int tpPrioridade){
		this.tpPrioridade=tpPrioridade;
	}
	public int getTpPrioridade(){
		return this.tpPrioridade;
	}
	public void setCdDocumentoSuperior(int cdDocumentoSuperior){
		this.cdDocumentoSuperior=cdDocumentoSuperior;
	}
	public int getCdDocumentoSuperior(){
		return this.cdDocumentoSuperior;
	}
	public void setDsAssunto(String dsAssunto){
		this.dsAssunto=dsAssunto;
	}
	public String getDsAssunto(){
		return this.dsAssunto;
	}
	public void setNrAtendimento(String nrAtendimento){
		this.nrAtendimento=nrAtendimento;
	}
	public String getNrAtendimento(){
		return this.nrAtendimento;
	}
	public void setLgNotificacao(int lgNotificacao){
		this.lgNotificacao=lgNotificacao;
	}
	public int getLgNotificacao(){
		return this.lgNotificacao;
	}
	public void setCdTipoDocumentoAnterior(int cdTipoDocumentoAnterior){
		this.cdTipoDocumentoAnterior=cdTipoDocumentoAnterior;
	}
	public int getCdTipoDocumentoAnterior(){
		return this.cdTipoDocumentoAnterior;
	}
	public void setCdDocumentoExterno(String cdDocumentoExterno){
		this.cdDocumentoExterno=cdDocumentoExterno;
	}
	public String getCdDocumentoExterno(){
		return this.cdDocumentoExterno;
	}
	public void setNrAssunto(String nrAssunto){
		this.nrAssunto=nrAssunto;
	}
	public String getNrAssunto(){
		return this.nrAssunto;
	}
	public void setNrDocumentoExterno(String nrDocumentoExterno){
		this.nrDocumentoExterno=nrDocumentoExterno;
	}
	public String getNrDocumentoExterno(){
		return this.nrDocumentoExterno;
	}
	public void setNrProtocoloExterno(String nrProtocoloExterno){
		this.nrProtocoloExterno=nrProtocoloExterno;
	}
	public String getNrProtocoloExterno(){
		return this.nrProtocoloExterno;
	}
	public void setNrAnoExterno(String nrAnoExterno){
		this.nrAnoExterno=nrAnoExterno;
	}
	public String getNrAnoExterno(){
		return this.nrAnoExterno;
	}
	public void setTpDocumentoExterno(int tpDocumentoExterno){
		this.tpDocumentoExterno=tpDocumentoExterno;
	}
	public int getTpDocumentoExterno(){
		return this.tpDocumentoExterno;
	}
	public void setTpInternoExterno(int tpInternoExterno){
		this.tpInternoExterno=tpInternoExterno;
	}
	public int getTpInternoExterno(){
		return this.tpInternoExterno;
	}
	public void setNmRequerente(String nmRequerente){
		this.nmRequerente=nmRequerente;
	}
	public String getNmRequerente(){
		return this.nmRequerente;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumento: " +  getCdDocumento();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", nmLocalOrigem: " +  getNmLocalOrigem();
		valueToString += ", dtProtocolo: " +  sol.util.Util.formatDateTime(getDtProtocolo(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpDocumento: " +  getTpDocumento();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", idDocumento: " +  getIdDocumento();
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", cdFase: " +  getCdFase();
		valueToString += ", cdSetorAtual: " +  getCdSetorAtual();
		valueToString += ", cdSituacaoDocumento: " +  getCdSituacaoDocumento();
		valueToString += ", cdServico: " +  getCdServico();
		valueToString += ", cdAtendimento: " +  getCdAtendimento();
		valueToString += ", txtDocumento: " +  getTxtDocumento();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", tpPrioridade: " +  getTpPrioridade();
		valueToString += ", cdDocumentoSuperior: " +  getCdDocumentoSuperior();
		valueToString += ", dsAssunto: " +  getDsAssunto();
		valueToString += ", nrAtendimento: " +  getNrAtendimento();
		valueToString += ", lgNotificacao: " +  getLgNotificacao();
		valueToString += ", cdTipoDocumentoAnterior: " +  getCdTipoDocumentoAnterior();
		valueToString += ", cdDocumentoExterno: " +  getCdDocumentoExterno();
		valueToString += ", nrAssunto: " +  getNrAssunto();
		valueToString += ", nrDocumentoExterno: " +  getNrDocumentoExterno();
		valueToString += ", nrProtocoloExterno: " +  getNrProtocoloExterno();
		valueToString += ", nrAnoExterno: " +  getNrAnoExterno();
		valueToString += ", tpDocumentoExterno: " +  getTpDocumentoExterno();
		valueToString += ", tpInternoExterno: " +  getTpInternoExterno();
		valueToString += ", nmRequerente: " +  getNmRequerente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Documento(getCdDocumento(),
			getCdArquivo(),
			getCdSetor(),
			getCdUsuario(),
			getNmLocalOrigem(),
			getDtProtocolo()==null ? null : (GregorianCalendar)getDtProtocolo().clone(),
			getTpDocumento(),
			getTxtObservacao(),
			getIdDocumento(),
			getNrDocumento(),
			getCdTipoDocumento(),
			getCdFase(),
			getCdSetorAtual(),
			getCdSituacaoDocumento(),
			getCdServico(),
			getCdAtendimento(),
			getTxtDocumento(),
			getCdEmpresa(),
			getCdProcesso(),
			getTpPrioridade(),
			getCdDocumentoSuperior(),
			getDsAssunto(),
			getNrAtendimento(),
			getLgNotificacao(),
			getCdTipoDocumentoAnterior(),
			getCdDocumentoExterno(),
			getNrAssunto(),
			getNrDocumentoExterno(),
			getNrProtocoloExterno(),
			getNrAnoExterno(),
			getTpDocumentoExterno(),
			getTpInternoExterno(),
			getNmRequerente());
	}
}
