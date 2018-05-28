package panel.util;

public class LanguageUtil {
	private char[] cho = {
			'ぁ','あ', 'い', 'ぇ', 'え', 'ぉ', 'け', 'げ', 'こ', 'さ', 'ざ', 'し', 'じ', 'す', 'ず', 'せ', 'ぜ', 'そ', 'ぞ' 
	};
	
	private char[] jung = {
			'た', 'だ', 'ち', 'ぢ', 'っ', 'つ', 'づ', 'て', 'で', 'と',
			'ど', 'な', 'に', 'ぬ', 'ね', 'の', 'は', 'ば', 'ぱ', 'ひ','び' 
	};
	
	private char[] jong = {
			' ', 'ぁ', 'あ', 'ぃ', 'い', 'ぅ', 'う', 'ぇ', 'ぉ', 'お', 
			'か', 'が', 'き', 'ぎ', 'く', 'ぐ', 'け', 'げ', 'ご', 'さ',
			'ざ', 'し', 'じ', 'ず', 'せ', 'ぜ', 'そ', 'ぞ'
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
	
	public char combineWord(int cho, int jung, int jong) {
		int sum = (cho * 21 * 28) + (jung * 28) + jong;
		return (char) (sum + 0xAC00);
	}
}
