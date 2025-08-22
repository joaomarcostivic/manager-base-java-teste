package com.tivic.manager.seg;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import sol.util.Result;
import sol.util.HashServices;
import sol.util.DateServices;
import sol.util.HardwareManager;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Empresa;

public class ChaveLicencaServices {
	
	/**
	 * Validações de dados e geração da chave de ativação.
	 * @author Edgard Hufelande
	 * @param cdLicenca
	 * @param dtExpiracao
	 * @return
	 */	
	public static Result gerarChave(int cdLicenca, GregorianCalendar dtExpiracao){
		return gerarChave(cdLicenca, dtExpiracao, null);
	}
	
	public static Result gerarChave(int cdLicenca, GregorianCalendar dtExpiracao, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}			
			
			/* Verifica se a licença é existente na base de dados */
			Licenca lic = LicencaDAO.get(cdLicenca, connect);
			
			if(lic==null) {
				return new Result(-1, "A licenca indicada não existe.");
			}
			
			/* Definindo strings que serão usadas para a geração da chave e validações */
			String idUnico       =  lic.getIdUnico();					
			String dtInMillis    =  String.valueOf(dtExpiracao.getTimeInMillis());
			String newKey        =  newKey(idUnico, dtInMillis);
			
			/* Fazendo validações básicas para que a chave possa ser inserida na base de dados */
			if(Long.valueOf(dtInMillis) == null) {
				return new Result(-2, "A data de expiração não foi informada.");
			}			
			
			if(DateServices.countDaysBetween(new GregorianCalendar(), dtExpiracao) < 1) {
				return new Result(-3, "A data de expiração é menor do que a data atual.");
			}
			
