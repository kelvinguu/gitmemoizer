# GitMemoizer

#### Automagically memoize any function and save results to disk.

## Features
1. Add the `@Memoizable` annotation above any method to make it automatically memoize results.
2. All memoized results are **saved to disk**, and are reused on future executions.
3. Even objects that do not implement Serializable will be correctly saved.
4. Reload results computed by a **previous version** of your code by marking your function with a Git commit SHA: `@Memoizable(version="1645cf61245098c04e4c97946dc1fe4f6e09339c")`.

## Why use GitMemoizer?
Suppose you're writing a program that has several computationally intensive steps.
If each step takes a few minutes, you probably want to save intermediate results to disk after each step.
Otherwise, if your program crashes on step 3, you lose everything from steps 1 and 2.
(Or maybe you haven't even written the code for step 3 yet.)

So people often write their own code to serialize and reload intermediate results from data files.

**This can get hacky for several reasons:**
- Users need to manually specify where their data files are located, or you need to hard-code file paths into your program.
- If you don't establish a proper naming scheme for your data files, it becomes difficult to keep track of what code and what version of it generated your data file.
- Over time your code becomes fragmented into multiple scripts reading from multiple data files.

**With GitMemoizer:**
- Your entire pipeline can remain expressed as a single program with one entry point, and the core logic isn't obscured by code for saving/reloading.
- Each saved result is associated with the exact code that generated it (the function that was called, the arguments, and the Git commit SHA).
- You don't need to devise a file naming scheme. All data is saved in one memoization cache.

## Example
You might have a piece of code like this:

```java
public class Demo {

    public static void main(String[] args) throws Exception {
        System.out.println(exclaim("end the war"));
    }

    public static String exclaim(String input) {
        return input.toUpperCase();
    }
}
```

To enable memoization, add `@Memoizable` above the target method, and configure GitMemoizer by adding the `@MemoConfig` annotation above your `main` method:

```java
import com.github.memoize.aspect.MemoConfig;
import com.github.memoize.aspect.Memoizable;

public class Demo {
    // To configure GitMemoizer, specify:
    // repoPath -- the path to your Git repository
    // cachePath -- the path to your memoization cache (where results will be saved)
    // If the cache does not exist, it will be created.

    @MemoConfig(repoPath="some/path/.git", cachePath="some/path/memocache")
    public static void main(String[] args) throws Exception {
        System.out.println(exclaim("end the war"));
    }

    @Memoizable
    public static String exclaim(String input) {
        return input.toUpperCase();
    }
}
```

That's it! Any results computed by `exclaim()` will be saved to `some/path/memocache` and reused whenever possible.

**Whenever a function is called, GitMemoizer keeps track of 3 things:**

1. The function being called.
2. The arguments passed into that function.
3. The Git commit SHA for the current HEAD of the Git repository.

If these 3 things match a previous function call, then the result of the previous function call is reloaded.

### Using results from a previous Git commit
If you make a new commit to your Git repository (changing where HEAD points), then by default GitMemoizer will not reload results computed at a previous commit.
This is because the function behavior may have changed, and the result would be different.

However, you can force GitMemoizer to keep reloading the result from an older commit, if you're happy with that result.
Just add a `version` argument to the `@Memoizable` annotation:

```java
@Memoizable(version="97a1f21826bad92fac924b4ff3a11f1e8fd144c6")
public static String exclaim(String input) {
    return input.toUpperCase();
}
```

Now `exclaim()` will return results that were memoized by the code at commit `97a1f21826bad92fac924b4ff3a11f1e8fd144c6`.
This often makes sense if you know that a certain function has not been changed by a new commit.

### Notes
- If you call `exclaim()` with **function arguments** that were never memoized in commit `97a1f21826bad92fac924b4ff3a11f1e8fd144c6`, GitMemoizer will throw an error.
- GitMemoizer will record the HEAD commit SHA at the **start** of program execution. Once it is recorded, it will not be changed while the program executes, even if you alter the Git repository.

## Dependencies
1. [AspectJ](http://eclipse.org/aspectj/) is used to intercept function calls for memoization.
2. [Kryo](https://github.com/EsotericSoftware/kryo) for serializing arbitrary Java objects.
3. [Berkeley DB Java Edition](http://en.wikipedia.org/wiki/Berkeley_DB) for saving objects to disk.
4. [JGit](http://www.eclipse.org/jgit/) for accessing Git repositories.
5. [log4j](logging.apache.org/log4j/) for logging GitMemoizer operations.

## Usage
1. Install AspectJ
    - Download `aspectj-1.7.4.jar` from http://eclipse.org/aspectj/downloads.php
    - Double-click `aspectj-1.7.4.jar` to launch the installer. Follow the instructions.
2. Download the jar files for GitMemoizer and its dependencies. They are all included in `demo/lib`. The full list is:
    - `memoize.jar` (GitMemoizer)
    - `aspectjweaver.jar` (AspectJ 1.7.4. The version of AspectJ you installed should also be 1.7.4)
    - `je-5.0.97.jar` (Berkeley DB)
    - `kryo-2.22-all.jar` (Kryo)
    - `log4j-1.2.17.jar` (log4j)
    - `org.eclipse.jgit-3.1.0.201310021548-r.jar` (JGit)
3. Add all the above jars to your classpath (should be accessible to both `javac` and `java`)
4. In your code, add the `@Memoizable` and `@MemoConfig` annotations as demonstrated in the **Example** section above.
5. At execution, pass `java` one extra argument which enables AspectJ to intercept function calls: `-javaagent:lib/aspectjweaver.jar`.

### A small trick for adding jars to your classpath
Because there are a significant number of jars on the classpath, it may be tedious to manually specify them on the command line when calling `java`.
This can be simplified by putting all your jars in a `lib` folder and including `lib/*` on the classpath.

So in full, the command for execution is:

```bash
java -javaagent:lib/aspectjweaver.jar -classpath "bin:lib/*" SomeClass
```

Note that the directory containing the class files (`bin`) must also be on the classpath.

StackOverflow has more information on how to do this:
- http://stackoverflow.com/questions/219585/setting-multiple-jars-in-java-classpath
- http://stackoverflow.com/questions/2096283/including-jars-in-classpath-on-commandline-javac-or-apt

## Demo
Clone this repo and execute the `run` script in `demo`:

```bash
$ git clone https://github.com/kelvingu/gitmemoizer.git
$ cd gitmemoizer/demo
$ ./run
```

(Make sure you are in the `demo` folder when you call `run`.)
After the program executes, GitMemoizer should have created the file `demo/memolog.txt`.
Inside it you should see:

```
INFO - - - - - -
INFO - Repository: .git
INFO - Cache: memocache
INFO - Log file: memolog.txt
INFO - - - - - -
INFO - STORED: public static java.lang.String Demo.exclaim(java.lang.String) | 1645cf61245098c04e4c97946dc1fe4f6e09339c | end the war
```

If you call `run` a second time, GitMemoizer will reload the previously computed result.
You can verify this by checking `memolog.txt` again. It should now look like this:

```
INFO - - - - - -
INFO - Repository: .git
INFO - Cache: memocache
INFO - Log file: memolog.txt
INFO - - - - - -
INFO - STORED: public static java.lang.String Demo.exclaim(java.lang.String) | 1645cf61245098c04e4c97946dc1fe4f6e09339c | end the war
INFO - - - - - -
INFO - Repository: .git
INFO - Cache: memocache
INFO - Log file: memolog.txt
INFO - - - - - -
INFO - LOADED: public static java.lang.String Demo.exclaim(java.lang.String) | 1645cf61245098c04e4c97946dc1fe4f6e09339c | end the war
```

- Note that the first execution `STORED` the result and in the second execution it was `LOADED`.
- The function call is displayed as : `<name of function> | <commit SHA> | <function arguments>`.

**NOTE:** There is currently a small bug related to AspectJ which causes the `STORED` event to be logged twice.