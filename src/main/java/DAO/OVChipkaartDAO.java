package main.java.DAO;

import main.java.POJO.OVChipkaart;
import main.java.POJO.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {

    boolean save(
            OVChipkaart ovChipkaart);

    boolean update(
            OVChipkaart ovChipkaart);

    boolean delete(
            OVChipkaart ovChipkaart);

    List<OVChipkaart> findByReiziger(
            Reiziger reiziger);

    List<OVChipkaart> findAll();
}