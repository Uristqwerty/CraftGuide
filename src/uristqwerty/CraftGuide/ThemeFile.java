package uristqwerty.CraftGuide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import uristqwerty.datafile.DataNode;

public class ThemeFile
{
	private DataNode rootNode = new DataNode();
	
	public ThemeFile(File file) throws FileNotFoundException
	{
		this(new FileReader(file));
	}

	public ThemeFile(InputStream stream)
	{
		this(new InputStreamReader(stream));
	}
	
	public ThemeFile(Reader reader)
	{
		rootNode.load(reader);
	}
	
	public ThemeFile()
	{
	}
	
	public String[] getMeta()
	{
		String[] meta = {
			rootNode.child("meta.name").value(),
			rootNode.child("meta.version").value(),
			rootNode.child("meta.author").value(),
		};
		
		return meta;
	}
	
	public void merge(ThemeFile other)
	{
		rootNode.merge(other.rootNode);
	}
	
	public Theme parse()
	{
		Theme theme = new Theme();
		
		for(DataNode node: rootNode.child("image").children())
		{
			theme.addImage(node.name(), node.value());
		}
		
		return theme;
	}
}
