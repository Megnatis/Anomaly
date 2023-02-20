package ir.mehritco.naqizadeh;


import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import ir.mehritco.naqizadeh.rdf.RdfBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


public class Nlp {
    Properties props = new Properties();
    StanfordCoreNLP pipeline;

    public Nlp() {
        props.setProperty("coref.algorithm", "neural");
        props.setProperty("use_gpu", "true");
        props.setProperty("annotators", "tokenize,pos,lemma,ner,parse");
        pipeline = new StanfordCoreNLP(props);
    }

    public String[] normalizeWords(String sen) {
        CoreDocument document = pipeline.processToCoreDocument(sen);
        String lemSen = "";
        for (CoreLabel tok : document.tokens()) {
            lemSen += " " + tok.lemma();
        }
        Annotation annotation =
                new Annotation(sen);
        pipeline.annotate(annotation);
        Tree tree =
                annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0).get(TreeCoreAnnotations.TreeAnnotation.class);

        return new String[]{lemSen, tree.toString()};
    }


    public ArrayList<Normalized> analyzedForCategorized(ArrayList<Normalized> normalizeds) {
        HashMap<String, Normalized> categorized = new HashMap<String, Normalized>();
        for (Normalized nor : normalizeds) {
            String tree = nor.getTree();
            if (tree.contains("ADJP") || tree.contains("VB") || tree.contains("JJ")) {
                categorized.put(nor.getLem(), nor);
            }
        }
        System.out.println("Categorized data into : " + categorized.size());
        return new ArrayList<Normalized>(categorized.values());
    }

    public void communication(ArrayList<Normalized> categorized, RdfBean rdfBean) {
       // System.out.println("Analyze bean found Object : " + rdfBean.getObject()
         //       + " | that subject is : " + rdfBean.getSubject()+" |Predicate is : " + rdfBean.getPredicate());
        for (Normalized nor : categorized) {
            Tree tree = Tree.valueOf(nor.getTree());
            Tree vp = null;
            Tree nn = null;
            String verb = "";
            String about = "";

            vp = find(tree, "VP");
            if(vp != null) {
                verb = vp.yield().get(0).value();
                nn = find(vp, "NN");
                if(nn != null) {
                    about = nn.yield().get(0).value();
                }
            }

            if(!verb.isEmpty() && !about.isEmpty()) {
                String object = rdfBean.getObject();
                if(object.contains(about)){
                    System.err.println("Anomalies About :" + about
                    +"\tobject is : " + object + "\tWorkflow is: " + rdfBean.getSubject()
                            +"\tَActivity Cause Happen  This : " + rdfBean.getPredicate()
                    );
                }



              //  System.err.println("Find Values of vp : " + verb + "-" + about);
            }
        }
    }

    public Tree find(Tree tree, String lable) {
        for (Tree subTree : tree) {
            if (subTree.label().value().equals(lable)) {
                return subTree;
            }
        }
        return null;
    }

}
