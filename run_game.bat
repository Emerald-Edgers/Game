@echo off
mvn clean install -DskipTests && java -cp "mods-mvn/*" dk.ee.zg.DesktopLauncher
pause