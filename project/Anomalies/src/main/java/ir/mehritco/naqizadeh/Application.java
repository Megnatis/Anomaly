package ir.mehritco.naqizadeh;

import ir.mehritco.naqizadeh.rdf.RdfBean;
import org.apache.jena.rdf.model.Model;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Application {
    static Repository repository = new Repository();
    public static String SRC_PATH = "D:\\ProjectSpace\\Applications\\payanname\\";

    public static void main(String... arg) throws IOException {
        //Insert Log4J prop
        String log4jConfPath = SRC_PATH+"project\\Anomalies\\log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        /*
         * Pool Gathering Data
         * Step one : Get Raw Data From BPMS
         */
        Anomalies anomalies = new Anomalies();
        ArrayList<Notification> rawData = anomalies.getRawData();
        System.out.println("Pool Gathering Data -> Step one : Get Raw Data From BPMS -> COMPLETED");
        System.out.println("=====================================================================");
        /*
         * Pool Gathering Data
         * Step Two : Insert Into DataBase(Mysql , Sql Server , Sqlite or etc...)
         */
        createRawData(rawData);
        System.out.println("Pool Gathering Data -> Step Two : Insert Into Repository(Sqlite/Mysql) -> COMPLETED");
        System.out.println("=====================================================================");

        /*
         * Pool Categorization Data
         * Step One : Categorization With Nlp
         */
        Nlp nlp = new Nlp();
        ArrayList<Normalized> normalizeds = new ArrayList<Normalized>();
      //  Turn off After create Table. Using existing data from table.
        for (Notification sen : rawData) {
            String[] norm = nlp.normalizeWords(sen.getTitle());
            Normalized normalized = new Normalized();
            normalized.setId(sen.getId());
            normalized.setSen(sen.getTitle());
            normalized.setLem(norm[0]);
            normalized.setTree(norm[1]);
            normalizeds.add(normalized);
        }
        //Store normalized data into repository
        insertNormalizedDataFromNlp(normalizeds);

        System.out.println("Pool Categorization Data -> Step One : Modification and Normalized Title With NLP -> COMPLETED");
        System.out.println("=====================================================================");

        normalizeds = getNormalizedFromDatabase();
        ArrayList<Normalized> categorized = nlp.analyzedForCategorized(normalizeds);
        insertCategorizedData(categorized);
        System.out.println("Pool Categorization Data -> Step Two : Categorize with NLP -> COMPLETED");
        System.out.println("=====================================================================");


        /*
        From csv we get RDF of work flow with bpmn architect
         */
        Rdf rdf = new Rdf();
        Model rdfModel = null;
            rdfModel = rdf.csvToRdf(SRC_PATH+"Repository\\RDF.csv");
        System.out.println("=======================================================\nprint RDF MODEL Into File. \n");
        FileWriter rdfIntoFile = new FileWriter(SRC_PATH+"Repository\\RdfIntoFile.txt");
        rdfModel.write(rdfIntoFile);
        System.out.println("=======================================================");
        //String predicate = "j.0:Read";
        String predicate = "?predicate";
        String query = """
                PREFIX WorkFlow: <https://workflow.hedc.co.ir/workflow#>
                PREFIX j.0: <https://workflow.hedc.co.ir/workflow/>
                SELECT *
                WHERE {
                    ?subject %s ?object 
                     FILTER(LANG(?object) = "" || LANGMATCHES(LANG(?object), "en"))
                }
                """.formatted(predicate);
        System.out.println("We going to Find Somethings .");

        ArrayList<RdfBean> findRdf =  rdf.findInRdf(rdfModel , query , predicate);
        System.out.println("Find For Predicate :" + predicate);
        for (RdfBean bean:findRdf) {
            /*
             * Pool Categorization Data
             * Step Two : Categorization With Rdf
             */
            nlp.communication(categorized ,bean);

        }
        System.out.println("Are We Found somethings?");


    }

    private static void createRawData(ArrayList<Notification> rawData){
        try {
            repository.storeRawData(rawData);
            System.out.println("We added raw data into database");
        }catch (Exception e){
            System.err.println("We have error/Ex insert data to database : " + e.getMessage());
        }
    }

    private static void insertNormalizedDataFromNlp(ArrayList<Normalized> normalizeds){
        try {
            repository.storeNormalized(normalizeds);
            System.out.println("We added Normalized data From NLP into database");
        }catch (Exception e){
            System.err.println("We have error/Ex insert data to database For Normalized NLP : " + e.getMessage());
        }
    }
    private static ArrayList<Normalized> getNormalizedFromDatabase(){
        ArrayList<Normalized> normalizeds = new ArrayList<Normalized>();
        try{
            normalizeds = repository.getNormalized();
        }catch (Exception e){
            System.err.println("We have error/Ex get data from database For Normalized NLP : " + e.getMessage());
        }
        return normalizeds;
    }

    private static void insertCategorizedData(ArrayList<Normalized> normalizeds){
        try {
            repository.storeCategorized(normalizeds);
            System.out.println("We added Categorized data From NLP into database");
        }catch (Exception e){
            System.err.println("We have error/Ex insert data to database For Categorized : " + e.getMessage());
        }
    }

}
