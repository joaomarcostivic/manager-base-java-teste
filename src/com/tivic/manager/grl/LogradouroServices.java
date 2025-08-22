
package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.util.DneUtilities;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class LogradouroServices {
	public static Result save(Logradouro logradouro){
		return save(logradouro, null);
	}
	
	public static Result save(Logradouro logradouro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(logradouro==null)
				return new Result(-1, "Erro ao salvar. Logradouro é nulo");
			
			int retorno;
			if(logradouro.getCdLogradouro()==0){
				retorno = LogradouroDAO.insert(logradouro, connect);
				logradouro.setCdLogradouro(retorno);
			}
			else {
				retorno = LogradouroDAO.update(logradouro, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LOGRADOURO", logradouro);
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
	
	public static Result remove(int cdLogradouro){
		return remove(cdLogradouro, false, null);
	}
	
	public static Result remove(int cdLogradouro, boolean cascade){
		return remove(cdLogradouro, cascade, null);
	}
	
	public static Result remove(int cdLogradouro, boolean cascade, Connection connect){
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
				retorno = LogradouroDAO.delete(cdLogradouro, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este logradouro está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Logradouro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir logradouro!");
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
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_logradouro ORDER BY nm_logradouro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getBairrosOfLogradouro(int cdLogradouro) {
		return getBairrosOfLogradouro(cdLogradouro, null);
	}

	public static ResultSetMap getBairrosOfLogradouro(int cdLogradouro, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM grl_logradouro_bairro A, grl_bairro B " +
																  "WHERE A.cd_bairro = B.cd_bairro " +
																  "  AND A.cd_logradouro = ?");
			pstmt.setInt(1, cdLogradouro);
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
	
	public static ResultSetMap getEnderecoByCep(String cep){
		return getEnderecoByCep(cep, null);
	}
	
	/**
	 * Faz a busca do endereço a partir de um CEP passado de uma base que está no banco de dados
	 * @param cep
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getEnderecoByCep(String cep, Connection connection){
		
		boolean isConnectionNull = connection==null;
		ResultSetMap rsm = new ResultSetMap();
		
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();	
			
			if(cep == null)
				return null;
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nr_cep", cep.replaceAll("[^0-9]+", ""), ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmCep = LogradouroCepDAO.find(criterios, connection);
			if(rsmCep.next()){
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_logradouro", String.valueOf(rsmCep.getInt("cd_logradouro")), ItemComparator.EQUAL, Types.INTEGER));
				return find(criterios, connection);
			}
			//Pesquisa pelo Dne 
			else{
				JSONObject object = new JSONObject(DneUtilities.findEnderecoByCep(cep, "json"));
				if(object == null || object.has("erro") && object.getBoolean("erro") == true) {
					return rsm;	
				}
				
				Result result = insertRegisterOnDB(object, connection);
				rsm = (ResultSetMap) result.getObjects().get("ENDERECO");
				
				return rsm;
			}
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return rsm;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getEnderecoByLogradouro(String uf, String cidade, String rua){
		return getEnderecoByLogradouro(uf, cidade, rua, null);
	}
	
	public static ResultSetMap getEnderecoByLogradouro(String uf, String cidade, String rua, Connection connection){
		
		try {		
			Object[]     objeto = new Object[]{};
			ResultSetMap rsm = new ResultSetMap();
			
			JSONArray array  = new JSONArray(DneUtilities.findEnderecoByEndereco(uf, cidade, rua, "json"));

			for(int i = 0; i<array.length(); i++){				
				JSONObject jOutput = array.getJSONObject(i);
								
				if(Boolean.valueOf(jOutput.getString("erro")) == true) {
					System.out.println("CEP inexistente ou tivemos problemas ao buscar.");
					return rsm;
				}
				System.out.println(jOutput);
//				Result result = LogradouroServices.insertRegisterOnDB(jOutput, connection);
			}		
			
			return rsm;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
		}
	}
	
	public static Result insertRegisterOnDB (JSONObject object) {
		return insertRegisterOnDB(object, null);
	}
	
	public static Result insertRegisterOnDB(JSONObject object, Connection connection) {
		boolean isConnectionNull = connection==null;
		int cdCidade     = 0;
		int cdDistrito   = 0;
		int cdBairro     = 0;
		int cdLogradouro = 0;
		
		try {
			

			connection = Conexao.conectar();
			connection.setAutoCommit(false);
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			crt.add(new ItemComparator("A.id_ibge", object.getString("ibge"), ItemComparator.EQUAL, Types.VARCHAR));
			crt.add(new ItemComparator("qtLimite", "1", ItemComparator.EQUAL, Types.INTEGER));		
			
			ResultSetMap rsmCidade = CidadeServices.find(crt, connection);			
			Estado estado = EstadoServices.getBySg(object.getString("uf"));
			
			if(rsmCidade.size() < 1) {
				cdCidade = CidadeDAO.insert(new Cidade(0, object.getString("localidade").toUpperCase(), "", new Float(0), 
						new Float(0), new Float(0), estado.getCdEstado(), object.getString("ibge"), estado.getCdRegiao(), 
						object.getString("ibge"), "", 0, 0), connection);
				if(cdCidade > 0){
					cdDistrito = DistritoDAO.insert(new Distrito(0, cdCidade, "DISTRITO SEDE", "0"), connection);
				}
				
				rsmCidade = new ResultSetMap(connection.prepareStatement("SELECT * FROM GRL_CIDADE WHERE CD_CIDADE="+cdCidade).executeQuery());
			}
			
			while(rsmCidade.next()){
				cdCidade = cdCidade != 0 ? cdCidade : rsmCidade.getInt("CD_CIDADE");
				cdDistrito = cdDistrito != 0 ? cdDistrito :  rsmCidade.getInt("CD_DISTRITO");
			}
			
			rsmCidade.beforeFirst();
			
			if (!object.getString("bairro").equals("")) {
				crt = new ArrayList<ItemComparator>();
				crt.add(new ItemComparator("A.nm_bairro", object.getString("bairro").toUpperCase(), ItemComparator.EQUAL, Types.VARCHAR));			
				ResultSetMap rsmBairro = BairroServices.find(crt, connection);
				
				if(rsmBairro.size() < 1) {
					cdBairro = BairroDAO.insert(new Bairro(0, cdDistrito, cdCidade, object.getString("bairro").toUpperCase(), "", estado.getCdEstado()), connection);
				}
				
				while(rsmBairro.next()){
					cdBairro = cdBairro != 0 ? cdBairro : rsmBairro.getInt("CD_BAIRRO");
				}
				
				rsmBairro.beforeFirst();
			}

			String nrCep = object.getString("cep").replaceAll("[^0-9]+", "");
			if (!object.getString("logradouro").equals("")) {			
				crt = new ArrayList<ItemComparator>();
				crt.add(new ItemComparator("B.nm_logradouro", object.getString("logradouro").toUpperCase(), ItemComparator.EQUAL, Types.VARCHAR));
				crt.add(new ItemComparator("A.cd_bairro", "" + cdBairro, ItemComparator.EQUAL, Types.VARCHAR));
				ResultSetMap rsmLogradouro = LogradouroBairroServices.find(crt, connection);
							
				if(rsmLogradouro.size() < 1) {
					cdLogradouro = LogradouroDAO.insert(new Logradouro(0, 0, cdCidade, 0, object.getString("logradouro").toUpperCase(), ""));
					if(cdLogradouro > 0){
						LogradouroBairroDAO.insert(new LogradouroBairro(cdBairro, cdLogradouro), connection);
					}
				}
				
				while(rsmLogradouro.next()){
					cdLogradouro = cdLogradouro != 0 ? cdLogradouro : rsmLogradouro.getInt("CD_LOGRADOURO");
				}
				
				crt = new ArrayList<ItemComparator>();
				crt.add(new ItemComparator("A.NR_CEP", nrCep, ItemComparator.EQUAL, Types.VARCHAR));		
				ResultSetMap rsmLogradouroCep = LogradouroCepServices.find(crt, connection);
				
				if(rsmLogradouroCep.size() < 1) {
					LogradouroCepDAO.insert(new LogradouroCep(cdLogradouro, nrCep, "", ""), connection);
				}
						
				rsmLogradouro.beforeFirst();
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_logradouro", String.valueOf(cdLogradouro), ItemComparator.EQUAL, Types.VARCHAR));
			
			ResultSetMap rsmLogradouro = find(criterios, connection);
			
			if(rsmLogradouro.size() <= 0){
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("CD_ESTADO", estado.getCdEstado());
				register.put("NM_ESTADO", estado.getNmEstado());
				register.put("SG_ESTADO", estado.getSgEstado());
				register.put("CD_CIDADE", rsmCidade.getLines().get(0).get("CD_CIDADE"));
				register.put("NM_CIDADE", rsmCidade.getLines().get(0).get("NM_CIDADE"));
				register.put("NR_CEP", nrCep);
				rsmLogradouro.addRegister(register);
			}
			
			connection.commit();
			
			connection.prepareStatement("DELETE FROM grl_logradouro_cep WHERE nr_cep = '"+nrCep+"'").executeUpdate();
			
			return new Result(1, "Cep ou logradouro inseridos com sucesso!", "ENDERECO", rsmLogradouro);			
		} catch(Exception e) {
			e.printStackTrace();
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

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios, Connection connect) {
		
		String limit = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
				limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			} 
		}
		ResultSetMap rsm = Search.find("SELECT DISTINCT "+
							"A.cd_logradouro, A.nm_logradouro, A.cd_regiao, " +
							"B.cd_tipo_logradouro, B.nm_tipo_logradouro, B.sg_tipo_logradouro, " +
							"B.nm_tipo_logradouro || ' ' || A.nm_logradouro AS ds_logradouro, " +
							"C.nm_cidade, " +
							"C.cd_cidade, " +
							"D.nm_distrito, " +
							"E.sg_estado, E.nm_estado, E.cd_estado, " +
							"I.nm_bairro, " +
							"G.nr_cep " +
							"FROM grl_logradouro A " +
							"LEFT OUTER JOIN grl_tipo_logradouro B ON (A.cd_tipo_logradouro = B.cd_tipo_logradouro) " +
							"LEFT OUTER JOIN grl_cidade C ON (A.cd_cidade = C.cd_cidade) " +
							"LEFT OUTER JOIN grl_distrito D ON (A.cd_distrito = D.cd_distrito AND A.cd_cidade = D.cd_cidade) " +
							"LEFT OUTER JOIN grl_estado E ON (C.cd_estado = E.cd_estado) " +
							"LEFT OUTER JOIN grl_bairro F ON (C.cd_cidade = F.cd_cidade AND A.cd_distrito = F.cd_distrito) " +
							"LEFT OUTER JOIN grl_logradouro_cep G ON (G.cd_logradouro = A.cd_logradouro) " +
							"LEFT OUTER JOIN grl_logradouro_bairro H ON (H.cd_logradouro = A.cd_logradouro) " +
							"LEFT OUTER JOIN grl_bairro I ON (I.cd_bairro = H.cd_bairro) ", 
							" " + limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);

		return rsm;
		
	}

	public static ResultSetMap getSyncData() {
		return getSyncData(null, null);
	}

	public static ResultSetMap getSyncData(GregorianCalendar dtInicial) {
		return getSyncData(dtInicial, null);
	}
		
	public static ResultSetMap getSyncData(GregorianCalendar dtInicial, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
					
			System.out.println("\n["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] Sincronizando tabela de logradouro...");
			
			String sql = "SELECT * FROM grl_logradouro";
						
			pstmt = connect.prepareStatement(sql);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			System.out.println("\tNº registro(s): "+rsm.size());
			
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
	
}