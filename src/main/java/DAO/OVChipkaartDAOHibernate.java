package main.java.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import main.java.POJO.OVChipkaart;
import main.java.POJO.Reiziger;

import java.util.List;

public class OVChipkaartDAOHibernate
        implements OVChipkaartDAO {

    private final EntityManagerFactory emf;

    public OVChipkaartDAOHibernate(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {

        if (ovChipkaart == null) {
            return false;
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            em.persist(ovChipkaart);

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
    public boolean update(OVChipkaart ovChipkaart) {

        if (ovChipkaart == null) {
            return false;
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            em.merge(ovChipkaart);

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
    public boolean delete(OVChipkaart ovChipkaart) {

        if (ovChipkaart == null) {
            return false;
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            int aantal =
                    em.createQuery(
                                    "DELETE FROM OVChipkaart o " +
                                            "WHERE o.kaart_nummer = :kaartNummer"
                            )
                            .setParameter(
                                    "kaartNummer",
                                    ovChipkaart.getKaart_nummer()
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
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {

        if (reiziger == null) {
            return List.of();
        }

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT o " +
                                    "FROM OVChipkaart o " +
                                    "WHERE o.reiziger.reiziger_id = :reizigerId",
                            OVChipkaart.class
                    )
                    .setParameter(
                            "reizigerId",
                            reiziger.getId()
                    )
                    .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public List<OVChipkaart> findAll() {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT o FROM OVChipkaart o",
                            OVChipkaart.class
                    )
                    .getResultList();

        } finally {
            em.close();
        }
    }
}