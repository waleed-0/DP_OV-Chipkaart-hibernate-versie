package main.java.DAO;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import main.java.POJO.Adres;
import main.java.POJO.OVChipkaart;
import main.java.POJO.Product;
import main.java.POJO.Reiziger;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class Main {

    private static final int REIZIGER_ID_TEST = 9100;
    private static final int REIZIGER_ID_ADRES_TEST = 9101;
    private static final int REIZIGER_ID_KAART_TEST = 9102;
    private static final int REIZIGER_ID_PRODUCT_TEST = 9103;

    private static final int ADRES_ID_TEST = 910001;

    private static final int KAART_NUMMER_TEST = 910002;
    private static final int KAART_NUMMER_PRODUCT_TEST = 910003;

    private static final int PRODUCT_NUMMER_TEST = 910004;

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory(
                        "ovchip"
                );

        ReizigerDAO reizigerDAO =
                new ReizigerDAOHibernate(
                        emf
                );

        AdresDAO adresDAO =
                new AdresDAOHibernate(
                        emf
                );

        OVChipkaartDAO ovChipkaartDAO =
                new OVChipkaartDAOHibernate(
                        emf
                );

        ProductDAO productDAO =
                new ProductDAOHibernate(
                        emf
                );

        try {

            testReizigerDAO(
                    reizigerDAO
            );

            testAdresDAO(
                    reizigerDAO,
                    adresDAO
            );

            testOVChipkaartDAO(
                    reizigerDAO,
                    ovChipkaartDAO
            );

            testProductDAO(
                    reizigerDAO,
                    ovChipkaartDAO,
                    productDAO
            );

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (emf.isOpen()) {
                emf.close();
            }
        }
    }


    private static void testReizigerDAO(
            ReizigerDAO reizigerDAO) {

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "REIZIGER DAO HIBERNATE"
        );

        System.out.println(
                "=========================================="
        );

        Reiziger reiziger =
                new Reiziger(
                        REIZIGER_ID_TEST,
                        "W.",
                        null,
                        "Test",
                        Date.valueOf(
                                "2000-01-01"
                        )
                );


        System.out.println(
                "\n--- SAVE ---"
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
                "\n--- FIND BY ID ---"
        );

        Reiziger gevondenReiziger =
                reizigerDAO.findById(
                        REIZIGER_ID_TEST
                );

        if (gevondenReiziger != null) {

            System.out.println(
                    "Gevonden: #" +
                            gevondenReiziger.getId() +
                            " " +
                            gevondenReiziger.getVoorletters() +
                            " " +
                            gevondenReiziger.getAchternaam() +
                            ", geb. " +
                            gevondenReiziger.getGeboortedatum()
            );

        } else {

            System.out.println(
                    "Reiziger niet gevonden."
            );
        }


        System.out.println(
                "\n--- FIND BY GEBOORTEDATUM ---"
        );

        List<Reiziger> reizigers =
                reizigerDAO.findByGbdatum(
                        "2000-01-01"
                );

        for (Reiziger r : reizigers) {

            System.out.println(
                    "Gevonden: #" +
                            r.getId() +
                            " " +
                            r.getVoorletters() +
                            " " +
                            r.getAchternaam() +
                            ", geb. " +
                            r.getGeboortedatum()
            );
        }


        System.out.println(
                "\n--- UPDATE ---"
        );

        reiziger.setAchternaam(
                "Test Gewijzigd"
        );

        boolean gewijzigd =
                reizigerDAO.update(
                        reiziger
                );

        System.out.println(
                "Reiziger gewijzigd: " +
                        gewijzigd
        );


        System.out.println(
                "\n--- CONTROLE NA UPDATE ---"
        );

        Reiziger reizigerNaUpdate =
                reizigerDAO.findById(
                        REIZIGER_ID_TEST
                );

        if (reizigerNaUpdate != null) {

            System.out.println(
                    "Achternaam na update: " +
                            reizigerNaUpdate.getAchternaam()
            );
        }


        System.out.println(
                "\n--- FIND ALL ---"
        );

        List<Reiziger> alleReizigers =
                reizigerDAO.findAll();

        for (Reiziger r : alleReizigers) {

            System.out.println(
                    "#" +
                            r.getId() +
                            " " +
                            r.getVoorletters() +
                            " " +
                            r.getAchternaam()
            );
        }


        System.out.println(
                "\n--- DELETE ---"
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
                "\n--- CONTROLE NA DELETE ---"
        );

        System.out.println(
                "Reiziger gevonden na delete: " +
                        (reizigerDAO.findById(
                                REIZIGER_ID_TEST
                        ) != null)
        );
    }


    private static void testAdresDAO(
            ReizigerDAO reizigerDAO,
            AdresDAO adresDAO) {

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "ADRES DAO HIBERNATE"
        );

        System.out.println(
                "=========================================="
        );

        Reiziger reiziger =
                new Reiziger(
                        REIZIGER_ID_ADRES_TEST,
                        "A.",
                        null,
                        "AdresTest",
                        Date.valueOf(
                                "2001-02-03"
                        )
                );


        System.out.println(
                "\n--- SAVE REIZIGER ---"
        );

        boolean reizigerOpgeslagen =
                reizigerDAO.save(
                        reiziger
                );

        System.out.println(
                "Reiziger opgeslagen: " +
                        reizigerOpgeslagen
        );


        Adres adres =
                new Adres(
                        ADRES_ID_TEST,
                        "1234AB",
                        "10",
                        "Teststraat",
                        "Utrecht",
                        reiziger
                );


        System.out.println(
                "\n--- SAVE ADRES ---"
        );

        boolean opgeslagen =
                adresDAO.save(
                        adres
                );

        System.out.println(
                "Adres opgeslagen: " +
                        opgeslagen
        );


        System.out.println(
                "\n--- FIND BY ID ---"
        );

        Adres gevondenAdres =
                adresDAO.findById(
                        ADRES_ID_TEST
                );

        if (gevondenAdres != null) {

            System.out.println(
                    "Gevonden adres: #" +
                            gevondenAdres.getId() +
                            ", " +
                            gevondenAdres.getStraat() +
                            " " +
                            gevondenAdres.getHuisnummer() +
                            ", " +
                            gevondenAdres.getPostcode() +
                            " " +
                            gevondenAdres.getWoonplaats()
            );
        }


        System.out.println(
                "\n--- FIND BY REIZIGER ---"
        );

        Adres adresVanReiziger =
                adresDAO.findByReiziger(
                        reiziger
                );

        if (adresVanReiziger != null) {

            System.out.println(
                    "Adres van reiziger #" +
                            reiziger.getId() +
                            ": " +
                            adresVanReiziger.getStraat() +
                            " " +
                            adresVanReiziger.getHuisnummer() +
                            ", " +
                            adresVanReiziger.getWoonplaats()
            );
        }


        System.out.println(
                "\n--- UPDATE ---"
        );

        adres.setWoonplaats(
                "Amsterdam"
        );

        boolean gewijzigd =
                adresDAO.update(
                        adres
                );

        System.out.println(
                "Adres gewijzigd: " +
                        gewijzigd
        );


        System.out.println(
                "\n--- CONTROLE NA UPDATE ---"
        );

        Adres adresNaUpdate =
                adresDAO.findById(
                        ADRES_ID_TEST
                );

        if (adresNaUpdate != null) {

            System.out.println(
                    "Woonplaats na update: " +
                            adresNaUpdate.getWoonplaats()
            );
        }


        System.out.println(
                "\n--- FIND ALL ---"
        );

        List<Adres> alleAdressen =
                adresDAO.findAll();

        for (Adres a : alleAdressen) {

            System.out.println(
                    "#" +
                            a.getId() +
                            " " +
                            a.getStraat() +
                            " " +
                            a.getHuisnummer() +
                            ", " +
                            a.getWoonplaats()
            );
        }


        System.out.println(
                "\n--- DELETE ADRES ---"
        );

        boolean verwijderd =
                adresDAO.delete(
                        adres
                );

        System.out.println(
                "Adres verwijderd: " +
                        verwijderd
        );


        System.out.println(
                "\n--- DELETE REIZIGER ---"
        );

        boolean reizigerVerwijderd =
                reizigerDAO.delete(
                        reiziger
                );

        System.out.println(
                "Reiziger verwijderd: " +
                        reizigerVerwijderd
        );
    }


    private static void testOVChipkaartDAO(
            ReizigerDAO reizigerDAO,
            OVChipkaartDAO ovChipkaartDAO) {

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "OV-CHIPKAART DAO HIBERNATE"
        );

        System.out.println(
                "=========================================="
        );

        Reiziger reiziger =
                new Reiziger(
                        REIZIGER_ID_KAART_TEST,
                        "O.",
                        null,
                        "KaartTest",
                        Date.valueOf(
                                "2002-01-01"
                        )
                );


        System.out.println(
                "\n--- SAVE REIZIGER ---"
        );

        boolean reizigerOpgeslagen =
                reizigerDAO.save(
                        reiziger
                );

        System.out.println(
                "Reiziger opgeslagen: " +
                        reizigerOpgeslagen
        );


        OVChipkaart ovChipkaart =
                new OVChipkaart(
                        KAART_NUMMER_TEST,
                        LocalDate.of(
                                2028,
                                12,
                                31
                        ),
                        2,
                        25.50,
                        reiziger
                );


        System.out.println(
                "\n--- SAVE OV-CHIPKAART ---"
        );

        boolean opgeslagen =
                ovChipkaartDAO.save(
                        ovChipkaart
                );

        System.out.println(
                "OV-chipkaart opgeslagen: " +
                        opgeslagen
        );


        System.out.println(
                "\n--- FIND BY REIZIGER ---"
        );

        List<OVChipkaart> kaarten =
                ovChipkaartDAO.findByReiziger(
                        reiziger
                );

        for (OVChipkaart kaart : kaarten) {

            System.out.println(
                    "Kaart #" +
                            kaart.getKaart_nummer() +
                            ", geldig tot " +
                            kaart.getGeldig_tot() +
                            ", klasse " +
                            kaart.getKlasse() +
                            ", saldo " +
                            kaart.getSaldo()
            );
        }


        System.out.println(
                "\n--- UPDATE ---"
        );

        ovChipkaart.setSaldo(
                50.00
        );

        boolean gewijzigd =
                ovChipkaartDAO.update(
                        ovChipkaart
                );

        System.out.println(
                "OV-chipkaart gewijzigd: " +
                        gewijzigd
        );


        System.out.println(
                "\n--- CONTROLE NA UPDATE ---"
        );

        List<OVChipkaart> kaartenNaUpdate =
                ovChipkaartDAO.findByReiziger(
                        reiziger
                );

        for (OVChipkaart kaart : kaartenNaUpdate) {

            System.out.println(
                    "Kaart #" +
                            kaart.getKaart_nummer() +
                            " heeft saldo: " +
                            kaart.getSaldo()
            );
        }


        System.out.println(
                "\n--- FIND ALL ---"
        );

        List<OVChipkaart> alleKaarten =
                ovChipkaartDAO.findAll();

        for (OVChipkaart kaart : alleKaarten) {

            System.out.println(
                    "Kaart #" +
                            kaart.getKaart_nummer() +
                            ", saldo " +
                            kaart.getSaldo()
            );
        }


        System.out.println(
                "\n--- DELETE OV-CHIPKAART ---"
        );

        boolean verwijderd =
                ovChipkaartDAO.delete(
                        ovChipkaart
                );

        System.out.println(
                "OV-chipkaart verwijderd: " +
                        verwijderd
        );


        System.out.println(
                "\n--- DELETE REIZIGER ---"
        );

        boolean reizigerVerwijderd =
                reizigerDAO.delete(
                        reiziger
                );

        System.out.println(
                "Reiziger verwijderd: " +
                        reizigerVerwijderd
        );
    }


    private static void testProductDAO(
            ReizigerDAO reizigerDAO,
            OVChipkaartDAO ovChipkaartDAO,
            ProductDAO productDAO) {

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "PRODUCT DAO HIBERNATE"
        );

        System.out.println(
                "=========================================="
        );

        Reiziger reiziger =
                new Reiziger(
                        REIZIGER_ID_PRODUCT_TEST,
                        "P.",
                        null,
                        "ProductTest",
                        Date.valueOf(
                                "2003-01-01"
                        )
                );


        System.out.println(
                "\n--- SAVE REIZIGER ---"
        );

        boolean reizigerOpgeslagen =
                reizigerDAO.save(
                        reiziger
                );

        System.out.println(
                "Reiziger opgeslagen: " +
                        reizigerOpgeslagen
        );


        OVChipkaart ovChipkaart =
                new OVChipkaart(
                        KAART_NUMMER_PRODUCT_TEST,
                        LocalDate.of(
                                2029,
                                6,
                                30
                        ),
                        1,
                        50.00,
                        reiziger
                );


        System.out.println(
                "\n--- SAVE OV-CHIPKAART ---"
        );

        boolean kaartOpgeslagen =
                ovChipkaartDAO.save(
                        ovChipkaart
                );

        System.out.println(
                "OV-chipkaart opgeslagen: " +
                        kaartOpgeslagen
        );


        Product product =
                new Product(
                        PRODUCT_NUMMER_TEST,
                        "Dal Voordeel",
                        "Voordeelproduct voor reizen buiten de spits",
                        5.00
                );

        product.addOVChipkaart(
                ovChipkaart
        );


        System.out.println(
                "\n--- SAVE PRODUCT ---"
        );

        boolean opgeslagen =
                productDAO.save(
                        product
                );

        System.out.println(
                "Product opgeslagen: " +
                        opgeslagen
        );


        System.out.println(
                "\n--- FIND BY ID ---"
        );

        Product gevondenProduct =
                productDAO.findById(
                        PRODUCT_NUMMER_TEST
                );

        if (gevondenProduct != null) {

            System.out.println(
                    "Product #" +
                            gevondenProduct.getProduct_nummer() +
                            ", naam: " +
                            gevondenProduct.getNaam() +
                            ", prijs: " +
                            gevondenProduct.getPrijs()
            );
        }


        System.out.println(
                "\n--- FIND BY OV-CHIPKAART ---"
        );

        List<Product> producten =
                productDAO.findByOVChipkaart(
                        ovChipkaart
                );

        for (Product p : producten) {

            System.out.println(
                    "Product #" +
                            p.getProduct_nummer() +
                            ", naam: " +
                            p.getNaam() +
                            ", prijs: " +
                            p.getPrijs()
            );
        }


        System.out.println(
                "\n--- UPDATE ---"
        );

        product.setNaam(
                "Dal Voordeel Gewijzigd"
        );

        product.setBeschrijving(
                "Gewijzigde beschrijving"
        );

        product.setPrijs(
                7.50
        );

        boolean gewijzigd =
                productDAO.update(
                        product
                );

        System.out.println(
                "Product gewijzigd: " +
                        gewijzigd
        );


        System.out.println(
                "\n--- CONTROLE NA UPDATE ---"
        );

        Product productNaUpdate =
                productDAO.findById(
                        PRODUCT_NUMMER_TEST
                );

        if (productNaUpdate != null) {

            System.out.println(
                    "Naam na update: " +
                            productNaUpdate.getNaam()
            );

            System.out.println(
                    "Beschrijving na update: " +
                            productNaUpdate.getBeschrijving()
            );

            System.out.println(
                    "Prijs na update: " +
                            productNaUpdate.getPrijs()
            );
        }


        System.out.println(
                "\n--- FIND ALL ---"
        );

        List<Product> alleProducten =
                productDAO.findAll();

        for (Product p : alleProducten) {

            System.out.println(
                    "Product #" +
                            p.getProduct_nummer() +
                            ", naam: " +
                            p.getNaam() +
                            ", prijs: " +
                            p.getPrijs()
            );
        }


        System.out.println(
                "\n--- DELETE PRODUCT ---"
        );

        boolean productVerwijderd =
                productDAO.delete(
                        product
                );

        System.out.println(
                "Product verwijderd: " +
                        productVerwijderd
        );


        System.out.println(
                "\n--- DELETE OV-CHIPKAART ---"
        );

        boolean kaartVerwijderd =
                ovChipkaartDAO.delete(
                        ovChipkaart
                );

        System.out.println(
                "OV-chipkaart verwijderd: " +
                        kaartVerwijderd
        );


        System.out.println(
                "\n--- DELETE REIZIGER ---"
        );

        boolean reizigerVerwijderd =
                reizigerDAO.delete(
                        reiziger
                );

        System.out.println(
                "Reiziger verwijderd: " +
                        reizigerVerwijderd
        );
    }
}