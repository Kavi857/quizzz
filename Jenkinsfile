pipeline {

    agent any

    options {
        // Allow retry on Jenkins restart
        retry(conditions: [nonresumable()], count: 2)

        durabilityHint('PERFORMANCE_OPTIMIZED')

        timeout(time: 30, unit: 'MINUTES')

        disableConcurrentBuilds()
    }

    environment {

        // ============================================================
        // GENERIC CONFIGURATION
        // CHANGE PATHS ONLY HERE
        // ============================================================

        // ------------------------------------------------------------
        // JAVA / MAVEN
        // ------------------------------------------------------------

        JAVA_HOME = 'C:/Program Files/Java/jdk-17.0.2'
        MAVEN_HOME = 'D:/apache-maven-3.8.5'

        // ------------------------------------------------------------
        // BACKEND
        // ------------------------------------------------------------

        APP_JAR = 'target/quizapp
        BACKEND_PORT = '8080'
        BACKEND_URL = 'http://localhost:8080/api/categories'

        // ------------------------------------------------------------
        // TOMCAT / APPZILLON
        // ------------------------------------------------------------

        APPZ_HOME = 'D:/apache-tomcat-9.0.53-windows-x64/apache-tomcat-9.0.53'

        APPZ_ARTIFACTS = 'D:/forDeploy'

        QUIZZ_PROJECT = 'D:/MONTH-2/Week-4/wednesday/quizzz/quizzz'

        QUIZZ_BIN = 'D:/MONTH-2/Week-4/wednesday/quizzz/quizzz/bin'

        TOMCAT_PORT = '9090'

        APPZILLON_URL = 'http://localhost:9090/quizapp'

        // ------------------------------------------------------------
        // DATABASE
        // ------------------------------------------------------------

        DB_NAME = 'quiz_app'
        DB_USER = 'root'
        DB_PASS = 'root'

        MYSQL_BIN = 'C:/Program Files/MySQL/MySQL Server 8.0/bin'

        // ------------------------------------------------------------
        // PLAYWRIGHT
        // ------------------------------------------------------------

        PLAYWRIGHT_DIR = 'C:/Users/Kavi.bharathi/Downloads/quiz-app-backend (1)/quiz-app/src/test/java'
    }


    stages {

        // ============================================================
        // BUILD BACKEND
        // ============================================================

        stage('Build Backend Jar') {

            steps {

                echo '=========================================='
                echo 'BUILDING QUIZAPP BACKEND'
                echo '=========================================='

                bat '''
                    @echo off

                    set "JAVA_HOME=%JAVA_HOME%"
                    set "PATH=%JAVA_HOME%\\bin;%MAVEN_HOME%\\bin;%PATH%"

                    echo.
                    echo ==========================================
                    echo JAVA VERSION
                    echo ==========================================

                    java -version

                    echo.
                    echo ==========================================
                    echo MAVEN VERSION
                    echo ==========================================

                    mvn -version
                '''


                echo '=========================================='
                echo 'CHECKING MAVEN PROJECT'
                echo '=========================================='


                bat '''
                    @echo off

                    echo Current workspace:
                    cd

                    echo.
                    echo Checking pom.xml...

                    if not exist "pom.xml" (

                        echo ERROR: pom.xml not found in workspace

                        echo.
                        echo Workspace contents:

                        dir

                        exit /b 1
                    )

                    echo pom.xml found successfully.
                '''


                echo '=========================================='
                echo 'KILLING OLD BACKEND PROCESS'
                echo '=========================================='


                bat '''
                    @echo off

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%BACKEND_PORT% ^| findstr LISTENING') do (

                        echo Killing process %%a on port %BACKEND_PORT%

                        taskkill /F /PID %%a >nul 2>&1
                    )

                    ping 127.0.0.1 -n 3 >nul
                '''


                echo '=========================================='
                echo 'STARTING MAVEN BUILD'
                echo '=========================================='


                bat '''
                    @echo off

                    set "JAVA_HOME=%JAVA_HOME%"
                    set "PATH=%JAVA_HOME%\\bin;%MAVEN_HOME%\\bin;%PATH%"

                    mvn clean package -DskipTests

                    if errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo MAVEN BUILD FAILED
                        echo ==========================================

                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo MAVEN BUILD SUCCESSFUL
                    echo ==========================================
                '''


                echo '=========================================='
                echo 'CHECKING GENERATED JAR'
                echo '=========================================='


                bat '''
                    @echo off

                    if not exist "%APP_JAR%" (

                        echo ERROR: %APP_JAR% NOT FOUND

                        echo.
                        echo Target directory contents:

                        if exist target (
                            dir target
                        ) else (
                            echo target directory does not exist
                        )

                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo QUIZBACKEND JAR FOUND
                    echo ==========================================

                    dir target\\*.jar
                '''
            }
        }


        // ============================================================
        // DEPLOY BACKEND
        // ============================================================

        stage('Deploy Backend') {

            steps {

                echo '=========================================='
                echo 'DEPLOYING QUIZAPP BACKEND'
                echo '=========================================='


                bat '''
                    @echo off

                    if not exist "%WORKSPACE%\\%APP_JAR%" (

                        echo ERROR: JAR NOT FOUND

                        echo Expected:

                        echo %WORKSPACE%\\%APP_JAR%

                        exit /b 1
                    )

                    echo.
                    echo QuizBackend JAR found.

                    echo.
                    echo ==========================================
                    echo CHECKING PORT %BACKEND_PORT%
                    echo ==========================================

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%BACKEND_PORT% ^| findstr LISTENING') do (

                        echo Stopping process %%a on port %BACKEND_PORT%

                        taskkill /F /PID %%a >nul 2>&1
                    )

                    echo.
                    echo Waiting for port %BACKEND_PORT%...

                    ping 127.0.0.1 -n 4 >nul

                    echo.
                    echo ==========================================
                    echo STARTING QUIZAPP BACKEND
                    echo ==========================================

                    set "JAVA_HOME=%JAVA_HOME%"
                    set "PATH=%JAVA_HOME%\\bin;%PATH%"

                    set "JENKINS_NODE_COOKIE=dontKillMe"

                    echo Starting:

                    echo java -jar %APP_JAR%

                    start "QuizApp-Backend" /B cmd /c "set JENKINS_NODE_COOKIE=dontKillMe && set JAVA_HOME=%JAVA_HOME% && java -jar %APP_JAR% > backend.log 2>&1"

                    echo.
                    echo BACKEND START COMMAND EXECUTED

                    echo.
                    echo Waiting for application to start...

                    ping 127.0.0.1 -n 8 >nul

                    echo.
                    echo ==========================================
                    echo BACKEND LOG
                    echo ==========================================

                    if exist backend.log (

                        powershell -Command "Get-Content backend.log -Tail 30"

                    ) else (

                        echo backend.log not found
                    )
                '''
            }
        }


        // ============================================================
        // BACKEND HEALTH CHECK
        // ============================================================

        stage('Backend Health Check') {

            steps {

                echo '=========================================='
                echo 'CHECKING QUIZAPP BACKEND'
                echo '=========================================='


                bat '''
                    @echo off

                    echo.
                    echo Backend URL:

                    echo %BACKEND_URL%

                    echo.
                    echo Backend Port:

                    echo %BACKEND_PORT%

                    set RETRIES=20


                    :CHECK_BACKEND

                    echo.
                    echo Checking backend...

                    echo Remaining attempts: %RETRIES%


                    curl -s -o nul -w "%%{http_code}" "%BACKEND_URL%" | findstr "200 201"

                    if not errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo BACKEND IS RUNNING
                        echo ==========================================

                        echo Backend URL:

                        echo %BACKEND_URL%

                        exit /b 0
                    )


                    echo.
                    echo Backend not ready.


                    set /a RETRIES-=1


                    if %RETRIES% LEQ 0 (

                        echo.
                        echo ==========================================
                        echo BACKEND FAILED TO START
                        echo ==========================================

                        echo.
                        echo ==========================================
                        echo PORT %BACKEND_PORT% STATUS
                        echo ==========================================

                        netstat -ano | findstr :%BACKEND_PORT%

                        echo.
                        echo ==========================================
                        echo BACKEND LOG
                        echo ==========================================

                        if exist backend.log (

                            type backend.log

                        ) else (

                            echo backend.log not found
                        )

                        exit /b 1
                    )


                    echo.
                    echo Waiting 3 seconds before retry...

                    ping 127.0.0.1 -n 4 >nul

                    goto CHECK_BACKEND
                '''
            }
        }


        // ============================================================
        // DEPLOY APPZILLON
        // ============================================================

        stage('Deploy Appzillon - Full') {

            steps {

                echo '=========================================='
                echo 'DEPLOYING APPZILLON - FULL STEPS'
                echo '=========================================='


                // ----------------------------------------------------
                // FIND APPZILLON FILES
                // ----------------------------------------------------

                powershell '''
                    $ErrorActionPreference = "Stop"

                    Write-Host "=========================================="
                    Write-Host "CHECKING APPZILLON PROJECT"
                    Write-Host "=========================================="


                    Write-Host "QUIZZ_PROJECT: $env:QUIZZ_PROJECT"

                    Write-Host "QUIZZ_BIN: $env:QUIZZ_BIN"

                    Write-Host "APPZ_HOME: $env:APPZ_HOME"


                    $quizBin = $env:QUIZZ_BIN

                    $appzHome = $env:APPZ_HOME

                    $artifacts = $env:APPZ_ARTIFACTS


                    if (-not (Test-Path $quizBin)) {

                        Write-Host "WARNING: QUIZZ_BIN not found."

                        Write-Host "Using fallback: $artifacts"

                        $quizBin = $null

                    } else {

                        Write-Host "Found QUIZZ_BIN: $quizBin"

                        Get-ChildItem -LiteralPath $quizBin -Recurse -Depth 2 |
                            ForEach-Object {
                                Write-Host $_.FullName
                            }
                    }


                    if (-not (Test-Path $appzHome)) {

                        Write-Host "ERROR: Tomcat not found at $appzHome"

                        exit 1
                    }


                    if (-not (Test-Path "$appzHome/bin/catalina.bat")) {

                        Write-Host "ERROR: catalina.bat missing"

                        exit 1
                    }


                    Write-Host "Tomcat found: $appzHome"


                    Write-Host ""
                    Write-Host "=========================================="
                    Write-Host "SEARCHING FOR WAR FILES"
                    Write-Host "=========================================="


                    $webWar = $null

                    $serverWar = $null

                    $webPropsSource = $null

                    $serverPropsSource = $null

                    $dbSqlPath = $null


                    if ($quizBin) {

                        // ------------------------------------------------
                        // WEB WAR
                        // ------------------------------------------------

                        $webWarCandidates =
                            Get-ChildItem `
                                -LiteralPath "$quizBin/Web" `
                                -Filter "*.war" `
                                -ErrorAction SilentlyContinue |
                            Select-Object -First 1


                        if ($webWarCandidates) {

                            $webWar = $webWarCandidates.FullName
                        }


                        if (-not $webWar) {

                            $webWar =
                                (
                                    Get-ChildItem `
                                        -Path "$quizBin/Web" `
                                        -Filter "*.war" `
                                        -Recurse `
                                        -ErrorAction SilentlyContinue |
                                    Select-Object -First 1
                                ).FullName
                        }


                        // ------------------------------------------------
                        // SERVER WAR
                        // ------------------------------------------------

                        $serverWarCandidates =
                            Get-ChildItem `
                                -LiteralPath "$quizBin/Server" `
                                -Filter "*.war" `
                                -ErrorAction SilentlyContinue |
                            Select-Object -First 1


                        if ($serverWarCandidates) {

                            $serverWar = $serverWarCandidates.FullName
                        }


                        if (-not $serverWar) {

                            $serverWar =
                                (
                                    Get-ChildItem `
                                        -Path "$quizBin/Server" `
                                        -Filter "*.war" `
                                        -Recurse `
                                        -ErrorAction SilentlyContinue |
                                    Select-Object -First 1
                                ).FullName
                        }


                        // ------------------------------------------------
                        // WEB PROPERTIES
                        // ------------------------------------------------

                        $webPropsRoot = "$quizBin/Web/Properties"


                        if (Test-Path $webPropsRoot) {

                            $webPropsSource =
                                (
                                    Get-ChildItem `
                                        -LiteralPath $webPropsRoot `
                                        -Directory `
                                        -ErrorAction SilentlyContinue |
                                    Select-Object -First 1
                                ).FullName


                            Write-Host "Web Properties found: $webPropsSource"
                        }


                        // ------------------------------------------------
                        // SERVER PROPERTIES
                        // ------------------------------------------------

                        $serverPropsRoot = "$quizBin/Server/Properties"


                        if (Test-Path $serverPropsRoot) {

                            $serverPropsSource =
                                (
                                    Get-ChildItem `
                                        -LiteralPath $serverPropsRoot `
                                        -Directory `
                                        -ErrorAction SilentlyContinue |
                                    Select-Object -First 1
                                ).FullName


                            Write-Host "Server Properties found: $serverPropsSource"
                        }


                        // ------------------------------------------------
                        // DATABASE
                        // ------------------------------------------------

                        $dbSqlPath = "$quizBin/Server/Database/MySql"


                        if (-not (Test-Path $dbSqlPath)) {

                            $dbSqlPath =
                                "$quizBin/Server/Properties/AppzillonServer/quizzz/Database/MySql"


                            if (-not (Test-Path $dbSqlPath)) {

                                $dbSqlPath =
                                    Get-ChildItem `
                                        -Path "$quizBin" `
                                        -Filter "*.sql" `
                                        -Recurse `
                                        -ErrorAction SilentlyContinue |
                                    Select-Object -First 1 |
                                    ForEach-Object {
                                        $_.DirectoryName
                                    }
                            }
                        }
                    }


                    // ----------------------------------------------------
                    // FALLBACK
                    // ----------------------------------------------------

                    if (-not $webWar -and (Test-Path "$artifacts/quizzz.war")) {

                        $webWar = "$artifacts/quizzz.war"

                        Write-Host "Fallback Web WAR: $webWar"
                    }


                    if (-not $serverWar -and (Test-Path "$artifacts/AppzillonServer.war")) {

                        $serverWar = "$artifacts/AppzillonServer.war"

                        Write-Host "Fallback Server WAR: $serverWar"
                    }


                    if (-not $webPropsSource -and (Test-Path "$artifacts/quizzz")) {

                        $webPropsSource = "$artifacts/quizzz"

                        Write-Host "Fallback Web Props: $webPropsSource"
                    }


                    if (-not $serverPropsSource -and (Test-Path "$artifacts/lib/AppzillonServer")) {

                        $serverPropsSource = "$artifacts/lib/AppzillonServer"

                        Write-Host "Fallback Server Props: $serverPropsSource"
                    }


                    if (-not $dbSqlPath -and
                        (Test-Path "$artifacts/lib/AppzillonServer/quizzz/Database/MySql")) {

                        $dbSqlPath =
                            "$artifacts/lib/AppzillonServer/quizzz/Database/MySql"
                    }


                    Write-Host ""

                    Write-Host "Web WAR: $webWar"

                    Write-Host "Server WAR: $serverWar"

                    Write-Host "Web Props Source: $webPropsSource"

                    Write-Host "Server Props Source: $serverPropsSource"

                    Write-Host "DB SQL Path: $dbSqlPath"


                    if (-not $webWar -or -not (Test-Path $webWar)) {

                        Write-Host "ERROR: Web WAR not found!"

                        exit 1
                    }


                    if (-not $serverWar -or -not (Test-Path $serverWar)) {

                        Write-Host "WARNING: Server WAR missing."

                        Write-Host "Continuing with Web WAR only."
                    }


                    Set-Content `
                        -Path "$env:WORKSPACE/appzillon_vars.txt" `
                        -Value "WEB_WAR=$webWar`nSERVER_WAR=$serverWar`nWEB_PROPS=$webPropsSource`nSERVER_PROPS=$serverPropsSource`nDB_PATH=$dbSqlPath"


                    Write-Host "Vars saved to appzillon_vars.txt"
                '''


                // ====================================================
                // COPY PROPERTIES
                // ====================================================

                powershell '''
                    $ErrorActionPreference = "Stop"


                    Write-Host "=========================================="

                    Write-Host "COPYING PROPERTIES TO TOMCAT LIB"

                    Write-Host "=========================================="


                    $appzHome = $env:APPZ_HOME


                    $vars =
                        Get-Content `
                            -LiteralPath "$env:WORKSPACE/appzillon_vars.txt"


                    $map = @{}


                    foreach ($line in $vars) {

                        if ($line -match "^(.*?)=(.*)$") {

                            $map[$matches[1]] = $matches[2]
                        }
                    }


                    $webProps = $map["WEB_PROPS"]

                    $serverProps = $map["SERVER_PROPS"]


                    Write-Host "Web Props: $webProps"

                    Write-Host "Server Props: $serverProps"

                    Write-Host "Tomcat LIB: $appzHome/lib"


                    if ($webProps -and (Test-Path $webProps)) {

                        Write-Host ""

                        Write-Host "Copying Web Properties..."

                        Write-Host "$webProps -> $appzHome/lib"


                        if (-not (Test-Path "$appzHome/lib")) {

                            New-Item `
                                -ItemType Directory `
                                -Path "$appzHome/lib" `
                                -Force |
                            Out-Null
                        }


                        $destName = Split-Path $webProps -Leaf

                        $dest = Join-Path "$appzHome/lib" $destName

                        $src = $webProps.TrimEnd("\\")


                        try {

                            if (Test-Path $dest) {

                                Remove-Item `
                                    -LiteralPath $dest `
                                    -Recurse `
                                    -Force `
                                    -ErrorAction SilentlyContinue
                            }


                            Copy-Item `
                                -LiteralPath $src `
                                -Destination "$appzHome/lib/" `
                                -Recurse `
                                -Force


                            Write-Host "Web Properties copied successfully."

                        }
                        catch {

                            Write-Host "ERROR copying Web Props: $_"

                            exit 1
                        }

                    }
                    else {

                        Write-Host "WARNING: Web Props not found."
                    }


                    if ($serverProps -and (Test-Path $serverProps)) {

                        Write-Host ""

                        Write-Host "Copying Server Properties..."

                        Write-Host "$serverProps -> $appzHome/lib"


                        $destName = Split-Path $serverProps -Leaf

                        $dest = Join-Path "$appzHome/lib" $destName

                        $src = $serverProps.TrimEnd("\\")


                        try {

                            if (Test-Path $dest) {

                                Remove-Item `
                                    -LiteralPath $dest `
                                    -Recurse `
                                    -Force `
                                    -ErrorAction SilentlyContinue
                            }


                            Copy-Item `
                                -LiteralPath $src `
                                -Destination "$appzHome/lib/" `
                                -Recurse `
                                -Force


                            Write-Host "Server Properties copied successfully."

                        }
                        catch {

                            Write-Host "ERROR copying Server Props: $_"

                            exit 1
                        }

                    }
                    else {

                        Write-Host "WARNING: Server Props not found."
                    }


                    Write-Host ""

                    Write-Host "Tomcat lib contents after copy:"


                    if (Test-Path "$appzHome/lib") {

                        Get-ChildItem `
                            -LiteralPath "$appzHome/lib" |
                        ForEach-Object {

                            Write-Host $_.Name
                        }
                    }
                '''


                // ====================================================
                // DATABASE
                // ====================================================

                bat '''
                    @echo off

                    echo.
                    echo ==========================================
                    echo RUNNING MYSQL DATABASE SCRIPTS
                    echo ==========================================

                    echo DB_NAME: %DB_NAME%

                    echo MYSQL_BIN: %MYSQL_BIN%

                    echo DB_USER: %DB_USER%


                    set "MYSQL_EXE=%MYSQL_BIN%\\mysql.exe"


                    if not exist "%MYSQL_EXE%" (

                        echo ERROR: mysql.exe not found at:

                        echo %MYSQL_EXE%

                        echo Trying alternate path...

                        set "MYSQL_EXE=C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql.exe"
                    )


                    if not exist "%MYSQL_EXE%" (

                        echo ERROR: mysql.exe still not found.

                        where mysql >nul 2>&1

                        if not errorlevel 1 (

                            for /f "delims=" %%i in ('where mysql') do set "MYSQL_EXE=%%i"

                        )
                    )


                    echo Using MYSQL_EXE: %MYSQL_EXE%


                    if not exist "%MYSQL_EXE%" (

                        echo WARNING: mysql.exe not found.

                        echo Skipping database setup.

                        goto DB_SKIP
                    )


                    echo.
                    echo Creating database if not exists...


                    "%MYSQL_EXE%" `
                        -u %DB_USER% `
                        -p%DB_PASS% `
                        -e "CREATE DATABASE IF NOT EXISTS %DB_NAME% CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"


                    if errorlevel 1 (

                        echo WARNING: Failed to create database.

                    )
                    else (

                        echo Database %DB_NAME% ensured.
                    )


                    echo.
                    echo Searching for SQL files...


                    set "DB_PATH="


                    if exist "%WORKSPACE%\\appzillon_vars.txt" (

                        for /f "tokens=1,* delims==" %%a in (
                            'type "%WORKSPACE%\\appzillon_vars.txt" ^| findstr DB_PATH'
                        ) do set "DB_PATH=%%b"
                    )


                    echo DB_PATH from vars: %DB_PATH%


                    if "%DB_PATH%"=="" (

                        set "DB_PATH=%QUIZZ_BIN%\\Server\\Database\\MySql"
                    )


                    echo Using DB_PATH: %DB_PATH%


                    if not exist "%DB_PATH%" (

                        echo DB_PATH not found.

                        if exist "%QUIZZ_PROJECT%\\bin\\Server\\Database\\MySql" (

                            set "DB_PATH=%QUIZZ_PROJECT%\\bin\\Server\\Database\\MySql"
                        )
                    )


                    if not exist "%DB_PATH%" (

                        if exist "%APPZ_ARTIFACTS%\\lib\\AppzillonServer\\quizzz\\Database\\MySql" (

                            set "DB_PATH=%APPZ_ARTIFACTS%\\lib\\AppzillonServer\\quizzz\\Database\\MySql"
                        )
                    )


                    echo Final DB_PATH: %DB_PATH%


                    if not exist "%DB_PATH%" (

                        echo WARNING: DB_PATH not found.

                        goto DB_SKIP
                    )


                    dir "%DB_PATH%\\*.sql" >nul 2>&1


                    if errorlevel 1 (

                        echo No SQL files found.

                        goto DB_SKIP
                    )


                    echo.
                    echo Found SQL files.

                    echo Executing SQL files...


                    for %%f in ("%DB_PATH%\\*.sql") do (

                        echo.
                        echo ==========================================

                        echo Executing: %%~nxf

                        echo ==========================================


                        echo USE %DB_NAME%; > "%TEMP%\\%%~nxf.tmp"

                        type "%%f" >> "%TEMP%\\%%~nxf.tmp"


                        "%MYSQL_EXE%" `
                            -u %DB_USER% `
                            -p%DB_PASS% `
                            %DB_NAME% `
                            < "%TEMP%\\%%~nxf.tmp"


                        if errorlevel 1 (

                            echo ERROR executing %%~nxf

                        )
                        else (

                            echo Successfully executed %%~nxf
                        )


                        del "%TEMP%\\%%~nxf.tmp" >nul 2>&1
                    )


                    echo.
                    echo Verifying tables...


                    "%MYSQL_EXE%" `
                        -u %DB_USER% `
                        -p%DB_PASS% `
                        -D %DB_NAME% `
                        -e "SHOW TABLES;"


                    :DB_SKIP

                    echo.

                    echo DB stage completed
                '''


                // ====================================================
                // TOMCAT DEPLOYMENT
                // ====================================================

                bat '''
                    @echo off

                    echo.
                    echo ==========================================
                    echo TOMCAT SHUTDOWN AND WAR DEPLOYMENT
                    echo ==========================================


                    echo TOMCAT HOME: %APPZ_HOME%

                    echo TOMCAT PORT: %TOMCAT_PORT%


                    set "WEB_WAR="

                    set "SERVER_WAR="


                    if exist "%WORKSPACE%\\appzillon_vars.txt" (

                        for /f "tokens=1,* delims==" %%a in (
                            'type "%WORKSPACE%\\appzillon_vars.txt" ^| findstr WEB_WAR'
                        ) do set "WEB_WAR=%%b"


                        for /f "tokens=1,* delims==" %%a in (
                            'type "%WORKSPACE%\\appzillon_vars.txt" ^| findstr SERVER_WAR'
                        ) do set "SERVER_WAR=%%b"
                    }


                    echo WEB_WAR: %WEB_WAR%

                    echo SERVER_WAR: %SERVER_WAR%


                    if "%WEB_WAR%"=="" (

                        set "WEB_WAR=%APPZ_ARTIFACTS%\\quizzz.war"
                    )


                    if "%SERVER_WAR%"=="" (

                        set "SERVER_WAR=%APPZ_ARTIFACTS%\\AppzillonServer.war"
                    )


                    echo Final WEB_WAR: %WEB_WAR%

                    echo Final SERVER_WAR: %SERVER_WAR%


                    if not exist "%WEB_WAR%" (

                        echo ERROR: WEB WAR not found:

                        echo %WEB_WAR%

                        exit /b 1
                    )


                    echo.
                    echo ==========================================
                    echo SHUTTING DOWN TOMCAT
                    echo ==========================================


                    call "%APPZ_HOME%\\bin\\shutdown.bat"


                    echo shutdown.bat executed.


                    echo.

                    echo Waiting 5 seconds...

                    ping 127.0.0.1 -n 6 >nul


                    echo Killing remaining process on port %TOMCAT_PORT%...


                    for /f "tokens=5" %%a in (
                        'netstat -ano ^| findstr :%TOMCAT_PORT% ^| findstr LISTENING'
                    ) do (

                        echo Killing PID %%a

                        taskkill /F /PID %%a >nul 2>&1
                    )


                    ping 127.0.0.1 -n 3 >nul


                    echo.
                    echo ==========================================
                    echo CLEANING OLD DEPLOYMENTS
                    echo ==========================================


                    rmdir /S /Q "%APPZ_HOME%\\webapps\\quizzz" >nul 2>&1

                    rmdir /S /Q "%APPZ_HOME%\\webapps\\AppzillonServer" >nul 2>&1

                    del /F /Q "%APPZ_HOME%\\webapps\\quizzz.war" >nul 2>&1

                    del /F /Q "%APPZ_HOME%\\webapps\\AppzillonServer.war" >nul 2>&1


                    rmdir /S /Q "%APPZ_HOME%\\work\\Catalina\\localhost\\quizzz" >nul 2>&1

                    rmdir /S /Q "%APPZ_HOME%\\work\\Catalina\\localhost\\AppzillonServer" >nul 2>&1


                    echo.
                    echo ==========================================
                    echo COPYING NEW WARS
                    echo ==========================================


                    copy /Y "%WEB_WAR%" "%APPZ_HOME%\\webapps\\quizzz.war"


                    if errorlevel 1 (

                        echo ERROR: Failed to copy Web WAR.

                        exit /b 1
                    )


                    echo Web WAR copied successfully.


                    if exist "%SERVER_WAR%" (

                        echo.

                        echo Copying Server WAR...


                        copy /Y "%SERVER_WAR%" "%APPZ_HOME%\\webapps\\AppzillonServer.war"


                        if errorlevel 1 (

                            echo ERROR: Failed to copy Server WAR.

                            exit /b 1
                        )


                        echo Server WAR copied successfully.
                    )


                    echo.
                    echo ==========================================
                    echo STARTING TOMCAT
                    echo ==========================================


                    set "JAVA_HOME=%JAVA_HOME%"

                    set "PATH=%JAVA_HOME%\\bin;%PATH%"

                    set "CATALINA_HOME=%APPZ_HOME%"

                    set "JENKINS_NODE_COOKIE=dontKillMe"


                    echo JAVA_HOME: %JAVA_HOME%

                    echo CATALINA_HOME: %CATALINA_HOME%


                    call "%APPZ_HOME%\\bin\\catalina.bat" start


                    echo.

                    echo Waiting for Tomcat...


                    ping 127.0.0.1 -n 21 >nul


                    echo.
                    echo ==========================================
                    echo CHECKING TOMCAT PORT
                    echo ==========================================


                    netstat -ano | findstr :%TOMCAT_PORT% | findstr LISTENING


                    if errorlevel 1 (

                        echo WARNING: Tomcat port not listening yet.

                        ping 127.0.0.1 -n 10 >nul

                        netstat -ano | findstr :%TOMCAT_PORT%

                    )
                    else (

                        echo Tomcat is LISTENING.
                    )


                    echo.
                    echo ==========================================
                    echo TOMCAT LOGS
                    echo ==========================================


                    if exist "%APPZ_HOME%\\logs\\catalina.out" (

                        powershell -Command "Get-Content '%APPZ_HOME%\\logs\\catalina.out' -Tail 40"

                    )
                    else (

                        echo catalina.out not found.

                        dir "%APPZ_HOME%\\logs\\"
                    )


                    echo.

                    echo Checking webapps...


                    dir "%APPZ_HOME%\\webapps\\" | findstr quizzz

                    dir "%APPZ_HOME%\\webapps\\" | findstr Appzillon


                    if exist "%APPZ_HOME%\\webapps\\quizzz" (

                        echo quizzz exploded directory exists.

                    )
                    else (

                        echo WARNING: quizzz exploded directory not created yet.
                    )
                '''
            }
        }


        // ============================================================
        // APPZILLON HEALTH CHECK
        // ============================================================

        stage('Appzillon Health Check') {

            steps {

                echo '=========================================='
                echo 'CHECKING APPZILLON'
                echo '=========================================='


                bat '''
                    @echo off


                    echo.
                    echo Appzillon URL:

                    echo %APPZILLON_URL%


                    echo.
                    echo Tomcat Port:

                    echo %TOMCAT_PORT%


                    set RETRIES=30


                    :CHECK_APPZILLON


                    echo.
                    echo Checking Appzillon...

                    echo Attempts remaining: %RETRIES%


                    curl -s -o nul -w "%%{http_code}" "%APPZILLON_URL%" |
                        findstr "200 302"


                    if not errorlevel 1 (

                        echo.

                        echo ==========================================

                        echo APPZILLON IS RUNNING

                        echo ==========================================

                        echo URL:

                        echo %APPZILLON_URL%

                        exit /b 0
                    )


                    curl -s -o nul -w "%%{http_code}" "%APPZILLON_URL%" |
                        findstr "404"


                    if not errorlevel 1 (

                        echo.

                        echo Appzillon returned 404.

                        echo Application may still be deploying.
                    )


                    set /a RETRIES-=1


                    if %RETRIES% LEQ 0 (

                        echo.

                        echo ==========================================

                        echo APPZILLON HEALTH CHECK TIMEOUT

                        echo ==========================================


                        echo.

                        echo TOMCAT PORT STATUS


                        netstat -ano | findstr :%TOMCAT_PORT%


                        echo.

                        echo TOMCAT LOGS


                        if exist "%APPZ_HOME%\\logs\\catalina.out" (

                            powershell -Command "Get-Content '%APPZ_HOME%\\logs\\catalina.out' -Tail 50"

                        )
                        else (

                            dir "%APPZ_HOME%\\logs\\"
                        )


                        echo.

                        echo WEBAPPS


                        dir "%APPZ_HOME%\\webapps\\"


                        netstat -ano |
                            findstr :%TOMCAT_PORT% |
                            findstr LISTENING >nul


                        if not errorlevel 1 (

                            echo.

                            echo WARNING:

                            echo Tomcat is listening.

                            echo Continuing pipeline.

                            exit /b 0
                        )


                        exit /b 1
                    }


                    echo.

                    echo Waiting 5 seconds...


                    ping 127.0.0.1 -n 6 >nul


                    goto CHECK_APPZILLON
                '''
            }
        }


        // ============================================================
        // OPEN APPZILLON
        // ============================================================

        stage('Open Appzillon Popup') {

            steps {

                echo '=========================================='

                echo 'OPENING APPZILLON'

                echo '=========================================='


                bat '''
                    @echo off


                    echo URL:

                    echo %APPZILLON_URL%


                    start "" "%APPZILLON_URL%"


                    ping 127.0.0.1 -n 3 >nul


                    echo Appzillon browser popup triggered.


                    if exist "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe" (

                        start "" `
                            "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe" `
                            "%APPZILLON_URL%"
                    )


                    echo.

                    echo Waiting 8 seconds for UI...

                    ping 127.0.0.1 -n 9 >nul
                '''
            }
        }


        // ============================================================
        // PLAYWRIGHT
        // ============================================================

        stage('Playwright UI Tests - After Open') {

            steps {

                echo '=========================================='

                echo 'PLAYWRIGHT UI TESTS'

                echo '=========================================='


                bat '''
                    @echo off


                    echo Playwright dir:

                    echo %PLAYWRIGHT_DIR%


                    echo Appzillon URL:

                    echo %APPZILLON_URL%


                    if not exist "%PLAYWRIGHT_DIR%" (

                        echo ERROR: Playwright directory not found.

                        echo %PLAYWRIGHT_DIR%

                        exit /b 1
                    )


                    if not exist "%PLAYWRIGHT_DIR%\\package.json" (

                        echo ERROR: package.json missing.

                        dir "%PLAYWRIGHT_DIR%"

                        exit /b 1
                    )


                    echo.

                    echo Checking tests...


                    dir "%PLAYWRIGHT_DIR%\\tests"


                    echo.

                    cd /d "%PLAYWRIGHT_DIR%"


                    echo.

                    echo ==========================================

                    echo RUNNING PLAYWRIGHT

                    echo ==========================================


                    npx playwright test tests/05-home-quiz-flow.spec.js --headed --project=chromium 2>&1


                    set PW_EXIT=%errorlevel%


                    echo.

                    echo Playwright exit code:

                    echo %PW_EXIT%


                    if %PW_EXIT% NEQ 0 (

                        echo.

                        echo WARNING:

                        echo Some Playwright tests failed.


                        if exist "playwright-report\\index.html" (

                            echo Opening report...

                            start "" "playwright-report\\index.html"
                        )


                        echo Playwright completed with failures.

                    )
                    else (

                        echo.

                        echo ==========================================

                        echo ALL PLAYWRIGHT TESTS PASSED

                        echo ==========================================


                        if exist "playwright-report\\index.html" (

                            start "" "playwright-report\\index.html"
                        )
                    )


                    exit /b 0
                '''
            }
        }
    }


    // ============================================================
    // POST ACTIONS
    // ============================================================

    post {

        success {

            echo '=========================================='

            echo 'QUIZAPP DEPLOYMENT SUCCESSFUL - NANBA!'

            echo '=========================================='


            echo 'Backend: http://localhost:8080/api/user/getQuizzes'

            echo 'Appzillon: http://localhost:9090/quizapp'

            echo 'AppzillonServer: http://localhost:9090/AppzillonServer/Appzillon'


            echo '=========================================='
        }


        failure {

            echo '=========================================='

            echo 'QUIZAPP DEPLOYMENT FAILED - CHECK LOGS DA!'

            echo '=========================================='


            echo 'Check the stage that failed.'

            echo 'Backend log: backend.log (workspace)'

            echo "Tomcat logs: ${APPZ_HOME}/logs/"


            echo '=========================================='
        }
    }
}







