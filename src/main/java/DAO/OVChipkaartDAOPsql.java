package main.java.DAO;

import main.java.POJO.OVChipkaart;
import main.java.POJO.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OVChipkaartDAOPsql
        implements OVChipkaartDAO {

    private final Connection conn;
    private ReizigerDAO reizigerDAO;

    public OVChipkaartDAOPsql(
            Connection conn) {

        this.conn = conn;
    }

    public void setReizigerDAO(
            ReizigerDAO reizigerDAO) {

        this.reizigerDAO = reizigerDAO;
    }

    @Override
    public boolean save(
            OVChipkaart ovChipkaart)
            throws SQLException {

        if (ovChipkaart == null) {
            return false;
        }

        String query =
                "INSERT INTO ov_chipkaart " +
                        "(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    ovChipkaart.getKaart_nummer()
            );

            statement.setDate(
                    2,
                    java.sql.Date.valueOf(
                            ovChipkaart.getGeldig_tot()
                    )
            );

            statement.setInt(
                    3,
                    ovChipkaart.getKlasse()
            );

            statement.setDouble(
                    4,
                    ovChipkaart.getSaldo()
            );

            if (ovChipkaart.getReiziger() != null) {

                statement.setInt(
                        5,
                        ovChipkaart
                                .getReiziger()
                                .getId()
                );

            } else {

                statement.setNull(
                        5,
                        Types.INTEGER
                );
            }

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(
            OVChipkaart ovChipkaart)
            throws SQLException {

        if (ovChipkaart == null) {
            return false;
        }

        String query =
                "UPDATE ov_chipkaart " +
                        "SET geldig_tot = ?, " +
                        "klasse = ?, " +
                        "saldo = ?, " +
                        "reiziger_id = ? " +
                        "WHERE kaart_nummer = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setDate(
                    1,
                    java.sql.Date.valueOf(
                            ovChipkaart.getGeldig_tot()
                    )
            );

            statement.setInt(
                    2,
                    ovChipkaart.getKlasse()
            );

            statement.setDouble(
                    3,
                    ovChipkaart.getSaldo()
            );

            if (ovChipkaart.getReiziger() != null) {

                statement.setInt(
                        4,
                        ovChipkaart
                                .getReiziger()
                                .getId()
                );

            } else {

                statement.setNull(
                        4,
                        Types.INTEGER
                );
            }

            statement.setInt(
                    5,
                    ovChipkaart.getKaart_nummer()
            );

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(
            OVChipkaart ovChipkaart)
            throws SQLException {

        if (ovChipkaart == null) {
            return false;
        }

        String query =
                "DELETE FROM ov_chipkaart " +
                        "WHERE kaart_nummer = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    ovChipkaart.getKaart_nummer()
            );

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(
            Reiziger reiziger)
            throws SQLException {

        List<OVChipkaart> ovChipkaarten =
                new ArrayList<>();

        if (reiziger == null) {
            return ovChipkaarten;
        }

        String query =
                "SELECT kaart_nummer, " +
                        "geldig_tot, " +
                        "klasse, " +
                        "saldo " +
                        "FROM ov_chipkaart " +
                        "WHERE reiziger_id = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    reiziger.getId()
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    OVChipkaart ovChipkaart =
                            new OVChipkaart(
                                    resultSet.getInt(
                                            "kaart_nummer"
                                    ),
                                    resultSet.getDate(
                                            "geldig_tot"
                                    ).toLocalDate(),
                                    resultSet.getInt(
                                            "klasse"
                                    ),
                                    resultSet.getDouble(
                                            "saldo"
                                    ),
                                    reiziger
                            );

                    ovChipkaarten.add(
                            ovChipkaart
                    );
                }
            }
        }

        return ovChipkaarten;
    }

    @Override
    public List<OVChipkaart> findAll()
            throws SQLException {

        List<OVChipkaart> ovChipkaarten =
                new ArrayList<>();

        Map<Integer, Reiziger> reizigersCache =
                new HashMap<>();

        String query =
                "SELECT kaart_nummer, " +
                        "geldig_tot, " +
                        "klasse, " +
                        "saldo, " +
                        "reiziger_id " +
                        "FROM ov_chipkaart";

        try (PreparedStatement statement =
                     conn.prepareStatement(query);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                int reizigerId =
                        resultSet.getInt(
                                "reiziger_id"
                        );

                boolean reizigerIdWasNull =
                        resultSet.wasNull();

                Reiziger reiziger =
                        null;

                if (!reizigerIdWasNull &&
                        reizigerDAO != null) {

                    if (reizigersCache.containsKey(
                            reizigerId)) {

                        reiziger =
                                reizigersCache.get(
                                        reizigerId
                                );

                    } else {

                        reiziger =
                                reizigerDAO.findById(
                                        reizigerId
                                );

                        reizigersCache.put(
                                reizigerId,
                                reiziger
                        );
                    }
                }

                OVChipkaart ovChipkaart =
                        new OVChipkaart(
                                resultSet.getInt(
                                        "kaart_nummer"
                                ),
                                resultSet.getDate(
                                        "geldig_tot"
                                ).toLocalDate(),
                                resultSet.getInt(
                                        "klasse"
                                ),
                                resultSet.getDouble(
                                        "saldo"
                                ),
                                reiziger
                        );

                ovChipkaarten.add(
                        ovChipkaart
                );
            }
        }

        return ovChipkaarten;
    }
}