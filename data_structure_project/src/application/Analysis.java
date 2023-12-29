package application;
import java.util.ArrayList;
import java.util.List;

import application.helpers.XMLNode;

public class Analysis {
	private XMLNode rootNode;
	
	List<XMLNode> users = rootNode.getChildrenNodes();
	int numUsers = users.size(); 
	
	@SuppressWarnings("unchecked")
	ArrayList<String>[] followersList = new ArrayList[numUsers];
	
	public Analysis(XMLNode rootNode) {
        this.rootNode = rootNode;
        doAnalysis();
    }
	
	private void doAnalysis() {
		for(int i = 0;i< numUsers;i++) {
			followersList[i]= new ArrayList<String>();
			XMLNode user = users.get(i);
			List<XMLNode> followers = user.getChildrenNodes().get(3).getChildrenNodes();
			
			for (XMLNode follower : followers) {
				followersList[i].add(follower.getChildrenNodes().get(0).getInnerText());
			}
			
		}
	}
	public int getMaxUserFollowered() {
		int maxFollowers = 0;
		int maxUserFollowers = -1;
		for(int i = 0;i<numUsers;i++) {
			if(followersList[i].size() > maxFollowers){
				maxFollowers = followersList[i].size();
				maxUserFollowers = i+1;
			}
		}
		return maxUserFollowers;
	}
	
	
}
