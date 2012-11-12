use strict;
use warnings;

my $override = "";
my $version = "";
my $build = "";

open(my $versionfile, "<", "eclipse/CraftGuide/version.txt") or die "Could not read from version file";

while(<$versionfile>)
{
    if($_ =~ /([^\t]+)\t+([^\t\n]*)\n?/)
    {
        if($1 eq "BUILD")
        {
            $build = $2;
        }
        elsif($1 eq "VERSION")
        {
            $version = $2;
        }
        elsif($1 eq "OVERRIDE")
        {
            $override = $2;
        }
    }
}

my $versionstring = "$version.$build";

if($override ne "")
{
    $versionstring = $override;
}

print "Version string: $versionstring\n";
close $versionfile;
my $infile;
my $outfile;
if(open($infile, "<", "eclipse/CraftGuide/src/uristqwerty/CraftGuide/CraftGuide.java"))
{
    open($outfile, ">", "src/minecraft/uristqwerty/CraftGuide/CraftGuide.java");

    while(<$infile>)
    {
        if($_ =~ /\@Mod\(modid = "(.+)", name = "(.+)", version = ".+"\)/)
        {
            print $outfile "\@Mod(modid = \"$1\", name = \"$2\", version = \"$versionstring\")\n";
        }
        else
        {
            print $outfile "$_";
        }
    }

    close($infile);
    close($outfile);
}
open($infile, "<", "eclipse/CraftGuide/src/mcmod.info");
open($outfile, ">", "eclipse/CraftGuide/out/mcmod.info");

while(<$infile>)
{
    if($_ =~ /  "version": ".+",/)
    {
        print $outfile "  \"version\": \"$versionstring\",\n";
    }
    else
    {
        print $outfile "$_";
    }
}

close($infile);
close($outfile);

if($override eq "")
{
    $build += 1;

    open($versionfile, ">", "eclipse/CraftGuide/version.txt");
    print $versionfile "BUILD\t\t$build\n";
    print $versionfile "VERSION\t\t$version\n";
    print $versionfile "OVERRIDE\t$override\n";
    close $versionfile;
}

my $buildzip;

if(open($buildzip, ">", "eclipse/CraftGuide/build-zip.bat"))
{
    print $buildzip "cd zip\\build\n";
    print $buildzip "if exist ..\\CraftGuide-$versionstring.zip del ..\\CraftGuide-$versionstring.zip\n";
    print $buildzip "7z a ..\\CraftGuide-$versionstring.zip \"*\"\n";
    print $buildzip "cd ..\\..\n";
    close($buildzip);
}

if(open($buildzip, ">", "eclipse/CraftGuide/build-zip-modloader.bat"))
{
    print $buildzip "cd zip\\build-modloader\n";
    print $buildzip "if exist ..\\CraftGuide-$versionstring-modloader.zip del ..\\CraftGuide-$versionstring-modloader.zip\n";
    print $buildzip "7z a ..\\CraftGuide-$versionstring-modloader.zip \"*\"\n";
    print $buildzip "cd ..\\..\n";
    close($buildzip);
}
