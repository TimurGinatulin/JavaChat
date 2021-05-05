package Lesson_7.source.server.database;

public class Main {
    public static void main(String[] args) {
        DBHandler dbHandler = new DBHandler();
        System.out.println(dbHandler.authentication("l1","p1"));
    }

}
