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
	public List<User>users;
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
		parseXMLFileHelper(rootNode);

	}

	private void parseXMLFileHelper(XMLNode root) {
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

			parseXMLFileHelper(res);
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
		try {

			// Create a FileOutputStream to write to the file
			OutputStream outputStream = new FileOutputStream(filePath);

			// Create an OutputStreamWriter with US-ASCII encoding
			Writer writer = new OutputStreamWriter(outputStream);
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
	public List<String> xmlCorrection(String xml) {
		//index 0 is detected and index 1 is corrected
		//next line to remove any spaces at the beginning of the line in the xml
		xml=xml.replaceAll("\r\n|\r", "\n");
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
	public boolean isMatchingTag(String openTag, String closingTag) {
		StringBuffer opentagbuffer = new StringBuffer(openTag);
		opentagbuffer = opentagbuffer.insert(1, '/');
		return opentagbuffer.toString().equals(closingTag);
	}

	public String createJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		createJsonHelper(sb,rootNode,true,true);
		sb.append("}");
		return sb.toString();

	}

	private boolean createJsonHelper(StringBuilder sb,XMLNode node,boolean writewriteTagNameFlag,boolean writeComma){
		boolean obJSON_diff_flag = false;
		// start JSON BY ITS FIRST CURLY BRACKET
		if(writewriteTagNameFlag) {
			sb.append('"'+node.tagName+'"'+':');			
		}

		if(node.childrenNodes.size()>1) {
			String firstTag=node.childrenNodes.get(0).tagName;

			// check if the children if they are array of objects or keys
			for (int i = 1; i < node.childrenNodes.size(); i++) {
				if (node.childrenNodes.get(i).tagName.compareTo(firstTag)!=0) {
					obJSON_diff_flag = true;
					break;
				}
			}	
		}else {
			obJSON_diff_flag = true;
		}

		if(node.childrenNodes==null||node.childrenNodes.size()<1) {
			sb.append('"'+node.innerText+'"');
			if(writeComma) {
				sb.append(',');
			}
			return false;
		}
		sb.append("{");
		// if the children are different
		if (obJSON_diff_flag) {

			for (int i = 0; i < node.childrenNodes.size(); i++) {
				// Check if still have children
				//				if (node.childrenNodes.get(i).childrenNodes != null)
				createJsonHelper(sb,node.childrenNodes.get(i),true,i!=node.childrenNodes.size()-1);
				// if not >>> print inner text
				//				else {
				//					sb.append('"'+node.childrenNodes.get(i).innerText+'"'+'\n');
				//				}
			}

		} else // else children are array of the object
		{
			sb.append('"'+node.childrenNodes.get(0).tagName+'"'+':');
			sb.append("[");
			for (int i = 0; i < node.childrenNodes.size()-1; i++) {
				// Check if still have children
				//				if (node.childrenNodes.get(i).childrenNodes != null)
				if(createJsonHelper(sb,node.childrenNodes.get(i),false,true)){
					sb.append(',');
				}
				// if not >>> print inner text
				//				else {
				//					sb.append('"'+node.childrenNodes.get(i).innerText+'"'+'\n');
				//				}
			}
			createJsonHelper(sb,node.childrenNodes.get(node.childrenNodes.size()-1),false,false);
			sb.append("]");
			//			
			//			sb.append("\"");
			//			sb.append(node.tagName);
			//			sb.append("\" :[ \n");
			//			for (int i = 1; i < node.childrenNodes.size(); i++) {
			//				sb.append(createJsonHelper(sb,node.childrenNodes.get(i)));
			//			}
			//			sb.append(" ]\n");
		}
		sb.append("}");
		// CONVERT
		return true;
	}
	public String formatJSON(String json_str,int indent_width) {
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

	public String formatXml() {
		StringBuilder formattedXml = new StringBuilder();
		formatXmlHelper(rootNode, formattedXml, 0);
		return formattedXml.toString();
	}
	public void formatXmlHelper(XMLNode root, StringBuilder formattedXml, int indentationLevel) {
		formattedXml.append("    ".repeat(indentationLevel)+'<'+root.getTagName()+'>'+'\n');

		if (root.getChildrenNodes() != null && !root.getChildrenNodes().isEmpty()) {
			for (XMLNode child : root.getChildrenNodes()) {
				formatXmlHelper(child, formattedXml, indentationLevel + 1);
				formattedXml.append("\n");
			}
		} else if (root.getInnerText() != null && !root.getInnerText().trim().isEmpty()) {
			String trimmedInnerText = root.getInnerText().trim();
			formattedXml.append("    ".repeat(indentationLevel+1)+trimmedInnerText+'\n');

		}
		formattedXml.append("    ".repeat(indentationLevel)+"</"+root.getTagName()+">");
	}
	public static boolean isNumeric(String str) { 
		try {  
			Integer.parseInt(str);  
			return true;
		} catch(NumberFormatException e){  
			return false;  
		}  
	}
	public void parseXMLToUsers() {
		this.users=new ArrayList<User>();
		String idTagName="";
		boolean areFollowersFound=false;
		String followersTagName="";
		for(XMLNode currentUser:rootNode.getChildrenNodes()) {
			User user=new User();
			for(XMLNode currentUserChild:currentUser.getChildrenNodes()) {
				//responsible for setting of name and id
				if(!currentUserChild.haveChildren()) {
					if(isNumeric(currentUserChild.innerText)) {
						idTagName=currentUserChild.tagName;
						user.id=Integer.parseInt(currentUserChild.innerText); 
					}else {
						//						System.out.println(currentUserChild.innerText);
						user.name=currentUserChild.innerText;
					}
				}
			}
			//responsible for creating the followers 
			for(XMLNode currentUserChild:currentUser.getChildrenNodes()) {
				if(currentUserChild.haveChildren()) {
					if(currentUserChild.childrenNodes.get(0).haveChildren()&&currentUserChild.childrenNodes.get(0).getChildrenNodes().get(0).tagName.compareTo(idTagName)==0) {
						areFollowersFound=true;
						followersTagName=currentUserChild.tagName;
						for(XMLNode followerId:currentUserChild.childrenNodes) {
							user.followers.add(Integer.parseInt(followerId.getChildrenNodes().get(0).innerText));							
						}
					}
				}
			}
			//responsible for creating the post 
			for(XMLNode currentUserChild:currentUser.getChildrenNodes()) {
				if(currentUserChild.haveChildren()&&currentUserChild.tagName.compareTo(followersTagName)!=0) {
					for(XMLNode post:currentUserChild.childrenNodes) {
						Post postGenerated=new Post();
						for(XMLNode postChild:post.childrenNodes) {
							if(!postChild.haveChildren()) {
								postGenerated.body=postChild.innerText;
							}else {
								for(XMLNode topic:postChild.childrenNodes) {
									postGenerated.topics.add(topic.innerText);
								}
							}
						}

						user.posts.add(postGenerated);							
					}
				}



			}
			//			System.out.println(user);
			this.users.add(user);
		}
	}
	public List<String> postSearch(String searchKeyword) {
		List<String> res=new ArrayList<>();
		for(User user:users) {
			for(Post post:user.posts) {
				if(post.body.contains(searchKeyword)) {
					res.add(user.id+"\n"+user.name+'\n'+post.body+'\n'+post.topics+'\n');
				}else {
					for(String topic:post.topics) {
						if(topic.contains(searchKeyword)) {
							res.add(user.id+'\n'+user.name+'\n'+post.body+'\n'+post.topics+'\n');
						}
					}
				}
			}
		}
		return res;
	}
	List<List<Integer>> adjanceyListUsers;
	public void buildAdjanceyListUsers() {
		adjanceyListUsers = new ArrayList<List<Integer>>(1000);
		for(int i=0;i<1000;i++) {
			adjanceyListUsers.add(new ArrayList<Integer>());
		}
		for(User user :users) {
			if(user.id>1000) {
				System.out.println("User id cant pass 1000");
				return;
			}
			for(int followerId:user.followers) {
				adjanceyListUsers.get(user.id).add(followerId);
			}
		}
	}
	public int userWithMostConnectedTo() {
		int id=-1;
		int maxLen=0;
		for(int i=0;i<adjanceyListUsers.size();i++){
			if(adjanceyListUsers.get(i).size()>maxLen) {
				maxLen=adjanceyListUsers.get(i).size();
				id=i;
			}
		}
		return id;
	}

	public int userWithMostFollowers() {
		int id=-1;
		int maxCount=0;
		int[] usersCounter=new int[adjanceyListUsers.size()];
		for(int i=0;i<adjanceyListUsers.size();i++){
			for(int j=0;j<adjanceyListUsers.get(i).size();j++) {
				int currentId=adjanceyListUsers.get(i).get(j);
				usersCounter[currentId]++;
				if(usersCounter[currentId]>maxCount) {
					maxCount=usersCounter[currentId];
					id=currentId;
				}
			}
		}
		return id;
	}
	public List<Integer> mutualFollowers(int user1Id,int user2Id){
		List<Integer> users1Followers=adjanceyListUsers.get(user1Id);
		List<Integer> users2Followers=adjanceyListUsers.get(user2Id);
		List<Integer> res=new ArrayList<Integer>(users1Followers.size()+users2Followers.size());
		for(int i = 0; i<users1Followers.size(); i++ ) {
	         for(int j = 0; j<users2Followers.size(); j++) {
	            if(users1Followers.get(i)==users2Followers.get(j)) {
	            	res.add(users1Followers.get(i));
	            }
	         }
	      }
		return res;
	}
	public List<Integer> suggestUser(int user1Id){
		List<Integer> res=new ArrayList<Integer>();
		List<Integer>followers=adjanceyListUsers.get(user1Id);
		for(int i=0;i<followers.size();i++) {
			List<Integer>followersOfFollowers=adjanceyListUsers.get(followers.get(i));
			res.addAll(followersOfFollowers);
		}
		res.removeAll(followers);
		res.remove((Object)user1Id);
		return res;
	}

}
