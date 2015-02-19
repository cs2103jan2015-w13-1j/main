package basicElements;


/**
 * Used to store the id's of tasks with a particular tag.
 * Mainly for instant searching purpose
 * The name of the tag can be changed
 * @author Yichen
 */

public class TaskByTag extends TaskByProperty{
	private String tag;
	
	/**
	 * Construct by specifying a tag string.
	 * @param tag
	 */
	public TaskByTag(String tag){
		this.tag = tag;
	}
	
	public String getTag(){
		return this.tag;
	}
		
	public void changeTagName(String newName){
		this.tag = newName;
	}


}
