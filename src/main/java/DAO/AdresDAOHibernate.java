package main.java.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import main.java.POJO.Adres;
import main.java.POJO.Reiziger;

import java.util.List;

public class AdresDAOHibernate implements AdresDAO {

    private final EntityManagerFactory emf;

    public AdresDAOHibernate(
            EntityManagerFactory emf) {

        this.emf = emf;
    }

    @Override
    public boolean save(
            Adres adres) {

        if (adres == null) {
            return false;
        }

        EntityManager em =
                emf.createEntityManager();

        EntityTransaction transaction =
                em.getTransaction();

        try {

            transaction.begin();

            em.persist(
                    adres
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
    public boolean update(
            Adres adres) {

        if (adres == null) {
            return false;
        }

        EntityManager em =
                emf.createEntityManager();

        EntityTransaction transaction =
                em.getTransaction();

        try {

            transaction.begin();

            em.merge(
                    adres
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
    public boolean delete(
            Adres adres) {

        if (adres == null) {
            return false;
        }

        EntityManager em =
                emf.createEntityManager();

        EntityTransaction transaction =
                em.getTransaction();

        try {

            transaction.begin();

            Adres managedAdres =
                    em.find(
                            Adres.class,
                            adres.getId()
                    );

            if (managedAdres == null) {

                transaction.rollback();

                return false;
            }

            em.remove(
                    managedAdres
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
    public Adres findById(
            int id) {

        EntityManager em =
                emf.createEntityManager();

        try {

            List<Adres> resultaten =
                    em.createQuery(
                                    "SELECT a " +
                                            "FROM Adres a " +
                                            "JOIN FETCH a.reiziger " +
                                            "WHERE a.adres_id = :id",
                                    Adres.class
                            )
                            .setParameter(
                                    "id",
                                    id
                            )
                            .getResultList();

            if (resultaten.isEmpty()) {
                return null;
            }

            return resultaten.get(0);

        } finally {

            em.close();
        }
    }

    @Override
    public Adres findByReiziger(
            Reiziger reiziger) {

        if (reiziger == null) {
            return null;
        }

        EntityManager em =
                emf.createEntityManager();

        try {

            List<Adres> resultaten =
                    em.createQuery(
                                    "SELECT a " +
                                            "FROM Adres a " +
                                            "JOIN FETCH a.reiziger " +
                                            "WHERE a.reiziger.reiziger_id = :reizigerId",
                                    Adres.class
                            )
                            .setParameter(
                                    "reizigerId",
                                    reiziger.getId()
                            )
                            .getResultList();

            if (resultaten.isEmpty()) {
                return null;
            }

            return resultaten.get(0);

        } finally {

            em.close();
        }
    }

    @Override
    public List<Adres> findAll() {

        EntityManager em =
                emf.createEntityManager();

        try {

            return em.createQuery(
                            "SELECT a " +
                                    "FROM Adres a " +
                                    "JOIN FETCH a.reiziger",
                            Adres.class
                    )
                    .getResultList();

        } finally {

            em.close();
        }
    }
}