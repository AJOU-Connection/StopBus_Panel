package panel.util;

public class LanguageUtil {
	private char[] cho = {
			'��','��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��' 
	};
	
	private char[] jung = {
			'��', '��', '��', '��', '��', '��', '��', '��', '��', '��',
			'��', '��', '��', '��', '��', '��', '��', '��', '��', '��','��' 
	};
	
	private char[] jong = {
			' ', '��', '��', '��', '��', '��', '��', '��', '��', '��', 
			'��', '��', '��', '��', '��', '��', '��', '��', '��', '��',
			'��', '��', '��', '��', '��', '��', '��', '��'
	};
	
	public int findChosung (char c) {
		int index = 0;
		for(int i = 0; i < cho.length; i++) {
			if(cho[i] == c) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public int findJungsung (char c) {
		int index = 0;
		for(int i = 0; i < jung.length; i++) {
			if(jung[i] == c) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public int findJongsung (char c) {
		int index = 0;
		for(int i = 0; i < jong.length; i++) {
			if(jong[i] == c) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public int jong2cho(int jong_index) {
		int index = -1;
		for(int i = 0; i < cho.length; i++) {
			if(jong[jong_index] == cho[i]) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public int getChosung(char c) {
		return ((((int)c - 0xAC00) - ((int)c - 0xAC00) % 28) / 28) / 21;
	}
	
	public int getJungsung(char c) {
		return ((((int)c - 0xAC00) - ((int)c - 0xAC00) % 28) / 28) % 21;
	}
	
	public int getJongsung(char c) {
		return ((int)c - 0xAC00) % 28;
	}
	
	public boolean checkVowels(int jung_index) {
		if(jung_index == 8 || jung_index == 13 || jung_index == 18) {
			return true;
		}
		else
			return false;
	}
	
	public int mergeVowels(int jung_index, char input) {
		if(jung_index == 8) {
			switch(findJungsung(input)) {
			case 0: return 9;
			case 1: return 10;
			case 20: return 11;
			default: break;
			}
		}
		else if(jung_index == 13) {
			switch(findJungsung(input)) {
			case 4: return 14;
			case 5: return 15;
			case 20: return 16;
			default: break;
			}
		}
		else if(jung_index == 18) {
			if(findJungsung(input) == 20) {
				return 19;
			}
		}
		return 0;
	}
	
	public char findVowelsChar(int jung_index) {
		return jung[jung_index];
	}
	
	public char combineWord(int cho, int jung, int jong) {
		int sum = (cho * 21 * 28) + (jung * 28) + jong;
		return (char) (sum + 0xAC00);
	}
}
