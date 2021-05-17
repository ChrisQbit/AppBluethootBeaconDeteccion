package com.cdsautomatico.apparkame2.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.utils.Constant;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener
{
       public static void showMessage(Context ctx, String title, String message, int iconRes)
       {
              Intent intent = new Intent(ctx, MessageActivity.class);
              intent.putExtra(Constant.Extra.TITLE, title);
              intent.putExtra(Constant.Extra.MESSAGE, message);
              intent.putExtra(Constant.Extra.ICON_RES, iconRes);
              ctx.startActivity(intent);
       }

       @Override
       protected void onCreate (@Nullable Bundle savedInstanceState)
       {
              super.onCreate(savedInstanceState);
              setContentView(R.layout.dialogo_mensaje);

              TextView tvTitle = findViewById(R.id.dialogoMensajeTitulo),
                    tvMesage = findViewById(R.id.dialogoMensajeTexto);
              ImageView ivIcon = findViewById(R.id.dialogoMensajeIcon),
                    btnCerrar = findViewById(R.id.dialogoMensajeCerrar);
              Button btnAceptar = findViewById(R.id.dialogoMensajeAceptar);

              Bundle extras = getIntent().getExtras();
              if (extras != null)
              {
                     if (extras.getString(Constant.Extra.TITLE) != null)
                            tvTitle.setText(extras.getString(Constant.Extra.TITLE));
                     if (extras.getString(Constant.Extra.MESSAGE) != null)
                            tvMesage.setText(extras.getString(Constant.Extra.MESSAGE));
                     if(extras.containsKey(Constant.Extra.ICON_RES))
                            ivIcon.setImageResource(extras.getInt(Constant.Extra.ICON_RES));
              }

              btnCerrar.setOnClickListener(this);
              btnAceptar.setOnClickListener(this);
       }

       @Override
       public void onClick (View view)
       {
              switch (view.getId())
              {
                     case R.id.dialogoMensajeAceptar:
                     case R.id.dialogoMensajeCerrar:
                     {
                           finish();
                     }
                     break;
              }
       }
}
