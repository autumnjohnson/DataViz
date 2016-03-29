
package dataviz;

import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import java.util.Collection;

/**
 * Created by autumnljohnson on 3/14/16.
 */
public class ParseTree {
    public Tree tree;
    public TreePrint treePrint;
    public Collection<TypedDependency> lexTdl;
    public TreebankLanguagePack tlp;

    public ParseTree(Tree tree) {
        this.tree = tree;
        this.tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory(tlp.punctuationWordRejectFilter());
        GrammaticalStructure lexGs = gsf.newGrammaticalStructure(tree);
        this.treePrint = new TreePrint("penn", "CCPropagatedDependencies", tlp);
        this.lexTdl = lexGs.typedDependenciesCCprocessed(); //exGs.typedDependenciesCollapsedTree();

    }

    public Collection<TypedDependency> getTypedDependencies() {
        return lexTdl;
    }

    public void printTree() {
        treePrint.printTree(tree);
    }

    public TregexMatcher getRegexTree(String regexPattern) {
        TregexPattern patternMW = TregexPattern.compile(regexPattern);
        // Run the pattern on one particular tree
        return patternMW.matcher(tree);

    }

    public void printRegexTree(String regexPattern, String descr) {
        TregexPattern patternMW = TregexPattern.compile(regexPattern);
        // Run the pattern on one particular tree
        TregexMatcher matcher = patternMW.matcher(tree);
        // Iterate over all of the subtrees that matched
        System.out.println();
        System.out.println(descr);
        System.out.println("Regex: " + regexPattern);
        //TregexGUI gui = TregexGUI.getInstance();

        // gui.isMacOSX();
        // TreeReaderFactory treeReader = new TregexPattern.TRegexTreeReaderFactory();
        //gui.getGraphics();

        while (matcher.findNextMatchingNode()) {
            Tree match = matcher.getMatch();
            match.pennPrint();
            System.out.println(match.labels().toString());
            System.out.println("fisrt child: ");
            match.firstChild().pennPrint();

            HeadFinder headFinder = new SemanticHeadFinder(tlp, false);
            System.out.println("preterm");
            match.headPreTerminal(headFinder).pennPrint();
            System.out.println("term");
            match.headTerminal(headFinder).pennPrint();

            if (match.size() > 1) {
                //System.out.println("methc " + match.getNodeNumber(0).label().value());
                Tree[] children = match.children();
                int i = 1;
                for (Tree child : children) {
                    System.out.println("child " + i + " " );
                    child.pennPrint();
                    i++;

                }

            }



            // do what we want to with the subtree
        }

    }
}
