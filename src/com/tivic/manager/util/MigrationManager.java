package com.tivic.manager.util;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.util.Result;

public class MigrationManager {

	private static final int DB_SOURCE  = 0;
	private static final int DB_TARGET  = 1;
	
	private static final int MAX_CONNECTIONS  = 50;
	private static final int CONNECTION_TIMEOUT = 1200;
	
	@SuppressWarnings("unused")
	private static final String SELECT_METHOD = "Cursor";
	
	private static boolean AUTO_COMMIT = false;
	private static int COMMIT_COUNT = 1000;
	private static int BATCH_COUNT = 4000;
	private static int FAST_COMMIT_COUNT = 1;
	private static int FAST_BATCH_COUNT = 2;
	private static ArrayList<String> FAST_COMMIT_TABLES = new ArrayList<String>();
	private static ArrayList<String> IGNORE_TABLES = new ArrayList<String>();
	
	/**
	 * SOURCE PROPERTIES
	 */
	private static ArrayList<Connection> sourcePool = new ArrayList<Connection>();
	private static String SOURCE_DRIVER;
	private static String SOURCE_URL;
	private static String SOURCE_LOGIN;
	private static String SOURCE_PASSWORD;
	
	/**
	 * TARGET PROPERTIES
	 */
	private static ArrayList<Connection> targetPool = new ArrayList<Connection>();
	private static String TARGET_DRIVER;
	private static String TARGET_URL;
	private static String TARGET_LOGIN;
	private static String TARGET_PASSWORD;
	
	/**
	 * Inicializacao do gerenciador de migracao
	 * @param sourceDriver
	 * @param sourceUrl
	 * @param sourceLogin
	 * @param sourcePassword
	 * @param targetDriver
	 * @param targetUrl
	 * @param targetLogin
	 * @param targetPassword
	 */
	public static void init(String sourceDriver, String sourceUrl, String sourceLogin, String sourcePassword, 
							String targetDriver, String targetUrl, String targetLogin, String targetPassword,
							boolean autoCommit, int commitCount, int batchCount, 
							int fastCommitCount, int fastBatchCount, ArrayList<String> fastCommitTables) {
		init(sourceDriver, sourceUrl, sourceLogin, sourcePassword, 
				targetDriver, targetUrl, targetLogin, targetPassword,
				autoCommit, commitCount, batchCount, 
				fastCommitCount, fastBatchCount, fastCommitTables, 
				null);
	}
	
	/**
	 * Inicializacao do gerenciador de migracao
	 * @param sourceDriver
	 * @param sourceUrl
	 * @param sourceLogin
	 * @param sourcePassword
	 * @param targetDriver
	 * @param targetUrl
	 * @param targetLogin
	 * @param targetPassword
	 */
	public static void init(String sourceDriver, String sourceUrl, String sourceLogin, String sourcePassword, 
							String targetDriver, String targetUrl, String targetLogin, String targetPassword,
							boolean autoCommit, int commitCount, int batchCount, 
							int fastCommitCount, int fastBatchCount, ArrayList<String> fastCommitTables, 
							ArrayList<String> ignoreTables) {
		
		SOURCE_DRIVER = sourceDriver;
		SOURCE_URL = sourceUrl;
		SOURCE_LOGIN = sourceLogin;
		SOURCE_PASSWORD = sourcePassword;
		
		TARGET_DRIVER = targetDriver;
		TARGET_URL = targetUrl;
		TARGET_LOGIN = targetLogin;
		TARGET_PASSWORD = targetPassword;
		
		AUTO_COMMIT = autoCommit;
		COMMIT_COUNT = commitCount;
		BATCH_COUNT = batchCount;
		FAST_COMMIT_COUNT = fastCommitCount;
		FAST_BATCH_COUNT = fastBatchCount;
		FAST_COMMIT_TABLES = fastCommitTables;
		
		IGNORE_TABLES = ignoreTables;
	}
	
	/**
	 * Conecta no banco de destino
	 * @return
	 */
	synchronized public static Connection connectTarget() {
		return connect(DB_TARGET);
	}
	
