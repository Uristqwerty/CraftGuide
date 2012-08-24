xcopy "eclipse\CraftGuide\src" "src\minecraft" /S /I /Y
xcopy "eclipse\Datafile\src"  "src\minecraft" /S /I /Y

runtime\bin\python\python_mcp runtime\recompile.py
runtime\bin\python\python_mcp runtime\reobfuscate.py

rmdir /S /Q "src\minecraft\net\minecraft\src\CraftGuide"
rmdir /S /Q "src\minecraft\uristqwerty"
rmdir /S /Q zip\build
rmdir /S /Q zip\build-resources

xcopy "eclipse\CraftGuide\gui" "zip\build\gui" /S /I /Y
xcopy "reobf\minecraft\CraftGuide" "zip\build\CraftGuide" /S /I /Y
xcopy "reobf\minecraft\uristqwerty\CraftGuide" "zip\build\uristqwerty\CraftGuide" /S /I /Y
xcopy "reobf\minecraft\uristqwerty\datafile" "zip\build\uristqwerty\datafile" /S /I /Y
xcopy "eclipse\CraftGuide\default_theme.txt" "zip\build\" /Y
xcopy "eclipse\CraftGuide\mcmod.info" "zip\build\" /Y

xcopy "eclipse\CraftGuide\CraftGuide Themes" "zip\build-resources\CraftGuide Themes" /S /I /Y

cd zip\build-resources
7z d ..\build\uristqwerty\CraftGuide\resources.zip "*"
7z a ..\build\uristqwerty\CraftGuide\resources.zip "*"

cd ..\build
7z d ..\CraftGuide-test-build.zip "*"
7z a ..\CraftGuide-test-build.zip "*"
