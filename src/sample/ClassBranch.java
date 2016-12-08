package sample;

/**
 * Created by vicky on 31/12/14.
 */
public class ClassBranch {
    String classid=null;
    String branch=null;
    int start=0,end=0,sem=0,sliderStart=0,sliderEnd=0;

    ClassBranch(String classid, String branch, int sem, int start, int end,int sliderStart,int sliderEnd){
        this.classid=classid;
        this.branch=branch;
        this.sem=sem;
        this.start=start;
        this.end=end;
        this.sliderEnd=sliderEnd;
        this.sliderStart=sliderStart;
    }

    /*ClassBranch(String classid, String branch, int sem, int start, int end){
        this.classid=classid;
        this.branch=branch;
        this.sem=sem;
        this.start=start;
        this.end=end;
    }*/
}
