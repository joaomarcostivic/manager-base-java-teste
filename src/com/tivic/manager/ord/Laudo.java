package com.tivic.manager.ord;

import java.util.GregorianCalendar;

public class Laudo {

	private int cdLaudo;
	private int cdTipoLaudo;
	private int cdOrdemServico;
	private int cdOrdemServicoItem;
	private String nmLaudo;
	private String txtLaudo;
	private GregorianCalendar dtLaudo;
	private int cdDocumento;

	public Laudo(){ }

	public Laudo(int cdLaudo,
			int cdTipoLaudo,
			int cdOrdemServico,
			int cdOrdemServicoItem,
			String nmLaudo,
			String txtLaudo,
			GregorianCalendar dtLaudo,
			int cdDocumento){
		setCdLaudo(cdLaudo);
		setCdTipoLaudo(cdTipoLaudo);
		setCdOrdemServico(cdOrdemServico);
		setCdOrdemServicoItem(cdOrdemServicoItem);
		setNmLaudo(nmLaudo);
		setTxtLaudo(txtLaudo);
		setDtLaudo(dtLaudo);
		setCdDocumento(cdDocumento);
	}
	public void setCdLaudo(int cdLaudo){
		this.cdLaudo=cdLaudo;
	}
	public int getCdLaudo(){
		return this.cdLaudo;
	}
	public void setCdTipoLaudo(int cdTipoLaudo){
		this.cdTipoLaudo=cdTipoLaudo;
	}
	public int getCdTipoLaudo(){
		return this.cdTipoLaudo;
	}
	public void setCdOrdemServico(int cdOrdemServico){
		this.cdOrdemServico=cdOrdemServico;
	}
	public int getCdOrdemServico(){
		return this.cdOrdemServico;
	}
	public void setCdOrdemServicoItem(int cdOrdemServicoItem){
		this.cdOrdemServicoItem=cdOrdemServicoItem;
	}
	public int getCdOrdemServicoItem(){
		return this.cdOrdemServicoItem;
	}
	public void setNmLaudo(String nmLaudo){
		this.nmLaudo=nmLaudo;
	}
	public String getNmLaudo(){
		return this.nmLaudo;
	}
	public void setTxtLaudo(String txtLaudo){
		this.txtLaudo=txtLaudo;
	}
	public String getTxtLaudo(){
		return this.txtLaudo;
	}
	public void setDtLaudo(GregorianCalendar dtLaudo){
		this.dtLaudo=dtLaudo;
	}
	public GregorianCalendar getDtLaudo(){
		return this.dtLaudo;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLaudo: " +  getCdLaudo();
		valueToString += ", cdTipoLaudo: " +  getCdTipoLaudo();
		valueToString += ", cdOrdemServico: " +  getCdOrdemServico();
		valueToString += ", cdOrdemServicoItem: " +  getCdOrdemServicoItem();
		valueToString += ", nmLaudo: " +  getNmLaudo();
		valueToString += ", txtLaudo: " +  getTxtLaudo();
		valueToString += ", dtLaudo: " +  sol.util.Util.formatDateTime(getDtLaudo(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdDocumento: " +  getCdDocumento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Laudo(getCdLaudo(),
			getCdTipoLaudo(),
			getCdOrdemServico(),
			getCdOrdemServicoItem(),
			getNmLaudo(),
			getTxtLaudo(),
			getDtLaudo()==null ? null : (GregorianCalendar)getDtLaudo().clone(),
			getCdDocumento());
	}

}
