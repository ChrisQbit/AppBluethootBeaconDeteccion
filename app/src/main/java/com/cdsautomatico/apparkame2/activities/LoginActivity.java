package com.cdsautomatico.apparkame2.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.Toast;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.api.ApiHelper;
import com.cdsautomatico.apparkame2.api.ApiSession;
import com.cdsautomatico.apparkame2.models.Usuario;
import com.cdsautomatico.apparkame2.repository.UserPreferences;
import com.cdsautomatico.apparkame2.utils.FormErrors;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import httprequest.HttpRequestCallback;
import httprequest.HttpRequestException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

	  private static final String TAG = "LoginActivity";

	  // ---- LOGIN ----
	  private View contLoginForm;
	  private FormErrors formLoginErrors;
	  private EditText txtLoginEmail;
	  private EditText txtLoginPassword;
	  private CircularProgressButton btnLogin;

	  // ---- LOGIN ----
	  private View contRegistroForm;
	  private FormErrors formSignupErrors;
	  private EditText txtSignupNombre;
	  private EditText txtSignupEmail;
	  private EditText txtSignupPassword;
	  private EditText txtSignupPassword2;
	  private CircularProgressButton btnSignup;

	  // ---- FB LOGIN ----
	  private CallbackManager fbCallbackManager;
	  private View btnFacebook;
	  private View btnFacebookIcon;
	  private View btnFacebookProgressBar;


	  @Override
	  protected void onCreate (Bundle savedInstanceState)
	  {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.activity_login);
		    ApiSession.setContext(getBaseContext());

		    UserPreferences userPreferences = new UserPreferences(this);
		    if (userPreferences.showOnboarding())
		    {
				 Intent intent = new Intent(this, OnboardingActivity.class);
				 startActivity(intent);
		    }

		    printHash();

		    // ---- LOGIN -----
		    contLoginForm = findViewById(R.id.contLoginForm);
		    txtLoginEmail = findViewById(R.id.loginEmail);
		    txtLoginPassword = findViewById(R.id.loginPassword);
		    btnLogin = findViewById(R.id.btnLogin);

		    formLoginErrors = new FormErrors();
		    formLoginErrors.setInput("email", txtLoginEmail);
		    formLoginErrors.setInput("password", txtLoginPassword);

		    // ---- SIGNUP ----
		    contRegistroForm = findViewById(R.id.contRegistroForm);
		    txtSignupNombre = findViewById(R.id.signupNombre);
		    txtSignupEmail = findViewById(R.id.signupEmail);
		    txtSignupPassword = findViewById(R.id.signupPassword);
		    txtSignupPassword2 = findViewById(R.id.signupPassword2);
		    btnSignup = findViewById(R.id.btnSignup);

		    formSignupErrors = new FormErrors();
		    formSignupErrors.setInput("nombre", txtSignupNombre);
		    formSignupErrors.setInput("email", txtSignupEmail);
		    formSignupErrors.setInput("password", txtSignupPassword);

		    // ---- FB LOGIN -----
		    btnFacebookIcon = findViewById(R.id.btnFacebookIcon);
		    btnFacebookProgressBar = findViewById(R.id.btnFacebookProgressBar);
		    setupFacebookLogin();

