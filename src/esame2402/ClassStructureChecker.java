package esame2402;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Checker per la struttura della classe
 */
class ClassStructureChecker {

    private final Class<?> clazz;
    private final ValidationResults results;

    private ClassStructureChecker(final Class<?> pClazz) {
        clazz = pClazz;
        results = new ValidationResults();
    }

    static ClassStructureChecker create(final Class<?> clazz) {
        return new ClassStructureChecker(clazz);
    }

    /*
        Controlla che ci sia un costruttore vuoto.
        Se fallisce la chiamata di clazz.getDeclaredConstructor(), allor significa che non c'è
     */
    public ClassStructureChecker shouldHaveEmptyConstructor() {
        try {
            if (Modifier.isPublic(clazz.getModifiers())) {
                clazz.getDeclaredConstructor();
                results.addResults("\t- the public empty constructor is declared");
            }
        } catch (Exception e) {
            results.addResults("\t- the public empty constructor is missing");
        }
        return this;
    }

    /*
        Controlla che ci sia il costruttore con l'elenco di parametri corretti
        Se fallisce la chiamata di clazz.getDeclaredConstructor(params), allora significa che non c'è
     */
    public ClassStructureChecker shouldHaveAllArgumentsConstructor() {
        Class<?>[] params = filterFields(clazz.getDeclaredFields(),
                f -> !Modifier.isFinal(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()));
        try {
            clazz.getDeclaredConstructor(params);
            results.addResults("\t- a constructor with the right arguments types is declared");
        } catch (Exception e){
            results.addResults("\t- there is no public construtor that inizializes the neither STATIC nor FINAL attributes\n\t\t argument types: [ " +
                            Arrays.stream(params).map(Class:: getTypeName).collect(Collectors.joining(", ")) + " ]");
        }
        return this;
    }

    /*
        Controlla che ci sia il metodo setStatic con l'elenco di parametri corretti
        Se fallisce la chiamata di clazz.getMethod(), allora significa che non c'è
     */
    public ClassStructureChecker shouldHaveSetStatic() {
        Class<?>[] params = filterFields(clazz.getDeclaredFields(),
                f -> !Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers()));
        try {
            if (params.length != 0) {
                clazz.getMethod("setStatic", params);
                results.addResults("\t- setStatic is OK");
            }
        } catch (Exception e) {
            results.addResults("\t- there is no setStatic method that inizializes the neither STATIC nor FINAL attributes\n\t\t argument types: [ " +
                    Arrays.stream(params).map(Class:: getTypeName).collect(Collectors.joining(", ")) + " ]");
        }
        return this;
    }

    /*
        Filtra i fields della classe in base alla condizione e li restituisce in ordine alfabetico
     */
    private Class<?>[] filterFields(Field[] fields, Predicate<Field> condition) {
        return Arrays.stream(fields)
                .sorted(Comparator.comparing(Field::getName))
                .filter(condition)
                .map(Field::getType)
                .toArray(size -> new Class<?>[size]);

    }
}
