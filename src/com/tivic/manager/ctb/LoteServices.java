package com.tivic.manager.ctb;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.NumeracaoDocumentoServices;


public class LoteServices {

	public static final int ST_EM_ABERTO = 0;
	public static final int ST_ENCERRADO = 1;
	public static final int ST_CANCELADO = 2;

	public static final String[] situacaoLote = {"Aberto", "Encerrado", "Cancelado"};

	public static Lote save(Lote lote){
		return save(lote, null);
	}

	public static Lote save(Lote lote, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}

			int retorno = 1;
			if(lote.getCdLote()==0){
				retorno = LoteDAO.insert(lote, connect);
				if(retorno > 0){
					lote.setCdLote(retorno);
				}
			}
			else {
				retorno = LoteDAO.update(lote, connect);
			}

			return lote;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static String getProximoNrLote(int cdEmpresa) {
		return getProximoNrLote(cdEmpresa, null);
	}

	public static String getProximoNrLote(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrLote = 0;
			if ((nrLote = NumeracaoDocumentoServices.getProximoNumero("LOTE", nrAno, cdEmpresa, connection)) <= 0)
				return null;
			return new DecimalFormat("000000").format(nrLote) + "/" + nrAno;
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int setSituacaoLote(int cdLote, int stLote) {
		return setSituacaoLote(cdLote, stLote, null, null);
	}

	public static int setSituacaoLote(int cdLote, int stLote, GregorianCalendar dtEncerramento) {
		return setSituacaoLote(cdLote, stLote, dtEncerramento, null);
	}

	public static int setSituacaoLote(int cdLote, int stLote, GregorianCalendar dtEncerramento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			Lote lote = LoteDAO.get(cdLote, connection);
			int situacaoAtual = lote.getStLote();
			if (stLote == situacaoAtual)
				return 1;
			else {
				lote.setStLote(stLote);
				if (dtEncerramento != null)
					lote.setDtEncerramento(dtEncerramento);
				if (LoteDAO.update(lote, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}
			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteServices.setSituacaoLote: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
}
