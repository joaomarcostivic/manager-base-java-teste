package com.tivic.manager.adm;

public class ProgramaFatura {

	private int cdProgramaFatura;
	private String idProgramaFatura;
	private int stProgramaFatura;
	private int lgPadrao;
	private String nmProgramaFatura;
	private String txtDescricao;
	private int lgAposEntrega;
	private int lgProximoDiaUtil;
	private int nrDiaFixo;
	private int qtDiasCarencia;
	private float prDesconto;
	private int qtSuspenderApos;
	private int lgPeriodo;
	private int lgCobrarJuros;
	private float prCobrarJuros;
	private int lgProtestar;
	private int qtDiasProtesto;
	private String txtNota;
	private int nrInicioPeriodo;
	private int nrFinalPeriodo;
	private int nrVencimentoPeriodo;
	private int lgNaVenda;
	private int lgVencimentoFixo;

	public ProgramaFatura(int cdProgramaFatura,
			String idProgramaFatura,
			int stProgramaFatura,
			int lgPadrao,
			String nmProgramaFatura,
			String txtDescricao,
			int lgAposEntrega,
			int lgProximoDiaUtil,
			int nrDiaFixo,
			int qtDiasCarencia,
			float prDesconto,
			int qtSuspenderApos,
			int lgPeriodo,
			int lgCobrarJuros,
			float prCobrarJuros,
			int lgProtestar,
			int qtDiasProtesto,
			String txtNota,
			int nrInicioPeriodo,
			int nrFinalPeriodo,
			int nrVencimentoPeriodo,
			int lgNaVenda,
			int lgVencimentoFixo){
		setCdProgramaFatura(cdProgramaFatura);
		setIdProgramaFatura(idProgramaFatura);
		setStProgramaFatura(stProgramaFatura);
		setLgPadrao(lgPadrao);
		setNmProgramaFatura(nmProgramaFatura);
		setTxtDescricao(txtDescricao);
		setLgAposEntrega(lgAposEntrega);
		setLgProximoDiaUtil(lgProximoDiaUtil);
		setNrDiaFixo(nrDiaFixo);
		setQtDiasCarencia(qtDiasCarencia);
		setPrDesconto(prDesconto);
		setQtSuspenderApos(qtSuspenderApos);
		setLgPeriodo(lgPeriodo);
		setLgCobrarJuros(lgCobrarJuros);
		setPrCobrarJuros(prCobrarJuros);
		setLgProtestar(lgProtestar);
		setQtDiasProtesto(qtDiasProtesto);
		setTxtNota(txtNota);
		setNrInicioPeriodo(nrInicioPeriodo);
		setNrFinalPeriodo(nrFinalPeriodo);
		setNrVencimentoPeriodo(nrVencimentoPeriodo);
		setLgNaVenda(lgNaVenda);
		setLgVencimentoFixo(lgVencimentoFixo);
	}
	public void setCdProgramaFatura(int cdProgramaFatura){
		this.cdProgramaFatura=cdProgramaFatura;
	}
	public int getCdProgramaFatura(){
		return this.cdProgramaFatura;
	}
	public void setIdProgramaFatura(String idProgramaFatura){
		this.idProgramaFatura=idProgramaFatura;
	}
	public String getIdProgramaFatura(){
		return this.idProgramaFatura;
	}
	public void setStProgramaFatura(int stProgramaFatura){
		this.stProgramaFatura=stProgramaFatura;
	}
	public int getStProgramaFatura(){
		return this.stProgramaFatura;
	}
	public void setLgPadrao(int lgPadrao){
		this.lgPadrao=lgPadrao;
	}
	public int getLgPadrao(){
		return this.lgPadrao;
	}
	public void setNmProgramaFatura(String nmProgramaFatura){
		this.nmProgramaFatura=nmProgramaFatura;
	}
	public String getNmProgramaFatura(){
		return this.nmProgramaFatura;
	}
	public void setTxtDescricao(String txtDescricao){
		this.txtDescricao=txtDescricao;
	}
	public String getTxtDescricao(){
		return this.txtDescricao;
	}
	public void setLgAposEntrega(int lgAposEntrega){
		this.lgAposEntrega=lgAposEntrega;
	}
	public int getLgAposEntrega(){
		return this.lgAposEntrega;
	}
	public void setLgProximoDiaUtil(int lgProximoDiaUtil){
		this.lgProximoDiaUtil=lgProximoDiaUtil;
	}
	public int getLgProximoDiaUtil(){
		return this.lgProximoDiaUtil;
	}
	public void setNrDiaFixo(int nrDiaFixo){
		this.nrDiaFixo=nrDiaFixo;
	}
	public int getNrDiaFixo(){
		return this.nrDiaFixo;
	}
	public void setQtDiasCarencia(int qtDiasCarencia){
		this.qtDiasCarencia=qtDiasCarencia;
	}
	public int getQtDiasCarencia(){
		return this.qtDiasCarencia;
	}
	public void setPrDesconto(float prDesconto){
		this.prDesconto=prDesconto;
	}
	public float getPrDesconto(){
		return this.prDesconto;
	}
	public void setQtSuspenderApos(int qtSuspenderApos){
		this.qtSuspenderApos=qtSuspenderApos;
	}
	public int getQtSuspenderApos(){
		return this.qtSuspenderApos;
	}
	public void setLgPeriodo(int lgPeriodo){
		this.lgPeriodo=lgPeriodo;
	}
	public int getLgPeriodo(){
		return this.lgPeriodo;
	}
	public void setLgCobrarJuros(int lgCobrarJuros){
		this.lgCobrarJuros=lgCobrarJuros;
	}
	public int getLgCobrarJuros(){
		return this.lgCobrarJuros;
	}
	public void setPrCobrarJuros(float prCobrarJuros){
		this.prCobrarJuros=prCobrarJuros;
	}
	public float getPrCobrarJuros(){
		return this.prCobrarJuros;
	}
	public void setLgProtestar(int lgProtestar){
		this.lgProtestar=lgProtestar;
	}
	public int getLgProtestar(){
		return this.lgProtestar;
	}
	public void setQtDiasProtesto(int qtDiasProtesto){
		this.qtDiasProtesto=qtDiasProtesto;
	}
	public int getQtDiasProtesto(){
		return this.qtDiasProtesto;
	}
	public void setTxtNota(String txtNota){
		this.txtNota=txtNota;
	}
	public String getTxtNota(){
		return this.txtNota;
	}
	public void setNrInicioPeriodo(int nrInicioPeriodo){
		this.nrInicioPeriodo=nrInicioPeriodo;
	}
	public int getNrInicioPeriodo(){
		return this.nrInicioPeriodo;
	}
	public void setNrFinalPeriodo(int nrFinalPeriodo){
		this.nrFinalPeriodo=nrFinalPeriodo;
	}
	public int getNrFinalPeriodo(){
		return this.nrFinalPeriodo;
	}
	public void setNrVencimentoPeriodo(int nrVencimentoPeriodo){
		this.nrVencimentoPeriodo=nrVencimentoPeriodo;
	}
	public int getNrVencimentoPeriodo(){
		return this.nrVencimentoPeriodo;
	}
	public void setLgNaVenda(int lgNaVenda){
		this.lgNaVenda=lgNaVenda;
	}
	public int getLgNaVenda(){
		return this.lgNaVenda;
	}
	public void setLgVencimentoFixo(int lgVencimentoFixo){
		this.lgVencimentoFixo=lgVencimentoFixo;
	}
	public int getLgVencimentoFixo(){
		return this.lgVencimentoFixo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProgramaFatura: " +  getCdProgramaFatura();
		valueToString += ", idProgramaFatura: " +  getIdProgramaFatura();
		valueToString += ", stProgramaFatura: " +  getStProgramaFatura();
		valueToString += ", lgPadrao: " +  getLgPadrao();
		valueToString += ", nmProgramaFatura: " +  getNmProgramaFatura();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		valueToString += ", lgAposEntrega: " +  getLgAposEntrega();
		valueToString += ", lgProximoDiaUtil: " +  getLgProximoDiaUtil();
		valueToString += ", nrDiaFixo: " +  getNrDiaFixo();
		valueToString += ", qtDiasCarencia: " +  getQtDiasCarencia();
		valueToString += ", prDesconto: " +  getPrDesconto();
		valueToString += ", qtSuspenderApos: " +  getQtSuspenderApos();
		valueToString += ", lgPeriodo: " +  getLgPeriodo();
		valueToString += ", lgCobrarJuros: " +  getLgCobrarJuros();
		valueToString += ", prCobrarJuros: " +  getPrCobrarJuros();
		valueToString += ", lgProtestar: " +  getLgProtestar();
		valueToString += ", qtDiasProtesto: " +  getQtDiasProtesto();
		valueToString += ", txtNota: " +  getTxtNota();
		valueToString += ", nrInicioPeriodo: " +  getNrInicioPeriodo();
		valueToString += ", nrFinalPeriodo: " +  getNrFinalPeriodo();
		valueToString += ", nrVencimentoPeriodo: " +  getNrVencimentoPeriodo();
		valueToString += ", lgNaVenda: " +  getLgNaVenda();
		valueToString += ", lgVencimentoFixo: " +  getLgVencimentoFixo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProgramaFatura(getCdProgramaFatura(),
			getIdProgramaFatura(),
			getStProgramaFatura(),
			getLgPadrao(),
			getNmProgramaFatura(),
			getTxtDescricao(),
			getLgAposEntrega(),
			getLgProximoDiaUtil(),
			getNrDiaFixo(),
			getQtDiasCarencia(),
			getPrDesconto(),
			getQtSuspenderApos(),
			getLgPeriodo(),
			getLgCobrarJuros(),
			getPrCobrarJuros(),
			getLgProtestar(),
			getQtDiasProtesto(),
			getTxtNota(),
			getNrInicioPeriodo(),
			getNrFinalPeriodo(),
			getNrVencimentoPeriodo(),
			getLgNaVenda(),
			getLgVencimentoFixo());
	}

}