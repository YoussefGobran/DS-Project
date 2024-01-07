package application.helpers;

import java.util.ArrayList;
import java.util.List;

public class User{
	public int id;
	public String name;
	public List<Post> posts;
	public List<Integer> followers;
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
		return id+name+"\n"+postRes+followerRes;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the posts
	 */
	public List<Post> getPosts() {
		return posts;
	}

	/**
	 * @param posts the posts to set
	 */
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	/**
	 * @return the followers
	 */
	public List<Integer> getFollowers() {
		return followers;
	}

	/**
	 * @param followers the followers to set
	 */
	public void setFollowers(List<Integer> followers) {
		this.followers = followers;
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