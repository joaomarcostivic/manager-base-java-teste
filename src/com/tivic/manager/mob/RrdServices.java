package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.rrd.RrdReport;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class RrdServices {
	
	public static Result sync(ArrayList<Rrd> rrds) {
		return sync(rrds, null);
	}
	
	public static Result sync(ArrayList<Rrd> rrds, Connection connect) {
		return sync(rrds, null, connect);
	}
	
	public static Result sync(ArrayList<Rrd> rrds, ArrayList<RrdAit> rrdAit, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			// Retira rrds duplicadas
			if(rrds.size()>1) {
				for (Rrd rrd : rrds) {
					for (Rrd rrdCheck : rrds) {
						if(rrd!=rrdCheck && rrd.getNrRrd()==rrdCheck.getNrRrd()) {
							rrds.remove(rrd);
							break;
						}
					}
				}
			}
			
			ArrayList<Rrd> rrdsRetorno = new ArrayList<Rrd>();
			ArrayList<Rrd> rrdsDuplicadas = new ArrayList<Rrd>();
			ArrayList<Rrd> rrdsErro = new ArrayList<Rrd>();
			
			int retorno = 0;
			for (Rrd rrd: rrds) {
				
				Result r = sync(rrd, connect);
				retorno = r.getCode();
				
				if(r.getCode()<=0) {
					rrdsErro.add(rrd);
					continue;
				}
				else if(r.getCode()==2) {
					rrdsDuplicadas.add(rrd);
					continue;
				}
				else {
					rrd.setCdRrd(r.getCode());
					rrdsRetorno.add(rrd);
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			Result r = new Result(retorno, retorno>0 ? "Sincronizado " + (rrds.size() == rrdsRetorno.size() ? " com sucesso." : " parcialmente.") : "Erro ao sincronizar RRDs.");
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar RRDs");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result sync(Rrd rrd, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			System.out.println("\n["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] Recebendo RRD...");
			System.out.println("\tNr. RRD: "+rrd.getNrRrd());
			System.out.println("\tAgente: "+AgenteDAO.get(rrd.getCdAgente(), connect).getNmAgente());
			System.out.println("\tLocalizacao: ["+rrd.getVlLatitude()+", "+rrd.getVlLongitude()+"]");
			
			int retorno = 0;
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			ResultSet rs = connect.prepareStatement(
					lgBaseAntiga ? 
						"SELECT * FROM MOB_RRD WHERE NR_RRD = " + rrd.getNrRrd(): 
						"SELECT * FROM mob_rrd WHERE nr_rrd = " + rrd.getNrRrd()
				).executeQuery();
				
			if(rs.next()) {
				retorno = 2;
				System.out.println("Diagnostico: RRD Duplicada...");
			}
			else {
				retorno = insert(rrd, connect);
				
				if(retorno > 0) {
					System.out.println("Diagnostico: RRD Recebida...");
				}
				else {
					System.out.println("Diagnostico: Erro ao inserir...");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, retorno>0 ? "Sincronizado com sucesso." : "Erro ao sincronizar RRDs.");
		}
		catch(Exception e) {
			System.out.println("Diagnostico: Erro na sincronizacao...");
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar RRDs");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int insert(Rrd objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			int code = 1;
			ResultSet rs = connect.prepareStatement(lgBaseAntiga ? "SELECT MAX(cd_rrd) as maxCode FROM mob_rrd" : 
				"SELECT MAX(cd_rrd) as maxCode FROM mob_rrd").executeQuery();
			if(rs.next())
				code = rs.getInt("maxCode") + 1;
			objeto.setCdRrd(code);
			
			String sql = "INSERT INTO mob_rrd(" +
		            	 "cd_rrd," + 
		            	 "nr_rrd," +
		            	 "dt_ocorrencia," + 
		            	 "cd_usuario," + 
		            	 "cd_agente," +
		            	 "ds_observacao," + 
		            	 "ds_local_ocorrencia," + 
		            	 "ds_ponto_referencia," + 
		            	 "vl_latitude," + 
		            	 "vl_longitude," + 
		            	 "cd_cidade," + 
		            	 "dt_regularizar, " + 
		    			 "cd_rrd_orgao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
			if(lgBaseAntiga) {
				sql = "INSERT INTO mob_rrd(" +
		            	 "cd_rrd," + 
		            	 "nr_rrd," +
		            	 "dt_ocorrencia," + 
		            	 "cd_usuario," + 
		            	 "cd_agente," +
		            	 "ds_observacao," + 
		            	 "ds_local_ocorrencia," + 
		            	 "ds_ponto_referencia," + 
		            	 "vl_latitude," + 
		            	 "vl_longitude," + 
		            	 "cd_cidade," + 
		            	 "dt_regularizar, " + 
		    			 "cd_rrd_orgao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			}
						
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, code);
			if(objeto.getNrRrd()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getNrRrd());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3, new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAgente());			
			
			pstmt.setString(6,objeto.getDsObservacao());
			pstmt.setString(7,objeto.getDsLocalOcorrencia());
			pstmt.setString(8,objeto.getDsPontoReferencia());
			
			if(objeto.getVlLatitude()==null)
				pstmt.setNull(9, Types.DOUBLE);
			else
				pstmt.setDouble(9,objeto.getVlLatitude());
			if(objeto.getVlLongitude()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setDouble(10,objeto.getVlLongitude());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdCidade());
			if(objeto.getDtRegularizar()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtRegularizar().getTimeInMillis()));
			
			pstmt.setInt(13,objeto.getCdRrdOrgao());
		
			pstmt.executeUpdate();
				
			int codeDocumentacao = 0, codeExame = 1,codeAitVinculadas = 0;
			
			//INSERIR DOCUMENTAÇÃO
			if(objeto.getDocumentacao()!=null && objeto.getDocumentacao().size()>0) {
				codeDocumentacao = RecolhimentoDocumentacaoServices.save(objeto.getDocumentacao(), objeto.getCdRrd(), 0, null, connect).getCode();
			}
			else if(objeto.getDocumentacao().size()==0){
				codeAitVinculadas = 1;
			}
			
			//INSERIR EXAMES
			if(objeto.getExame()!=null && objeto.getExame().size()>0) {
				codeExame = ExameServices.save(objeto.getExame(),objeto.getCdRrd(),0,null, connect).getCode();
			}
			else if(objeto.getExame().size()==0){
				codeAitVinculadas = 1;
			}
						
			//INSERIR AIT VINCULADAS
			if(objeto.getAitvinculadas()!=null && objeto.getAitvinculadas().size()>0) {
				
				// Utiliza as chaves candidatas (nr_ait e nr_rrd) para garantir que o vínculo esteja correto
				for (RrdAit obj : objeto.getAitvinculadas()) {
					obj.setCdAit(AitServices.getById(obj.getNrAit(), connect).getCdAit());
					obj.setCdRrd(objeto.getCdRrd());
				}
				
				codeAitVinculadas = RrdAitServices.save(objeto.getCdRrd(), objeto.getAitvinculadas(), connect).getCode();
			}
			else if(objeto.getAitvinculadas().size()==0){
				codeAitVinculadas = 1;
			}
			
			code = codeDocumentacao * codeExame * codeAitVinculadas;
						
			if(code<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return code;
				
				
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdServices.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static Result save(Rrd rRD){
		return save(rRD, null, null);
	}

	public static Result save(Rrd rRD, AuthData authData){
		return save(rRD, authData, null);
	}

	public static Result save(Rrd rRD, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			System.out.println("\t\t$"+rRD);

			if(rRD==null)
				return new Result(-1, "Erro ao salvar. Rrd é nulo");

			int retorno;
			if(rRD.getCdRrd()==0){
				retorno = RrdDAO.insert(rRD, connect);
				rRD.setCdRrd(retorno);
			}
			else {
				retorno = RrdDAO.update(rRD, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "Rrd", rRD);
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
	public static Result remove(int cdRrd){
		return remove(cdRrd, false, null, null);
	}
	public static Result remove(int cdRrd, boolean cascade){
		return remove(cdRrd, cascade, null, null);
	}
	public static Result remove(int cdRrd, boolean cascade, AuthData authData){
		return remove(cdRrd, cascade, authData, null);
	}
	public static Result remove(int cdRrd, boolean cascade, AuthData authData, Connection connect){
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
			retorno = RrdDAO.delete(cdRrd, connect);
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

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_rrd");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdServices.getAll: " + e);
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
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			int qtLimite = 0;
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
				}
			}
			
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
			
			String sql = " SELECT "+sqlLimit[0]
					   + " A.* "
					   + " FROM mob_rrd 					A"
					   + " LEFT OUTER JOIN mob_agente 		B ON (A.cd_agente = B.cd_agente)"
					   + " LEFT OUTER JOIN mob_rrd_orgao 	C ON (A.cd_rrd_orgao = C.cd_rrd_orgao)"
					   + " LEFT OUTER JOIN grl_cidade 		D ON (A.cd_cidade = D.cd_cidade)";
			
			if(Util.isStrBaseAntiga()) {
				sql = " SELECT A.*, "
					+ " B.nm_agente, B.nr_matricula, "
					+ " C.id_rrd_orgao, C.nm_rrd_orgao, "
					+ " D.nm_municipio AS nm_cidade, D.nm_uf AS sg_estado"
				    + " FROM mob_rrd 					A"
				    + " LEFT OUTER JOIN agente 		  	B ON (A.cd_agente = B.cod_agente)"
				    + " LEFT OUTER JOIN mob_rrd_orgao 	C ON (A.cd_rrd_orgao = C.cd_rrd_orgao)"
				    + " LEFT OUTER JOIN municipio 		D ON (A.cd_cidade = D.cod_municipio)";
			}
			
			ResultSetMap rsm = Search.find(sql, " ORDER BY A.nr_rrd DESC "+sqlLimit[1], criterios, connect, isConnectionNull);
			
			// TODO: buscar rsmAit para cada RRD
			while(rsm.next()) {
				rsm.setValueToField("rsmAit", getAit(rsm.getInt("cd_rrd"))/*AITS do RRD*/);
			}
			rsm.beforeFirst();
			
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdServices.find: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAit(int cdRrd) {
		return null;
	}
	
	public static Result get(int cdAit) {
		return get(cdAit, null);
	}

	public static Result get(int cdAit, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull) {
			connect = Conexao.conectar();
		}
		PreparedStatement pstmt;
		try {
			
			Rrd rrd = null;
			int resultCode = -1;
			String resultMsg = "Nenhum RRD encontrado.";
			
			pstmt = connect.prepareStatement("SELECT cd_rrd FROM mob_rrd_ait WHERE cd_ait = "+cdAit);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				int cdRrd = rs.getInt("cd_rrd");
				rrd = RrdDAO.get(cdRrd, connect);
				
				resultCode = 1;
				resultMsg = "";
			}
			
			Result result = new Result(resultCode, resultMsg, "RRD", rrd);
						
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByAit(int cdAit, Connection connect) {
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			pstmt = connect.prepareStatement("SELECT cd_rrd FROM mob_rrd_ait WHERE cd_ait = "+cdAit);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			String listaRrd = Util.join(rsm, "cd_rrd", true);
			
			Criterios crt = new Criterios();
			crt.add("A.cd_rrd", listaRrd, ItemComparator.IN, Types.INTEGER);
						
			return find(crt, connect);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdServices.getAllByAit: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * STATS
	 */
	@Deprecated
	public static Result statsQtRrd() {
		GregorianCalendar hoje = new GregorianCalendar();
		GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR)-1, Calendar.JANUARY, 1, 0, 0, 0);
		GregorianCalendar dtFinal = new GregorianCalendar(hoje.get(Calendar.YEAR), Calendar.JANUARY, 31, 0, 0, 0);
		
		return statsQtRrd(dtInicial, dtFinal, null);
	}
	
	public static Result statsQtRrd(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsQtRrd(dtInicial, dtFinal, null);
	}
	
	public static Result statsQtRrd(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection) {
	
		boolean isConnectionNull = connection == null;
		
		try {			
			if (isConnectionNull) 
				connection = Conexao.conectar();
			
			Result result = new Result(1, "");
			
			PreparedStatement ps = connection.prepareStatement(
					" SELECT COUNT(*) AS qt_rrd"
					+ " FROM mob_rrd"
					+ " WHERE dt_ocorrencia BETWEEN ? AND ?");
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));	
	
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				result.addObject("QT_RRD", rs.getInt("qt_rrd"));
			}
			
			return result;
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public byte[] imprimirRrd(int cdRrd) throws ValidationException, Exception {
		if (cdRrd <= 0) {
			throw new ValidationException("O RRD com o código " + cdRrd + " não foi encontrado.");
		}
		byte[] rrd = new RrdReport().gerar(cdRrd);
		return rrd;
	}
}