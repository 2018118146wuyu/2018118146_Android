package student;

import java.io.Serializable;

public class Student implements Serializable {
    private long id;
    private String adclass;
    private String sno;
    private String name;
    private String sex;
    private String modify_time;

    public Student(){
        super();
    }

    public Student(long id, String adclass, String sno, String name, String sex, String modify_time){
        this.id = id;
        this.adclass = adclass;
        this.sno = sno;
        this.name = name;
        this.sex = sex;
        this.modify_time = modify_time;
    }

    public Student(String adclass, String sno, String name, String sex, String modify_time){
        this.adclass = adclass;
        this.sno = sno;
        this.name = name;
        this.sex = sex;
        this.modify_time = modify_time;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getAdclass() {
        return adclass;
    }
    public void setAdclass(String adclass) {
        this.adclass = adclass;
    }

    public String getSno() {
        return sno;
    }
    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getModify_time() {
        return modify_time;
    }
    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }
}
