package main.java.DAO;

import main.java.POJO.OVChipkaart;
import main.java.POJO.Reiziger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {

    private static final String Url =
            "jdbc:postgresql://localhost:5432/ovchip";

    private static final String User =
            "postgres";

    private static final String Password =
            "0000";

    private static final int TEST_REIZIGER_ID =
            100;

    private static final int TEST_KAART_ID_1 =
            123456;

    private static final int TEST_KAART_ID_2 =
            654321;

    private static final int TEST_KAART_ID_3 =
            777777;

    public static void main(String[] args) {

        Connection conn =
                getConnection();

        if (conn == null) {

            System.out.println(
                    "Er is een fout opgetreden tijdens " +
                            "het maken van de databaseverbinding."
            );

            return;
        }

        try {


            ReizigerDAOPsql reizigerDAO =
                    new ReizigerDAOPsql(
                            conn
                    );

            OVChipkaartDAOPsql ovChipkaartDAO =
                    new OVChipkaartDAOPsql(
                            conn
                    );


            reizigerDAO.setOVChipkaartDAO(
                    ovChipkaartDAO
            );

            ovChipkaartDAO.setReizigerDAO(
                    reizigerDAO
            );


            verwijderOudeP4TestData(
                    conn
            );

            testP4(
                    reizigerDAO,
                    ovChipkaartDAO
            );


        } catch (SQLException e) {

            System.out.println(
                    "Er is een databasefout opgetreden."
            );

            e.printStackTrace();

        } finally {

            closeConnection(
                    conn
            );
        }
    }


    private static Connection getConnection() {

        Connection connection =
                null;

        try {

            connection =
                    DriverManager.getConnection(
                            Url,
                            User,
                            Password
                    );

            System.out.println(
                    "Databaseverbinding is ok."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Databaseverbinding kon niet worden gemaakt."
            );

            e.printStackTrace();
        }

        return connection;
    }

    private static void closeConnection(
            Connection connection) {

        if (connection != null) {

            try {

                if (!connection.isClosed()) {

                    connection.close();

                    System.out.println(
                            "Databaseverbinding gesloten."
                    );
                }

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }


    private static void verwijderOudeP4TestData(
            Connection conn)
            throws SQLException {

        System.out.println(
                "\n--- Oude P4-testdata controleren ---"
        );


        String deleteKaarten =
                "DELETE FROM ov_chipkaart " +
                        "WHERE kaart_nummer = ? " +
                        "OR kaart_nummer = ? " +
                        "OR kaart_nummer = ? " +
                        "OR reiziger_id = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(
                             deleteKaarten
                     )) {

            statement.setInt(
                    1,
                    TEST_KAART_ID_1
            );

            statement.setInt(
                    2,
                    TEST_KAART_ID_2
            );

            statement.setInt(
                    3,
                    TEST_KAART_ID_3
            );

            statement.setInt(
                    4,
                    TEST_REIZIGER_ID
            );

            int aantalVerwijderd =
                    statement.executeUpdate();

            if (aantalVerwijderd > 0) {

                System.out.println(
                        aantalVerwijderd +
                                " oude test-OVChipkaart(en) verwijderd."
                );
            }
        }


        String deleteAdres =
                "DELETE FROM adres " +
                        "WHERE reiziger_id = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(
                             deleteAdres
                     )) {

            statement.setInt(
                    1,
                    TEST_REIZIGER_ID
            );

            statement.executeUpdate();
        }

        String deleteReiziger =
                "DELETE FROM reiziger " +
                        "WHERE reiziger_id = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(
                             deleteReiziger
                     )) {

            statement.setInt(
                    1,
                    TEST_REIZIGER_ID
            );

            int aantalVerwijderd =
                    statement.executeUpdate();

            if (aantalVerwijderd > 0) {

                System.out.println(
                        "Oude P4-testreiziger met ID " +
                                TEST_REIZIGER_ID +
                                " verwijderd."
                );
            }
        }

        System.out.println(
                "Database is klaar voor de P4-test."
        );
    }

    public static void testP4(
            ReizigerDAO reizigerDAO,
            OVChipkaartDAO ovChipkaartDAO)
            throws SQLException {

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "        P4 - VOLLEDIGE DAO TEST"
        );

        System.out.println(
                "=========================================="
        );


        Reiziger reiziger =
                new Reiziger(
                        TEST_REIZIGER_ID,
                        "W.",
                        null,
                        "Test",
                        Date.valueOf(
                                "2000-01-01"
                        )
                );


        OVChipkaart kaart1 =
                new OVChipkaart(
                        TEST_KAART_ID_1,
                        LocalDate.of(
                                2028,
                                12,
                                31
                        ),
                        2,
                        25.50
                );

        OVChipkaart kaart2 =
                new OVChipkaart(
                        TEST_KAART_ID_2,
                        LocalDate.of(
                                2029,
                                6,
                                30
                        ),
                        1,
                        50.00
                );



        System.out.println(
                "\n--- Bidirectionele relatie maken ---"
        );

        reiziger.voegToeOVChipkaart(
                kaart1
        );

        reiziger.voegToeOVChipkaart(
                kaart2
        );

        System.out.println(
                "Aantal kaarten bij Reiziger: " +
                        reiziger
                                .getOvChipkaarten()
                                .size()
        );

        System.out.println(
                "Reiziger van kaart 1: #" +
                        kaart1
                                .getReiziger()
                                .getId()
        );

        System.out.println(
                "Reiziger van kaart 2: #" +
                        kaart2
                                .getReiziger()
                                .getId()
        );


        System.out.println(
                "\n--- Reiziger + OVChipkaarten opslaan ---"
        );

        boolean opgeslagen =
                reizigerDAO.save(
                        reiziger
                );

        System.out.println(
                "Reiziger opgeslagen: " +
                        opgeslagen
        );



        System.out.println(
                "\n--- Kaarten controleren in database ---"
        );

        List<OVChipkaart> opgeslagenKaarten =
                ovChipkaartDAO.findByReiziger(
                        reiziger
                );

        for (OVChipkaart kaart :
                opgeslagenKaarten) {

            System.out.println(
                    kaart
            );
        }

        System.out.println(
                "Aantal opgeslagen kaarten: " +
                        opgeslagenKaarten.size()
        );



        System.out.println(
                "\n--- ReizigerDAO.findById() ---"
        );

        Reiziger reizigerUitDatabase =
                reizigerDAO.findById(
                        TEST_REIZIGER_ID
                );

        System.out.println(
                reizigerUitDatabase
        );

        if (reizigerUitDatabase != null) {

            System.out.println(
                    "Aantal opgehaalde OVChipkaarten: " +
                            reizigerUitDatabase
                                    .getOvChipkaarten()
                                    .size()
            );
        }

        System.out.println(
                "\n--- ReizigerDAO.findByGbdatum() ---"
        );

        List<Reiziger> reizigersOpDatum =
                reizigerDAO.findByGbdatum(
                        "2000-01-01"
                );

        for (Reiziger r :
                reizigersOpDatum) {

            System.out.println(
                    r
            );

            System.out.println(
                    "Aantal kaarten: " +
                            r.getOvChipkaarten().size()
            );
        }


        System.out.println(
                "\n--- ReizigerDAO.findAll() ---"
        );

        List<Reiziger> alleReizigers =
                reizigerDAO.findAll();

        for (Reiziger r :
                alleReizigers) {

            System.out.println(
                    r
            );
        }


        System.out.println(
                "\n--- OVChipkaartDAO.findAll() ---"
        );

        List<OVChipkaart> alleKaarten =
                ovChipkaartDAO.findAll();

        for (OVChipkaart kaart :
                alleKaarten) {

            System.out.println(
                    kaart
            );
        }


        System.out.println(
                "\n--- Reiziger + OVChipkaart wijzigen ---"
        );

        reiziger.setAchternaam(
                "Gewijzigd"
        );

        kaart1.setSaldo(
                75.75
        );

        kaart1.setKlasse(
                1
        );

        kaart1.setGeldig_tot(
                LocalDate.of(
                        2030,
                        12,
                        31
                )
        );


        OVChipkaart kaart3 =
                new OVChipkaart(
                        TEST_KAART_ID_3,
                        LocalDate.of(
                                2031,
                                1,
                                1
                        ),
                        2,
                        100.00
                );

        reiziger.voegToeOVChipkaart(
                kaart3
        );


        boolean gewijzigd =
                reizigerDAO.update(
                        reiziger
                );

        System.out.println(
                "Reiziger bijgewerkt: " +
                        gewijzigd
        );


        System.out.println(
                "\n--- Update controleren ---"
        );

        Reiziger naUpdate =
                reizigerDAO.findById(
                        TEST_REIZIGER_ID
                );

        System.out.println(
                naUpdate
        );

        if (naUpdate != null) {

            System.out.println(
                    "Achternaam: " +
                            naUpdate.getAchternaam()
            );

            System.out.println(
                    "Aantal kaarten na update: " +
                            naUpdate
                                    .getOvChipkaarten()
                                    .size()
            );

            for (OVChipkaart kaart :
                    naUpdate.getOvChipkaarten()) {

                System.out.println(
                        kaart
                );
            }
        }


        System.out.println(
                "\n--- Kaart uit Reiziger verwijderen ---"
        );

        boolean uitObjectVerwijderd =
                reiziger.verwijderOVChipkaart(
                        kaart2
                );

        System.out.println(
                "Kaart uit Java-object verwijderd: " +
                        uitObjectVerwijderd
        );


        boolean opnieuwGewijzigd =
                reizigerDAO.update(
                        reiziger
                );

        System.out.println(
                "Reiziger opnieuw bijgewerkt: " +
                        opnieuwGewijzigd
        );


        System.out.println(
                "\n--- Kaarten na verwijderen relatie ---"
        );

        Reiziger naVerwijderenKaart =
                reizigerDAO.findById(
                        TEST_REIZIGER_ID
                );

        if (naVerwijderenKaart != null) {

            System.out.println(
                    "Aantal kaarten: " +
                            naVerwijderenKaart
                                    .getOvChipkaarten()
                                    .size()
            );

            for (OVChipkaart kaart :
                    naVerwijderenKaart
                            .getOvChipkaarten()) {

                System.out.println(
                        kaart
                );
            }
        }


        System.out.println(
                "\n--- Reiziger inclusief kaarten verwijderen ---"
        );

        boolean verwijderd =
                reizigerDAO.delete(
                        reiziger
                );

        System.out.println(
                "Reiziger verwijderd: " +
                        verwijderd
        );


        System.out.println(
                "\n--- Reiziger delete controleren ---"
        );

        Reiziger controleReiziger =
                reizigerDAO.findById(
                        TEST_REIZIGER_ID
                );

        System.out.println(
                "Reiziger na verwijderen: " +
                        controleReiziger
        );



        System.out.println(
                "\n--- Kaarten delete controleren ---"
        );

        List<OVChipkaart> kaartenNaDelete =
                ovChipkaartDAO.findByReiziger(
                        reiziger
                );

        System.out.println(
                "Aantal kaarten na verwijderen Reiziger: " +
                        kaartenNaDelete.size()
        );



        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "      EINDE VOLLEDIGE P4 TEST"
        );

        System.out.println(
                "=========================================="
        );
    }
}