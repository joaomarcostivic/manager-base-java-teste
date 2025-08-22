package com.tivic.manager.mob.lotes.builders.impressao;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.sol.util.date.DateUtil;

public class ServicoDetranDTOFactory {
	public static ServicoDetranDTO create(Ait ait, String mensagemRetorno) {
        ServicoDetranDTO servicoDetranDTO = new ServicoDetranDTO();
        servicoDetranDTO.setNrAit(ait.getIdAit());
        servicoDetranDTO.setDataMovimento(DateUtil.getDataAtual());
        servicoDetranDTO.setMensagemRetorno(mensagemRetorno);
        return servicoDetranDTO;
    }
}
