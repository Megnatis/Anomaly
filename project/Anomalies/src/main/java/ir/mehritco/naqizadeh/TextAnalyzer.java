package ir.mehritco.naqizadeh;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.apache.log4j.PropertyConfigurator;

import java.util.List;
import java.util.Properties;

public class TextAnalyzer {
    public static String text = """
            در ابتدا، زندگی‌نامه‌ها به عنوان یک بخش از تاریخ با تمرکز بر یک فرد خاص، با اهمیت تاریخی در نظر گرفته شد. انواع مستقل زندگی‌نامه‌نویسی با تمایز از تاریخ عمومی از قرن ۱۸ ام شروع شده و فرم‌های معاصر آن به قرن بیستم می‌رسد.
            """;

    public static void main(String[] args) {
        //Insert Log4J prop
        String log4jConfPath = "log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        //props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote");
        props.setProperty("coref.algorithm", "neural");
        //props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote");
        //props.setProperty("processors", "tokenize,mwt,pos,lemma,depparse");

        props.setProperty("processors", "tokenize, mwt, lemma, pos, depparse");
//        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse");
        props.setProperty("annotators", "tokenize, ssplit, parse");
        props.setProperty("lang", "fa");
        props.setProperty("use_gpu", "true");
        props.setProperty("tokenize_model_path", "C:\\Users\\Megnatis\\Desktop\\fa_seraji_models\\" +"fa_seraji_tokenizer.pt");
        props.setProperty("mwt_model_path",  "C:\\Users\\Megnatis\\Desktop\\fa_seraji_models\\" +"fa_seraji_mwt_expander.pt");
        props.setProperty("pos_model_path", "C:\\Users\\Megnatis\\Desktop\\fa_seraji_models\\" +"fa_seraji_tagger.pt");
        props.setProperty("pos_pretrain_path", "C:\\Users\\Megnatis\\Desktop\\fa_seraji_models\\" +"fa_seraji.pretrain.pt");
        props.setProperty("lemma_model_path", "C:\\Users\\Megnatis\\Desktop\\fa_seraji_models\\" +"fa_seraji_lemmatizer.pt");
        props.setProperty("depparse_model_path", "C:\\Users\\Megnatis\\Desktop\\fa_seraji_models\\" +"fa_seraji_parser.pt");
        props.setProperty("depparse_pretrain_path", "C:\\Users\\Megnatis\\Desktop\\fa_seraji_models\\" +"fa_seraji.pretrain.pt");

        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        // create a document object
        // CoreDocument document = new CoreDocument(text);

        Annotation document = new Annotation(text);
        // run all Annotators on this text
        pipeline.annotate(document);

        // annnotate the document
        //pipeline.annotate(document);
        // examples

        // 10th token of the document
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            // Get the parse tree for each sentence
            Tree parseTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            // Do something interesting with the parse tree!
            System.out.println(parseTree);
        }





    }

}
