package main.java.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import main.java.POJO.OVChipkaart;
import main.java.POJO.Product;

import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductDAOHibernate implements ProductDAO {

    private final EntityManagerFactory emf;

    public ProductDAOHibernate(
            EntityManagerFactory emf) {

        this.emf = emf;
    }

    @Override
    public boolean save(
            Product product) {

        if (product == null) {
            return false;
        }

        EntityManager em =
                emf.createEntityManager();

        EntityTransaction transaction =
                em.getTransaction();

        try {

            transaction.begin();

            List<OVChipkaart> gewensteKaarten =
                    new ArrayList<>(
                            product.getOvChipkaarten()
                    );

            product.getOvChipkaarten().clear();

            em.persist(
                    product
            );

            for (OVChipkaart kaart :
                    gewensteKaarten) {

                OVChipkaart managedKaart =
                        em.find(
                                OVChipkaart.class,
                                kaart.getKaart_nummer()
                        );

                if (managedKaart == null) {

                    throw new IllegalArgumentException(
                            "OVChipkaart #" +
                                    kaart.getKaart_nummer() +
                                    " bestaat niet."
                    );
                }

                product.addOVChipkaart(
                        managedKaart
                );
            }

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction.isActive()) {

                transaction.rollback();
            }

            e.printStackTrace();

            return false;

        } finally {

            em.close();
        }
    }

    @Override
    public boolean update(
            Product product) {

        if (product == null) {
            return false;
        }

        EntityManager em =
                emf.createEntityManager();

        EntityTransaction transaction =
                em.getTransaction();

        try {

            transaction.begin();

            Product managedProduct =
                    em.find(
                            Product.class,
                            product.getProduct_nummer()
                    );

            if (managedProduct == null) {

                transaction.rollback();

                return false;
            }

            managedProduct.setNaam(
                    product.getNaam()
            );

            managedProduct.setBeschrijving(
                    product.getBeschrijving()
            );

            managedProduct.setPrijs(
                    product.getPrijs()
            );

            Set<Integer> gewensteKaartNummers =
                    new HashSet<>();

            for (OVChipkaart kaart :
                    product.getOvChipkaarten()) {

                gewensteKaartNummers.add(
                        kaart.getKaart_nummer()
                );
            }

            List<OVChipkaart> bestaandeKaarten =
                    new ArrayList<>(
                            managedProduct.getOvChipkaarten()
                    );

            for (OVChipkaart bestaandeKaart :
                    bestaandeKaarten) {

                if (!gewensteKaartNummers.contains(
                        bestaandeKaart.getKaart_nummer())) {

                    managedProduct.removeOVChipkaart(
                            bestaandeKaart
                    );
                }
            }

            Set<Integer> huidigeKaartNummers =
                    new HashSet<>();

            for (OVChipkaart kaart :
                    managedProduct.getOvChipkaarten()) {

                huidigeKaartNummers.add(
                        kaart.getKaart_nummer()
                );
            }

            for (OVChipkaart kaart :
                    product.getOvChipkaarten()) {

                if (!huidigeKaartNummers.contains(
                        kaart.getKaart_nummer())) {

                    OVChipkaart managedKaart =
                            em.find(
                                    OVChipkaart.class,
                                    kaart.getKaart_nummer()
                            );

                    if (managedKaart == null) {

                        throw new IllegalArgumentException(
                                "OVChipkaart #" +
                                        kaart.getKaart_nummer() +
                                        " bestaat niet."
                        );
                    }

                    managedProduct.addOVChipkaart(
                            managedKaart
                    );
                }
            }

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction.isActive()) {

                transaction.rollback();
            }

            e.printStackTrace();

            return false;

        } finally {

            em.close();
        }
    }

    @Override
    public boolean delete(
            Product product) {

        if (product == null) {
            return false;
        }

        EntityManager em =
                emf.createEntityManager();

        EntityTransaction transaction =
                em.getTransaction();

        try {

            transaction.begin();

            Product managedProduct =
                    em.find(
                            Product.class,
                            product.getProduct_nummer()
                    );

            if (managedProduct == null) {

                transaction.rollback();

                return false;
            }

            List<OVChipkaart> gekoppeldeKaarten =
                    new ArrayList<>(
                            managedProduct.getOvChipkaarten()
                    );

            for (OVChipkaart kaart :
                    gekoppeldeKaarten) {

                managedProduct.removeOVChipkaart(
                        kaart
                );
            }

            em.remove(
                    managedProduct
            );

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction.isActive()) {

                transaction.rollback();
            }

            e.printStackTrace();

            return false;

        } finally {

            em.close();
        }
    }

    @Override
    public Product findById(
            int id) {

        EntityManager em =
                emf.createEntityManager();

        try {

            List<Product> resultaten =
                    em.createQuery(
                                    "SELECT DISTINCT p " +
                                            "FROM Product p " +
                                            "LEFT JOIN FETCH p.ovChipkaarten o " +
                                            "LEFT JOIN FETCH o.reiziger " +
                                            "WHERE p.product_nummer = :id",
                                    Product.class
                            )
                            .setParameter(
                                    "id",
                                    id
                            )
                            .getResultList();

            if (resultaten.isEmpty()) {

                return null;
            }

            Product product =
                    resultaten.get(0);

            for (OVChipkaart kaart :
                    product.getOvChipkaarten()) {

                Hibernate.initialize(
                        kaart.getProducten()
                );
            }

            return product;

        } finally {

            em.close();
        }
    }

    @Override
    public List<Product> findByOVChipkaart(
            OVChipkaart ovChipkaart) {

        if (ovChipkaart == null) {

            return List.of();
        }

        EntityManager em =
                emf.createEntityManager();

        try {

            List<Product> producten =
                    em.createQuery(
                                    "SELECT DISTINCT p " +
                                            "FROM Product p " +
                                            "LEFT JOIN FETCH p.ovChipkaarten o " +
                                            "LEFT JOIN FETCH o.reiziger " +
                                            "WHERE EXISTS (" +
                                            "SELECT 1 " +
                                            "FROM Product p2 " +
                                            "JOIN p2.ovChipkaarten o2 " +
                                            "WHERE p2 = p " +
                                            "AND o2.kaart_nummer = :kaartNummer" +
                                            ")",
                                    Product.class
                            )
                            .setParameter(
                                    "kaartNummer",
                                    ovChipkaart.getKaart_nummer()
                            )
                            .getResultList();

            for (Product product :
                    producten) {

                for (OVChipkaart kaart :
                        product.getOvChipkaarten()) {

                    Hibernate.initialize(
                            kaart.getProducten()
                    );
                }
            }

            return producten;

        } finally {

            em.close();
        }
    }

    @Override
    public List<Product> findAll() {

        EntityManager em =
                emf.createEntityManager();

        try {

            List<Product> producten =
                    em.createQuery(
                                    "SELECT DISTINCT p " +
                                            "FROM Product p " +
                                            "LEFT JOIN FETCH p.ovChipkaarten o " +
                                            "LEFT JOIN FETCH o.reiziger",
                                    Product.class
                            )
                            .getResultList();

            for (Product product :
                    producten) {

                for (OVChipkaart kaart :
                        product.getOvChipkaarten()) {

                    Hibernate.initialize(
                            kaart.getProducten()
                    );
                }
            }

            return producten;

        } finally {

            em.close();
        }
    }
}