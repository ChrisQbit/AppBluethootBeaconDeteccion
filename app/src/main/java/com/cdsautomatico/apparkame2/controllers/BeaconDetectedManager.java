package com.cdsautomatico.apparkame2.controllers;

import android.util.Log;

import com.cdsautomatico.apparkame2.models.BeaconDetectedModel;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BeaconDetectedManager
{
       private static final String TAG = "BeaconDetectedManager";

       private List<BeaconDetectedModel> beaconsDetectados;

       public BeaconDetectedManager ()
       {
              beaconsDetectados = new ArrayList<>();
       }

       public void registerReadBeacons (Collection<Beacon> beacons)
       {
              addNewBeacons(beacons);
              removeBeaconsNotRead(beacons);
       }

       private void addNewBeacons (Collection<Beacon> beacons)
       {
              for (Beacon beacon : beacons)
              {
                     BeaconDetectedModel beaconModel = new BeaconDetectedModel(beacon.getId1().toString(), beacon.getRssi());
                     Log.i(TAG, "addNewBeacons: Beacon a validar -> " + beaconModel.getUUid() + " Distancia -> " + beaconModel.getIntencity());

                     if (beaconsDetectados.size() == 0)
                     {
                            if (beaconModel.getIntencity() > 2.5)
                            {
                                   continue;
                            }
                            beaconsDetectados.add(beaconModel);
                     }
                     else
                     {
                            for (int i = 0; i < beaconsDetectados.size(); i++)
                            {
                                   if (beaconsDetectados.get(i).getUUid().equals(beaconModel.getUUid()))
                                   {
                                          Log.d(TAG, "Read " + beaconsDetectados.get(i).getUUid());
                                          if (beaconModel.getIntencity() > 2.5)
                                          {
                                                 beaconsDetectados.get(i).lost();
                                                 if (beaconsDetectados.get(i).isCompletlyLost())
                                                 {
                                                        beaconsDetectados.remove(i);
                                                 }
                                                 break;
                                          }
                                          beaconsDetectados.get(i).read(beaconModel.getIntencity());
                                          break;
                                   }
                                   if (i == beaconsDetectados.size() - 1 && beaconModel.getIntencity() <= 2.5)
                                   {
                                          beaconsDetectados.add(beaconModel);
                                   }
                            }
                     }
              }
       }

       private void removeBeaconsNotRead (Collection<Beacon> beacons)
       {

              for (int i = 0; i < beaconsDetectados.size(); i++)
              {
                     BeaconDetectedModel beaconModel = beaconsDetectados.get(i);
                     boolean found = false;
                     for (Beacon beacon : beacons)
                     {
                            String uuid = beacon.getId1().toString().replace("-", "").toLowerCase();
                            if (beaconModel.getUUid().equals(uuid))
                            {
                                   found = true;
                                   break;
                            }
                     }
                     if (!found)
                     {
                            if (beaconModel.getReadTimes() >= 3)
                            {
                                   Log.d(TAG, "Lost " + beaconsDetectados.get(i).getUUid());
                                   beaconsDetectados.get(i).lost();
                                   if (!beaconsDetectados.get(i).isCompletlyLost())
                                          return;
                            }
                            beaconsDetectados.remove(i);
                            i--;

                     }
              }
       }

       public List<BeaconDetectedModel> getBeaconsAvailable ()
       {
              List<BeaconDetectedModel> beaconsAvailable = new LinkedList<>();
              for (int i = 0; i < beaconsDetectados.size(); i++)
              {
                     if (beaconsDetectados.get(i).getReadTimes() > 0)
                     {
                            beaconsAvailable.add(beaconsDetectados.get(i));
                     }
              }
              return beaconsAvailable;
       }

}
