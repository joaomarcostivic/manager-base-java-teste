package com.tivic.manager.seg;

import java.sql.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.security.ModuleServices;
import sol.util.Result;

public class ModuloServices implements ModuleServices {

	public static void init()	{
		System.out.println("Inicializando Módulos...");
		Connection connect = Conexao.conectar();
		try {
			PreparedStatement pstmtPesq = connect.prepareStatement("SELECT * FROM seg_modulo WHERE id_modulo = ?");
			ResultSetMap rsm = SistemaDAO.getAll();
			int cdSistema = 1;
			if(rsm.next())
				cdSistema = rsm.getInt("cd_sistema");
			else	
				cdSistema = SistemaDAO.insert(new Sistema(0,"MyManager - Gestão de Recursos Empresariais","MMng", 1), connect);
			// Módulos
			String[] idModulos = {"fnc","cms","alm","mcr","seg","bpm","ctb","cpr","acd","srh","crm","crt","fta","ptc","jur","ctr","agd","pcb","egov","os","fsc", "fac", "grl", "cae", "bi", "ged", "mob"};
			String[] nmModulos = {"Administração Financeira",
					              "CMS - Controle de Conteúdo",
					              "Administração de Materiais (Estoque) e Vendas",
					              "Microcrédito",
								  "Painel de Controle",
								  "Administração Patrimonial", "Contábil",
								  "Gestão de Compras",
								  "Acadêmico", 
								  "RH - Recursos Humanos", 
								  "CRM - Relacionamento Clientes", 
								  "Corretagem",
								  "Frota", "Protocolo", "Gestão Jurídica", "Gestão de Contratos", "Agenda Corporativa",
								  "Posto de Combustíveis", "Governo Eletrônico", "Ordem de Serviço", "Gestão Fiscal", 
								  "Factoring", "Cadastro Geral", "Alimentação Escolar", "Estatística e Apoio à Decisão", 
								  "Gestão Eletrônica de Documentos", "Mobilidade Urbana"};
			for(int i=0; i<idModulos.length; i++)	{
				pstmtPesq.setString(1, idModulos[i]);
				ResultSet rs = pstmtPesq.executeQuery();
				if(!rs.next())	{
					Modulo modulo = new Modulo(0,cdSistema,nmModulos[i],idModulos[i],"1.0"/*nrVersao*/,"0"/*nrRelease*/,0/*lgAtivo*/);
					ModuloDAO.insert(modulo, connect);
				}
				else	{
					Modulo modulo = ModuloDAO.get(rs.getInt("cd_modulo"), rs.getInt("cd_sistema"), connect);
					modulo.setNmModulo(nmModulos[i]);
					ModuloDAO.update(modulo, connect);
				}
			}

		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	
	public static Result save(Modulo modulo){
		return save(modulo, null);
	}

	public static Result save(Modulo modulo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(modulo==null)
				return new Result(-1, "Erro ao salvar. Modulo é nulo");

			int retorno;
			if(modulo.getCdModulo()==0){
				retorno = ModuloDAO.insert(modulo, connect);
				modulo.setCdModulo(retorno);
			}
			else {
				retorno = ModuloDAO.update(modulo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MODULO", modulo);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdModulo, int cdSistema){
		return remove(cdModulo, cdSistema, false, null);
	}
	public static Result remove(int cdModulo, int cdSistema, boolean cascade){
		return remove(cdModulo, cdSistema, cascade, null);
	}
	public static Result remove(int cdModulo, int cdSistema, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = ModuloDAO.delete(cdModulo, cdSistema, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllAtivos() {
		return getAll(true, null);
	}
	
	public static ResultSetMap getAll() {
		return getAll(false, null);
	}
	
	public static ResultSetMap getAll(boolean lgAtivos, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_modulo " +
										(lgAtivos ? " WHERE lg_ativo = 1 " : ""));
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModuloServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModuloServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllOrdered() {
		return getAllOrdered(null);
	}
	
	public static ResultSetMap getAllOrdered(Connection connect) {
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		PreparedStatement pstmt;
		
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_modulo WHERE lg_ativo = 1 ORDER BY nm_modulo ASC");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModuloServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModuloServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM seg_modulo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	@Deprecated
	public static int salvar(Modulo modulo){
		return save(modulo, null).getCode();
	}
	
	
//	public static int save(Modulo modulo, Connection connect){
//		boolean isConnectionNull = connect==null;
//		try {
//			if (isConnectionNull) {
//				connect = Conexao.conectar();
//				connect.setAutoCommit(false);
//			}
//			if(modulo==null)
//				return -1;
//
//			int retorno;
//			if(modulo.getCdModulo()==0)
//				retorno = ModuloDAO.insert(modulo, connect);
//			else	{
//				retorno = ModuloDAO.update(modulo, connect);
//				retorno = retorno>0?modulo.getCdModulo():retorno;
//			}
//
//			if(retorno<0)
//				Conexao.rollback(connect);
//			else if (isConnectionNull)
//				connect.commit();
//
//			return retorno;
//		}
//		catch(Exception e){
//			e.printStackTrace(java.lang.System.out);
//			if (isConnectionNull)
//				Conexao.rollback(connect);
//			return  -1;
//		}
//		finally{
//			if (isConnectionNull)
//				Conexao.desconectar(connect);
//		}
//	}

	public int insertModule(String nameModule, int idSystem) {
		return ModuloDAO.insert(new Modulo(0,idSystem,nameModule,null,""/*nrVersao*/,""/*nrRelease*/,1/*lgAtivo*/));
	}

	public int updateModule(int idModule, String nameModule, int idSystem) {
		return ModuloDAO.update(new Modulo(idModule,idSystem,nameModule,null,""/*nrVersao*/,""/*nrRelease*/,1/*lgAtivo*/));
	}

	public int deleteModule(int idModule, int idSystem) {
		return delete(idModule, idSystem, null);
	}

	public static int delete(int cdModulo, int cdSistema) {
		return delete(cdModulo, cdSistema, null);
	}

	public static int delete(int cdModulo, int cdSistema, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			/* exclusao de objetos */
			connection.prepareStatement("DELETE FROM seg_objeto WHERE cd_modulo = "+cdModulo+" AND cd_sistema = "+cdSistema).execute();

			/* exclusao de permissoes de grupo */
			connection.prepareStatement("DELETE FROM seg_grupo_permissao_acao WHERE cd_modulo = "+cdModulo+" AND cd_sistema = "+cdSistema).execute();

			/* exclusao de permissoes de usuario */
			connection.prepareStatement("DELETE FROM seg_usuario_permissao_acao WHERE cd_modulo = "+cdModulo+" AND cd_sistema = "+cdSistema).execute();

			/* exclusao das acoes */
			connection.prepareStatement("DELETE FROM seg_acao WHERE cd_modulo = "+cdModulo+" AND cd_sistema = "+cdSistema).execute();

			/* exclusao de grupos de acoes */
			connection.prepareStatement("DELETE FROM seg_agrupamento_acao WHERE cd_modulo = "+cdModulo+" AND cd_sistema = "+cdSistema).execute();

			if (ModuloDAO.delete(cdModulo, cdSistema, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllBySistema(int cdSistema) {
		return getAllBySistema(cdSistema, null);
	}

	public static ResultSetMap getAllBySistema(int cdSistema, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
										"FROM SEG_MODULO " +
										"WHERE CD_SISTEMA = ? " +
										"ORDER BY NM_MODULO");
			pstmt.setInt(1, cdSistema);
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
	
	public static ResultSetMap getModulos(String idSistema) {
		return getModulos(idSistema, null);
	}

	public static ResultSetMap getModulos(String idSistema, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
										"FROM SEG_MODULO A " +
										"JOIN SEG_SISTEMA B ON (B.ID_SISTEMA = ? AND A.CD_SISTEMA=B.CD_SISTEMA) " +
										"ORDER BY NM_MODULO");
			pstmt.setString(1, idSistema);
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

	public ResultSetMap getModulesOfSystem(int idSystem) {
		return getModulesOfSystem(idSystem, null);
	}

	public static ResultSetMap getModulesOfSystem(int idSystem, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, CD_MODULO AS ID_MODULE, NM_MODULO AS NAME_MODULE FROM SEG_MODULO A " +
										                          "WHERE CD_SISTEMA = ? " +
										                          "ORDER BY nm_modulo");
			pstmt.setInt(1, idSystem);
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

	public static Modulo getModuloById(String id) {
		return getModuloById(id, null);
	}

	public static Modulo getModuloById(String id, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			if(id==null || id.equals(""))
				return null;
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("id_modulo", id, ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsm = ModuloDAO.find(criterios, connection);
			return !rsm.next() ? null : ModuloDAO.get(rsm.getInt("cd_modulo"), rsm.getInt("cd_sistema"), connection);
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

	public static boolean isActive(String idModulo) {
		return isActive(idModulo, null);
	}

	public static boolean isActive(String idModulo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT lg_ativo FROM SEG_MODULO " +
										"WHERE id_modulo = ? ");
			pstmt.setString(1, idModulo);
			ResultSet rs = pstmt.executeQuery();

			if(rs.next()){
				return rs.getInt("lg_ativo")==1?true:false;
			}
			return false;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int getTotalModulosAtivos() {
		return getTotalModulosAtivos(null);
	}

	public static int getTotalModulosAtivos(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(cd_modulo) as qt_modulos_ativos " +
										"FROM SEG_MODULO " +
										"WHERE lg_ativo = 1 ");
			ResultSet rs = pstmt.executeQuery();

			if(rs.next()){
				return rs.getInt("qt_modulos_ativos");
			}
			return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
}