	/**
	 * Desconecta no banco de destino
	 * @param connect
	 */
	synchronized public static void disconnectTarget(Connection connect){
		disconnect(connect, DB_TARGET);
	}
	
	/**
	 * Conecta no banco de origem
	 * @return
	 */
	synchronized public static Connection connectSource() {
		return connect(DB_SOURCE);
	}
	
	/**
	 * Desconecta no banco de origem
	 * @param connect
	 */
	synchronized public static void disconnectSource(Connection connect){
		disconnect(connect, DB_SOURCE);
	}
	
	/**
	 * Conecta em um banco de dados (origem ou destino) gerenciando um pool de conexoes 
	 * @param dbType
	 * @return
	 */
	synchronized public static Connection connect(int dbType) {
		try	{
			
			ArrayList<Connection> pool = dbType == DB_TARGET ? targetPool : sourcePool;
			
			String DRIVER = dbType == DB_TARGET ? TARGET_DRIVER : SOURCE_DRIVER;
			String URL = dbType == DB_TARGET ? TARGET_URL : SOURCE_URL;
			String LOGIN = dbType == DB_TARGET ? TARGET_LOGIN : SOURCE_LOGIN;
			String PASSWORD = dbType == DB_TARGET ? TARGET_PASSWORD : SOURCE_PASSWORD;
									
			if(pool.size() > 0)	{
				Connection connect = pool.get(0);
				pool.remove(0);
				if(connect.isClosed())
					return connect(dbType);
				
				return connect;
			}
			
			Class.forName(DRIVER).newInstance();
	  		DriverManager.setLoginTimeout(CONNECTION_TIMEOUT);
			return DriverManager.getConnection(URL, LOGIN, PASSWORD);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
  	}
	
	/**
	 * Desconecta em um banco de dados (origem ou destino) gerenciando um pool de conexoes
	 * @param connect
	 * @param dbType
	 */
	synchronized public static void disconnect(Connection connect, int dbType){
		try	{
			ArrayList<Connection> pool = dbType == DB_TARGET ? targetPool : sourcePool;
			
			if(connect!=null && (pool.size() < MAX_CONNECTIONS) && !connect.isClosed())	{
				
				if(!connect.getAutoCommit())
					connect.rollback();
				connect.setAutoCommit(true);
				
				boolean found = false;
				for (Connection c : pool) {
					if(c == connect){
						found = true;
					}	
				}
				
				if(!found)
					pool.add(connect);
			}
			else if(connect!=null)
				connect.close();
		}
		catch(Exception e)	{
			e.printStackTrace();
		}
	}
	
	/**
	 * Verifica as propriedades de conexão com o banco de origem
	 * @return
	 */
	private static boolean verifySource() {
		return (SOURCE_DRIVER!=null && !SOURCE_DRIVER.equals("") &&
				SOURCE_URL!=null && !SOURCE_URL.equals("") &&
				SOURCE_LOGIN!=null && !SOURCE_LOGIN.equals("") &&
				SOURCE_PASSWORD!=null && !SOURCE_PASSWORD.equals(""));
	}
	
	/**
	 * Verifica as propriedades de conexão com o banco de destino
	 * @return
	 */
	private static boolean verifyTarget() {
		return (TARGET_DRIVER!=null && !TARGET_DRIVER.equals("") &&
				TARGET_URL!=null && !TARGET_URL.equals("") &&
				TARGET_LOGIN!=null && !TARGET_LOGIN.equals("") &&
				TARGET_PASSWORD!=null && !TARGET_PASSWORD.equals(""));
	}
	
	/**
	 * Realiza a migração dos dados entre os bancos de origem e destino
	 * @return
	 */
	public static Result migrate()	{
		
		/**
		 * 1. Listar tabelas a serem importadas no banco de origem;
		 * 2. Desligar integridade referencial por tabela;
		 * 3. Calcular registros por tabela na origem e no destino e determinar se ainda possui algum registro a ser importado;
		 * 4. Migrar a cada tabela os registros que faltam ser importados;
		 * 5. Validar a quantidade de registros importados por tabela;
		 * 6. Religar a integridade referencial por tabela;
		 */
		
		if(!verifySource()) {
			LogUtils.info("Propriedades do banco de dados de origem nao foram inicializadas!");
			return new Result(-1, "Propriedades do banco de dados de origem nao foram inicializadas!");
		}
		
		if(!verifyTarget()) {
			LogUtils.info("Propriedades do banco de dados de destino nao foram inicializadas!");
			return new Result(-1, "Propriedades do banco de dados de destino nao foram inicializadas!");
		}
		
		LogUtils.createTimer("MIGRATION");
		
		LogUtils.info("FASE 1: Iniciando migração...");
		LogUtils.info("BANCO DE DADOS ORIGEM: "+SOURCE_URL);
		LogUtils.info("BANCO DE DADOS DESTINO: "+TARGET_URL);
		
		Connection connectTarget = connectTarget();
		Connection connectSource = connectSource();
		
		try {
			
			LogUtils.createTimer("MIGRATION_FASE1");
			
			/*
			 * 1. Listar tabelas a serem importadas no banco de origem;
			 */
			LogUtils.info("Carregando Tabelas...");
			ResultSetMap rsmTabelas = DeveloperServices.getTables(connectSource);
			
			int insertCount = 0;
			int commitCount = 1000;
			int batchCount = 4000;
			
			LogUtils.info(rsmTabelas.size() + " tabelas encontradas.");
			

			System.out.println("Tabelas a serem ignoradas:" + IGNORE_TABLES);
			
			while(rsmTabelas.next())	{
				String tableName = rsmTabelas.getString("TABLE_NAME");
				
				//nao processar as views
				if(tableName.toUpperCase().indexOf("VW_")>=0)
					continue;
				
				//Ignorar tabelas listadas na variavel IGNORE_TABLES
				if(IGNORE_TABLES!=null && IGNORE_TABLES.size()>0 && Util.inArrayString(tableName.trim().toLowerCase(), IGNORE_TABLES)) {
					System.out.println("Ignorando a tabela: "+tableName.trim().toLowerCase());
					continue;
				}

				boolean fastCommit = Util.inArrayString(tableName.trim().toLowerCase(), FAST_COMMIT_TABLES);
				
				commitCount = fastCommit ? FAST_COMMIT_COUNT : COMMIT_COUNT;
				batchCount = fastCommit ? FAST_BATCH_COUNT : BATCH_COUNT;
				
				LogUtils.info("\tTabela " + rsmTabelas.getPosition() + ": " + tableName.trim() + " [fastCommit:"+fastCommit+"]");
				
				if(fastCommit)
					LogUtils.info("\t\tFAST COMMIT [commitCount:"+commitCount+", batchCount:"+batchCount+"]");
				
				/* 
				 * 2. Desligar integridade referencial por tabela;
				 */
				connectTarget.prepareCall("ALTER TABLE "+tableName+" DISABLE TRIGGER ALL").executeUpdate();
				
				
				/*
				 * 3. Calcular registros por tabela na origem e no destino e determinar se ainda possui algum registro a ser importado;
				 */
				Statement stmt = connectTarget.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
				int row   = 0;
				int skip  = 0;
				int total = 0;
				
				int processedRows = 0;
				
				// Calculando registros de origem
				ResultSet rs = connectSource.prepareStatement("SELECT COUNT(*) FROM "+tableName).executeQuery();
				if(rs.next())
					total = rs.getInt(1);
				
				// Calculando registros de destino
				rs = connectTarget.prepareStatement("SELECT COUNT(*) FROM "+tableName).executeQuery();
				
				if(rs.next())
					skip = rs.getInt(1);
				row   = skip;
				
				LogUtils.info("\t\tTOTAL: " + total + ", IMPORTADOS: " + skip);
				
				if(total==skip)	{
					LogUtils.info("\t\t" + tableName.trim() + " não possui mais nenhum registro a importar!");
					continue;
				}
				
				/*
				 * 4. Migrar a cada tabela os registros que faltam ser importados;
				 */
				
				boolean hasRecords = false;
				boolean cleanUp = false;
				
				connectTarget.setAutoCommit(AUTO_COMMIT);
				
				do {
					
					//limpeza
					if(cleanUp) {
						LogUtils.info("\t\t- - - REALIZANDO CLEAN-UP! - - -");
						//LogUtils.info("\t\tinsertCount: "+insertCount+", batchCount: "+batchCount+", cleanUp: "+cleanUp);
						
						stmt.close();
						
						disconnectSource(connectSource);
						disconnectTarget(connectTarget);
						
						stmt = null;
						
						System.gc();
						
						connectTarget = connectTarget();
						connectTarget.setAutoCommit(AUTO_COMMIT);
						connectSource  = connectSource();
						
						stmt = connectTarget.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
						
						cleanUp = false;
					}
					
					ResultSet rsOrigem     = connectSource.prepareStatement("SELECT FIRST "+batchCount+" SKIP "+skip+" * FROM "+tableName).executeQuery();
					ResultSetMetaData rsmd = rsOrigem.getMetaData();
					hasRecords = false;
					ResultSet rsDestino = stmt.executeQuery("SELECT * FROM "+tableName+" WHERE 1=3");
					
					while(rsOrigem.next())	{
						
						row++;
						insertCount++;
						processedRows++;
						
						hasRecords = true;
						rsDestino.moveToInsertRow();
						
						for(int i=1; i<=rsmd.getColumnCount(); i++) {
							
							int tp = rsmd.getColumnType(i);
							String colName = rsmd.getColumnLabel(i).trim();
							String colTypeName = rsmd.getColumnTypeName(i);
							
							boolean processText = ((colName.toLowerCase().indexOf("txt_")!=-1) ||
												   (colName.toLowerCase().indexOf("ds_")!=-1)  ||
												   (tp==-16 || 
												    tp==-15 || 
												    tp==-9 || 
												    tp==-1 || //BLOB SUB_TYPE 1 
												    tp==1 ||  //CHAR
												    tp==12));
								
							if(processText) {
								
								String value = null;
								
								if(colTypeName.equals("BLOB SUB_TYPE 0")) {
									byte[] blbValue = rsOrigem.getBytes(colName);
									
									if(blbValue!=null)
										value = new String(blbValue, StandardCharsets.ISO_8859_1);
								}
								else
									value = rsOrigem.getString(colName);
								
								if(value!=null) {
									value = value.replace((char)0x00, ' ');
									value = value.replace((char)0xc2, ' ');
									value = value.replace((char)0x89, ' ');
									//value = value.replace((char)0x00, ' ');
									//value = value.replace((char)0x00, ' ');
									
									//LogUtils.info("\t\t\trow:"+row+"\t" + colName + "\t["+tp+"]\t" + colTypeName + ", value:"+value);
									
									rsDestino.updateObject(colName, value);
								}
							}
							else
								rsDestino.updateObject(colName, rsOrigem.getObject(colName));
						}
						
						// Gravando o registro
						try	{
							rsDestino.insertRow();
						}
						catch(Exception e) { 
							try {
								rsDestino.close();
								rsDestino = stmt.executeQuery("SELECT * FROM "+tableName+" WHERE 1=3");
								
								LogUtils.info("\t\tErro ao gravar: " + tableName.trim() + ": Registro " + row);
								
								e.printStackTrace(); 
							}
							catch(Exception e1) {
								e1.printStackTrace();
							}
						}
						
						// Dando o commit
						try	{
							if((row % commitCount) == 0) {
								if(!AUTO_COMMIT)
									connectTarget.commit();
								
								LogUtils.info("\t\t"+row+"/"+total+" registro(s) inseridos! [insertCount: "+insertCount+"] - " + Util.formatNumber((row*100)/total, 2) + "%" );
							}
						}
						catch(Exception e) { 
							e.printStackTrace(System.out); 
						}
						
						//calculando cleanUp
						cleanUp = ((processedRows % batchCount) == 0 && insertCount>1);
						//LogUtils.info("\t\tinsertCount: "+insertCount+", batchCount: "+batchCount+", cleanUp: "+cleanUp);
					}
					
					try {
						if(!AUTO_COMMIT)
							connectTarget.commit();
					}
					catch(Exception e) { 
						e.printStackTrace(System.out); 
					}
					
					skip += batchCount;
				} while(hasRecords);
				
				// tabelasImportadas.put(tableName, new Integer(1));
				
				LogUtils.info("\t\t"+tableName.trim()+" concluída!");
				LogUtils.info("\t\t"+row+" registro(s) inseridos!");
			}
			
			LogUtils.info("Fase 1 finalizada!");
			LogUtils.logTimer("MIGRATION_FASE1", LogUtils.TIMER_MINUTE, LogUtils.VERBOSE_LEVEL_INFO);
			
			/*
			 * 5. Validar a quantidade de registros importados por tabela;
			 */
			LogUtils.info("FASE 2: Validando registros importados e religando as integridades referenciais!");
			LogUtils.createTimer("MIGRATION_FASE2");
			
			
			rsmTabelas.beforeFirst();
			while(rsmTabelas.next()) {
				String tableName = rsmTabelas.getString("TABLE_NAME");
				
				if(tableName.toUpperCase().indexOf("VW_")>=0)
					continue;
				
				/*
				 * 6. Religar a integridade referencial por tabela;
				 */
				connectTarget.prepareStatement("ALTER TABLE "+tableName+" ENABLE TRIGGER ALL").executeUpdate();
				
				int qtOrigem = 0;
				ResultSet rs = connectSource.prepareStatement("SELECT COUNT(*) FROM "+tableName).executeQuery();
				if(rs.next())
					qtOrigem = rs.getInt(1);
				int qtDestino = 0;
				rs = connectTarget.prepareStatement("SELECT COUNT(*) FROM "+tableName).executeQuery();
				if(rs.next())
					qtDestino = rs.getInt(1);
				
				LogUtils.info(tableName+(qtOrigem != qtDestino?" X ":" OK ") + qtOrigem+"/"+qtDestino+" registro(s)");
			}
			
			LogUtils.info("Fase 2 finalizada!");
			LogUtils.logTimer("MIGRATION_FASE2", LogUtils.TIMER_MINUTE, LogUtils.VERBOSE_LEVEL_INFO);
			
			LogUtils.info("Migracao concluida com sucesso!");
			LogUtils.logTimer("MIGRATION", LogUtils.TIMER_MINUTE, LogUtils.VERBOSE_LEVEL_INFO);
			
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace();
			
			LogUtils.info("Erro no processo de migracao");
			LogUtils.logTimer("MIGRATION", LogUtils.TIMER_MINUTE, LogUtils.VERBOSE_LEVEL_INFO);
			
			
			return new sol.util.Result(-1, "Erro no processo de migracao!", e);
		}
		finally{
			disconnectSource(connectSource);
			disconnectTarget(connectTarget);
			
			LogUtils.destroyTimer("MIGRATION");
			LogUtils.destroyTimer("MIGRATION_FASE1");
			LogUtils.destroyTimer("MIGRATION_FASE2");
		}
	}
	
	/**
	 * Imprime os dados do banco de origem 
	 */
	public static void printSourceMetaData()	{
		try {
			
			Connection connect = connectSource();
			
			ResultSetMap rsmTabelas = DeveloperServices.getTables(connect);
			
			while(rsmTabelas.next())	{
				String tableName = rsmTabelas.getString("TABLE_NAME");
				
				if(tableName.toUpperCase().indexOf("VW_")>=0)
					continue;
				
				ResultSet rs =  connect.prepareStatement("SELECT * FROM "+tableName+" WHERE 0<>0").executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();

				LogUtils.info("[" + tableName.trim() + "]");
				
				for(int i=1; i<=rsmd.getColumnCount(); i++) {
					
					int tp = rsmd.getColumnType(i);
					
					String colName = rsmd.getColumnLabel(i).trim();
					String colTypeName = rsmd.getColumnTypeName(i);
				
					LogUtils.info("\t" + colName + "\t["+tp+"]\t" + colTypeName);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
