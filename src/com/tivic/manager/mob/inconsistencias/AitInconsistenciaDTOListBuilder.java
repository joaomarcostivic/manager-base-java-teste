package com.tivic.manager.mob.inconsistencias;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;

public class AitInconsistenciaDTOListBuilder {

	private int total;
    private List<AitInconsistenciaDTO> aits;
    
    public AitInconsistenciaDTOListBuilder(List<AitInconsistenciaDTO> list, int total) throws SQLException {
        this.aits = new ArrayList<AitInconsistenciaDTO>(list);
        this.total = total;
    }
    
    public PagedResponse<AitInconsistenciaDTO> build() throws IllegalArgumentException, Exception {        
        return new PagedResponse<AitInconsistenciaDTO>(aits, this.total);	
    }
    
}
