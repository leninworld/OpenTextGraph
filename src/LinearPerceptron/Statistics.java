package LinearPerceptron;
public class Statistics {
	double precision;
	double recall;
	double f1;
	double accuracy;
	
	public Statistics(double precision, double recall, double f1, double accuracy) {
		super();
		this.precision = precision;
		this.recall = recall;
		this.f1 = f1;
		this.accuracy=accuracy;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getRecall() {
		return recall;
	}

	
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public double getAccuracy() {
		return accuracy;
	}
	
	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getF1() {
		return f1;
	}

	public void setF1(double f1) {
		this.f1 = f1;
	}
	
	
}