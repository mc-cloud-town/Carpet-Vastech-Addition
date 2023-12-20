@echo off
echo Cleaning previous installations ...
rd /s /q build\tmp\fullRelease
mkdir build\tmp\fullRelease
echo Copying server ...
copy %userprofile%\.gradle\caches\minecraft\net\minecraft\minecraft_server\1.12.2\minecraft_server-1.12.2.jar build\tmp\fullRelease > nul
mkdir build\tmp\fullRelease\patches
echo Extracting patches ...
7z x build\distributions\Carpetmod_dev.zip -bd -obuild\tmp\fullRelease\patches > nul
echo Patch work ...
7z a build\tmp\fullRelease\minecraft_server-1.12.2.jar .\build\tmp\fullRelease\patches\* > nul
echo Applying world edit ...
rd /s /q build\tmp\fullRelease\patches
mkdir build\tmp\fullRelease\patches
7z x worldedit-core-6.1.jar -bd -obuild\tmp\fullRelease\patches > nul
7z a build\tmp\fullRelease\minecraft_server-1.12.2.jar .\build\tmp\fullRelease\patches\* > nul
echo Cleanup ...
rd /s /q build\tmp\fullRelease\patches
move /y build\tmp\fullRelease\minecraft_server-1.12.2.jar ..\releases\VasCM_latest.jar > nul
: move /y build\tmp\fullRelease\minecraft_server-1.12.2.jar %appdata%\.minecraft\saves\minecraft_server.1.12.2_carpet_test.jar > nul
: pushd %appdata%\.minecraft\saves
: echo Starting server ...
: java -jar minecraft_server.1.12.2_carpet_test.jar --nogui
: popd
