package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class TipoDescontoEmpresaServices {

	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO = 1;

	public static int insert(TipoDesconto tipoDesconto, TipoDescontoEmpresa tipoDescEmpresa) {
		return insert(tipoDesconto, tipoDescEmpresa, null);
	}

	public static int insert(TipoDesconto tipoDesconto, TipoDescontoEmpresa tipoDescEmpresa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int cdTipoDesconto = TipoDescontoDAO.insert(tipoDesconto, connection);
			if (cdTipoDesconto<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			tipoDescEmpresa.setCdTipoDesconto(cdTipoDesconto);
			if (TipoDescontoEmpresaDAO.insert(tipoDescEmpresa, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return cdTipoDesconto;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			System.err.println("Erro! TipoDescontoEmpresaServices.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int update(TipoDesconto tipoDesconto, TipoDescontoEmpresa tipoDescEmpresa) {
		return update(tipoDesconto, tipoDescEmpresa, null);
	}

	public static int update(TipoDesconto tipoDesconto, TipoDescontoEmpresa tipoDescEmpresa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			if (TipoDescontoDAO.update(tipoDesconto, connection)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (TipoDescontoEmpresaDAO.update(tipoDescEmpresa, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			System.err.println("Erro! TipoDescontoEmpresaServices.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int delete(int cdTipoDesconto, int cdEmpresa) {
		return delete(cdTipoDesconto, cdEmpresa, null);
	}

	public static int delete(int cdTipoDesconto, int cdEmpresa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("DELETE " +
					"FROM adm_faixa_desconto " +
					"WHERE cd_tipo_desconto = ? " +
					"  AND cd_empresa = ?");
			pstmt.setInt(1, cdTipoDesconto);
			pstmt.setInt(2, cdEmpresa);
			pstmt.execute();

			if (TipoDescontoEmpresaDAO.delete(cdTipoDesconto, cdEmpresa, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			pstmt = connection.prepareStatement("SELECT cd_tipo_desconto " +
					"FROM adm_tipo_desconto_empresa " +
					"WHERE cd_tipo_desconto = ?");
			pstmt.setInt(1, cdTipoDesconto);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				if (TipoDescontoDAO.delete(cdTipoDesconto, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			System.err.println("Erro! TipoDescontoEmpresaServices.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		for (int i=0; criterios!=null && i<criterios.size(); i++)
			if (criterios.get(i).getColumn().equalsIgnoreCase("st_tipo_desconto") && Integer.parseInt(criterios.get(i).getValue())==-1) {
				criterios.remove(i);
				break;
			}
		return Search.find("SELECT A.*, B.st_tipo_desconto, B.cd_empresa " +
				"FROM adm_tipo_desconto A " +
				"JOIN adm_tipo_desconto_empresa B ON (A.cd_tipo_desconto = B.cd_tipo_desconto) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getAllTiposDescontos(int cdEmpresa) {
		return getAllTiposDescontos(cdEmpresa, false, null);
	}

	public static ResultSetMap getAllTiposDescontos(int cdEmpresa, boolean justAtivos) {
		return getAllTiposDescontos(cdEmpresa, justAtivos, null);
	}

	public static ResultSetMap getAllTiposDescontos(int cdEmpresa, boolean justAtivos, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
					"FROM adm_tipo_desconto A, adm_tipo_desconto_empresa B  " +
					"WHERE A.cd_tipo_desconto = B.cd_tipo_desconto " +
					"  AND B.cd_empresa = ? " +
					(justAtivos ? "  AND B.st_tipo_desconto = ? " +
					"  AND EXISTS (SELECT C.cd_faixa_desconto " +
					"			   FROM adm_faixa_desconto C " +
					"			   WHERE C.cd_tipo_desconto = B.cd_tipo_desconto " +
					"				 AND C.cd_empresa = B.cd_empresa " +
					"				 AND (C.dt_inicial_validade IS NULL OR C.dt_inicial_validade <= ?) " +
					"				 AND (C.dt_final_validade IS NULL OR ? <= C.dt_final_validade))" : ""));
			pstmt.setInt(1, cdEmpresa);
			if (justAtivos) {
				pstmt.setInt(2, ST_ATIVO);
				GregorianCalendar dtAtual = Util.convStringToCalendar(Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy"));
				pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtAtual));
				pstmt.setTimestamp(4, Util.convCalendarToTimestamp(dtAtual));
			}
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllFaixasDescontos(int cdTipoDesconto, int cdEmpresa, boolean justAtivos) {
		return getAllFaixasDescontos(cdTipoDesconto, cdEmpresa, justAtivos, null);
	}

	public static ResultSetMap getAllFaixasDescontos(int cdTipoDesconto, int cdEmpresa, boolean justAtivos, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
					"FROM adm_faixa_desconto A, adm_tipo_desconto_empresa B  " +
					"WHERE A.cd_tipo_desconto = B.cd_tipo_desconto " +
					"  AND A.cd_empresa = B.cd_empresa " +
					"  AND A.cd_tipo_desconto = ? " +
					"  AND A.cd_empresa = ? " +
					(justAtivos ? "  AND B.st_tipo_desconto = ? " +
					"  AND (A.dt_inicial_validade IS NULL OR A.dt_inicial_validade <= ?) " +
					"  AND (A.dt_final_validade IS NULL OR ? <= A.dt_final_validade)" : ""));
			pstmt.setInt(1, cdTipoDesconto);
			pstmt.setInt(2, cdEmpresa);
			if (justAtivos) {
				pstmt.setInt(3, ST_ATIVO);
				GregorianCalendar dtAtual = Util.convStringToCalendar(Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy"));
				pstmt.setTimestamp(4, Util.convCalendarToTimestamp(dtAtual));
				pstmt.setTimestamp(5, Util.convCalendarToTimestamp(dtAtual));
			}
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
