package dataviz;
import com.sun.tools.javac.util.Pair;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.*;

import java.util.*;

/**
 * Created by autumnljohnson 
 */
public class Scatterplot {

    public static final Set<String> IGNORED_WORDS =
            new HashSet<>(Arrays.asList(new String[]{"number", "amount", "count", "percentage"}));

    public String xAxis;
    public String yAxis;
    public String dotValue;
    public boolean ableToCreate;
    public ParseTree tree;


    public Scatterplot(ParseTree tree) {
        this.tree = tree;
        this.ableToCreate = createChart();

    }

    @Override
    public String toString() {
        if (ableToCreate) {
            return "x-axis: " + xAxis + "\ny-axis: " + yAxis + "\n"
                    + "dot-value: " + dotValue + "\n";
        } else {
            //System.out.println(tree.getTypedDependencies().toString());
            return "unable to create";
        }
    }

    public static void dfs(Tree node, Tree parent, HeadFinder headFinder, List<Pair<Tree, List<Tree>>> headAndNPs) {
        if (node == null || node.isLeaf()) {
            return;
        }

        //if node is a NP - Get the terminal nodes to get the words in the NP
        if (node.value().equals("NP")) {
            List<Tree> leaves = node.getLeaves();
            Tree head = node.headTerminal(headFinder, parent);

            if (!IGNORED_WORDS.contains(head.value())) {
                Pair<Tree, List<Tree>> pair = new Pair<>(head, leaves);
                headAndNPs.add(pair);
            }


        }
        for (Tree child : node.children()) {
            dfs(child, node, headFinder, headAndNPs);
        }


    }

    public boolean createChart() {
        SemanticGraph graph = new SemanticGraph(tree.getTypedDependencies());
        SemanticHeadFinder semanticHF = new SemanticHeadFinder(new PennTreebankLanguagePack(), true);
        List<Pair<Tree, List<Tree>>> headAndNPs = new ArrayList<>();

        dfs(tree.tree, null, semanticHF, headAndNPs);

        for (int i = 0; i < headAndNPs.size(); i++) {
            Pair<Tree, List<Tree>> res = headAndNPs.get(i);
            System.out.print(res.fst.value() + " -> ");
            List<Tree> nounPhrase = res.snd;
            for (int z = 0; z < nounPhrase.size(); z++) {
                System.out.print(nounPhrase.get(z) + " ");
            }
            System.out.println();
        }
        if (headAndNPs.size() >= 4) {
            Map<String, Pair<Tree, List<Tree>>> headToNP = new HashMap<>();
            for (int i = 0; i < headAndNPs.size(); i++) {
                Tree h = headAndNPs.get(i).fst;
                List<Tree> np = headAndNPs.get(i).snd;
                headToNP.put(h.value(), new Pair(h, np));
            }
            headAndNPs = new ArrayList<>();
            for (String key : headToNP.keySet()) {
                Pair<Tree, List<Tree>> result = headToNP.get(key);
                headAndNPs.add(new Pair(result.fst, result.snd));
            }
        }

        if (headAndNPs.size() == 3) {

            for (int i = 0; i < headAndNPs.size(); i++) {

                Pair<Tree, List<Tree>> res = headAndNPs.get(i);
                List<Tree> nounPhrase = res.snd;
                String result = "";

                for (int z = 0; z < nounPhrase.size(); z++) {
                    result += nounPhrase.get(z) + " ";
                }
                if (i == 0) {
                    xAxis = result;
                } else if (i == 1) {
                    yAxis = result;
                } else {
                    dotValue = result;
                }
            }
            return true;
        } else {

            return false;
        }
    }
}
