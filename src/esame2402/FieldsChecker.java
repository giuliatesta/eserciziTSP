package esame2402;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Checker per i field della classe
 */
class FieldsChecker {

    private final Class<?> clazz;
    private final ValidationResults results;

    private FieldsChecker(final Class<?> pClazz) {
        clazz = pClazz;
        results = new ValidationResults();
    }

    static FieldsChecker create(final Class<?> clazz) {
        return new FieldsChecker(clazz);
    }

    public ValidationResults getResults() {
        return results;
    }

    /*
        Controlla che tutti i campi FINAL siano anche PUBLIC e STATIC
     */
    public FieldsChecker checkFinalFieldsShouldBePublicAndStatic() {
        Arrays.stream(clazz.getDeclaredFields())
              .forEach(f -> this.ifFieldIsFinalShouldBePublicAndStatic(f));
        return this;
    }

    /*
        Controlla che tutti i campi non FINAL siano PRIVATE e che abbiano GETTER e SETTER
     */
    public FieldsChecker checkNonFinalFieldsShouldBePrivateAndHaveGetterAndSetter() {
        Arrays.stream(clazz.getDeclaredFields())
              .filter(f -> !f.getName().startsWith("test_const"))
              .forEach(f -> this.ifFieldIsNotFinalShouldBePrivate(f)
                                .ifFieldNotFinalShouldHaveGetter(f)
                                .ifFieldNotFinalShouldHaveSetter(f));
        return this;
    }


    /*
        Controlla che il singolo campo FINAL, sia PUBLIC e STATIC
     */
    private FieldsChecker ifFieldIsFinalShouldBePublicAndStatic(Field f) {
        int modifiers = f.getModifiers();
        if (Modifier.isFinal(modifiers)) {
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                results.addResults("The Field " + f.getName().toUpperCase() + " is OK");
            } else {
                if (Modifier.isPublic(modifiers)) {
                    results.addResults("The Field " + f.getName().toUpperCase() + " must be declared as STATIC");
                } else if (Modifier.isStatic(modifiers)) {
                    results.addResults("The Field " + f.getName().toUpperCase() + " must be declared as PUBLIC");
                } else {
                    results.addResults("The Field " + f.getName().toUpperCase() + " must be declared as PUBLIC and STATIC");
                }
            }
        }
        return this;
    }

    /*
        Controlla che il singolo campo non FINAL sia PRIVATE
     */
    private FieldsChecker ifFieldIsNotFinalShouldBePrivate(Field field) {
        int modifiers = field.getModifiers();
        if (!Modifier.isFinal(modifiers)) {
            if (Modifier.isPrivate(modifiers)) {
                results.addResults("The Field " + field.getName().toUpperCase() + " is correctly declared as PRIVATE");
            } else {
                results.addResults("The Field " + field.getName().toUpperCase() + " must be declared as PRIVATE");
            }
        }
        return this;
    }

    /*
        Controlla che il singolo campo non FINAL abbia un GETTER
     */
    private FieldsChecker ifFieldNotFinalShouldHaveGetter(Field f) {
        // Deve avere il nome "get+nome_attributo" e non deve avere parametri
        Method get = findMethod(clazz.getDeclaredMethods(),
                                m -> ("get" + f.getName()).equals(m.getName())
                                        && m.getParameterTypes().length==0);

        // se viene restituito qualcosa non NULL, allora il getter esiste
        if (get != null) {
            results.addResults("\t- the getter is get" + f.getName());
            // deve restituire un valore dello stesso tipo del field passato come parametro del metodo
            if (!get.getReturnType().equals(f.getType())) {
                results.addResults("\t\t- it must return " + f.getType().getSimpleName() + " instead of "
                        + get.getReturnType().getSimpleName());
            }

            // Se il field è STATIC, anche il getter deve esserlo
            if (Modifier.isStatic(f.getModifiers()) && !Modifier.isStatic(get.getModifiers())) {
                results.addResults("\t\t- it must be declared as STATIC");
            }
        } else {
            results.addResults("\t- the getter is missing");
        }
        return this;
    }
    /*
           Controlla che il singolo campo non FINAL abbia un SETTER
        */
    private FieldsChecker ifFieldNotFinalShouldHaveSetter(Field f) {
        // Deve avere il nome giusto "set+nome_attributo", avere un solo parametro e
        // il parametro deve essere del tipo uguale a quello del field passato come parametro del metodo
        Method set = findMethod(clazz.getDeclaredMethods(), m -> ("set" + f.getName()).equals(m.getName())
                && m.getParameterTypes().length != 0
                && m.getParameterTypes()[0].equals(f.getType()));

        // se viene restituito qualcosa non NULL, allora il setter esiste
        if (set != null) {
            results.addResults("\t- the setter is set" + f.getName());
            // Se il field è STATIC, anche il setter deve esserlo
            if (Modifier.isStatic(f.getModifiers()) && !Modifier.isStatic(set.getModifiers())) {
                results.addResults("\t\t- it must be declared as STATIC");
            }
        } else {
            results.addResults("\t- the setter is missing");
        }

        return this;
    }

    /*
        Trova il primo metodo tra tutti quelli dichiarati nella classe che rispecchi la condizione
        Se non lo trova, restituisce NULL
     */
    private Method findMethod(Method[] methods, Predicate<Method> condition) {
        return Arrays.stream(methods).filter(condition).findFirst().orElse(null);
    }


}
