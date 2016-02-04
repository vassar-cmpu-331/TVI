# TVI
The Vassar Interpreter. A simple interpreter used in CMPU-331

TVI 2.0.0 is a Java implementation of the original C++ interpreter used previously. TVI 2.x supports the same set of [opcodes](http://www.cs.vassar.edu/~cs331/tvi/tvi.html) used in the original interpreter.  The only differences are:

1. `DATA` sections are not supported, but the CMPU-331 compiler didn't make use of them anyway.
2. Every instruction must now be numbered sequentially.

## Build
 
To build the interpreter from scratch download the code and compile with Maven.
 
```bash
$> git clone https://github.com/vassar-cmpu-331/TVI.git
$> cd TVI
$> mvn package
```
 
If you would like to change the name of the project directory simply specify the name you want git to use when you clone
the repository:

```bash
$> git clone https://github.com/vassar-cmpu-331/TVI.git my_directory
$> cd my_directory
$> mvn package
```
The directory `my_directory` must not already exist.

After running `mvn package` the generated jar file can be found in the `target` directory.

## Running

Maven generates a runnable jar file that accepts one parameter; the name of the .tvi file to be interpreted (note: you do not have to use a .tvi file extension).
```bash
$> java -jar target/tvi-2.0.0.jar /path/to/file.tvi
```
If the compiled program uses a large amount of memory you may need to increase the size of the Java heap:

```bash
$> java -Xmx512M -jar target/tvi-2.0.0.jar /path/to/file.tvi
```
The `-Xmx` parameter takes a number followed by the memory unit size (M=megabyte, G=gigabytes).  If you forget to include the memory unit size then the default (bytes) is assumed.

## Options

TVI accepts the following options:

**-t, --trace**  
 Enables tracing.  Each instruction is printed to System.out before it is executed  
**-s, --size**  
Sets the size of the memory pool the interpreter.  The default is 1M memory locations.  
*NOTE* Setting the size of the memory pool is not the same as setting the `-Xmx` Java command line parameter.  Although if you create a very large memory pool you will likely also need to increase the Java heap size.  
**-d, --debug**  
 Produces some extra debugging output. This is mostly useful for developing TVI itself.  
 **-r, --renumber**  
 Renumbers the statements in a .tvi file. Use this option if you manually add/delte instructions in your .tvi file and need to fix the line numbers after editting.  
 **-v, --version**  
 Displays the TVI version.  
 **-h, --help**  
 Displays a simple help message.
 
 **WARNING**
 The `--renumber` option only renumbers the lines; it **DOES NOT** back-patch any `goto` statements; so this option will break your .tvi file if it contains `goto` statements.
 
## Problems or Bugs
 
Please [open a new issue](https://github.com/vassar-cmpu-331/tvi/issues) if (when) you find any bugs in the interpreter.
 
