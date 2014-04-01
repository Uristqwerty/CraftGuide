xcopy "eclipse\CraftGuide\src" "src\minecraft" /S /I /Y
xcopy "eclipse\CraftGuide\APIs" "src\minecraft" /S /I /Y
xcopy "eclipse\CraftGuide\ForgeStubs" "src\minecraft" /S /I /Y
perl eclipse\CraftGuide\setversion.pl
del src\minecraft\mcmod.info

runtime\bin\python\python_mcp runtime\recompile.py
rem runtime\bin\python\python_mcp runtime\reobfuscate.py

cd eclipse\CraftGuide
call build-resources.bat
cd ..\..

rmdir /S /Q "src\minecraft\net\minecraft\src\CraftGuide"
rmdir /S /Q "src\minecraft\uristqwerty\gui_craftguide"
rmdir /S /Q "src\minecraft\uristqwerty\CraftGuide"
rmdir /S /Q "src\minecraft\ic2"
del src\minecraft\net\minecraft\src\mod_CraftGuide.java

rmdir /S /Q zip\build
rmdir /S /Q zip\build-modloader

mkdir zip\build
mkdir zip\build-modloader

runtime\bin\python\python_mcp runtime\reobfuscate.py

rem xcopy "reobf\minecraft\CraftGuide" "zip\build-modloader\CraftGuide" /S /I /Y
rem xcopy "reobf\minecraft\uristqwerty\CraftGuide" "zip\build-modloader\uristqwerty\CraftGuide" /S /I /Y
rem xcopy "reobf\minecraft\uristqwerty\gui_craftguide" "zip\build-modloader\uristqwerty\gui_craftguide" /S /I /Y
rem xcopy "reobf\minecraft\mod_CraftGuide.class" "zip\build-modloader" /Y
rem xcopy "eclipse\CraftGuide\src\pack.mcmeta" "zip\build-modloader\" /Y
rem xcopy "eclipse\CraftGuide\assets" "zip\build-modloader\assets" /S /I /Y
rem xcopy "eclipse\CraftGuide\CraftGuideResources.zip" "zip\build-modloader\uristqwerty\CraftGuide" /Y

xcopy "reobf\minecraft\CraftGuide" "zip\build-liteloader\CraftGuide" /S /I /Y
xcopy "reobf\minecraft\uristqwerty\CraftGuide" "zip\build-liteloader\uristqwerty\CraftGuide" /S /I /Y
xcopy "reobf\minecraft\uristqwerty\gui_craftguide" "zip\build-liteloader\uristqwerty\gui_craftguide" /S /I /Y
xcopy "eclipse\CraftGuide\out\mcmod.info" "zip\build-liteloader\" /Y
xcopy "eclipse\CraftGuide\src\pack.mcmeta" "zip\build-liteloader\" /Y
xcopy "eclipse\CraftGuide\src\litemod.json" "zip\build-liteloader\" /Y
xcopy "eclipse\CraftGuide\assets" "zip\build-liteloader\assets" /S /I /Y
xcopy "eclipse\CraftGuide\CraftGuideResources.zip" "zip\build-liteloader\uristqwerty\CraftGuide" /Y

runtime\bin\python\python_mcp runtime\reobfuscate.py --srgnames

xcopy "reobf\minecraft\CraftGuide" "zip\build\CraftGuide" /S /I /Y
xcopy "reobf\minecraft\uristqwerty\CraftGuide" "zip\build\uristqwerty\CraftGuide" /S /I /Y
xcopy "reobf\minecraft\uristqwerty\gui_craftguide" "zip\build\uristqwerty\gui_craftguide" /S /I /Y
xcopy "eclipse\CraftGuide\out\mcmod.info" "zip\build\" /Y
xcopy "eclipse\CraftGuide\src\pack.mcmeta" "zip\build\" /Y
xcopy "eclipse\CraftGuide\assets" "zip\build\assets" /S /I /Y
xcopy "eclipse\CraftGuide\CraftGuideResources.zip" "zip\build\uristqwerty\CraftGuide" /Y

del "zip\build\uristqwerty\CraftGuide\client\modloader\CraftGuideClient_ModLoader.class"
del "zip\build\uristqwerty\CraftGuide\client\liteLoader\CraftGuideClient_LiteLoader.class"
del "zip\build\uristqwerty\CraftGuide\LiteMod_CraftGuide.class"

del "zip\build-modloader\uristqwerty\CraftGuide\CraftGuide_FML.class"
rmdir /S /Q "zip\build-modloader\uristqwerty\CraftGuide\client\fml"

del "zip\build-liteloader\uristqwerty\CraftGuide\CraftGuide_FML.class"
rmdir /S /Q "zip\build-liteloader\uristqwerty\CraftGuide\client\fml"


call eclipse\CraftGuide\build-zip.bat
call eclipse\CraftGuide\build-zip-liteloader.bat
rem call eclipse\CraftGuide\build-zip-modloader.bat
