package main;

import data.Director;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class Main {

    private Connection connection;

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    private void run() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("lab.properties"))) {
            Properties props = new Properties();
            props.load(reader);
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/privatdemo", props);
            doJob();
        } catch (IOException e) {

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void doJob() {
        int m;
        while ((m = menu())!=0) {
            switch (m) {
                case 1: showAll(); break;
                case 2: showAddDirector(); break;
            }
        }

    }

    private void showAddDirector() {
        Scanner in = new Scanner(System.in);
        System.out.println("------ New director -------");
        System.out.print("Name: ");
        String name = in.nextLine();
        System.out.print("Country: ");
        String country = in.nextLine();
        addDirector(name, country);
    }

    private void addDirector(String name, String country) {
        try (PreparedStatement statement = connection.prepareStatement("insert into director (name, country) values (?, ?)")) {
            statement.setString(1, name);
            statement.setString(2, country);
            statement.executeUpdate();
            System.out.println("--- add success ---");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAll() {
        List<Director> directors = findAllDirectors();
        for (Director director : directors) {
            System.out.println(director);
        }
    }

    public List<Director> findAllDirectors() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from director");
            List<Director> directors = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String country = resultSet.getString("country");
                directors.add(new Director(id, name, country));
            }
            return directors;
        } catch (SQLException e) {
            System.out.println("error");
            return Collections.emptyList();
        }
    }

    private int menu() {
        System.out.println("1. Show All");
        System.out.println("2. Add director");
        System.out.println("0. Exit");
        return new Scanner(System.in).nextInt();
    }
}
