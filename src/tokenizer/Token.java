package tokenizer;

public abstract class Token implements Comparable{
	protected String name;
	protected int count=0;
	protected int tfidf=0;
	
	public Token(String name,int count){
		this.name=name;
		this.count=count;
	}
	@Override
	public abstract int compareTo(Object arg);
	public String toString(){
		return name+":"+count+"\n";
		
	}
	public String getName() {
		return name;
	}
	public int getCount() {
		return count;
	}
	public int getTfidf() {
		return tfidf;
	}
	public String[] getStrings(){
		return new String[]{name,String.valueOf(count),String.valueOf(tfidf)};
	}
}
