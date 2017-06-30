/**
 * 
 */
package executeImpl;

/**
 * @author zhenxi
 *
 */
public class Ook2BF {
	public String CodeInBF(String CodeInOOK){
		StringBuilder sb=new StringBuilder();
		while(CodeInOOK.length()>=10){
			switch(CodeInOOK.substring(0, 10)){
			case "Ook. Ook? ":
				sb.append('>');
				break;
			case "Ook? Ook. ":
				sb.append('<');
				break;
			case "Ook. Ook. ":
				sb.append('+');
				break;
			case "Ook! Ook! ":
				sb.append('-');
				break;
			case "Ook! Ook. ":
				sb.append('.');
				break;
			case "Ook. Ook! ":
				sb.append(',');
				break;
			case "Ook! Ook? ":
				sb.append('[');
				break;
			case "Ook? Ook! ":
				sb.append(']');
				break;
			}
			CodeInOOK=CodeInOOK.substring(10);
		}
		return sb.toString();
	}
}
