package org.generation.italy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class main {
	private final static String URL = "jdbc:mysql://localhost:3306/db_nations";
	private final static String USER = "root";
	private final static String PASSWORD = "RootPassword";

	public static void main(String[] args) {
		
		try(Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
			
			String sql = "select c2.country_id as Id_nazione, c2.name as Nazione, r.name as Regione, c.name as Continente\r\n"
					+ "from regions r\r\n"
					+ "inner join continents c  on r.continent_id = c.continent_id\r\n"
					+ "inner join countries c2 on c2.region_id = r.region_id\r\n"
					+ "order by c2.name;" ;
			try(PreparedStatement ps = con.prepareStatement(sql)) {
				try (ResultSet rs = ps.executeQuery()) {
					while(rs.next()) {
						System.out.format(rs.getString(1) + " - ");
						System.out.format(rs.getString(2) + " - ");
						System.out.format(rs.getString(3) + " - ");
						System.out.println(rs.getString(4));
					}
				}
				}
			}catch(SQLException e) {
			System.out.println("Si è verificato un errore:");
			System.out.println(e.getMessage());	
		}

	}

}
