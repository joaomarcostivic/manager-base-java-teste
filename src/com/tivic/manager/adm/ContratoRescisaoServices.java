package com.tivic.manager.adm;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.DocumentoServices;

import sol.util.Result;

public class ContratoRescisaoServices {

	public static final int TP_FORM_PESSOAL = 0;
	public static final int TP_FORM_TELEFONE = 1;

	public static final String[] formasRecisao = {"Pessoal",
		"Por telefone"};

	public static int cancelarContrato(GregorianCalendar dtSolicitacao, ContratoRescisao rescisao) {
		return cancelarContrato(dtSolicitacao, rescisao, null);
	}

	@SuppressWarnings("unchecked")
	public static int cancelarContrato(GregorianCalendar dtSolicitacao, ContratoRescisao rescisao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			Contrato contrato = ContratoDAO.get(rescisao.getCdContrato(), connection);
			int cdContratante = contrato.getCdPessoa();
			int cdTipoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_RESCISAO_CONTRATO", 0, 0, connection);
			ArrayList<Integer> tiposDocumento = new ArrayList<Integer>();
			tiposDocumento.add(cdTipoDocumento);
			Result result = DocumentoServices.insertSolicitacao(cdContratante /*cdSolicitante*/, dtSolicitacao /*dtSolicitacao*/, "" /*txtObservacao*/,
					                                            contrato.getCdEmpresa(), 0 /*cdUsuario*/, tiposDocumento, connection);
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result.getCode();
			}
			ArrayList<Integer> docs = (ArrayList<Integer>)result.getObjects().get("documentos");
			int cdDocumento = docs!=null && docs.size()>0 ? docs.get(0) : 0;
			rescisao.setCdDocumento(cdDocumento);

			int cdRescisao = ContratoRescisaoDAO.insert(rescisao, connection);
			if (cdRescisao <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			contrato.setStContrato(ContratoServices.ST_CANCELADO);
			if (ContratoDAO.update(contrato, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return cdDocumento;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
