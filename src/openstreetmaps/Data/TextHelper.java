package openstreetmaps.Data;

public class TextHelper 
{
	public static String uppercase(String s)
	{
		String new_first_letter = null;
		String first_letter = s.substring(0, 1);
		
		if(first_letter.equals("�"))
			new_first_letter = new String("�");
		else if(first_letter.equals("�"))
			new_first_letter = new String("�");
		else if(first_letter.equals("�"))
			new_first_letter = new String("�");
		else if(first_letter.equals("�"))
			new_first_letter = new String("�");
		else
			new_first_letter = first_letter.toUpperCase();
	
		return new_first_letter + s.substring(1);
	}

	public static String parseString(String s)
	{
		return s.toLowerCase();	//work well with polish chars too
	}
}
