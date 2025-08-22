package com.tivic.manager.ptc;

public class Acao {

	private int cdAcao;
	private int tpAcao;
	private int lgAutomatico;
	private int cdSetor;
	private int cdDecisao;
	private int cdFluxo;
	private int cdTipoPendencia;
	private int cdTipoOcorrencia;
	private int cdFase;

	public Acao(){ }

	public Acao(int cdAcao,
			int tpAcao,
			int lgAutomatico,
			int cdSetor,
			int cdDecisao,
			int cdFluxo,
			int cdTipoPendencia,
			int cdTipoOcorrencia,
			int cdFase){
		setCdAcao(cdAcao);
		setTpAcao(tpAcao);
		setLgAutomatico(lgAutomatico);
		setCdSetor(cdSetor);
		setCdDecisao(cdDecisao);
		setCdFluxo(cdFluxo);
		setCdTipoPendencia(cdTipoPendencia);
		setCdTipoOcorrencia(cdTipoOcorrencia);
		setCdFase(cdFase);
	}
	public void setCdAcao(int cdAcao){
		this.cdAcao=cdAcao;
	}
	public int getCdAcao(){
		return this.cdAcao;
	}
	public void setTpAcao(int tpAcao){
		this.tpAcao=tpAcao;
	}
	public int getTpAcao(){
		return this.tpAcao;
	}
	public void setLgAutomatico(int lgAutomatico){
		this.lgAutomatico=lgAutomatico;
	}
	public int getLgAutomatico(){
		return this.lgAutomatico;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setCdDecisao(int cdDecisao){
		this.cdDecisao=cdDecisao;
	}
	public int getCdDecisao(){
		return this.cdDecisao;
	}
	public void setCdFluxo(int cdFluxo){
		this.cdFluxo=cdFluxo;
	}
	public int getCdFluxo(){
		return this.cdFluxo;
	}
	public void setCdTipoPendencia(int cdTipoPendencia){
		this.cdTipoPendencia=cdTipoPendencia;
	}
	public int getCdTipoPendencia(){
		return this.cdTipoPendencia;
	}
	public void setCdTipoOcorrencia(int cdTipoOcorrencia){
		this.cdTipoOcorrencia=cdTipoOcorrencia;
	}
	public int getCdTipoOcorrencia(){
		return this.cdTipoOcorrencia;
	}
	public void setCdFase(int cdFase){
		this.cdFase=cdFase;
	}
	public int getCdFase(){
		return this.cdFase;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAcao: " +  getCdAcao();
		valueToString += ", tpAcao: " +  getTpAcao();
		valueToString += ", lgAutomatico: " +  getLgAutomatico();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", cdDecisao: " +  getCdDecisao();
		valueToString += ", cdFluxo: " +  getCdFluxo();
		valueToString += ", cdTipoPendencia: " +  getCdTipoPendencia();
		valueToString += ", cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		valueToString += ", cdFase: " +  getCdFase();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Acao(getCdAcao(),
			getTpAcao(),
			getLgAutomatico(),
			getCdSetor(),
			getCdDecisao(),
			getCdFluxo(),
			getCdTipoPendencia(),
			getCdTipoOcorrencia(),
			getCdFase());
	}

}
