package com.cdsautomatico.apparkame2.models.manager;

import com.cdsautomatico.apparkame2.models.AlertMessage;
import com.cdsautomatico.apparkame2.models.Boleto;
import com.cdsautomatico.apparkame2.models.SocketMessage;
import com.cdsautomatico.apparkame2.models.SpotAvailable;
import com.cdsautomatico.apparkame2.models.SpotBusy;
import com.cdsautomatico.apparkame2.models.TicketChange;
import com.cdsautomatico.apparkame2.models.enums.MessageType;
import com.cdsautomatico.apparkame2.utils.Constant;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;


public class SocketMessageHandler
{
	  private SocketMessage<?> message;
	  private MessageType type;

	  public SocketMessageHandler (JsonObject jsonObject)
	  {
		    type = MessageType.values()[jsonObject.get(Constant.Extra.TYPE).getAsInt()];
		    message = buildMessage(jsonObject.get(Constant.Extra.DATA).toString());
	  }

	  public SocketMessage<?> getMessage ()
	  {
		    return message;
	  }

	  private SocketMessage<?> buildMessage (String msg)
	  {
		    SocketMessage message = null;
		    switch (type)
		    {
				 case SpotAvailable:
				 {
					   SpotAvailable spotAvailabre = new SpotAvailable(msg);
					   message = new SocketMessage<>(spotAvailabre);
				 }
				 break;
				 case SpotBusy:
				 {
					   SpotBusy spotBusy = new SpotBusy(msg);
					   message = new SocketMessage<>(spotBusy);
				 }
				 break;
				 case TicketChange:
				 {
					   JSONObject json = null;
					   TicketChange change = null;
					   try
					   {
							json = new JSONObject(msg).getJSONObject("Ticket");
							change = new TicketChange(new Boleto(json));
					   }
					   catch (JSONException e)
					   {
							e.printStackTrace();
					   }
					   message = new SocketMessage<>(change);
				 }
				 break;
				 case Alert:
				 {
					   AlertMessage alertMessage = new AlertMessage(msg.replace("\"", ""));
					   message = new SocketMessage<>(alertMessage);
				 }
				 break;
		    }

		    return message;
	  }

	  public MessageType getType ()
	  {
		    return type;
	  }
}
