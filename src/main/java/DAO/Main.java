package main.java.DAO;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import main.java.POJO.OVChipkaart;
import main.java.POJO.Product;
import main.java.POJO.Reiziger;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class Main {

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

        EntityManagerFactory emf =
                null;

        try {

            emf =
                    Persistence.createEntityManagerFactory(
                            "ovchip"
                    );

            ReizigerDAOHibernate reizigerDAO =
                    new ReizigerDAOHibernate(
                            emf
                    );

            OVChipkaartDAOHibernate ovChipkaartDAO =
                    new OVChipkaartDAOHibernate(
                            emf
                    );

            ProductDAOHibernate productDAO =
                    new ProductDAOHibernate(
                            emf
                    );

            testP5H(
                    reizigerDAO,
                    ovChipkaartDAO,
                    productDAO
            );

        } catch (Exception e) {

            System.out.println(
                    "Er is een fout opgetreden tijdens de P5H-test."
            );

            e.printStackTrace();

        } finally {

            if (emf != null &&
                    emf.isOpen()) {

                emf.close();

                System.out.println(
                        "\nEntityManagerFactory gesloten."
                );
            }
        }
    }


    public static void testP5H(
            ReizigerDAO reizigerDAO,
            OVChipkaartDAO ovChipkaartDAO,
            ProductDAO productDAO)
            throws Exception {

        ruimOudeTestdataOp(
                reizigerDAO,
                ovChipkaartDAO,
                productDAO
        );

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "        P5H - VOLLEDIGE HIBERNATE TEST"
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
                        Date.valueOf("2000-01-01")
                );

        System.out.println(
                "\n--- Reiziger opslaan ---"
        );

        System.out.println(
                "Reiziger opgeslagen: " +
                        reizigerDAO.save(
                                reiziger
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

        System.out.println(
                "Kaart 1 opgeslagen: " +
                        ovChipkaartDAO.save(
                                kaart1
                        )
        );

        System.out.println(
                "Kaart 2 opgeslagen: " +
                        ovChipkaartDAO.save(
                                kaart2
                        )
        );

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


        System.out.println(
                "\n--- Bidirectionele Product-OVChipkaart relatie ---"
        );

        System.out.println(
                "Product 1 toegevoegd aan kaart 1: " +
                        kaart1.addProduct(
                                product1
                        )
        );

        System.out.println(
                "Product 2 toegevoegd aan kaart 1: " +
                        kaart1.addProduct(
                                product2
                        )
        );

        System.out.println(
                "Product 1 toegevoegd aan kaart 2: " +
                        kaart2.addProduct(
                                product1
                        )
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


        System.out.println(
                "\n--- Producten + koppelingen opslaan ---"
        );

        System.out.println(
                "Product 1 opgeslagen: " +
                        productDAO.save(
                                product1
                        )
        );

        System.out.println(
                "Product 2 opgeslagen: " +
                        productDAO.save(
                                product2
                        )
        );

        System.out.println(
                "Product 3 opgeslagen: " +
                        productDAO.save(
                                product3
                        )
        );


        System.out.println(
                "\n--- ProductDAO.findById() ---"
        );

        Product productUitDatabase =
                productDAO.findById(
                        TEST_PRODUCT_ID_1
                );

        if (productUitDatabase != null) {

            System.out.println(
                    "Product: #" +
                            productUitDatabase.getProduct_nummer() +
                            " " +
                            productUitDatabase.getNaam()
            );

            System.out.println(
                    "Beschrijving: " +
                            productUitDatabase.getBeschrijving()
            );

            System.out.println(
                    "Prijs: " +
                            productUitDatabase.getPrijs()
            );

            System.out.println(
                    "Aantal OVChipkaarten bij product 1: " +
                            productUitDatabase
                                    .getOvChipkaarten()
                                    .size()
            );

            for (OVChipkaart kaart :
                    productUitDatabase.getOvChipkaarten()) {

                System.out.println(
                        "Gekoppelde kaart: #" +
                                kaart.getKaart_nummer() +
                                ", geldig tot " +
                                kaart.getGeldig_tot() +
                                ", klasse " +
                                kaart.getKlasse() +
                                ", saldo " +
                                kaart.getSaldo()
                );

                if (kaart.getReiziger() != null) {

                    System.out.println(
                            "Reiziger van gekoppelde kaart: #" +
                                    kaart
                                            .getReiziger()
                                            .getId()
                    );
                }
            }

        } else {

            System.out.println(
                    "Product 1 niet gevonden."
            );
        }


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
                    "Product #" +
                            product.getProduct_nummer() +
                            " " +
                            product.getNaam()
            );

            System.out.println(
                    "Aantal teruggekoppelde kaarten: " +
                            product
                                    .getOvChipkaarten()
                                    .size()
            );

            for (OVChipkaart kaart :
                    product.getOvChipkaarten()) {

                System.out.println(
                        "Teruggekoppelde kaart: #" +
                                kaart.getKaart_nummer()
                );
            }
        }

        System.out.println(
                "\n--- ProductDAO.findAll() ---"
        );

        List<Product> alleProducten =
                productDAO.findAll();

        for (Product product :
                alleProducten) {

            System.out.println(
                    "Product #" +
                            product.getProduct_nummer() +
                            ", naam " +
                            product.getNaam() +
                            ", beschrijving " +
                            product.getBeschrijving() +
                            ", prijs " +
                            product.getPrijs()
            );

            System.out.println(
                    "Aantal gekoppelde kaarten: " +
                            product
                                    .getOvChipkaarten()
                                    .size()
            );

            for (OVChipkaart kaart :
                    product.getOvChipkaarten()) {

                System.out.println(
                        "  OVChipkaart #" +
                                kaart.getKaart_nummer()
                );
            }
        }


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

                printOVChipkaart(
                        kaart
                );
            }
        }


        System.out.println(
                "\n--- Product + koppelingen wijzigen ---"
        );

        Product product1VoorUpdate =
                productDAO.findById(
                        TEST_PRODUCT_ID_1
                );

        OVChipkaart kaart1VanProduct =
                zoekKaart(
                        product1VoorUpdate,
                        TEST_KAART_ID_1
                );

        boolean kaartVerwijderd =
                product1VoorUpdate != null &&
                        kaart1VanProduct != null &&
                        product1VoorUpdate
                                .removeOVChipkaart(
                                        kaart1VanProduct
                                );

        System.out.println(
                "Kaart 1 verwijderd van product 1: " +
                        kaartVerwijderd
        );

        if (product1VoorUpdate != null) {

            product1VoorUpdate.setNaam(
                    "Dal Voordeel Gewijzigd"
            );

            product1VoorUpdate.setBeschrijving(
                    "Gewijzigde beschrijving"
            );

            product1VoorUpdate.setPrijs(
                    7.50
            );

            System.out.println(
                    "Product 1 bijgewerkt: " +
                            productDAO.update(
                                    product1VoorUpdate
                            )
            );
        }


        System.out.println(
                "\n--- Product update controleren ---"
        );

        Product product1NaUpdate =
                productDAO.findById(
                        TEST_PRODUCT_ID_1
                );

        if (product1NaUpdate != null) {

            System.out.println(
                    "Product #" +
                            product1NaUpdate.getProduct_nummer()
            );

            System.out.println(
                    "Naam: " +
                            product1NaUpdate.getNaam()
            );

            System.out.println(
                    "Beschrijving: " +
                            product1NaUpdate.getBeschrijving()
            );

            System.out.println(
                    "Prijs: " +
                            product1NaUpdate.getPrijs()
            );

            System.out.println(
                    "Aantal kaarten na update: " +
                            product1NaUpdate
                                    .getOvChipkaarten()
                                    .size()
            );

            for (OVChipkaart kaart :
                    product1NaUpdate.getOvChipkaarten()) {

                System.out.println(
                        "Gekoppelde kaart na update: #" +
                                kaart.getKaart_nummer()
                );
            }
        }


        System.out.println(
                "\n--- Nieuwe koppeling via Product.update() ---"
        );

        Product product3VoorUpdate =
                productDAO.findById(
                        TEST_PRODUCT_ID_3
                );

        OVChipkaart kaart1UitDatabase =
                zoekKaartOpNummer(
                        ovChipkaartDAO.findAll(),
                        TEST_KAART_ID_1
                );

        if (product3VoorUpdate != null &&
                kaart1UitDatabase != null) {

            System.out.println(
                    "Nieuwe koppeling toegevoegd: " +
                            product3VoorUpdate
                                    .addOVChipkaart(
                                            kaart1UitDatabase
                                    )
            );

            System.out.println(
                    "Product 3 bijgewerkt: " +
                            productDAO.update(
                                    product3VoorUpdate
                            )
            );
        }

        Product product3NaUpdate =
                productDAO.findById(
                        TEST_PRODUCT_ID_3
                );

        if (product3NaUpdate != null) {

            System.out.println(
                    "Product 3: #" +
                            product3NaUpdate
                                    .getProduct_nummer() +
                            " " +
                            product3NaUpdate
                                    .getNaam()
            );

            System.out.println(
                    "Aantal kaarten bij product 3: " +
                            product3NaUpdate
                                    .getOvChipkaarten()
                                    .size()
            );
        }



        System.out.println(
                "\n--- OVChipkaart + Product relatie wijzigen ---"
        );

        OVChipkaart kaart1VoorUpdate =
                zoekKaartOpNummer(
                        ovChipkaartDAO.findAll(),
                        TEST_KAART_ID_1
                );

        if (kaart1VoorUpdate != null) {

            System.out.println(
                    "Kaart vóór wijziging:"
            );

            printOVChipkaart(
                    kaart1VoorUpdate
            );
        }

        Product product2VanKaart =
                zoekProduct(
                        kaart1VoorUpdate,
                        TEST_PRODUCT_ID_2
                );

        boolean productVerwijderd =
                product2VanKaart != null &&
                        kaart1VoorUpdate != null &&
                        kaart1VoorUpdate
                                .removeProduct(
                                        product2VanKaart
                                );

        System.out.println(
                "Product uit kaart-object verwijderd: " +
                        productVerwijderd
        );

        if (kaart1VoorUpdate != null) {

            System.out.println(
                    "OVChipkaart bijgewerkt: " +
                            ovChipkaartDAO.update(
                                    kaart1VoorUpdate
                            )
            );
        }

        System.out.println(
                "\n--- OVChipkaart update controleren ---"
        );

        OVChipkaart kaart1NaUpdate =
                zoekKaartOpNummer(
                        ovChipkaartDAO.findAll(),
                        TEST_KAART_ID_1
                );

        if (kaart1NaUpdate != null) {

            printOVChipkaart(
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

        Product product2VoorDelete =
                productDAO.findById(
                        TEST_PRODUCT_ID_2
                );

        if (product2VoorDelete != null) {

            System.out.println(
                    "Product 2 verwijderd: " +
                            productDAO.delete(
                                    product2VoorDelete
                            )
            );

        } else {

            System.out.println(
                    "Product 2 bestond niet meer."
            );
        }

        Product product2NaDelete =
                productDAO.findById(
                        TEST_PRODUCT_ID_2
                );

        System.out.println(
                "Product 2 na verwijderen: " +
                        product2NaDelete
        );

        OVChipkaart kaart1Controle =
                zoekKaartOpNummer(
                        ovChipkaartDAO.findAll(),
                        TEST_KAART_ID_1
                );

        System.out.println(
                "Kaart 1 bestaat na Product-delete nog: " +
                        (kaart1Controle != null)
        );


        System.out.println(
                "\n--- OVChipkaart verwijderen zonder Product te verwijderen ---"
        );

        OVChipkaart kaart2VoorDelete =
                zoekKaartOpNummer(
                        ovChipkaartDAO.findAll(),
                        TEST_KAART_ID_2
                );

        if (kaart2VoorDelete != null) {

            System.out.println(
                    "Kaart 2 verwijderd: " +
                            ovChipkaartDAO.delete(
                                    kaart2VoorDelete
                            )
            );

        } else {

            System.out.println(
                    "Kaart 2 bestond niet meer."
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
                    "Product 1: #" +
                            product1NaKaartDelete
                                    .getProduct_nummer() +
                            " " +
                            product1NaKaartDelete
                                    .getNaam()
            );

            System.out.println(
                    "Aantal kaarten bij product 1: " +
                            product1NaKaartDelete
                                    .getOvChipkaarten()
                                    .size()
            );
        }


        System.out.println(
                "\n--- toString() controle ---"
        );

        Product productVoorToString =
                productDAO.findById(
                        TEST_PRODUCT_ID_3
                );

        if (productVoorToString != null) {

            System.out.println(
                    "Product.toString():"
            );

            System.out.println(
                    productVoorToString
            );
        }

        OVChipkaart kaartVoorToString =
                zoekKaartOpNummer(
                        ovChipkaartDAO.findAll(),
                        TEST_KAART_ID_1
                );

        if (kaartVoorToString != null) {

            System.out.println(
                    "OVChipkaart.toString():"
            );

            System.out.println(
                    kaartVoorToString
            );
        }



        System.out.println(
                "\n--- P5H-testdata opruimen ---"
        );

        ruimOudeTestdataOp(
                reizigerDAO,
                ovChipkaartDAO,
                productDAO
        );

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "      EINDE VOLLEDIGE P5H TEST"
        );

        System.out.println(
                "=========================================="
        );
    }


    private static void printOVChipkaart(
            OVChipkaart kaart) {

        if (kaart == null) {

            System.out.println(
                    "OVChipkaart is null."
            );

            return;
        }

        System.out.println(
                "OVChipkaart #" +
                        kaart.getKaart_nummer()
        );

        System.out.println(
                "Geldig tot: " +
                        kaart.getGeldig_tot()
        );

        System.out.println(
                "Klasse: " +
                        kaart.getKlasse()
        );

        System.out.println(
                "Saldo: " +
                        kaart.getSaldo()
        );

        if (kaart.getReiziger() != null) {

            System.out.println(
                    "Reiziger: #" +
                            kaart
                                    .getReiziger()
                                    .getId()
            );
        }

        System.out.println(
                "Aantal producten: " +
                        kaart
                                .getProducten()
                                .size()
        );

        for (Product product :
                kaart.getProducten()) {

            System.out.println(
                    "  Product #" +
                            product.getProduct_nummer() +
                            " " +
                            product.getNaam()
            );
        }
    }


    private static OVChipkaart zoekKaart(
            Product product,
            int kaartNummer) {

        if (product == null) {
            return null;
        }

        for (OVChipkaart kaart :
                product.getOvChipkaarten()) {

            if (kaart.getKaart_nummer() ==
                    kaartNummer) {

                return kaart;
            }
        }

        return null;
    }


    private static Product zoekProduct(
            OVChipkaart ovChipkaart,
            int productNummer) {

        if (ovChipkaart == null) {
            return null;
        }

        for (Product product :
                ovChipkaart.getProducten()) {

            if (product.getProduct_nummer() ==
                    productNummer) {

                return product;
            }
        }

        return null;
    }


    private static OVChipkaart zoekKaartOpNummer(
            List<OVChipkaart> kaarten,
            int kaartNummer) {

        if (kaarten == null) {
            return null;
        }

        for (OVChipkaart kaart :
                kaarten) {

            if (kaart.getKaart_nummer() ==
                    kaartNummer) {

                return kaart;
            }
        }

        return null;
    }


    private static void ruimOudeTestdataOp(
            ReizigerDAO reizigerDAO,
            OVChipkaartDAO ovChipkaartDAO,
            ProductDAO productDAO)
            throws Exception {

        System.out.println(
                "\n--- Oude P5H-testdata controleren ---"
        );


        int[] productNummers = {
                TEST_PRODUCT_ID_1,
                TEST_PRODUCT_ID_2,
                TEST_PRODUCT_ID_3
        };

        for (int productNummer :
                productNummers) {

            Product product =
                    productDAO.findById(
                            productNummer
                    );

            if (product != null) {

                productDAO.delete(
                        product
                );
            }
        }


        List<OVChipkaart> kaarten =
                ovChipkaartDAO.findAll();

        for (OVChipkaart kaart :
                kaarten) {

            if (kaart.getKaart_nummer() ==
                    TEST_KAART_ID_1 ||
                    kaart.getKaart_nummer() ==
                            TEST_KAART_ID_2) {

                ovChipkaartDAO.delete(
                        kaart
                );
            }
        }



        Reiziger reiziger =
                reizigerDAO.findById(
                        TEST_REIZIGER_ID
                );

        if (reiziger != null) {

            reizigerDAO.delete(
                    reiziger
            );
        }

        System.out.println(
                "Database is klaar voor de P5H-test."
        );
    }
}