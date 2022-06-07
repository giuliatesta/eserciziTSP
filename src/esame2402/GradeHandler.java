package esame2402;

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

    private final ClassStructureChecker classStructureChecker;
    private final FieldsChecker fieldsChecker;

    public GradeHandler(Class<?> theClazz) {
        classStructureChecker = ClassStructureChecker.create(theClazz);
        fieldsChecker = FieldsChecker.create(theClazz);
    }

    public static void main(String[] args) {
        try {
            Class<?> theClazz = Class.forName(args[0]);
            GradeHandler gradeHandler = new GradeHandler(theClazz);
            ValidationResults results = gradeHandler.check();
            results.printResults();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ValidationResults check() {
        ValidationResults results = new ValidationResults();

        classStructureChecker
            .shouldHaveEmptyConstructor(results)
            .shouldHaveAllArgumentsConstructor(results)
            .shouldHaveSetStatic(results);

        fieldsChecker
            .checkFields(results);
        return results;
    }
}
