package com.tivic.manager.mob.lotes.movimento;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ServicoDetranDTO;

public interface IGeraMovimentoNotificacao {
	List<ServicoDetranDTO> gerarMovimentos(List<Ait> listAits, int cdUsuario) throws Exception;
}
