package example;

import java.util.Random;
import org.checkerframework.checker.templatefora.qual.TemplateforaBottom;

/**
 * Stands in for application code in a project that depends on templatefora-checker. Demonstrates
 * this project's own -Astubs stub file, stubs/jdk-extra.astub, taking effect: see that file.
 */
public class Client {

  void diceRoll() {
    // Only type-checks because stubs/jdk-extra.astub declares Random.nextInt()'s return type
    // @TemplateforaBottom; templatefora-checker's default for an unstubbed JDK method is
    // @TemplateforaUnknown, which would not be assignable here.
    @TemplateforaBottom int roll = new Random().nextInt();
  }
}
