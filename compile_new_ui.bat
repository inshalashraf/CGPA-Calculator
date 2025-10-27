@echo off
echo Compiling CGPA Calculator with new UI...

if not exist "target\classes" mkdir target\classes

echo Compiling Java files...
javac -encoding UTF-8 -d target/classes -cp "target/classes;lib/*" ^
    src/main/java/com/cgpa/backend/service/*.java ^
    src/main/java/com/cgpa/frontend/ui/theme/*.java ^
    src/main/java/com/cgpa/frontend/ui/event/*.java ^
    src/main/java/com/cgpa/frontend/ui/swing/*.java ^
    src/main/java/com/cgpa/frontend/ui/component/*.java ^
    src/main/java/com/cgpa/frontend/ui/dialog/*.java ^
    src/main/java/com/cgpa/frontend/ui/form/*.java ^
    src/main/java/com/cgpa/frontend/ui/menu/*.java ^
    src/main/java/com/cgpa/frontend/ui/DashboardFrame.java ^
    src/main/java/com/cgpa/app/Main.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Running application...
    java -cp "target/classes;lib/*" com.cgpa.app.Main
) else (
    echo Compilation failed!
    pause
) 