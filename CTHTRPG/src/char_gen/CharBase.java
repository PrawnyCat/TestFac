package char_gen;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//SQLite를 이용하여 oracle을 사용하지 않고 database를 내장하여 이용한다.
//DB를 만들 때 sample을 만들어둘 것. sample은 Arcane series의 세 주인공.

public class CharBase {
	
	public final String[] CHAR_BASIC = {"STR","CON","SIZ","INT","POW","DEX","APP","EDU"};
	public final String[] CHAR_STAT = 
		{"IDEA","LUCK","KNOW","DB","HP","MP","SAN","SKP","INP"};
	public final int MAX_VAL = 99;
	
	
	public int dice(int diceNum, int diceFig) {
		
		return diceNum*((int)(Math.random()*diceFig)+1);
		
	}
	
	public int charRoll(String cha) {
		switch(cha) {
			case "STR": return dice(3,6);
			case "CON": return dice(3,6);
			case "SIZ": return dice(2,6)+6;
			case "INT": return dice(2,6)+6;
			case "POW": return dice(3,6);
			case "DEX": return dice(3,6);
			case "APP": return dice(3,6);
			default: return dice(3,6)+6;
		}
	}
	
	public int status(String cha,Map<String, Integer> baseMap) {
		switch(cha) {
			case "IDEA": return baseMap.get("INT")*5;
			case "LUCK": return baseMap.get("POW")*5;
			case "KNOW": return baseMap.get("EDU")*5>MAX_VAL?MAX_VAL:baseMap.get("EDU")*5;
			case "DB": return baseMap.get("SIZ")+baseMap.get("STR");
			case "HP": return (baseMap.get("CON")+baseMap.get("SIZ"))/2;
			case "MP": return baseMap.get("POW");
			case "SAN": return baseMap.get("POW")*5;
			case "SKP": return baseMap.get("EDU")*20;
			default: return baseMap.get("INT")*10;
		}
	}
	
	public Map<String,Integer> charGen(){
		Map<String, Integer> baseMap = new HashMap<String, Integer>();
		
		for(String cha:CHAR_BASIC) {
			baseMap.put(cha, charRoll(cha));
		}
		return baseMap;
	}
	
	public Map<String,Integer> statGen(Map<String, Integer> baseMap){
		Map<String, Integer> statMap = new HashMap<String, Integer>();
		
		for(String cha:CHAR_STAT) {
			statMap.put(cha, status(cha,baseMap));
		}
		return statMap;
	}
	
	public static void main(String[] args) {
		Map<String, Integer> baseMap = new CharBase().charGen();
		Map<String, Integer> statMap = new CharBase().statGen(baseMap);
		Set<String> bKeys = baseMap.keySet();
		Set<String> sKeys = statMap.keySet();
		System.out.println("------------");
		for(String key:bKeys)
			System.out.println(String.format("%s : %s",key, baseMap.get(key)));
		System.out.println("------------");
		for(String key:sKeys)
			System.out.println(String.format("%s : %s",key, statMap.get(key)));
		System.out.println("------------");
	}
	
}

