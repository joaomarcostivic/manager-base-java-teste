package com.tivic.manager.grl;

import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class CidadeServices {
	
    public static sol.util.Result init()	{
		Connection connect   = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			/***********************
			 * Importando Cidades
			 ***********************/
			
			PreparedStatement pesqNmEstado  		= connect.prepareStatement("SELECT * FROM grl_estado WHERE nm_estado = ?");
			PreparedStatement pesqNmCidade  		= connect.prepareStatement("SELECT * FROM grl_cidade WHERE nm_cidade = ? AND cd_estado = ?");
			
			System.out.println("Importando cidades de arquivo CSV...");
			//
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("/cidades.csv", ";", true);
	  		System.out.println("Arquivo carregado...");
			connect.setAutoCommit(false);
			int l = 0;
			int inserts = 0, updates = 0;
			while(rsm.next())	{
	  			if((l%500==0) && l>0)	{
					System.out.println(l+" registros gravados. [inserts:"+inserts+",updates:"+updates+"]");
					connect.commit();
					connect.close();
					System.gc();
					connect = Conexao.conectar();
					connect.setAutoCommit(false);
					pesqNmEstado = connect.prepareStatement("SELECT * FROM grl_estado WHERE nm_estado = ?");
					pesqNmCidade = connect.prepareStatement("SELECT * FROM grl_cidade WHERE nm_cidade = ? AND cd_estado = ?");
				}
	  			
	  			String nomeMunicipio = rsm.getString("NM_MUNICIP").toUpperCase();
				String idMunicipio   = rsm.getString("CD_GEOCODM");
				String nmEstado      = rsm.getString("NM_UF");
				String altitude      = rsm.getString("ALT").replaceAll(",", ".");
				String latitude      = rsm.getString("LAT").replaceAll(",", ".");
				String longitude     = rsm.getString("LONG").replaceAll(",", ".");
				
				System.out.println(nomeMunicipio + " - " + idMunicipio + " - " + nmEstado + " - " + altitude + " - " + latitude + " - " + longitude);
				
				int cdEstado = 0;
				pesqNmEstado.setString(1, nmEstado);
				ResultSet rsE = pesqNmEstado.executeQuery();
				if(rsE.next())
					cdEstado = rsE.getInt("cd_estado");
				else{
					cdEstado = com.tivic.manager.grl.EstadoDAO.insert(new Estado(0, 1, nmEstado, null, 0), connect);
					if(cdEstado <= 0){
						connect.rollback();
						return new Result(-1, "Erro ao cadastrar Estado!");
					}
				}
				int cdCidade = 0;
				pesqNmCidade.setString(1, nomeMunicipio);
				pesqNmCidade.setInt(2, cdEstado);
				ResultSet rsC = pesqNmCidade.executeQuery();
				if(rsC.next()){
					cdCidade = rsC.getInt("cd_cidade");
					Cidade cidade = CidadeDAO.get(cdCidade, connect);
					cdCidade = CidadeDAO.update(new Cidade(cidade.getCdCidade(), cidade.getNmCidade(), cidade.getNrCep(), cidade.getVlAltitude(), cidade.getVlLatitude(), cidade.getVlLongitude(), cidade.getCdEstado(),cidade.getIdCidade(), cidade.getCdRegiao(), idMunicipio/*ATUALIZA*/, cidade.getSgCidade(),cidade.getQtDistanciaCapital(),  cidade.getQtDistanciaBase()), connect);
					if(cdCidade <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar o cidade!");
					}
				}
				else{
					cdCidade = com.tivic.manager.grl.CidadeDAO.insert(new Cidade(0, nomeMunicipio, null, Float.parseFloat(altitude), Float.parseFloat(latitude), Float.parseFloat(longitude), cdEstado, idMunicipio, 0, idMunicipio, null, 0, 0), connect);
					if(cdCidade <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar o cidade!");
					}
				}
			}
			connect.commit();
			System.out.println("Importaï¿½ï¿½o de cidades concluï¿½da!");
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar cidades!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
			Conexao.desconectar(connect);
		}
	}
    
	
	public static Result save(Cidade cidade){
		return save(cidade, null);
	}
	
	public static Result save(Cidade cidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cidade==null)
				return new Result(-1, "Erro ao salvar. Cidade é nulo");
			
			int retorno;
			if(cidade.getCdCidade()==0){
				ResultSet rs = connect.prepareStatement("SELECT * FROM grl_cidade " +
						                                "WHERE nm_cidade = \'"+cidade.getNmCidade()+"\' " +
						                                (cidade.getCdEstado()>0 ? " AND cd_estado = "+cidade.getCdEstado():"")).executeQuery();
				if(rs.next()){
					Conexao.rollback(connect);
					return new Result(-100, "Já existe uma cidade com este nome no estado.");
				}
				
				retorno = CidadeDAO.insert(cidade, connect);
				cidade.setCdCidade(retorno);
			}
			else {
				retorno = CidadeDAO.update(cidade, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CIDADE", cidade);
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
	
	public static Result remove(int cdCidade){
		return remove(cdCidade, false, null);
	}
	
	public static Result remove(int cdCidade, boolean cascade){
		return remove(cdCidade, cascade, null);
	}
	
	public static Result remove(int cdCidade, boolean cascade, Connection connect){
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
				retorno = CidadeDAO.delete(cdCidade, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta cidade estï¿½ vinculada a outros registros e nï¿½o pode ser excluï¿½da!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Cidade excluï¿½da com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir cidade!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}
	
	public static ResultSetMap getAll(int limit) {
		return getAll(limit, null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_estado, B.sg_estado " +
					                         "FROM grl_cidade A " +
					                         "LEFT OUTER JOIN grl_estado B ON (A.cd_estado = B.cd_estado) " +
					                         "ORDER BY A.nm_cidade " + 
					                         //ordenaï¿½ï¿½o no firebird
					                         (Util.getConfManager().getIdOfDbUsed().equals("FB")?"collate pt_BR":"") );
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll(int limit, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(limit, 0);
			pstmt = connect.prepareStatement("SELECT "+ sqlLimit[0] +
											 " A.*, B.nm_estado, B.sg_estado " +
					                         " FROM grl_cidade A " +
					                         " LEFT OUTER JOIN grl_estado B ON (A.cd_estado = B.cd_estado) " +
					                         " ORDER BY A.nm_cidade " + sqlLimit[1] +
					                         //ordenaï¿½ï¿½o no firebird
					                         (Util.getConfManager().getIdOfDbUsed().equals("FB")?"collate pt_BR":"") );
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllDistritosOfCidade(int cdCidade) {
		return getAllDistritosOfCidade(cdCidade, null);
	}

	public static ResultSetMap getAllDistritosOfCidade(int cdCidade, Connection connection) {
		boolean isConnectioNull = connection==null;
		try {
			if (isConnectioNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM GRL_DISTRITO WHERE cd_cidade = ?");
			pstmt.setInt(1, cdCidade);
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

	/**
	 * Sincronização para O eTransito Mobile
	 * @return
	 */
	public static ResultSetMap getSyncData() {
		return getSyncData(null, null);
	}
	
	/**
	 * Sincronização para O eTransito Mobile
	 * @param dtInicial
	 * @return
	 */
	public static ResultSetMap getSyncData(GregorianCalendar dtInicial) {
		return getSyncData(dtInicial, null);
	}

	/**
	 * Sincronização para O eTransito Mobile
	 * @param dtInicial
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getSyncData(GregorianCalendar dtInicial, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			System.out.println("\n["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] Sincronizando tabelas de cidades...");
			
			//LogUtils.debug("CidadeServices.getSyncData");
			//LogUtils.debug("dtInicial: "+Util.convCalendarStringCompleto(dtInicial));
			
			String sql = "SELECT A.*, B.sg_estado FROM grl_cidade A, grl_estado B WHERE A.cd_estado = B.cd_estado " + (dtInicial != null ? " AND dt_atualizacao >= '" + Util.convCalendarStringSql(dtInicial) + "'" : "");
					
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT A.*, A.cod_municipio as cd_cidade, A.nm_municipio as nm_cidade, A.NM_UF as sg_estado FROM MUNICIPIO A " + (dtInicial != null ? " WHERE dt_atualizacao >= '" + Util.convCalendarStringSql(dtInicial) + "'" : "");
			
			//LogUtils.debug("SQL:\n"+Search.formatSQL(sql));
			
			pstmt = connect.prepareStatement(sql);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			System.out.println("\tNï¿½ registro(s): "+rsm.size());
			
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
	
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, -1, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connection) {
		return find(criterios, -1, connection);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, int maxRegistros) {
		return find(criterios, maxRegistros, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, int maxRegistros, Connection connect) {
		String nmCidade = "";
		String qtLimite = ""; 
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().toLowerCase().indexOf("nm_cidade")>=0) {
				nmCidade = Util.limparTexto(criterios.get(i).getValue());
				nmCidade = "%"+nmCidade.replaceAll(" ", "%")+"%";
				//criterios.get(i).setValue(nmCidade);
				//crt.add(criterios.get(i));
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtLimite = criterios.get(i).getValue();
			}
			else
				crt.add(criterios.get(i));
		}

		ResultSetMap rsm = Search.find("SELECT A.*, B.nm_estado, B.sg_estado, C.nm_pais, C.sg_pais, A.nm_cidade AS NM_NATURALIDADE, B.sg_estado As SG_ESTADO_NATURALIDADE, " +
									   " CONCAT(A.nm_cidade, ' - ', B.sg_estado) AS ds_cidade, " +
									   " CONCAT(A.nm_cidade, ' - ', B.sg_estado) AS ds_naturalidade, " +
									   " A.nm_cidade AS nm_cidade_condutor, A.cd_cidade AS cd_cidade_condutor " +
							           "FROM grl_cidade A " +
							           "LEFT OUTER JOIN grl_estado B ON (A.cd_estado = B.cd_estado) " +
							           "LEFT OUTER JOIN grl_pais   C ON (B.cd_pais = C.cd_pais) " +
							           "WHERE 1=1 " +
							           (!nmCidade.equals("") && !Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
							        		   " AND TRANSLATE(nm_cidade, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE \'"+nmCidade+"\'" : ""),
							           "ORDER BY A.nm_cidade" + (!qtLimite.equals("") ? " LIMIT " + qtLimite : ""), crt, connect!=null ? connect : Conexao.conectar(), connect==null);
 		//
 		maxRegistros = maxRegistros==-1 ? maxRegistros : maxRegistros==0 ? 500 : maxRegistros;
 		if (maxRegistros>0 && rsm.size() > maxRegistros) {
 			rsm = new ResultSetMap();
 			HashMap<String,Object> register = new HashMap<String,Object>();
			register.put("NM_CIDADE", "Refine seus critérios de busca. Os resultados retornados pela pesquisa estï¿½o acima do permitido.");
			register.put("NM_ESTADO", "Refine seus critérios de busca. Os resultados retornados pela pesquisa estï¿½o acima do permitido.");
			register.put("NM_PAIS", "Refine seus critérios de busca. Os resultados retornados pela pesquisa estï¿½o acima do permitido.");
			register.put("SG_ESTADO", "Refine seus critérios de busca. Os resultados retornados pela pesquisa estï¿½o acima do permitido.");
			register.put("SG_PAIS", "Refine seus critérios de busca. Os resultados retornados pela pesquisa estï¿½o acima do permitido.");
			register.put("ID_CIDADE", "Refine seus critérios de busca. Os resultados retornados pela pesquisa estï¿½o acima do permitido.");
			rsm.addRegister(register);
 		}
		
 		return rsm;
	}
	
	public static Cidade getById(String idCidade) {
		return getById(idCidade, null);
	}

	public static Cidade getById(String idCidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_cidade WHERE id_ibge=?");
			pstmt.setString(1, idCidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return CidadeDAO.get(rs.getInt("cd_cidade"), connect);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ArrayList<Cidade> getBySgBaseAntiga(String sgEstado, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			ArrayList<Cidade> arrayCidades = new ArrayList<Cidade>();
			pstmt = connect.prepareStatement("SELECT * FROM municipio WHERE nm_uf=? ORDER BY nm_municipio");
			pstmt.setString(1, sgEstado);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Cidade cidade = new Cidade();
				cidade.setCdCidade(rs.getInt("cod_municipio"));
				cidade.setNmCidade(rs.getString("nm_municipio"));
				cidade.setSgCidade(rs.getString("nm_uf"));
				arrayCidades.add(cidade);
			}
			
			return arrayCidades;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeServices.getBySgBaseAntiga: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeServices.getBySgBaseAntiga: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Cidade getByCodMunicipio(int codMunicipio){
		return getByCodMunicipio(codMunicipio, null);
	}
	
	public static Cidade getByCodMunicipio(int codMunicipio, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			Cidade cidade = new Cidade();
			pstmt = connect.prepareStatement("SELECT * FROM municipio WHERE cod_municipio=?");
			pstmt.setInt(1, codMunicipio);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				cidade.setCdCidade(rs.getInt("cod_municipio"));
				cidade.setNmCidade(rs.getString("nm_municipio"));
				cidade.setSgCidade(rs.getString("nm_uf"));
			}
			
			return cidade;			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeServices.getByCodMunicipio: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeServices.getByCodMunicipio: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Cidade getByIdCidade(String idCidade){
		return getByIdCidade(idCidade, null);
	}
	
	public static Cidade getByIdCidade(String idCidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_cidade WHERE id_cidade=?");
			pstmt.setString(1, Util.fillNum(Integer.parseInt(idCidade), 5));
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				return CidadeDAO.get(rs.getInt("cd_cidade"));
			}
			
			return null;			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeServices.getByIdCidade: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeServices.getByIdCidade: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Cidade getCidade(int cdCidade) {
		return getCidade(cdCidade, null);
	}
	
	public static Cidade getCidade(int cdCidade, Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			if(lgBaseAntiga)
				return getByCodMunicipio(cdCidade, connection);
			else
				return CidadeDAO.get(cdCidade, connection);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeServices.getCidade: " + e);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
		
	}
	
	public static Cidade getByNmCidade(String nmCidade) {
		return getByNmCidade(nmCidade, null);
	}
	
	public static Cidade getByNmCidade(String nmCidade, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			String sql = "SELECT * FROM grl_cidade WHERE nm_cidade=?";
			
			if(lgBaseAntiga)
				sql = "SELECT * FROM municipio WHERE nm_municipio=?";
			
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, nmCidade);
			ResultSet rs = pstmt.executeQuery();
			
			Cidade cidade = new Cidade();
			if(rs.next()){
				if(lgBaseAntiga) {
					cidade.setCdCidade(rs.getInt("cod_municipio"));
					cidade.setNmCidade(rs.getString("nm_municipio"));
					cidade.setSgCidade(rs.getString("nm_uf"));
				}
				else {
					cidade.setCdCidade(rs.getInt("cd_cidade"));
					cidade.setNmCidade(rs.getString("nm_cidade"));
					cidade.setSgCidade(rs.getString("sg_cidade"));
				}
			}
			
			return cidade;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeServices.getByNmCidade: " + e);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap findByCidadeEstado(ArrayList<ItemComparator> criterios) {
		return findByCidadeEstado(criterios, -1, null);
	}
	public static ResultSetMap findByCidadeEstado(ArrayList<ItemComparator> criterios, int maxRegistros, Connection connect) {
		String nmCidade = "";
		String qtLimite = ""; 
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().toLowerCase().indexOf("nm_cidade")>=0) {
				nmCidade = Util.limparTexto(criterios.get(i).getValue());
				nmCidade = "%"+nmCidade.replaceAll(" ", "%")+"%";
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtLimite = criterios.get(i).getValue();
			}
			else
				crt.add(criterios.get(i));
		}
		
		ResultSetMap rsm = Search.find("SELECT A.*, B.nm_estado, B.sg_estado, C.nm_pais, C.sg_pais, A.nm_cidade AS NM_NATURALIDADE, B.sg_estado As SG_ESTADO_NATURALIDADE, " +
							           "FROM grl_cidade A " +
							           "LEFT OUTER JOIN grl_estado B ON (A.cd_estado = B.cd_estado) " +
							           "LEFT OUTER JOIN grl_pais   C ON (B.cd_pais = C.cd_pais) " +
							           "WHERE 1=1 " +
							           " AND (("
							           + "TRANSLATE(nm_cidade, 'ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE \'"+nmCidade+"\'"
							           + ") OR ("
							           + "TRANSLATE(b.nm_estado, 'ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE \'"+nmCidade+"\'"
							           + "))",
							           "ORDER BY A.nm_cidade" + (!qtLimite.equals("") ? " LIMIT " + qtLimite : ""), crt, connect!=null ? connect : Conexao.conectar(), connect==null);
 		//
 		maxRegistros = maxRegistros==-1 ? maxRegistros : maxRegistros==0 ? 500 : maxRegistros;
 		if (maxRegistros>0 && rsm.size() > maxRegistros) {
 			rsm = new ResultSetMap();
 			HashMap<String,Object> register = new HashMap<String,Object>();
			register.put("NM_CIDADE", "Refine seus critérios de busca. Os resultados retornados pela pesquisa estï¿½o acima do permitido.");
			register.put("NM_ESTADO", "Refine seus critérios de busca. Os resultados retornados pela pesquisa estï¿½o acima do permitido.");
			register.put("NM_PAIS", "Refine seus critérios de busca. Os resultados retornados pela pesquisa estï¿½o acima do permitido.");
			register.put("SG_ESTADO", "Refine seus critérios de busca. Os resultados retornados pela pesquisa estï¿½o acima do permitido.");
			register.put("SG_PAIS", "Refine seus critérios de busca. Os resultados retornados pela pesquisa estï¿½o acima do permitido.");
			register.put("ID_CIDADE", "Refine seus critérios de busca. Os resultados retornados pela pesquisa estï¿½o acima do permitido.");
			rsm.addRegister(register);
 		}
		
 		return rsm;
	}
	
	public static ResultSetMap findByCdCidade(int cdCidade) {
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("CD_CIDADE", String.valueOf(cdCidade), ItemComparator.EQUAL, Types.INTEGER));
		
		return find(criterios, -1, null);
	}
	
	public static ResultSetMap getAllCidadeOrgao() {
		Connection connection = Conexao.conectar();
		try {
			
			ResultSetMap rsmCidadeOrgao = new ResultSetMap(connection
					.prepareStatement("SELECT cd_cidade FROM mob_orgao GROUP BY cd_cidade")
					.executeQuery());
			
			ResultSetMap rsm = new ResultSetMap();
			
			if(rsmCidadeOrgao.next()) {
				
				rsm = new ResultSetMap(connection
						.prepareStatement("SELECT A.*, CONCAT(A.nm_cidade, ' - ', B.sg_estado) AS ds_cidade "
										+ " FROM grl_cidade A "
										+ " LEFT OUTER JOIN grl_estado B ON (A.cd_estado = B.cd_estado)"
										+ " WHERE A.cd_cidade IN ("+Util.join(rsmCidadeOrgao, "cd_cidade", true)+")"
										+ " ORDER BY A.nm_cidade")
						.executeQuery());
			}
			
			return rsm;
			
		}
		catch(Exception e) {
			Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getCidadeDTO(int cdCidade) {
		return getCidadeDTO(cdCidade, null);
	}
	
	public static ResultSetMap getCidadeDTO(int cdCidade, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.NM_ESTADO, B.SG_ESTADO FROM GRL_CIDADE A INNER JOIN GRL_ESTADO B " +
															   "ON (A.CD_ESTADO = B.CD_ESTADO) WHERE CD_CIDADE = ?");
			
			pstmt.setInt(1, cdCidade);
			
			return new ResultSetMap(pstmt.executeQuery());
		}catch( Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}