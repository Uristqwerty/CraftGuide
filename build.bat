xcopy "eclipse\CraftGuide\src" "src\minecraft" /S /I /Y
perl eclipse\CraftGuide\setversion.pl
del src\minecraft\mcmod.info

runtime\bin\python\python_mcp runtime\recompile.py
runtime\bin\python\python_mcp runtime\reobfuscate.py

cd eclipse\CraftGuide
call build-resources.bat
cd ..\..

rmdir /S /Q "src\minecraft\net\minecraft\src\CraftGuide"
rmdir /S /Q "src\minecraft\uristqwerty\gui"
rmdir /S /Q "src\minecraft\uristqwerty\CraftGuide"
rmdir /S /Q "src\minecraft\ic2"
rmdir /S /Q zip\build
del src\minecraft\net\minecraft\src\mod_CraftGuide.java

if not exist zip\build mkdir zip\build

xcopy "eclipse\CraftGuide\images\gui" "zip\build\gui" /S /I /Y
xcopy "reobf\minecraft\CraftGuide" "zip\build\CraftGuide" /S /I /Y
xcopy "reobf\minecraft\uristqwerty\CraftGuide" "zip\build\uristqwerty\CraftGuide" /S /I /Y
xcopy "reobf\minecraft\uristqwerty\gui" "zip\build\uristqwerty\gui" /S /I /Y
xcopy "eclipse\CraftGuide\out\mcmod.info" "zip\build\" /Y
xcopy "eclipse\CraftGuide\CraftGuideResources.zip" "zip\build\uristqwerty\CraftGuide" /Y

xcopy "zip\build" "zip\build-modloader" /S /I /Y
xcopy "reobf\minecraft\mod_CraftGuide.class" "zip\build-modloader" /Y

del "zip\build\uristqwerty\CraftGuide\client\modloader\CraftGuideClient_ModLoader.class"

del "zip\build-modloader\uristqwerty\CraftGuide\CraftGuide_FML.class"
rmdir /S /Q "zip\build-modloader\uristqwerty\CraftGuide\client\fml"
del "zip\build-modloader\mcmod.info"


call eclipse\CraftGuide\build-zip.bat
call eclipse\CraftGuide\build-zip-modloader.bat
