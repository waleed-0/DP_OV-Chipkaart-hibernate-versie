package main.java.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import main.java.POJO.Reiziger;

import java.sql.Date;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO {

    private final EntityManagerFactory emf;

    public ReizigerDAOHibernate(
            EntityManagerFactory emf) {

        this.emf = emf;
    }

    @Override
    public boolean save(
            Reiziger reiziger) {

        if (reiziger == null) {
            return false;
        }

        EntityManager em =
                emf.createEntityManager();

        EntityTransaction transaction =
                em.getTransaction();

        try {

            transaction.begin();

            em.persist(
                    reiziger
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
            Reiziger reiziger) {

        if (reiziger == null) {
            return false;
        }

        EntityManager em =
                emf.createEntityManager();

        EntityTransaction transaction =
                em.getTransaction();

        try {

            transaction.begin();

            em.merge(
                    reiziger
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
            Reiziger reiziger) {

        if (reiziger == null) {
            return false;
        }

        EntityManager em =
                emf.createEntityManager();

        EntityTransaction transaction =
                em.getTransaction();

        try {

            transaction.begin();

            Reiziger managedReiziger =
                    em.find(
                            Reiziger.class,
                            reiziger.getId()
                    );

            if (managedReiziger == null) {

                transaction.rollback();

                return false;
            }

            em.remove(
                    managedReiziger
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
    public Reiziger findById(
            int id) {

        EntityManager em =
                emf.createEntityManager();

        try {

            List<Reiziger> resultaten =
                    em.createQuery(
                                    "SELECT DISTINCT r " +
                                            "FROM Reiziger r " +
                                            "LEFT JOIN FETCH r.ovChipkaarten " +
                                            "WHERE r.reiziger_id = :id",
                                    Reiziger.class
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
    public List<Reiziger> findByGbdatum(
            String datum) {

        EntityManager em =
                emf.createEntityManager();

        try {

            Date geboortedatum =
                    Date.valueOf(
                            datum
                    );

            return em.createQuery(
                            "SELECT DISTINCT r " +
                                    "FROM Reiziger r " +
                                    "LEFT JOIN FETCH r.ovChipkaarten " +
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

        EntityManager em =
                emf.createEntityManager();

        try {

            return em.createQuery(
                            "SELECT DISTINCT r " +
                                    "FROM Reiziger r " +
                                    "LEFT JOIN FETCH r.ovChipkaarten",
                            Reiziger.class
                    )
                    .getResultList();

        } finally {

            em.close();
        }
    }
}