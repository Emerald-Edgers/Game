@echo off
mvn clean install && java -cp "mods-mvn/*" dk.ee.zg.DesktopLauncher
pause