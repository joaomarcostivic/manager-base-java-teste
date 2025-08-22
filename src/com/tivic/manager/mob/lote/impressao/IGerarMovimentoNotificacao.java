package com.tivic.manager.mob.lote.impressao;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ServicoDetranDTO;

public interface IGerarMovimentoNotificacao {
	List<ServicoDetranDTO> gerarMovimentos(List<Ait> listAits, int cdUsuario) throws Exception;
}
