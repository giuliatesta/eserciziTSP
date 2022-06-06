package esame2402;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;

/*
    Grader che riconosce dove la classe passata come argomento differisce dal comportamento standard
    ATTRIBUTI
    Se FINAL --> PUBLIC e STATIC
    Se non FINAL --> PRIVATE e deve avere GETTER e SETTER
    Se STATIC e non FINAL --> GETTER e SETTER sono STATIC

    Deve avere:
        - COSTRUTTORE VUOTO
        - COSTUTTORE con tutti gli attributi non FINAL e non STATIC e in ordine AlFABETICO
        - Se c'è un attributo STATIC --> public static void setStatic(<<argument list>>)
 */
public class GradeHandler {

    public static void main(String[] args) {
        try {
            GradeHandler gradeHandler = new GradeHandler();
            System.out.println(gradeHandler.grade(Class.forName(args[0])));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String grade(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append("CLASS: ").append(clazz.getSimpleName()).append("\n");
        sb.append(checkConstructors(clazz));
        sb.append(checkSetStatic(clazz));
        sb.append(checkFields(clazz));
        return sb.toString();
    }
    private String checkConstructors(Class<?> clazz) {
        Class<?>[] params = filterFields(clazz.getDeclaredFields(), f ->!Modifier.isFinal(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()));
        return checkEmptyConstructor(clazz) + checkConstructorWithParams(clazz, params);
    }
    /*
        Controlla che i campi abbiano i giusti modificatori e che abbiano setter/getter
     */
    private String checkFields(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        for (Field f : clazz.getDeclaredFields()) {
            sb.append(checkFieldModifiers(f))
                    .append(checkGetterAndSetter(clazz.getDeclaredMethods(), f));
        }
        return sb.toString();
    }

    private String checkSetStatic(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        Class<?>[] params = filterFields(clazz.getDeclaredFields(),
                                    f-> !Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers()));
        try {
            if(params.length != 0) {
                clazz.getMethod("setStatic", params);
                sb.append("\t- setStatic is OK\n");
            }
        } catch (Exception e) {
            sb.append(makeMethodErrorString("setStatic", params));
        }
        return sb.toString();
    }

    private String makeMethodErrorString(String methodName, Class<?>[] params) {
        StringBuilder sb = new StringBuilder();
        sb.append("\t- there is no " + methodName+ " that initializes all the STATIC but FINAL attributes" +
                "\n\t\t- argument types: [ ");
        for(Class<?> p: params){
            sb.append(p.getTypeName()).append(" ");
        }
        sb.append("]\n");
        return sb.toString();
    }

    private String checkEmptyConstructor(Class<?> clazz) {
        if (Modifier.isPublic(clazz.getModifiers())) {
            try {
                clazz.getDeclaredConstructor();
                return ("\t- a public empty constructor is declared\n");
            } catch (Exception e) {
                return ("\t- the public empty constructor is missing\n");
            }
        }
        return "";
    }
    private String checkConstructorWithParams(Class<?> clazz, Class<?>[] params) {
        StringBuilder sb = new StringBuilder();
        try {
            clazz.getConstructor(params);
            sb.append("\t- a constructor with the right arguments types is declared\n");
        } catch (Exception e) {
            sb.append(makeMethodErrorString("public constructor", params));
        }
        return sb.toString();
    }

    private Class<?>[] filterFields(Field[] fields, Predicate<Field> condition) {
        List<Class<?>> filteredFields = new ArrayList<>();
        sortInAlphabeticallyOrder(fields);
        for(Field f : fields) {
            if(condition.test(f)) {
                filteredFields.add(f.getType());
            }
        }
        return filteredFields.toArray(new Class<?>[0]);
    }

    private void sortInAlphabeticallyOrder(Field[] declaredFields) {
        Arrays.sort(declaredFields, Comparator.comparing(Field::getName));
    }


    private String checkGetterAndSetter(Method[] methods, Field field) {
        String name = field.getName();
        StringBuilder sb = new StringBuilder();
        Method get = findMethod(methods, m -> ("get" + name).equals(m.getName()));
        Method set = findMethod(methods, m -> ("set" + name).equals(m.getName()));;
        if (get != null) {
            sb.append("\t- the getter is get").append(name).append("\n");
            sb.append(checkIfIsDefinedCorrectly(get, field));
        } else {
            sb.append("\t- the getter is missing\n");
        }

        if (set != null) {
            sb.append("\t- the setter is set").append(name).append("\n");
            sb.append(checkIfIsDefinedCorrectly(set, field));
        } else {
            sb.append("\t- the setter is missing\n");
        }
        return sb.toString();
    }

    private String checkIfIsDefinedCorrectly(Method method, Field field) {
        if (!method.getReturnType().equals(field.getType())) {
            return ("\t\t- it must return " + field.getType().getSimpleName() + " instead of " + method.getReturnType().getSimpleName() + "\n");
        }
        if (Modifier.isStatic(field.getModifiers()) && !Modifier.isStatic(method.getModifiers())) {
            return ("\t\t- it must be declared as STATIC\n");
        }
        return "";
    }

    private Method findMethod(Method[] methods, Predicate<Method> condition) {
        for (Method m : methods) {
            if (condition.test(m)) {
                return m;
            }
        }
        return null;
    }

    /*
    Controlla che i campi abbiano i modificatori giusti
     */
    private String checkFieldModifiers(Field field) {
        StringBuilder grading = new StringBuilder();
        int modifiers = field.getModifiers();
        String fieldName = field.getName();
        // Se il campo è FINAL...
        if (Modifier.isFinal(modifiers)) {
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                // ...allora, deve essere anche PUBLIC e STATIC
                grading.append("The Field ").append(fieldName.toUpperCase()).append(" is Ok\n");
            } else {    //Altrimenti
                if (Modifier.isPublic(modifiers)) {     // Se è solo PUBLIC
                    grading.append("The Field ").append(fieldName.toUpperCase()).append(" must be declared as STATIC\n");
                } else if (Modifier.isStatic(modifiers)) {      // Se è solo STATIC
                    grading.append("The Field ").append(fieldName.toUpperCase()).append(" must be declared as PUBLIC\n");
                } else {    //Se non è nessuno dei due
                    grading.append("The Field ").append(fieldName.toUpperCase()).append(" must be declared as STATIC and PUBLIC\n");
                }
            }
        // Se non è FINAL...
        } else {
            // ...allora, deve essere PRIVATE
            if (Modifier.isPrivate(modifiers)) {
                grading.append("The Field ").append(fieldName.toUpperCase()).append(" is correctly declared as PRIVATE\n");
            } else {
                grading.append("The Field ").append(fieldName.toUpperCase()).append(" must be declared as PRIVATE\n");
            }
        }
        return grading.toString();
    }
}

