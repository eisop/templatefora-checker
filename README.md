# Template for a checker built on the Checker Framework

This repository contains a template for a pluggable type-checker built on the Checker Framework.

### How to use this template to build your own typechecker

The example commands are for a
checker to verify character encoding, such as UTF-8.

0. Copy this repository's contents by clicking the
   "Use this template" green button on its Github page.  Don't make a fork.

1. Choose a name for your type system (e.g., "Character Encoding Checker"),
and change every occurrence of "TemplateForA" to "Character Encoding".
This command does so:

**Important note:** If you are using MacOSX (or any other BSD Unix derivative), then you must either
 * use `gsed` from coreutils instead of MacOS `sed`, or
 * anywhere the instructions say `sed -i`, instead use `sed -i ''`

```
LC_ALL=C find . -name .git -prune -o -type f -exec sed -i -e 's/TemplateForA/Character Encoding/g' {} \;
```

2. Choose a name for the implementation class (e.g., "CharEncoding"), and
change every occurrence of "Templatefora" to "CharEncoding".
This includes in file names (rename several files including
`src/main/java/org/checkerframework/checker/templatefora/TemplateforaChecker.java`).
These commands make the changes:

```
LC_ALL=C find . -name .git -prune -o -type f -exec sed -i -e 's/Templatefora/CharEncoding/g' {} \;
find . -name '*Templatefora*' -exec bash -c 'mv $0 ${0/Templatefora/CharEncoding}' {} \;
```

3. Choose a directory/package name (e.g., "charencoding"), and
Change every occurrence of "templatefora" to "charencoding".
  This includes in file names (for example, rename directories
  `src/main/java/org/checkerframework/checker/templatefora/` and `tests/templatefora/`).
These commands make the changes:

```
LC_ALL=C find . -name .git -prune -o -type f -exec sed -i -e 's/templatefora/charencoding/g' {} \;
for file in $(find . -name '*templatefora*'); do mv $file ${file/templatefora/charencoding}; done
```

4. Change the groupId of the `publishing` block in the `build.gradle` file
  to an organization you belong to that can publish to Maven
  Central. Change the name of the package containing the source (currently
  `org.checkerframework.checker.templatefora`) so that it reflects your
  organization's naming standards. Replace all instances of
  `org.checkerframework.checker.templatefora` and
  `org/checkerframework/checker/templatefora` with whatever name you have chosen.
  These commands make the changes within files but do not rename directories:

```
LC_ALL=C find . -type f -exec sed -i -e 's/org\.checkerframework\.checker\.templatefora/my.organization.templatefora/g' {} \;
LC_ALL=C find . -type f -exec sed -i -e 's:org/checkerframework/checker/templatefora:my/organization/templatefora:g' {} \;
```


5. Change the copyright info in the `LICENSE` file.  You may change the license as well, if you wish.

