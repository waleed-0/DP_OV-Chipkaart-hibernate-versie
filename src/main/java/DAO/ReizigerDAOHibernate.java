package main.java.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import main.java.POJO.Reiziger;

import java.sql.Date;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO {

    private final EntityManagerFactory emf;

    public ReizigerDAOHibernate(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean save(Reiziger reiziger) {

        if (reiziger == null) {
            return false;
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            em.persist(reiziger);

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
    public boolean update(Reiziger reiziger) {

        if (reiziger == null) {
            return false;
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            em.merge(reiziger);

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
    public boolean delete(Reiziger reiziger) {

        if (reiziger == null) {
            return false;
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            int aantal =
                    em.createQuery(
                                    "DELETE FROM Reiziger r " +
                                            "WHERE r.reiziger_id = :reizigerId"
                            )
                            .setParameter(
                                    "reizigerId",
                                    reiziger.getId()
                            )
                            .executeUpdate();

            transaction.commit();

            return aantal > 0;

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
    public Reiziger findById(int id) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.find(
                    Reiziger.class,
                    id
            );

        } finally {
            em.close();
        }
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {

        EntityManager em = emf.createEntityManager();

        try {
            Date geboortedatum =
                    Date.valueOf(
                            datum
                    );

            return em.createQuery(
                            "SELECT r " +
                                    "FROM Reiziger r " +
                                    "WHERE r.geboortedatum = :datum",
                            Reiziger.class
                    )
                    .setParameter(
                            "datum",
                            geboortedatum
                    )
                    .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public List<Reiziger> findAll() {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT r FROM Reiziger r",
                            Reiziger.class
                    )
                    .getResultList();

        } finally {
            em.close();
        }
    }
}