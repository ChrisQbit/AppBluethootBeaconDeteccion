package com.cdsautomatico.apparkame2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.models.Terminal;

import java.util.List;

/**
 * Created by alangvara on 27/04/18.
 */

public class DialogoCarril extends FrameLayout
{

	  private View mainView;
	  private View modal;
	  private View dialog;
	  private View btnConfirmar;
	  private List<Terminal> terminales;
	  private Runnable confirmCallback;
	  private RadioGroup radioGroupTerminales;

	  private Terminal carrilSeleccionado;

	  public DialogoCarril (Context context)
	  {
		    super(context);
		    init();
	  }

	  public DialogoCarril (Context context, AttributeSet attrs)
	  {
		    super(context, attrs);
		    init();
	  }

	  public DialogoCarril (Context context, AttributeSet attrs, int defStyle)
	  {
		    super(context, attrs, defStyle);
		    init();
	  }

	  private void init ()
	  {
		    mainView = inflate(getContext(), R.layout.dialogo_seleccionar_carril, null);
		    addView(mainView);

		    modal = mainView.findViewById(R.id.modal);
		    dialog = mainView.findViewById(R.id.dialog);
		    btnConfirmar = mainView.findViewById(R.id.btnConfirmar);
		    radioGroupTerminales = findViewById(R.id.radioGroupTerminales);

		    mainView.setVisibility(INVISIBLE);
		    modal.setOnClickListener((View view) -> hide());

		    btnConfirmar.setOnClickListener((View view) ->
		    {
				 btnConfirmar.setEnabled(false);
				 hide();
				 if (confirmCallback != null)
				 {
					   confirmCallback.run();
				 }
		    });

		    radioGroupTerminales.setOnCheckedChangeListener((RadioGroup radioGroup, int i) -> carrilSeleccionado = terminales.get(i));
	  }

	  public void setTerminales (List<Terminal> terminales)
	  {
		    this.terminales = terminales;
		    radioGroupTerminales.removeAllViews();

		    int i = 0;

		    if (terminales.size() > 0)
		    {
				 btnConfirmar.setEnabled(true);
				 for (Terminal terminal : terminales)
				 {
					   RadioButton radio = (RadioButton) inflate(getContext(), R.layout.template_list_radio_forma_pago, null);
					   radio.setText(terminal.getName());

					   RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
						    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					   radio.setId(i);
					   radio.setLayoutParams(layoutParams);

					   if (i == 0)
					   {
							carrilSeleccionado = terminal;
							radio.setChecked(true);
					   }

					   radioGroupTerminales.addView(radio);
					   i++;
				 }
		    }
		    else
		    {
				 btnConfirmar.setEnabled(false);
		    }
	  }

	  public void setOnConfirm (Runnable callback)
	  {
		    this.confirmCallback = callback;
	  }

	  @Override
	  protected void onFinishInflate ()
	  {
		    super.onFinishInflate();
	  }

	  public void show ()
	  {
		    mainView.setVisibility(VISIBLE);
		    btnConfirmar.setEnabled(true);
		    Animation animFadeIn = new AlphaAnimation(0f, 1f);
		    animFadeIn.setInterpolator(new AccelerateInterpolator());
		    animFadeIn.setDuration(200);
		    animFadeIn.setFillAfter(true);
		    modal.startAnimation(animFadeIn);

		    Animation animSlideUp = new TranslateAnimation(0, 0,
				dialog.getMeasuredHeight(), 0);
		    animFadeIn.setInterpolator(new AccelerateInterpolator());
		    animSlideUp.setDuration(200);
		    animSlideUp.setFillAfter(true);
		    dialog.startAnimation(animSlideUp);
	  }

	  public void hide ()
	  {
		    Animation animFadeIn = new AlphaAnimation(1f, 0f);
		    animFadeIn.setInterpolator(new DecelerateInterpolator());
		    animFadeIn.setDuration(200);
		    animFadeIn.setFillAfter(true);
		    modal.startAnimation(animFadeIn);

		    Animation animSlideUp = new TranslateAnimation(0, 0, 0,
				dialog.getMeasuredHeight());
		    animFadeIn.setInterpolator(new DecelerateInterpolator());
		    animSlideUp.setDuration(200);
		    animSlideUp.setFillAfter(true);


		    animSlideUp.setAnimationListener(new Animation.AnimationListener()
		    {
				 @Override
				 public void onAnimationStart (Animation animation)
				 {

				 }

				 @Override
				 public void onAnimationEnd (Animation animation)
				 {
					   mainView.setVisibility(GONE);
				 }

				 @Override
				 public void onAnimationRepeat (Animation animation)
				 {

				 }
		    });

		    dialog.startAnimation(animSlideUp);
	  }

	  public Terminal getCarrilSeleccionado ()
	  {
		    return carrilSeleccionado;
	  }

}
