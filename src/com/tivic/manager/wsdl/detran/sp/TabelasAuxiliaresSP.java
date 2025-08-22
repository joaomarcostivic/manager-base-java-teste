package com.tivic.manager.wsdl.detran.sp;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.lang.NotImplementedException;

import com.tivic.manager.mob.AitServices;
import com.tivic.manager.wsdl.interfaces.TabelasAuxiliares;

public class TabelasAuxiliaresSP implements TabelasAuxiliares{

	
	public final int TP_CNH_NORMAL 		= 1;
	public final int TP_CNH_ANTIGA 		= 2;
	public final int TP_CNH_ESTRANGEIRA = 3;
	
	public HashMap<Integer, Integer> tiposCnh;
	
	public static final int LG_ASSINADO			= 1;
	public static final int LG_NAO_ASSINADO		= 2;
	public static final int LG_RECUSOU_ASSINAR	= 3;
	
	public HashMap<Integer, Integer> lgAssinatura;
	
	public TabelasAuxiliaresSP() {
		initTipoCnh();
	}
	
	private void initTipoCnh(){
		tiposCnh = new LinkedHashMap<Integer, Integer>();
		tiposCnh.put(AitServices.TP_CNH_ANTIGA, TP_CNH_ANTIGA);
		tiposCnh.put(AitServices.TP_CNH_NOVA, TP_CNH_NORMAL);
		tiposCnh.put(AitServices.TP_CNH_HABILITACAO_ESTRANGEIRA, TP_CNH_ESTRANGEIRA);
	}
	
	@Override
	public int getTipoDocumento(int tpDocumentoBanco) {
		throw new NotImplementedException("Metodo nao implementado para esse estado");
	}

	@Override
	public int getTipoCnh(int tpCnhBanco) {
		return tiposCnh.get(tpCnhBanco);
	}

	@Override
	public int getAssinatura(int lgAutoAssinado) {
		throw new NotImplementedException("Metodo nao implementado para esse estado");
	}

	@Override
	public int getMovimento(int tpStatus) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPrazoMovimento(int tpStatus) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
}
