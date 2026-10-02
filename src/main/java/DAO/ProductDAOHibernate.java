package main.java.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import main.java.POJO.OVChipkaart;
import main.java.POJO.Product;

import java.util.List;

public class ProductDAOHibernate
        implements ProductDAO {

    private final EntityManagerFactory emf;

    public ProductDAOHibernate(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean save(Product product) {

        if (product == null) {
            return false;
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            em.persist(product);

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
    public boolean update(Product product) {

        if (product == null) {
            return false;
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            em.merge(product);

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
    public boolean delete(Product product) {

        if (product == null) {
            return false;
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            int aantal =
                    em.createQuery(
                                    "DELETE FROM Product p " +
                                            "WHERE p.product_nummer = :productNummer"
                            )
                            .setParameter(
                                    "productNummer",
                                    product.getProduct_nummer()
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
    public Product findById(int id) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.find(
                    Product.class,
                    id
            );

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

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT p " +
                                    "FROM Product p " +
                                    "JOIN p.ovChipkaarten o " +
                                    "WHERE o.kaart_nummer = :kaartNummer",
                            Product.class
                    )
                    .setParameter(
                            "kaartNummer",
                            ovChipkaart.getKaart_nummer()
                    )
                    .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findAll() {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT p FROM Product p",
                            Product.class
                    )
                    .getResultList();

        } finally {
            em.close();
        }
    }
}