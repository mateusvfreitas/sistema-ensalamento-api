Rodar os comandos abaixo para configurar o CPLEX no projeto local.
```bash
mvn install:install-file -Dfile='C:\Program Files\IBM\ILOG\CPLEX_Studio221\cplex\lib\cplex.jar' -DgroupId=cplex -DartifactId=cplex -Dversion='22.1.0.0' -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile='C:\Program Files\IBM\ILOG\CPLEX_Studio221\opl\lib\oplall.jar' -DgroupId=opl -DartifactId=opl -Dversion='22.1.0.0' -Dpackaging=jar -DgeneratePom=true
```
