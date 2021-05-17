package com.cdsautomatico.apparkame2.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.dataSource.SocketConsumer;
import com.cdsautomatico.apparkame2.models.SocketMessage;
import com.cdsautomatico.apparkame2.models.SpotBusy;
import com.cdsautomatico.apparkame2.repository.UserPreferences;
import com.cdsautomatico.apparkame2.utils.Constant;

public class NoMapa extends AppCompatActivity implements View.OnClickListener
{
	  private ImageView ivMyDot;
	  private Button btnActivateAlarm;

	  private UserPreferences preferences;
	  private BroadcastReceiver receiver = new BroadcastReceiver()
	  {
		    @Override
		    public void onReceive (Context context, Intent intent)
		    {
				 Bundle extras = intent.getExtras();
				 if (extras == null)
					   return;
				 Parcelable parcelable = extras.getParcelable(Constant.Extra.DATA);
				 if (parcelable instanceof SocketMessage<?>)
				 {
					   SocketMessage<?> socketMessage = (SocketMessage<?>) parcelable;
					   if (socketMessage.getValue() instanceof SpotBusy)
					   {
							btnActivateAlarm.setVisibility(View.VISIBLE);
					   }
				 }
		    }
	  };

	  @Override
	  protected void onCreate (Bundle savedInstanceState)
	  {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.activity_no_mapa);
		    ivMyDot = findViewById(R.id.myDot);
		    preferences = new UserPreferences(this);
		    btnActivateAlarm = findViewById(R.id.activateAlarm);
		    shrink();
		    startService(new Intent(this, SocketConsumer.class));
		    btnActivateAlarm.setOnClickListener(this);
		    if (savedInstanceState != null)
		    {
				 boolean btnVisible = savedInstanceState.getBoolean("visible");
				 if (btnVisible)
					   btnActivateAlarm.setVisibility(View.VISIBLE);
		    }
		    if (preferences.getLastOccupation() != null && preferences.isLocked())
		    {
				 btnActivateAlarm.setVisibility(View.VISIBLE);
				 btnActivateAlarm.setText("Desactivar alarma para el cajón " + preferences.getLastOccupation());
				 btnActivateAlarm.setCompoundDrawables(null, null, null, null);
		    }
	  }

	  @Override
	  protected void onSaveInstanceState (Bundle outState)
	  {
		    outState.putBoolean("visible", btnActivateAlarm.getVisibility() == View.VISIBLE);
		    super.onSaveInstanceState(outState);
	  }

	  @Override
	  protected void onResume ()
	  {
		    super.onResume();
		    LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(SocketConsumer.SOCKET_MESSAGE));
	  }

	  @Override
	  protected void onPause ()
	  {
		    super.onPause();
		    LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
	  }

	  private void grow ()
	  {
		    Animation increase = AnimationUtils.loadAnimation(this, R.anim.grow_animation);
		    increase.setDuration(1000);
		    increase.setAnimationListener(new Animation.AnimationListener()
		    {
				 @Override
				 public void onAnimationStart (Animation animation)
				 {}

				 @Override
				 public void onAnimationEnd (Animation animation)
				 {
					   shrink();
				 }

				 @Override
				 public void onAnimationRepeat (Animation animation)
				 {}
		    });
		    ivMyDot.startAnimation(increase);
	  }

	  private void shrink ()
	  {
		    Animation decrease = AnimationUtils.loadAnimation(this, R.anim.shrink_animation);
		    decrease.setDuration(1000);
		    decrease.setAnimationListener(new Animation.AnimationListener()
		    {
				 @Override
				 public void onAnimationStart (Animation animation)
				 {}

				 @Override
				 public void onAnimationEnd (Animation animation)
				 {
					   grow();
				 }

				 @Override
				 public void onAnimationRepeat (Animation animation)
				 {}
		    });
		    ivMyDot.startAnimation(decrease);
	  }

	  @Override
	  public void onClick (View view)
	  {
		    if (view.getId() == R.id.activateAlarm)
		    {
				 String place = preferences.getLastOccupation();
				 AlertDialog dialog;
				 if (preferences.isLocked())
				 {
					   dialog = new AlertDialog.Builder(this)
						    .setMessage("¿Desactivar la alarma para lugar " + place + "?")
						    .setTitle("Alarma de lugar")
						    .setPositiveButton("Desactivar", (dialogInterface, i) ->
						    {
								 preferences.setLock(false);
								 btnActivateAlarm.setText(R.string.activate_lock);
								 btnActivateAlarm.setCompoundDrawables(null, null, null, getResources().getDrawable(android.R.drawable.ic_lock_idle_lock));
								 dialogInterface.dismiss();
						    }).create();
				 }
				 else
				 {
					   dialog = new AlertDialog.Builder(this)
						    .setMessage("¿Activar alarma para lugar " + place + "?")
						    .setTitle("Alarma de lugar")
						    .setPositiveButton("Activar", (dialogInterface, i) ->
						    {
								 preferences.setLock(true);
								 btnActivateAlarm.setText("Desactivar alarma para el cajón " + place);
								 btnActivateAlarm.setCompoundDrawables(null, null, null, null);
								 dialogInterface.dismiss();
						    }).create();
				 }
				 dialog.show();
		    }
	  }
}
