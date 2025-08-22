package com.tivic.manager.importacao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Giss {

	public static Connection conectarMySQL()	{
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
	  		DriverManager.setLoginTimeout(80);
			return DriverManager.getConnection("jdbc:mysql://127.0.0.1/giss", "root","t1v1k!");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static Connection conectarOracle()	{
		try{
			Class.forName("oracle.jdbc.OracleDriver").newInstance();
	  		DriverManager.setLoginTimeout(80);
			return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:giss", "system","Adelio14");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static void main() {
		try {
			System.out.println("**********************************************");
			System.out.println("Preparando importação do GISS-ONLINE \n");
			Connection connOra   = conectarOracle();
			Connection connMySql = conectarMySQL();
			boolean isTeste = false;
			java.sql.ResultSet rs = null;
			PreparedStatement pstmt = null;
			int total = 0;
			int count = 0;
			//
			/*
			 *  TB_CONTADOR
			 */
			if(false)	{
				rs = connOra.prepareStatement("SELECT COUNT(*) as TOTAL FROM vconquista.tb_contador").executeQuery();
				total = rs.next() ? rs.getInt(1) : 0;
				count = 0;
				System.out.println("Efetuando carga dos contadores...");
				rs = connOra.prepareStatement("SELECT * FROM vconquista.tb_contador").executeQuery();
				System.out.println("Selecionadas "+total+" contador");
				//
				pstmt = connMySql.prepareStatement("INSERT INTO tb_contador (CLIE_COD_CLIENTE,CONT_NUM_CONTADOR,PESS_NOM_PESSOA,PESS_CPF_CGC,CONT_NUM_CRC,CONT_LOGIN_ACESSO,CONT_SENHA_ACESSO,CONT_UTL_ACESSO,CONT_QTD_ACESSO,CONT_END_TIPO,CONT_END_TITULO,CONT_END_LOGRADOURO,CONT_END_NUMERO,CONT_END_COMPLEMENTO,CONT_END_BAIRRO,CONT_END_CEP,CONT_END_CIDADE,CONT_END_ESTADO,CONT_END_EMAIL,CONT_STA_ACCESSO_LIBERADO,CONT_DDD,CONT_TELEFONE,CONT_RAMAL,CONT_DTA_ALTERA,CONT_NUM_CCM,CONT_DT_ENVIO,CONTROLE,CONT_NUM_USU_CONTADOR) " +
												   "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); 
				while(rs.next() && !isTeste) {
					count++;
					if(count%100==0)
						System.out.println("Incluido: "+count+"/"+total);
					
					pstmt.setObject(1, rs.getObject("CLIE_COD_CLIENTE"));
					pstmt.setObject(2, rs.getObject("CONT_NUM_CONTADOR"));
					pstmt.setObject(3, rs.getObject("PESS_NOM_PESSOA"));
					pstmt.setObject(4, rs.getObject("PESS_CPF_CGC"));
					pstmt.setObject(5, rs.getObject("CONT_NUM_CRC"));
					pstmt.setObject(6, rs.getObject("CONT_LOGIN_ACESSO"));
					pstmt.setObject(7, rs.getObject("CONT_SENHA_ACESSO"));
					pstmt.setObject(8, rs.getObject("CONT_UTL_ACESSO"));
					pstmt.setObject(9, rs.getObject("CONT_QTD_ACESSO"));
					pstmt.setObject(10, rs.getObject("CONT_END_TIPO"));
					pstmt.setObject(11, rs.getObject("CONT_END_TITULO"));
					pstmt.setObject(12, rs.getObject("CONT_END_LOGRADOURO"));
					pstmt.setObject(13, rs.getObject("CONT_END_NUMERO"));
					pstmt.setObject(14, rs.getObject("CONT_END_COMPLEMENTO"));
					pstmt.setObject(15, rs.getObject("CONT_END_BAIRRO"));
					pstmt.setObject(16, rs.getObject("CONT_END_CEP"));
					pstmt.setObject(17, rs.getObject("CONT_END_CIDADE"));
					pstmt.setObject(18, rs.getObject("CONT_END_ESTADO"));
					pstmt.setObject(19, rs.getObject("CONT_END_EMAIL"));
					pstmt.setObject(20, rs.getObject("CONT_STA_ACCESSO_LIBERADO"));
					pstmt.setObject(21, rs.getObject("CONT_DDD"));
					pstmt.setObject(22, rs.getObject("CONT_TELEFONE"));
					pstmt.setObject(23, rs.getObject("CONT_RAMAL"));
					pstmt.setObject(24, rs.getObject("CONT_DTA_ALTERA"));
					pstmt.setObject(25, rs.getObject("CONT_NUM_CCM"));
					pstmt.setObject(26, rs.getObject("CONT_DT_ENVIO"));
					pstmt.setObject(27, rs.getObject("CONTROLE"));
					pstmt.setObject(28, rs.getObject("CONT_NUM_USU_CONTADOR"));
					try {
						pstmt.execute();
					}
					catch(Exception ins) {
						System.out.println("Erro no contador: "+rs.getObject("PESS_NOM_PESSOA"));
						ins.printStackTrace(System.out);	
					}
				}
				System.out.println("Contadores importados com sucesso!");
			}
			/*
			 *  TB_USUARIOS_GESTAO
			 */
			if(false)	{
				rs = connOra.prepareStatement("SELECT COUNT(*) as TOTAL FROM vconquista.tb_usuarios_gestao").executeQuery();
				total = rs.next() ? rs.getInt(1) : 0;
				count = 0;
				System.out.println("Efetuando carga de usuarios da gestão...");
				rs = connOra.prepareStatement("SELECT * FROM vconquista.tb_usuarios_gestao").executeQuery();
				System.out.println("Selecionados "+total+" usuarios");
				//
				pstmt = connMySql.prepareStatement("INSERT INTO tb_usuarios_gestao (USGE_NUM_USUARIO,USGE_NOM_USUARIO,USGE_LOGON_USUARIO,CLIE_COD_CLIENTE,USGE_EMAIL_USUARIO,FISC_NUM_MATRICULA,FISC_DSC_CARGO) " +
												   "VALUES (?,?,?,?,?,?,?)"); 
				while(rs.next() && !isTeste) {
					count++;
					if(count%100==0)
						System.out.println("Incluido: "+count+"/"+total);
					
					pstmt.setObject(1, rs.getObject("USGE_NUM_USUARIO"));
					pstmt.setObject(2, rs.getObject("USGE_NOM_USUARIO"));
					pstmt.setObject(3, rs.getObject("USGE_LOGON_USUARIO"));
					pstmt.setObject(4, rs.getObject("CLIE_COD_CLIENTE"));
					pstmt.setObject(5, rs.getObject("USGE_EMAIL_USUARIO"));
					pstmt.setObject(6, rs.getObject("FISC_NUM_MATRICULA"));
					pstmt.setObject(7, rs.getObject("FISC_DSC_CARGO"));
	
					try {
						pstmt.execute();
					}
					catch(Exception ins) {
						System.out.println("Erro na usuário: "+rs.getObject("USGE_NOM_USUARIO"));
						ins.printStackTrace(System.out);	
					}
				}
				System.out.println("Importação de usuários da gestão concluida com sucesso!");
			}
			/*
			 *  TB_CONTAS_BANCARIAS
			 */
			if(false)	{ 
				rs = connOra.prepareStatement("SELECT COUNT(*) as TOTAL FROM vconquista.TB_CONTAS_BANCARIAS").executeQuery();
				total = rs.next() ? rs.getInt(1) : 0;
				count = 0;
				System.out.println("Efetuando carga de Contas Bancárias...");
				rs = connOra.prepareStatement("SELECT * FROM vconquista.TB_CONTAS_BANCARIAS").executeQuery();
				System.out.println("Selecionados "+total+" contas bancárias");
				//
				pstmt = connMySql.prepareStatement("INSERT INTO TB_CONTAS_BANCARIAS (BANC_NUM_BANCO,BANC_NUM_CONTA,BANC_DES_DESCRICAO,SVTB_COD_SERVICO,CLIE_COD_CLIENTE,BANC_NUM_CONTA_FK,BANC_NUM_BANCO_FK,EXER_ANO_BASE,BANC_TIPO_CONTA) " +
												   "VALUES (?,?,?,?,?,?,?,?,?)"); 
				while(rs.next() && !isTeste) {
					count++;
					if(count%100==0)
						System.out.println("Incluido: "+count+"/"+total);
					
					pstmt.setObject(1, rs.getObject("BANC_NUM_BANCO"));
					pstmt.setObject(2, rs.getObject("BANC_NUM_CONTA"));
					pstmt.setObject(3, rs.getObject("BANC_DES_DESCRICAO"));
					pstmt.setObject(4, rs.getObject("SVTB_COD_SERVICO"));
					pstmt.setObject(5, rs.getObject("CLIE_COD_CLIENTE"));
					pstmt.setObject(6, rs.getObject("BANC_NUM_CONTA_FK"));
					pstmt.setObject(7, rs.getObject("BANC_NUM_BANCO_FK"));
					pstmt.setObject(8, rs.getObject("EXER_ANO_BASE"));
					pstmt.setObject(9, rs.getObject("BANC_TIPO_CONTA"));
	
					try {
						pstmt.execute();
					}
					catch(Exception ins) {
						System.out.println("Erro na conta: "+rs.getObject("BANC_NUM_CONTA"));
						ins.printStackTrace(System.out);	
					}
				}
				System.out.println("Importação de Contas Bancárias com sucesso!");
			}
			/*
			 * BAIXA USUÁRIO  
			 */
			if(false)	{
				rs = connOra.prepareStatement("SELECT COUNT(*) as TOTAL FROM vconquista.TB_BAIXA_USUARIO").executeQuery();
				total = rs.next() ? rs.getInt(1) : 0;
				count = 0;
				System.out.println("Efetuando carga de baixa de usuário...");
				rs = connOra.prepareStatement("SELECT * FROM vconquista.TB_BAIXA_USUARIO").executeQuery();
				System.out.println("Selecionados "+total+" baixa de usuário");
				//
				pstmt = connMySql.prepareStatement("INSERT INTO TB_BAIXA_USUARIO (CLIE_COD_CLIENTE,MOBI_NUM_CADASTRO,BOLE_ID,BOLE_MES_COMPETENCIA,BOLE_ANO_COMPETENCIA,BOLE_VALOR_BAIXA,USGE_NUM_USUARIO,BOLE_IP_USUARIO,BOLE_DT_VENC_MANUAL) " +
												   "VALUES (?,?,?,?,?,?,?,?,?)"); 
				while(rs.next() && !isTeste) {
					count++;
					if(count%100==0)
						System.out.println("Incluido: "+count+"/"+total);
					
					pstmt.setObject(1, rs.getObject("CLIE_COD_CLIENTE"));
					pstmt.setObject(2, rs.getObject("MOBI_NUM_CADASTRO"));
					pstmt.setObject(3, rs.getObject("BOLE_ID"));
					pstmt.setObject(4, rs.getObject("BOLE_MES_COMPETENCIA"));
					pstmt.setObject(5, rs.getObject("BOLE_ANO_COMPETENCIA"));
					pstmt.setObject(6, rs.getObject("BOLE_VALOR_BAIXA"));
					pstmt.setObject(7, rs.getObject("USGE_NUM_USUARIO"));
					pstmt.setObject(8, rs.getObject("BOLE_IP_USUARIO"));
					pstmt.setObject(9, rs.getObject("BOLE_DT_VENC_MANUAL"));
	
					try {
						pstmt.execute();
					}
					catch(Exception ins) {
						System.out.println("Erro na baixa do boleto: "+rs.getObject("MOBI_NUM_CADASTRO"));
						ins.printStackTrace(System.out);	
					}
				}
				System.out.println("Importação de baixa de usuário concluida com sucesso!");
			}
			/*
			 *   TB_BOLETOS
			 */
			String nmTabela = "TB_BOLETOS";
			if(false) {
				rs = connOra.prepareStatement("SELECT COUNT(*) as TOTAL FROM vconquista."+nmTabela).executeQuery();
				total = rs.next() ? rs.getInt(1) : 0;
				count = 0;
				System.out.println("Efetuando carga de "+nmTabela+"...");
				rs = connOra.prepareStatement("SELECT * FROM vconquista."+nmTabela).executeQuery();
				System.out.println("Selecionados "+total+" registros em "+nmTabela);
				//
				pstmt = connMySql.prepareStatement("INSERT INTO "+nmTabela+" (CLIE_COD_CLIENTE,MOBI_NUM_CADASTRO,BOLE_DAT_EMISSAO,BOLE_COD_BARRAS,BOLE_VALOR_IMPOSTO,BOLE_MES_COMPETENCIA,BOLE_ANO_COMPETENCIA,BOLE_ID,BOLE_VL_FATURADO,BOLE_ATIT_COD_ATIVIDADE,BOLE_ORIGEM,BOLE_DT_BAIXA,BOLE_VL_RECEBIDO,BOLE_BCO_BAIXA,BOLE_LTE_BAIXA,BOLE_TIPOBOLETO,BOLE_DESCRICAO,BOLE_SUB_ID,BOLE_ENC_ID,BOLE_OBRA_ID,BOLE_STA_SITUACAO,BOLE_NFS_ID,BOLE_USR_GESTOR,BOLE_OBS_CANCEL,OBRA_MOBI_TOMA,BOLE_AUDIT,BOLE_STA_AUDIT,BOLE_LOCAL_OBRA,BOLE_DT_VENC,BOLE_VLR_ACRESCIMO,BOLE_DESC_MANUAL,BOLE_DT_VENC_MANUAL,BOLE_TAXA_EXP,DATA_TIMESTAMP) " +
												   "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); 
				while(rs.next()) {
					count++;
					if(count%100==0) {
						System.out.println("Incluido: "+count+"/"+total);
						if(isTeste && count > 100)
							break;
					}
					
					pstmt.setObject(1, rs.getObject("CLIE_COD_CLIENTE"));
					pstmt.setObject(2, rs.getObject("MOBI_NUM_CADASTRO"));
					pstmt.setObject(3, rs.getObject("BOLE_DAT_EMISSAO"));
					pstmt.setObject(4, rs.getObject("BOLE_COD_BARRAS"));
					pstmt.setObject(5, rs.getObject("BOLE_VALOR_IMPOSTO"));
					pstmt.setObject(6, rs.getObject("BOLE_MES_COMPETENCIA"));
					pstmt.setObject(7, rs.getObject("BOLE_ANO_COMPETENCIA"));
					pstmt.setObject(8, rs.getObject("BOLE_ID"));
					pstmt.setObject(9, rs.getObject("BOLE_VL_FATURADO"));
					pstmt.setObject(10, rs.getObject("BOLE_ATIT_COD_ATIVIDADE"));
					pstmt.setObject(11, rs.getObject("BOLE_ORIGEM"));
					pstmt.setObject(12, rs.getObject("BOLE_DT_BAIXA"));
					pstmt.setObject(13, rs.getObject("BOLE_VL_RECEBIDO"));
					pstmt.setObject(14, rs.getObject("BOLE_BCO_BAIXA"));
					pstmt.setObject(15, rs.getObject("BOLE_LTE_BAIXA"));
					pstmt.setObject(16, rs.getObject("BOLE_TIPOBOLETO"));
					pstmt.setObject(17, rs.getObject("BOLE_DESCRICAO"));
					pstmt.setObject(18, rs.getObject("BOLE_SUB_ID"));
					pstmt.setObject(19, rs.getObject("BOLE_ENC_ID"));
					pstmt.setObject(20, rs.getObject("BOLE_OBRA_ID"));
					pstmt.setObject(21, rs.getObject("BOLE_STA_SITUACAO"));
					pstmt.setObject(22, rs.getObject("BOLE_NFS_ID"));
					pstmt.setObject(23, rs.getObject("BOLE_USR_GESTOR"));
					pstmt.setObject(24, rs.getObject("BOLE_OBS_CANCEL"));
					pstmt.setObject(25, rs.getObject("OBRA_MOBI_TOMA"));
					pstmt.setObject(26, rs.getObject("BOLE_AUDIT"));
					pstmt.setObject(27, rs.getObject("BOLE_STA_AUDIT"));
					pstmt.setObject(28, rs.getObject("BOLE_LOCAL_OBRA"));
					pstmt.setObject(29, rs.getObject("BOLE_DT_VENC"));
					pstmt.setObject(30, rs.getObject("BOLE_VLR_ACRESCIMO"));
					pstmt.setObject(31, rs.getObject("BOLE_DESC_MANUAL"));
					pstmt.setObject(32, rs.getObject("BOLE_DT_VENC_MANUAL"));
					pstmt.setObject(33, rs.getObject("BOLE_TAXA_EXP"));
					pstmt.setObject(34, rs.getObject("DATA_TIMESTAMP"));
	
					try {
						pstmt.execute();
					}
					catch(Exception ins) {
						System.out.println("Erro na "+nmTabela+": "+rs.getObject("MOBI_NUM_CADASTRO"));
						ins.printStackTrace(System.out);	
					}
				}
				System.out.println("Importação de "+nmTabela+" concluida com sucesso!");
			}
			/*
			 *   TB_BOLETOS_PROPRIETARIOS
			 */
			if(false){
				nmTabela = "TB_BOLETOS_PROPRIETARIOS";
				rs = connOra.prepareStatement("SELECT COUNT(*) as TOTAL FROM vconquista."+nmTabela).executeQuery();
				total = rs.next() ? rs.getInt(1) : 0;
				count = 0;
				System.out.println("Efetuando carga de "+nmTabela+"...");
				rs = connOra.prepareStatement("SELECT * FROM vconquista."+nmTabela).executeQuery();
				System.out.println("Selecionados "+total+" registros em "+nmTabela);
				//
				pstmt = connMySql.prepareStatement("INSERT INTO "+nmTabela+" (CLIE_COD_CLIENTE,IMOV_ID,BOLE_DAT_EMISSAO,BOLE_COD_BARRAS,BOLE_VALOR_IMPOSTO,BOLE_MES_COMPETENCIA,BOLE_ANO_COMPETENCIA,BOLE_ID,BOLE_VL_FATURADO,BOLE_ORIGEM,BOLE_DT_BAIXA,BOLE_VL_RECEBIDO,BOLE_BCO_BAIXA,BOLE_LTE_BAIXA,BOLE_TIPOBOLETO,BOLE_DESCRICAO,BOLE_SUB_ID,BOLE_ENC_ID,BOLE_OBRA_ID,BOLE_VLR_ACUM,BOLE_STA_SITUACAO,BOLE_NFS_ID,BOLE_USR_GESTOR,BOLE_OBS_CANCEL,OBRA_MOBI_TOMA,BOLE_LOCAL_OBRA,BOLE_DT_VENC,BOLE_VLR_ACRESCIMO,BOLE_DESC_MANUAL,BOLE_DT_VENC_MANUAL) " +
												   "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); 
				while(rs.next()) {
					count++;
					if(count%100==0) {
						System.out.println("Incluido: "+count+"/"+total);
						if(isTeste && count > 100)
							break;
					}
					
					pstmt.setObject(1, rs.getObject("CLIE_COD_CLIENTE"));
					pstmt.setObject(2, rs.getObject("IMOV_ID"));
					pstmt.setObject(3, rs.getObject("BOLE_DAT_EMISSAO"));
					pstmt.setObject(4, rs.getObject("BOLE_COD_BARRAS"));
					pstmt.setObject(5, rs.getObject("BOLE_VALOR_IMPOSTO"));
					pstmt.setObject(6, rs.getObject("BOLE_MES_COMPETENCIA"));
					pstmt.setObject(7, rs.getObject("BOLE_ANO_COMPETENCIA"));
					pstmt.setObject(8, rs.getObject("BOLE_ID"));
					pstmt.setObject(9, rs.getObject("BOLE_VL_FATURADO"));
					pstmt.setObject(10, rs.getObject("BOLE_ORIGEM"));
					pstmt.setObject(11, rs.getObject("BOLE_DT_BAIXA"));
					pstmt.setObject(12, rs.getObject("BOLE_VL_RECEBIDO"));
					pstmt.setObject(13, rs.getObject("BOLE_BCO_BAIXA"));
					pstmt.setObject(14, rs.getObject("BOLE_LTE_BAIXA"));
					pstmt.setObject(15, rs.getObject("BOLE_TIPOBOLETO"));
					pstmt.setObject(16, rs.getObject("BOLE_DESCRICAO"));
					pstmt.setObject(17, rs.getObject("BOLE_SUB_ID"));
					pstmt.setObject(18, rs.getObject("BOLE_ENC_ID"));
					pstmt.setObject(19, rs.getObject("BOLE_OBRA_ID"));
					pstmt.setObject(20, rs.getObject("BOLE_VLR_ACUM"));
					pstmt.setObject(21, rs.getObject("BOLE_STA_SITUACAO"));
					pstmt.setObject(22, rs.getObject("BOLE_NFS_ID"));
					pstmt.setObject(23, rs.getObject("BOLE_USR_GESTOR"));
					pstmt.setObject(24, rs.getObject("BOLE_OBS_CANCEL"));
					pstmt.setObject(25, rs.getObject("OBRA_MOBI_TOMA"));
					pstmt.setObject(26, rs.getObject("BOLE_LOCAL_OBRA"));
					pstmt.setObject(27, rs.getObject("BOLE_DT_VENC"));
					pstmt.setObject(28, rs.getObject("BOLE_VLR_ACRESCIMO"));
					pstmt.setObject(29, rs.getObject("BOLE_DESC_MANUAL"));
					pstmt.setObject(30, rs.getObject("BOLE_DT_VENC_MANUAL"));
	
					try {
						pstmt.execute();
					}
					catch(Exception ins) {
						System.out.println("Erro na "+nmTabela+": "+rs.getObject("MOBI_NUM_CADASTRO"));
						ins.printStackTrace(System.out);	
					}
				}
				System.out.println("Importação de "+nmTabela+" concluida com sucesso!");
			}
			/*
			 * TB_BOLETOS_DESCARTADOS
			 */
			if(false)	{
				nmTabela = "TB_BOLETOS_DESCARTADOS";
				rs = connOra.prepareStatement("SELECT COUNT(*) as TOTAL FROM vconquista."+nmTabela).executeQuery();
				total = rs.next() ? rs.getInt(1) : 0;
				count = 0;
				System.out.println("Efetuando carga de "+nmTabela+"...");
				rs = connOra.prepareStatement("SELECT * FROM vconquista."+nmTabela).executeQuery();
				System.out.println("Selecionados "+total+" registros em "+nmTabela);
				//
				pstmt = connMySql.prepareStatement("INSERT INTO "+nmTabela+" (CLIE_COD_CLIENTE,MOBI_NUM_CADASTRO,BOLE_ANO_COMPETENCIA,BOLE_MES_COMPETENCIA,BOLE_ID,ID_USUARIO_GESTAO,BOLE_DATA_DESCARTE,IP_USUARIO_GESTAO,STATUS) " +
												   "VALUES (?,?,?,?,?,?,?,?,?)"); 
				while(rs.next()) {
					count++;
					if(count%100==0) {
						System.out.println("Incluido: "+count+"/"+total);
						if(isTeste && count > 100)
							break;
					}
					
					pstmt.setObject(1, rs.getObject("CLIE_COD_CLIENTE"));
					pstmt.setObject(2, rs.getObject("MOBI_NUM_CADASTRO"));
					pstmt.setObject(3, rs.getObject("BOLE_ANO_COMPETENCIA"));
					pstmt.setObject(4, rs.getObject("BOLE_MES_COMPETENCIA"));
					pstmt.setObject(5, rs.getObject("BOLE_ID"));
					pstmt.setObject(6, rs.getObject("ID_USUARIO_GESTAO"));
					pstmt.setObject(7, rs.getObject("BOLE_DATA_DESCARTE"));
					pstmt.setObject(8, rs.getObject("IP_USUARIO_GESTAO"));
					pstmt.setObject(9, rs.getObject("STATUS"));
	
					try {
						pstmt.execute();
					}
					catch(Exception ins) {
						System.out.println("Erro na "+nmTabela+": "+rs.getObject("MOBI_NUM_CADASTRO"));
						ins.printStackTrace(System.out);	
					}
				}
				System.out.println("Importação de "+nmTabela+" concluida com sucesso!");
			}
			/*
			 * TB_BOLETOS_ESTORNADOS
			 */
			if(false)	{
				nmTabela = "TB_BOLETOS_ESTORNADOS";
				rs = connOra.prepareStatement("SELECT COUNT(*) as TOTAL FROM vconquista."+nmTabela).executeQuery();
				total = rs.next() ? rs.getInt(1) : 0;
				count = 0;
				System.out.println("Efetuando carga de "+nmTabela+"...");
				rs = connOra.prepareStatement("SELECT * FROM vconquista."+nmTabela).executeQuery();
				System.out.println("Selecionados "+total+" registros em "+nmTabela);
				//
				pstmt = connMySql.prepareStatement("INSERT INTO "+nmTabela+" (CLIE_COD_CLIENTE,MOBI_NUM_CADASTRO,BOLE_ANO_COMPETENCIA,BOLE_MES_COMPETENCIA,BOLE_ID,ID_USUARIO_GESTAO,BOLE_DATA_ESTORNO,IP_USUARIO_GESTAO) " +
												   "VALUES (?,?,?,?,?,?,?,?)"); 
				while(rs.next()) {
					count++;
					if(count%100==0) {
						System.out.println("Incluido: "+count+"/"+total);
						if(isTeste && count > 100)
							break;
					}
					
					pstmt.setObject(1, rs.getObject("CLIE_COD_CLIENTE"));
					pstmt.setObject(2, rs.getObject("MOBI_NUM_CADASTRO"));
					pstmt.setObject(3, rs.getObject("BOLE_ANO_COMPETENCIA"));
					pstmt.setObject(4, rs.getObject("BOLE_MES_COMPETENCIA"));
					pstmt.setObject(5, rs.getObject("BOLE_ID"));
					pstmt.setObject(6, rs.getObject("ID_USUARIO_GESTAO"));
					pstmt.setObject(7, rs.getObject("BOLE_DATA_ESTORNO"));
					pstmt.setObject(8, rs.getObject("IP_USUARIO_GESTAO"));
	
					try {
						pstmt.execute();
					}
					catch(Exception ins) {
						System.out.println("Erro na "+nmTabela+": "+rs.getObject("MOBI_NUM_CADASTRO"));
						ins.printStackTrace(System.out);	
					}
				}
				System.out.println("Importação de "+nmTabela+" concluida com sucesso!");
			}
			/*
			 *  TB_EMPRESAS
			 */
			if(false) {
				rs    = connOra.prepareStatement("SELECT COUNT(*) as TOTAL FROM vconquista.tb_empresas").executeQuery();
				total = rs.next() ? rs.getInt(1) : 0;
				count = 0;
				System.out.println("Efetuando carga de todas as empresas...");
				rs = connOra.prepareStatement("SELECT * FROM vconquista.tb_empresas").executeQuery();
				System.out.println("Selecionadas "+total+" empresas");
				//
				pstmt = connMySql.prepareStatement("INSERT INTO tb_empresas (CLIE_COD_CLIENTE, MOBI_NUM_CADASTRO, CONT_NUM_CONTADOR, EMPR_NUM_INSCRICAO, EMPR_INS_ESTADUAL, "+
												   "EMPR_NOM_EMPRESA, EMPR_TIPO, EMPR_CPF_CGC, EMPR_STA_ESTABELECIDO, EMPR_DAT_ABERTURA, EMPR_DAT_ENCERRAMENTO,  "+
												   "EMPR_TIP_TRIBUTACAO, LOCA_ABR_TIP, LOCA_ABR_TIT, LOCA_LOGRADOURO, LOCA_NUM_IMOVEL,  "+
												   "LOCA_COMPLEMENTO,LOCA_NOM_BAIRRO, LOCA_CEP, LOCA_CIDADE, LOCA_ESTADO, LOCA_DDD, LOCA_TELEFONE,  "+
												   "LOCA_RAMAL, LOCA_FAX, LOCA_EMAIL, EMPR_LOGIN_ACESSO, EMPR_SENHA_ACESSO, EMPR_NUM_BANCO, EMPR_QTD_ACESSO,  "+
																	"EMPR_UTL_ACESSO, CLIE_DIA_VENCIMENTO, EMPR_STA_LOGON, EMPR_STA_REGIME, EMPR_DAT_INCLUSAO,  "+
																	"EMPR_STA_MICRO, EMPR_DAT_ALTERACAO, EMPR_STA_ESPECIAIS, EMPR_STA_DEDUZ_CONSTRUCAO, EMPR_STA_ORG,  "+
																	"EMPR_ORG_TIPO, EMPR_NUM_DG, EMPR_ID_COMDOMINIO, EMPR_ID_CONSTRUCAO, CONT_NUM_ADMINISTRADOR,  "+
																	"EMPR_STA_FISICA, EMPR_STA_ACCESSO_LIBERADO, EMPR_USU_GESTAO, EMPR_STA_RETENCAO, EMPR_ABAT_CONTRIBUINTE,  "+
																	"EMPR_LOGO_RECIBO, EMPR_VALIDA_MES, EMPR_DT_ATUALIZACAO, EMPR_TIPO_BANCO, EMPR_AUTO_CADASTRO, EMPR_AREA_TOTAL, "+ 
																	"EMPR_AREA_OCUPADA, EMPR_QTD_PROFISSIONAIS, EMPR_NIVEL_INSTRUCAO, EMPR_NOM_FANTASIA, EMPR_DAT_CONSTITUICAO,  "+
																	"EMCO_NUMERO_DECA, EMCO_DATA_ENTREGA_DECA, EMPR_COOPERATIVA, EMPR_GAVULSA_BLOQUEADA, EMPR_ORG_ESTABELECIDO) " +
																	"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); 
				while(rs.next()) {
					count++;
					if(count%100==0) {
						System.out.println("Incluido: "+count+"/"+total);
						if(isTeste)
							break;
					}
					
					pstmt.setObject(1, rs.getObject("CLIE_COD_CLIENTE"));
					pstmt.setObject(2, rs.getObject("MOBI_NUM_CADASTRO"));
					pstmt.setObject(3, rs.getObject("CONT_NUM_CONTADOR"));
					pstmt.setObject(4, rs.getObject("EMPR_NUM_INSCRICAO"));
					pstmt.setObject(5, rs.getObject("EMPR_INS_ESTADUAL"));
					pstmt.setObject(6, rs.getObject("EMPR_NOM_EMPRESA")); 
					pstmt.setObject(7, rs.getObject("EMPR_TIPO")); 
					pstmt.setObject(8, rs.getObject("EMPR_CPF_CGC")); 
					pstmt.setObject(9, rs.getObject("EMPR_STA_ESTABELECIDO"));
					pstmt.setObject(10, rs.getObject("EMPR_DAT_ABERTURA")); 
					pstmt.setObject(11, rs.getObject("EMPR_DAT_ENCERRAMENTO")); 
					pstmt.setObject(12, rs.getObject("EMPR_TIP_TRIBUTACAO")); 
					pstmt.setObject(13, rs.getObject("LOCA_ABR_TIP")); 
					pstmt.setObject(14, rs.getObject("LOCA_ABR_TIT")); 
					pstmt.setObject(15, rs.getObject("LOCA_LOGRADOURO")); 
					pstmt.setObject(16, rs.getObject("LOCA_NUM_IMOVEL")); 
					pstmt.setObject(17, rs.getObject("LOCA_COMPLEMENTO")); 
					pstmt.setObject(18, rs.getObject("LOCA_NOM_BAIRRO")); 
					pstmt.setObject(19, rs.getObject("LOCA_CEP")); 
					pstmt.setObject(20, rs.getObject("LOCA_CIDADE")); 
					pstmt.setObject(21, rs.getObject("LOCA_ESTADO")); 
					pstmt.setObject(22, rs.getObject("LOCA_DDD")); 
					pstmt.setObject(23, rs.getObject("LOCA_TELEFONE")); 
					pstmt.setObject(24, rs.getObject("LOCA_RAMAL")); 
					pstmt.setObject(25, rs.getObject("LOCA_FAX")); 
					pstmt.setObject(26, rs.getObject("LOCA_EMAIL")); 
					pstmt.setObject(27, rs.getObject("EMPR_LOGIN_ACESSO")); 
					pstmt.setObject(28, rs.getObject("EMPR_SENHA_ACESSO")); 
					pstmt.setObject(29, rs.getObject("EMPR_NUM_BANCO")); 
					pstmt.setObject(30, rs.getObject("EMPR_QTD_ACESSO")); 
					pstmt.setObject(31, rs.getObject("EMPR_UTL_ACESSO")); 
					pstmt.setObject(32, rs.getObject("CLIE_DIA_VENCIMENTO")); 
					pstmt.setObject(33, rs.getObject("EMPR_STA_LOGON")); 
					pstmt.setObject(34, rs.getObject("EMPR_STA_REGIME")); 
					pstmt.setObject(35, rs.getObject("EMPR_DAT_INCLUSAO")); 
					pstmt.setObject(36, rs.getObject("EMPR_STA_MICRO")); 
					pstmt.setObject(37, rs.getObject("EMPR_DAT_ALTERACAO")); 
					pstmt.setObject(38, rs.getObject("EMPR_STA_ESPECIAIS")); 
					pstmt.setObject(39, rs.getObject("EMPR_STA_DEDUZ_CONSTRUCAO")); 
					pstmt.setObject(40, rs.getObject("EMPR_STA_ORG")); 
					pstmt.setObject(41, rs.getObject("EMPR_ORG_TIPO")); 
					pstmt.setObject(42, rs.getObject("EMPR_NUM_DG")); 
					pstmt.setObject(43, rs.getObject("EMPR_ID_COMDOMINIO")); 
					pstmt.setObject(44, rs.getObject("EMPR_ID_CONSTRUCAO")); 
					pstmt.setObject(45, rs.getObject("CONT_NUM_ADMINISTRADOR")); 
					pstmt.setObject(46, rs.getObject("EMPR_STA_FISICA")); 
					pstmt.setObject(47, rs.getObject("EMPR_STA_ACCESSO_LIBERADO")); 
					pstmt.setObject(48, rs.getObject("EMPR_USU_GESTAO")); 
					pstmt.setObject(49, rs.getObject("EMPR_STA_RETENCAO")); 
					pstmt.setObject(50, rs.getObject("EMPR_ABAT_CONTRIBUINTE")); 
					pstmt.setObject(51, rs.getObject("EMPR_LOGO_RECIBO")); 
					pstmt.setObject(52, rs.getObject("EMPR_VALIDA_MES")); 
					pstmt.setObject(53, rs.getObject("EMPR_DT_ATUALIZACAO")); 
					pstmt.setObject(54, rs.getObject("EMPR_TIPO_BANCO")); 
					pstmt.setObject(55, rs.getObject("EMPR_AUTO_CADASTRO")); 
					pstmt.setObject(56, rs.getObject("EMPR_AREA_TOTAL")); 
					pstmt.setObject(57, rs.getObject("EMPR_AREA_OCUPADA")); 
					pstmt.setObject(58, rs.getObject("EMPR_QTD_PROFISSIONAIS")); 
					pstmt.setObject(59, rs.getObject("EMPR_NIVEL_INSTRUCAO")); 
					pstmt.setObject(60, rs.getObject("EMPR_NOM_FANTASIA")); 
					pstmt.setObject(61, rs.getObject("EMPR_DAT_CONSTITUICAO")); 
					pstmt.setObject(62, rs.getObject("EMCO_NUMERO_DECA")); 
					pstmt.setObject(63, rs.getObject("EMCO_DATA_ENTREGA_DECA")); 
					pstmt.setObject(64, rs.getObject("EMPR_COOPERATIVA")); 
					pstmt.setObject(65, rs.getObject("EMPR_GAVULSA_BLOQUEADA")); 
					pstmt.setObject(66, rs.getObject("EMPR_ORG_ESTABELECIDO"));
					try {
						pstmt.execute();
					}
					catch(Exception ins) {
						System.out.println("Erro na empresa: "+rs.getObject("EMPR_NOM_EMPRESA"));
						ins.printStackTrace(System.out);	
					}
				}
			}
			/*
			 * TB_EMPRESAS_NFS
			 */
			nmTabela = "TB_EMPRESAS_NFS";
			rs = connOra.prepareStatement("SELECT COUNT(*) as TOTAL FROM vconquista."+nmTabela).executeQuery();
			total = rs.next() ? rs.getInt(1) : 0;
			count = 0;
			System.out.println("Efetuando carga de "+nmTabela+"...");
			rs = connOra.prepareStatement("SELECT * FROM vconquista."+nmTabela+(isTeste?" WHERE rownum < 1001 " : "")).executeQuery();
			System.out.println("Selecionados "+total+" registros em "+nmTabela);
			//
			pstmt = connMySql.prepareStatement("INSERT INTO "+nmTabela+" (CLIE_COD_CLIENTE,MOBI_NUM_CADASTRO,ENFS_MES_COMPETENCIA,ENFS_ANO_COMPETENCIA,ID,ENFS_DIA_COMPETENCIA,ENFS_NUM_NFS_INI,ENFS_NUM_SR_INI,ENFS_NUM_NFS_FIM,ENFS_NUM_SR_FIM,ENFS_DAT_ESCRITURACAO,ENFS_VLR_NFS,ENFS_STA_NFS,ATIT_COD_ATIVIDADE,TOMA_STA_ESTABELECIDO,TOMA_NUM_INSCRICAO,TOMA_NOM_PESSOA,TOMA_INS_ESTADUALL,TOMA_TIPO,TOMA_CPF_CGC,TOMA_ABR_TIPO,TOMA_ABR_TITL,TOMA_LOGRADOURO,TOMA_NUMERO,TOMA_COMPLEMENTO,TOMA_BAIRRO,TOMA_CEP,TOMA_CIDADE,TOMA_ESTADO,ENFS_STA_ESCRITURACAO,ENFS_STA_FECHADO,ENFS_DAT_INS,SUBS_ID,SIGLA_PAIS,EMPRESA_EXTERIOR,ENFS_STA_MODALIDADE,TOMA_NUM_DG,TOMA_ISENTO_IE,ENFS_ORIGEM_DADOS,BANC_COD_BANCO,ISCB_NUM_CONTA,ENFS_STA_ACEITE,ENFS_OBRA_ID,EXTERIOR_INFORMACOES,PEDA_MUNICIPIO_NOME,PEDA_STA_PEDAGIO,PEDA_PERCENT_MUNICIPIO,PEDA_REDACRE,PEDA_BASE,PEDA_ALIQUOTA,LICO_COD_LISTA,LICO_COD_LISTA_FK,PEDA_NOME,ENFS_BOLE_ID,TOMA_SENHA_INFORME,TOMA_NUM_INFORME,ENFS_STA_SERVICO,PEDA_EXTENSAO_VIA,ENFS_TIPO_SERVICO,ISCB_NUM_CONTA_PAI,ENFS_BASE_CALCULO,ENFS_MOBI_NOTA_AVULSA,ENFS_MES_PRESTACAO,ENFS_ANO_PRESTACAO,ENFS_REMOTE_IP,ENFS_ESPACO_CEDIDO,ENFS_COOPERADOS,ENFS_ID_LOTE_ABATIMENTO,TOMA_INS_ESTADUAL,ENFS_SUS,ENFS_VLR_ALIQUOTA,ENFS_TIP_DOC_CARTORIO,ENFS_SSIMPLES_SETADO,TIMESTAMP) " +
											   "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); 
			connMySql.setAutoCommit(false);
			while(rs.next()) {
				count++;
				
				if(count <= 1494128)
					continue;
				
				if(count%1000==0) {
					System.out.println("Commit. "+count+"/"+total);
					if(isTeste && count > 1000)
						break;
					connMySql.commit();
					if(count%10000==0){
						System.out.println("Garbage Colector!");
						connMySql.close();
						System.gc();
						connMySql = conectarMySQL();
						connMySql.setAutoCommit(false);
						pstmt = connMySql.prepareStatement("INSERT INTO "+nmTabela+" (CLIE_COD_CLIENTE,MOBI_NUM_CADASTRO,ENFS_MES_COMPETENCIA,ENFS_ANO_COMPETENCIA,ID,ENFS_DIA_COMPETENCIA,ENFS_NUM_NFS_INI,ENFS_NUM_SR_INI,ENFS_NUM_NFS_FIM,ENFS_NUM_SR_FIM,ENFS_DAT_ESCRITURACAO,ENFS_VLR_NFS,ENFS_STA_NFS,ATIT_COD_ATIVIDADE,TOMA_STA_ESTABELECIDO,TOMA_NUM_INSCRICAO,TOMA_NOM_PESSOA,TOMA_INS_ESTADUALL,TOMA_TIPO,TOMA_CPF_CGC,TOMA_ABR_TIPO,TOMA_ABR_TITL,TOMA_LOGRADOURO,TOMA_NUMERO,TOMA_COMPLEMENTO,TOMA_BAIRRO,TOMA_CEP,TOMA_CIDADE,TOMA_ESTADO,ENFS_STA_ESCRITURACAO,ENFS_STA_FECHADO,ENFS_DAT_INS,SUBS_ID,SIGLA_PAIS,EMPRESA_EXTERIOR,ENFS_STA_MODALIDADE,TOMA_NUM_DG,TOMA_ISENTO_IE,ENFS_ORIGEM_DADOS,BANC_COD_BANCO,ISCB_NUM_CONTA,ENFS_STA_ACEITE,ENFS_OBRA_ID,EXTERIOR_INFORMACOES,PEDA_MUNICIPIO_NOME,PEDA_STA_PEDAGIO,PEDA_PERCENT_MUNICIPIO,PEDA_REDACRE,PEDA_BASE,PEDA_ALIQUOTA,LICO_COD_LISTA,LICO_COD_LISTA_FK,PEDA_NOME,ENFS_BOLE_ID,TOMA_SENHA_INFORME,TOMA_NUM_INFORME,ENFS_STA_SERVICO,PEDA_EXTENSAO_VIA,ENFS_TIPO_SERVICO,ISCB_NUM_CONTA_PAI,ENFS_BASE_CALCULO,ENFS_MOBI_NOTA_AVULSA,ENFS_MES_PRESTACAO,ENFS_ANO_PRESTACAO,ENFS_REMOTE_IP,ENFS_ESPACO_CEDIDO,ENFS_COOPERADOS,ENFS_ID_LOTE_ABATIMENTO,TOMA_INS_ESTADUAL,ENFS_SUS,ENFS_VLR_ALIQUOTA,ENFS_TIP_DOC_CARTORIO,ENFS_SSIMPLES_SETADO,TIMESTAMP) " +
								   						   "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); 
					}
				}
				
				pstmt.setObject(1, rs.getObject("CLIE_COD_CLIENTE"));
				pstmt.setObject(2, rs.getObject("MOBI_NUM_CADASTRO"));
				pstmt.setObject(3, rs.getObject("ENFS_MES_COMPETENCIA"));
				pstmt.setObject(4, rs.getObject("ENFS_ANO_COMPETENCIA"));
				pstmt.setObject(5, rs.getObject("ID"));
				pstmt.setObject(6, rs.getObject("ENFS_DIA_COMPETENCIA"));
				pstmt.setObject(7, rs.getObject("ENFS_NUM_NFS_INI"));
				pstmt.setObject(8, rs.getObject("ENFS_NUM_SR_INI"));
				pstmt.setObject(9, rs.getObject("ENFS_NUM_NFS_FIM"));
				pstmt.setObject(10, rs.getObject("ENFS_NUM_SR_FIM"));
				pstmt.setObject(11, rs.getObject("ENFS_DAT_ESCRITURACAO"));
				pstmt.setObject(12, rs.getObject("ENFS_VLR_NFS"));
				pstmt.setObject(13, rs.getObject("ENFS_STA_NFS"));
				pstmt.setObject(14, rs.getObject("ATIT_COD_ATIVIDADE"));
				pstmt.setObject(15, rs.getObject("TOMA_STA_ESTABELECIDO"));
				pstmt.setObject(16, rs.getObject("TOMA_NUM_INSCRICAO"));
				pstmt.setObject(17, rs.getObject("TOMA_NOM_PESSOA"));
				pstmt.setObject(18, rs.getObject("TOMA_INS_ESTADUALL"));
				pstmt.setObject(19, rs.getObject("TOMA_TIPO"));
				pstmt.setObject(20, rs.getObject("TOMA_CPF_CGC"));
				pstmt.setObject(21, rs.getObject("TOMA_ABR_TIPO"));
				pstmt.setObject(22, rs.getObject("TOMA_ABR_TITL"));
				pstmt.setObject(23, rs.getObject("TOMA_LOGRADOURO"));
				pstmt.setObject(24, rs.getObject("TOMA_NUMERO"));
				pstmt.setObject(25, rs.getObject("TOMA_COMPLEMENTO"));
				pstmt.setObject(26, rs.getObject("TOMA_BAIRRO"));
				pstmt.setObject(27, rs.getObject("TOMA_CEP"));
				pstmt.setObject(28, rs.getObject("TOMA_CIDADE"));
				pstmt.setObject(29, rs.getObject("TOMA_ESTADO"));
				pstmt.setObject(30, rs.getObject("ENFS_STA_ESCRITURACAO"));
				pstmt.setObject(31, rs.getObject("ENFS_STA_FECHADO"));
				pstmt.setObject(32, rs.getObject("ENFS_DAT_INS"));
				pstmt.setObject(33, rs.getObject("SUBS_ID"));
				pstmt.setObject(34, rs.getObject("SIGLA_PAIS"));
				pstmt.setObject(35, rs.getObject("EMPRESA_EXTERIOR"));
				pstmt.setObject(36, rs.getObject("ENFS_STA_MODALIDADE"));
				pstmt.setObject(37, rs.getObject("TOMA_NUM_DG"));
				pstmt.setObject(38, rs.getObject("TOMA_ISENTO_IE"));
				pstmt.setObject(39, rs.getObject("ENFS_ORIGEM_DADOS"));
				pstmt.setObject(40, rs.getObject("BANC_COD_BANCO"));
				pstmt.setObject(41, rs.getObject("ISCB_NUM_CONTA"));
				pstmt.setObject(42, rs.getObject("ENFS_STA_ACEITE"));
				pstmt.setObject(43, rs.getObject("ENFS_OBRA_ID"));
				pstmt.setObject(44, rs.getObject("EXTERIOR_INFORMACOES"));
				pstmt.setObject(45, rs.getObject("PEDA_MUNICIPIO_NOME"));
				pstmt.setObject(46, rs.getObject("PEDA_STA_PEDAGIO"));
				pstmt.setObject(47, rs.getObject("PEDA_PERCENT_MUNICIPIO"));
				pstmt.setObject(48, rs.getObject("PEDA_REDACRE"));
				pstmt.setObject(49, rs.getObject("PEDA_BASE"));
				pstmt.setObject(50, rs.getObject("PEDA_ALIQUOTA"));
				pstmt.setObject(51, rs.getObject("LICO_COD_LISTA"));
				pstmt.setObject(52, rs.getObject("LICO_COD_LISTA_FK"));
				pstmt.setObject(53, rs.getObject("PEDA_NOME"));
				pstmt.setObject(54, rs.getObject("ENFS_BOLE_ID"));
				pstmt.setObject(55, rs.getObject("TOMA_SENHA_INFORME"));
				pstmt.setObject(56, rs.getObject("TOMA_NUM_INFORME"));
				pstmt.setObject(57, rs.getObject("ENFS_STA_SERVICO"));
				pstmt.setObject(58, rs.getObject("PEDA_EXTENSAO_VIA"));
				pstmt.setObject(59, rs.getObject("ENFS_TIPO_SERVICO"));
				pstmt.setObject(60, rs.getObject("ISCB_NUM_CONTA_PAI"));
				pstmt.setObject(61, rs.getObject("ENFS_BASE_CALCULO"));
				pstmt.setObject(62, rs.getObject("ENFS_MOBI_NOTA_AVULSA"));
				pstmt.setObject(63, rs.getObject("ENFS_MES_PRESTACAO"));
				pstmt.setObject(64, rs.getObject("ENFS_ANO_PRESTACAO"));
				pstmt.setObject(65, rs.getObject("ENFS_REMOTE_IP"));
				pstmt.setObject(66, rs.getObject("ENFS_ESPACO_CEDIDO"));
				pstmt.setObject(67, rs.getObject("ENFS_COOPERADOS"));
				pstmt.setObject(68, rs.getObject("ENFS_ID_LOTE_ABATIMENTO"));
				pstmt.setObject(69, rs.getObject("TOMA_INS_ESTADUAL"));
				pstmt.setObject(70, rs.getObject("ENFS_SUS"));
				pstmt.setObject(71, rs.getObject("ENFS_VLR_ALIQUOTA"));
				pstmt.setObject(72, rs.getObject("ENFS_TIP_DOC_CARTORIO"));
				pstmt.setObject(73, rs.getObject("ENFS_SSIMPLES_SETADO"));
				pstmt.setObject(74, rs.getObject("TIMESTAMP"));

				try {
					pstmt.execute();
				}
				catch(Exception ins) {
					System.out.println("Erro na "+nmTabela+": "+rs.getObject("MOBI_NUM_CADASTRO"));
					ins.printStackTrace(System.out);	
				}
			}
			connMySql.commit();
			System.out.println("Importação de "+nmTabela+" concluida com sucesso!");
			System.out.println("Importação de empresas concluida com sucesso!");
			System.out.println("**********************************************");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
