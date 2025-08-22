package com.tivic.manager.prc;

import java.util.GregorianCalendar;

public class ProcessoArquivo {

	private int cdArquivo;
	private int cdProcesso;
	private int cdAndamento;
	private String nmArquivo;
	private String nmDocumento;
	private GregorianCalendar dtArquivamento;
	private byte[] blbArquivo;
	private int lgComprimido;
	private int stArquivo;
	private int cdAgendaItem;
	private GregorianCalendar dtBackup;
	private String idRepositorio;
	private int cdAssinatura;
	private String txtOcr;
	private int cdTipoDocumento;

	public ProcessoArquivo() { }
	
	public ProcessoArquivo(int cdArquivo,
			int cdProcesso,
			int cdAndamento,
			String nmArquivo,
			String nmDocumento,
			GregorianCalendar dtArquivamento,
			byte[] blbArquivo,
			int lgComprimido,
			int cdTipoDocumento,
			String idRepositorio){
		setCdArquivo(cdArquivo);
		setCdProcesso(cdProcesso);
		setCdAndamento(cdAndamento);
		setNmArquivo(nmArquivo);
		setNmDocumento(nmDocumento);
		setDtArquivamento(dtArquivamento);
		setBlbArquivo(blbArquivo);
		setLgComprimido(lgComprimido);
		setCdTipoDocumento(cdTipoDocumento);
		setIdRepositorio(idRepositorio);
	}

	public ProcessoArquivo(int cdArquivo,
			int cdProcesso,
			int cdAndamento,
			String nmArquivo,
			String nmDocumento,
			GregorianCalendar dtArquivamento,
			byte[] blbArquivo,
			int lgComprimido,
			int stArquivo,
			int cdAgendaItem,
			GregorianCalendar dtBackup,
			String idRepositorio,
			int cdAssinatura,
			String txtOcr,
			int cdTipoDocumento) {
		setCdArquivo(cdArquivo);
		setCdProcesso(cdProcesso);
		setCdAndamento(cdAndamento);
		setNmArquivo(nmArquivo);
		setNmDocumento(nmDocumento);
		setDtArquivamento(dtArquivamento);
		setBlbArquivo(blbArquivo);
		setLgComprimido(lgComprimido);
		setStArquivo(stArquivo);
		setCdAgendaItem(cdAgendaItem);
		setDtBackup(dtBackup);
		setIdRepositorio(idRepositorio);
		setCdAssinatura(cdAssinatura);
		setTxtOcr(txtOcr);
		setCdTipoDocumento(cdTipoDocumento);
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdAndamento(int cdAndamento){
		this.cdAndamento=cdAndamento;
	}
	public int getCdAndamento(){
		return this.cdAndamento;
	}
	public void setNmArquivo(String nmArquivo){
		this.nmArquivo=nmArquivo;
	}
	public String getNmArquivo(){
		return this.nmArquivo;
	}
	public void setNmDocumento(String nmDocumento){
		this.nmDocumento=nmDocumento;
	}
	public String getNmDocumento(){
		return this.nmDocumento;
	}
	public void setDtArquivamento(GregorianCalendar dtArquivamento){
		this.dtArquivamento=dtArquivamento;
	}
	public GregorianCalendar getDtArquivamento(){
		return this.dtArquivamento;
	}
	public void setBlbArquivo(byte[] blbArquivo){
		this.blbArquivo=blbArquivo;
	}
	public byte[] getBlbArquivo(){
		return this.blbArquivo;
	}
	public void setLgComprimido(int lgComprimido){
		this.lgComprimido=lgComprimido;
	}
	public int getLgComprimido(){
		return this.lgComprimido;
	}
	public void setStArquivo(int stArquivo){
		this.stArquivo=stArquivo;
	}
	public int getStArquivo(){
		return this.stArquivo;
	}
	public void setCdAgendaItem(int cdAgendaItem){
		this.cdAgendaItem=cdAgendaItem;
	}
	public int getCdAgendaItem(){
		return this.cdAgendaItem;
	}
	public void setDtBackup(GregorianCalendar dtBackup){
		this.dtBackup=dtBackup;
	}
	public GregorianCalendar getDtBackup(){
		return this.dtBackup;
	}
	public void setIdRepositorio(String idRepositorio){
		this.idRepositorio=idRepositorio;
	}
	public String getIdRepositorio(){
		return this.idRepositorio;
	}
	public void setCdAssinatura(int cdAssinatura){
		this.cdAssinatura=cdAssinatura;
	}
	public int getCdAssinatura(){
		return this.cdAssinatura;
	}
	public void setTxtOcr(String txtOcr){
		this.txtOcr=txtOcr;
	}
	public String getTxtOcr(){
		return this.txtOcr;
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdArquivo: " +  getCdArquivo();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", cdAndamento: " +  getCdAndamento();
		valueToString += ", nmArquivo: " +  getNmArquivo();
		valueToString += ", nmDocumento: " +  getNmDocumento();
		valueToString += ", dtArquivamento: " +  sol.util.Util.formatDateTime(getDtArquivamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", blbArquivo: " +  getBlbArquivo();
		valueToString += ", lgComprimido: " +  getLgComprimido();
		valueToString += ", stArquivo: " +  getStArquivo();
		valueToString += ", cdAgendaItem: " +  getCdAgendaItem();
		valueToString += ", dtBackup: " +  sol.util.Util.formatDateTime(getDtBackup(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idRepositorio: " +  getIdRepositorio();
		valueToString += ", cdAssinatura: " +  getCdAssinatura();
		valueToString += ", txtOcr: " +  getTxtOcr();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProcessoArquivo(getCdArquivo(),
			getCdProcesso(),
			getCdAndamento(),
			getNmArquivo(),
			getNmDocumento(),
			getDtArquivamento()==null ? null : (GregorianCalendar)getDtArquivamento().clone(),
			getBlbArquivo(),
			getLgComprimido(),
			getStArquivo(),
			getCdAgendaItem(),
			getDtBackup()==null ? null : (GregorianCalendar)getDtBackup().clone(),
			getIdRepositorio(),
			getCdAssinatura(),
			getTxtOcr(),
			getCdTipoDocumento());
	}

}