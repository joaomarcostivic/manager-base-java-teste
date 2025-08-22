package com.tivic.manager.mob.ait.circuitoait;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.sol.cdi.BeansFactory;


public class CircuitoAit {
	List<CircuitoAitItem> itensAit;
	private IAitMovimentoService aitMovimentoService;
	
	public CircuitoAit() throws Exception{
		
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		
		this.itensAit = new ArrayList<CircuitoAitItem>();
		itensAit.add(new AdvertenciaDefesa());
		itensAit.add(new AdvertenciaDefesaDeferida());
		itensAit.add(new AdvertenciaDefesaIndeferida());
		itensAit.add(new ApresentacaoCondutor());
		itensAit.add(new ApresentacaoCondutorDeferido());
		itensAit.add(new ApresentacaoCondutorIndeferido());
		itensAit.add(new ArNai());
		itensAit.add(new ArNip());
		itensAit.add(new CancelarCadastro());
		itensAit.add(new CancelarCetranDeferido());
		itensAit.add(new CancelarCetranIndeferido());
		itensAit.add(new CancelarDefesaDeferida());
		itensAit.add(new CancelarDefesaIndeferida());
		itensAit.add(new CancelarDefesaPrevia());
		itensAit.add(new CancelarJariComProvimento());
		itensAit.add(new CancelarJariSemProvimento());
		itensAit.add(new CancelarNip());
		itensAit.add(new CancelarPublicacaoResultadoJari());
		itensAit.add(new CancelarRecursoCetran());
		itensAit.add(new CancelarRecursoJari());
		itensAit.add(new CancelarRegistroDetran());
		itensAit.add(new CetranDeferido());
		itensAit.add(new CetranIndeferido());
		itensAit.add(new DefesaDeferida());
		itensAit.add(new DefesaIndeferida());
		itensAit.add(new DefesaPrevia());
		itensAit.add(new DevolverPagamento());
		itensAit.add(new SuspensaoDividaAtiva());
		itensAit.add(new CancelamentoDividaAtiva());
		itensAit.add(new EnviadoAoDetran());
		itensAit.add(new FimPrazoDefesa());
		itensAit.add(new JariComProvimento());
		itensAit.add(new JariSemProvimento());
		itensAit.add(new NaiEnviado());
		itensAit.add(new NipEnviado());		
		itensAit.add(new PagamentoMulta());
		itensAit.add(new PublicacaoNai());
		itensAit.add(new PublicacaoNip());
		itensAit.add(new PublicacaoResultadoJari());
		itensAit.add(new RecursoCetran());
		itensAit.add(new RecursoJari());
		itensAit.add(new RegistroInfracao());
		itensAit.add(new CancelamentoAutuacao());
		itensAit.add(new SuspenderPenalidade());
		itensAit.add(new AlteraPrazoDefesa());
		itensAit.add(new CancelarPagamento());
		itensAit.add(new NotificacaoPenalidadeAdvertencia());
		itensAit.add(new BaixaDesvinculadaLeilao());
		itensAit.add(new CancelamentoApresentacaoCondutor());
		itensAit.add(new DadosCorreioNa());
		itensAit.add(new DadosCorreioNp());
		itensAit.add(new NicEnviado());
	}
	
	public CircuitoAitItem getItem(Integer tpStatus) throws Exception {
		for(CircuitoAitItem circuitoAitItem : this.itensAit) {
			if(circuitoAitItem.getTpStatus() == tpStatus)
				return circuitoAitItem;
		}
		
		throw new Exception("Nenhum item encontrado para o movimento de status " + tpStatus);
	}
	
