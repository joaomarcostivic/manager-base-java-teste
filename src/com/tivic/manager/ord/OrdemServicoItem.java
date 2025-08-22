package com.tivic.manager.ord;

import java.util.GregorianCalendar;

public class OrdemServicoItem {

	private int cdOrdemServicoItem;
	private int cdOrdemServico;
	private int cdContrato;
	private int cdProdutoServico;
	private int cdMarca;
	private int cdGarantia;
	private GregorianCalendar dtAdmissao;
	private GregorianCalendar dtInicio;
	private GregorianCalendar dtFechamento;
	private GregorianCalendar dtEntrega;
	private String txtDiagnostico;
	private String txtObservacao;
	private String nrControle;
	private int stOrdemServicoItem;
	private String nmItem;
	private String nmMarca;
	private String nmModelo;
	private String nrSerie;
	private int cdTipoProdutoServico;
	private int cdTecnicoResponsavel;
	private GregorianCalendar dtPrevisaoEntrega;
	private int cdUnidadeMedida;
	private int cdReferencia;

	public OrdemServicoItem(){ }

	public OrdemServicoItem(int cdOrdemServicoItem,
			int cdOrdemServico,
			int cdContrato,
			int cdProdutoServico,
			int cdMarca,
			int cdGarantia,
			GregorianCalendar dtAdmissao,
			GregorianCalendar dtInicio,
			GregorianCalendar dtFechamento,
			GregorianCalendar dtEntrega,
			String txtDiagnostico,
			String txtObservacao,
			String nrControle,
			int stOrdemServicoItem,
			String nmItem,
			String nmMarca,
			String nmModelo,
			String nrSerie,
			int cdTipoProdutoServico,
			int cdTecnicoResponsavel,
			GregorianCalendar dtPrevisaoEntrega,
			int cdUnidadeMedida,
			int cdReferencia){
		setCdOrdemServicoItem(cdOrdemServicoItem);
		setCdOrdemServico(cdOrdemServico);
		setCdContrato(cdContrato);
		setCdProdutoServico(cdProdutoServico);
		setCdMarca(cdMarca);
		setCdGarantia(cdGarantia);
		setDtAdmissao(dtAdmissao);
		setDtInicio(dtInicio);
		setDtFechamento(dtFechamento);
		setDtEntrega(dtEntrega);
		setTxtDiagnostico(txtDiagnostico);
		setTxtObservacao(txtObservacao);
		setNrControle(nrControle);
		setStOrdemServicoItem(stOrdemServicoItem);
		setNmItem(nmItem);
		setNmMarca(nmMarca);
		setNmModelo(nmModelo);
		setNrSerie(nrSerie);
		setCdTipoProdutoServico(cdTipoProdutoServico);
		setCdTecnicoResponsavel(cdTecnicoResponsavel);
		setDtPrevisaoEntrega(dtPrevisaoEntrega);
		setCdUnidadeMedida(cdUnidadeMedida);
		setCdReferencia(cdReferencia);
	}
	public void setCdOrdemServicoItem(int cdOrdemServicoItem){
		this.cdOrdemServicoItem=cdOrdemServicoItem;
	}
	public int getCdOrdemServicoItem(){
		return this.cdOrdemServicoItem;
	}
	public void setCdOrdemServico(int cdOrdemServico){
		this.cdOrdemServico=cdOrdemServico;
	}
	public int getCdOrdemServico(){
		return this.cdOrdemServico;
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public void setCdGarantia(int cdGarantia){
		this.cdGarantia=cdGarantia;
	}
	public int getCdGarantia(){
		return this.cdGarantia;
	}
	public void setDtAdmissao(GregorianCalendar dtAdmissao){
		this.dtAdmissao=dtAdmissao;
	}
	public GregorianCalendar getDtAdmissao(){
		return this.dtAdmissao;
	}
	public void setDtInicio(GregorianCalendar dtInicio){
		this.dtInicio=dtInicio;
	}
	public GregorianCalendar getDtInicio(){
		return this.dtInicio;
	}
	public void setDtFechamento(GregorianCalendar dtFechamento){
		this.dtFechamento=dtFechamento;
	}
	public GregorianCalendar getDtFechamento(){
		return this.dtFechamento;
	}
	public void setDtEntrega(GregorianCalendar dtEntrega){
		this.dtEntrega=dtEntrega;
	}
	public GregorianCalendar getDtEntrega(){
		return this.dtEntrega;
	}
	public void setTxtDiagnostico(String txtDiagnostico){
		this.txtDiagnostico=txtDiagnostico;
	}
	public String getTxtDiagnostico(){
		return this.txtDiagnostico;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setNrControle(String nrControle){
		this.nrControle=nrControle;
	}
	public String getNrControle(){
		return this.nrControle;
	}
	public void setStOrdemServicoItem(int stOrdemServicoItem){
		this.stOrdemServicoItem=stOrdemServicoItem;
	}
	public int getStOrdemServicoItem(){
		return this.stOrdemServicoItem;
	}
	public void setNmItem(String nmItem){
		this.nmItem=nmItem;
	}
	public String getNmItem(){
		return this.nmItem;
	}
	public void setNmMarca(String nmMarca){
		this.nmMarca=nmMarca;
	}
	public String getNmMarca(){
		return this.nmMarca;
	}
	public void setNmModelo(String nmModelo){
		this.nmModelo=nmModelo;
	}
	public String getNmModelo(){
		return this.nmModelo;
	}
	public void setNrSerie(String nrSerie){
		this.nrSerie=nrSerie;
	}
	public String getNrSerie(){
		return this.nrSerie;
	}
	public void setCdTipoProdutoServico(int cdTipoProdutoServico){
		this.cdTipoProdutoServico=cdTipoProdutoServico;
	}
	public int getCdTipoProdutoServico(){
		return this.cdTipoProdutoServico;
	}
	public void setCdTecnicoResponsavel(int cdTecnicoResponsavel){
		this.cdTecnicoResponsavel=cdTecnicoResponsavel;
	}
	public int getCdTecnicoResponsavel(){
		return this.cdTecnicoResponsavel;
	}
	public void setDtPrevisaoEntrega(GregorianCalendar dtPrevisaoEntrega){
		this.dtPrevisaoEntrega=dtPrevisaoEntrega;
	}
	public GregorianCalendar getDtPrevisaoEntrega(){
		return this.dtPrevisaoEntrega;
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrdemServicoItem: " +  getCdOrdemServicoItem();
		valueToString += ", cdOrdemServico: " +  getCdOrdemServico();
		valueToString += ", cdContrato: " +  getCdContrato();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdMarca: " +  getCdMarca();
		valueToString += ", cdGarantia: " +  getCdGarantia();
		valueToString += ", dtAdmissao: " +  sol.util.Util.formatDateTime(getDtAdmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtInicio: " +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFechamento: " +  sol.util.Util.formatDateTime(getDtFechamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtEntrega: " +  sol.util.Util.formatDateTime(getDtEntrega(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtDiagnostico: " +  getTxtDiagnostico();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", nrControle: " +  getNrControle();
		valueToString += ", stOrdemServicoItem: " +  getStOrdemServicoItem();
		valueToString += ", nmItem: " +  getNmItem();
		valueToString += ", nmMarca: " +  getNmMarca();
		valueToString += ", nmModelo: " +  getNmModelo();
		valueToString += ", nrSerie: " +  getNrSerie();
		valueToString += ", cdTipoProdutoServico: " +  getCdTipoProdutoServico();
		valueToString += ", cdTecnicoResponsavel: " +  getCdTecnicoResponsavel();
		valueToString += ", dtPrevisaoEntrega: " +  sol.util.Util.formatDateTime(getDtPrevisaoEntrega(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OrdemServicoItem(getCdOrdemServicoItem(),
			getCdOrdemServico(),
			getCdContrato(),
			getCdProdutoServico(),
			getCdMarca(),
			getCdGarantia(),
			getDtAdmissao()==null ? null : (GregorianCalendar)getDtAdmissao().clone(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone(),
			getDtFechamento()==null ? null : (GregorianCalendar)getDtFechamento().clone(),
			getDtEntrega()==null ? null : (GregorianCalendar)getDtEntrega().clone(),
			getTxtDiagnostico(),
			getTxtObservacao(),
			getNrControle(),
			getStOrdemServicoItem(),
			getNmItem(),
			getNmMarca(),
			getNmModelo(),
			getNrSerie(),
			getCdTipoProdutoServico(),
			getCdTecnicoResponsavel(),
			getDtPrevisaoEntrega()==null ? null : (GregorianCalendar)getDtPrevisaoEntrega().clone(),
			getCdUnidadeMedida(),
			getCdReferencia());
	}

}
