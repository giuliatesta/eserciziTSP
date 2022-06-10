package esame2402;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Checker per la struttura della classe
 */
class ClassStructureChecker {

    private final Class<?> clazz;

    private ClassStructureChecker(final Class<?> pClazz) {
        clazz = pClazz;
    }

    static ClassStructureChecker create(final Class<?> clazz) {
        return new ClassStructureChecker(clazz);
    }


    public ClassStructureChecker shouldHaveEmptyConstructor(ValidationResults results) {
        results.addResults("CLASS: " + clazz.getName());
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

    public ClassStructureChecker shouldHaveAllArgumentsConstructor(ValidationResults results) {
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

    public ClassStructureChecker shouldHaveSetStatic(ValidationResults results) {
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

    private Class<?>[] filterFields(Field[] fields, Predicate<Field> condition) {
        return Arrays.stream(fields)
                .sorted(Comparator.comparing(Field::getName))
                .filter(condition)
                .map(Field::getType)
                .toArray(size -> new Class<?>[size]);

    }
}