6. Follow the ["How to create a new checker"
  instructions](https://checkerframework.org/manual/#creating-a-checker)
  in the Checker Framework Manual.
  While following the instructions, you should change all occurrences of TODO
  (and probably all occurrences of "Bottom") throughout the repository, and
  modified this `README.md` file so that it describes your checker.

7. Remove all text from the top of this `README.md` file, through this line.


# The remainder of this README

The remainder of this README is the user-facing documentation for the analysis
you will build.

You should remove, from your version, everything above and including this line.


# TemplateForA Checker

A common problem when programming is TODO.
This results in a run-time exception.

The TemplateForA Checker guarantees, at compile time, that your code will
not suffer that run-time exception.


## How to run the checker

First, publish the checker to your local Maven repository by running
`./gradlew publishToMavenLocal` in this repository.

Then, if you use Gradle, add the following to the `build.gradle` file in
the project you wish to type-check (using Maven is similar):

```
repositories {
    mavenLocal()
    mavenCentral()
}
dependencies {
    annotationProcessor 'io.github.eisop:templatefora-checker:0.1-SNAPSHOT'
}
```

Now, when you build your project, the TemplateForA Checker will also run,
informing you of any potential errors related to TODO.


## Giving JDK and library methods a contract

Your own code's contracts come from the `@TemplateforaBottom`/`@TemplateforaUnknown`
qualifiers you write directly on it (see "How to specify your code" below). A method in the
JDK or another library, whose source you cannot annotate, instead gets its contract from a
*stub file* -- see the Checker Framework manual's
[Stub files](https://eisop.github.io/cf/manual/#stub) section for the full format. There are
two ways to supply one, both demonstrated in this repository:

- **The checker's own built-in stub.**
  [`src/main/java/org/checkerframework/checker/templatefora/jdk.astub`](src/main/java/org/checkerframework/checker/templatefora/jdk.astub)
  ships with this checker and applies automatically for every downstream project -- no
  configuration needed on their end. It is loaded by filename convention (a file named exactly
  `jdk.astub`, sitting beside `TemplateforaChecker.java`); add more built-in stub files with a
  `@StubFiles` annotation on `TemplateforaChecker` (see the manual's [Using a stub
  file](https://eisop.github.io/cf/manual/#stub-using) section).
- **A checker user's own `-Astubs` file.** Anyone using this checker on their own project can
  add contracts of their own, for methods this checker's built-in stub does not cover, by
  passing `-Astubs=<path>` pointing at their own stub file or directory.
  [`example-client/`](example-client/) is a small, self-contained example of exactly that: a
  project that depends on `templatefora-checker` (the same way the "How to run the checker"
  section above describes) and supplies
  [`example-client/stubs/jdk-extra.astub`](example-client/stubs/jdk-extra.astub) for a JDK
  method the checker's own built-in stub does not annotate. Run
  `./gradlew :example-client:build` to see it type-check successfully because of that stub.

Either kind of stub file can be pre-parsed into a faster binary form ahead of time, instead of
being parsed as text on every compilation that uses it -- this template does so automatically:

- [`build.gradle`](build.gradle)'s `generateBinaryStubFiles` task pre-parses the checker's own
  `jdk.astub`, and packages the result into the built jar right alongside the text file, so
  every downstream consumer gets the faster form, not just this repository's own tests.
- [`example-client/build.gradle`](example-client/build.gradle)'s task of the same name does the
  same for that project's own `stubs/jdk-extra.astub`, in place beside it -- a user-supplied
  `-Astubs` file is never packaged into any jar, so there is nothing to add to
  `example-client`'s own build output.

If a stub file's binary form no longer matches its current text (for example, someone edited
the `.astub` file without re-running the build), the Checker Framework falls back to
text-parsing it and warns; it never silently applies a stale contract. This binary-stub-file
support, including for a `-Astubs` file specifically, is recent Checker Framework work that has
not shipped in a released eisop version as of this writing: see `checkerframework_local` in
[`build.gradle`](build.gradle) and [`example-client/build.gradle`](example-client/build.gradle)
for how to point this repository at a local, unreleased checkout in the meantime.


## How to specify your code

At compile time, the TemplateForAChecker estimates what values the program
may compute at run time.  It issues a warning if the program may
TODO.
It works via a technique called pluggable typechecking.

You need to specify the contracts of methods and fields in your code --
that is, their requirements and their guarantees.  The TemplateForAChecker
ensures that your code is consistent with the contracts, and that the
contracts guarantee that TODO.

You specify your code by writing *qualifiers* such as `@TemplateforaBottom`
on types, to indicate more precisely what values the type represents.
Here is a list of the type qualifiers that are supported by
the TemplateForAChecker, with an explanation of each one:

`@TemplateforaUnknown`:
The value might or might not be TODO. It is not safe to use for TODO.
This is the default type, so programmers usually do not need to write it.

`@TemplateforaBottom`:
The value is definitely TODO. It is safe to use for TODO.


## How to build the checker

Run these commands from the top-level directory.

`./gradlew build`: build the checker. This also builds and type-checks `example-client`
(see "Giving JDK and library methods a contract" above), since it is a subproject of this
same Gradle build, not a separate repository.

`./gradlew publishToMavenLocal`: publish the checker to your local Maven repository.
This is useful for testing before you publish it elsewhere, such as to Maven Central.


## More information

The TemplateForA Checker is built upon the Checker Framework.  Please see
the [Checker Framework Manual](https://checkerframework.org/manual/) for
more information about using pluggable type-checkers, including this one.
