xcopy "eclipse\CraftGuide\src" "src\minecraft" /S /I /Y
runtime\bin\python\python_mcp runtime\recompile.py --no-server
runtime\bin\python\python_mcp runtime\reobfuscate.py --no-server
del /S /Q "src\minecraft\net\minecraft\src\CraftGuide\*"
del /Q "src\minecraft\net\minecraft\src\mod_CraftGuide.java"
del /S /Q zip\build\*
xcopy "eclipse\CraftGuide\gui" "zip\build\gui" /S /I /Y
xcopy "reobf\minecraft\CraftGuide" "zip\build\CraftGuide" /S /I /Y
xcopy "reobf\minecraft\mod_CraftGuide.class" "zip\build\" /Y
cd zip\build
7z d ..\CraftGuide-test-build.zip "*"
7z a ..\CraftGuide-test-build.zip "*"