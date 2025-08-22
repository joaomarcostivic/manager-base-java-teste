package com.tivic.manager.fsc;

public class NotaFiscalItemTributo {

	private int cdNotaFiscal;
	private int cdItem;
	private int cdTributo;
	private int cdTributoAliquota;
	private int tpRegimeTributario;
	private int tpOrigem;
	private int tpCalculo;
	private float vlBaseCalculo;
	private float vlOutrasDespesas;
	private float vlOutrosImpostos;
	private float prAliquota;
	private float vlTributo;
	private float prCredito;
	private float vlCredito;
	private String nrClasse;
	private String nrEnquadramento;
	private int cdSituacaoTributaria;
	private float vlBaseRetencao;
	private float vlRetido;
	
	public NotaFiscalItemTributo(int cdNotaFiscal,
			int cdItem,
			int cdTributo,
			int cdTributoAliquota,
			int tpRegimeTributario,
			int tpOrigem,
			int tpCalculo,
			float vlBaseCalculo,
			float vlOutrasDespesas,
			float vlOutrosImpostos,
			float prAliquota,
			float vlTributo,
			float prCredito,
			float vlCredito,
			String nrClasse,
			String nrEnquadramento){
		setCdNotaFiscal(cdNotaFiscal);
		setCdItem(cdItem);
		setCdTributo(cdTributo);
		setCdTributoAliquota(cdTributoAliquota);
		setTpRegimeTributario(tpRegimeTributario);
		setTpOrigem(tpOrigem);
		setTpCalculo(tpCalculo);
		setVlBaseCalculo(vlBaseCalculo);
		setVlOutrasDespesas(vlOutrasDespesas);
		setVlOutrosImpostos(vlOutrosImpostos);
		setPrAliquota(prAliquota);
		setVlTributo(vlTributo);
		setPrCredito(prCredito);
		setVlCredito(vlCredito);
		setNrClasse(nrClasse);
		setNrEnquadramento(nrEnquadramento);
	}
	public NotaFiscalItemTributo(int cdNotaFiscal,
			int cdItem,
			int cdTributo,
			int cdTributoAliquota,
			int tpRegimeTributario,
			int tpOrigem,
			int tpCalculo,
			float vlBaseCalculo,
			float vlOutrasDespesas,
			float vlOutrosImpostos,
			float prAliquota,
			float vlTributo,
			float prCredito,
			float vlCredito,
			String nrClasse,
			String nrEnquadramento,
			int cdSituacaoTributaria,
			float vlBaseRetencao,
			float vlRetido){
		setCdNotaFiscal(cdNotaFiscal);
		setCdItem(cdItem);
		setCdTributo(cdTributo);
		setCdTributoAliquota(cdTributoAliquota);
		setTpRegimeTributario(tpRegimeTributario);
		setTpOrigem(tpOrigem);
		setTpCalculo(tpCalculo);
		setVlBaseCalculo(vlBaseCalculo);
		setVlOutrasDespesas(vlOutrasDespesas);
		setVlOutrosImpostos(vlOutrosImpostos);
		setPrAliquota(prAliquota);
		setVlTributo(vlTributo);
		setPrCredito(prCredito);
		setVlCredito(vlCredito);
		setNrClasse(nrClasse);
		setNrEnquadramento(nrEnquadramento);
		setCdSituacaoTributaria(cdSituacaoTributaria);
		setVlBaseRetencao(vlBaseRetencao);
		setVlRetido(vlRetido);
	}
	public void setCdNotaFiscal(int cdNotaFiscal){
		this.cdNotaFiscal=cdNotaFiscal;
	}
	public int getCdNotaFiscal(){
		return this.cdNotaFiscal;
	}
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
	}
	public void setCdTributo(int cdTributo){
		this.cdTributo=cdTributo;
	}
	public int getCdTributo(){
		return this.cdTributo;
	}
	public void setCdTributoAliquota(int cdTributoAliquota){
		this.cdTributoAliquota=cdTributoAliquota;
	}
	public int getCdTributoAliquota(){
		return this.cdTributoAliquota;
	}
	public void setTpRegimeTributario(int tpRegimeTributario){
		this.tpRegimeTributario=tpRegimeTributario;
	}
	public int getTpRegimeTributario(){
		return this.tpRegimeTributario;
	}
	public void setTpOrigem(int tpOrigem){
		this.tpOrigem=tpOrigem;
	}
	public int getTpOrigem(){
		return this.tpOrigem;
	}
	public void setTpCalculo(int tpCalculo){
		this.tpCalculo=tpCalculo;
	}
	public int getTpCalculo(){
		return this.tpCalculo;
	}
	public void setVlBaseCalculo(float vlBaseCalculo){
		this.vlBaseCalculo=vlBaseCalculo;
	}
	public float getVlBaseCalculo(){
		return this.vlBaseCalculo;
	}
	public void setVlOutrasDespesas(float vlOutrasDespesas){
		this.vlOutrasDespesas=vlOutrasDespesas;
	}
	public float getVlOutrasDespesas(){
		return this.vlOutrasDespesas;
	}
	public void setVlOutrosImpostos(float vlOutrosImpostos){
		this.vlOutrosImpostos=vlOutrosImpostos;
	}
	public float getVlOutrosImpostos(){
		return this.vlOutrosImpostos;
	}
	public void setPrAliquota(float prAliquota){
		this.prAliquota=prAliquota;
	}
	public float getPrAliquota(){
		return this.prAliquota;
	}
	public void setVlTributo(float vlTributo){
		this.vlTributo=vlTributo;
	}
	public float getVlTributo(){
		return this.vlTributo;
	}
	public void setPrCredito(float prCredito){
		this.prCredito=prCredito;
	}
	public float getPrCredito(){
		return this.prCredito;
	}
	public void setVlCredito(float vlCredito){
		this.vlCredito=vlCredito;
	}
	public float getVlCredito(){
		return this.vlCredito;
	}
	public void setNrClasse(String nrClasse){
		this.nrClasse=nrClasse;
	}
	public String getNrClasse(){
		return this.nrClasse;
	}
	public void setNrEnquadramento(String nrEnquadramento){
		this.nrEnquadramento=nrEnquadramento;
	}
	public String getNrEnquadramento(){
		return this.nrEnquadramento;
	}
	public void setCdSituacaoTributaria(int cdSituacaoTributaria) {
		this.cdSituacaoTributaria = cdSituacaoTributaria;
	}
	public int getCdSituacaoTributaria() {
		return cdSituacaoTributaria;
	}
	public void setVlBaseRetencao(float vlBaseRetencao) {
		this.vlBaseRetencao = vlBaseRetencao;
	}
	public float getVlBaseRetencao() {
		return vlBaseRetencao;
	}
	public void setVlRetido(float vlRetido) {
		this.vlRetido = vlRetido;
	}
	public float getVlRetido() {
		return vlRetido;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNotaFiscal: " +  getCdNotaFiscal();
		valueToString += ", cdItem: " +  getCdItem();
		valueToString += ", cdTributo: " +  getCdTributo();
		valueToString += ", cdTributoAliquota: " +  getCdTributoAliquota();
		valueToString += ", tpRegimeTributario: " +  getTpRegimeTributario();
		valueToString += ", tpOrigem: " +  getTpOrigem();
		valueToString += ", tpCalculo: " +  getTpCalculo();
		valueToString += ", vlBaseCalculo: " +  getVlBaseCalculo();
		valueToString += ", vlOutrasDespesas: " +  getVlOutrasDespesas();
		valueToString += ", vlOutrosImpostos: " +  getVlOutrosImpostos();
		valueToString += ", prAliquota: " +  getPrAliquota();
		valueToString += ", vlTributo: " +  getVlTributo();
		valueToString += ", prCredito: " +  getPrCredito();
		valueToString += ", vlCredito: " +  getVlCredito();
		valueToString += ", nrClasse: " +  getNrClasse();
		valueToString += ", nrEnquadramento: " +  getNrEnquadramento();
		valueToString += ", cdSituacaoTributaria: " +  getCdSituacaoTributaria();
		valueToString += ", vlBaseRetencao: " +  getVlBaseRetencao();
		valueToString += ", vlRetido: " +  getVlRetido();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NotaFiscalItemTributo(getCdNotaFiscal(),
			getCdItem(),
			getCdTributo(),
			getCdTributoAliquota(),
			getTpRegimeTributario(),
			getTpOrigem(),
			getTpCalculo(),
			getVlBaseCalculo(),
			getVlOutrasDespesas(),
			getVlOutrosImpostos(),
			getPrAliquota(),
			getVlTributo(),
			getPrCredito(),
			getVlCredito(),
			getNrClasse(),
			getNrEnquadramento(),
			getCdSituacaoTributaria(),
			getVlBaseRetencao(),
			getVlRetido());
	}

}