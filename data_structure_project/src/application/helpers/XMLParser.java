package application.helpers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class XMLParser {
  public XMLNode rootNode;

  public String cleanFileContent(String content) {
    // responsible for removing any extra space
    content = content.trim();
    content = content.replaceAll("\n|\t|\r", "");
    content = content.replaceAll(" +", " ");
    content = content.replaceAll("< ?([A-Za-z]+) ?>", "<$1>");
    content = content.replaceAll("< ?/ ?([A-Za-z]+) ?>", "</$1>");
    content = content.replaceAll("> +<", "><");
    return content;
  }

  public String readFileContent(String filePath) {
    String fileContent = "";
    try {
      fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
    } catch (Exception e) {
      System.out.println("File doesn't exist");
      e.printStackTrace();
    }
    return fileContent;
  }

  public void parseXMLFile(String fileContent) {

    fileContent = cleanFileContent(fileContent);

    rootNode = new XMLNode();
    int leftBracketIndexOpening = fileContent.indexOf('<', 0);
    int rightBracketIndexOpening = fileContent.indexOf('>', leftBracketIndexOpening + 1);
    String tagName = fileContent.substring(leftBracketIndexOpening + 1, rightBracketIndexOpening);
    String closingTag = "</" + tagName + ">";
    int closingTagIndex = fileContent.indexOf(closingTag, rightBracketIndexOpening + 1);

    rootNode.tagName = tagName;
    rootNode.innerText = fileContent.substring(rightBracketIndexOpening + 1, closingTagIndex).trim();
    parseFileContent(rootNode);

  }

  private void parseFileContent(XMLNode root) {
    List<XMLNode> childrenNode = new ArrayList<>();
    int i = 0;
    while (i < root.innerText.length()) {
      XMLNode res = new XMLNode();
      int leftBracketIndexOpening = root.innerText.indexOf('<', i);
      if (leftBracketIndexOpening == -1) {
        break;
      }
      int rightBracketIndexOpening = root.innerText.indexOf('>', leftBracketIndexOpening + 1);
      String tagName = root.innerText.substring(leftBracketIndexOpening + 1, rightBracketIndexOpening);
      String closingTag = "</" + tagName + ">";
      int closingTagIndex = root.innerText.indexOf(closingTag, rightBracketIndexOpening + 1);
      i = closingTagIndex + closingTag.length();

      res.tagName = tagName;
      res.innerText = root.innerText.substring(rightBracketIndexOpening + 1, closingTagIndex).trim();

      parseFileContent(res);
      childrenNode.add(res);
    }
    if (!childrenNode.isEmpty()) {
      root.innerText = null;
    }
    root.childrenNodes = childrenNode;
  }

  public String compressFile(String fileContent) {
    // System.out.println(fileContent);
    String temp = fileContent.toString();
    String res = cleanFileContent(temp);
    System.out.println(res);
    return res;
  }

  public boolean saveFile(String fileContent, String filePath) {
    try (
      
        // Create a FileOutputStream to write to the file
        OutputStream outputStream = new FileOutputStream(filePath);

        // Create an OutputStreamWriter with US-ASCII encoding
        Writer writer = new OutputStreamWriter(outputStream)) {
      // Write the content to the file
      writer.write(fileContent);
          writer.close();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**************************************************************************
   *                       Xml Correction/Detection                         *
   **************************************************************************/
  public static List<String> xmlCorrection(String xml) {
    //next line to remove any spaces at the beginning of the line in the xml
    xml = xml.replaceAll("(?m)^\\s+", "");
    StringBuffer xmlBuffer = new StringBuffer(xml);
    Stack<String> stack = new Stack<String>(); //stack to hold openning tags
    String Tag = "";
    StringBuffer Tagbuffer = new StringBuffer();
    char[] chars = xml.toCharArray();
    int xmlcount = 0;
    int xmlbuffercount = 0;
    boolean endOfFile = false;
    List<Integer> errorIndex = new ArrayList<>();
    List<String> result = new ArrayList<>();
    try{
    while (xmlcount != chars.length) {
        if (chars[xmlcount] == '<') { //start by going through every tag in xml , tags start with '<' and ends with '>'
            Tag = "" + chars[xmlcount];
        } else if (chars[xmlcount] == '>') {
            Tag += chars[xmlcount];
            if (Tag.contains("/")) { //check if the tag is a closing tag
                if (isMatchingTag(stack.peek(), Tag)) { //if it matches the tag on top of the stack pop the last tag in stack
                    stack.pop();
                } else { //if it doesn't match then form a closing tag for the openning tag in the stack and insert it 
                    Tagbuffer = new StringBuffer(stack.peek());
                    Tagbuffer = Tagbuffer.insert(1, '/');
                    xmlBuffer = xmlBuffer.insert(xmlBuffer.indexOf(Tag, xmlbuffercount - Tag.length()), Tagbuffer.toString() + "\n");
                    errorIndex.add(xml.indexOf(Tag, xmlcount - Tag.length())); //index of the error to be used later
                    stack.pop();
                    xmlbuffercount = xmlbuffercount + Tagbuffer.length() - Tag.length();
                    xmlcount = xmlcount - Tag.length();
                }
            } else {
                if (!stack.empty()) {
                    //handling different cases of openning tags
					if (!(Tag.equals(stack.peek()))) { //if the stack not empty and current openning tag equals the tag on top of the stack we close it directly
                                stack.push(Tag); //if it is not equal we then push it in the stack

                                xmlcount++;
                                xmlbuffercount++;

                                endOfFile = (xmlcount + 1) >= chars.length - 1;
                                if (!endOfFile && chars[xmlcount] != '\n') { //if open tag and attribute and closing tag in one line

                                    while (!endOfFile && chars[xmlcount] != '<') {
                                        xmlcount++;
                                        xmlbuffercount++;
                                    }
                                    if (!endOfFile) {
                                        Tag = "" + chars[xmlcount];
                                    }

                                    while (!endOfFile && chars[xmlcount] != '>') {
                                        xmlcount++;
                                        xmlbuffercount++;
                                        Tag += chars[xmlcount];
                                    }
                                    if (!endOfFile) {
                                        if (Tag.contains("/")) {
                                            if (isMatchingTag(stack.peek(), Tag)) {
                                                stack.pop();
                                            } else {
                                                Tagbuffer = new StringBuffer(stack.peek());
                                                Tagbuffer = Tagbuffer.insert(1, '/');
                                                xmlBuffer = xmlBuffer.insert(xmlBuffer.indexOf(Tag, xmlbuffercount - Tag.length()) - 1, Tagbuffer.toString());
                                                errorIndex.add(xml.indexOf(Tag, xmlcount - Tag.length()));
                                                stack.pop();
                                                xmlbuffercount = xmlbuffercount + Tagbuffer.length() - Tag.length();
                                                xmlcount = xmlcount - Tag.length();
                                            }

                                        } else {
                                            Tagbuffer = new StringBuffer(stack.peek());
                                            Tagbuffer = Tagbuffer.insert(1, '/');
                                            xmlBuffer = xmlBuffer.insert(xmlBuffer.indexOf(Tag, xmlbuffercount - Tag.length()), Tagbuffer.toString());
                                            errorIndex.add(xml.indexOf(Tag, xmlcount - Tag.length()));
                                            stack.pop();
                                            xmlbuffercount = xmlbuffercount + Tagbuffer.length() - Tag.length();
                                            xmlcount = xmlcount - Tag.length();
                                        }
                                    }
                                    endOfFile = (xmlcount + 1) >= chars.length - 1;
                                } else if (!endOfFile && chars[xmlcount + 1] != '<') { //if open tag and attribute and closing tag in seperate lines
                                    while (!endOfFile && chars[xmlcount] != '<') {
                                        xmlcount++;
                                        xmlbuffercount++;
                                        endOfFile = xmlcount >= chars.length - 1;
                                    }
                                    if (!endOfFile) {
                                        Tag = "" + chars[xmlcount];
                                    }
                                    while ((!endOfFile) && (chars[xmlcount] != '>')) {
                                        xmlcount++;
                                        xmlbuffercount++;
                                        Tag += chars[xmlcount];
                                    }
                                    if (!endOfFile) {
                                        if (Tag.contains("/")) {
                                            if (isMatchingTag(stack.peek(), Tag)) {
                                                stack.pop();
                                            } else {
                                                Tagbuffer = new StringBuffer(stack.peek());
                                                Tagbuffer = Tagbuffer.insert(1, '/');
                                                xmlBuffer = xmlBuffer.insert(xmlBuffer.indexOf(Tag, xmlbuffercount - Tag.length()), Tagbuffer.toString() + "\n");
                                                stack.pop();
                                                xmlbuffercount = xmlbuffercount + Tagbuffer.length() - Tag.length();
                                                xmlcount = xmlcount - Tag.length();
                                                errorIndex.add(xml.indexOf(Tag, xmlcount - Tag.length()));

                                            }

                                        } else {
                                            Tagbuffer = new StringBuffer(stack.peek());
                                            Tagbuffer = Tagbuffer.insert(1, '/');
                                            xmlBuffer = xmlBuffer.insert(xmlBuffer.indexOf(Tag, xmlbuffercount - Tag.length()), Tagbuffer.toString() + "\n");
                                            errorIndex.add(xml.indexOf(Tag, xmlcount - Tag.length()));
                                            stack.pop();
                                            xmlbuffercount = xmlbuffercount + Tagbuffer.length() - Tag.length();
                                            xmlcount = xmlcount - Tag.length();
                                        }
                                    }
                                }
                            } else {
                                Tagbuffer = new StringBuffer(stack.peek());
                                Tagbuffer = Tagbuffer.insert(1, '/');
                                xmlBuffer = xmlBuffer.insert(xmlBuffer.indexOf(Tag, xmlbuffercount - Tag.length()), Tagbuffer.toString() + "\n");
                                errorIndex.add(xml.indexOf(Tag, xmlcount - Tag.length()));
                                stack.pop();
                                xmlbuffercount = xmlbuffercount + Tagbuffer.length() - Tag.length();
                                xmlcount = xmlcount - Tag.length();
                            }

                            Tag = "";


                } else { //if it is an openning tag push it in stack
                    stack.push(Tag);
                    Tag = "";
                }
            }
        } else if (!Tag.isEmpty()) { //forming tags by taking characters between < >
            Tag += chars[xmlcount];
        }
        xmlcount++;
        xmlbuffercount++;
    }
    
    //lastly check if the stack is not empty and close all the openning tags remaining
    if (!stack.empty()) {
        while (!stack.empty()) {

            Tagbuffer = new StringBuffer(stack.peek());
            Tagbuffer = Tagbuffer.insert(1, '/');
            xmlBuffer = xmlBuffer.insert(xmlBuffer.length(), Tagbuffer.toString() + "\n");
            stack.pop();
            errorIndex.add(xmlcount);
        }
    }
            //adding xml comments for the detected errors to show the place of the error detected
            String errormsg = "<!-- Missing closing tag here -->\n";
            StringBuffer xmlerrorbuffer = new StringBuffer(xml);
            Collections.sort(errorIndex);
            Collections.reverse(errorIndex);

            for (int index : errorIndex) {
                if (index >= xmlcount) {
                    xmlerrorbuffer.insert(xmlerrorbuffer.length() - 1, "\n" + errormsg);
                } else {
                    xmlerrorbuffer.insert(index, errormsg);
                }
            }
            //creating a list of Strings to return the xml containing errors with the detectet error msga and the corrected xml
            result.add(xmlerrorbuffer.toString());
            result.add(xmlBuffer.toString());
        } catch (Exception e) {
            //for any corner cases that couldn't be handled for bad xml format this msg is shown
            result.add ("Errors cannot be detected , try formatting your xml first");
            result.add( "Errors cannot be corrected ,try formatting your xml first ");
            return result;
        }
        return result;
}

//finction checks if this closing tag matches the current openning tag
public static boolean isMatchingTag(String openTag, String closingTag) {
    StringBuffer opentagbuffer = new StringBuffer(openTag);
    opentagbuffer = opentagbuffer.insert(1, '/');
    return opentagbuffer.toString().equals(closingTag);
}


}
