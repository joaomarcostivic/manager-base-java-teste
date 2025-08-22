package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class ConteinerServices {

	public static ResultSetMap getAllHierarquia(int cdUsuario) {
		return getAllHierarquia(cdUsuario, null);
	}

	public static ResultSetMap getAllHierarquia(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if (cdUsuario > 0)
				criterios.add(new ItemComparator("CD_USUARIO", Integer.toString(cdUsuario), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = ConteinerDAO.find(criterios, connect);
			while (rsm != null && rsm.next()) {
				if (rsm.getInt("cd_conteiner_pai") != 0) {
					int pointer = rsm.getPointer();
					int cdConteiner = rsm.getInt("cd_conteiner_pai");
					HashMap<String,Object> register = rsm.getRegister();
					if (rsm.locate("cd_conteiner", new Integer(rsm.getInt("cd_conteiner_pai")), false, true)) {
						HashMap<String,Object> parentNode = rsm.getRegister();
						boolean isFound = rsm.getInt("cd_conteiner")==cdConteiner;
						ResultSetMap subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						while (!isFound && subRsm!=null) {
							subRsm = parentNode==null ? null : (ResultSetMap)parentNode.get("subResultSetMap");
							parentNode = subRsm==null ? null : subRsm.getRegister();
							isFound = subRsm==null ? false : subRsm.getInt("cd_conteiner")==cdConteiner;
						}
						subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						if (subRsm==null) {
							subRsm = new ResultSetMap();
							parentNode.put("subResultSetMap", subRsm);
						}
						subRsm.addRegister(register);
						rsm.getLines().remove(register);
						pointer--;
					}
					rsm.goTo(pointer);
				}
			}
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllArquivos(int cdConteiner) {
		return getAllArquivos(cdConteiner, null);
	}

	public static ResultSetMap getAllArquivos(int cdConteiner, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT B.*, " +
					"C.nm_tipo_arquivo " +
					"FROM grl_conteiner_arquivo A " +
					"JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo) " +
					"LEFT OUTER JOIN grl_tipo_arquivo C ON (B.cd_tipo_arquivo = C.cd_tipo_arquivo) " +
					"WHERE A.cd_conteiner = ?");
			pstmt.setInt(1, cdConteiner);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm.next()) {
				rsm.getRegister().put("QT_BYTES", rsm.getObject("blb_arquivo") instanceof byte[] ? ((byte[])rsm.getObject("blb_arquivo")).length : 0);
			}
			rsm.beforeFirst();
			return rsm;
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

	public static int delete(int cdConteiner) {
		return delete(cdConteiner, null);
	}

	public static int delete(int cdConteiner, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			/* arquivos */
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM grl_conteiner_arquivo " +
					"WHERE cd_conteiner = ?");
			pstmt.setInt(1, cdConteiner);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				if (ConteinerArquivoServices.delete(cdConteiner, rs.getInt("cd_arquivo"), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

			/* conteiners filhos */
			pstmt = connection.prepareStatement("SELECT * FROM grl_conteiner " +
					"WHERE cd_conteiner_pai = ?");
			pstmt.setInt(1, cdConteiner);
			rs = pstmt.executeQuery();
			while (rs.next())
				if (delete(rs.getInt("cd_conteiner"), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

			if (ConteinerDAO.delete(cdConteiner, connection) <= 0) {
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
			System.err.println("Erro! ConteinerServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
