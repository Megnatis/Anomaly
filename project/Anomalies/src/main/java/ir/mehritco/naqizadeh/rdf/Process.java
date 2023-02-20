package ir.mehritco.naqizadeh.rdf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class Process {
    private Model model ;
    private Resource insertionResource;
    private Property insertProperty ;
    public Process(Model model){
        this.model = model;
        insertionResource = model.createResource();
        insertProperty = model.createProperty("inserting");
    }
    public Process(Resource insertionResource,Model m){
        this.model = model;
        this.insertionResource  = insertionResource;
    }
    public void addInsertion(String dataInsert){
        insertionResource.addProperty(insertProperty , dataInsert);
    }

    public Resource getInsertionResource() {
        return insertionResource;
    }

    public Property getInsertProperty() {
        return insertProperty;
    }
}
