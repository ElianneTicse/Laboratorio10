package com.example.laboratorio10.Daos;

import com.example.laboratorio10.Beans.Country;
import com.example.laboratorio10.Beans.Region;


import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class CountryDao extends DaoBase {

    public ArrayList<Country> listar() {

        ArrayList<Country> lista = new ArrayList<>();

        try (Connection conn = this.getConection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * from countries c inner join regions r on (c.region_id=r.region_id);")) {

            while (rs.next()) {
                Country country = new Country();
                Region region = new Region();
                country.setCountryId(rs.getString(1));
                country.setCountryName(rs.getString(2));
                region.setIdRegion(rs.getBigDecimal(3));
                region.setName(rs.getString(5));
                country.setRegion(region);
                lista.add(country);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public void crear(String countryId, String countryName, BigDecimal regionId) {
        try {
            try (Connection conn = this.getConection();) {
                System.out.println(countryName);
                String sql = "INSERT INTO countries (`country_id`, `country_name`, `region_id`) "
                        + "VALUES (?,?,?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, countryId);
                    pstmt.setString(2, countryName);
                    pstmt.setBigDecimal(3, regionId);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Country obtener(String countryId) {

        Country country = null;
        try {
            String sql = "SELECT * FROM countries WHERE country_id = ?";
            try (Connection conn = this.getConection();
                    PreparedStatement pstmt = conn.prepareStatement(sql);) {
                pstmt.setString(1, countryId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        country = new Country();
                        country.setCountryId(rs.getString(1));
                        country.setCountryName(rs.getString(2));
                        country.setRegionId(rs.getBigDecimal(3));
                    }
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return country;
    }

    public void actualizar(String countryId, String countryName, BigDecimal regionId) {
        try {
            try (Connection conn = this.getConection();) {
                String sql = "UPDATE countries SET country_name = ?, region_id = ? "
                        + "WHERE country_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, countryName);
                    pstmt.setBigDecimal(2, regionId);
                    pstmt.setString(3, countryId);
                    pstmt.executeUpdate();
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void borrar(String countryId) {
        try {
            try (Connection conn = this.getConection();) {
                String sql = "DELETE FROM countries WHERE country_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, countryId);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public ArrayList<Region> listaRegiones(){
        ArrayList<Region> lista = new ArrayList<>();

        try (Connection conn = this.getConection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * from regions;")) {

            while (rs.next()) {
                Region region = new Region();
                region.setIdRegion(rs.getBigDecimal(1));
                region.setName(rs.getString(2));
                lista.add(region);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

}
