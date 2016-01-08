package tweet;

public class TokenInfo implements Comparable<TokenInfo>{

	public String word;
	public int tf;
	public int df;
	public double idf;
	public double tf_idf;

	TokenInfo(String word, int tf){
		this.word = word;
		this.tf = tf;
		this.df = 0;
		this.idf = 0;
		this.tf_idf = 0;
	}

	public int compareTo(TokenInfo token){
		return (token.tf - tf);
	}

	public String[] toCSV(){
		String csv[] = new String[5];

		csv[0] = word;
		csv[1] = Integer.toString(tf);
		csv[2] = Integer.toString(df);
		csv[3] = Double.toString(idf);
		csv[4] = Double.toString(tf_idf);

		return csv;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();

		sb.append(word + ",");
		sb.append(tf + ",");
		sb.append(df + ",");
		sb.append(idf + ",");
		sb.append(tf_idf);

		return sb.toString();
	}
}
