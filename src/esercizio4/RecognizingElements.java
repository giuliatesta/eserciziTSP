package esercizio4;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class RecognizingElements {

    private final java.util.List<Class<?>> classes;
    private final String[] names;
    private final String[] fieldsAndMethods;


    public RecognizingElements(List<Class<?>> classes, String[] names, String[] fieldsAndMethods) {
        this.classes = classes;
        this.names = names;
        this.fieldsAndMethods = fieldsAndMethods;
    }

    public static void main(String[] args) {
        ArrayList<Class<?>> classes = new ArrayList<>();
        classes.add(A.class);
        classes.add(B.class);
        classes.add(C.class);

        String[] fm = {"b1", "b2", "b3", "bm1", "bm2", "am3"};
        RecognizingElements re = new RecognizingElements(classes, new String[]{"esercizio4.A", "esercizio4.B", "esercizio4.C"}, fm);
        System.out.println("RESULT: " + re.recognize());
    }

    private Class<?> recognize() {
        Map<Class<?>, Object[]> cfm = associateClassToFieldsAndMethods();
        Map<Class<?>, Boolean[]> check = initialize(cfm);
        //Map<Class, Boolean> check = new HashMap<>();
        for (Class<?> c : cfm.keySet()) {        //classes
            System.out.println("CLASSES " + c.getName());
            for (Object o : cfm.get(c)) {    //fields and methods
                System.out.println("FIELDS AND METHODS: " + o.getClass().getName());
                for (int i = 0; i < fieldsAndMethods.length; i++) {
                    System.out.println("CHECK: " + fieldsAndMethods[i]);
                    check.get(c)[i] = false;
                    if (o.getClass().equals(Field.class)) {
                        if (((Field) o).getName().equals(fieldsAndMethods[i])) {
                            System.out.println("F!!!");
                            check.get(c)[i] = true;
                            break;
                        }
                    } else if (o.getClass().equals(Method.class)) {
                        if (((Method) o).getName().equals(fieldsAndMethods[i])) {
                            System.out.println("M!!!");
                            check.get(c)[i] = true;
                            break;
                        }
                    } else {
                        System.out.println("F");

                    }
                    System.out.println("!!!! " + check.get(c)[i]);
                }
            }
        }

        for (Class<?> cs : check.keySet()) {
            System.out.println("CLASS: " + cs.getName());
            for (int i = 0; i < check.get(cs).length; i++) {
                System.out.println(check.get(cs)[i]);
            }
        }
        for (Class<?> cs : check.keySet()) {
            int count = check.get(cs).length;
            if(count ==0) {
                continue;
            }
            for (int i = 0; i < check.get(cs).length; i++) {
                System.out.println(check.get(cs)[i]);
                if (check.get(cs)[i]) {
                    count--;
                }

            }
            if (count== 0) {
                return cs;
            }
        }
        return null;
    }

    private Map<Class<?>, Boolean[]> initialize(Map<Class<?>, Object[]> cfm) {
        Map<Class<?>, Boolean[]> check = new HashMap<>();
        for (Class<?> c : classes) {
            int size = 0;
            for (Class<?> cs : cfm.keySet()) {
                if (cs.equals(c)) {
                    size = cfm.get(c).length;
                }
            }
            check.put(c, new Boolean[size]);
        }
        return check;
    }

    private Map<Class<?>, Object[]> associateClassToFieldsAndMethods() {
        Map<Class<?>, Object[]> cls = new HashMap<>();
        for (Class<?> c : classes) {
            if (isInNames(c)) {
                List<Object> fam = new ArrayList<>();
                fam.addAll(Arrays.asList(c.getDeclaredFields()));
                fam.addAll(Arrays.asList(c.getDeclaredMethods()));
                cls.put(c, fam.toArray());

            }
        }
        return cls;
    }

    private boolean isInNames(Class<?> c) {
        for (String n : names) {
            if (n.equals(c.getName())) {
                return true;
            }
        }
        return false;
    }

}
