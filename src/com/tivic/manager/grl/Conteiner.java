package com.tivic.manager.grl;

public class Conteiner {

	private int cdConteiner;
	private int cdConteinerPai;
	private String nmConteiner;
	private int cdUsuario;
	private int cdEmpresa;
	private int cdTipoConteiner;
	private float vlCapacidade;
	private String idConteiner;

	public Conteiner(int cdConteiner,
			int cdConteinerPai,
			String nmConteiner,
			int cdUsuario,
			int cdEmpresa,
			int cdTipoConteiner,
			float vlCapacidade,
			String idConteiner){
		setCdConteiner(cdConteiner);
		setCdConteinerPai(cdConteinerPai);
		setNmConteiner(nmConteiner);
		setCdUsuario(cdUsuario);
		setCdEmpresa(cdEmpresa);
		setCdTipoConteiner(cdTipoConteiner);
		setVlCapacidade(vlCapacidade);
		setIdConteiner(idConteiner);
	}
	public void setCdConteiner(int cdConteiner){
		this.cdConteiner=cdConteiner;
	}
	public int getCdConteiner(){
		return this.cdConteiner;
	}
	public void setCdConteinerPai(int cdConteinerPai){
		this.cdConteinerPai=cdConteinerPai;
	}
	public int getCdConteinerPai(){
		return this.cdConteinerPai;
	}
	public void setNmConteiner(String nmConteiner){
		this.nmConteiner=nmConteiner;
	}
	public String getNmConteiner(){
		return this.nmConteiner;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdTipoConteiner(int cdTipoConteiner){
		this.cdTipoConteiner=cdTipoConteiner;
	}
	public int getCdTipoConteiner(){
		return this.cdTipoConteiner;
	}
	public void setVlCapacidade(float vlCapacidade){
		this.vlCapacidade=vlCapacidade;
	}
	public float getVlCapacidade(){
		return this.vlCapacidade;
	}
	public void setIdConteiner(String idConteiner){
		this.idConteiner=idConteiner;
	}
	public String getIdConteiner(){
		return this.idConteiner;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConteiner: " +  getCdConteiner();
		valueToString += ", cdConteinerPai: " +  getCdConteinerPai();
		valueToString += ", nmConteiner: " +  getNmConteiner();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdTipoConteiner: " +  getCdTipoConteiner();
		valueToString += ", vlCapacidade: " +  getVlCapacidade();
		valueToString += ", idConteiner: " +  getIdConteiner();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Conteiner(getCdConteiner(),
			getCdConteinerPai(),
			getNmConteiner(),
			getCdUsuario(),
			getCdEmpresa(),
			getCdTipoConteiner(),
			getVlCapacidade(),
			getIdConteiner());
	}

}
