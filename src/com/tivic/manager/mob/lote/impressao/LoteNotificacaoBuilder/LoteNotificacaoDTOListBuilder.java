package com.tivic.manager.mob.lote.impressao.LoteNotificacaoBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.lote.impressao.LoteNotificacaoDTO;
import com.tivic.manager.util.pagination.PagedResponse;

public class LoteNotificacaoDTOListBuilder {
	private int total;
    private List<LoteNotificacaoDTO> lotes;
    
    public LoteNotificacaoDTOListBuilder(List<LoteNotificacaoDTO> list, int total) throws SQLException {
        this.lotes = new ArrayList<LoteNotificacaoDTO>(list);
        this.total = total;
    }
    
    public PagedResponse<LoteNotificacaoDTO> build() throws IllegalArgumentException, Exception {        
        return new PagedResponse<LoteNotificacaoDTO>(lotes, this.total);	
    }
}
