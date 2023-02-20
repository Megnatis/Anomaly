package ir.mehritco.naqizadeh;

import ir.mehritco.naqizadeh.rdf.Process;
import ir.mehritco.naqizadeh.rdf.RdfBean;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.VCARD;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Rdf {
@Deprecated
    public Model csvToRdfModel(String pathOfCsv) {
        Model model = ModelFactory.createDefaultModel();
        String companyDomain = "https://workflow.hedc.co.ir";
        model.setNsPrefix("Hedc", companyDomain);
        model.setNsPrefix("WorkFlow", companyDomain + "/workflow#");

        try {
            File myObj = new File(pathOfCsv);
            Scanner myReader = new Scanner(myObj);
            int rows = 0;

            Resource resource = null;
            resource = model.createResource(companyDomain);
            while (myReader.hasNextLine()) {
                if (rows++ == 0) {
                    myReader.nextLine();
                    continue;
                }
                rows++;
                String data = myReader.nextLine();
                try {
                    String[] col = data.split(",");
                    String subject = col[0].trim();
                    String name = col[1].trim();
                    String faData = col[2].trim();
                    String enData = col[3].trim();
                    boolean literal = Boolean.parseBoolean(col[4].trim());


                    Property property = null;

                    String paths[] = subject.replace(companyDomain, "").split("/");
                    String path = companyDomain + "";

//                    resource = model.getResource(path);
                    for (int i = 0; i < paths.length; i++) {
                        if (i == 0) {
                            path += paths[i];
                        } else {
                            path += "/" + paths[i];
                        }
                        if (!path.equalsIgnoreCase(companyDomain)) {
                            if (i + 1 < paths.length) {
                                property = model.createProperty(path);
                                Resource resourceInner;
                                if (!model.getResource(path).getURI().equalsIgnoreCase(path)) {
                                    resourceInner = model.createResource(path);
                                } else {
                                    resourceInner = model.getResource(path);
                                }
                                resource.addProperty(property, resourceInner);
                                resource = resourceInner;

                            } else {
                                if (paths.length > 0) {
                                    property = model.createProperty(path);
                                    Resource resourceInner;
                                    if (!model.getResource(path).getURI().equalsIgnoreCase(path)) {
                                        resourceInner = model.createResource(path);
                                    } else {
                                        resourceInner = model.getResource(path);
                                    }
                                    resource.addProperty(property, resourceInner);
                                    resource = resourceInner;
                                }
                            }
                        }
                        if (property != null) {
                            if (!literal) {
                                resource.addProperty(property, faData, "fa").addProperty(property, enData, "en");
                            } else {
                                resource.addLiteral(property,
                                                model.createLiteral(faData, "fa")).
                                        addLiteral(property,
                                                model.createLiteral(enData, "en"));
                            }
                        }
                    }


                } catch (Exception e) {
                    System.err.println("We can't add one Row from CSV : " + rows + "->" + e.getMessage());
                }
                resource = model.createResource(companyDomain);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return model;

    }

    public Model csvToRdf(String pathOfCsv) {
        Model model = ModelFactory.createDefaultModel();
        String companyDomain = "https://workflow.hedc.co.ir/";
        model.setNsPrefix("Hedc", companyDomain);
        model.setNsPrefix("WorkFlow", companyDomain + "workflow#");

        try {
            File myObj = new File(pathOfCsv);
            Scanner myReader = new Scanner(myObj);
            int rows = 0;

            model.createResource(companyDomain);
            while (myReader.hasNextLine()) {
                if (rows++ == 0) {
                    myReader.nextLine();
                    continue;
                }
                rows++;
                String data = myReader.nextLine();
                try {
                    String[] col = data.split(",");
                    String subject = col[0].trim();
                    String activity = col[1].trim();
                    String faData = col[2].trim();
                    String enData = col[3].trim();

                    Property property = null;
                    Literal literalFa = model.createLiteral(faData , "fa");
                    Literal literalEn = model.createLiteral(enData , "en");

                if(activity.isEmpty()){
                    property = model.createProperty(subject);
                    //Resource
                    model.createResource(subject).addLiteral(property , literalFa).addLiteral(property , literalEn);
                }else{
                    //Resource with property

                    String uri = subject.split("#")[0]+"/"+activity;
                    property = model.createProperty(uri);
                    if(!model.getResource(subject).isResource()){
                        //if no resource find
                        model.getResource(subject).addProperty(property,activity).addLiteral(property,literalFa).addLiteral(property,literalEn);
                    }else{
                        //if resource find
                        model.createResource(subject).addProperty(property,activity).addLiteral(property,literalFa).addLiteral(property,literalEn);

                    }
                }



                } catch (Exception ex) {

                }
            }
        } catch (Exception e) {
        }
        return model;
    }

    public ArrayList<RdfBean> findInRdf(Model model, String queryString ,String predicate) {
        ArrayList<RdfBean> findStatement = new ArrayList<RdfBean>();
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(
                query, model);
        ResultSet rs = qexec.execSelect();
        while (rs.hasNext()) {
            // Get Result
            QuerySolution qs = rs.next();
            // Get Variable Names
            Iterator<String> itVars = qs.varNames();

            RdfBean bean = new RdfBean();

            // Display Result
            while (itVars.hasNext()) {
                String szVar = itVars.next().toString().trim().toLowerCase();
                String szVal = qs.get(szVar).toString().trim().toLowerCase().split("@")[0];
                if(szVar.equalsIgnoreCase("object")){
                    bean.setObject(szVal);
                } else if (szVar.equalsIgnoreCase("subject")) {
                    bean.setSubject(szVal);
                }else if (szVar.equalsIgnoreCase("predicate")) {
                    bean.setPredicate(szVal);
                }
                findStatement.add(bean);
            }
        }
        return findStatement;
    }

}