	public void validarEnvioDetran(AitMovimento aitMovimento) throws Exception{
		try {
			CircuitoAitItem circuitoAitItem = getItem(aitMovimento.getTpStatus());
			verificarProprioMovimento(circuitoAitItem, aitMovimento.getCdAit());
			verificarExigidos(circuitoAitItem, aitMovimento.getCdAit());
			verificarImpeditivos(circuitoAitItem, aitMovimento.getCdAit());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void verificarExigidos(CircuitoAitItem circuitoAitItem, Integer cdAit) throws Exception {
		CircuitoAitItemPrazo itemExigido = circuitoAitItem.getItemExigido();
		if(itemExigido == null)
			return;
		List<AitMovimento> movimentos = this.aitMovimentoService.getByAit(cdAit);
		boolean exigidoLancado = false;
		boolean cancelamentoExigidoLancado = false;
		for(AitMovimento movimento : movimentos) {
			if(movimento.getTpStatus() == itemExigido.getCirculoAitItem() && movimento.getLgEnviadoDetran() == 1 && movimento.getNrErro() == null) {
				exigidoLancado = true;
			}
			else if(movimento.getTpStatus() == getItem(itemExigido.getCirculoAitItem()).getItemCancelamento() && movimento.getLgEnviadoDetran() == 1 && movimento.getNrErro() == null) {
				cancelamentoExigidoLancado = true;
			}
		}
		if(itemExigido!= null && !exigidoLancado || (exigidoLancado && cancelamentoExigidoLancado)) {
			throw new Exception("Para lançar " + TipoStatusEnum.valueOf(circuitoAitItem.getTpStatus()) + " é necessário ter um movimento " + TipoStatusEnum.valueOf(itemExigido.getCirculoAitItem()) + " lançado no detran");
		}
	}
	
	private void verificarImpeditivos(CircuitoAitItem circuitoAitItem, Integer cdAit) throws Exception {
		List<AitMovimento> movimentos = this.aitMovimentoService.getByAit(cdAit);
		List<Integer> impeditivos = circuitoAitItem.getItensImpeditivos();
		for(Integer tpStatusImpeditivo : impeditivos) {
			boolean impeditivoLancado = false;
			boolean cancelamentoImpeditivoLancado = false;
			for(AitMovimento movimento : movimentos) {
				if(movimento.getTpStatus() == tpStatusImpeditivo && movimento.getLgEnviadoDetran() == 1 && movimento.getNrErro() == null) {
					impeditivoLancado = true;
				}
				else if(movimento.getTpStatus() == getItem(tpStatusImpeditivo).getItemCancelamento() && movimento.getLgEnviadoDetran() == 1 && movimento.getNrErro() == null) {
					cancelamentoImpeditivoLancado = true;
				}
			}
			
			if(impeditivoLancado && !cancelamentoImpeditivoLancado) {
				throw new Exception("Para lançar " + TipoStatusEnum.valueOf(circuitoAitItem.getTpStatus()) + " não pode ter o movimento " + TipoStatusEnum.valueOf(tpStatusImpeditivo) + " lançado no detran");
			}
		}
	}

	private void verificarProprioMovimento(CircuitoAitItem circuitoAitItem, int cdAit) throws Exception {
		List<AitMovimento> movimentos = this.aitMovimentoService.getByAit(cdAit);
		Integer tpStatusProprioMovimento = circuitoAitItem.getTpStatus();
		boolean proprioLancado = false;
		boolean cancelamentoProprioLancado = false;
		for(AitMovimento movimento : movimentos) {
			if(movimento.getTpStatus() == tpStatusProprioMovimento && movimento.getLgEnviadoDetran() == 1 && movimento.getNrErro() == null) {
				proprioLancado = true;
			}
			else if(movimento.getTpStatus() == circuitoAitItem.getItemCancelamento() && movimento.getLgEnviadoDetran() == 1 && movimento.getNrErro() == null) {
				cancelamentoProprioLancado = true;
			}
		}
		
		if(proprioLancado && !cancelamentoProprioLancado) {
			throw new Exception("O movimento " + TipoStatusEnum.valueOf(tpStatusProprioMovimento) + " já foi lançado no detran");
		}
	}

}
