package application;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import application.helpers.XMLNode;

public class JSON {
	StringBuilder sb = new StringBuilder();

	String createJson(XMLNode node) {
		boolean obJSON_diff_flag = false;
		// start JSON BY ITS FIRST CURLY BRACKET
		sb.append("{ \n");
		// check if the children if they are array of objects or keys
		for (int i = 1; i < node.childrenNodes.size(); i++) {
			if (node.childrenNodes.get(i).tagName != node.childrenNodes.get(i - 1).tagName) {
				obJSON_diff_flag = true;
			}
		}

		// if the children are different
		if (obJSON_diff_flag == true) {
			for (int i = 1; i < node.childrenNodes.size(); i++) {
				sb.append("\"");
				sb.append(node.childrenNodes.get(i).tagName);
				sb.append("\" :");
				// Check if still have children
				if (node.childrenNodes.get(i).childrenNodes != null)
					sb.append(createJson(node.childrenNodes.get(i)));
				// if not >>> print inner text
				else {
					sb.append("\"");
					sb.append(node.childrenNodes.get(i).innerText);
					sb.append("\" \n");
				}
			}
		} else // else children are array of the object
		{
			sb.append("\"");
			sb.append(node.tagName);
			sb.append("\" :[ \n");
			for (int i = 1; i < node.childrenNodes.size(); i++) {
				sb.append(createJson(node.childrenNodes.get(i)));
			}
			sb.append(" ]\n");
		}
		// JSON END CURLY BRAKET
		sb.append(" }\n");
		// CONVERT
		return sb.toString();
	}

	boolean JsonSaveFile(XMLNode node, String filePath) {
		try (

				// Create a FileOutputStream to write to the file
				OutputStream outputStream = new FileOutputStream(filePath);

				// Create an OutputStreamWriter with US-ASCII encoding
				Writer writer = new OutputStreamWriter(outputStream)) {
			// Write the content to the file
			writer.write(createJson(node));
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String formatJSONStr(final String json_str, final int indent_width) {
		final char[] chars = json_str.toCharArray();
		final String newline = System.lineSeparator();
	
		String ret = "";
		boolean begin_quotes = false;
	
		for (int i = 0, indent = 0; i < chars.length; i++) {
			char c = chars[i];
	
			if (c == '\"') {
				ret += c;
				begin_quotes = !begin_quotes;
				continue;
			}
	
			if (!begin_quotes) {
				switch (c) {
				case '{':
				case '[':
					ret += c + newline + String.format("%" + (indent += indent_width) + "s", "");
					continue;
				case '}':
				case ']':
					ret += newline + ((indent -= indent_width) > 0 ? String.format("%" + indent + "s", "") : "") + c;
					continue;
				case ':':
					ret += c + " ";
					continue;
				case ',':
					ret += c + newline + (indent > 0 ? String.format("%" + indent + "s", "") : "");
					continue;
				default:
					if (Character.isWhitespace(c)) continue;
				}
			}
	
			ret += c + (c == '\\' ? "" + chars[++i] : "");
		}
	
		return ret;
	}
}
