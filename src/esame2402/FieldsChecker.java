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

    private FieldsChecker(final Class<?> pClazz) {
        clazz = pClazz;
    }

    static FieldsChecker create(final Class<?> clazz) {
        return new FieldsChecker(clazz);
    }

    public FieldsChecker checkFields(ValidationResults results) {
        for(Field f: clazz.getDeclaredFields()) {
            this
            .ifFieldIsFinalShouldBePublicAndStatic(f, results)
            .ifFieldIsNotFinalShouldBePrivate(f, results);
            if(!f.getName().startsWith("test_const")) {
                this
                    .ifFieldNotFinalShouldHaveGetter(f, results)
                    .ifFieldNotFinalShouldHaveSetter(f, results);
            }
        }
        return this;
    }
    public FieldsChecker ifFieldIsFinalShouldBePublicAndStatic(Field f, ValidationResults results) {
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

    public FieldsChecker ifFieldIsNotFinalShouldBePrivate(Field field, ValidationResults results) {
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

    public FieldsChecker ifFieldNotFinalShouldHaveGetter(Field f, ValidationResults results) {
        Method get = findMethod(clazz.getDeclaredMethods(), m -> ("get" + f.getName()).equals(m.getName()));

        if (get != null) {
            results.addResults("\t- the getter is get" + f.getName());
            if (!get.getReturnType().equals(f.getType())) {
                results.addResults("\t\t- it must return " + f.getType().getSimpleName() + " instead of "
                        + get.getReturnType().getSimpleName());
            }

            if(Modifier.isStatic(f.getModifiers())) {
                results.addResults("\t\t- it must be declared as STATIC");
            }
        } else {
            results.addResults("\t- the getter is missing");
        }
        return this;
    }

    public FieldsChecker ifFieldNotFinalShouldHaveSetter(Field f, ValidationResults results) {
        Method set = findMethod(clazz.getDeclaredMethods(), m -> ("set" + f.getName()).equals(m.getName())
                                                            && m.getParameterTypes().length!=0
                                                            && m.getParameterTypes()[0].equals(f.getType()));
        if (set != null) {
            results.addResults("\t- the setter is set" + f.getName());
            if (Modifier.isStatic(f.getModifiers()) && !Modifier.isStatic(set.getModifiers())) {
                results.addResults("\t\t- it must be declared as STATIC");
            }
            if(Modifier.isStatic(f.getModifiers())) {
                results.addResults("\t\t- it must be declared as STATIC");
            }
        } else {
            results.addResults("\t- the setter is missing");
        }

        return this;
    }


    private Method findMethod(Method[] methods, Predicate<Method> condition) {
        for (Method m : methods) {
            if (condition.test(m)) {
                return m;
            }
        }
        return null;
    }

}
