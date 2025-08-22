package com.tivic.manager.mob;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;


public class AitReaberturaPrazo {
    
    public void reabrirPrazo(AitMovimento movimento, Connection connect) {
        boolean isConnNull = (connect == null);
        
        try {
            if(isConnNull)
                connect = Conexao.conectar();
            
            int cdAit = movimento.getCdAit();
            Ait ait = AitDAO.get(cdAit);
            
            criarRecursos(ait, movimento);
            
        } catch(Exception ex) {
            ex.printStackTrace(System.out);
        }
    }
    
    private void criarRecursos(Ait ait, AitMovimento movimento) throws Exception {
        List<AitMovimento> movimentos = new ArrayList<AitMovimento>();
        
        movimentos.add(criarReabertura(movimento, AitMovimentoServices.REABERTURA_FICI));
        movimentos.add(criarReabertura(movimento, AitMovimentoServices.REABERTURA_DEFESA));
        movimentos.add(criarReabertura(movimento, AitMovimentoServices.REABERTURA_JARI));
        
        ServicoDetranServices services = ServicoDetranServicesFactory.gerarServico();
        
        try {
            services.remessa(movimentos);
        } catch(Exception ex) {
            ex.printStackTrace(System.out);
        }
    }
    
    protected AitMovimento criarReabertura(AitMovimento movimento, int tpReabertura) {
        AitMovimento _nmov = (AitMovimento) movimento.clone();
        _nmov.setCdMovimento(0);
        _nmov.setTpStatus(tpReabertura);
        _nmov.setDtMovimento(new GregorianCalendar());
        AitMovimento _ins = (AitMovimento) (AitMovimentoServices.save(_nmov)).getObjects().get("AITMOVIMENTO");
        _ins.setNrMovimento(_ins.getCdMovimento());
        AitMovimentoServices.save(_ins);
        return _nmov;
    }

}
