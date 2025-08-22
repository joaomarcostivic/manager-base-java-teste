package com.tivic.manager.ctb;

import java.sql.Connection;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class PlanoContasServices {

	public static PlanoContas save(PlanoContas planoContas){
		return save(planoContas, null);
	}

	public static PlanoContas save(PlanoContas planoContas, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}

			int retorno = 1;
			if(planoContas.getCdPlanoContas()==0){
				retorno = PlanoContasDAO.insert(planoContas, connect);
				if(retorno > 0){
					planoContas.setCdPlanoContas(retorno);
				}
			}
			else {
				retorno = PlanoContasDAO.update(planoContas, connect);
			}

			return planoContas;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoContasServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT DISTINCT A.*, " +
						   " 	B.nr_conta || ' - ' || L.nm_conta AS nm_conta_ativo, " +
						   "	C.nr_conta || ' - ' || M.nm_conta AS nm_conta_passivo, " +
						   "	D.nr_conta || ' - ' || N.nm_conta AS nm_conta_receita, " +
						   "	E.nr_conta || ' - ' || O.nm_conta AS nm_conta_despesa, " +
						   " 	F.nr_conta || ' - ' || P.nm_conta AS nm_conta_lucro, " +
						   "	G.nr_conta || ' - ' || Q.nm_conta AS nm_conta_prejuizo, " +
						   "	I.nr_conta || ' - ' || R.nm_conta AS nm_conta_custo, " +
						   "	J.nr_conta || ' - ' || S.nm_conta AS nm_conta_disponivel, " +
						   "	K.nr_conta || ' - ' || T.nm_conta AS nm_conta_patrimonio_liquido, " +
						   "	U.nr_conta || ' - ' || V.nm_conta AS nm_conta_resultado, " +
						   " 	H.nm_moeda || ' - ' || H.sg_moeda AS nm_moeda " +
						   "FROM ctb_plano_contas A " +
						   "	LEFT OUTER JOIN ctb_conta_plano_contas B ON (A.cd_conta_ativo = B.cd_conta AND A.cd_plano_contas = B.cd_plano_contas) " +
						   "	LEFT OUTER JOIN ctb_conta_plano_contas C ON (A.cd_conta_passivo = C.cd_conta AND A.cd_plano_contas = C.cd_plano_contas) " +
						   "	LEFT OUTER JOIN ctb_conta_plano_contas D ON (A.cd_conta_receita = D.cd_conta AND A.cd_plano_contas = D.cd_plano_contas) " +
						   "	LEFT OUTER JOIN ctb_conta_plano_contas E ON (A.cd_conta_despesa = E.cd_conta AND A.cd_plano_contas = E.cd_plano_contas) " +
						   "	LEFT OUTER JOIN ctb_conta_plano_contas F ON (A.cd_conta_lucro = F.cd_conta AND A.cd_plano_contas = F.cd_plano_contas) " +
						   "	LEFT OUTER JOIN ctb_conta_plano_contas G ON (A.cd_conta_prejuizo = G.cd_conta AND A.cd_plano_contas = G.cd_plano_contas) " +
						   "	LEFT OUTER JOIN ctb_conta_plano_contas I ON (A.cd_conta_custo = I.cd_conta AND A.cd_plano_contas = I.cd_plano_contas) " +
						   "	LEFT OUTER JOIN ctb_conta_plano_contas J ON (A.cd_conta_disponivel = J.cd_conta AND A.cd_plano_contas = J.cd_plano_contas) " +
						   "	LEFT OUTER JOIN ctb_conta_plano_contas K ON (A.cd_conta_patrimonio_liquido = K.cd_conta AND A.cd_plano_contas = K.cd_plano_contas) " +
						   "	LEFT OUTER JOIN ctb_conta_plano_contas U ON (A.cd_conta_resultado = U.cd_conta AND A.cd_plano_contas = U.cd_plano_contas) " +
						   "	LEFT OUTER JOIN ctb_conta L ON (A.cd_conta_ativo = L.cd_conta) " +
						   "	LEFT OUTER JOIN ctb_conta M ON (A.cd_conta_passivo = M.cd_conta) " +
						   "	LEFT OUTER JOIN ctb_conta N ON (A.cd_conta_receita = N.cd_conta) " +
						   "	LEFT OUTER JOIN ctb_conta O ON (A.cd_conta_despesa = O.cd_conta) " +
						   "	LEFT OUTER JOIN ctb_conta P ON (A.cd_conta_lucro = P.cd_conta) " +
						   "	LEFT OUTER JOIN ctb_conta Q ON (A.cd_conta_prejuizo = Q.cd_conta) " +
						   "	LEFT OUTER JOIN ctb_conta R ON (A.cd_conta_custo = R.cd_conta) " +
						   "	LEFT OUTER JOIN ctb_conta S ON (A.cd_conta_disponivel = S.cd_conta) " +
						   "	LEFT OUTER JOIN ctb_conta T ON (A.cd_conta_patrimonio_liquido = T.cd_conta) " +
						   "	LEFT OUTER JOIN ctb_conta V ON (A.cd_conta_resultado = V.cd_conta) " +
						   "	LEFT OUTER JOIN grl_moeda H ON (A.cd_moeda = H.cd_moeda) ", criterios, true, connect != null ? connect : Conexao.conectar(), connect == null);
	}
}
