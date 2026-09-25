package main.java.DAO;

import main.java.POJO.OVChipkaart;
import main.java.POJO.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAOPsql implements ProductDAO {

    private final Connection conn;
    private OVChipkaartDAO ovChipkaartDAO;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
    }

    public void setOVChipkaartDAO(OVChipkaartDAO ovChipkaartDAO) {
        this.ovChipkaartDAO = ovChipkaartDAO;
    }

    @Override
    public boolean save(Product product) throws SQLException {

        if (product == null) {
            return false;
        }

        String query =
                "INSERT INTO product " +
                        "(product_nummer, naam, beschrijving, prijs) " +
                        "VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    product.getProduct_nummer()
            );

            statement.setString(
                    2,
                    product.getNaam()
            );

            statement.setString(
                    3,
                    product.getBeschrijving()
            );

            statement.setDouble(
                    4,
                    product.getPrijs()
            );

            if (statement.executeUpdate() <= 0) {
                return false;
            }
        }

        if (product.getOvChipkaarten() != null) {

            for (OVChipkaart ovChipkaart :
                    product.getOvChipkaarten()) {

                if (!saveKoppeling(
                        ovChipkaart.getKaart_nummer(),
                        product.getProduct_nummer())) {

                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean update(Product product)
            throws SQLException {

        if (product == null) {
            return false;
        }

        String query =
                "UPDATE product " +
                        "SET naam = ?, " +
                        "beschrijving = ?, " +
                        "prijs = ? " +
                        "WHERE product_nummer = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setString(
                    1,
                    product.getNaam()
            );

            statement.setString(
                    2,
                    product.getBeschrijving()
            );

            statement.setDouble(
                    3,
                    product.getPrijs()
            );

            statement.setInt(
                    4,
                    product.getProduct_nummer()
            );

            if (statement.executeUpdate() <= 0) {
                return false;
            }
        }

        List<Integer> databaseKaartNummers =
                findKaartNummersByProduct(
                        product.getProduct_nummer()
                );

        Map<Integer, OVChipkaart> javaKaarten =
                new HashMap<>();

        if (product.getOvChipkaarten() != null) {

            for (OVChipkaart ovChipkaart :
                    product.getOvChipkaarten()) {

                int kaartNummer =
                        ovChipkaart.getKaart_nummer();

                javaKaarten.put(
                        kaartNummer,
                        ovChipkaart
                );

                if (!databaseKaartNummers.contains(
                        kaartNummer)) {

                    if (!saveKoppeling(
                            kaartNummer,
                            product.getProduct_nummer())) {

                        return false;
                    }
                }
            }
        }

        for (Integer kaartNummer :
                databaseKaartNummers) {

            if (!javaKaarten.containsKey(
                    kaartNummer)) {

                if (!deleteKoppeling(
                        kaartNummer,
                        product.getProduct_nummer())) {

                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean delete(Product product)
            throws SQLException {

        if (product == null) {
            return false;
        }

        String deleteKoppelingen =
                "DELETE FROM ov_chipkaart_product " +
                        "WHERE product_nummer = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(
                             deleteKoppelingen
                     )) {

            statement.setInt(
                    1,
                    product.getProduct_nummer()
            );

            statement.executeUpdate();
        }

        String deleteProduct =
                "DELETE FROM product " +
                        "WHERE product_nummer = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(
                             deleteProduct
                     )) {

            statement.setInt(
                    1,
                    product.getProduct_nummer()
            );

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public Product findById(int id)
            throws SQLException {

        String query =
                "SELECT product_nummer, " +
                        "naam, " +
                        "beschrijving, " +
                        "prijs " +
                        "FROM product " +
                        "WHERE product_nummer = ?";

        Product product = null;

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    id
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    product =
                            maakProduct(
                                    resultSet
                            );
                }
            }
        }

        if (product != null) {

            vulOVChipkaartenRechtstreeks(
                    product
            );
        }

        return product;
    }

    @Override
    public List<Product> findByOVChipkaart(
            OVChipkaart ovChipkaart)
            throws SQLException {

        List<Product> producten =
                new ArrayList<>();

        if (ovChipkaart == null) {
            return producten;
        }

        String query =
                "SELECT p.product_nummer, " +
                        "p.naam, " +
                        "p.beschrijving, " +
                        "p.prijs " +
                        "FROM product p " +
                        "JOIN ov_chipkaart_product op " +
                        "ON p.product_nummer = op.product_nummer " +
                        "WHERE op.kaart_nummer = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    ovChipkaart.getKaart_nummer()
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Product product =
                            maakProduct(
                                    resultSet
                            );

                    product.addOVChipkaart(
                            ovChipkaart
                    );

                    vulOVChipkaartenRechtstreeks(
                            product
                    );

                    producten.add(
                            product
                    );
                }
            }
        }

        return producten;
    }

    @Override
    public List<Product> findAll()
            throws SQLException {

        List<Product> producten =
                new ArrayList<>();

        String query =
                "SELECT product_nummer, " +
                        "naam, " +
                        "beschrijving, " +
                        "prijs " +
                        "FROM product";

        try (PreparedStatement statement =
                     conn.prepareStatement(query);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Product product =
                        maakProduct(
                                resultSet
                        );

                vulOVChipkaartenRechtstreeks(
                        product
                );

                producten.add(
                        product
                );
            }
        }

        return producten;
    }

    private Product maakProduct(
            ResultSet resultSet)
            throws SQLException {

        return new Product(
                resultSet.getInt(
                        "product_nummer"
                ),
                resultSet.getString(
                        "naam"
                ),
                resultSet.getString(
                        "beschrijving"
                ),
                resultSet.getDouble(
                        "prijs"
                )
        );
    }

    private void vulOVChipkaartenRechtstreeks(
            Product product)
            throws SQLException {

        if (product == null) {
            return;
        }

        String query =
                "SELECT o.kaart_nummer, " +
                        "o.geldig_tot, " +
                        "o.klasse, " +
                        "o.saldo " +
                        "FROM ov_chipkaart o " +
                        "JOIN ov_chipkaart_product op " +
                        "ON o.kaart_nummer = op.kaart_nummer " +
                        "WHERE op.product_nummer = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    product.getProduct_nummer()
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    int kaartNummer =
                            resultSet.getInt(
                                    "kaart_nummer"
                            );


                    boolean bestaatAl = false;

                    for (OVChipkaart bestaandeKaart :
                            product.getOvChipkaarten()) {

                        if (bestaandeKaart
                                .getKaart_nummer()
                                == kaartNummer) {

                            bestaatAl = true;
                            break;
                        }
                    }

                    if (!bestaatAl) {

                        OVChipkaart ovChipkaart =
                                new OVChipkaart(
                                        kaartNummer,
                                        resultSet
                                                .getDate(
                                                        "geldig_tot"
                                                )
                                                .toLocalDate(),
                                        resultSet.getInt(
                                                "klasse"
                                        ),
                                        resultSet.getDouble(
                                                "saldo"
                                        )
                                );

                        product.addOVChipkaart(
                                ovChipkaart
                        );
                    }
                }
            }
        }
    }

    private List<Integer> findKaartNummersByProduct(
            int productNummer)
            throws SQLException {

        List<Integer> kaartNummers =
                new ArrayList<>();

        String query =
                "SELECT kaart_nummer " +
                        "FROM ov_chipkaart_product " +
                        "WHERE product_nummer = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    productNummer
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    kaartNummers.add(
                            resultSet.getInt(
                                    "kaart_nummer"
                            )
                    );
                }
            }
        }

        return kaartNummers;
    }

    private boolean saveKoppeling(
            int kaartNummer,
            int productNummer)
            throws SQLException {

        String query =
                "INSERT INTO ov_chipkaart_product " +
                        "(kaart_nummer, product_nummer) " +
                        "VALUES (?, ?) " +
                        "ON CONFLICT (kaart_nummer, product_nummer) " +
                        "DO NOTHING";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    kaartNummer
            );

            statement.setInt(
                    2,
                    productNummer
            );

            statement.executeUpdate();

            return true;
        }
    }

    private boolean deleteKoppeling(
            int kaartNummer,
            int productNummer)
            throws SQLException {

        String query =
                "DELETE FROM ov_chipkaart_product " +
                        "WHERE kaart_nummer = ? " +
                        "AND product_nummer = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    kaartNummer
            );

            statement.setInt(
                    2,
                    productNummer
            );

            statement.executeUpdate();

            return true;
        }
    }
}