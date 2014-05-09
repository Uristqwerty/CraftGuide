CraftGuide
==========

CraftGuide is a Minecraft mod started back in October 2011, with the intention of providing the functionality of Risugami's Recipe Book, but a user interface which could better handle the absurd number of recipes encountered when playing with certain mods.

Since then, it has slowly developed features to enhance the main functionality, such as making the GUI resizable to comfortably handle screens of all sizes at all GUI scales, and adding an API so that other mods can display recipes which wouldn't normally be visible (such as those for machines, or custom implementations of IRecipe).


### Contributing
If you want to contribute to CraftGuide, simply submit an issue or pull request.

Things currently needed:
- Translations to other languages
- Advice on improving the project layout (including how to best credit contributors)
- Advice on improving the compile process
- Advice on improving this README.md
- Anything you are interested in working on


### Compiling
CraftGuide's current compile process is a horrible kludge that barely works:

1. First, set up MCP, including decompiling. It is strongly recommended that there aren't any other projects present in the MCP directory.
2. Second, put the CraftGuide project (the contents of this repository) in &lt;MCP-directory&gt;/eclipse/CraftGuide
3. Third, place required APIs in &lt;MCP-directory&gt;/eclipse/CraftGuide/APIs
4. Fourth, if compiling without Forge set up with MCP, create stubs of any referenced classes and methods in &lt;MCP-directory&gt;/eclipse/CraftGuide/ForgeStubs
5. Finally, run &lt;MCP-directory&gt;/eclipse/CraftGuide/build.bat from &lt;MCP-directory&gt;

This will produce
- &lt;MCP-directory&gt;/zip/CraftGuide-&lt;version&gt;.zip, and
- &lt;MCP-directory&gt;/zip/CraftGuide-&lt;version&gt;.litemod


build.bat has quite a few issues:
- It's a .bat file, so only works on Windows
- It uses perl, so that must be included in PATH, somewhere
- It copies files into &lt;MCP-directory&gt;/src/minecraft, which may overwrite files from other mods, if any are present
- It later deletes those files from &lt;MCP-directory&gt;/src/minecraft, which may delete files from other mods, as well

**If you have any advice on how to improve this process, I would love to hear it.** *(Other than "use ForgeGradle", unless it can be done without removing the current process, as I want to maintain the possibility of using other loaders in the future, or even no loader at all)*
