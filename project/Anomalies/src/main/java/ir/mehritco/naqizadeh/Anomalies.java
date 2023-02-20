package ir.mehritco.naqizadeh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class Anomalies {
    /**
     * convert Notification data into raw data as arrayList
     * that can more read able data to analyzed , we can
     * get from sources or repository like : SQLite , SQL Server,
     * Oracle DataBase , Xml and etc ...
     */
    private ArrayList<Notification> rawData = new ArrayList<Notification>();

    /**
     * get data from repository
     * @return array list of raw data.
     */
    public ArrayList<Notification> getRawData() {
        System.out.println("Starting get Raw data.");
        try {
            Scanner reader = new Scanner(new File(Application.SRC_PATH+"ican\\export_ican_3895_translated_and_dateAdded.csv"));
            rawData = new ArrayList<Notification>();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
            while (reader.hasNext()){
                String line  = reader.nextLine();
                String rowData[] = line.split(",");
                Notification notif = new Notification();
                notif.setId(Long.parseLong(rowData[0].replaceAll("[^0-9]+", "")));
                notif.setTitle(rowData[1]);
                notif.setFaTitle(rowData[2]);
                notif.setNotifTime(formatter.parse(rowData[3]));
                notif.setJalaliDate(rowData[4]);

                rawData.add(notif);
            }
            System.out.format("Finished Get total %s Raw Data.\n",rawData.size());
        }catch (Exception ex){
            System.err.println("Can't get raw Data from getRawData Method :" + ex.getMessage());
        }
        return rawData;
    }
}
