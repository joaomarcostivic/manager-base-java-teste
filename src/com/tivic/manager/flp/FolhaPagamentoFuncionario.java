package com.tivic.manager.flp;

public class FolhaPagamentoFuncionario {

	private int cdMatricula;
	private int cdFolhaPagamento;
	private int qtDiasTrabalhados;
	private int qtHorasMes;
	private float vlBaseIrrf;
	private float vlBaseInss;
	private float vlBaseFgts;
	private float vlInss;
	private float vlInssPatronal;
	private float vlTotalProvento;
	private float vlTotalDesconto;
	private float vlHora;
	private float vlDia;
	private float vlProventoPrincipal;
	private float vlValeTransporte;
	private float vlFgts;
	private float vlIrrf;
	private float prFgts;
	private float prInss;
	private float prIrrf;
	private float prInssPatronal;
	private float vlParcelaDeducaoIrrf;
	private float vlDeducaoDependenteIrrf;
	private float vlSalarioFamilia;
	private float vlBaseInssPatronal;
	private float vlSalarioComissao;
	private float vlSat;
	private int cdVinculoEmpregaticio;
	private int cdSetor;
	private int cdEventoPrincipal;
	private int cdConvenio;

	public FolhaPagamentoFuncionario(int cdMatricula, int cdFolhaPagamento, int cdVinculoEmpregaticio,
			int cdSetor, int cdConvenio){
		setCdMatricula(cdMatricula);
		setCdFolhaPagamento(cdFolhaPagamento);
		setCdVinculoEmpregaticio(cdVinculoEmpregaticio);
		setCdSetor(cdSetor);
		setCdConvenio(cdConvenio);
	}
	
