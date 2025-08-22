package com.tivic.manager.mob.lote.impressao.ServicoDetranDTOBuilder;

import java.util.GregorianCalendar;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ServicoDetranDTO;

public class ServicoDetranDTOBuilder {
	public ServicoDetranDTO build(Ait ait, String menssagemRetorno) {
		ServicoDetranDTO servicoDetranDTOError = new ServicoDetranDTO();
		servicoDetranDTOError.setNrAit(ait.getIdAit());
		servicoDetranDTOError.setDataMovimento(new GregorianCalendar());
		servicoDetranDTOError.setMensagemRetorno(menssagemRetorno);
		return servicoDetranDTOError;
	}
}
