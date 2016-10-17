JFLAGS = -g
JC = javac
JVM = java
MAIN = homework
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Game.java \
        homework.java 

default: classes

newgame: classes

run: $(MAIN).class
	$(JVM) $(MAIN)

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
