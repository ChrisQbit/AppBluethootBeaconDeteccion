package com.cdsautomatico.apparkame2.utils;

public class MathUtils
{
	  public static int upperRound(double raw)
	  {
		    return raw > 1 ? (((int)(raw) % raw) > 0 ?
				(int)(raw) + 1 : (int)(raw)) : 1;
	  }
}
