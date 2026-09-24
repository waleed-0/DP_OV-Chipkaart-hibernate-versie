package main.java.DAO;

import main.java.POJO.OVChipkaart;
import main.java.POJO.Reiziger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReizigerDAOPsql
        implements ReizigerDAO {

    private final Connection conn;
    private OVChipkaartDAO ovChipkaartDAO;

    public ReizigerDAOPsql(
            Connection conn) {

        this.conn = conn;
    }

    public void setOVChipkaartDAO(
            OVChipkaartDAO ovChipkaartDAO) {

        this.ovChipkaartDAO =
                ovChipkaartDAO;
    }

    @Override
    public boolean save(
            Reiziger reiziger)
            throws SQLException {

        if (reiziger == null) {
            return false;
        }

        String query =
                "INSERT INTO reiziger " +
                        "(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    reiziger.getId()
            );

            statement.setString(
                    2,
                    reiziger.getVoorletters()
            );

            statement.setString(
                    3,
                    reiziger.getTussenvoegsel()
            );

            statement.setString(
                    4,
                    reiziger.getAchternaam()
            );

            statement.setDate(
                    5,
                    reiziger.getGeboortedatum()
            );

            if (statement.executeUpdate() <= 0) {
                return false;
            }
        }

        if (ovChipkaartDAO != null &&
                reiziger.getOvChipkaarten() != null) {

            for (OVChipkaart ovChipkaart :
                    reiziger.getOvChipkaarten()) {

                ovChipkaart.setReiziger(
                        reiziger
                );

                if (!ovChipkaartDAO.save(
                        ovChipkaart)) {

                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean update(
            Reiziger reiziger)
            throws SQLException {

        if (reiziger == null) {
            return false;
        }

        String query =
                "UPDATE reiziger " +
                        "SET voorletters = ?, " +
                        "tussenvoegsel = ?, " +
                        "achternaam = ?, " +
                        "geboortedatum = ? " +
                        "WHERE reiziger_id = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setString(
                    1,
                    reiziger.getVoorletters()
            );

            statement.setString(
                    2,
                    reiziger.getTussenvoegsel()
            );

            statement.setString(
                    3,
                    reiziger.getAchternaam()
            );

            statement.setDate(
                    4,
                    reiziger.getGeboortedatum()
            );

            statement.setInt(
                    5,
                    reiziger.getId()
            );

            if (statement.executeUpdate() <= 0) {
                return false;
            }
        }

        if (ovChipkaartDAO != null) {

            List<OVChipkaart> databaseKaarten =
                    ovChipkaartDAO.findByReiziger(
                            reiziger
                    );

            Map<Integer, OVChipkaart> databaseKaartenMap =
                    new HashMap<>();

            for (OVChipkaart databaseKaart :
                    databaseKaarten) {

                databaseKaartenMap.put(
                        databaseKaart.getKaart_nummer(),
                        databaseKaart
                );
            }

            Map<Integer, OVChipkaart> javaKaartenMap =
                    new HashMap<>();

            if (reiziger.getOvChipkaarten() != null) {

                for (OVChipkaart ovChipkaart :
                        reiziger.getOvChipkaarten()) {

                    ovChipkaart.setReiziger(
                            reiziger
                    );

                    javaKaartenMap.put(
                            ovChipkaart.getKaart_nummer(),
                            ovChipkaart
                    );

                    if (databaseKaartenMap.containsKey(
                            ovChipkaart.getKaart_nummer())) {

                        if (!ovChipkaartDAO.update(
                                ovChipkaart)) {

                            return false;
                        }

                    } else {

                        if (!ovChipkaartDAO.save(
                                ovChipkaart)) {

                            return false;
                        }
                    }
                }
            }

            for (OVChipkaart databaseKaart :
                    databaseKaarten) {

                if (!javaKaartenMap.containsKey(
                        databaseKaart.getKaart_nummer())) {

                    if (!ovChipkaartDAO.delete(
                            databaseKaart)) {

                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean delete(
            Reiziger reiziger)
            throws SQLException {

        if (reiziger == null) {
            return false;
        }

        if (ovChipkaartDAO != null) {

            List<OVChipkaart> ovChipkaarten =
                    ovChipkaartDAO.findByReiziger(
                            reiziger
                    );

            for (OVChipkaart ovChipkaart :
                    ovChipkaarten) {

                if (!ovChipkaartDAO.delete(
                        ovChipkaart)) {

                    return false;
                }
            }
        }

        String query =
                "DELETE FROM reiziger " +
                        "WHERE reiziger_id = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    reiziger.getId()
            );

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public Reiziger findById(
            int id)
            throws SQLException {

        String query =
                "SELECT reiziger_id, " +
                        "voorletters, " +
                        "tussenvoegsel, " +
                        "achternaam, " +
                        "geboortedatum " +
                        "FROM reiziger " +
                        "WHERE reiziger_id = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    id
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    Reiziger reiziger =
                            maakReiziger(
                                    resultSet
                            );

                    vulOVChipkaarten(
                            reiziger
                    );

                    return reiziger;
                }
            }
        }

        return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(
            String datum)
            throws SQLException {

        List<Reiziger> reizigers =
                new ArrayList<>();

        String query =
                "SELECT reiziger_id, " +
                        "voorletters, " +
                        "tussenvoegsel, " +
                        "achternaam, " +
                        "geboortedatum " +
                        "FROM reiziger " +
                        "WHERE geboortedatum = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setDate(
                    1,
                    Date.valueOf(
                            datum
                    )
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Reiziger reiziger =
                            maakReiziger(
                                    resultSet
                            );

                    vulOVChipkaarten(
                            reiziger
                    );

                    reizigers.add(
                            reiziger
                    );
                }
            }
        }

        return reizigers;
    }

    @Override
    public List<Reiziger> findAll()
            throws SQLException {

        List<Reiziger> reizigers =
                new ArrayList<>();

        String query =
                "SELECT reiziger_id, " +
                        "voorletters, " +
                        "tussenvoegsel, " +
                        "achternaam, " +
                        "geboortedatum " +
                        "FROM reiziger";

        try (PreparedStatement statement =
                     conn.prepareStatement(query);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Reiziger reiziger =
                        maakReiziger(
                                resultSet
                        );

                vulOVChipkaarten(
                        reiziger
                );

                reizigers.add(
                        reiziger
                );
            }
        }

        return reizigers;
    }

    private Reiziger maakReiziger(
            ResultSet resultSet)
            throws SQLException {

        return new Reiziger(
                resultSet.getInt(
                        "reiziger_id"
                ),
                resultSet.getString(
                        "voorletters"
                ),
                resultSet.getString(
                        "tussenvoegsel"
                ),
                resultSet.getString(
                        "achternaam"
                ),
                resultSet.getDate(
                        "geboortedatum"
                )
        );
    }

    private void vulOVChipkaarten(
            Reiziger reiziger)
            throws SQLException {

        if (reiziger == null ||
                ovChipkaartDAO == null) {

            return;
        }

        List<OVChipkaart> ovChipkaarten =
                ovChipkaartDAO.findByReiziger(
                        reiziger
                );

        reiziger.setOvChipkaarten(
                ovChipkaarten
        );
    }
}