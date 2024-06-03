@ECHO OFF
setlocal

set DEFAULT_JDK_HOME=%HOMEDRIVE%\Program Files\Java\jdk-17
set DEFAULT_JDK_PATH=%HOMEDRIVE%\Program Files\Java\jdk-17\bin
set BATCH_PATH=%~dp0
GOTO:MAIN

:DownloadAndInstallJDK
    runas /noprofile /user:%USERNAME% /savecred "powershell.exe -ExecutionPolicy Bypass -File %BATCH_PATH%\jdk17setup.ps1"
    echo PowerShell script finished running.
EXIT /B


::function that checks path variable
:CheckJavaEnvironmentVariableAndSet
    ::set JAVA_HOME
    ECHO checking environment variable for JAVA_HOME %DEFAULT_JDK_HOME%
    IF not defined JAVA_HOME (
        ECHO setting JAVA_HOME...
        SETX JAVA_HOME "%DEFAULT_JDK_HOME%"
    )

    ::set DEFAULT_JDK_PATH in PATH
    ECHO checking environment variable for PATH %DEFAULT_JDK_PATH%

    SETLOCAL ENABLEDELAYEDEXPANSION

    IF not defined PATH (
        SETX PATH "%DEFAULT_JDK_PATH%"
    ) ELSE (
        IF "!PATH:%DEFAULT_JDK_PATH%=!" NEQ "!PATH!" (
            ECHO Environment variable is already set. Skipping.
        ) ELSE (
            ECHO Setting java/bin into PATH variable.
            SETX PATH "%PATH%;%DEFAULT_JDK_PATH%"
        )
    )
EXIT /B

:CheckJavaSDK
    ECHO Checking JRE 17 located at %DEFAULT_JDK_HOME%
    IF exist %DEFAULT_JDK_PATH% (
        ECHO jdk-17 default path found

        CALL :CheckJavaEnvironmentVariableAndSet
        EXIT /B
    ) ELSE (
        ECHO jdk-17 default path not found. Proceed to download and installation of JDK17?
        set /p UserInput=Enter Y/N:
        ECHO Download and installation for jre17 starting...

        if /I "%UserInput%"=="Y" (
        ::Download jdk-17 and install
            CALL :DownloadAndInstallJDK
            CALL :CheckJavaEnvironmentVariableAndSet
        ) else if /I "%UserInput%"=="N" (
            ECHO Cancelling process. Please setup your own jdk and rerun this batch.
        ) else (
            ECHO Invalid input
        )
    )
EXIT /B

:MAIN
CALL :CheckJavaSDK

cd %BATCH_PATH%
java -jar "%BATCH_PATH%\Mine-Sweeper.jar"

endlocal
PAUSE