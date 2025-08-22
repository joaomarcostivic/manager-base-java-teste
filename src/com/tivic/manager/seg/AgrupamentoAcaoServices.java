package com.tivic.manager.seg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.*;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.LogUtils;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class AgrupamentoAcaoServices {

	public static Result save(AgrupamentoAcao agrupamentoAcao){
		return save(agrupamentoAcao, null);
	}

	public static Result save(AgrupamentoAcao agrupamentoAcao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(agrupamentoAcao==null)
				return new Result(-1, "Erro ao salvar. AgrupamentoAcao é nulo");

			int retorno;
			if(agrupamentoAcao.getCdAgrupamento()==0){
				retorno = AgrupamentoAcaoDAO.insert(agrupamentoAcao, connect);
				agrupamentoAcao.setCdAgrupamento(retorno);
			}
			else {
				retorno = AgrupamentoAcaoDAO.update(agrupamentoAcao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AGRUPAMENTOACAO", agrupamentoAcao);
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
	
	public static Result save(ArrayList<AgrupamentoAcao> agrupamentos){
		return save(agrupamentos,  null);
	}

	public static Result save(ArrayList<AgrupamentoAcao> agrupamentos, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(agrupamentos==null){
				return new Result(-1, "Lista de agrupamentos é nula.");
			}
			int retorno=0;

			LogUtils.debug("AGRUPAMENTOS:\n" + agrupamentos.toString());
			
			for(int i=0; i<agrupamentos.size(); i++){
				AgrupamentoAcao agrupamento = agrupamentos.get(i);

				if((retorno=save(agrupamento, connect).getCode())<0)
					break;
			}
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, retorno < 0 ? "Erro ao salvar agrupamentos." : "Agrupamentos salvos com sucesso");
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! AgrupamentoServices.save: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result((-1)*sqlExpt.getErrorCode(), "Erro ao salvar agrupamentos.");
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! AgrupamentoServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao salvar agrupamentos.");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	public static Result remove(int cdAgrupamento, int cdModulo, int cdSistema){
		return remove(cdAgrupamento, cdModulo, cdSistema, false, null);
	}
	public static Result remove(int cdAgrupamento, int cdModulo, int cdSistema, boolean cascade){
		return remove(cdAgrupamento, cdModulo, cdSistema, cascade, null);
	}
	public static Result remove(int cdAgrupamento, int cdModulo, int cdSistema, boolean cascade, Connection connect){
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
			retorno = AgrupamentoAcaoDAO.delete(cdAgrupamento, cdModulo, cdSistema, connect);
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
	
	public static ResultSetMap getAllNmAgrupamento() {
		return getAllNmAgrupamento(null);
	}

	public static ResultSetMap getAllNmAgrupamento(Connection connect) {
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_agrupamento_acao ORDER BY nm_agrupamento ASC");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoServices.getAllNmAgrupamento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_modulo, B.id_modulo "
					+ " FROM seg_agrupamento_acao A, seg_modulo B "
					+ " WHERE A.cd_modulo = B.cd_modulo "
					+ " ORDER BY A.cd_sistema, A.cd_modulo, A.nr_ordem, A.nm_agrupamento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByModulo(int cdSistema, int cdModulo) {
		return getAllByModulo(cdSistema, cdModulo, -1, null);
	}
	
	public static ResultSetMap getAllByModulo(int cdSistema, int cdModulo, int lgAtivo) {
		return getAllByModulo(cdSistema, cdModulo, lgAtivo, null);
	}

	public static ResultSetMap getAllByModulo(int cdSistema, int cdModulo, int lgAtivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_agrupamento_acao "
					+ " WHERE 1=1 "
					+ (cdSistema > 0 ? " AND cd_sistema = " + cdSistema : "")
					+ (cdModulo > 0 ? " AND cd_modulo = " + cdModulo : "")
					+ (lgAtivo > -1 ? " AND lg_ativo=" + lgAtivo : "")
					+ " ORDER BY cd_sistema, cd_modulo, nr_ordem, nm_agrupamento");
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoServices.getAllByModulo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoServices.getAllByModulo: " + e);
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
		return Search.find("SELECT * FROM seg_agrupamento_acao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	@Deprecated
	public static int salvar(AgrupamentoAcao agrupamento){
		return save(agrupamento, null).getCode();
	}
	
//	public static int save(AgrupamentoAcao agrupamento, Connection connect){
//		boolean isConnectionNull = connect==null;
//		try {
//			if (isConnectionNull) {
//				connect = Conexao.conectar();
//				connect.setAutoCommit(false);
//			}
//			if(agrupamento==null){
//				return -1;
//			}
//
//			int retorno;
//			if(agrupamento.getCdAgrupamento()==0){
//				retorno = AgrupamentoAcaoDAO.insert(agrupamento, connect);
//			}
//			else{
//				retorno = AgrupamentoAcaoDAO.update(agrupamento, connect);
//				retorno = retorno>0?agrupamento.getCdAgrupamento():retorno;
//			}
//
//			if(retorno<0)
//				Conexao.rollback(connect);
//			else if (isConnectionNull)
//				connect.commit();
//
//			return retorno;
//		}
//		catch(SQLException sqlExpt){
//			sqlExpt.printStackTrace(java.lang.System.out);
//			java.lang.System.err.println("Erro! AgrupamentoAcaoServices.save: " + sqlExpt);
//			if (isConnectionNull)
//				Conexao.rollback(connect);
//			return (-1)*sqlExpt.getErrorCode();
//		}
//		catch(Exception e){
//			e.printStackTrace(java.lang.System.out);
//			java.lang.System.err.println("Erro! AgrupamentoAcaoServices.save: " +  e);
//			if (isConnectionNull)
//				Conexao.rollback(connect);
//			return  -1;
//		}
//		finally{
//			if (isConnectionNull)
//				Conexao.desconectar(connect);
//		}
//	}

//	public int insert(ArrayList<HashMap<String, Object>> agrupamentos) {
//		return insert(agrupamentos, null);
//	}
//
//	@SuppressWarnings("unchecked")
//	public int insert(ArrayList<HashMap<String, Object>> agrupamentos, Connection connection) {
//		boolean isConnectionNull = connection==null;
//		try {
//			connection = isConnectionNull ? Conexao.conectar() : connection;
//			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
//
//			for (int i=0; agrupamentos!=null && i<agrupamentos.size(); i++) {
//				HashMap<String, Object> hash = agrupamentos.get(i);
//				AgrupamentoAcao agrupamento = (AgrupamentoAcao)hash.get("agrupamento");
//				ArrayList<Acao> acoes = (ArrayList<Acao>)hash.get("acoes");
//				if (insert(agrupamento, acoes, connection) <= 0) {
//					if (isConnectionNull)
//						Conexao.rollback(connection);
//					return -1;
//				}
//			}
//
//			if (isConnectionNull)
//				connection.commit();
//
//			return 1;
//		}
//		catch(Exception e) {
//			e.printStackTrace(System.out);
//			if (isConnectionNull)
//				Conexao.rollback(connection);
//			return -1;
//		}
//		finally {
//			if (isConnectionNull)
//				Conexao.desconectar(connection);
//		}
//	}
//
//	public int insert(AgrupamentoAcao agrupamento, ArrayList<Acao> acoes) {
//		return insert(agrupamento, acoes, null);
//	}
//
//	public int insert(AgrupamentoAcao agrupamento, ArrayList<Acao> acoes, Connection connection) {
//		boolean isConnectionNull = connection==null;
//		try {
//			connection = isConnectionNull ? Conexao.conectar() : connection;
//			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
//
//			int cdAgrupamento = 0;
//			if ((cdAgrupamento = AgrupamentoAcaoDAO.insert(agrupamento, connection)) <= 0) {
//				if (isConnectionNull)
//					Conexao.rollback(connection);
//				return -1;
//			}
//			for (int i=0; acoes!=null && i<acoes.size(); i++) {
//				System.out.println(acoes.get(i));
//				acoes.get(i).setCdSistema(agrupamento.getCdSistema());
//				acoes.get(i).setCdAgrupamento(cdAgrupamento);
//				if (AcaoDAO.insert(acoes.get(i), connection) <= 0) {
//					if (isConnectionNull)
//						Conexao.rollback(connection);
//					return -1;
//				}
//			}
//
//			if (isConnectionNull)
//				connection.commit();
//
//			return 1;
//		}
//		catch(Exception e) {
//			e.printStackTrace(System.out);
//			if (isConnectionNull)
//				Conexao.rollback(connection);
//			return -1;
//		}
//		finally {
//			if (isConnectionNull)
//				Conexao.desconectar(connection);
//		}
//	}
//
//	public int deleteGroupActions(int idGroupActions, int idSystem) {
//		return AgrupamentoAcaoDAO.delete(idGroupActions, idSystem);
//	}
//
//	public GroupActions[] getGroupsActions(int idSystem) {
//		return getGroupsActions(idSystem, null);
//	}
//
//	public static GroupActions[] getGroupsActions(int idSystem, Connection connect) {
//		boolean isConnectionNull = connect==null;
//		try {
//			if (isConnectionNull)
//				connect = Conexao.conectar();
//			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, C.NM_SISTEMA, C.ID_SISTEMA " +
//										"FROM SEG_AGRUPAMENTO_ACAO A, SEG_SISTEMA C " +
//										"WHERE A.CD_SISTEMA = C.CD_SISTEMA " +
//										"  AND A.CD_SISTEMA = ?");
//			pstmt.setInt(1, idSystem);
//			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
//			GroupActions[] groupsActions = new GroupActions[rsm!=null ? rsm.size() : 0];
//			sol.security.System system = null;
//			for (int i = 0; rsm!=null && rsm.next(); i++) {
//				system = system!=null ? system : new sol.security.System(rsm.getInt("CD_SISTEMA"), rsm.getString("NM_SISTEMA"), rsm.getString("ID_SISTEMA"));
//				GroupActions groupActions = rsm.getInt("CD_AGRUPAMENTO")==0 ? null : new GroupActions(rsm.getInt("CD_AGRUPAMENTO"), rsm.getString("NM_AGRUPAMENTO"), rsm.getString("DS_AGRUPAMENTO"), null);
//				groupsActions[i] = groupActions;
//			}
//			return groupsActions;
//		}
//		catch(Exception e) {
//			e.printStackTrace(System.out);
//			return null;
//		}
//		finally {
//			if (isConnectionNull)
//				Conexao.desconectar(connect);
//		}
//	}
//
//	public int insertGroupActions(GroupActions groupActions) {
//		return AgrupamentoAcaoDAO.insert(new AgrupamentoAcao(0, groupActions.getModule().getSystem().getId(),
//															 groupActions.getName(), null,  groupActions.getDescription()));
//	}
//
//	public int updateGroupActions(GroupActions groupActions) {
//		return AgrupamentoAcaoDAO.update(new AgrupamentoAcao(groupActions.getId(), groupActions.getModule().getSystem().getId(),
//				 groupActions.getName(), null, groupActions.getDescription()));
//	}

	public static ResultSetMap getSistemasFromPacotes(String dir) {
		return getSistemasFromPacotes(dir, null);
	}

	public static ResultSetMap getSistemasFromPacotes(String dir, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			String nmPackage = dir.replaceAll("[\\\\]","/");
			String[] parts = nmPackage.split("/");
			if(parts.length>0)
				nmPackage = parts[parts.length-1];
			ResultSetMap rsmSistemas = new ResultSetMap();
			ResultSetMap rsmFiles = sol.admin.FileSystem.list(dir, true);
			while(rsmFiles.next())	{
				if(rsmFiles.getString("nm_arquivo").equalsIgnoreCase("cvs"))
					continue;
				int cdSistema = 0;
				String idSistema = nmPackage+"."+rsmFiles.getString("nm_arquivo");
				HashMap<String,Object> reg = new HashMap<String,Object>();
				reg.put("CD_SISTEMA", new Integer(cdSistema));
				reg.put("NM_SISTEMA", rsmFiles.getString("nm_arquivo"));
				reg.put("ID_SISTEMA", idSistema);
				reg.put("DS_PATH", rsmFiles.getString("ds_path_arquivo"));
				rsmSistemas.addRegister(reg);
			}
			return rsmSistemas;
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

	public static ResultSetMap getSistemasFromPacotes(byte[] bytesZip) {
		try {
			CheckedInputStream checksum = new CheckedInputStream(new ByteArrayInputStream(bytesZip), new CRC32());
			ZipInputStream zis = new ZipInputStream(checksum);
			ZipEntry entry = null;
			ResultSetMap rsmSistemas = new ResultSetMap();
			HashMap<String,Object> regSistema = null;
			while((entry = zis.getNextEntry()) != null) {
				if (entry.isDirectory()) {
					String nmPackage = entry.getName().replaceAll("[\\\\]","/");
					String[] parts = nmPackage.split("/");
					if(parts.length>0)
						nmPackage = parts[parts.length-1];
					regSistema = new HashMap<String,Object>();
					regSistema.put("CD_SISTEMA", 0);
					regSistema.put("NM_SISTEMA", nmPackage);
					regSistema.put("ID_SISTEMA", nmPackage);
					regSistema.put("DS_PATH", entry.getName());
					rsmSistemas.addRegister(regSistema);
				}
				else if (regSistema!=null) {
					String nameFile = entry.getName();
					String[] parts = nameFile.split("/");
					if(parts.length>0)
						nameFile = parts[parts.length-1];
					if (nameFile.length()>=4 && nameFile.substring(nameFile.length()-4, nameFile.length()).equals("java")) {
						nameFile = nameFile.substring(0, nameFile.length()-4);
					}
					if(nameFile.indexOf("DAO")>=0 || nameFile.indexOf("Services")>=0)
						continue;

					if (regSistema.get("rsmGroupActions")==null)
						regSistema.put("rsmGroupActions", new ResultSetMap());
					ResultSetMap rsmGroupActions = (ResultSetMap)regSistema.get("rsmGroupActions");

					String idAgrupamento = nameFile;
					String nmAgrupamento = "Cadastro e Manutenção de " + nameFile;
					if(idAgrupamento.length()>20)
						idAgrupamento = idAgrupamento.substring(0, 20);
					HashMap<String,Object> regAgrupamento = new HashMap<String,Object>();
					regAgrupamento.put("LG_AGRUPAMENTO", new Integer(1));
					regAgrupamento.put("CD_SISTEMA", 0);
					regAgrupamento.put("CD_AGRUPAMENTO", 0);
					regAgrupamento.put("NM_AGRUPAMENTO", nmAgrupamento);
					regAgrupamento.put("ID_AGRUPAMENTO", idAgrupamento);
					regAgrupamento.put("NM_ACAO", "-");
					regAgrupamento.put("DS_ACAO", "-");
					regAgrupamento.put("CD_ACAO", 0);
					rsmGroupActions.addRegister(regAgrupamento);

					// Ações
					String[] acao    = new String[] {"Incluir", "Alterar", "Excluir"};
					String[] metodo  = new String[] {"insert", "update", "delete"};
					ResultSetMap rsmTemp = new ResultSetMap();
					for(int i=0; i<3; i++)	{
						String nmAcao = nameFile+"DAO."+metodo[i];
						String dsAcao = acao[i]+" "+nameFile;
						int cdAcao = 0;
						// Pesquisando no banco de dados
						HashMap<String,Object> reg = new HashMap<String,Object>();
						reg.put("LG_AGRUPAMENTO", 0);
						reg.put("CD_SISTEMA", 0);
						reg.put("CD_AGRUPAMENTO", 0);
						reg.put("CD_ACAO", new Integer(cdAcao));
						reg.put("NM_AGRUPAMENTO", "");
						reg.put("NM_ACAO", nmAcao);
						reg.put("DS_ACAO", dsAcao);
						rsmTemp.addRegister(reg);
					}
					rsmTemp.beforeFirst();
					regAgrupamento.put("subResultSetMap", rsmTemp);
				}
			}
			return rsmSistemas;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static ResultSetMap getAcoesFromClasses(String dir) {
		return getAcoesFromClasses(dir, null);
	}

	public static ResultSetMap getAcoesFromClasses(String dir, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			ResultSetMap rsmResult = new ResultSetMap();
			ResultSetMap rsmFiles = sol.admin.FileSystem.list(dir, "java");
			while(rsmFiles.next())	{
				String nmArquivo = rsmFiles.getString("nm_arquivo");
				if(nmArquivo.indexOf("DAO")>=0 || nmArquivo.indexOf("Services")>=0)
					continue;
				// Agrupamento
				String nmPackage = rsmFiles.getString("DS_PATH_ARQUIVO");
				String idAgrupamento = nmArquivo.substring(0, nmArquivo.indexOf("."));
				String nmArquivoTemp = nmArquivo.toString();
				//nmArquivoTemp = nmArquivoTemp.indexOf('.')>0 ? nmArquivoTemp.substring(nmArquivoTemp.indexOf('.')+1) : nmArquivoTemp;
				String nmAgrupamento = "Cadastro e Manutenção de "+nmArquivoTemp.substring(0, nmArquivoTemp.indexOf("."));
				if(idAgrupamento.length()>20)
					idAgrupamento = idAgrupamento.substring(0, 20);
				int cdAgrupamento = 0;
				int cdSistema = 0;
				// Pesquisando no banco de dados
				HashMap<String,Object> regAgrupamento = new HashMap<String,Object>();
				regAgrupamento.put("LG_AGRUPAMENTO", new Integer(1));
				regAgrupamento.put("CD_SISTEMA", new Integer(cdSistema));
				regAgrupamento.put("CD_AGRUPAMENTO", new Integer(cdAgrupamento));
				regAgrupamento.put("NM_AGRUPAMENTO", nmAgrupamento);
				regAgrupamento.put("ID_AGRUPAMENTO", idAgrupamento);
				regAgrupamento.put("NM_ACAO", "-");
				regAgrupamento.put("DS_ACAO", "-");
				regAgrupamento.put("CD_ACAO", new Integer(0));
				rsmResult.addRegister(regAgrupamento);
				// Ações
				String[] acao    = new String[] {"Incluir", "Alterar", "Excluir"};
				String[] metodo  = new String[] {"insert", "update", "delete"};
				nmPackage = nmPackage.replaceAll("[\\\\]","/");
				String[] parts = null;
				if(nmPackage.indexOf("/")>0)
					parts = nmPackage.split("/");
				if(parts.length>2)
					nmPackage = parts[parts.length-3]+"."+parts[parts.length-2];
				ResultSetMap rsmTemp = new ResultSetMap();
				for(int i=0; i<3; i++)	{
					String nmAcao = nmPackage+"."+nmArquivo.substring(0, nmArquivo.indexOf("."))+"DAO."+metodo[i];
					String dsAcao = acao[i]+" "+nmArquivo.substring(0, nmArquivo.indexOf("."));
					int cdAcao = 0;
					// Pesquisando no banco de dados
					HashMap<String,Object> reg = new HashMap<String,Object>();
					reg.put("LG_AGRUPAMENTO", new Integer(0));
					reg.put("CD_SISTEMA", new Integer(cdSistema));
					reg.put("CD_AGRUPAMENTO", new Integer(cdAgrupamento));
					reg.put("CD_ACAO", new Integer(cdAcao));
					reg.put("NM_AGRUPAMENTO", "");
					reg.put("NM_ACAO", nmAcao);
					reg.put("DS_ACAO", dsAcao);
					rsmTemp.addRegister(reg);
				}
				rsmTemp.beforeFirst();
				regAgrupamento.put("subResultSetMap", rsmTemp);
			}
			return rsmResult;
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

	public static int importAgrupamentoAndAcao(int cdSistema, String xml) {
		return importAgrupamentoAndAcao(cdSistema, xml, null);
	}

	@SuppressWarnings("unchecked")
	public static int importAgrupamentoAndAcao(int cdSistema, String xml, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			SAXBuilder sb = new SAXBuilder();
			Document document = sb.build(new ByteArrayInputStream(xml.getBytes("ISO-8859-1")));
			Element acoes = document.getRootElement();
			if (acoes.getName().equals("acoes")) {
				List<Element> agrupamentos = acoes.getChildren("agrupamentoAcao");
				Iterator<Element> itAgrupamentos = agrupamentos.iterator();
				while (itAgrupamentos.hasNext()) {
					Element agrupamentoElement = itAgrupamentos.next();
					String idAgrupamento = agrupamentoElement.getAttributeValue("id");
					String nmAgrupamento = agrupamentoElement.getAttributeValue("nome");
					String dsAgrupamento = agrupamentoElement.getAttributeValue("descricao");
					if (idAgrupamento==null || idAgrupamento.trim().equals(""))
						continue;

					String idSistema = agrupamentoElement.getAttributeValue("idSistema");
					String nmSistema = agrupamentoElement.getAttributeValue("nomeSistema");
					PreparedStatement pstmt = connection.prepareStatement("SELECT cd_sistema " +
							"FROM seg_sistema " +
							"WHERE id_sistema = ? " +
							"  AND cd_sistema = ?");
					pstmt.setString(1, idSistema);
					pstmt.setInt(2, cdSistema);
					ResultSet rs = pstmt.executeQuery();
					if (cdSistema<=0 && (cdSistema = SistemaDAO.insert(new Sistema(0 /*cdSistema*/,
							nmSistema,
							idSistema,
							0 /*lgAtivo*/), connection)) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
					else {
						Sistema sistema = SistemaDAO.get(cdSistema, connection);
						sistema.setNmSistema(nmSistema);
						if (SistemaDAO.update(sistema, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return -1;
						}
					}

					pstmt = connection.prepareStatement("SELECT cd_agrupamento " +
							"FROM seg_agrupamento_acao " +
							"WHERE id_agrupamento = ? " +
							"  AND cd_sistema = ? " +
							"  AND cd_sistema = ?");
					pstmt.setString(1, idAgrupamento);
					pstmt.setInt(2, cdSistema);
					pstmt.setInt(3, cdSistema);
					rs = pstmt.executeQuery();
					int cdAgrupamento = rs.next() ? rs.getInt("cd_agrupamento") : 0;
					if (cdAgrupamento<=0 && (cdAgrupamento = AgrupamentoAcaoDAO.insert(new AgrupamentoAcao(0 /*cdAgrupamento*/,
							1, //cdModulo,
							cdSistema,
							nmAgrupamento,
							idAgrupamento /*idAgrupamento*/,
							dsAgrupamento, 0, 1, 0), connection)) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
					else {
						AgrupamentoAcao agrupamento = AgrupamentoAcaoDAO.get(cdAgrupamento, 1 /*cdModulo*/, cdSistema, connection);
						agrupamento.setDsAgrupamento(dsAgrupamento);
						agrupamento.setNmAgrupamento(nmAgrupamento);
						if (AgrupamentoAcaoDAO.update(agrupamento, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return -1;
						}
					}

					List<Element> acoesAgrupamento = agrupamentoElement.getChildren("acao");
					Iterator<Element> itAcoes = acoesAgrupamento.iterator();
					while (itAcoes.hasNext()) {
						Element acaoElement = itAcoes.next();
						String nmAcao = acaoElement.getAttributeValue("nome");
						String dsAcao = acaoElement.getAttributeValue("descricao");

						pstmt = connection.prepareStatement("SELECT cd_acao " +
								"FROM seg_acao " +
								"WHERE nm_acao = ? " +
								"  AND cd_agrupamento = ? " +
								"  AND cd_sistema = ? " +
								"  AND cd_sistema = ?");
						pstmt.setString(1, nmAcao);
						pstmt.setInt(2, cdAgrupamento);
						pstmt.setInt(3, cdSistema);
						pstmt.setInt(4, cdSistema);
						rs = pstmt.executeQuery();
						int cdAcao = rs.next() ? rs.getInt("cd_acao") : 0;
						if (cdAcao<=0 && (cdAcao = AcaoDAO.insert(new Acao(0 /*cdAcao*/,
								cdSistema,
								1,
								nmAcao,
								dsAcao,
								cdAgrupamento, "", 0, 0, 0), connection)) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return -1;
						}
						else {
							Acao acao = AcaoDAO.get(cdAcao, 1, cdSistema, connection);
							acao.setDsAcao(dsAcao);
							if (AcaoDAO.update(acao, connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return -1;
							}
						}
					}
				}
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

	public static String getAgrupamentoAndAcaoAsXml(int cdSistema) {
		return getAgrupamentoAndAcaoAsXml(cdSistema, null);
	}

	public static String getAgrupamentoAndAcaoAsXml(int cdSistema, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ResultSetMap rsm = getAgrupamentoAndAcao(cdSistema, connection);

			Document doc = new Document();
			Element root = new Element("acoes");

			while (rsm.next()) {
				if (rsm.getString("id_agrupamento", "").trim().equals(""))
					continue;
				Element agrupamentoAcao = new Element("agrupamentoAcao");
				agrupamentoAcao.setAttribute("idSistema", rsm.getString("id_sistema", "").trim());
				agrupamentoAcao.setAttribute("nomeSistema", rsm.getString("nm_sistema", "").trim());
				agrupamentoAcao.setAttribute("nome", rsm.getString("nm_agrupamento", "").trim());
				agrupamentoAcao.setAttribute("descricao", rsm.getString("ds_agrupamento", "").trim());
				agrupamentoAcao.setAttribute("id", rsm.getString("id_agrupamento", "").trim());

				ResultSetMap rsmAcoes = (ResultSetMap)rsm.getRegister().get("subResultSetMap");
				while (rsmAcoes.next()) {
					if (!rsmAcoes.getString("nm_acao", "").trim().equals("")) {
						Element acao = new Element("acao");
						acao.setAttribute("nome", rsmAcoes.getString("nm_acao", "").trim());
						acao.setAttribute("descricao", rsmAcoes.getString("ds_acao", "").trim());
						agrupamentoAcao.addContent(acao);
					}
				}

				root.addContent(agrupamentoAcao);
			}

			doc.addContent(root);

			XMLOutputter outp = new XMLOutputter();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			outp.setFormat(Format.getPrettyFormat().setEncoding("ISO-8859-1"));
			outp.output(doc, stream);
			return stream.toString();
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

	public static ResultSetMap getAgrupamentoAndAcao(int cdSistema) {
		return getAgrupamentoAndAcao(cdSistema, null);
	}

	public static ResultSetMap getAgrupamentoAndAcao(int cdSistema, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
					"FROM seg_acao A " +
					"WHERE A.cd_sistema     = ? " +
					"  AND A.cd_agrupamento = ? " +
					"ORDER BY ds_acao");
			ResultSetMap rsmAgrupamento = new ResultSetMap(connection.prepareStatement("SELECT A.*, B.id_sistema, B.nm_sistema " +
					"FROM seg_agrupamento_acao A, seg_sistema B  " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.cd_sistema = " + cdSistema+
					(cdSistema>0?"  AND A.cd_sistema = "+cdSistema:"")).executeQuery());
			while(rsmAgrupamento.next())	{
				pstmt.setInt(1, rsmAgrupamento.getInt("cd_sistema"));
				pstmt.setInt(2, rsmAgrupamento.getInt("cd_sistema"));
				pstmt.setInt(3, rsmAgrupamento.getInt("cd_agrupamento"));
				rsmAgrupamento.getRegister().put("subResultSetMap", new ResultSetMap(pstmt.executeQuery()));
			}
			rsmAgrupamento.beforeFirst();

			return rsmAgrupamento;
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

	public ResultSetMap getAgrupamentosOfSistema(int cdSistema) {
		return getAgrupamentosOfSistema(cdSistema, null);
	}

	public ResultSetMap getAgrupamentosOfSistema(int cdSistema, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT * " +
					"FROM seg_agrupamento_acao " +
					"WHERE cd_sistema = "+cdSistema+
                    " ORDER BY nm_agrupamento").executeQuery());
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
	
	public static ResultSetMap getAgrupamentoById(String idAgrupamento) {
		return getAgrupamentoById(idAgrupamento, null);
	}

	public static ResultSetMap getAgrupamentoById(String idAgrupamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT * " +
					"FROM seg_agrupamento_acao " +
					"WHERE id_agrupamento = '"+idAgrupamento+"'"+
                    " ORDER BY nm_agrupamento").executeQuery());
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
	
	public static ResultSetMap getAgrupamentoByIdModulo(String idAgrupamento, int cdModulo) {
		return getAgrupamentoByIdModulo(idAgrupamento, cdModulo, null);
	}

	public static ResultSetMap getAgrupamentoByIdModulo(String idAgrupamento, int cdModulo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM seg_agrupamento_acao WHERE id_agrupamento = ? AND cd_modulo = ? ORDER BY nm_agrupamento");
			pstmt.setString(1, idAgrupamento);
			pstmt.setInt(2, cdModulo);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
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

//	public static int delete(int cdAgrupamento, int cdSistema) {
//		return delete(cdAgrupamento, cdSistema, null);
//	}
//
//	public static int delete(int cdAgrupamento, int cdSistema, Connection connection){
//		boolean isConnectionNull = connection==null;
//		try {
//			connection = isConnectionNull ? Conexao.conectar() : connection;
//			connection.setAutoCommit(isConnectionNull ? connection.getAutoCommit() : false);
//
//			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
//					"FROM seg_acao " +
//					"WHERE cd_agrupamento = ? " +
//					"  AND cd_sistema = ?");
//			pstmt.setInt(1, cdAgrupamento);
//			pstmt.setInt(2, cdSistema);
//			pstmt.setInt(3, cdSistema);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				if (AcaoServices.delete(rs.getInt("CD_ACAO"), rs.getInt("CD_MODULO"), cdSistema, connection) <= 0) {
//					if (isConnectionNull)
//						Conexao.rollback(connection);
//					return -1;
//				}
//			}
//
//			if (AgrupamentoAcaoDAO.delete(cdAgrupamento, 1 /*cdModulo*/, cdSistema, connection) <= 0) {
//				if (isConnectionNull)
//					Conexao.rollback(connection);
//				return -1;
//			}
//
//			if (isConnectionNull)
//				connection.commit();
//
//			return 1;
//		}
//		catch(Exception e){
//			e.printStackTrace(System.out);
//			System.err.println("Erro! AgrupamentoAcaoServices.delete: " +  e);
//			return -1;
//		}
//		finally{
//			if (isConnectionNull)
//				Conexao.desconectar(connection);
//		}
//	}

}