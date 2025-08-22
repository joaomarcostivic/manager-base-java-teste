package com.tivic.manager.str;

import java.util.GregorianCalendar;

@Deprecated
public class AitMovimento {
	
	private int codigoAit;
	private int nrMovimento;
	private GregorianCalendar dtMovimento;
	private int nrRemessa;
	private int tpStatus;
	private int tpArquivo;
	private byte[] dsObservacao;
	private int codOcorrencia;
	private int lgEnviadoDetran;
	private int stEntrega;
	private String nrProcesso;
	private GregorianCalendar dtRegistroDetran;
	private int stRecurso;
	private int nrSequencial;
	private String nrErro;
	private GregorianCalendar dtDigitacao;
	private int lgCancelaMovimento;
	private GregorianCalendar dtCancelamento;
	private int nrRemessaRegistro;
	private GregorianCalendar dtPrimeiroRegistro;
	private int stRegistroDetran;
	private int stAvisoRecebimento;
	private GregorianCalendar dtAvisoRecebimento;
	private int cdProcesso;
	private int cdUsuario;
	private int cdContaReceber;

	public AitMovimento() { }

	public AitMovimento(int codigoAit,
			int nrMovimento,
			GregorianCalendar dtMovimento,
			int nrRemessa,
			int tpStatus,
			int tpArquivo,
			byte[] dsObservacao,
			int codOcorrencia,
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
			int stAvisoRecebimento,
			GregorianCalendar dtAvisoRecebimento,
			int cdProcesso,
			int cdUsuario,
			int cdContaReceber) {
		setCodigoAit(codigoAit);
		setNrMovimento(nrMovimento);
		setDtMovimento(dtMovimento);
		setNrRemessa(nrRemessa);
		setTpStatus(tpStatus);
		setTpArquivo(tpArquivo);
		setDsObservacao(dsObservacao);
		setCodOcorrencia(codOcorrencia);
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
		setStAvisoRecebimento(stAvisoRecebimento);
		setDtAvisoRecebimento(dtAvisoRecebimento);
		setCdProcesso(cdProcesso);
		setCdUsuario(cdUsuario);
		setCdContaReceber(cdContaReceber);
	}
	public void setCodigoAit(int codigoAit){
		this.codigoAit=codigoAit;
	}
	public int getCodigoAit(){
		return this.codigoAit;
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
	public void setDsObservacao(byte[] dsObservacao){
		this.dsObservacao=dsObservacao;
	}
	public byte[] getDsObservacao(){
		return this.dsObservacao;
	}
	public void setCodOcorrencia(int codOcorrencia){
		this.codOcorrencia=codOcorrencia;
	}
	public int getCodOcorrencia(){
		return this.codOcorrencia;
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
	public void setStAvisoRecebimento(int stAvisoRecebimento){
		this.stAvisoRecebimento=stAvisoRecebimento;
	}
	public int getStAvisoRecebimento(){
		return this.stAvisoRecebimento;
	}
	public void setDtAvisoRecebimento(GregorianCalendar dtAvisoRecebimento){
		this.dtAvisoRecebimento=dtAvisoRecebimento;
	}
	public GregorianCalendar getDtAvisoRecebimento(){
		return this.dtAvisoRecebimento;
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
	public String toString() {
		String valueToString = "";
		valueToString += "codigoAit: " +  getCodigoAit();
		valueToString += ", nrMovimento: " +  getNrMovimento();
		valueToString += ", dtMovimento: " +  sol.util.Util.formatDateTime(getDtMovimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrRemessa: " +  getNrRemessa();
		valueToString += ", tpStatus: " +  getTpStatus();
		valueToString += ", tpArquivo: " +  getTpArquivo();
		valueToString += ", dsObservacao: " +  getDsObservacao();
		valueToString += ", codOcorrencia: " +  getCodOcorrencia();
		valueToString += ", lgEnviadoDetran: " +  getLgEnviadoDetran();
		valueToString += ", stEntrega: " +  getStEntrega();
		valueToString += ", nrProcesso: " +  getNrProcesso();
		valueToString += ", dtRegistroDetran: " +  sol.util.Util.formatDateTime(getDtRegistroDetran(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stRecurso: " +  getStRecurso();
		valueToString += ", nrSequencial: " +  getNrSequencial();
		valueToString += ", nrErro: " +  getNrErro();
		valueToString += ", dtDigitacao: " +  sol.util.Util.formatDateTime(getDtDigitacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgCancelaMovimento: " +  getLgCancelaMovimento();
		valueToString += ", dtCancelamento: " +  sol.util.Util.formatDateTime(getDtCancelamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrRemessaRegistro: " +  getNrRemessaRegistro();
		valueToString += ", dtPrimeiroRegistro: " +  sol.util.Util.formatDateTime(getDtPrimeiroRegistro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stRegistroDetran: " +  getStRegistroDetran();
		valueToString += ", stAvisoRecebimento: " +  getStAvisoRecebimento();
		valueToString += ", dtAvisoRecebimento: " +  sol.util.Util.formatDateTime(getDtAvisoRecebimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitMovimento(getCodigoAit(),
			getNrMovimento(),
			getDtMovimento()==null ? null : (GregorianCalendar)getDtMovimento().clone(),
			getNrRemessa(),
			getTpStatus(),
			getTpArquivo(),
			getDsObservacao(),
			getCodOcorrencia(),
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
			getStAvisoRecebimento(),
			getDtAvisoRecebimento()==null ? null : (GregorianCalendar)getDtAvisoRecebimento().clone(),
			getCdProcesso(),
			getCdUsuario(),
			getCdContaReceber());
	}

}
