import org.checkerframework.checker.templatefora.qual.*;

// Demonstrates that a JDK method gets its contract from the checker's built-in
// src/main/java/org/checkerframework/checker/templatefora/jdk.astub (see that file for the
// annotations themselves) instead of the checker's own default.
class JdkStubTest {

  void stubbedReturnIsBottom() {
    // jdk.astub declares Math.random()'s return type @TemplateforaBottom.
    @TemplateforaBottom double luckyNumber = Math.random();
  }

  void unstubbedCallIsNotBottom() {
    // Math.sqrt is not mentioned in the stub file, so it keeps the checker's default
    // @TemplateforaUnknown return type -- not safe to assign into a @TemplateforaBottom
    // variable.
    // :: error: (assignment.type.incompatible)
    @TemplateforaBottom double notLucky = Math.sqrt(4);
  }
}
