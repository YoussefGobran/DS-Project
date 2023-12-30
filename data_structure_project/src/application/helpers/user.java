package application.helpers;

import java.util.ArrayList;
import java.util.List;

class User{
	int id;
	String name;
	List<Post> posts;
	List<Integer> followers;
	public User() {
		// TODO Auto-generated constructor stub
		posts=new ArrayList<Post>();
		followers=new ArrayList<Integer>();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String postRes="";
		String followerRes="";
		for(Post post:posts) {
			postRes+=post.toString();
		}
		for(int follower:followers) {
			followerRes+=follower;
		}
//		System.out.println(name);
		return id+name+"\n"+postRes+followerRes;
	}
}
class Post{
	String body;
	List<String> topics;
	public Post() {
		// TODO Auto-generated constructor stub
		topics=new ArrayList<String>();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String topicRes="";
		for(String topic:topics) {
			topicRes+=topic+" ,";
		}
		return "Body: "+body+"\n"+"Topics: "+topicRes+"\n";
	}
}