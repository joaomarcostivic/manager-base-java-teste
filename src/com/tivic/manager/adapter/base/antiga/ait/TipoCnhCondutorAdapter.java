package com.tivic.manager.adapter.base.antiga.ait;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.adapter.base.antiga.ait.AitOld;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.TipoCnhEnum;
import com.tivic.manager.ptc.protocolos.mg.validators.fici.TipoModeloCnhEnum;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;

public class TipoCnhCondutorAdapter {

    private static final Map<Integer, Integer> NOVA_PARA_ANTIGA = new HashMap<>();
    private static final Map<Integer, Integer> ANTIGA_PARA_NOVA = new HashMap<>();

    static {
        NOVA_PARA_ANTIGA.put(TabelasAuxiliaresMG.TP_CNH_PGU, TipoModeloCnhEnum.PGU.getKey());
        NOVA_PARA_ANTIGA.put(TabelasAuxiliaresMG.TP_CNH_RENACH, TipoModeloCnhEnum.RENACH.getKey());
        NOVA_PARA_ANTIGA.put(TabelasAuxiliaresMG.TP_CNH_HABILITACAO_ESTRANGEIRA, TipoModeloCnhEnum.HABILITACAO_ESTRANGEIRA.getKey());
        NOVA_PARA_ANTIGA.put(TabelasAuxiliaresMG.TP_CNH_NAO_HABILITADO, TipoModeloCnhEnum.NAO_HABILITADO.getKey());

        ANTIGA_PARA_NOVA.put(TipoModeloCnhEnum.PGU.getKey(), TabelasAuxiliaresMG.TP_CNH_PGU);
        ANTIGA_PARA_NOVA.put(TipoModeloCnhEnum.RENACH.getKey(), TabelasAuxiliaresMG.TP_CNH_RENACH);
        ANTIGA_PARA_NOVA.put(TipoModeloCnhEnum.HABILITACAO_ESTRANGEIRA.getKey(), TabelasAuxiliaresMG.TP_CNH_HABILITACAO_ESTRANGEIRA);
        ANTIGA_PARA_NOVA.put(TipoModeloCnhEnum.NAO_HABILITADO.getKey(), TabelasAuxiliaresMG.TP_CNH_NAO_HABILITADO);
    }

    public static Integer convertToOld(Ait ait) {
        int tipo = ait.getTpCnhCondutor();
        if (tipo == 0 || tipo == TipoCnhEnum.NAO_INFORMADO.getKey()) {
            return null;
        }
        if (!NOVA_PARA_ANTIGA.containsKey(tipo)) {
            throw new IllegalArgumentException("Tipo de CNH não reconhecido");
        }
        return NOVA_PARA_ANTIGA.get(tipo);
    }

    public static Integer convertToNew(AitOld aitOld) {
        Integer tipo = aitOld.getTpCnhCondutor();
        if (tipo == null) {
            return TipoCnhEnum.NAO_INFORMADO.getKey();
        }
        if (!ANTIGA_PARA_NOVA.containsKey(tipo)) {
            throw new IllegalArgumentException("Tipo de CNH não reconhecido");
        }
        return ANTIGA_PARA_NOVA.get(tipo);
    }
}