@ECHO off

cd src
javac *.java
copy *.class ..\class\
del *.class

PAUSE