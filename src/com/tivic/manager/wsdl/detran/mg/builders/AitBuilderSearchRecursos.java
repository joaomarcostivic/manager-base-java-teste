package com.tivic.manager.wsdl.detran.mg.builders;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.wsdl.detran.mg.consultarrecursos.ConsultarRecursosDadosRetorno;
import com.tivic.manager.wsdl.interfaces.DadosRetorno;

public class AitBuilderSearchRecursos extends AitBuilderSearchProdemge implements IAitBuilderSearchProdemge {

	@Override
	public void build(Ait ait, DadosRetorno dadosRetorno) throws Exception {
		ConsultarRecursosDadosRetorno consultarRecursosDadosRetorno = (ConsultarRecursosDadosRetorno) dadosRetorno;
		adicionarDefesaPrevia(consultarRecursosDadosRetorno, ait);
		adicionarDefesaResultado(consultarRecursosDadosRetorno, ait);
		adicionarRecursoJari(consultarRecursosDadosRetorno, ait);
		adicionarResultadoJari(consultarRecursosDadosRetorno, ait);
		adicionarRecursoCetran(consultarRecursosDadosRetorno, ait);
		adicionarResultadoCetran(consultarRecursosDadosRetorno, ait);
	}
	
	private void adicionarDefesaPrevia(ConsultarRecursosDadosRetorno consultarRecursosDadosRetorno, Ait ait) {
		if(consultarRecursosDadosRetorno.getNumeroProcessoDefesa() != null && !consultarRecursosDadosRetorno.getNumeroProcessoDefesa().equals("")) {
			boolean encontradoDefesaPrevia = false;
			for(AitMovimento aitMovimento : ait.getMovimentos()) {
				if(aitMovimento.getTpStatus() == AitMovimentoServices.DEFESA_PREVIA) {
					aitMovimento.setNrProcesso(consultarRecursosDadosRetorno.getNumeroProcessoDefesa());
					encontradoDefesaPrevia = true;
					break;
				}
			}
			
			if(!encontradoDefesaPrevia) {
				AitMovimento aitMovimento = new AitMovimento();
				aitMovimento.setTpStatus(AitMovimentoServices.DEFESA_PREVIA);
				aitMovimento.setNrProcesso(consultarRecursosDadosRetorno.getNumeroProcessoDefesa());
				aitMovimento.setDtMovimento(consultarRecursosDadosRetorno.getDataEntradaDefesa());
				aitMovimento.setLgEnviadoDetran(1);
				aitMovimento.setDtRegistroDetran(consultarRecursosDadosRetorno.getDataEntradaDefesa());
				ait.getMovimentos().add(aitMovimento);
			}
		}
	}
	
	private void adicionarDefesaResultado(ConsultarRecursosDadosRetorno consultarRecursosDadosRetorno, Ait ait) {
		if(consultarRecursosDadosRetorno.getDataEncerramentoDefesa() != null) {
			boolean encontradoDefesaResultado = false;
			for(AitMovimento aitMovimento : ait.getMovimentos()) {
				if(aitMovimento.getTpStatus() == AitMovimentoServices.DEFESA_DEFERIDA || aitMovimento.getTpStatus() == AitMovimentoServices.DEFESA_INDEFERIDA) {
					aitMovimento.setNrProcesso(consultarRecursosDadosRetorno.getNumeroProcessoDefesa());
					encontradoDefesaResultado = true;
					break;
				}
			}
			
			if(!encontradoDefesaResultado) {
				AitMovimento aitMovimento = new AitMovimento();
				aitMovimento.setTpStatus((consultarRecursosDadosRetorno.getDecisaoDefesa().equals("NAO ACOLHIDA") ? AitMovimentoServices.DEFESA_INDEFERIDA : AitMovimentoServices.DEFESA_DEFERIDA));
				aitMovimento.setNrProcesso(consultarRecursosDadosRetorno.getNumeroProcessoDefesa());
				aitMovimento.setDtMovimento(consultarRecursosDadosRetorno.getDataEncerramentoDefesa());
				aitMovimento.setLgEnviadoDetran(1);
				aitMovimento.setDtRegistroDetran(consultarRecursosDadosRetorno.getDataEncerramentoDefesa());
				
				ait.setDtResultadoDefesa(consultarRecursosDadosRetorno.getDataEncerramentoDefesa());
				ait.getMovimentos().add(aitMovimento);
			}
		}
	}

	private void adicionarRecursoJari(ConsultarRecursosDadosRetorno consultarRecursosDadosRetorno, Ait ait) {
		if(consultarRecursosDadosRetorno.getNumeroProcessoRecurso() != null && !consultarRecursosDadosRetorno.getNumeroProcessoRecurso().equals("")) {
			boolean encontradoDefesaRecurso = false;
			for(AitMovimento aitMovimento : ait.getMovimentos()) {
				if(aitMovimento.getTpStatus() == AitMovimentoServices.RECURSO_JARI) {
					aitMovimento.setNrProcesso(consultarRecursosDadosRetorno.getNumeroProcessoRecurso());
					encontradoDefesaRecurso = true;
					break;
				}
			}
			
			if(!encontradoDefesaRecurso) {
				AitMovimento aitMovimento = new AitMovimento();
				aitMovimento.setTpStatus(AitMovimentoServices.RECURSO_JARI);
				aitMovimento.setNrProcesso(consultarRecursosDadosRetorno.getNumeroProcessoRecurso());
				aitMovimento.setDtMovimento(consultarRecursosDadosRetorno.getDataEntradaRecurso());
				aitMovimento.setLgEnviadoDetran(1);
				aitMovimento.setDtRegistroDetran(consultarRecursosDadosRetorno.getDataEntradaRecurso());
				ait.getMovimentos().add(aitMovimento);
			}
		}
	}

