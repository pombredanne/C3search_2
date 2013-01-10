package tokenizer;

public class TokenSortByFrequency extends Token{

	public TokenSortByFrequency(String name, int count) {
		super(name, count);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(Object arg) {
		Token other = (Token) arg;
		return this.count==other.count?
				this.name.compareTo(other.name):
					this.count-other.count;
	}
	

}
