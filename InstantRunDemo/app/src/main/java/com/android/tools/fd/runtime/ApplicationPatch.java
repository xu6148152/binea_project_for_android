package com.android.tools.fd.runtime;

import com.android.tools.fd.common.Log;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ApplicationPatch
{
  public final byte[] data;
  public final String path;
  
  public ApplicationPatch(String paramString, byte[] paramArrayOfByte)
  {
    this.path = paramString;
    this.data = paramArrayOfByte;
  }
  
  public static List<ApplicationPatch> read(DataInputStream paramDataInputStream)
          throws IOException
  {
    int j = paramDataInputStream.readInt();
    if ((Log.logging != null) && (Log.logging.isLoggable(Level.FINE))) {
      Log.logging.log(Level.FINE, "Receiving " + j + " changes");
    }
    ArrayList localArrayList = new ArrayList(j);
    int i = 0;
    while (i < j)
    {
      String str = paramDataInputStream.readUTF();
      byte[] arrayOfByte = new byte[paramDataInputStream.readInt()];
      paramDataInputStream.readFully(arrayOfByte);
      localArrayList.add(new ApplicationPatch(str, arrayOfByte));
      i += 1;
    }
    return localArrayList;
  }
  
  public byte[] getBytes()
  {
    return this.data;
  }
  
  public String getPath()
  {
    return this.path;
  }
  
  public String toString()
  {
    return "ApplicationPatch{path='" + this.path + '\'' + ", data.length='" + this.data.length + '\'' + '}';
  }
}
