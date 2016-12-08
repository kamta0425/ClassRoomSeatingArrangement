package sample;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataAccesor {

    // in real life, use a connection pool....
    private Connection connection;

    public DataAccesor(String driverClassName, String dbURL, String user, String password) throws SQLException, ClassNotFoundException {
        Class.forName(driverClassName);
        connection = DriverManager.getConnection(dbURL, user, password);
    }

    public void shutdown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public User detail(String Username, String pass) throws SQLException {
        ResultSet rs;
        User userinfo = null;
        try {
            Statement stmnt = connection.createStatement();
            rs = stmnt.executeQuery("SELECT * FROM user  WHERE username = '" + Username + "'    and password = '" + pass + "' ");
            rs.first();
            userinfo = new User(rs.getString("username"), rs.getString("password"), rs.getString("email"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userinfo;
    }

    public boolean isUserExist(String name, String passwd) {

        boolean ans = false;
        try {
            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM user  WHERE username = '" + name + "'    and password = '" + passwd + "' ");
            if (!rs.next())
                ans = false;
            else
                ans = true;
        } catch (SQLException e) {
            System.out.print(e);
            e.printStackTrace();
        }
        return ans;
    }


    public List<ClassDetail> getclasslist() throws SQLException {
        List<ClassDetail> classess = new ArrayList<>();
        try {
            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery("select * from ClassDetail");
            while (rs.next()) {
                String classname = rs.getString("classid");
                String strenght = rs.getString("strength");
                String collumn = rs.getString("collumn");

                ClassDetail eachclass = new ClassDetail(classname, Integer.parseInt(strenght), Integer.parseInt(collumn));
                classess.add(eachclass);
            }
        }catch (Exception e){}
        return classess;
    }

    ArrayList<ClassBranch> getList(String name) throws SQLException {
        ArrayList<ClassBranch> list = new ArrayList<ClassBranch>();

        Statement stmnt = connection.createStatement();

        ResultSet rs = stmnt.executeQuery("SELECT * FROM classbranch WHERE classid = '" + name + "'");
        while (rs.next()) {
            String classid = rs.getString("classid");
            int start = rs.getInt("start");
            int end = rs.getInt("end");
            int sliderStart = rs.getInt("sliderStart");
            int sliderEnd = rs.getInt("sliderEnd");
            String branch = rs.getString("branch");
            int sem = rs.getInt("semester");
            list.add(new ClassBranch(classid, branch, sem, start, end, sliderStart, sliderEnd));
        }

        return list;
    }

    public ClassDetail getSingle(String name) throws SQLException {
        ClassDetail detail = new ClassDetail();
        Statement stmnt = connection.createStatement();
        ResultSet rs = stmnt.executeQuery("select * from ClassDetail WHERE classid = '" + name + "'");

        if (rs.next()) {
            int strength = rs.getInt("strength");
            int col = rs.getInt("collumn");
            detail.setCollumn(col);
            detail.setStrength(strength);
            detail.setClassname(name);
        }
        return detail;
    }

    public void addClass(ClassDetail cls) {

        try {
            String insertString = "INSERT INTO ClassDetail (classid, strength, collumn) VALUES (?, ?, ?)";
            PreparedStatement stmnt = connection.prepareStatement(insertString);
            stmnt.setString(1, cls.classname);
            stmnt.setInt(2, cls.strength);
            stmnt.setInt(3, cls.collumn);
            stmnt.executeUpdate();
            System.out.println("this is added to the detail");

        } catch (Exception e) {
            System.out.println("Exception in Add ClassDetail");
            e.printStackTrace();
        }
    }

    public void addClass(List<ClassBranch> classBranch) {
        String name = null;
        for (ClassBranch obj : classBranch) {
            switch (obj.branch) {
                case "Architecture":
                    name = "Arch";
                    break;
                case "Information Technology":
                    name = "IT";
                    break;
                case "Mechanical Engineering":
                    name = "Mech";
                    break;
                case "Bio Medical Engineering":
                    name = "BM";
                    break;
                case "Bio Technology":
                    name = "BT";
                    break;
                case "Chemical Enginering":
                    name = "CHE";
                    break;
                case "Civil Engineering":
                    name = "CIVIL";
                    break;
                case "Computetr Science & Engineering":
                    name = "CS";
                    break;
                case "Electrical Engineering":
                    name = "EE";
                    break;
                case "Electronics & Telicom. Engineering":
                    name = "ELEX";
                    break;
                case "Mining":
                    name = "MINING";
                    break;
                case "Metallargical Engineering":
                    name = "META";
                    break;
            }
            try {
                String insertString = "INSERT INTO classbranch (classid, branch, semester,start,end,sliderstart,sliderend) VALUES (? , ? , ? , ? , ? , ? , ? )";
                PreparedStatement stmnt = connection.prepareStatement(insertString);
                System.out.println(obj.classid + " " + obj.branch + " " + obj.sem + " " + obj.start + " " + obj.end);
                stmnt.setString(1, obj.classid);
                stmnt.setString(2, name);
                stmnt.setInt(3, obj.sem);
                stmnt.setInt(4, obj.start);
                stmnt.setInt(5, obj.end);
                stmnt.setInt(6, obj.sliderStart);
                stmnt.setInt(7, obj.sliderEnd);
                stmnt.executeUpdate();
                System.out.println("this is added to the detail");
            } catch (SQLException e) {
                System.out.println("SQLException in DataAccessor addClass(List<ClassBranch> classBranch)"+e);
                //e.printStackTrace();
            }catch (Exception e) {
                System.out.println("Exception in DataAccessor addClass(List<ClassBranch> classBranch)"+e);
                //e.printStackTrace();
            }
        }
    }


    public void adduser(User newuser) {
        try {
            String insertString = "INSERT INTO user (username, password,email) VALUES (?, ? ,?)";
            PreparedStatement stmnt = connection.prepareStatement(insertString);
            stmnt.setString(1, newuser.name);
            stmnt.setString(2, newuser.passwd);
            stmnt.setString(3, newuser.email);
            stmnt.executeUpdate();
            System.out.println("this is added to the detail");

        } catch (Exception e) {
            System.out.println("Exception in adduser function ");
            e.printStackTrace();
        }
    }

    public void deleteclass(String name) throws SQLException {

        String sql = "DELETE FROM ClassDetail WHERE classid=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.executeUpdate();

        String sql2 = "DELETE FROM classbranch WHERE classid=?";
        PreparedStatement pstmt2 = connection.prepareStatement(sql2);
        pstmt2.setString(1, name);
        pstmt2.executeUpdate();

    }

    public String searchDetail(int k) throws SQLException {
        String cd = null;
        Statement stm = connection.createStatement();
        ResultSet rs1 = stm.executeQuery("select * from classbranch where  (start <='" + k + "'  and end >= '" + k + "') OR (sliderstart <='" + k + "'  and sliderend >= '" + k + "')    ");
        if (rs1.next())
            cd = rs1.getString("classid");
        else
            cd = null;
        return cd;
    }
}

// other methods, eg. addPerson(...)