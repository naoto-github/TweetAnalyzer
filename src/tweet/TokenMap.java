package tweet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import com.opencsv.CSVWriter;

public class TokenMap extends HashMap<String, Integer>{

	public void putToken(String word, int count){
		if(containsKey(word)){
			int value = get(word) + count;
			put(word, value);
		}else{
			put(word, count);
		}
	}

	public ArrayList<TokenInfo> getTokenList(){
		ArrayList<TokenInfo> list = new ArrayList<TokenInfo>();

		Set<String> words = this.keySet();
		for(String word: words){
			int count = get(word);
			TokenInfo ti = new TokenInfo(word, count);
			list.add(ti);
		}

		Collections.sort(list);

		return list;
	}

	public void writeTokens(File file){

		ArrayList<TokenInfo> list = getTokenList();

		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "SJIS");

			CSVWriter cw = new CSVWriter(osw);
			for(TokenInfo ti: list){
				cw.writeNext(ti.toCSV());
			}
			cw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	public static void writeTokens(File file, ArrayList<TokenInfo> list){

		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "SJIS");

			CSVWriter cw = new CSVWriter(osw);
			for(TokenInfo ti: list){
				cw.writeNext(ti.toCSV());
			}
			cw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	public String toString(){
		StringBuffer sb = new StringBuffer();

		ArrayList<TokenInfo> list = getTokenList();
		for(TokenInfo ti: list){
			sb.append(ti.word + ",");
			sb.append(ti.tf + "\n");
		}

		return sb.toString();
	}

}
