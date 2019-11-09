//Page definition for simulation
public class page {
	String pageNum;
    int frame;
    int age;
    boolean dirty;

    public page(){
        this.frame = 0;
        this.age = 0;
        this.dirty = false;
    }
    public page(String pageNum){
    	this.pageNum = pageNum;
        this.frame = 0;
        this.age = 0;
        this.dirty = false;
    }
    public page(page entry){
    	this.pageNum = entry.pageNum;
        this.frame = entry.frame;
        this.age = entry.age;
        this.dirty = entry.dirty;
    }
    public int addAge(int time)
    {
    	this.age = this.age + time;
    	return this.age;
    }
    public int resetAge() {
    	this.age = 0;
    	return this.age;
    }
}
