package com.tivic.manager.mob.pagamento;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitPagamento;

public class PagamentoDTO extends AitPagamento {

	int cdUsuario;
	String line;
	Ait ait;
	int cdAit;
	int cdPagamento;
	String nrControle;
	String idAit;

	public PagamentoDTO() {
		super();
	}

	public PagamentoDTO(int cdUsuario, String line, Ait ait, int cdAit, int cdPagamento, String nrControle, String idAit) {
		super();
		this.cdUsuario = cdUsuario;
		this.line = line;
		this.ait = ait;
		this.cdAit = cdAit;
		this.cdPagamento = cdPagamento;
		this.nrControle = nrControle;
		this.idAit = idAit;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public Ait getAit() {
		return ait;
	}

	public void setAit(Ait ait) {
		this.ait = ait;
	}

	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public int getCdPagamento() {
		return cdPagamento;
	}

	public void setCdPagamento(int cdPagamento) {
		this.cdPagamento = cdPagamento;
	}

	public String getNrControle() {
		return nrControle;
	}

	public void setNrControle(String nrControle) {
		this.nrControle = nrControle;
	}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
}