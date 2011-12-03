xcopy "eclipse\CraftGuide\src" "src\minecraft" /S /I /Y
xcopy "eclipse\QuickGuide\src" "src\minecraft" /S /I /Y
xcopy "eclipse\BrewGuide\src"  "src\minecraft" /S /I /Y

runtime\bin\python\python_mcp runtime\recompile.py
runtime\bin\python\python_mcp runtime\reobfuscate.py

del /S /Q "src\minecraft\net\minecraft\src\CraftGuide\*"
del /Q "src\minecraft\net\minecraft\src\mod_CraftGuide.java"
del /S /Q zip\build\*

del /Q "src\minecraft\net\minecraft\src\mod_QuickGuide.java"
del /S /Q zip\build-QuickGuide\*

del /Q "src\minecraft\net\minecraft\src\mod_BrewGuide.java"
del /Q "src\minecraft\net\minecraft\src\BrewGuideRecipes.java"
del /S /Q zip\build-BrewGuide\*

xcopy "eclipse\CraftGuide\gui" "zip\build\gui" /S /I /Y
xcopy "reobf\minecraft\CraftGuide" "zip\build\CraftGuide" /S /I /Y
xcopy "reobf\minecraft\mod_CraftGuide.class" "zip\build\" /Y

xcopy "reobf\minecraft\mod_QuickGuide.class" "zip\build-QuickGuide\" /Y

xcopy "eclipse\BrewGuide\gui" "zip\build-BrewGuide\gui" /S /I /Y
xcopy "reobf\minecraft\mod_BrewGuide.class" "zip\build-BrewGuide\" /Y
xcopy "reobf\minecraft\BrewGuideRecipes.class" "zip\build-BrewGuide\" /Y

cd zip\build
7z d ..\CraftGuide-test-build.zip "*"
7z a ..\CraftGuide-test-build.zip "*"

cd ..\build-QuickGuide
7z d ..\QuickGuide-test-build.zip "*"
7z a ..\QuickGuide-test-build.zip "*"

cd ..\build-BrewGuide
7z d ..\BrewGuide-test-build.zip "*"
7z a ..\BrewGuide-test-build.zip "*"