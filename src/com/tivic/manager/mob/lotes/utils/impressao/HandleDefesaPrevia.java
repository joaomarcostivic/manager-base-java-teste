package com.tivic.manager.mob.lotes.utils.impressao;

import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;

public class HandleDefesaPrevia {
	
	public static void handle(Notificacao dadosNotificacao) throws Exception {
		AitMovimento movimentoDefesaIndeferida = verificarDefesaIndeferida(dadosNotificacao.getCdAit());
		if (movimentoDefesaIndeferida.getNrProcesso() != null) {
			String defesaPreviaIndeferida = ParametroServices.getValorOfParametroAsString("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", "");
			String coodernador = ParametroServices.getValorOfParametroAsString("MOB_NOME_COORDENADOR_IMPRESSAO", "");
			String cidade = ParametroServices.getValorOfParametroAsString("NM_CIDADE", "");
			String proprietario =  dadosNotificacao.getNmProprietario() != null ? dadosNotificacao.getNmProprietario() : " ";
			String nrAit = dadosNotificacao.getIdAit() != null ? dadosNotificacao.getIdAit() : " ";
			String nrRenavan = dadosNotificacao.getNrRenavan() != null ? dadosNotificacao.getNrRenavan() : " ";
			String nrPlaca = dadosNotificacao.getNrPlaca() != null ? dadosNotificacao.getNrPlaca() : " ";
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#PROPRIETARIO>", proprietario);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr PROCESSO>", movimentoDefesaIndeferida.getNrProcesso());
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#COORDENADOR>", coodernador);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr AIT>", nrAit);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr RENAVAN>", nrRenavan);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr PLACA>", nrPlaca);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#CIDADE>", cidade);
			dadosNotificacao.setDefesaPreviaIndeferida(defesaPreviaIndeferida);
		}
	}
	
	private static AitMovimento verificarDefesaIndeferida(int cdAit) throws Exception {
		List<AitMovimento> listDefesas = AitMovimentoServices.getAllDefesas(cdAit);
		for(AitMovimento movimentoDefesaIndeferida : listDefesas) {
			if(movimentoDefesaIndeferida.getTpStatus() == TipoStatusEnum.DEFESA_INDEFERIDA.getKey() ||
				movimentoDefesaIndeferida.getTpStatus() == TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey()) {
				return movimentoDefesaIndeferida;
			}
		}
		return new AitMovimento();
	}
}
