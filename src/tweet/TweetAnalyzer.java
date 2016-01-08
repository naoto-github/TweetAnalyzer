package tweet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

import com.opencsv.CSVReader;

import mytwitter.TweetInfo;

/**
 * ツイートを構文解析する
 * @author Naoto
 */
public class TweetAnalyzer {

	public static void main(String[] args) {

		System.out.println("-----構文解析-----");

		File dir = new File("Tweets/");
		File[] files = dir.listFiles();
		TokenMap tm[] = new TokenMap[files.length];

		for(int i=0; i<files.length; i++){
			File file = files[i];

			//ツイート情報の処理
			ArrayList<TweetInfo> tweets = new ArrayList<TweetInfo>();
			try {

				//CSVファイルの読込
				CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file), "SJIS"));
				List<String[]> list = reader.readAll();
				reader.close();

				//ツイート情報の取得
				for(String[] line: list){
					long id = Long.parseLong(line[0]);
					String name = line[1];
					long time = Long.parseLong(line[2]);
					String text = line[3];
					int page = Integer.parseInt(line[4]);
					TweetInfo ti = new TweetInfo(id, name, time, text ,page);
					tweets.add(ti);
					//System.out.println(ti);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			//形態素解析の処理
			Tokenizer tokenizer = Tokenizer.builder().build();
			tm[i] = new TokenMap();
			for(TweetInfo ti: tweets){
				List<Token> tokens = tokenizer.tokenize(ti.text);
				for(Token token: tokens){
					String features[] = token.getAllFeaturesArray();
					if(token.isKnown()){
						if((features[0].equals("名詞") && features[1].equals("一般")) ||
						   (features[0].equals("名詞") && features[1].equals("固有名詞")) ||
						   (features[0].equals("名詞") && features[1].equals("サ変接続"))){
							String word = features[6];
							tm[i].putToken(word, 1);
						}
					}
				}
			}

			//形態素解析結果の保存
			System.out.println(file.getName());
			File file_tokens = new File("Tokens/" + file.getName());
			tm[i].writeTokens(file_tokens);

		}

		//TF-IDFの算出
		System.out.println("-----TF-IDFの算出-----");
		TokenMap tm_all = new TokenMap();
		for(int i=0; i<tm.length; i++){
			ArrayList<TokenInfo> list = tm[i].getTokenList();
			for(TokenInfo ti: list){
				tm_all.putToken(ti.word, ti.tf);
			}
		}

		ArrayList<TokenInfo> list = tm_all.getTokenList();
		for(TokenInfo ti: list){
			for(int i=0; i<tm.length; i++){
				if(tm[i].containsKey(ti.word)){
					ti.df += 1;
				}
			}
		}

		for(TokenInfo ti: list){
			ti.idf = Math.log(tm.length / ti.df) + 1;
			ti.tf_idf = ti.tf * ti.idf;
			System.out.println(ti);
		}

		//ファイル出力
		File file_tokens = new File("Tokens/All.csv");
		TokenMap.writeTokens(file_tokens, list);

	}

}
