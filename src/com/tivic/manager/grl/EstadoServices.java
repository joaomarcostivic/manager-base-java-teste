package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.ItemComparator;
import sol.util.Result;

public class EstadoServices {
	
	public static void init()	{
		/*
		 * Estados
		 */
		int cdPais = PaisServices.init();
		ArrayList<Estado> estados = new ArrayList<Estado>();
		estados.add(new Estado(0,cdPais,"ACRE","AC",0));
		estados.add(new Estado(0,cdPais,"ALAGOAS","AL",0));
		estados.add(new Estado(0,cdPais,"AMAPÁ","AP",0));
		estados.add(new Estado(0,cdPais,"AMAZONAS","AM",0));
		estados.add(new Estado(0,cdPais,"BAHIA","BA",0));
		estados.add(new Estado(0,cdPais,"CEARÁ","CE",0));
		estados.add(new Estado(0,cdPais,"DISTRITO FEDERAL","DF",0));
		estados.add(new Estado(0,cdPais,"ESPÍRITO SANTO","ES",0));
		estados.add(new Estado(0,cdPais,"GOIÁS","GO",0));
		estados.add(new Estado(0,cdPais,"MARANHÃO","MA",0));
		estados.add(new Estado(0,cdPais,"MATO GROSSO","MT",0));
		estados.add(new Estado(0,cdPais,"MATO GROSSO DO SUL","MS",0));
		estados.add(new Estado(0,cdPais,"MINAS GERAIS","MG",0));
		estados.add(new Estado(0,cdPais,"PARÁ","PA",0));
		estados.add(new Estado(0,cdPais,"PARAÍBA","PB",0));
		estados.add(new Estado(0,cdPais,"PARANÁ","PR",0));
		estados.add(new Estado(0,cdPais,"PERNAMBUCO","PE",0));
		estados.add(new Estado(0,cdPais,"PIAUÍ","PI",0));
		estados.add(new Estado(0,cdPais,"RIO DE JANEIRO","RJ",0));
		estados.add(new Estado(0,cdPais,"RIO GRANDE DO NORTE","RN",0));
		estados.add(new Estado(0,cdPais,"RIO GRANDE DO SUL","RS",0));
		estados.add(new Estado(0,cdPais,"RONDÔNIA","RO",0));
		estados.add(new Estado(0,cdPais,"RORAIMA","RR",0));
		estados.add(new Estado(0,cdPais,"SANTA CATARINA","SC",0));
		estados.add(new Estado(0,cdPais,"SÃO PAULO","SP",0));
		estados.add(new Estado(0,cdPais,"SERGIPE","SE",0));
		estados.add(new Estado(0,cdPais,"TOCANTINS","TO",0));
		//
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_estado WHERE sg_estado = ?");
			for(int i=0; i<estados.size(); i++)	{
				pstmt.setString(1, estados.get(i).getSgEstado());
				if(!pstmt.executeQuery().next())
					EstadoDAO.insert(estados.get(i), connect);
			}
			System.out.println("Inicialização de Estados concluida!");
			return;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static Result save(Estado estado){
		return save(estado, null);
	}
	
	public static Result save(Estado estado, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(estado==null)
				return new Result(-1, "Erro ao salvar. Estado é nulo");
			
			int retorno;
			if(estado.getCdEstado()==0)	{
				retorno = EstadoDAO.insert(estado, connect);
				estado.setCdEstado(retorno);
			}
			else 
				retorno = EstadoDAO.update(estado, connect);
			
			if(retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ESTADO", estado);
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdEstado){
		return remove(cdEstado, false, null);
	}
	
	public static Result remove(int cdEstado, boolean cascade){
		return remove(cdEstado, cascade, null);
	}
	
	public static Result remove(int cdEstado, boolean cascade, Connection connect){
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
				retorno = EstadoDAO.delete(cdEstado, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este estado está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Estado excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir estado!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
			
			String sql = "SELECT A.*, B.nm_regiao, C.nm_pais, C.sg_pais, (sg_estado||'-'||nm_estado) AS sg_nm_estado " +
                    "FROM grl_estado A " +
                    "LEFT OUTER JOIN grl_regiao B ON (A.cd_regiao = B.cd_regiao) " +
                    "LEFT OUTER JOIN grl_pais   C ON (A.cd_pais   = C.cd_pais) " +
                    "ORDER BY sg_estado";
			
			if(lgBaseAntiga)
				sql = "SELECT A.*, (sg_estado||'-'||nm_estado) AS sg_nm_estado " +
	                    "FROM grl_estado A " +
	                    "ORDER BY sg_estado";
			
			PreparedStatement pstmt = connect.prepareStatement(sql);			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			System.err.println("Erro! EstadoServices.getAll: " + e);
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
		if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1")) {
			return Search.find(
	 				   "SELECT A.* "+
			           "FROM grl_estado A " +
			           "ORDER BY sg_estado",
			           criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		String nmEstado = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("NM_ESTADO")) {
				nmEstado =	Util.limparTexto(criterios.get(i).getValue());
				nmEstado = nmEstado.trim();
				criterios.remove(i);
				i--;
			}
		}
		return Search.find(
				   "SELECT A.*, B.nm_pais, B.sg_pais, C.nm_regiao " +
		           "FROM grl_estado A " +
		           "LEFT OUTER JOIN grl_pais   B ON (A.cd_pais = B.cd_pais) "+
		           "LEFT OUTER JOIN grl_regiao C ON (A.cd_regiao = C.cd_regiao) "+
		        	(!nmEstado.equals("") ?  
		        	"WHERE TRANSLATE (nm_estado, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmEstado)+"%' " 
		        	: ""),
		        	"ORDER BY nm_pais, sg_estado",
		           criterios, connect!=null ? connect : Conexao.conectar(), connect==null);	
 		
	}

	public static ResultSetMap getCidadesByEstado(int cdEstado) {
		return getCidadesByEstado(cdEstado, null);
	}

	public static ResultSetMap getCidadesByEstado(int cdEstado, Connection connection) {
		boolean isConnectioNull = connection==null;
		try {
			if (isConnectioNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM GRL_CIDADE WHERE cd_estado = ? ORDER BY nm_cidade");
			pstmt.setInt(1, cdEstado);

			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectioNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static String getEstadoByCdEmpresa(int cdEmpresa) {
		return getEstadoByCdEmpresa(cdEmpresa, null);
	}
	public static String getEstadoByCdEmpresa(int cdEmpresa, Connection connection) {
		boolean isConnectioNull = connection==null;
		try {
			String sgUF = "";
			if (isConnectioNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT nm_pessoa, sg_estado FROM grl_pessoa A " + 
															      "LEFT OUTER JOIN grl_pessoa_juridica  	  B ON (A.cd_pessoa = B.cd_pessoa) "+ 
															      "LEFT OUTER JOIN grl_pessoa_endereco   	  C ON (A.cd_pessoa = C.cd_pessoa AND lg_principal = 1) "+ 
															      "LEFT OUTER JOIN grl_cidade            	  D ON (C.cd_cidade     = D.cd_cidade) " + 
															      "LEFT OUTER JOIN grl_estado            	  E ON (E.cd_estado     = D.cd_estado) " + 
															      "WHERE A.cd_pessoa = ? ");	
			pstmt.setInt(1, cdEmpresa);		
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if (rsm.next())
				sgUF = rsm.getString("sg_estado");
			return sgUF; 
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectioNull)
				Conexao.desconectar(connection);
		}
	}

	public static Estado getBySg(String sgEstado) {
		return getBySg(sgEstado, null);
	}
	
	public static Estado getBySg(String sgEstado, Connection connection) {
		boolean isConnectioNull = connection==null;
		try {
			if (isConnectioNull)
				connection = Conexao.conectar();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("sg_estado", sgEstado, ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmEstado = EstadoDAO.find(criterios, connection);
			Estado estado = null;
			if(rsmEstado.next()){
				estado = EstadoDAO.get(rsmEstado.getInt("cd_estado"), connection);
			}
			return estado; 
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectioNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Estado getEstadoOrgaoAutuador() {
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Cidade cidade = CidadeDAO.get(orgao.getCdCidade());
		return EstadoDAO.get(cidade.getCdEstado());
	}
}
