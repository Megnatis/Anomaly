package ir.mehritco.naqizadeh;

import java.io.Serializable;

public class Normalized implements Serializable {
    long id;
    String lem , tree , sen;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLem() {
        return lem;
    }

    public void setLem(String lem) {
        this.lem = lem;
    }

    public String getTree() {
        return tree;
    }

    public void setTree(String tree) {
        this.tree = tree;
    }

    public String getSen() {
        return sen;
    }

    public void setSen(String sen) {
        this.sen = sen;
    }
}
