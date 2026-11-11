@echo off
set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr
set Path=%JAVA_HOME%\bin;C:\maven\apache-maven-3.9.9\bin;%Path%
cd /d C:\Users\ianle\Downloads\auron-box-androidtst
mvn spring-boot:run