			// Verifica se há uma chave com a mesma característica já registrado na base de dados.
			ChaveLicenca key  = ChaveLicencaServices.getByChave(cdLicenca, newKey, connect);
			if(key!=null) {
				return new Result(-5, "Esta chave já está registrada para esse mesmo ID Único.");
			}

			
			key = new ChaveLicenca(0, cdLicenca, newKey, new GregorianCalendar(), dtExpiracao);
			Result result = ChaveLicencaServices.save(key, connect);
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, e.getMessage());
		}
		finally{
		}
	}
	
	public static Result getChaveByIdUnico(Empresa empresa) {
		
		String id = HashServices.md5(HardwareManager.getSerialNumber());
				
		Licenca lic = LicencaServices.getLicencaById(id);
		
		if(lic==null) { //não havendo a licenca, primeira utilização, cria-se;
			
			lic = new Licenca(0, empresa.getCdPessoa(), id, new GregorianCalendar());
			
			Result r = LicencaServices.save(lic);
			
			if(r.getCode()<=0) {
				return new Result(-1, "Erro ao gerar a primeira licença da empresa.");
			}
		}
		
		ChaveLicenca key = ChaveLicencaServices.getByIdUnico(id);
		
		if(key==null) {
			return new Result(-3, "Não existe ainda uma chave para o id indicado.");
		}
		
		Long timeInMillis = Long.valueOf(decryptKey(key.getTxtChave(), 16, 29));
		
		GregorianCalendar Calendar = new GregorianCalendar();
		Calendar.setTimeInMillis(timeInMillis);
		int  countDays = DateServices.countDaysBetween(new GregorianCalendar(), Calendar);
		
		if(countDays <= 10 && countDays > 0){
			return new Result(-2, "O período da sua licença atual do sistema irá expirar em " + (countDays <= 1 ? countDays + " dia" : countDays + " dias") + ".");
		}	
		
		if(countDays < 1){
			return new Result(-3, "O período da sua licença expirou.");
		}
		
		return validarChave(key.getTxtChave()); 
	}
	
	/**
	 * Interpretar a chave e verificar se é uma chave válida.
	 * @author Edgard Hufelande
	 * @param cdLicenca
	 * @param txtChave
	 * @return
	 */ 	
	public static Result validarChave(String txtChave){
		return validarChave(txtChave, null);
	}	
	
	public static Result validarChave(String txtChave, Connection connect){
		
		/* Convertendo a chave em Long */
		Long timeInMillis = Long.valueOf(decryptKey(txtChave, 16, 29));
		
		/* Convertendo a chave em Long para GregorianCalendar e identificar a data de expiração */
		GregorianCalendar Calendar = new GregorianCalendar();
		Calendar.setTimeInMillis(timeInMillis);
		
		/* Verificando se a chave está apenas em letras de caixa alta. */
		if(!txtChave.matches("[A-Z]+")){
			return new Result(-1, "A característica da chave é inválida.");
		}
		
		Licenca rsLicenca = LicencaServices.getLicencaByIdUnico(HashServices.md5(HardwareManager.getSerialNumberWin()), connect);
		
		/* Verifica se a licença é existente na base de dados */
		Licenca lic = LicencaDAO.get(rsLicenca.getCdLicenca(), connect);
		
		if(lic==null) {
			return new Result(-4, "A licenca indicada não existe.");
		}

		// Verifica se a data de expiração da chave já passou.
		if(DateServices.countDaysBetween(new GregorianCalendar(), Calendar) < 1) {
			return new Result(-5, "A data de expiração está ultrapassada.");
		}
		
		// Verifica se há uma chave com a mesma característica já registrado na base de dados.
		ChaveLicenca key  = ChaveLicencaServices.getByChave(rsLicenca.getCdLicenca(), txtChave, connect);
		if(key!=null) {
			return new Result(-6, "Esta chave já está registrada.");
		}

		String newKey = newKey(lic.getIdUnico(), timeInMillis.toString());

		if(!newKey.equals(txtChave)) {
			return new Result(-7, "A chave informada não é uma chave válida.");
		}
		
		Result save = ChaveLicencaServices.saveChave(rsLicenca.getCdLicenca(), txtChave, new GregorianCalendar(), Calendar, connect);
				
		/* Verifica se a chave foi inserida na base de dados. */
		if(save.getCode() <= 0) {
			return new Result(-8, "Houve um problema ao efetuar a ativação da chave.", "KEY", newKey);
		}
		
		return new Result(1, "Chave de ativação validada e aplicada!", "KEY", newKey);
	}

	public static Result save(ChaveLicenca chaveLicenca){
		return save(chaveLicenca, null);
	}

	public static Result save(ChaveLicenca chaveLicenca, Connection connect){
		boolean isConnectionNull = connect==null;
		
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(chaveLicenca==null)
				return new Result(-1, "Erro ao salvar. ChaveLicenca é nulo");
			
			int retorno;
			if(chaveLicenca.getCdChave()==0){
				retorno = ChaveLicencaDAO.insert(chaveLicenca, connect);
				chaveLicenca.setCdChave(retorno);
			}
			else {
				retorno = ChaveLicencaDAO.update(chaveLicenca, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CHAVELICENCA", chaveLicenca);
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
	
	public static Result remove(int cdChave, int cdLicenca){
		return remove(cdChave, cdLicenca, false, null);
	}
	
	public static Result remove(int cdChave, int cdLicenca, boolean cascade){
		return remove(cdChave, cdLicenca, cascade, null);
	}
	
	public static Result remove(int cdChave, int cdLicenca, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ChaveLicenca chave = ChaveLicencaDAO.get(cdChave, cdLicenca, connect);
			if(chave==null){
				return new Result(-2, "Esta chave não existe!");
			}
			
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			
			if(!cascade || retorno>0)
				retorno = ChaveLicencaDAO.delete(cdChave, cdLicenca, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-3, "Este registro está vinculado a outros e não pode ser excluído!");
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_chave_licenca");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM seg_chave_licenca", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	/**
	 * Verificar se a chave já está registrada na base de dados
	 * @author Edgard Hufelande
	 * @param txtChave
	 * @param cdLicenca
	 * @return
	 */
	public static ChaveLicenca getByChave (int cdLicenca, String txtChave) {
		return getByChave(cdLicenca, txtChave, null);
	}

	public static ChaveLicenca getByChave (int cdLicenca, String txtChave, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_chave_licenca WHERE txt_chave=? and cd_licenca=?");
			pstmt.setString(1, txtChave);
			pstmt.setInt(2, cdLicenca);
			rs = new ResultSetMap(pstmt.executeQuery());
			if(rs.next()){
				
				return new ChaveLicenca(rs.getInt("cd_chave"),
						rs.getInt("cd_licenca"),
						rs.getString("txt_chave"),
						(rs.getTimestamp("dt_criacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_criacao").getTime()),
						(rs.getTimestamp("dt_expiracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_expiracao").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Verifica se a chave já está registrada na base de dados
	 * @author Edgard Hufelande
	 * @param cdLicenca
	 * @return
	 */
	public static ChaveLicenca getByLicenca(int cdLicenca) {
		return getByLicenca(cdLicenca, null);
	}

	public static ChaveLicenca getByLicenca (int cdLicenca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_chave_licenca WHERE cd_licenca=?");
			pstmt.setInt(1, cdLicenca);
			rs = pstmt.executeQuery();			
			if(rs.next()){
				return new ChaveLicenca(rs.getInt("cd_chave"),
						rs.getInt("cd_licenca"),
						rs.getString("txt_chave"),
						(rs.getTimestamp("dt_criacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_criacao").getTime()),
						(rs.getTimestamp("dt_expiracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_expiracao").getTime()));
			} else {
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaServices.getByLicenca: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaServices.getByLicenca: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Inserir uma string dentro de outra string.
	 * @author Edgard Hufelande
	 * @param string
	 * @param valor
	 * @param divisor
	 * @return string
	 */
	
	public static String meioString(String string, String valor, int divisor){
		Double i = Math.floor(string.length() / divisor);
		return string.substring(0, i.intValue()).concat(valor).concat(string.substring(i.intValue()));
	}
	
	/**
	 * Gerar uma chave
	 * @author Edgard Hufelande
	 * @param string
	 * @param valor
	 * @param divisor
	 * @return string
	 */
	
	public static String newKey(String idUnico, String dtInMillis) {
		String idHash        =  HashServices.md5(idUnico + dtInMillis);
		String k             =  meioString(idHash, dtInMillis, 2);
		String newKey        =  "";

		/* Gerando e formatando a chave da licença */
		for (int i = 0; i < k.length(); i++) {
			try {
				newKey += (char)(65+Integer.valueOf(""+k.charAt(i)).intValue());
			}
			catch(Exception e) {
				newKey += k.charAt(i);
			}
		}
		return newKey.toUpperCase();
	}
	
	/**
	 * Converter a chave para decimal, capturar a data de expiração e transformar em GregorianCalendar.
	 * @author Edgard Hufelande
	 * @param cdLicenca
	 * @param txtChave
	 * @return
	 */
	
	public static String decryptKey(String txtChave, int inicio, int fim){
		String dtExpiracao = txtChave.substring(inicio, fim);
		String dateInMillis = "";
		/* Convertendo a string da data de expiração retirada da chave em Decimal */
		for (int i = 0; i < dtExpiracao.length(); i++){
		    char c = dtExpiracao.charAt(i);
		    int value = c;
		    dateInMillis += value-65;
		}
		
		/* Converter o time da data de expiração em GregorianCalendar para fazer a comparação */
		GregorianCalendar timeInMillis = new GregorianCalendar();
		timeInMillis.setTimeInMillis(new Long(dateInMillis));
		return dateInMillis;
	}
	
	/**
	 * Verificar se a chave já está registrada na base de dados
	 * @author Edgard Hufelande
	 * @param txtChave
	 * @param cdLicenca
	 * @return
	 */

	public static ChaveLicenca getByIdUnico (String idUnico) {
		return getByIdUnico(idUnico, null);
	}

	public static ChaveLicenca getByIdUnico (String idUnico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rs;
		try {
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(1, 0);
			pstmt = connect.prepareStatement(
					"SELECT "+sqlLimit[0]+" A.id_unico, A.cd_licenca, B.txt_chave, B.dt_criacao, B.dt_expiracao " + 
					"FROM seg_licenca A " +
					"JOIN seg_chave_licenca    B ON A.cd_licenca = B.cd_licenca " +
					"WHERE A.id_unico=? " +
					"ORDER BY B.dt_criacao DESC "
					);
			pstmt.setString(1, idUnico);
			rs = new ResultSetMap(pstmt.executeQuery());
			if(rs.next()){				
				return new ChaveLicenca(rs.getInt("cd_chave"),
						rs.getInt("cd_licenca"),
						rs.getString("txt_chave"),
						(rs.getTimestamp("dt_criacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_criacao").getTime()),
						(rs.getTimestamp("dt_expiracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_expiracao").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChaveLicencaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveChave(int cdLicenca, String newKey, GregorianCalendar dtCriacao, GregorianCalendar dtExpiracao, Connection connect){
		
		if(cdLicenca == 0) {
			return new Result(-1, "A Licença é nula.");
		}
		
		if(newKey == null || newKey == "" || !newKey.matches("[a-zA-Z]+")) {
			return new Result(-2, "A Chave está vazia ou nula.");
		}
		
		ChaveLicenca key = ChaveLicencaServices.getByLicenca(cdLicenca);
		
		if(key!=null) {
			key.setTxtChave(newKey);
			key.setDtCriacao(new GregorianCalendar());
			key.setDtExpiracao(dtExpiracao);
		}	
		else
			key = new ChaveLicenca(0, cdLicenca, newKey, new GregorianCalendar(), dtExpiracao);
		
		Result result = ChaveLicencaServices.save(key, connect);
		return new Result(1, result.toString());
		
	}
}
