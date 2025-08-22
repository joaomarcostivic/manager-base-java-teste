package com.tivic.manager.adapter.base.antiga.ait;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitRepositoryOldDAO implements AitRepository {
	
	private final IAdapterService<AitOld, Ait> adapterService;
	
	public AitRepositoryOldDAO() {
		this.adapterService = new AdapterAitService();
	}

	@Override
	public Ait insert(Ait ait, CustomConnection customConnection) throws Exception {
		AitOld aitOld = this.adapterService.toBaseAntiga(ait);
		int codRetorno = AitOldDAO.insert(aitOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new ValidacaoException("Erro ao inserir AIT.");
		ait.setCdAit(codRetorno);
		return ait;
	}

	@Override
	public Ait update(Ait ait, CustomConnection customConnection) throws Exception {
		AitOld aitOld = this.adapterService.toBaseAntiga(ait);
		int codRetorno = AitOldDAO.update(aitOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new ValidacaoException("Erro ao atualizar AIT.");
		return ait;
	}

	@Override
	public Ait get(int cdAit) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Ait ait = get(cdAit, customConnection);
			customConnection.finishConnection();
			return ait;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Ait get(int cdAit, CustomConnection customConnection) throws Exception {
		AitOld aitOld = AitOldDAO.get(cdAit, customConnection.getConnection());
		return this.adapterService.toBaseNova(aitOld);
	}

	@Override
	public List<Ait> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Ait> aitList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return aitList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Ait> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<Ait> aitList = new ArrayList<>();
		ResultSetMapper<AitOld> rsm = new ResultSetMapper<>(AitOldDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), AitOld.class);
		List<AitOld> aitOldList = rsm.toList();
		for (AitOld aitOld : aitOldList) {
			Ait ait = this.adapterService.toBaseNova(aitOld);
			aitList.add(ait);
		}
		return aitList;
	}

	@Override
	public List<Ait> findByEvento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<Ait> aitList = new ArrayList<>();

		Search<AitOld> search = new SearchBuilder<AitOld>("ait A")
				.fields("A.*, C.cd_evento")	
				.addJoinTable("LEFT OUTER JOIN mob_ait_evento B ON (A.codigo_ait = B.cd_ait)")
				.addJoinTable("LEFT OUTER JOIN mob_evento_equipamento C ON (B.cd_evento = C.cd_evento)")
				.searchCriterios(searchCriterios)
				.build();
		
		for (AitOld aitOld : search.getList(AitOld.class)) {
			Ait ait = this.adapterService.toBaseNova(aitOld);
			aitList.add(ait);
		}
		return aitList;
	}
	
	public boolean hasAit(String nrAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nr_ait", nrAit, true);
		List<Ait> aits = find(searchCriterios, customConnection);

		return !aits.isEmpty();
	}

	public int getUltimoNrAitByTalao(Talonario talonario, CustomConnection customConnection) throws Exception {
		String sql =
				"WITH ait_numeros AS ( " +
						"  SELECT " +
						"    CAST(SUBSTRING(nr_ait FROM '[0-9]+') AS INTEGER) AS nr " +
						"  FROM ait " +
						"  WHERE " +
						"    (" +
						"      (? IS NOT NULL AND nr_ait LIKE ?) " +
						"      OR (? IS NULL AND nr_ait ~ '^[0-9]+$') " +
						"    )" +
						"    AND nr_ait ~ '[0-9]+' " +
						")" +
						"SELECT MAX(nr) AS maior_numero " +
						"FROM ait_numeros " +
						"WHERE nr BETWEEN ? AND ? ";

		try (PreparedStatement ps = customConnection.getConnection().prepareStatement(sql)) {
			String sigla = talonario.getSgTalao();
			boolean hasSigla = sigla != null && !sigla.isEmpty();

			if (hasSigla) {
				ps.setString(1, sigla);
				ps.setString(2, sigla + "%");
				ps.setString(3, sigla);
			} else {
				ps.setNull(1, Types.VARCHAR);
				ps.setNull(2, Types.VARCHAR);
				ps.setNull(3, Types.VARCHAR);
			}

			ps.setInt(4, talonario.getNrInicial());
			ps.setInt(5, talonario.getNrFinal());

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int numero = rs.getInt("maior_numero");
					return rs.wasNull() ? defaultValor(talonario) : numero;
				}
			}
		}

		return defaultValor(talonario);
	}

	private int defaultValor(Talonario talonario) {
	    return talonario.getNrInicial() > 0 ? talonario.getNrInicial() - 1 : 0;
	}
}