	private void adicionarResultadoJari(ConsultarRecursosDadosRetorno consultarRecursosDadosRetorno, Ait ait) {
		if(consultarRecursosDadosRetorno.getDataEncerramentoRecurso() != null) {
			boolean encontradoDefesaRecurso = false;
			for(AitMovimento aitMovimento : ait.getMovimentos()) {
				if(aitMovimento.getTpStatus() == AitMovimentoServices.JARI_SEM_PROVIMENTO || aitMovimento.getTpStatus() == AitMovimentoServices.JARI_COM_PROVIMENTO) {
					aitMovimento.setNrProcesso(consultarRecursosDadosRetorno.getNumeroProcessoRecurso());
					encontradoDefesaRecurso = true;
					break;
				}
			}
			
			if(!encontradoDefesaRecurso) {
				AitMovimento aitMovimento = new AitMovimento();
				aitMovimento.setTpStatus((consultarRecursosDadosRetorno.getDecisaoRecurso().equals("INDEFERIDO") ? AitMovimentoServices.JARI_SEM_PROVIMENTO : AitMovimentoServices.JARI_COM_PROVIMENTO));
				aitMovimento.setNrProcesso(consultarRecursosDadosRetorno.getNumeroProcessoRecurso());
				aitMovimento.setDtMovimento(consultarRecursosDadosRetorno.getDataEncerramentoRecurso());
				aitMovimento.setLgEnviadoDetran(1);
				aitMovimento.setDtRegistroDetran(consultarRecursosDadosRetorno.getDataEncerramentoRecurso());
				
				ait.setDtResultadoJari(consultarRecursosDadosRetorno.getDataEncerramentoRecurso());
				ait.getMovimentos().add(aitMovimento);
			}
		}
	}

	private void adicionarRecursoCetran(ConsultarRecursosDadosRetorno consultarRecursosDadosRetorno, Ait ait) {
		if(consultarRecursosDadosRetorno.getNumeroProcessoCetran() != null && !consultarRecursosDadosRetorno.getNumeroProcessoCetran().equals("")) {
			boolean encontradoDefesaRecurso = false;
			for(AitMovimento aitMovimento : ait.getMovimentos()) {
				if(aitMovimento.getTpStatus() == AitMovimentoServices.RECURSO_CETRAN) {
					aitMovimento.setNrProcesso(consultarRecursosDadosRetorno.getNumeroProcessoCetran());
					encontradoDefesaRecurso = true;
					break;
				}
			}
			
			if(!encontradoDefesaRecurso) {
				AitMovimento aitMovimento = new AitMovimento();
				aitMovimento.setTpStatus(AitMovimentoServices.RECURSO_CETRAN);
				aitMovimento.setNrProcesso(consultarRecursosDadosRetorno.getNumeroProcessoCetran());
				aitMovimento.setDtMovimento(consultarRecursosDadosRetorno.getDataEntradaCetran());
				aitMovimento.setLgEnviadoDetran(1);
				aitMovimento.setDtRegistroDetran(consultarRecursosDadosRetorno.getDataEntradaCetran());
				ait.getMovimentos().add(aitMovimento);
			}
		}
	}

	private void adicionarResultadoCetran(ConsultarRecursosDadosRetorno consultarRecursosDadosRetorno, Ait ait) {
		if(consultarRecursosDadosRetorno.getDataEncerramentoCetran() != null) {
			boolean encontradoDefesaRecurso = false;
			for(AitMovimento aitMovimento : ait.getMovimentos()) {
				if(aitMovimento.getTpStatus() == AitMovimentoServices.CETRAN_DEFERIDO || aitMovimento.getTpStatus() == AitMovimentoServices.CETRAN_INDEFERIDO) {
					aitMovimento.setNrProcesso(consultarRecursosDadosRetorno.getNumeroProcessoCetran());
					encontradoDefesaRecurso = true;
					break;
				}
			}
			
			if(!encontradoDefesaRecurso) {
				AitMovimento aitMovimento = new AitMovimento();
				aitMovimento.setTpStatus((consultarRecursosDadosRetorno.getDecisaoCetran().equals("INDEFERIDO") ? AitMovimentoServices.CETRAN_INDEFERIDO : AitMovimentoServices.CETRAN_DEFERIDO));
				aitMovimento.setNrProcesso(consultarRecursosDadosRetorno.getNumeroProcessoCetran());
				aitMovimento.setDtMovimento(consultarRecursosDadosRetorno.getDataEncerramentoCetran());
				aitMovimento.setLgEnviadoDetran(1);
				aitMovimento.setDtRegistroDetran(consultarRecursosDadosRetorno.getDataEncerramentoCetran());
				
				ait.setDtResultadoCetran(consultarRecursosDadosRetorno.getDataEncerramentoCetran());
				ait.getMovimentos().add(aitMovimento);
			}
		}
	}
	
}
