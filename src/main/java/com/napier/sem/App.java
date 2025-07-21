package com.napier.sem;

import java.sql.*;
import java.util.Scanner;


public class App {
    private Connection connection;
    private static final String URL = "jdbc:mysql://db:3306/world?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "example";

    public App(Connection connection) {
        this.connection = connection;
    }

    public static App getConnection() {
    for (int i = 0; i < 20; i++) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return new App(conn);
        } catch (ClassNotFoundException | SQLException e) {
            try { Thread.sleep(5000); } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    throw new RuntimeException("Failed to initialize database connection after 5 attempts");
}

    public static void main(String[] args) {
        App app = App.getConnection();
        app.ShowReportMenu();
    }

    public void ShowReportMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        System.out.println("\n=== Report Menu ===");
        System.out.println("1. All the countries in the world organised by largest population to smallest.");
        System.out.println("2. All the cities in the world organised by largest population to smallest.");
        System.out.println("3. All the capital cities in the world organised by largest population to smallest.");
        System.out.println("4. The top N populated cities in the world where N is provided by the user.");
        System.out.println("5. The population of people, people living in cities, and people not living in cities in each country.");
        System.out.println("6. The population of the world.");
        System.out.println("7. The population of continents.");
        System.out.println("8. The population of regions.");
        System.out.println("9. The population of countries.");
        System.out.println("10. The population of districts.");
        System.out.println("11. The population of cities.");
        System.out.println("12. The number of Chinese speakers with percentage of world population.");
        System.out.println("13. The number of English speakers with percentage of world population.");
        System.out.println("14. The number of Spanish speakers with percentage of world population");

        System.out.print("Enter your choice: ");

        choice = scanner.nextInt();

        switch (choice) {
            case 1: getAllCountriesPopulationLargestToSmallest();
                break;
            case 2: getAllCitiesPopulationLargestToSmallest();
                break;
            case 3: getAllCapitalCitiesPopulationLargestToSmallest();
                break;
            case 4: System.out.print("Enter how many cities you want to see: ");
                getTopNCitiesPopulation(scanner.nextInt());
                break;
            case 5: getPopulationCityAndRuralByCountry();
                break;
            case 6: System.out.println("The population of the world is: " + getWorldPopulation());
                break;
            case 7: getContinentPopulation();
                break;
            case 8: getRegionPopulation();
                break;
            case 9: getCountryPopulation();
                break;
            case 10: getDistrictPopulation();
                break;
            case 11: getCityPopulation();
                break;
            case 12: getLanguageWorldPercentage("Chinese");
                break;
            case 13: getLanguageWorldPercentage("English");
                break;
            case 14: getLanguageWorldPercentage("Spanish");
                break;


            default:
                System.out.print("Invalid choice. Please try again.");
        }

