@mkdir version-%1 && ^
cp changes.txt version-%1/ && ^
cp build/libs/CraftGuide-%1-forge.jar version-%1/ && ^
mv version-%1 %1 && ^
pscp -r -i [REMOVED: path to upload private key].ppk %1 [REMOVED: server username and directory structure] && ^
mv %1 version-%1 && ^
rmdir /S /Q version-%1
@echo {"mod-version":"%1","minecraft-versions":"%2"}>>version-list.txt&&^
pscp -r -i [REMOVED: path to upload private key].ppk version-list.txt [REMOVED: server username and directory structure]