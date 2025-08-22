package com.tivic.manager.str;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.ArquivoDTO;

public class ProcessoDTO extends Processo {

	private List<ArquivoDTO> arquivos;
	private ProcessoRequerente requerente;
	public ProcessoDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProcessoDTO(int cdProcesso, int cdAit, String nrProcesso, GregorianCalendar dtProcesso, String dsParecer,
			int stProcesso, int tpProcesso, int stImpressao, int tpOrigem, int cdEstadoOrigem, String nrOrgaoOrigem,
			int cdUsuario) {
		super(cdProcesso, cdAit, nrProcesso, dtProcesso, dsParecer, stProcesso, tpProcesso, stImpressao, tpOrigem,
				cdEstadoOrigem, nrOrgaoOrigem, cdUsuario);
	}
	public List<ArquivoDTO> getArquivos() {
		return arquivos;
	}
	public void setArquivos(List<ArquivoDTO> arquivos) {
		this.arquivos = arquivos;
	}
	public ProcessoRequerente getRequerente() {
		return requerente;
	}
	public void setRequerente(ProcessoRequerente requerente) {
		this.requerente = requerente;
	}
	
	
	
	
}
