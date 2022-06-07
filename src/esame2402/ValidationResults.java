package esame2402;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ValidationResults {

    private static final List<String> results = new ArrayList<>();

    boolean hasResults() {return !results.isEmpty();}

    void addResults(String result) {
        results.add(result);}

    void printResults() {
        for(String s : results) {
            System.out.println(s);
        }
    }
    
}
