xcopy "eclipse\CraftGuide\src" "src\minecraft" /S /I /Y
xcopy "eclipse\QuickGuide\src" "src\minecraft" /S /I /Y
xcopy "eclipse\BrewGuide\src"  "src\minecraft" /S /I /Y
xcopy "eclipse\CraftHide\src"  "src\minecraft" /S /I /Y
xcopy "eclipse\RP2Dummy\src"  "src\minecraft" /S /I /Y

runtime\bin\python\python_mcp runtime\recompile.py
runtime\bin\python\python_mcp runtime\reobfuscate.py

rmdir /S /Q "src\minecraft\net\minecraft\src\CraftGuide"
rmdir /S /Q "src\minecraft\eloraam"
del /Q "src\minecraft\net\minecraft\src\mod_CraftGuide.java"
del /Q "src\minecraft\RP2Recipes.java"
del /Q "src\minecraft\RedPowerBase.java"
rmdir /S /Q zip\build

del /Q "src\minecraft\net\minecraft\src\mod_QuickGuide.java"
rmdir /S /Q zip\build-QuickGuide

del /Q "src\minecraft\net\minecraft\src\mod_BrewGuide.java"
del /Q "src\minecraft\net\minecraft\src\BrewGuideRecipes.java"
rmdir /S /Q zip\build-BrewGuide

del /Q "src\minecraft\net\minecraft\src\mod_CraftHide.java"
del /Q "src\minecraft\net\minecraft\src\CraftHideFilter.java"
rmdir /S /Q zip\build-CraftHide

xcopy "eclipse\CraftGuide\gui" "zip\build\gui" /S /I /Y
xcopy "reobf\minecraft\CraftGuide" "zip\build\CraftGuide" /S /I /Y
xcopy "reobf\minecraft\mod_CraftGuide.class" "zip\build\" /Y
xcopy "reobf\minecraft\RP2Recipes.class" "zip\build" /Y

xcopy "reobf\minecraft\mod_QuickGuide.class" "zip\build-QuickGuide\" /Y

xcopy "eclipse\BrewGuide\gui" "zip\build-BrewGuide\gui" /S /I /Y
xcopy "reobf\minecraft\mod_BrewGuide.class" "zip\build-BrewGuide\" /Y
xcopy "reobf\minecraft\BrewGuideRecipes.class" "zip\build-BrewGuide\" /Y

xcopy "reobf\minecraft\mod_CraftHide.class" "zip\build-CraftHide\" /Y
xcopy "reobf\minecraft\CraftHideFilter.class" "zip\build-CraftHide\" /Y

cd zip\build
7z d ..\CraftGuide-test-build.zip "*"
7z a ..\CraftGuide-test-build.zip "*"

cd ..\build-QuickGuide
7z d ..\QuickGuide-test-build.zip "*"
7z a ..\QuickGuide-test-build.zip "*"

cd ..\build-BrewGuide
7z d ..\BrewGuide-test-build.zip "*"
7z a ..\BrewGuide-test-build.zip "*"

cd ..\build-CraftHide
7z d ..\CraftHide-test-build.zip "*"
7z a ..\CraftHide-test-build.zip "*"