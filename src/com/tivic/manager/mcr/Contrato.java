package com.tivic.manager.mcr;

import java.util.GregorianCalendar;

public class Contrato extends com.tivic.manager.adm.Contrato {

	private GregorianCalendar dtLiberacao;
	private int tpGarantia;
	private int tpEmprestimo;
	private int cdGrupoSolidario;
	private int cdMembroGrupo;

	public Contrato(int cdContrato,
			int cdConvenio,
			int cdCategoriaParcelas,
			int cdEmpresa,
			int cdPessoa,
			int cdModeloContrato,
			int cdIndicador,
			GregorianCalendar dtAssinatura,
			GregorianCalendar dtPrimeiraParcela,
			int nrDiaVencimento,
			int nrParcelas,
			float prJurosMora,
			float prMultaMora,
			float prDescontoAdimplencia,
			float prDesconto,
			int tpContrato,
			float vlParcelas,
			float vlAdesao,
			float vlContrato,
			String nrContrato,
			String txtContrato,
			int stContrato,
			String idContrato,
			GregorianCalendar dtInicioVigencia,
			GregorianCalendar dtFinalVigencia,
			int cdAgente,
			int cdContaCarteira,
			int cdConta,
			int tpAmortizacao,
			int gnContrato,
			float prJuros,
			int cdTipoOperacao,
			int cdDocumento,
			int tpDesconto,
			float vlDesconto,
			int cdContratoOrigem,
			String txtObservacao,
			int cdCategoriaAdesao,
			GregorianCalendar dtLiberacao,
			int tpGarantia,
			int tpEmprestimo,
			int cdGrupoSolidario,
			int cdMembroGrupo){
		super(cdContrato,
			cdConvenio,
			cdCategoriaParcelas,
			cdEmpresa,
			cdPessoa,
			cdModeloContrato,
			cdIndicador,
			dtAssinatura,
			dtPrimeiraParcela,
			nrDiaVencimento,
			nrParcelas,
			prJurosMora,
			prMultaMora,
			prDescontoAdimplencia,
			prDesconto,
			tpContrato,
			vlParcelas,
			vlAdesao,
			vlContrato,
			nrContrato,
			txtContrato,
			stContrato,
			idContrato,
			dtInicioVigencia,
			dtFinalVigencia,
			cdAgente,
			cdContaCarteira,
			cdConta,
			tpAmortizacao,
			gnContrato,
			prJuros,
			cdTipoOperacao,
			cdDocumento,
			tpDesconto,
			vlDesconto,
			cdContratoOrigem,
			txtObservacao,
			cdCategoriaAdesao);
		setDtLiberacao(dtLiberacao);
		setTpGarantia(tpGarantia);
		setTpEmprestimo(tpEmprestimo);
		setCdGrupoSolidario(cdGrupoSolidario);
		setCdMembroGrupo(cdMembroGrupo);
	}
	public void setDtLiberacao(GregorianCalendar dtLiberacao){
		this.dtLiberacao=dtLiberacao;
	}
	public GregorianCalendar getDtLiberacao(){
		return this.dtLiberacao;
	}
	public void setTpGarantia(int tpGarantia){
		this.tpGarantia=tpGarantia;
	}
	public int getTpGarantia(){
		return this.tpGarantia;
	}
	public void setTpEmprestimo(int tpEmprestimo){
		this.tpEmprestimo=tpEmprestimo;
	}
	public int getTpEmprestimo(){
		return this.tpEmprestimo;
	}
	public void setCdGrupoSolidario(int cdGrupoSolidario){
		this.cdGrupoSolidario=cdGrupoSolidario;
	}
	public int getCdGrupoSolidario(){
		return this.cdGrupoSolidario;
	}
	public void setCdMembroGrupo(int cdMembroGrupo){
		this.cdMembroGrupo=cdMembroGrupo;
	}
	public int getCdMembroGrupo(){
		return this.cdMembroGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", dtLiberacao: " +  sol.util.Util.formatDateTime(getDtLiberacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpGarantia: " +  getTpGarantia();
		valueToString += ", tpEmprestimo: " +  getTpEmprestimo();
		valueToString += ", cdGrupoSolidario: " +  getCdGrupoSolidario();
		valueToString += ", cdMembroGrupo: " +  getCdMembroGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Contrato(getCdContrato(),
			getCdConvenio(),
			getCdCategoriaParcelas(),
			getCdEmpresa(),
			getCdPessoa(),
			getCdModeloContrato(),
			getCdIndicador(),
			getDtAssinatura(),
			getDtPrimeiraParcela(),
			getNrDiaVencimento(),
			getNrParcelas(),
			getPrJurosMora(),
			getPrMultaMora(),
			getPrDescontoAdimplencia(),
			getPrDesconto(),
			getTpContrato(),
			getVlParcelas(),
			getVlAdesao(),
			getVlContrato(),
			getNrContrato(),
			getTxtContrato(),
			getStContrato(),
			getIdContrato(),
			getDtInicioVigencia(),
			getDtFinalVigencia(),
			getCdAgente(),
			getCdContaCarteira(),
			getCdConta(),
			getTpAmortizacao(),
			getGnContrato(),
			getPrJuros(),
			getCdTipoOperacao(),
			getCdDocumento(),
			getTpDesconto(),
			getVlDesconto(),
			getCdContratoOrigem(),
			getTxtObservacao(),
			getCdCategoriaAdesao(),
			getDtLiberacao()==null ? null : (GregorianCalendar)getDtLiberacao().clone(),
			getTpGarantia(),
			getTpEmprestimo(),
			getCdGrupoSolidario(),
			getCdMembroGrupo());
	}

}
