package com.tivic.manager.wsdl.detran.ba;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.tivic.manager.mob.AitServices;
import com.tivic.manager.wsdl.interfaces.TabelasAuxiliares;

public class TabelasAuxiliaresBA implements TabelasAuxiliares{

	public final int TP_DOCUMENTO_NAO_POSSUI		= 0;
	public final int TP_DOCUMENTO_CPF 				= 1;
	public final int TP_DOCUMENTO_CNPJ 				= 2;
	public final int TP_DOCUMENTO_RG				= 3;
	public final int TP_DOCUMENTO_DOC_ESTRANGEIRO	= 4;
	public final int TP_DOCUMENTO_OUTROS			= 5;
	public final int TP_DOCUMENTO_NAO_APRESENTOU	= 9;
	
	public HashMap<Integer, Integer> tiposDocumento;
	
	public final int TP_CNH_ANTIGA 					= 0;
	public final int TP_CNH_NOVA 					= 1;
	public final int TP_CNH_NAO_POSSUI   			= 4;
	
	public HashMap<Integer, Integer> tiposCnh;
	
	public static final int LG_ASSINADO			= 1;
	public static final int LG_NAO_ASSINADO		= 2;
	
	public HashMap<Integer, Integer> lgAssinatura;
	
	public TabelasAuxiliaresBA() {
		initTipoDocumento();
		initTipoCnh();
		initAssinatura();
	}
	
	private void initTipoDocumento(){
		tiposDocumento = new LinkedHashMap<Integer, Integer>();
		tiposDocumento.put(AitServices.TP_DOCUMENTO_NENHUM, TP_DOCUMENTO_NAO_POSSUI);
		tiposDocumento.put(AitServices.TP_DOCUMENTO_CPF, TP_DOCUMENTO_CPF);
		tiposDocumento.put(AitServices.TP_DOCUMENTO_CNPJ, TP_DOCUMENTO_CNPJ);
		tiposDocumento.put(AitServices.TP_DOCUMENTO_RG, TP_DOCUMENTO_RG);
		tiposDocumento.put(AitServices.TP_DOCUMENTO_DOC_ESTRANGEIRO, TP_DOCUMENTO_DOC_ESTRANGEIRO);
		tiposDocumento.put(AitServices.TP_DOCUMENTO_OUTROS, TP_DOCUMENTO_OUTROS);
		tiposDocumento.put(AitServices.TP_DOCUMENTO_NAO_APRESENTOU, TP_DOCUMENTO_NAO_APRESENTOU);
	}
	
	private void initTipoCnh(){
		tiposCnh = new LinkedHashMap<Integer, Integer>();
		tiposCnh.put(AitServices.TP_CNH_ANTIGA, TP_CNH_ANTIGA);
		tiposCnh.put(AitServices.TP_CNH_NOVA, TP_CNH_NOVA);
		tiposCnh.put(AitServices.TP_CNH_NAO_HABILITADO, TP_CNH_NAO_POSSUI);
	}
	
	private void initAssinatura(){
		lgAssinatura = new LinkedHashMap<Integer, Integer>();
		lgAssinatura.put(AitServices.LG_ASSINADO, LG_ASSINADO);
		lgAssinatura.put(AitServices.LG_NAO_ASSINADO, LG_NAO_ASSINADO);
		lgAssinatura.put(AitServices.LG_RECUSOU_ASSINAR, LG_NAO_ASSINADO);
	}
	
	@Override
	public int getTipoDocumento(int tpDocumentoBanco) {
		return tiposDocumento.get(tpDocumentoBanco);
	}

	@Override
	public int getTipoCnh(int tpCnhBanco) {
		return tiposCnh.get(tpCnhBanco);
	}

	@Override
	public int getAssinatura(int lgAutoAssinado) {
		return lgAssinatura.get(lgAutoAssinado);
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
