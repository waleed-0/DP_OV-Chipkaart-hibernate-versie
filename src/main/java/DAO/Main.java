package main.java.DAO;

import main.java.POJO.OVChipkaart;
import main.java.POJO.Product;
import main.java.POJO.Reiziger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    private static final int TEST_PRODUCT_ID_1 =
            900001;

    private static final int TEST_PRODUCT_ID_2 =
            900002;

    private static final int TEST_PRODUCT_ID_3 =
            900003;


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

            /*
             * Alle DAO's worden hier één keer
             * aangemaakt.
             *
             * OVChipkaartDAOPsql maakt dus NIET
             * zelf steeds een ProductDAOPsql aan.
             */

            ReizigerDAOPsql reizigerDAO =
                    new ReizigerDAOPsql(
                            conn
                    );

            OVChipkaartDAOPsql ovChipkaartDAO =
                    new OVChipkaartDAOPsql(
                            conn
                    );

            ProductDAOPsql productDAO =
                    new ProductDAOPsql(
                            conn
                    );


            /*
             * DAO's aan elkaar koppelen.
             */

            reizigerDAO.setOVChipkaartDAO(
                    ovChipkaartDAO
            );

            ovChipkaartDAO.setReizigerDAO(
                    reizigerDAO
            );

            ovChipkaartDAO.setProductDAO(
                    productDAO
            );

            productDAO.setOVChipkaartDAO(
                    ovChipkaartDAO
            );


            /*
             * Oude testdata verwijderen.
             */

            verwijderOudeP5TestData(
                    conn
            );


            /*
             * Volledige P5-test uitvoeren.
             */

            testP5(
                    conn,
                    reizigerDAO,
                    ovChipkaartDAO,
                    productDAO
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


    /*
     * ==========================================================
     * OUDE P5-TESTDATA VERWIJDEREN
     * ==========================================================
     */

    private static void verwijderOudeP5TestData(
            Connection conn)
            throws SQLException {

        System.out.println(
                "\n--- Oude P5-testdata controleren ---"
        );


        /*
         * Eerst koppelingen verwijderen.
         *
         * Dit moet vóór Product en OVChipkaart,
         * vanwege de foreign keys.
         */

        String deleteKoppelingen =
                "DELETE FROM ov_chipkaart_product " +
                        "WHERE kaart_nummer IN (?, ?) " +
                        "OR product_nummer IN (?, ?, ?)";

        try (PreparedStatement statement =
                     conn.prepareStatement(
                             deleteKoppelingen
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
                    TEST_PRODUCT_ID_1
            );

            statement.setInt(
                    4,
                    TEST_PRODUCT_ID_2
            );

            statement.setInt(
                    5,
                    TEST_PRODUCT_ID_3
            );

            int verwijderd =
                    statement.executeUpdate();

            if (verwijderd > 0) {

                System.out.println(
                        verwijderd +
                                " oude koppeling(en) verwijderd."
                );
            }
        }


        /*
         * Testproducten verwijderen.
         */

        String deleteProducten =
                "DELETE FROM product " +
                        "WHERE product_nummer IN (?, ?, ?)";

        try (PreparedStatement statement =
                     conn.prepareStatement(
                             deleteProducten
                     )) {

            statement.setInt(
                    1,
                    TEST_PRODUCT_ID_1
            );

            statement.setInt(
                    2,
                    TEST_PRODUCT_ID_2
            );

            statement.setInt(
                    3,
                    TEST_PRODUCT_ID_3
            );

            int verwijderd =
                    statement.executeUpdate();

            if (verwijderd > 0) {

                System.out.println(
                        verwijderd +
                                " oud(e) testproduct(en) verwijderd."
                );
            }
        }


        /*
         * Testkaarten verwijderen.
         */

        String deleteKaarten =
                "DELETE FROM ov_chipkaart " +
                        "WHERE kaart_nummer IN (?, ?) " +
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
                    TEST_REIZIGER_ID
            );

            int verwijderd =
                    statement.executeUpdate();

            if (verwijderd > 0) {

                System.out.println(
                        verwijderd +
                                " oude testkaart(en) verwijderd."
                );
            }
        }


        /*
         * Eventueel oud testadres verwijderen.
         */

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


        /*
         * Testreiziger verwijderen.
         */

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

            int verwijderd =
                    statement.executeUpdate();

            if (verwijderd > 0) {

                System.out.println(
                        "Oude testreiziger verwijderd."
                );
            }
        }


        System.out.println(
                "Database is klaar voor de P5-test."
        );
    }


    /*
     * ==========================================================
     * VOLLEDIGE P5-TEST
     * ==========================================================
     */

    public static void testP5(
            Connection conn,
            ReizigerDAO reizigerDAO,
            OVChipkaartDAO ovChipkaartDAO,
            ProductDAO productDAO)
            throws SQLException {

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "        P5 - VOLLEDIGE DAO TEST"
        );

        System.out.println(
                "=========================================="
        );


        /*
         * ======================================================
         * 1. REIZIGER AANMAKEN
         * ======================================================
         */

        Reiziger reiziger =
                new Reiziger(
                        TEST_REIZIGER_ID,
                        "W.",
                        null,
                        "P5Test",
                        Date.valueOf(
                                "2000-01-01"
                        )
                );


        System.out.println(
                "\n--- Reiziger opslaan ---"
        );

        boolean reizigerOpgeslagen =
                reizigerDAO.save(
                        reiziger
                );

        System.out.println(
                "Reiziger opgeslagen: " +
                        reizigerOpgeslagen
        );


        /*
         * ======================================================
         * 2. OVCHIPKAARTEN AANMAKEN
         * ======================================================
         */

        OVChipkaart kaart1 =
                new OVChipkaart(
                        TEST_KAART_ID_1,
                        LocalDate.of(
                                2028,
                                12,
                                31
                        ),
                        2,
                        25.50,
                        reiziger
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
                        50.00,
                        reiziger
                );


        reiziger.voegToeOVChipkaart(
                kaart1
        );

        reiziger.voegToeOVChipkaart(
                kaart2
        );


        System.out.println(
                "\n--- OVChipkaarten opslaan ---"
        );

        boolean kaart1Opgeslagen =
                ovChipkaartDAO.save(
                        kaart1
                );

        boolean kaart2Opgeslagen =
                ovChipkaartDAO.save(
                        kaart2
                );

        System.out.println(
                "Kaart 1 opgeslagen: " +
                        kaart1Opgeslagen
        );

        System.out.println(
                "Kaart 2 opgeslagen: " +
                        kaart2Opgeslagen
        );


        /*
         * ======================================================
         * 3. PRODUCTEN AANMAKEN
         * ======================================================
         */

        Product product1 =
                new Product(
                        TEST_PRODUCT_ID_1,
                        "Dal Voordeel",
                        "Voordeelproduct voor reizen buiten de spits",
                        5.00
                );

        Product product2 =
                new Product(
                        TEST_PRODUCT_ID_2,
                        "Weekend Vrij",
                        "Vrij reizen in het weekend",
                        35.00
                );

        Product product3 =
                new Product(
                        TEST_PRODUCT_ID_3,
                        "Altijd Voordeel",
                        "Korting tijdens verschillende reisperiodes",
                        25.00
                );


        /*
         * ======================================================
         * 4. BIDIRECTIONELE RELATIES MAKEN
         * ======================================================
         */

        System.out.println(
                "\n--- Bidirectionele Product-OVChipkaart relatie ---"
        );


        boolean koppeling1 =
                kaart1.addProduct(
                        product1
                );

        boolean koppeling2 =
                kaart1.addProduct(
                        product2
                );

        boolean koppeling3 =
                kaart2.addProduct(
                        product1
                );


        System.out.println(
                "Product 1 toegevoegd aan kaart 1: " +
                        koppeling1
        );

        System.out.println(
                "Product 2 toegevoegd aan kaart 1: " +
                        koppeling2
        );

        System.out.println(
                "Product 1 toegevoegd aan kaart 2: " +
                        koppeling3
        );


        System.out.println(
                "Aantal producten kaart 1: " +
                        kaart1
                                .getProducten()
                                .size()
        );

        System.out.println(
                "Aantal producten kaart 2: " +
                        kaart2
                                .getProducten()
                                .size()
        );


        /*
         * Terugkoppeling controleren.
         */

        System.out.println(
                "Aantal kaarten bij product 1: " +
                        product1
                                .getOvChipkaarten()
                                .size()
        );

        System.out.println(
                "Aantal kaarten bij product 2: " +
                        product2
                                .getOvChipkaarten()
                                .size()
        );


        /*
         * ======================================================
         * 5. PRODUCTEN + RELATIES OPSLAAN
         * ======================================================
         */

        System.out.println(
                "\n--- Producten + koppelingen opslaan ---"
        );


        boolean product1Opgeslagen =
                productDAO.save(
                        product1
                );

        boolean product2Opgeslagen =
                productDAO.save(
                        product2
                );

        boolean product3Opgeslagen =
                productDAO.save(
                        product3
                );


        System.out.println(
                "Product 1 opgeslagen: " +
                        product1Opgeslagen
        );

        System.out.println(
                "Product 2 opgeslagen: " +
                        product2Opgeslagen
        );

        System.out.println(
                "Product 3 opgeslagen: " +
                        product3Opgeslagen
        );


        /*
         * ======================================================
         * 6. TUSSENTABEL DIRECT CONTROLEREN
         * ======================================================
         */

        System.out.println(
                "\n--- Tussentabel controleren ---"
        );


        int aantalKoppelingen =
                telKoppelingen(
                        conn
                );


        System.out.println(
                "Aantal P5-koppelingen in database: " +
                        aantalKoppelingen
        );


        /*
         * ======================================================
         * 7. PRODUCTDAO.FINDBYID()
         * ======================================================
         */

        System.out.println(
                "\n--- ProductDAO.findById() ---"
        );


        Product gevondenProduct =
                productDAO.findById(
                        TEST_PRODUCT_ID_1
                );


        System.out.println(
                gevondenProduct
        );


        if (gevondenProduct != null) {

            System.out.println(
                    "Aantal OVChipkaarten bij product 1: " +
                            gevondenProduct
                                    .getOvChipkaarten()
                                    .size()
            );

            for (OVChipkaart kaart :
                    gevondenProduct.getOvChipkaarten()) {

                System.out.println(
                        "Gekoppelde kaart: " +
                                kaart
                );
            }
        }


        /*
         * ======================================================
         * 8. PRODUCTDAO.FINDBYOVCHIPKAART()
         * ======================================================
         */

        System.out.println(
                "\n--- ProductDAO.findByOVChipkaart() ---"
        );


        List<Product> productenVanKaart1 =
                productDAO.findByOVChipkaart(
                        kaart1
                );


        System.out.println(
                "Aantal producten van kaart 1: " +
                        productenVanKaart1.size()
        );


        for (Product product :
                productenVanKaart1) {

            System.out.println(
                    product
            );

            System.out.println(
                    "Aantal teruggekoppelde kaarten: " +
                            product
                                    .getOvChipkaarten()
                                    .size()
            );
        }


        /*
         * ======================================================
         * 9. PRODUCTDAO.FINDALL()
         * ======================================================
         */

        System.out.println(
                "\n--- ProductDAO.findAll() ---"
        );


        List<Product> alleProducten =
                productDAO.findAll();


        for (Product product :
                alleProducten) {

            System.out.println(
                    product
            );
        }


        /*
         * ======================================================
         * 10. OVCHIPKAARTDAO.FINDALL()
         * ======================================================
         */

        System.out.println(
                "\n--- OVChipkaartDAO.findAll() inclusief Producten ---"
        );


        List<OVChipkaart> alleKaarten =
                ovChipkaartDAO.findAll();


        for (OVChipkaart kaart :
                alleKaarten) {

            if (kaart.getKaart_nummer() ==
                    TEST_KAART_ID_1 ||
                    kaart.getKaart_nummer() ==
                            TEST_KAART_ID_2) {

                System.out.println(
                        kaart
                );

                System.out.println(
                        "Aantal producten: " +
                                kaart
                                        .getProducten()
                                        .size()
                );

                if (kaart.getReiziger() != null) {

                    System.out.println(
                            "Reiziger van kaart: #" +
                                    kaart
                                            .getReiziger()
                                            .getId()
                    );
                }
            }
        }


        /*
         * ======================================================
         * 11. PRODUCT UPDATE
         * ======================================================
         *
         * Product 1:
         *
         * Eerst:
         * kaart1 + kaart2
         *
         * Daarna:
         * kaart1 verwijderd
         * kaart2 blijft
         *
         * Zo testen we of update() ook daadwerkelijk
         * de tussentabel bijwerkt.
         * ======================================================
         */

        System.out.println(
                "\n--- Product + koppelingen wijzigen ---"
        );


        product1.setNaam(
                "Dal Voordeel Gewijzigd"
        );

        product1.setBeschrijving(
                "Gewijzigde beschrijving"
        );

        product1.setPrijs(
                7.50
        );


        boolean kaart1Verwijderd =
                product1.removeOVChipkaart(
                        kaart1
                );


        System.out.println(
                "Kaart 1 verwijderd van product 1: " +
                        kaart1Verwijderd
        );


        boolean productGewijzigd =
                productDAO.update(
                        product1
                );


        System.out.println(
                "Product 1 bijgewerkt: " +
                        productGewijzigd
        );

        System.out.println(
                "\n--- Product update controleren ---"
        );


        Product productNaUpdate =
                productDAO.findById(
                        TEST_PRODUCT_ID_1
                );


        System.out.println(
                productNaUpdate
        );


        if (productNaUpdate != null) {

            System.out.println(
                    "Naam: " +
                            productNaUpdate.getNaam()
            );

            System.out.println(
                    "Prijs: " +
                            productNaUpdate.getPrijs()
            );

            System.out.println(
                    "Aantal kaarten na update: " +
                            productNaUpdate
                                    .getOvChipkaarten()
                                    .size()
            );
        }


        System.out.println(
                "\n--- Nieuwe koppeling via Product.update() ---"
        );


        product3.addOVChipkaart(
                kaart1
        );


        boolean product3Gewijzigd =
                productDAO.update(
                        product3
                );


        System.out.println(
                "Product 3 bijgewerkt: " +
                        product3Gewijzigd
        );


        Product product3Controle =
                productDAO.findById(
                        TEST_PRODUCT_ID_3
                );


        System.out.println(
                product3Controle
        );


        if (product3Controle != null) {

            System.out.println(
                    "Aantal kaarten bij product 3: " +
                            product3Controle
                                    .getOvChipkaarten()
                                    .size()
            );
        }

        System.out.println(
                "\n--- OVChipkaart + Product relatie wijzigen ---"
        );


        OVChipkaart kaart1UitDatabase =
                zoekTestKaart(
                        ovChipkaartDAO,
                        TEST_KAART_ID_1
                );


        if (kaart1UitDatabase != null) {

            System.out.println(
                    "Kaart vóór wijziging: " +
                            kaart1UitDatabase
            );


            List<Product> productenKaart1 =
                    kaart1UitDatabase.getProducten();


            if (!productenKaart1.isEmpty()) {

                Product teVerwijderen =
                        productenKaart1.get(0);


                boolean verwijderdUitObject =
                        kaart1UitDatabase.removeProduct(
                                teVerwijderen
                        );


                System.out.println(
                        "Product uit kaart-object verwijderd: " +
                                verwijderdUitObject
                );


                boolean kaartGewijzigd =
                        ovChipkaartDAO.update(
                                kaart1UitDatabase
                        );


                System.out.println(
                        "OVChipkaart bijgewerkt: " +
                                kaartGewijzigd
                );
            }
        }


        System.out.println(
                "\n--- OVChipkaart update controleren ---"
        );


        OVChipkaart kaart1NaUpdate =
                zoekTestKaart(
                        ovChipkaartDAO,
                        TEST_KAART_ID_1
                );


        if (kaart1NaUpdate != null) {

            System.out.println(
                    kaart1NaUpdate
            );

            System.out.println(
                    "Aantal producten na update: " +
                            kaart1NaUpdate
                                    .getProducten()
                                    .size()
            );
        }


        System.out.println(
                "\n--- Product verwijderen ---"
        );


        Product product2UitDatabase =
                productDAO.findById(
                        TEST_PRODUCT_ID_2
                );


        if (product2UitDatabase != null) {

            boolean product2Verwijderd =
                    productDAO.delete(
                            product2UitDatabase
                    );


            System.out.println(
                    "Product 2 verwijderd: " +
                            product2Verwijderd
            );
        }


        Product product2Controle =
                productDAO.findById(
                        TEST_PRODUCT_ID_2
                );


        System.out.println(
                "Product 2 na verwijderen: " +
                        product2Controle
        );


        OVChipkaart kaartControle =
                zoekTestKaart(
                        ovChipkaartDAO,
                        TEST_KAART_ID_1
                );


        System.out.println(
                "Kaart 1 bestaat na Product-delete nog: " +
                        (kaartControle != null)
        );



        System.out.println(
                "\n--- OVChipkaart verwijderen zonder Product te verwijderen ---"
        );


        OVChipkaart kaart2UitDatabase =
                zoekTestKaart(
                        ovChipkaartDAO,
                        TEST_KAART_ID_2
                );


        if (kaart2UitDatabase != null) {

            boolean kaart2Verwijderd =
                    ovChipkaartDAO.delete(
                            kaart2UitDatabase
                    );


            System.out.println(
                    "Kaart 2 verwijderd: " +
                            kaart2Verwijderd
            );
        }

        Product product1NaKaartDelete =
                productDAO.findById(
                        TEST_PRODUCT_ID_1
                );


        System.out.println(
                "Product 1 bestaat na verwijderen kaart 2 nog: " +
                        (product1NaKaartDelete != null)
        );


        if (product1NaKaartDelete != null) {

            System.out.println(
                    product1NaKaartDelete
            );
        }


        System.out.println(
                "\n--- toString() controle ---"
        );


        if (product1NaKaartDelete != null) {

            System.out.println(
                    "Product.toString():"
            );

            System.out.println(
                    product1NaKaartDelete
            );
        }


        if (kaart1NaUpdate != null) {

            System.out.println(
                    "OVChipkaart.toString():"
            );

            System.out.println(
                    kaart1NaUpdate
            );
        }

        System.out.println(
                "\n--- Eindcontrole tussentabel ---"
        );


        toonTestKoppelingen(
                conn
        );


        System.out.println(
                "\n--- P5-testdata opruimen ---"
        );


        verwijderOudeP5TestData(
                conn
        );


        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "      EINDE VOLLEDIGE P5 TEST"
        );

        System.out.println(
                "=========================================="
        );
    }


    private static int telKoppelingen(
            Connection conn)
            throws SQLException {

        String query =
                "SELECT COUNT(*) " +
                        "FROM ov_chipkaart_product " +
                        "WHERE kaart_nummer IN (?, ?) " +
                        "OR product_nummer IN (?, ?, ?)";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

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
                    TEST_PRODUCT_ID_1
            );

            statement.setInt(
                    4,
                    TEST_PRODUCT_ID_2
            );

            statement.setInt(
                    5,
                    TEST_PRODUCT_ID_3
            );


            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return resultSet.getInt(
                            1
                    );
                }
            }
        }

        return 0;
    }


    private static void toonTestKoppelingen(
            Connection conn)
            throws SQLException {

        String query =
                "SELECT kaart_nummer, product_nummer " +
                        "FROM ov_chipkaart_product " +
                        "WHERE kaart_nummer IN (?, ?) " +
                        "OR product_nummer IN (?, ?, ?) " +
                        "ORDER BY kaart_nummer, product_nummer";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

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
                    TEST_PRODUCT_ID_1
            );

            statement.setInt(
                    4,
                    TEST_PRODUCT_ID_2
            );

            statement.setInt(
                    5,
                    TEST_PRODUCT_ID_3
            );


            try (ResultSet resultSet =
                         statement.executeQuery()) {

                int aantal =
                        0;

                while (resultSet.next()) {

                    aantal++;

                    System.out.println(
                            "kaart_nummer = " +
                                    resultSet.getInt(
                                            "kaart_nummer"
                                    ) +
                                    ", product_nummer = " +
                                    resultSet.getInt(
                                            "product_nummer"
                                    )
                    );
                }

                System.out.println(
                        "Totaal aantal testkoppelingen: " +
                                aantal
                );
            }
        }
    }


    private static OVChipkaart zoekTestKaart(
            OVChipkaartDAO ovChipkaartDAO,
            int kaartNummer)
            throws SQLException {

        List<OVChipkaart> kaarten =
                ovChipkaartDAO.findAll();

        for (OVChipkaart kaart :
                kaarten) {

            if (kaart.getKaart_nummer() ==
                    kaartNummer) {

                return kaart;
            }
        }

        return null;
    }
}