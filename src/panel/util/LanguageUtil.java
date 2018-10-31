package panel.util;

public class LanguageUtil {
	private char[] cho = {0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141, 
	        0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148, 
	        0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};
	
	private char[] jung = {0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 
	        0x3156, 0x3157, 0x3158, 0x3159, 0x315a, 0x315b, 
	        0x315c, 0x315d, 0x315e, 0x315f, 0x3160,    0x3161,    
	        0x3162, 0x3163};
	
	private char[] jong = {0x0000, 0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136, 
	        0x3137, 0x3139, 0x313a, 0x313b, 0x313c, 0x313d, 
	        0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 
	        0x3145, 0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 
	        0x314c, 0x314d, 0x314e};
	
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
	
	public char onlyCho(int cho_index) {
		return cho[cho_index];
	}
	
	public int isBadchim(int jong_index, char cho) {
		
		int cho_index = findChosung(cho);
		
		switch(jong_index) {
		case 1:	//ぁ
			if(cho_index == 9) {
				return 3;	//ぃ
			}
			break;
		case 4:	//い
			if(cho_index == 12) {
				return 5;	//ぅ
			}
			else if(cho_index == 18) {
				return 6;	//う
			}
			break;
		case 8:	//ぉ
			if(cho_index == 0) {
				return 9;	//お
			}
			else if(cho_index == 6) {
				return 10;	//か
			}
			else if(cho_index == 7) {
				return 11;	//が
			}
			else if(cho_index == 9) {
				return 12;	//き
			}
			else if(cho_index == 16) {
				return 13;	//ぎ
			}
			else if(cho_index == 17) {
				return 14;	//く
			}
			else if(cho_index == 18) {
				return 15;	//ぐ
			}
			break;
		case 17:
			if(cho_index == 9) {
				return 18;	//ご
			}
			break;
		}
		return -1;
	}
	
	public int deleteBadchim(int jong_index) {
		switch(jong_index){
		case 3:	//ぃ
			return 1;
		case 5:	//ぅ
			return 4;
		case 6:	//う
			return 4;
		case 9:	//お
			return 8;
		case 10:	//か
			return 8;
		case 11:	//が
			return 8;
		case 12:	//き
			return 8;
		case 13:	//ぎ
			return 8;
		case 14:	//く
			return 8;
		case 15:	//ぐ
			return 8;
		case 18:	//ご
			return 17;
		}
		return -1;
	}
}