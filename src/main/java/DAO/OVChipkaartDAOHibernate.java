package main.java.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import main.java.POJO.OVChipkaart;
import main.java.POJO.Product;
import main.java.POJO.Reiziger;

import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {

    private final EntityManagerFactory emf;

    public OVChipkaartDAOHibernate(
            EntityManagerFactory emf) {

        this.emf = emf;
    }

    @Override
    public boolean save(
            OVChipkaart ovChipkaart) {

        if (ovChipkaart == null) {
            return false;
        }

        EntityManager em =
                emf.createEntityManager();

        EntityTransaction transaction =
                em.getTransaction();

        try {

            transaction.begin();

            List<Product> gewensteProducten =
                    new ArrayList<>(
                            ovChipkaart.getProducten()
                    );
            ovChipkaart.getProducten().clear();

            if (ovChipkaart.getReiziger() != null) {

                Reiziger managedReiziger =
                        em.find(
                                Reiziger.class,
                                ovChipkaart
                                        .getReiziger()
                                        .getId()
                        );

                if (managedReiziger == null) {

                    throw new IllegalArgumentException(
                            "Reiziger #" +
                                    ovChipkaart
                                            .getReiziger()
                                            .getId() +
                                    " bestaat niet."
                    );
                }

                ovChipkaart.setReiziger(
                        managedReiziger
                );
            }

            em.persist(
                    ovChipkaart
            );

            for (Product product :
                    gewensteProducten) {

                Product managedProduct =
                        em.find(
                                Product.class,
                                product.getProduct_nummer()
                        );

                if (managedProduct == null) {

                    throw new IllegalArgumentException(
                            "Product #" +
                                    product.getProduct_nummer() +
                                    " bestaat niet."
                    );
                }

                managedProduct.addOVChipkaart(
                        ovChipkaart
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
            OVChipkaart ovChipkaart) {

        if (ovChipkaart == null) {
            return false;
        }

        EntityManager em =
                emf.createEntityManager();

        EntityTransaction transaction =
                em.getTransaction();

        try {

            transaction.begin();

            OVChipkaart managedOVChipkaart =
                    em.find(
                            OVChipkaart.class,
                            ovChipkaart.getKaart_nummer()
                    );

            if (managedOVChipkaart == null) {

                transaction.rollback();

                return false;
            }

            managedOVChipkaart.setGeldig_tot(
                    ovChipkaart.getGeldig_tot()
            );

            managedOVChipkaart.setKlasse(
                    ovChipkaart.getKlasse()
            );

            managedOVChipkaart.setSaldo(
                    ovChipkaart.getSaldo()
            );

            if (ovChipkaart.getReiziger() != null) {

                Reiziger managedReiziger =
                        em.find(
                                Reiziger.class,
                                ovChipkaart
                                        .getReiziger()
                                        .getId()
                        );

                if (managedReiziger == null) {

                    throw new IllegalArgumentException(
                            "Reiziger #" +
                                    ovChipkaart
                                            .getReiziger()
                                            .getId() +
                                    " bestaat niet."
                    );
                }

                managedOVChipkaart.setReiziger(
                        managedReiziger
                );
            }

            Set<Integer> gewensteProductNummers =
                    new HashSet<>();

            for (Product product :
                    ovChipkaart.getProducten()) {

                gewensteProductNummers.add(
                        product.getProduct_nummer()
                );
            }

            List<Product> bestaandeProducten =
                    new ArrayList<>(
                            managedOVChipkaart.getProducten()
                    );

            for (Product bestaandProduct :
                    bestaandeProducten) {

                if (!gewensteProductNummers.contains(
                        bestaandProduct.getProduct_nummer())) {

                    bestaandProduct.removeOVChipkaart(
                            managedOVChipkaart
                    );
                }
            }

            Set<Integer> huidigeProductNummers =
                    new HashSet<>();

            for (Product product :
                    managedOVChipkaart.getProducten()) {

                huidigeProductNummers.add(
                        product.getProduct_nummer()
                );
            }

            for (Product product :
                    ovChipkaart.getProducten()) {

                if (!huidigeProductNummers.contains(
                        product.getProduct_nummer())) {

                    Product managedProduct =
                            em.find(
                                    Product.class,
                                    product.getProduct_nummer()
                            );

                    if (managedProduct == null) {

                        throw new IllegalArgumentException(
                                "Product #" +
                                        product.getProduct_nummer() +
                                        " bestaat niet."
                        );
                    }

                    managedProduct.addOVChipkaart(
                            managedOVChipkaart
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
            OVChipkaart ovChipkaart) {

        if (ovChipkaart == null) {
            return false;
        }

        EntityManager em =
                emf.createEntityManager();

        EntityTransaction transaction =
                em.getTransaction();

        try {

            transaction.begin();
            OVChipkaart managedOVChipkaart =
                    em.merge(
                            ovChipkaart
                    );

            List<Product> gekoppeldeProducten =
                    new ArrayList<>(
                            managedOVChipkaart.getProducten()
                    );

            for (Product product :
                    gekoppeldeProducten) {

                product.removeOVChipkaart(
                        managedOVChipkaart
                );
            }

            em.remove(
                    managedOVChipkaart
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
    public List<OVChipkaart> findByReiziger(
            Reiziger reiziger) {

        if (reiziger == null) {

            return List.of();
        }

        EntityManager em =
                emf.createEntityManager();

        try {

            List<OVChipkaart> kaarten =
                    em.createQuery(
                                    "SELECT DISTINCT o " +
                                            "FROM OVChipkaart o " +
                                            "JOIN FETCH o.reiziger " +
                                            "LEFT JOIN FETCH o.producten " +
                                            "WHERE o.reiziger.reiziger_id = :reizigerId",
                                    OVChipkaart.class
                            )
                            .setParameter(
                                    "reizigerId",
                                    reiziger.getId()
                            )
                            .getResultList();

            for (OVChipkaart kaart :
                    kaarten) {

                for (Product product :
                        kaart.getProducten()) {

                    Hibernate.initialize(
                            product.getOvChipkaarten()
                    );
                }
            }

            return kaarten;

        } finally {

            em.close();
        }
    }

    @Override
    public List<OVChipkaart> findAll() {

        EntityManager em =
                emf.createEntityManager();

        try {

            List<OVChipkaart> kaarten =
                    em.createQuery(
                                    "SELECT DISTINCT o " +
                                            "FROM OVChipkaart o " +
                                            "JOIN FETCH o.reiziger " +
                                            "LEFT JOIN FETCH o.producten",
                                    OVChipkaart.class
                            )
                            .getResultList();

            for (OVChipkaart kaart :
                    kaarten) {

                for (Product product :
                        kaart.getProducten()) {

                    Hibernate.initialize(
                            product.getOvChipkaarten()
                    );
                }
            }

            return kaarten;

        } finally {

            em.close();
        }
    }
}