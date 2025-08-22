package com.tivic.manager.agd;

public class Usuario extends com.tivic.manager.seg.Usuario {

	private int nrMinutosUpdate;
	private byte[] blbAudio;

	public Usuario(int cdUsuario,
			int cdPessoa,
			int cdPerguntaSecreta,
			String nmLogin,
			String nmSenha,
			int tpUsuario,
			String nmRespostaSecreta,
			int stUsuario,
			int nrMinutosUpdate,
			byte[] blbAudio){
		super(cdUsuario,
			cdPessoa,
			cdPerguntaSecreta,
			nmLogin,
			nmSenha,
			tpUsuario,
			nmRespostaSecreta,
			stUsuario);
		setNrMinutosUpdate(nrMinutosUpdate);
		setBlbAudio(blbAudio);
	}
	public void setNrMinutosUpdate(int nrMinutosUpdate){
		this.nrMinutosUpdate=nrMinutosUpdate;
	}
	public int getNrMinutosUpdate(){
		return this.nrMinutosUpdate;
	}
	public void setBlbAudio(byte[] blbAudio){
		this.blbAudio=blbAudio;
	}
	public byte[] getBlbAudio(){
		return this.blbAudio;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdUsuario: " +  getCdUsuario();
		valueToString += ", nrMinutosUpdate: " +  getNrMinutosUpdate();
		valueToString += ", blbAudio: " +  getBlbAudio();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Usuario(getCdUsuario(),
			getCdPessoa(),
			getCdPerguntaSecreta(),
			getNmLogin(),
			getNmSenha(),
			getTpUsuario(),
			getNmRespostaSecreta(),
			getStUsuario(),
			getNrMinutosUpdate(),
			getBlbAudio());
	}

}
