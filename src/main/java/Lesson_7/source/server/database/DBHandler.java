package Lesson_7.source.server.database;

import java.sql.*;
import java.util.ArrayList;

public class DBHandler extends DBConst {
    private Connection connection;

    private Connection getDBConnection() {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error with SQL connection");
        }
        return connection;
    }

    public synchronized void create(User user) {
        String insert = "INSERT INTO " + dbTable + "(" +
                name + password + ") VALUES(?,?)";
        try {
            //Решил не использовать отключение автокомиита так как в наем пакете всего одна транзакция
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(insert);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in create new user" + e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized ArrayList<User> readAll() {
        //Возможно не езопасно выводить список изеров с
        // паролями но на решил поставлять полный список из БД
        String selectAll = "SELECT * FROM " + dbTable;
        ArrayList<User> userList = new ArrayList<>();
        User user;
        try {
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(selectAll);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new User();
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setId(resultSet.getInt("id"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return userList;
    }

    public synchronized User readId(User userId) {
        String selectId = "SELECT * FROM " + dbTable + "WHERE id=?";
        User user = null;
        try {
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(selectId);
            preparedStatement.setInt(1, userId.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new User();
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setId(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return user;
    }

    public synchronized void update(User user) {
        String update = "UPDATE " + dbTable + " SET name=? WHERE id=?";
        try {
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(update);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public synchronized void delete(User user) {
        String update = "DELETE  FROM " + dbTable + " WHERE id=?";
        try {
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(update);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public synchronized User authentication(String name1, String password1) {
        User user =null;
        String selectId = "SELECT * FROM " + dbTable + " WHERE (" + name + "=? AND " + password + "=?)";
        try {
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(selectId);
            preparedStatement.setString(1, name1);
            preparedStatement.setString(2, password1);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new User();
                user.setPassword(resultSet.getString("password"));
                user.setId(resultSet.getInt(id));
                user.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return user;
    }
}
