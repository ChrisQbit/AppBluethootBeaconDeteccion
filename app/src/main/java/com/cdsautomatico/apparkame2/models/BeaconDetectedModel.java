package com.cdsautomatico.apparkame2.models;

public class BeaconDetectedModel
{
       private String UUid;
       private int readTimes;
       private int lostTimes;
       private double intencity;

       public BeaconDetectedModel (String UUid, double distance)
       {
              this.UUid = UUid.replace("-", "").toLowerCase();
              readTimes = 1;
              lostTimes = 0;
              this.intencity = distance;
       }

       public int getReadTimes ()
       {
              return readTimes;
       }

       public String getUUid ()
       {
              return UUid;
       }

       public double getIntencity ()
       {
              return intencity;
       }

       public void read (double distance)
       {
              this.intencity = distance;
              readTimes++;
              lostTimes = 0;
       }

       public void lost ()
       {
              lostTimes++;
       }

       public boolean isCompletlyLost ()
       {
              return lostTimes >= 3;
       }
}
