package com.tivic.manager.mob.ecarta.dtos;

public class StatusLeituraArquivoRetornoDTO {
	private int cdLoteImpressao;
	private int stLoteImpressao;
	private String txtStLoteImpressao;

	public String getTxtStLoteImpressao() {
		return txtStLoteImpressao;
	}

	public void setTxtStLoteImpressao(String txtStLoteImpressao) {
		this.txtStLoteImpressao = txtStLoteImpressao;
	}

	public int getCdLoteImpressao() {
		return cdLoteImpressao;
	}

	public void setCdLoteImpressao(int cdLoteImpressao) {
		this.cdLoteImpressao = cdLoteImpressao;
	}

	public int getStLoteImpressao() {
		return stLoteImpressao;
	}

	public void setStLoteImpressao(int stLoteImpressao) {
		this.stLoteImpressao = stLoteImpressao;
	}
}
