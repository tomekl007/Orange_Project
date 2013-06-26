package openstreetmaps.Data;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileHelper 
{
	public static void saveToFile(InputStream is, File file) throws IOException
	{
//		File file = new File(MyApplication.getAppContext().getFilesDir(), filename);
		
		int count = 0;
		
		try
		{
			if(!file.exists())
				file.createNewFile();
			
			FileOutputStream fos = new FileOutputStream(file);
			
			int read = 0;
			byte[] bytes = new byte[1024];
	 			
			while ((read = is.read(bytes)) != -1) 
			{
				count += read;
				fos.write(bytes, 0, read);
			}		
			fos.close();
			
			Log.i("file", count + " bytes saved");
		}
		catch(Exception e)
		{
			Log.i("file", "exception: " + e.getMessage());
		}
	
		if(count == 0)
		{
			if(file.exists())
				file.delete();
			
			throw new IOException("no data downloaded");			
		}		
	}

}
