package dataviz;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.CoreNLPProtos;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.Tree;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

public class Main {
    public static final String NEURAL_NETWORK_UD_MODEL = "edu/stanford/nlp/models/parser/nndep/english_UD.gz";
    public static final String LEX_PARSER_UD_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
    public static final String TAGGER = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";


    public static void main(String[] args) throws IOException {
        //LexicalizedParser lexParser = LexicalizedParser.loadModel(LEX_PARSER_UD_MODEL);
            String wnhome = System.getenv("WNHOME");
             String path = wnhome + File.separator + "dict";
           URL url = null;
             try {
                 url = new URL("file", null, path);
             } catch(MalformedURLException e){ e.printStackTrace();
             }
             if(url == null) {
               System.out.println("null");
             }
        System.out.println(url);

            // construct the dictionary object and open it
            IDictionary dict = new Dictionary(url);
            dict.open();

           // look up first sense of the word "dog"
           IIndexWord idxWord = dict.getIndexWord("dog", POS.NOUN);
           IWordID wordID = idxWord.getWordIDs().get(0);
           IWord word = dict.getWord(wordID);
        System.out.println("Id = " + wordID);
           System.out.println("Lemma = " + word.getLemma());
           System.out.println("Gloss = " + word.getSynset().getGloss());
        //JWNL.initialize();
       // JWNL.initialize(new FileInputStream("file_properties.xml"));
        /*
        IndexWord indexWord = proc.lookupBaseForm(POS.VERB,"bear");
        int senseNum = 0;
        for(Synset synset: indexWord.getSenses()){
            senseNum++;
            System.out.println("For sense: " + senseNum + " (" + synset.getGloss()+")");
            Word[] words = synset.getWords();
            for(Word word: words){
                System.out.println("\t"+word.getLemma()+"("+word.getPOS()+")");
            }
        }
        */
        //Parser parse = new Parser();
//        String text = "The number of GitHub repositories by programming language";
/*
        port(9090);

        // set this to the location of you html directory
        staticFileLocation("cse401/finalproject/");

        post("/parser", (req, res) -> {
                    String msg = req.body().toLowerCase();
                    String[] arr = msg.split(":::");
                    if (arr == null || arr.length < 2) {
                        return null;
                    }
                    String myres = parseText(arr[0], arr[1], lexParser);
                    //System.out.println(msg); // log message
                    return myres;
                }
        );


*/
    }

    public static String parseText(String vizType, String text, LexicalizedParser lexParser) {
        text = text.toLowerCase();
        DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
        for (List<HasWord> sentence : tokenizer) {


            Tree tree = lexParser.apply(sentence);

           ParseTree parseTree = new ParseTree(tree);
            if (vizType.equals("pie")) {
                PieChart pie = new PieChart(parseTree);
                return pie.toString();
            } else if (vizType.equals("bar")) {
                BarGraph bar = new BarGraph(parseTree);
                return bar.toString();
            } else if (vizType.equals("scatter")) {
                Scatterplot sc = new Scatterplot(parseTree);
                return sc.toString();
            }
//            else if (vizType.equals("directed"))

        }
        return null;
    }

    public static String getTextFromStream(InputStream inputStream) throws IOException {

        BufferedReader fileCheck;
        fileCheck = new BufferedReader(new InputStreamReader(inputStream));
        String fileText = "";
        String thing;
        thing = fileCheck.readLine();
        while (thing != null) {
            fileText += thing+"\n";
            thing = fileCheck.readLine();
        }
        try {
            fileCheck.close();
        } catch (IOException e) {
            // doesn't matter.
        }
        return fileText;
    }

}
