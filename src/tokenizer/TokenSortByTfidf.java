package tokenizer;

public class TokenSortByTfidf extends Token{
	public TokenSortByTfidf(String name, int count) {
		super(name, count);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(Object arg) {
		Token other = (Token) arg;
		return this.tfidf==other.tfidf?
				this.name.compareTo(other.name):
					this.tfidf-other.tfidf;
	}
}
