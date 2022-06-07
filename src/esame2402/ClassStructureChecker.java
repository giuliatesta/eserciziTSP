package esame2402;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

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
        } catch (Exception e){
            String s = "\t- there is no public construtor that inizializes the neither STATIC nor FINAL attributes\n\t\t argument types: [ ";
            for(Class<?> p: params) {
                s += p.getTypeName() + " ";
            }
            s += " ]";
            results.addResults(s);
        }
        return this;
    }

    public ClassStructureChecker shouldHaveSetStatic(ValidationResults results) {
        Class<?>[] params = filterFields(clazz.getDeclaredFields(),
                f -> !Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers()));
        try {
            if (params.length != 0) {
                clazz.getMethod("setStatic", params);
                results.addResults("\t- setStatic is OK\n");
            }
        } catch (Exception e) {
            String s = "\t- there is no setStatic method that inizializes all the STATIC but FINAL attributes\n\t\t argument types: [ ";
            for(Class<?> p: params) {
                s += p.getTypeName() + " ";
            }
            s += " ]";
            results.addResults(s);
        }
        return this;
    }

    private Class<?>[] filterFields(Field[] fields, Predicate<Field> condition) {
        List<Class<?>> filteredFields = new ArrayList<>();
        sortInAlphabeticallyOrder(fields);
        for (Field f : fields) {
            if (condition.test(f)) {
                filteredFields.add(f.getType());
            }
        }
        return filteredFields.toArray(new Class<?>[0]);
    }

    private void sortInAlphabeticallyOrder(Field[] declaredFields) {
        Arrays.sort(declaredFields, Comparator.comparing(Field::getName));
    }
}