//              setupVideo();
	  }

       /*private void setupVideo ()
       {
              VideoView videoBackground = findViewById(R.id.videoBackground);
              Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login_background);
              videoBackground.setVideoURI(uri);
              videoBackground.start();

              // ---- FILL SCREEN ----

              Point winSize = getWindowSize();

              int newWidth = winSize.x;
              int newHeight = (int) ((1080f * (float) winSize.x) / 608f);

              if (newHeight < winSize.y)
              {
                     newWidth = (int) ((608f * (float) winSize.y) / 1080f);
                     newHeight = winSize.y;
              }

              ViewGroup.LayoutParams layoutParams = videoBackground.getLayoutParams();
              layoutParams.width = newWidth;
              layoutParams.height = newHeight;
              videoBackground.setLayoutParams(layoutParams);

              // ---- LOOP ----

              videoBackground.setOnPreparedListener((MediaPlayer mp) ->
              {
                     mp.setLooping(true);
              });
       }*/


	  private void doLogin ()
	  {
		    btnLogin.startAnimation();
		    btnLogin.setEnabled(false);

		    ApiHelper.login(txtLoginEmail.getText().toString(), txtLoginPassword.getText().toString(), new HttpRequestCallback()
		    {
				 @Override
				 public void onSuccess (JSONObject json)
				 {
					   btnLogin.revertAnimation();
					   btnLogin.setEnabled(true);

					   try
					   {
							if (json.getBoolean("success"))
							{
								  ApiSession.setAuthToken(json.getString("auth_token"));
								  ApiSession.setUsuario(new Usuario(json.getJSONObject("usuario")));
								  ApiHelper.setAuthToken(ApiSession.getAuthToken());

								  Intent intent = new Intent(getActivity(), MainActivity.class);
								  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
								  startActivity(intent);
							}
							else
							{
								  if (!json.isNull("errors"))
								  {
									    formLoginErrors.renderErrors(json.getJSONObject("errors"));
								  }
								  else
								  {
									    Toast.makeText(getBaseContext(), json.getString("message"),
											Toast.LENGTH_SHORT).show();
								  }
							}
					   }
					   catch (Exception e)
					   {
							e.printStackTrace();
							Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
					   }
				 }

				 @Override
				 public void onError (HttpRequestException e)
				 {
					   e.printStackTrace();
					   btnLogin.revertAnimation();
					   btnLogin.setEnabled(true);
					   Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
				 }
		    });
	  }

	  private void showLogin ()
	  {
		    contLoginForm.setVisibility(View.VISIBLE);

		    Animation slideIn = new TranslateAnimation(0, 0, getWindowSize().y, 0);
		    slideIn.setInterpolator(new DecelerateInterpolator());
		    slideIn.setDuration(500);
		    slideIn.setFillAfter(true);
		    contLoginForm.startAnimation(slideIn);
	  }

	  private void hideLogin ()
	  {
		    Animation slideOut = new TranslateAnimation(0, 0, 0, getWindowSize().y);
		    slideOut.setInterpolator(new AccelerateInterpolator());
		    slideOut.setDuration(500);
		    slideOut.setFillAfter(false);

		    slideOut.setAnimationListener(new Animation.AnimationListener()
		    {
				 @Override
				 public void onAnimationStart (Animation animation)
				 {

				 }

				 @Override
				 public void onAnimationEnd (Animation animation)
				 {
					   contLoginForm.setVisibility(View.GONE);
				 }

				 @Override
				 public void onAnimationRepeat (Animation animation)
				 {

				 }
		    });

		    contLoginForm.startAnimation(slideOut);
	  }

	  private void showSignup ()
	  {
		    contRegistroForm.setVisibility(View.VISIBLE);

		    Animation slideIn = new TranslateAnimation(0, 0, getWindowSize().y, 0);
		    slideIn.setInterpolator(new DecelerateInterpolator());
		    slideIn.setDuration(500);
		    slideIn.setFillAfter(true);
		    contRegistroForm.startAnimation(slideIn);
	  }

	  private void hideSignup ()
	  {
		    Animation slideOut = new TranslateAnimation(0, 0, 0, getWindowSize().y);
		    slideOut.setInterpolator(new AccelerateInterpolator());
		    slideOut.setDuration(500);
		    slideOut.setFillAfter(false);

		    slideOut.setAnimationListener(new Animation.AnimationListener()
		    {
				 @Override
				 public void onAnimationStart (Animation animation)
				 {

				 }

				 @Override
				 public void onAnimationEnd (Animation animation)
				 {
					   contRegistroForm.setVisibility(View.GONE);
				 }

				 @Override
				 public void onAnimationRepeat (Animation animation)
				 {

				 }
		    });

		    contRegistroForm.startAnimation(slideOut);
	  }

	  private void doSignup ()
	  {
		    btnSignup.startAnimation();
		    btnSignup.setEnabled(false);

		    ApiHelper.registro(txtSignupNombre.getText().toString(), txtSignupEmail.getText().toString(),
				txtSignupPassword.getText().toString(), txtSignupPassword2.getText().toString(), new HttpRequestCallback()
				{
					  @Override
					  public void onSuccess (JSONObject json)
					  {
						    btnSignup.revertAnimation();
						    btnSignup.setEnabled(true);

						    try
						    {
								 if (json.getBoolean("success"))
								 {
									   ApiSession.setAuthToken(json.getString("auth_token"));
									   ApiSession.setUsuario(new Usuario(json.getJSONObject("usuario")));

									   Intent intent = new Intent(getActivity(), MainActivity.class);
									   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
									   startActivity(intent);
								 }
								 else
								 {
									   if (!json.isNull("errors"))
									   {
											formSignupErrors.renderErrors(json.getJSONObject("errors"));
									   }
									   else
									   {
											Toast.makeText(getBaseContext(), json.getString("message"),
												 Toast.LENGTH_SHORT).show();
									   }
								 }
						    }
						    catch (Exception e)
						    {
								 e.printStackTrace();
								 Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
						    }
					  }

					  @Override
					  public void onError (HttpRequestException e)
					  {
						    e.printStackTrace();
						    btnSignup.revertAnimation();
						    btnSignup.setEnabled(true);
						    Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
					  }
				});
	  }

	  private void setupFacebookLogin ()
	  {
		    btnFacebook = findViewById(R.id.btnFacebook);
		    fbCallbackManager = CallbackManager.Factory.create();

		    LoginManager.getInstance().registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>()
		    {

				 @Override
				 public void onSuccess (LoginResult loginResult)
				 {
					   Log.d(TAG, "fb success");
					   sendFacebookTokenServer(loginResult.getAccessToken());
				 }

				 @Override
				 public void onCancel ()
				 {
					   btnFacebook.setClickable(true);
					   btnFacebookIcon.setVisibility(View.VISIBLE);
					   btnFacebookProgressBar.setVisibility(View.GONE);
				 }

				 @Override
				 public void onError (FacebookException error)
				 {
					   btnFacebook.setClickable(true);
					   btnFacebookIcon.setVisibility(View.VISIBLE);
					   btnFacebookProgressBar.setVisibility(View.GONE);

					   error.printStackTrace();
					   Toast.makeText(getBaseContext(), R.string.error_facebook, Toast.LENGTH_LONG).show();
				 }
		    });
	  }

	  private void sendFacebookTokenServer (AccessToken accessToken)
	  {
		    ApiHelper.loginFacebook(accessToken.getToken(), new HttpRequestCallback()
		    {
				 @Override
				 public void onSuccess (JSONObject json)
				 {
					   btnFacebook.setClickable(true);
					   btnFacebookIcon.setVisibility(View.VISIBLE);
					   btnFacebookProgressBar.setVisibility(View.GONE);

					   try
					   {
							if (json.getBoolean("success"))
							{
								  ApiSession.setAuthToken(json.getString("auth_token"));
								  ApiSession.setUsuario(new Usuario(json.getJSONObject("usuario")));

								  Intent intent = new Intent(getActivity(), MainActivity.class);
								  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
								  startActivity(intent);
							}
							else
							{
								  if (!json.isNull("errors"))
								  {
									    formLoginErrors.renderErrors(json.getJSONObject("errors"));
								  }
								  else
								  {
									    Toast.makeText(getBaseContext(), json.getString("message"),
											Toast.LENGTH_SHORT).show();
								  }
							}
					   }
					   catch (Exception e)
					   {
							e.printStackTrace();
							Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
					   }
				 }

				 @Override
				 public void onError (HttpRequestException e)
				 {
					   btnFacebook.setClickable(true);
					   btnFacebookIcon.setVisibility(View.VISIBLE);
					   btnFacebookProgressBar.setVisibility(View.GONE);

					   e.printStackTrace();
					   Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
				 }
		    });
	  }

	  private void doFacebookLogin ()
	  {
		    final AccessToken currentAccessToken = AccessToken.getCurrentAccessToken();

		    btnFacebook.setClickable(false);
		    btnFacebookIcon.setVisibility(View.GONE);
		    btnFacebookProgressBar.setVisibility(View.VISIBLE);


		    if (currentAccessToken != null)
		    {
				 sendFacebookTokenServer(currentAccessToken);
		    }
		    else
		    {
				 LoginManager.getInstance()
					  .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
		    }
	  }

	  private void mostrarPoliticas ()
	  {
		    Uri uri = Uri.parse("http://www.apparkame.com");
		    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		    startActivity(intent);
	  }

	  private LoginActivity getActivity ()
	  {
		    return this;
	  }

	  private Point getWindowSize ()
	  {
		    Point size = new Point();
		    getWindowManager().getDefaultDisplay().getSize(size);
		    return size;
	  }

	  @Override
	  public void onBackPressed ()
	  {
		    if (contRegistroForm.getVisibility() == View.VISIBLE)
		    {
				 hideSignup();
		    }
		    else if (contLoginForm.getVisibility() == View.VISIBLE)
		    {
				 hideLogin();
		    }
		    else
		    {
				 super.onBackPressed();
		    }
	  }

	  @Override
	  public void onClick (View v)
	  {
		    Intent intent;

		    switch (v.getId())
		    {
				 case R.id.btnShowLogin:
					   showLogin();
					   break;
				 case R.id.btnLogin:
					   doLogin();
					   break;
				 case R.id.btnShowSignup:
					   showSignup();
					   break;
				 case R.id.btnSignup:
					   doSignup();
					   break;
				 case R.id.btnFacebook:
					   doFacebookLogin();
					   break;
				 case R.id.btnForgotPassword:
					   intent = new Intent(this, RecuperarContraseniaActivity.class);
					   startActivity(intent);
					   break;
				 case R.id.btnPoliticas:
					   mostrarPoliticas();
					   break;
		    }
	  }

	  @Override
	  protected void onActivityResult (int requestCode, int resultCode, Intent data)
	  {
		    fbCallbackManager.onActivityResult(requestCode, resultCode, data);
		    super.onActivityResult(requestCode, resultCode, data);
	  }

	  public void printHash ()
	  {
		    try
		    {
				 PackageInfo info = getPackageManager().getPackageInfo("com.cdsautomatico.apparkame2",
					  PackageManager.GET_SIGNATURES);
				 for (Signature signature : info.signatures)
				 {
					   MessageDigest md;
					   md = MessageDigest.getInstance("SHA");
					   md.update(signature.toByteArray());
					   String something = new String(Base64.encode(md.digest(), 0));
					   //String something = new String(HttpRequest.Base64.encodeBytes(md.digest()));
					   Log.e("hash key", something);
				 }
		    }
		    catch (PackageManager.NameNotFoundException e1)
		    {
				 Log.e("name not found", e1.toString());
		    }
		    catch (NoSuchAlgorithmException e)
		    {
				 Log.e("no such an algorithm", e.toString());
		    }
		    catch (Exception e)
		    {
				 Log.e("exception", e.toString());
		    }
	  }

}
