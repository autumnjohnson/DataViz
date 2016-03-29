package dataviz;

import edu.stanford.nlp.trees.*;

import java.util.*;

/**
 * Created by autumnljohnson on 3/14/16.
 */
public class BarGraph {
    public String xAxis;
    public String yAxis;
    public boolean ableToCreate;
    public ParseTree tree;


    public BarGraph(ParseTree tree) {
        this.tree = tree;
        this.ableToCreate = createChart();

    }

    @Override
    public String toString() {
        if (ableToCreate) {
            return "x-axis (bar_kind): kind_of(" + xAxis + ")\ny-axis: " + yAxis + "\n"
             + "bar-height = count_of(" + yAxis + ") of the respective bar_kind\n";
        } else {
            return "unable to create";
        }
    }

    public boolean createChart() {
        Collection<TypedDependency> depList = tree.getTypedDependencies();
        List<TypedDependency> nmodList = new ArrayList<>();
        Set<String> sliceKinds = new HashSet<>();
        Set<String> sliceValues = new HashSet<>();
        Map<String, String> compoundsOrAmod = new HashMap<>();

        for (TypedDependency dep : depList) {

            String relation = dep.reln().toString().split("\\(")[0];
            String governingText = dep.gov().originalText();
            String dependentText = dep.dep().originalText();

            if (relation.equals("compound") || relation.equals("amod")) {
                compoundsOrAmod.put(governingText, dependentText + " " + governingText);
            } else if (relation.equals("case")) {
                sliceValues.add(governingText);
            } else if (relation.equals("nmod:by") || relation.equals("nmod:per") || relation.equals("nmod:versus")) {
                sliceKinds.add(dependentText);
            } else if (relation.equals("dep")) {
                sliceKinds.add(governingText);
            } else if (relation.split(":")[0].equals("conj")) {
                sliceKinds.add(governingText);
            }

            if (relation.split(":")[0].equals("nmod")) {
                nmodList.add(dep);
            }
        }

        sliceValues.removeAll(sliceKinds);
        if (sliceValues.size() == 0) {
            List<TypedDependency> depn = (List<TypedDependency>) tree.getTypedDependencies();
            sliceValues.add(depn.get(0).dep().originalText());
        }
        if (sliceKinds.size() == 0) {
            for (TypedDependency nmodDep : nmodList) {
                if (sliceValues.contains(nmodDep.dep().originalText())
                        && sliceValues.contains(nmodDep.gov().originalText())) {
                    sliceValues.remove(nmodDep.dep().originalText());
                    sliceKinds.add(nmodDep.dep().originalText());
                }
            }
        }
        for (String key : compoundsOrAmod.keySet()) {
            if (sliceValues.contains(key)) {
                sliceValues.remove(key);
                sliceValues.add(compoundsOrAmod.get(key));
            } else if (sliceKinds.contains(key)) {
                sliceKinds.remove(key);
                sliceKinds.add(compoundsOrAmod.get(key));
            }
        }
        if (sliceValues.size() != 1 || sliceKinds.size() != 1) {
            System.out.println(tree.getTypedDependencies().toString());
            System.out.println(sliceValues.toString());
            System.out.println(sliceKinds.toString());
            return false;
        } else {
            yAxis = sliceValues.toArray()[0].toString();
            xAxis = sliceKinds.toArray()[0].toString();
            return true;
        }

    }
}
