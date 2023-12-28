package application;
import application.helpers.XMLNode;
public class JSON {
	StringBuilder sb = new StringBuilder();
	
	String createJson (XMLNode node)
	{
		boolean obJSON_diff_flag = false;
		//start JSON BY ITS FIRST CURLY BRACKET
		sb.append("{ \n");
		//check if the children if they are array of objects or keys
		for(int i = 1; i < node.getChildrenNodes().size() ; i++)
		{
			if(node.getChildrenNodes().get(i).getTagName() != node.getChildrenNodes().get(i - 1).getTagName())
			{
				obJSON_diff_flag=true;
			}
		}

		//if the children are different
		if(obJSON_diff_flag==true)
		{
			for(int i = 1; i < node.getChildrenNodes().size() ; i++)
			{
				sb.append("\"");
				sb.append(node.getChildrenNodes().get(i).getTagName());
				sb.append("\" :");
				//Check if still have children
				if(node.getChildrenNodes().get(i).getChildrenNodes() != null)
				sb.append(createJson(node.getChildrenNodes().get(i)));
				//if not >>> print inner text
				else
				{
					sb.append("\"");
					sb.append(node.getChildrenNodes().get(i).getInnerText());
					sb.append("\" \n");	
				}
			}
		}
		else // else children are array of the object
		{
			sb.append("\"");
			sb.append(node.getTagName());
			sb.append("\" :[ \n");
			for(int i = 1; i < node.getChildrenNodes().size() ; i++)
			{
				sb.append(createJson(node.getChildrenNodes().get(i)));
			}
			sb.append(" ]\n");
		}
		//JSON END CURLY BRAKET 
		sb.append(" }\n");
		//CONVERT 
		return sb.toString();
	}

}
