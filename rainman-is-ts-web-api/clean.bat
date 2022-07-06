call mvn clean
del /s/q *.log
rd /s/q logs
rd /s/q consumer\logs
rd /s/q provider\logs
rd /s/q dist