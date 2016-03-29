package dataviz;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.shiftreduce.ShiftReduceParser;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.*;
import java.io.IOException;
import java.util.*;

/**
 * Created by autumnljohnson on 3/5/16.
 */
public class Parser {
    public static final String LEX_PARSER_UD_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
    public static final String NEURAL_NETWORK_UD_MODEL =  "edu/stanford/nlp/models/parser/nndep/english_UD.gz";

    public Parser() {

        /*
        String query = "";
        LexicalizedParser lexParser = LexicalizedParser.loadModel(LEX_PARSER_UD_MODEL);

        Tree tree = lexParser.apply();


        PennTreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory(tlp.punctuationWordRejectFilter());
        GrammaticalStructure lexGs = gsf.newGrammaticalStructure(tree);
        TreePrint treePrint = new TreePrint("penn", "CCPropagatedDependencies", tlp);
        List<TypedDependency> depList = lexGs.typedDependenciesCCprocessed();

        for (TypedDependency dep : depList) {
            */




    }
    // Neural Network parser
    // LEFT-ARC: marks the second item on the stack as a dependent of the first item, and removes the second item from the stack (if the stack contains at least two items).
    // RIGHT-ARC: marks the first item on the stack as a dependent of the second item, and removes the first item from the stack (if the stack contains at least two items).
    // SHIFT: removes a word from the buffer and pushes it onto the stack (if the buffer is not empty).
}