        scanner.close();
    }

    public void getAllCountriesPopulationLargestToSmallest() {
        String query = "SELECT co.Code, co.Name, co.Continent, co.Region, co.Population, ci.Name AS 'capital'" +
                " FROM country co JOIN city ci ON co.Capital = ci.Id" +
                " ORDER BY Population DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            System.out.println("All the countries in the world organised by largest population to smallest.");
            System.out.printf("%s | %s | %s | %s | %s | %s%n",
                    "code", "country", "continent", "region", "population", "capital");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CountryReport report = new CountryReport(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("continent"),
                        rs.getString("region"),
                        rs.getInt("population"),
                        rs.getString("capital")
                    );
                    PrintCountryReport(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAllCitiesPopulationLargestToSmallest(){

        String query = "SELECT ci.Name, co.Name AS 'Country', ci.District, ci.Population" +
                " FROM  country co JOIN city ci ON co.Code = ci.CountryCode" +
                " ORDER BY ci.Population DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            System.out.println("All the cities in the world organised by largest population to smallest.");
            System.out.printf("%s | %s | %s | %s%n",
                    "name", "country", "district","population");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PrintCityReport(rs);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAllCapitalCitiesPopulationLargestToSmallest(){
        String query = "SELECT ci.Name, co.Name AS 'Country', ci.District, ci.Population" +
                " FROM country co" +
                " JOIN city ci ON co.Capital = ci.Id" +
                " ORDER BY ci.Population DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            System.out.println("All the capital cities in the world organised by largest population to smallest.");
            System.out.printf("%s | %s | %s | %s%n",
                    "name", "country", "district","population");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PrintCityReport(rs);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getTopNCitiesPopulation(int n){
        String query = "SELECT ci.Name, co.Name AS 'Country', ci.District, ci.Population" +
                " FROM country co" +
                " JOIN city ci ON co.Code = ci.CountryCode" +
                " ORDER BY ci.Population DESC" +
                " LIMIT " + n;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            System.out.println("The top N populated cities in the world where N is provided by the user.");
            System.out.printf("%s | %s | %s | %s%n",
                    "name", "country", "district","population");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PrintCityReport(rs);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getPopulationCityAndRuralByCountry(){
        String query = "SELECT co.Name AS 'Country', co.Population," +
                " SUM(ci.Population) AS 'City Population'," +
                " (SUM(ci.Population) / co.Population) * 100 AS '% Population inside Cities'," +
                " (co.Population - SUM(ci.Population)) AS 'Outside City Population'," +
                " ((co.Population - SUM(ci.Population)) / co.Population) * 100 AS '% Population outside Cities'" +
                " FROM country co" +
                " JOIN city ci ON co.Code = ci.CountryCode" +
                " GROUP BY co.Name, co.Population" +
                " ORDER BY co.Population DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            System.out.println("The population of people, people living in cities, and people not living in cities in each country");
            System.out.printf("%s | %s | %s | %s | %s | %s%n",
                    "country", "population", "city population", "% population inside cities", "outside city population", "% population outside cities");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String country = rs.getString("country");
                    int population = rs.getInt("population");
                    int cityPopulation = rs.getInt("city population");
                    double cityPercentage = rs.getDouble("% population inside cities");
                    int outsideCityPopulation = rs.getInt("outside city population");
                    double outsideCityPercentage = rs.getDouble("% population outside cities");
                    System.out.printf("%s | %d | %d | %.2f | %d | %.2f%n",
                            country, population, cityPopulation, cityPercentage, outsideCityPopulation, outsideCityPercentage);


                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void PrintCountryReport(CountryReport report) {
    System.out.printf("%s | %s | %s | %s | %d | %s%n",
            report.getCode(),
            report.getName(),
            report.getContinent(),
            report.getRegion(),
            report.getPopulation(),
            report.getCapital());
}

    public void PrintCityReport(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String country = rs.getString("country");
        String district = rs.getString("district");
        int population = rs.getInt("population");


        System.out.printf("%s | %s | %s | %d%n",
                name, country, district, population);
    }

    public long getWorldPopulation() {
        String query = "SELECT SUM(Population) AS 'population'  FROM country";
        long population = 0;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    population = rs.getLong("population");


                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return population;
    }

    public void getContinentPopulation() {
        String query = "SELECT co.Continent," +
                " SUM(co.Population) as Population" +
                " FROM country co" +
                " GROUP BY co.Continent" +
                " ORDER BY Population DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            System.out.println("All continents organised by largest population to smallest.");
            System.out.printf("%s | %s%n", "Continent", "Population");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%s | %,d%n",
                            rs.getString("Continent"),
                            rs.getLong("Population"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void getRegionPopulation() {
        String query = "SELECT co.Region," +
                " SUM(co.Population) as Population" +
                " FROM country co" +
                " GROUP BY co.Region" +
                " ORDER BY Population DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            System.out.println("All regions organised by largest population to smallest.");
            System.out.printf("%s | %s%n", "Region", "Population");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%s | %,d%n",
                            rs.getString("region"),
                            rs.getLong("population"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getCountryPopulation() {
        String query = "SELECT co.Name," +
                " co.Population as Population" +
                " FROM country co" +
                " ORDER BY Population DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            System.out.println("All countries organised by largest population to smallest.");
            System.out.printf("%s | %s%n", "Country", "Population");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%s | %,d%n",
                            rs.getString("name"),
                            rs.getLong("population"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getDistrictPopulation() {
        String query = "SELECT ci.District," +
                " SUM(ci.Population) as Population" +
                " FROM city ci" +
                " GROUP BY ci.District" +
                " ORDER BY Population DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            System.out.println("All districts organised by largest population to smallest.");
            System.out.printf("%s | %s%n", "District", "Population");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%s | %,d%n",
                            rs.getString("district"),
                            rs.getLong("population"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getCityPopulation() {
        String query = "SELECT ci.Name," +
                " ci.Population as Population" +
                " FROM city ci" +
                " ORDER BY Population DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            System.out.println("All cities organised by largest population to smallest.");
            System.out.printf("%s | %s%n", "City", "Population");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%s | %,d%n",
                            rs.getString("name"),
                            rs.getLong("population"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getLanguageWorldPercentage(String language) {

        String query = "SELECT Language," +
                " SUM(co.population) AS Number_speakers," +
                " SUM(co.Population) / (SELECT SUM(Population) FROM country) * 100 AS Percentage_of_World" +
                " FROM country co" +
                " JOIN countrylanguage cl ON co.Code = cl.CountryCode" +
                " WHERE Language = '" + language + "'" +
                " GROUP BY Language" +
                " ORDER BY Number_speakers DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

        System.out.println("Number of speakers with percentage of world population.");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%s | %,d | %.2f%%%n",
                            rs.getString("language"),
                            rs.getLong("Number_speakers"),
                            rs.getDouble("Percentage_of_World"));

                }
        }
    } catch (SQLException e) {
    e.printStackTrace();
}

}

    public void PrintCityReport(CityReport report) {
    System.out.printf("%s | %s | %s | %d%n",
            report.getName(),
            report.getCountry(),
            report.getDistrict(),
            report.getPopulation());
}
}