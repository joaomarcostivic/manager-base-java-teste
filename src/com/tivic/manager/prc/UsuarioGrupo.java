package com.tivic.manager.prc;

public class UsuarioGrupo {

	private int cdGrupoProcesso;
	private int cdUsuario;

	public UsuarioGrupo(){ }

	public UsuarioGrupo(int cdGrupoProcesso,
			int cdUsuario){
		setCdGrupoProcesso(cdGrupoProcesso);
		setCdUsuario(cdUsuario);
	}
	public void setCdGrupoProcesso(int cdGrupoProcesso){
		this.cdGrupoProcesso=cdGrupoProcesso;
	}
	public int getCdGrupoProcesso(){
		return this.cdGrupoProcesso;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupoProcesso: " +  getCdGrupoProcesso();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new UsuarioGrupo(getCdGrupoProcesso(),
			getCdUsuario());
	}

}
