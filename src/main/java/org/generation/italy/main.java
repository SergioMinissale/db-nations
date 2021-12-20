package org.generation.italy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class main {
	private final static String URL = "jdbc:mysql://localhost:3306/db_nations";
	private final static String USER = "root";
	private final static String PASSWORD = "RootPassword";

	public static void main(String[] args) {

		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

			Scanner scan = new Scanner(System.in);
			System.out.print("Ricerca: ");
			String ricerca = "%" + scan.nextLine() + "%";

			String query1 = "select c2.country_id as Id_nazione, c2.name as Nazione, r.name as Regione, c.name as Continente\r\n"
					+ "from regions r\r\n" + "inner join continents c  on r.continent_id = c.continent_id\r\n"
					+ "inner join countries c2 on c2.region_id = r.region_id\r\n" + "where c2.name like ?\r\n"
					+ "order by c2.name;";

			try (PreparedStatement ps = con.prepareStatement(query1)) {

				ps.setString(1, ricerca);

				try (ResultSet rs = ps.executeQuery()) {

					System.out.format("%3s%40s%25s%20s%n", "ID", "NAZIONE", "REGIONE", "CONTINENTE");

					while (rs.next()) {

						System.out.format("%3s%40s%25s%20s%n", rs.getString(1), rs.getString(2), rs.getString(3),
								rs.getString(4));
					}
				}
			}

			System.out.print("Inserisci ID di un paese: ");
			String id = scan.nextLine();
			String query2 = "select c.name from countries c where c.country_id = ?";

			try (PreparedStatement ps = con.prepareStatement(query2)) {
				ps.setString(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						System.out.println("\nDettagli per la nazione: " + rs.getString(1));
					}
				}
			}

			String query3 = "select l.language as Lingua from languages l\r\n"
					+ "inner join country_languages cl on cl.language_id = l.language_id\r\n"
					+ "inner join countries c on cl.country_id = c.country_id\r\n" + "where c.country_id = ?;";

			try (PreparedStatement ps = con.prepareStatement(query3)) {
				ps.setString(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					System.out.print("Lingue parlate: ");
					while (rs.next()) {
						System.out.print(rs.getString(1) + "  ");
					}
				}
			}

			String query4 = "select  cs.`year` as Anno, cs.population as Popolazione , cs.gdp as GDP\r\n"
					+ "from  country_stats cs\r\n" + "where country_id = 107\r\n" + "order by `year`\r\n"
					+ "desc limit 1;";
			System.out.println("\nStatistiche più recenti");
			try (PreparedStatement ps = con.prepareStatement(query4)) {
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						System.out.println("Anno: " + rs.getString(1));
						System.out.println("Popolazione: " + rs.getString(2));
						System.out.println("GDP: " + rs.getString(3));
					}

				}
			}
		} catch (SQLException e) {
			System.out.println("Si è verificato un errore:");
			System.out.println(e.getMessage());
		}
	}
}
