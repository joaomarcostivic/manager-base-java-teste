package com.tivic.manager.mob.ait;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.ait.relatorios.PageResponseRelatorioAIT;
import com.tivic.manager.mob.ait.relatorios.RelatorioAitSneDTO;

public class RelatorioAitSneDTOListBuilder {
	
    private int total;
    private List<RelatorioAitSneDTO> aits;
    private double totalValor;
    
    public RelatorioAitSneDTOListBuilder(List<RelatorioAitSneDTO> listAit, int total, double totalValor) throws SQLException {
        this.aits = new ArrayList<RelatorioAitSneDTO>(listAit);
        this.total = total;
        this.totalValor = totalValor;
    }
    
    public PageResponseRelatorioAIT<RelatorioAitSneDTO> build() throws IllegalArgumentException, Exception {        
        return new PageResponseRelatorioAIT<RelatorioAitSneDTO>(aits, this.total, this.totalValor);	
    }
    
}

