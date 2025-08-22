package com.tivic.manager.prc;

import java.util.GregorianCalendar;

public class ServicoRecorteOcorrencia {

	private int cdOcorrencia;
	private int cdServico;
	private GregorianCalendar dtOcorrencia;
	private int tpOcorrencia;
	private int idRespostaServico;
	private String dsRespostaServico;
	private String txtRespostaServico;
	private int cdProcesso;
	private int cdRecorte;

	public ServicoRecorteOcorrencia() { }

	public ServicoRecorteOcorrencia(int cdOcorrencia,
			int cdServico,
			GregorianCalendar dtOcorrencia,
			int tpOcorrencia,
			int idRespostaServico,
			String dsRespostaServico,
			String txtRespostaServico,
			int cdProcesso,
			int cdRecorte) {
		setCdOcorrencia(cdOcorrencia);
		setCdServico(cdServico);
		setDtOcorrencia(dtOcorrencia);
		setTpOcorrencia(tpOcorrencia);
		setIdRespostaServico(idRespostaServico);
		setDsRespostaServico(dsRespostaServico);
		setTxtRespostaServico(txtRespostaServico);
		setCdProcesso(cdProcesso);
		setCdRecorte(cdRecorte);
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public void setCdServico(int cdServico){
		this.cdServico=cdServico;
	}
	public int getCdServico(){
		return this.cdServico;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}
	public void setTpOcorrencia(int tpOcorrencia){
		this.tpOcorrencia=tpOcorrencia;
	}
	public int getTpOcorrencia(){
		return this.tpOcorrencia;
	}
	public void setIdRespostaServico(int idRespostaServico){
		this.idRespostaServico=idRespostaServico;
	}
	public int getIdRespostaServico(){
		return this.idRespostaServico;
	}
	public void setDsRespostaServico(String dsRespostaServico){
		this.dsRespostaServico=dsRespostaServico;
	}
	public String getDsRespostaServico(){
		return this.dsRespostaServico;
	}
	public void setTxtRespostaServico(String txtRespostaServico){
		this.txtRespostaServico=txtRespostaServico;
	}
	public String getTxtRespostaServico(){
		return this.txtRespostaServico;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdRecorte(int cdRecorte){
		this.cdRecorte=cdRecorte;
	}
	public int getCdRecorte(){
		return this.cdRecorte;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdServico: " +  getCdServico();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpOcorrencia: " +  getTpOcorrencia();
		valueToString += ", idRespostaServico: " +  getIdRespostaServico();
		valueToString += ", dsRespostaServico: " +  getDsRespostaServico();
		valueToString += ", txtRespostaServico: " +  getTxtRespostaServico();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", cdRecorte: " +  getCdRecorte();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ServicoRecorteOcorrencia(getCdOcorrencia(),
			getCdServico(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone(),
			getTpOcorrencia(),
			getIdRespostaServico(),
			getDsRespostaServico(),
			getTxtRespostaServico(),
			getCdProcesso(),
			getCdRecorte());
	}

}