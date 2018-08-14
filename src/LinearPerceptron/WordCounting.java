package LinearPerceptron;

/* This class contains the statistical informations (tf, idf, tfidf) of a word in a document */
public class WordCounting {
	
	protected double tf;
	protected double idf;
	protected double tfidf;
	
	public WordCounting(double tf) {
		this.tf = tf;
		this.idf = 0;
		this.tfidf = 0;
	}

	public double getTf() {
		return tf;
	}
	
	public void setTf(double tf) {
		this.tf = tf;
	}
	
	public double getIdf() {
		return idf;
	}
	
	public void setIdf(double idf) {
		this.idf = idf;
	}
	
	public double getTfidf() {
		return tfidf;
	}
	
	public void setTfidf(double tfidf) {
		this.tfidf = tfidf;
	}
	
}
