package sample;

//import com.sun.org.apache.xpath.internal.operations.String;

/**
 * Created by vicky on 31/12/14.
 */
public class ClassDetail {
    String classname;
    int strength;
    int collumn;

    ClassDetail(){}
    public ClassDetail(String classname, int strength, int collumn) {

        this.classname = classname;
        this.strength = strength;
        this.collumn = collumn;

    }

    public String getClassname() {
        return classname;
    }

    public int getStrength() {
        return strength;
    }

    public int getCollumn() {
        return collumn;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setCollumn(int collumn) {
        this.collumn = collumn;
    }
}
