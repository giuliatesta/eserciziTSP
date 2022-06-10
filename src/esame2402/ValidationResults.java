package esame2402;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

/*
    Classe che contiene tutte le stringhe di validazione prodotte dai Checker
 */
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
