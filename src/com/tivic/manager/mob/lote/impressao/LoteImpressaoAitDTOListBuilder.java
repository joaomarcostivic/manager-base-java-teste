package com.tivic.manager.mob.lote.impressao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tivic.manager.util.pagination.PagedResponse;

public class LoteImpressaoAitDTOListBuilder {
	
    private int total;
    private List<LoteImpressaoAitDTO> aits;
    
    public LoteImpressaoAitDTOListBuilder(List<LoteImpressaoAitDTO> list, int total) throws SQLException {
        this.aits = new ArrayList<LoteImpressaoAitDTO>(list);
        this.total = total;
    }
    
    public PagedResponse<LoteImpressaoAitDTO> build() throws IllegalArgumentException, Exception {        
        return new PagedResponse<LoteImpressaoAitDTO>(aits, this.total);	

    }
    
}
