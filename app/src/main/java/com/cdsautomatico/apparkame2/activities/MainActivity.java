package com.cdsautomatico.apparkame2.activities;

import android.Manifest;
import androidx.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdsautomatico.apparkame2.BuildConfig;
import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.api.ApiHelper;
import com.cdsautomatico.apparkame2.api.ApiSession;
import com.cdsautomatico.apparkame2.controllers.BeaconDetectedManager;
import com.cdsautomatico.apparkame2.dataSource.SocketConsumer;
import com.cdsautomatico.apparkame2.models.Parking;
import com.cdsautomatico.apparkame2.models.Terminal;
import com.cdsautomatico.apparkame2.models.Usuario;
import com.cdsautomatico.apparkame2.utils.GenericDialogs;
import com.cdsautomatico.apparkame2.viewModel.ParkingViewModel;
import com.cdsautomatico.apparkame2.viewModel.TicketViewModel;
import com.squareup.picasso.Picasso;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, BeaconConsumer, RangeNotifier
{
	  private static final int REQUEST_EDIT_PROFILE = 377;
	  private static final int REQUEST_EDIT_PAYMENT_METHODS = 599;
	  private static final int REQUEST_ENABLE_BLUETOOTH = 573;
	  private static final int REQUEST_PERMISSION_LOCATION = 341;
	  private static final String TAG = "MainActivity";

	  private DrawerLayout drawerLayout;
	  private NavigationView navigationView;
	  private View dialogoMensaje;

	  private TicketFragment fgmtTicketFragment;
	  private BeaconManager beaconManager;
	  private BeaconDetectedManager beaconDetectedManager;

	  private ParkingViewModel parkingViewModel;
	  private TicketViewModel ticketViewModel;

	  private int timesLostTerminal = 0;

	  @Override
	  protected void onCreate (Bundle savedInstanceState)
	  {
		    super.onCreate(savedInstanceState);
		    ApiSession.setContext(getBaseContext());
		    ApiHelper.setAuthToken(ApiSession.getAuthToken());

		    if (ApiSession.getAuthToken() == null)
		    {
				 Intent intent = new Intent(this, LoginActivity.class);
				 startActivity(intent);
				 finish();
				 return;
		    }

		    startService(new Intent(this, SocketConsumer.class));

		    setContentView(R.layout.activity_main);

		    drawerLayout = findViewById(R.id.drawer_layout);
		    navigationView = findViewById(R.id.navView);
		    dialogoMensaje = findViewById(R.id.dialogoMensaje);

		    navigationView.setNavigationItemSelectedListener(this);

		    setupNavigatorInfo();

		    setUpBeaconScanning();
		    parkingViewModel = ViewModelProviders.of(this).get(ParkingViewModel.class);
		    ticketViewModel = ViewModelProviders.of(this).get(TicketViewModel.class);
	  }

	  private void setUpBeaconScanning ()
	  {
		    beaconManager = BeaconManager.getInstanceForApplication(this);
		    // Layout para detectar solo iBeacons
		    beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
		    beaconManager.setBackgroundScanPeriod(750);
		    beaconManager.bind(this);
	  }

	  private void verifyBluetooth ()
	  {
		    try
		    {
				 if (!beaconManager.checkAvailability())
				 {
					   final AlertDialog.Builder builder = new AlertDialog.Builder(this);
					   builder.setTitle(getString(R.string.title_bluetooth_not_enabled));
					   builder.setMessage(getString(R.string.enable_bluetooth));
					   builder.setPositiveButton(android.R.string.ok, (dialog, which) ->
					   {
							Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
							startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
					   });
					   builder.setCancelable(false);
					   builder.show();
				 }
				 else
				 {
					   setUpBeaconScanning();
				 }
		    }
		    catch (RuntimeException e)
		    {
				 final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				 builder.setTitle(getString(R.string.title_bluetooth_le_not_available));
				 builder.setMessage(getString(R.string.sorry_bluetooth_not_supported));
				 builder.setPositiveButton(android.R.string.ok, null);
				 builder.setOnDismissListener(null);
				 builder.show();
		    }
	  }

	  @Override
	  protected void onStart ()
	  {
		    super.onStart();
		    fgmtTicketFragment = ((TicketFragment) getSupportFragmentManager().findFragmentByTag("TicketFragment"));
		    if (fgmtTicketFragment == null)
		    {
				 Toast.makeText(this, "Fragmento no se encontró", Toast.LENGTH_SHORT).show();
				 finish();
				 return;
		    }
		    requestLocationPrivileges();
	  }

	  @Override
	  protected void onPause ()
	  {
		    super.onPause();
		    //if (beaconManager.isBound(this))
		    {
				 beaconManager.unbind(this);
				 beaconManager.removeAllRangeNotifiers();
		    }
	  }

	  private void setupNavigatorInfo ()
	  {
		    Usuario usuario = ApiSession.getUsuario();

		    View headerView = navigationView.getHeaderView(0);
		    TextView navHeaderNombre = headerView.findViewById(R.id.nombre);
		    TextView navHeaderEmail = headerView.findViewById(R.id.email);
		    TextView navHeaderUsuarioId = headerView.findViewById(R.id.usuarioId);
		    TextView navHeaderVersion = headerView.findViewById(R.id.lblVersion);
		    ImageView imgProfile = headerView.findViewById(R.id.avatar);

		    navHeaderUsuarioId.setText("Botón. ID " + String.format("%06d", usuario.getId()));
		    navHeaderNombre.setText(usuario.getNombre());
		    navHeaderVersion.setText(BuildConfig.VERSION_NAME);

		    if (usuario.getEmail() != null)
		    {
				 navHeaderEmail.setText(usuario.getEmail());
		    }

		    if (usuario.getImagen() != null)
		    {
				 Picasso.get().load(usuario.getImagen()).resize(100, 100)
					  .centerCrop().into(imgProfile);
		    }

		    imgProfile.setOnClickListener((View view) ->
		    {
				 Intent intent = new Intent(this, PerfilActivity.class);
				 startActivityForResult(intent, REQUEST_EDIT_PROFILE);
		    });
	  }

	  public void showDialogoMensaje (String titulo, String mensaje, int resIcon)
	  {
		    findViewById(R.id.dialogoMensajeCerrar).setOnClickListener((View view) ->
				dialogoMensaje.setVisibility(View.GONE));

		    findViewById(R.id.dialogoMensajeAceptar).setOnClickListener((View view) ->
				dialogoMensaje.setVisibility(View.GONE));

		    ((TextView) findViewById(R.id.dialogoMensajeTitulo)).setText(titulo);
		    ((TextView) findViewById(R.id.dialogoMensajeTexto)).setText(mensaje);

		    ((ImageView) findViewById(R.id.dialogoMensajeIcon)).setImageResource(resIcon);

		    dialogoMensaje.setVisibility(View.VISIBLE);

		    Animation animation = new AlphaAnimation(0f, 1f);
		    animation.setDuration(300);
		    animation.setFillAfter(false);
		    dialogoMensaje.startAnimation(animation);
	  }

	  private void cerrarSesion ()
	  {
		    GenericDialogs.confirm(this, "Cerrar Sesión", "¿Realmente deseas cerrar sesión?", () ->
		    {
				 ApiHelper.logout(null);
				 ApiSession.clear();
				 ApiHelper.setAuthToken(null);
				 Intent intent = new Intent(this, LoginActivity.class);
				 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				 stopService(new Intent(this, SocketConsumer.class));
				 startActivity(intent);
		    });
	  }

	  @Override
	  public void onBackPressed ()
	  {
		    if (drawerLayout.isDrawerOpen(GravityCompat.START))
		    {
				 drawerLayout.closeDrawer(GravityCompat.START);
		    }
		    else
		    {
				 finish();
		    }
	  }

	  @Override
	  public void onClick (View view)
	  {
		    if (view.getId() == R.id.btnMenu)
		    {
				 drawerLayout.openDrawer(GravityCompat.START);
		    }
		    else if (view.getId() == R.id.menuPensiones)
		    {
				 Intent intent = new Intent(this, PensionesActivity.class);
				 startActivity(intent);
		    }
	  }

	  @Override
	  public boolean onNavigationItemSelected (@NonNull MenuItem item)
	  {
		    Intent intent;
		    switch (item.getItemId())
		    {
				 case R.id.nav_profile:
					   intent = new Intent(this, PerfilActivity.class);
					   startActivityForResult(intent, REQUEST_EDIT_PROFILE);
					   break;
				 case R.id.nav_formas_pago:
					   intent = new Intent(this, FormasPagoActivity.class);
					   startActivityForResult(intent, REQUEST_EDIT_PAYMENT_METHODS);
					   break;
//            case R.id.nav_pensiones:
//                intent = new Intent(this, PensionesActivity.class);
//                startActivity(intent);
//                break;
				 case R.id.nav_logout:
					   cerrarSesion();
					   break;
				 case R.id.nav_history:
					   startActivity(new Intent(this, HistoryActivity.class));
					   break;
		    }

		    drawerLayout.closeDrawer(Gravity.START);

		    return false;
	  }

	  @Override
	  protected void onActivityResult (int requestCode, int resultCode, Intent data)
	  {
		    switch (requestCode)
		    {
				 case REQUEST_EDIT_PAYMENT_METHODS:
					   fgmtTicketFragment.loadTarjetas();
					   break;
				 case REQUEST_ENABLE_BLUETOOTH:
					   fgmtTicketFragment.verifyGPS();
					   break;
				 case REQUEST_EDIT_PROFILE:
				 {
					   setupNavigatorInfo();
				 }
				 break;
		    }
		    super.onActivityResult(requestCode, resultCode, data);
	  }

	  @Override
	  public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	  {
		    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		    if (requestCode == REQUEST_PERMISSION_LOCATION)
		    {
				 if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
				 {
					   final AlertDialog.Builder builder = new AlertDialog.Builder(this);
					   builder.setTitle("Funcionalidad limitada");
					   builder.setMessage("Es necesaria permisos de ubicación para acceder a un estacionamiento");
					   builder.setPositiveButton("Otorgar Permiso", (dialogInterface, i) ->
					   {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
							{
								  requestPermissions(new String[] {
									   Manifest.permission.ACCESS_FINE_LOCATION
								  }, REQUEST_PERMISSION_LOCATION);
							}
					   });
					   builder.setNegativeButton(android.R.string.no, null);
					   builder.setOnDismissListener(null);
					   builder.show();
				 }
				 else
				 {
					   verifyBluetooth();
				 }
		    }
	  }

	  private void requestLocationPrivileges ()
	  {
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		    {
				 if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
				 {
					   final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
					   alertBuilder.setTitle("Permiso de ubicación");
					   alertBuilder.setMessage("Es necesario permisos de ubicación para detectar entradas de estacionamiento");
					   alertBuilder.setPositiveButton(android.R.string.ok, null);
					   alertBuilder.setOnDismissListener((dialogInterface) ->
						    requestPermissions(new String[] {
								Manifest.permission.ACCESS_FINE_LOCATION
						    }, REQUEST_PERMISSION_LOCATION));
					   alertBuilder.create().show();
				 }
				 else
				 {
					   verifyBluetooth();
				 }
		    }
	  }

	  @Override
	  public void onBeaconServiceConnect ()
	  {
		    beaconManager.removeAllRangeNotifiers();
		    beaconManager.addRangeNotifier(this);
		    try
		    {
				 beaconManager.startRangingBeaconsInRegion(new Region("apparkameBeaconRanging", null, null, null));
		    }
		    catch (RemoteException e)
		    {
				 Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		    }
	  }

	  @Override
	  public void didRangeBeaconsInRegion (Collection<Beacon> beacons, Region region)
	  {
		    if (!beaconManager.isAnyConsumerBound())
		    {
				 beaconManager.removeAllRangeNotifiers();
				 return;
		    }
		    Parking estacionamiento = null;
		    //double distancia = 0;

		    if (fgmtTicketFragment == null || parkingViewModel.getParkingList() == null)
		    {
				 return;
		    }
		    fgmtTicketFragment.getBeaconsUUID().clear();
		    if (beacons.size() > 0)
		    {
				 processBeacon(beacons);
				 /*for (Beacon beacon : beacons)
				 {
					   String uid = beacon.getId1().toString().replace("-", "").toLowerCase();
					   //distancia = beacon.getRssi();
					   estacionamiento = fgmtTicketFragment.getEstacionamientoActivo();
					   if (estacionamiento != null)
					   {
							boolean found = false;
							for (int i = 0; i < fgmtTicketFragment.getEstacionamientoActivo().getTerminals().size(); i++)
							{
								  Terminal terminal = fgmtTicketFragment.getEstacionamientoActivo().getTerminals().get(i);
								  if (terminal.getBeaconUUID().equals(uid) && terminal.getDetectionIntensity() > beacon.getRssi())
								  {
									    //Log.d(TAG, "BEACON PERDIDO O LEJOS: " + uid + " : " + beacon.getRssi());
									    timesLostTerminal++;
								  }
								  else if (terminal.getBeaconUUID().equals(uid))
								  {
									    found = true;
									    timesLostTerminal = 0;
								  }
							}
							if (!found)
							{
								  timesLostTerminal++;
							}
							//if (timesLostTerminal <= 3)
							// continue;
					   }
					   //if (timesLostTerminal >= 3)
					   //estacionamiento = null;
					   //Log.d(TAG, "BEACON: " + uid + " : " + beacon.getRssi());
					   HashMap<String, Terminal> terminales;
					   if (fgmtTicketFragment.getBoletoActivo() == null)
					   {
							terminales = fgmtTicketFragment.getTerminalesEntrada();
					   }
					   else
					   {
							terminales = fgmtTicketFragment.getTerminalesSalida();
					   }

					   if (terminales.containsKey(uid) && terminales.get(uid).getDetectionIntensity() < beacon.getRssi())
					   {
							fgmtTicketFragment.getBeaconsUUID().add(uid);
							//nearestDistance = beacon.getRssi();
							estacionamiento = terminales.get(uid).getParking();
					   }
				 }
		    }*/
				 /*if (fgmtTicketFragment.getBoletoActivo() == null)
				 {
					   fgmtTicketFragment.setEstacionamientoActivo(estacionamiento);
				 }
					   runOnUiThread(() -> fgmtTicketFragment.updateHomeMessage());*/
				 /*if (estacionamiento != null)
				 {
					   //if (BuildConfig.DEBUG)
					   //fgmtTicketFragment.setEstacionamientoNombre(String.valueOf(distancia));
					   //Log.d(TAG, "ESTACIONAMIENTO CERCANO:" + estacionamiento.getNombre());
				 }
				 else
				 {
					   //Log.d(TAG, "SIN CERCANO");
				 }*/
				 //Log.d(TAG, "---");
		    }
	  }

	  private void processBeacon (Collection<Beacon> beacons)
	  {
		    new Thread(() ->
		    {
				 List<Terminal> availableTerminals = new ArrayList<>();
				 List<Parking> parkings = parkingViewModel.getParkingList().getValue();
				 if (parkings == null) return;
				 for (Beacon beacon : beacons)
				 {
					   String beaconUUID = beacon.getId1().toString().replace("-", "").toLowerCase();
					   Terminal terminalFromBeacon = null;
					  // System.out.println(beaconUUID);
					   parkingFor:
					   for (int i = 0; i < parkings.size(); i++)
					   {
							Parking parking = parkings.get(i);
							for (int j = 0; j < parking.getTerminals().size(); j++)
							{
								  Terminal terminal = parking.getTerminals().get(j);
								 // System.out.println("Terminal beacon : " + terminal.getBeaconUUID());
								  if (terminal.getBeaconUUID().equals(beaconUUID))
								  {
									    terminal.setActualIntensity(beacon.getRssi());
									    terminalFromBeacon = terminal;
									    break parkingFor;
								  }
							}
					   }
					   if (terminalFromBeacon != null && beacon.getRssi() > terminalFromBeacon.getDetectionIntensity())
					   {
					   	if (beacon.getDistance() < 0.0){

						} else if (beacon.getDistance() < 0.5){
						   	//Immediate
							   availableTerminals.add(terminalFromBeacon);
					   	} else if (beacon.getDistance() <= 3.0){
						   	//Near
							   availableTerminals.add(terminalFromBeacon);
					   	} else {
					   		//Far
							availableTerminals.add(terminalFromBeacon);
						}
					   }
				 }
				 parkingViewModel.setDataFromBeaconScann(availableTerminals);
		    }).start();
	  }

}
