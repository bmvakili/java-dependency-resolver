# Java Dependency Resolver #

Finds the dependent Classes/jars for a particular class in a Project. This is useful when you would like to fetch some features/classes from a particular java project.

## Getting Started ##
To use this project you have to get the source code. You can get the zip from [downloads](http://java-dependency-resolver.googlecode.com/files/javaDependencyResolver.zip) or you can also check out from the [Source](http://code.google.com/p/java-dependency-resolver/source/checkout) tab (svn repository). Now the steps are :

  * Open "javaDependencyResolver" project as an eclipse project (or at your preferred IDE)(let's say it current Project).
  * In the project add another project at your build path - The target project from which you want to fetch sources (let's say it target Project).

  * Add the libraries of target project at the current project's build path. These two steps are to make sure- what you want to fetch,  it must be at the build path of the current project.

  * Find the properties in the "conf/resolver.properties"  file. The properties are as follows :
> > - source.dir = Full File path of the source dir of the target project.
> > - dest.src.dir = File path of the directory where you want to keep the dependent sources(java files).
> > - dest.lib.dir = File path of the directory where you want to keep the dependent libraries (jars).


> - max.iteration =  This is the max iteration number. In one iteration it searches the dependent classes  of the classes it obtained so far, The moderate value is 25, you can put here larger number if you think all the dependent classes have not been fetched. But I have tested with some large project, this is good enough.

  * Now, Find the main method in the "ASMClassDependencyResolver" class. There is a method call ( in the third source line of method ) named  "resolveDependency( yourClass.class.getName ( )  );". Here give your class name for which you want to fetch dependency.

  * You are almost there, Now just Run the main from "ASMClassDependencyResolver" class.

  * You will get the dependent sources and jars in the "dest.src.dir" and " dest.lib.dir" respectively.

# References #

  * http://stackoverflow.com/questions/3734825/find-out-which-classes-of-a-given-api-are-used
  * http://stackoverflow.com/questions/320542/how-to-get-the-path-of-a-running-jar-file



