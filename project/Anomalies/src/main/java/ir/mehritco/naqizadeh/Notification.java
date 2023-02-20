package ir.mehritco.naqizadeh;

import java.io.Serializable;
import java.util.Date;

public class Notification implements Serializable {
    /**
     * Id : it's about organize the data of each row
     */
    private long id;
    /**
     * The title it's about message for resolve some problem
     * The faTitle it's farsi title
     * The jalaliDate it's about the date notif received
     */
    private String title , faTitle , jalaliDate;
    /**
     * it's about convert jalali date into java date to fetch data in date
     */
    private Date notifTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFaTitle() {
        return faTitle;
    }

    public void setFaTitle(String faTitle) {
        this.faTitle = faTitle;
    }

    public String getJalaliDate() {
        return jalaliDate;
    }

    public void setJalaliDate(String jalaliDate) {
        this.jalaliDate = jalaliDate;
    }

    public Date getNotifTime() {
        return notifTime;
    }

    public void setNotifTime(Date notifTime) {
        this.notifTime = notifTime;
    }
}
