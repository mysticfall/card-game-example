## Black Jack Game Example ##

### Introduction ###

Simple Black Jack game program written in Scala language.

### Purpose ###

This project started as a modelling practice, which intends to 
help understanding of basic principles in object-oriented software 
design.

### Limitations ###

 * Probably needs much refactoring before it can be used for  
  card games other than Black Jack.
 * Side rules have not been implemented yet (i.e. 'Doubling').
 * No considerations for network clients, as the API relies on 
  blocking calls.
 * Mutable class design, which is partly intentional for future 
 refactoring opportunities.
 * Lacks unit tests.

### How to Run ###

```bash
> sbt run
```

### License ###

This project is provided under the terms of Apache License, Version 2.0.