package com.tivic.manager.adm;

public class ContaCarteira {

	private int cdContaCarteira;
	private int cdConta;
	private String nmCarteira;
	private String sgCarteira;
	private String nmLocalPagamento;
	private String nmMoeda;
	private String nmAceite;
	private int tpDigito;
	private String txtMensagem;
	private float vlDespesaCobranca;
	private String nrCedente;
	private int tpCobranca;
	private float prJuros;
	private float prMulta;
	private float prDescontoAdimplencia;
	private int qtDiasDevolucao;
	private int qtDiasDesconto;
	private int qtDiasMulta;
	private int qtDigitosNumero;
	private String nrDigitosInicio;
	private byte[] imgLogotipo;
	private int qtDiasProtesto;
	private String nrCarteira;
	private String idCarteira;
	private int nrBaseInicial;
	private int nrBaseFinal;
	private String nrConvenio;
	private int tpArquivoEdi;
	private String nrServico;
	private String txtCampoLivre;

	public ContaCarteira(int cdContaCarteira,
			int cdConta,
			String nmCarteira,
			String sgCarteira,
			String nmLocalPagamento,
			String nmMoeda,
			String nmAceite,
			int tpDigito,
			String txtMensagem,
			float vlDespesaCobranca,
			String nrCedente,
			int tpCobranca,
			float prJuros,
			float prMulta,
			float prDescontoAdimplencia,
			int qtDiasDevolucao,
			int qtDiasDesconto,
			int qtDiasMulta,
			int qtDigitosNumero,
			String nrDigitosInicio,
			byte[] imgLogotipo,
			int qtDiasProtesto,
			String nrCarteira,
			String idCarteira,
			int nrBaseInicial,
			int nrBaseFinal,
			String nrConvenio,
			int tpArquivoEdi,
			String nrServico,
			String txtCampoLivre){
		setCdContaCarteira(cdContaCarteira);
		setCdConta(cdConta);
		setNmCarteira(nmCarteira);
		setSgCarteira(sgCarteira);
		setNmLocalPagamento(nmLocalPagamento);
		setNmMoeda(nmMoeda);
		setNmAceite(nmAceite);
		setTpDigito(tpDigito);
		setTxtMensagem(txtMensagem);
		setVlDespesaCobranca(vlDespesaCobranca);
		setNrCedente(nrCedente);
		setTpCobranca(tpCobranca);
		setPrJuros(prJuros);
		setPrMulta(prMulta);
		setPrDescontoAdimplencia(prDescontoAdimplencia);
		setQtDiasDevolucao(qtDiasDevolucao);
		setQtDiasDesconto(qtDiasDesconto);
		setQtDiasMulta(qtDiasMulta);
		setQtDigitosNumero(qtDigitosNumero);
		setNrDigitosInicio(nrDigitosInicio);
		setImgLogotipo(imgLogotipo);
		setQtDiasProtesto(qtDiasProtesto);
		setNrCarteira(nrCarteira);
		setIdCarteira(idCarteira);
		setNrBaseInicial(nrBaseInicial);
		setNrBaseFinal(nrBaseFinal);
		setNrConvenio(nrConvenio);
		setTpArquivoEdi(tpArquivoEdi);
		setNrServico(nrServico);
		setTxtCampoLivre(txtCampoLivre);
	}
	public ContaCarteira() {
	}
	public void setCdContaCarteira(int cdContaCarteira){
		this.cdContaCarteira=cdContaCarteira;
	}
	public int getCdContaCarteira(){
		return this.cdContaCarteira;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setNmCarteira(String nmCarteira){
		this.nmCarteira=nmCarteira;
	}
	public String getNmCarteira(){
		return this.nmCarteira;
	}
	public void setSgCarteira(String sgCarteira){
		this.sgCarteira=sgCarteira;
	}
	public String getSgCarteira(){
		return this.sgCarteira;
	}
	public void setNmLocalPagamento(String nmLocalPagamento){
		this.nmLocalPagamento=nmLocalPagamento;
	}
	public String getNmLocalPagamento(){
		return this.nmLocalPagamento;
	}
	public void setNmMoeda(String nmMoeda){
		this.nmMoeda=nmMoeda;
	}
	public String getNmMoeda(){
		return this.nmMoeda;
	}
	public void setNmAceite(String nmAceite){
		this.nmAceite=nmAceite;
	}
	public String getNmAceite(){
		return this.nmAceite;
	}
	public void setTpDigito(int tpDigito){
		this.tpDigito=tpDigito;
	}
	public int getTpDigito(){
		return this.tpDigito;
	}
	public void setTxtMensagem(String txtMensagem){
		this.txtMensagem=txtMensagem;
	}
	public String getTxtMensagem(){
		return this.txtMensagem;
	}
	public void setVlDespesaCobranca(float vlDespesaCobranca){
		this.vlDespesaCobranca=vlDespesaCobranca;
	}
	public float getVlDespesaCobranca(){
		return this.vlDespesaCobranca;
	}
	public void setNrCedente(String nrCedente){
		this.nrCedente=nrCedente;
	}
	public String getNrCedente(){
		return this.nrCedente;
	}
	public void setTpCobranca(int tpCobranca){
		this.tpCobranca=tpCobranca;
	}
	public int getTpCobranca(){
		return this.tpCobranca;
	}
	public void setPrJuros(float prJuros){
		this.prJuros=prJuros;
	}
	public float getPrJuros(){
		return this.prJuros;
	}
	public void setPrMulta(float prMulta){
		this.prMulta=prMulta;
	}
	public float getPrMulta(){
		return this.prMulta;
	}
	public void setPrDescontoAdimplencia(float prDescontoAdimplencia){
		this.prDescontoAdimplencia=prDescontoAdimplencia;
	}
	public float getPrDescontoAdimplencia(){
		return this.prDescontoAdimplencia;
	}
	public void setQtDiasDevolucao(int qtDiasDevolucao){
		this.qtDiasDevolucao=qtDiasDevolucao;
	}
	public int getQtDiasDevolucao(){
		return this.qtDiasDevolucao;
	}
	public void setQtDiasDesconto(int qtDiasDesconto){
		this.qtDiasDesconto=qtDiasDesconto;
	}
	public int getQtDiasDesconto(){
		return this.qtDiasDesconto;
	}
	public void setQtDiasMulta(int qtDiasMulta){
		this.qtDiasMulta=qtDiasMulta;
	}
	public int getQtDiasMulta(){
		return this.qtDiasMulta;
	}
	public void setQtDigitosNumero(int qtDigitosNumero){
		this.qtDigitosNumero=qtDigitosNumero;
	}
	public int getQtDigitosNumero(){
		return this.qtDigitosNumero;
	}
	public void setNrDigitosInicio(String nrDigitosInicio){
		this.nrDigitosInicio=nrDigitosInicio;
	}
	public String getNrDigitosInicio(){
		return this.nrDigitosInicio;
	}
	public void setImgLogotipo(byte[] imgLogotipo){
		this.imgLogotipo=imgLogotipo;
	}
	public byte[] getImgLogotipo(){
		return this.imgLogotipo;
	}
	public void setQtDiasProtesto(int qtDiasProtesto){
		this.qtDiasProtesto=qtDiasProtesto;
	}
	public int getQtDiasProtesto(){
		return this.qtDiasProtesto;
	}
	public void setNrCarteira(String nrCarteira){
		this.nrCarteira=nrCarteira;
	}
	public String getNrCarteira(){
		return this.nrCarteira;
	}
	public void setIdCarteira(String idCarteira){
		this.idCarteira=idCarteira;
	}
	public String getIdCarteira(){
		return this.idCarteira;
	}
	public void setNrBaseInicial(int nrBaseInicial){
		this.nrBaseInicial=nrBaseInicial;
	}
	public int getNrBaseInicial(){
		return this.nrBaseInicial;
	}
	public void setNrBaseFinal(int nrBaseFinal){
		this.nrBaseFinal=nrBaseFinal;
	}
	public int getNrBaseFinal(){
		return this.nrBaseFinal;
	}
	public void setNrConvenio(String nrConvenio){
		this.nrConvenio=nrConvenio;
	}
	public String getNrConvenio(){
		return this.nrConvenio;
	}
	public void setTpArquivoEdi(int tpArquivoEdi){
		this.tpArquivoEdi=tpArquivoEdi;
	}
	public int getTpArquivoEdi(){
		return this.tpArquivoEdi;
	}
	public void setNrServico(String nrServico){
		this.nrServico=nrServico;
	}
	public String getNrServico(){
		return this.nrServico;
	}
	public void setTxtCampoLivre(String txtCampoLivre){
		this.txtCampoLivre=txtCampoLivre;
	}
	public String getTxtCampoLivre(){
		return this.txtCampoLivre;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContaCarteira: " +  getCdContaCarteira();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", nmCarteira: " +  getNmCarteira();
		valueToString += ", sgCarteira: " +  getSgCarteira();
		valueToString += ", nmLocalPagamento: " +  getNmLocalPagamento();
		valueToString += ", nmMoeda: " +  getNmMoeda();
		valueToString += ", nmAceite: " +  getNmAceite();
		valueToString += ", tpDigito: " +  getTpDigito();
		valueToString += ", txtMensagem: " +  getTxtMensagem();
		valueToString += ", vlDespesaCobranca: " +  getVlDespesaCobranca();
		valueToString += ", nrCedente: " +  getNrCedente();
		valueToString += ", tpCobranca: " +  getTpCobranca();
		valueToString += ", prJuros: " +  getPrJuros();
		valueToString += ", prMulta: " +  getPrMulta();
		valueToString += ", prDescontoAdimplencia: " +  getPrDescontoAdimplencia();
		valueToString += ", qtDiasDevolucao: " +  getQtDiasDevolucao();
		valueToString += ", qtDiasDesconto: " +  getQtDiasDesconto();
		valueToString += ", qtDiasMulta: " +  getQtDiasMulta();
		valueToString += ", qtDigitosNumero: " +  getQtDigitosNumero();
		valueToString += ", nrDigitosInicio: " +  getNrDigitosInicio();
		valueToString += ", imgLogotipo: " +  getImgLogotipo();
		valueToString += ", qtDiasProtesto: " +  getQtDiasProtesto();
		valueToString += ", nrCarteira: " +  getNrCarteira();
		valueToString += ", idCarteira: " +  getIdCarteira();
		valueToString += ", nrBaseInicial: " +  getNrBaseInicial();
		valueToString += ", nrBaseFinal: " +  getNrBaseFinal();
		valueToString += ", nrConvenio: " +  getNrConvenio();
		valueToString += ", tpArquivoEdi: " +  getTpArquivoEdi();
		valueToString += ", nrServico: " +  getNrServico();
		valueToString += ", txtCampoLivre: " +  getTxtCampoLivre();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaCarteira(getCdContaCarteira(),
			getCdConta(),
			getNmCarteira(),
			getSgCarteira(),
			getNmLocalPagamento(),
			getNmMoeda(),
			getNmAceite(),
			getTpDigito(),
			getTxtMensagem(),
			getVlDespesaCobranca(),
			getNrCedente(),
			getTpCobranca(),
			getPrJuros(),
			getPrMulta(),
			getPrDescontoAdimplencia(),
			getQtDiasDevolucao(),
			getQtDiasDesconto(),
			getQtDiasMulta(),
			getQtDigitosNumero(),
			getNrDigitosInicio(),
			getImgLogotipo(),
			getQtDiasProtesto(),
			getNrCarteira(),
			getIdCarteira(),
			getNrBaseInicial(),
			getNrBaseFinal(),
			getNrConvenio(),
			getTpArquivoEdi(),
			getNrServico(),
			getTxtCampoLivre());
	}

}