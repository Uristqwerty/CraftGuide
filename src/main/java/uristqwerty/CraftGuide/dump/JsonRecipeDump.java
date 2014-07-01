package uristqwerty.CraftGuide.dump;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.google.gson.stream.JsonWriter;

public class JsonRecipeDump extends RecipeDump
{
	JsonWriter writer;

	@Override
	void startWriting(OutputStream output) throws IOException
	{
		writer = new JsonWriter(new OutputStreamWriter(output));
		writer.setIndent("\t");
	}

	@Override
	void stopWriting() throws IOException
	{
		writer.flush();
	}

	@Override
	void startArray() throws IOException
	{
		writer.beginArray();
	}

	@Override
	void endArray() throws IOException
	{
		writer.endArray();
	}

	@Override
	void startObject(String type) throws IOException
	{
		writer.beginObject();
	}

	@Override
	void endObject() throws IOException
	{
		writer.endObject();
	}

	@Override
	void writeString(String type, String value) throws IOException
	{
		writer.value(value);
	}

	@Override
	void writeStringValue(String type, String name, String value) throws IOException
	{
		writer.name(name).value(value);
	}

	@Override
	void startObjectValue(String name, String type) throws IOException
	{
		writer.name(name).beginObject();
	}

	@Override
	void endObjectValue() throws IOException
	{
		writer.endObject();
	}

	@Override
	void startArrayValue(String name) throws IOException
	{
		writer.name(name).beginArray();
	}

	@Override
	void endArrayValue() throws IOException
	{
		writer.endArray();
	}
}