	public FolhaPagamentoFuncionario(int cdMatricula,
			int cdFolhaPagamento,
			int qtDiasTrabalhados,
			int qtHorasMes,
			float vlBaseIrrf,
			float vlBaseInss,
			float vlBaseFgts,
			float vlInss,
			float vlInssPatronal,
			float vlTotalProvento,
			float vlTotalDesconto,
			float vlHora,
			float vlDia,
			float vlProventoPrincipal,
			float vlValeTransporte,
			float vlFgts,
			float vlIrrf,
			float prFgts,
			float prInss,
			float prIrrf,
			float prInssPatronal,
			float vlParcelaDeducaoIrrf,
			float vlDeducaoDependenteIrrf,
			float vlSalarioFamilia,
			float vlBaseInssPatronal,
			float vlSalarioComissao,
			float vlSat,
			int cdVinculoEmpregaticio,
			int cdSetor,
			int cdEventoPrincipal,
			int cdConvenio){
		setCdMatricula(cdMatricula);
		setCdFolhaPagamento(cdFolhaPagamento);
		setQtDiasTrabalhados(qtDiasTrabalhados);
		setQtHorasMes(qtHorasMes);
		setVlBaseIrrf(vlBaseIrrf);
		setVlBaseInss(vlBaseInss);
		setVlBaseFgts(vlBaseFgts);
		setVlInss(vlInss);
		setVlInssPatronal(vlInssPatronal);
		setVlTotalProvento(vlTotalProvento);
		setVlTotalDesconto(vlTotalDesconto);
		setVlHora(vlHora);
		setVlDia(vlDia);
		setVlProventoPrincipal(vlProventoPrincipal);
		setVlValeTransporte(vlValeTransporte);
		setVlFgts(vlFgts);
		setVlIrrf(vlIrrf);
		setPrFgts(prFgts);
		setPrInss(prInss);
		setPrIrrf(prIrrf);
		setPrInssPatronal(prInssPatronal);
		setVlParcelaDeducaoIrrf(vlParcelaDeducaoIrrf);
		setVlDeducaoDependenteIrrf(vlDeducaoDependenteIrrf);
		setVlSalarioFamilia(vlSalarioFamilia);
		setVlBaseInssPatronal(vlBaseInssPatronal);
		setVlSalarioComissao(vlSalarioComissao);
		setVlSat(vlSat);
		setCdVinculoEmpregaticio(cdVinculoEmpregaticio);
		setCdSetor(cdSetor);
		setCdEventoPrincipal(cdEventoPrincipal);
		setCdConvenio(cdConvenio);
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdFolhaPagamento(int cdFolhaPagamento){
		this.cdFolhaPagamento=cdFolhaPagamento;
	}
	public int getCdFolhaPagamento(){
		return this.cdFolhaPagamento;
	}
	public void setQtDiasTrabalhados(int qtDiasTrabalhados){
		this.qtDiasTrabalhados=qtDiasTrabalhados;
	}
	public int getQtDiasTrabalhados(){
		return this.qtDiasTrabalhados;
	}
	public void setQtHorasMes(int qtHorasMes){
		this.qtHorasMes=qtHorasMes;
	}
	public int getQtHorasMes(){
		return this.qtHorasMes;
	}
	public void setVlBaseIrrf(float vlBaseIrrf){
		this.vlBaseIrrf=vlBaseIrrf;
	}
	public float getVlBaseIrrf(){
		return this.vlBaseIrrf;
	}
	public void setVlBaseInss(float vlBaseInss){
		this.vlBaseInss=vlBaseInss;
	}
	public float getVlBaseInss(){
		return this.vlBaseInss;
	}
	public void setVlBaseFgts(float vlBaseFgts){
		this.vlBaseFgts=vlBaseFgts;
	}
	public float getVlBaseFgts(){
		return this.vlBaseFgts;
	}
	public void setVlInss(float vlInss){
		this.vlInss=vlInss;
	}
	public float getVlInss(){
		return this.vlInss;
	}
	public void setVlInssPatronal(float vlInssPatronal){
		this.vlInssPatronal=vlInssPatronal;
	}
	public float getVlInssPatronal(){
		return this.vlInssPatronal;
	}
	public void setVlTotalProvento(float vlTotalProvento){
		this.vlTotalProvento=vlTotalProvento;
	}
	public float getVlTotalProvento(){
		return this.vlTotalProvento;
	}
	public void setVlTotalDesconto(float vlTotalDesconto){
		this.vlTotalDesconto=vlTotalDesconto;
	}
	public float getVlTotalDesconto(){
		return this.vlTotalDesconto;
	}
	public void setVlHora(float vlHora){
		this.vlHora=vlHora;
	}
	public float getVlHora(){
		return this.vlHora;
	}
	public void setVlDia(float vlDia){
		this.vlDia=vlDia;
	}
	public float getVlDia(){
		return this.vlDia;
	}
	public void setVlProventoPrincipal(float vlProventoPrincipal){
		this.vlProventoPrincipal=vlProventoPrincipal;
	}
	public float getVlProventoPrincipal(){
		return this.vlProventoPrincipal;
	}
	public void setVlValeTransporte(float vlValeTransporte){
		this.vlValeTransporte=vlValeTransporte;
	}
	public float getVlValeTransporte(){
		return this.vlValeTransporte;
	}
	public void setVlFgts(float vlFgts){
		this.vlFgts=vlFgts;
	}
	public float getVlFgts(){
		return this.vlFgts;
	}
	public void setVlIrrf(float vlIrrf){
		this.vlIrrf=vlIrrf;
	}
	public float getVlIrrf(){
		return this.vlIrrf;
	}
	public void setPrFgts(float prFgts){
		this.prFgts=prFgts;
	}
	public float getPrFgts(){
		return this.prFgts;
	}
	public void setPrInss(float prInss){
		this.prInss=prInss;
	}
	public float getPrInss(){
		return this.prInss;
	}
	public void setPrIrrf(float prIrrf){
		this.prIrrf=prIrrf;
	}
	public float getPrIrrf(){
		return this.prIrrf;
	}
	public void setPrInssPatronal(float prInssPatronal){
		this.prInssPatronal=prInssPatronal;
	}
	public float getPrInssPatronal(){
		return this.prInssPatronal;
	}
	public void setVlParcelaDeducaoIrrf(float vlParcelaDeducaoIrrf){
		this.vlParcelaDeducaoIrrf=vlParcelaDeducaoIrrf;
	}
	public float getVlParcelaDeducaoIrrf(){
		return this.vlParcelaDeducaoIrrf;
	}
	public void setVlDeducaoDependenteIrrf(float vlDeducaoDependenteIrrf){
		this.vlDeducaoDependenteIrrf=vlDeducaoDependenteIrrf;
	}
	public float getVlDeducaoDependenteIrrf(){
		return this.vlDeducaoDependenteIrrf;
	}
	public void setVlSalarioFamilia(float vlSalarioFamilia){
		this.vlSalarioFamilia=vlSalarioFamilia;
	}
	public float getVlSalarioFamilia(){
		return this.vlSalarioFamilia;
	}
	public void setVlBaseInssPatronal(float vlBaseInssPatronal){
		this.vlBaseInssPatronal=vlBaseInssPatronal;
	}
	public float getVlBaseInssPatronal(){
		return this.vlBaseInssPatronal;
	}
	public void setVlSalarioComissao(float vlSalarioComissao){
		this.vlSalarioComissao=vlSalarioComissao;
	}
	public float getVlSalarioComissao(){
		return this.vlSalarioComissao;
	}
	public void setVlSat(float vlSat){
		this.vlSat=vlSat;
	}
	public float getVlSat(){
		return this.vlSat;
	}
	public void setCdVinculoEmpregaticio(int cdVinculoEmpregaticio){
		this.cdVinculoEmpregaticio=cdVinculoEmpregaticio;
	}
	public int getCdVinculoEmpregaticio(){
		return this.cdVinculoEmpregaticio;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setCdEventoPrincipal(int cdEventoPrincipal){
		this.cdEventoPrincipal=cdEventoPrincipal;
	}
	public int getCdEventoPrincipal(){
		return this.cdEventoPrincipal;
	}
	public void setCdConvenio(int cdConvenio){
		this.cdConvenio=cdConvenio;
	}
	public int getCdConvenio(){
		return this.cdConvenio;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatricula: " +  getCdMatricula();
		valueToString += ", cdFolhaPagamento: " +  getCdFolhaPagamento();
		valueToString += ", qtDiasTrabalhados: " +  getQtDiasTrabalhados();
		valueToString += ", qtHorasMes: " +  getQtHorasMes();
		valueToString += ", vlBaseIrrf: " +  getVlBaseIrrf();
		valueToString += ", vlBaseInss: " +  getVlBaseInss();
		valueToString += ", vlBaseFgts: " +  getVlBaseFgts();
		valueToString += ", vlInss: " +  getVlInss();
		valueToString += ", vlInssPatronal: " +  getVlInssPatronal();
		valueToString += ", vlTotalProvento: " +  getVlTotalProvento();
		valueToString += ", vlTotalDesconto: " +  getVlTotalDesconto();
		valueToString += ", vlHora: " +  getVlHora();
		valueToString += ", vlDia: " +  getVlDia();
		valueToString += ", vlProventoPrincipal: " +  getVlProventoPrincipal();
		valueToString += ", vlValeTransporte: " +  getVlValeTransporte();
		valueToString += ", vlFgts: " +  getVlFgts();
		valueToString += ", vlIrrf: " +  getVlIrrf();
		valueToString += ", prFgts: " +  getPrFgts();
		valueToString += ", prInss: " +  getPrInss();
		valueToString += ", prIrrf: " +  getPrIrrf();
		valueToString += ", prInssPatronal: " +  getPrInssPatronal();
		valueToString += ", vlParcelaDeducaoIrrf: " +  getVlParcelaDeducaoIrrf();
		valueToString += ", vlDeducaoDependenteIrrf: " +  getVlDeducaoDependenteIrrf();
		valueToString += ", vlSalarioFamilia: " +  getVlSalarioFamilia();
		valueToString += ", vlBaseInssPatronal: " +  getVlBaseInssPatronal();
		valueToString += ", vlSalarioComissao: " +  getVlSalarioComissao();
		valueToString += ", vlSat: " +  getVlSat();
		valueToString += ", cdVinculoEmpregaticio: " +  getCdVinculoEmpregaticio();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", cdEventoPrincipal: " +  getCdEventoPrincipal();
		valueToString += ", cdConvenio: " +  getCdConvenio();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FolhaPagamentoFuncionario(getCdMatricula(),
			getCdFolhaPagamento(),
			getQtDiasTrabalhados(),
			getQtHorasMes(),
			getVlBaseIrrf(),
			getVlBaseInss(),
			getVlBaseFgts(),
			getVlInss(),
			getVlInssPatronal(),
			getVlTotalProvento(),
			getVlTotalDesconto(),
			getVlHora(),
			getVlDia(),
			getVlProventoPrincipal(),
			getVlValeTransporte(),
			getVlFgts(),
			getVlIrrf(),
			getPrFgts(),
			getPrInss(),
			getPrIrrf(),
			getPrInssPatronal(),
			getVlParcelaDeducaoIrrf(),
			getVlDeducaoDependenteIrrf(),
			getVlSalarioFamilia(),
			getVlBaseInssPatronal(),
			getVlSalarioComissao(),
			getVlSat(),
			getCdVinculoEmpregaticio(),
			getCdSetor(),
			getCdEventoPrincipal(),
			getCdConvenio());
	}
}