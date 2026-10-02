package main.java.DAO;

import main.java.POJO.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO {

    boolean save(
            Reiziger reiziger);

    boolean update(
            Reiziger reiziger);

    boolean delete(
            Reiziger reiziger);

    Reiziger findById(
            int id);

    List<Reiziger> findByGbdatum(
            String datum);

    List<Reiziger> findAll();
}