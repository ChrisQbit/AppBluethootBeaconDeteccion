package com.cdsautomatico.apparkame2.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Boleto implements Parcelable
{
	  //"ticket":{"parkingId":4,"userId":0,"pensionId":0,"amount":0.0,"entryDate":"2019-12-13T17:18:21","active":0,"balanceDate":"2019-12-13T17:20:41","exitDate":null,"cancelationDate":"0001-01-01T00:00:00","entryCdsComand":"23","exitCdsCommand":null,"creationDate":"2019-12-13T17:18:20","updatedDate":"0001-01-01T00:00:00","cdsRateId":1,"id":640,"status":"1"}}

	  @Expose
	  private int id;
	  @Expose
	  @SerializedName("parkingId")
	  private int estacionamientoId;
	  @Expose
	  private int pensionId;
	  @Expose
	  private int usuarioId;
	  @Expose
	  @SerializedName("entryDate")
	  private String horaEntrada;
	  @Expose
	  @SerializedName("balanceDate")
	  private String horaBalance;
	  @Expose
	  private String horaCancelacion;
	  @Expose
	  private String horaSalida;
	  @Expose
	  @SerializedName("active")
	  private int activo;
	  @Expose
	  private Parking parking;
	  @Expose
	  private int segundosPendientes;
	  @Expose
	  private double saldo;
	  @Expose
	  private int cdsRateId;

	  public Boleto ()
	  {
	  }

	  public Boleto (JSONObject json) throws JSONException
	  {
		    if (!json.isNull("Id"))
				 id = json.getInt("Id");
		    else if (!json.isNull("id"))
				 id = json.getInt("id");

		    if (!json.isNull("ParkingId"))
				 estacionamientoId = json.getInt("ParkingId");
		    else if (!json.isNull("estacionamiento_id"))
				 estacionamientoId = json.getInt("estacionamiento_id");

		    if (!json.isNull("UserId"))
				 usuarioId = json.getInt("UserId");
		    else if (!json.isNull("usuario_id"))
				 usuarioId = json.getInt("usuario_id");

		    if (!json.isNull("EntryDate"))
				 horaEntrada = json.getString("EntryDate");
		    else if (!json.isNull("hora_entrada"))
				 horaEntrada = json.getString("hora_entrada");

		    if (!json.isNull("Active"))
				 activo = json.getInt("Active");
		    else if (!json.isNull("activo"))
				 activo = json.getInt("activo");
		    else if (!json.isNull("active"))
				 activo = json.getInt("active");

		    if (!json.isNull("PensionId"))
				 pensionId = json.getInt("PensionId");
		    else if (!json.isNull("pension_id"))
				 pensionId = json.getInt("pension_id");

		    if (!json.isNull("BalanceDate"))
				 horaBalance = json.getString("BalanceDate");
		    else if (!json.isNull("hora_balance"))
				 horaBalance = json.getString("hora_balance");

		    if (!json.isNull("CancelationDate"))
				 horaCancelacion = json.getString("CancelationDate");
		    else if (!json.isNull("hora_cancelacion"))
				 horaCancelacion = json.getString("hora_cancelacion");

		    if (!json.isNull("ExitDate"))
				 horaSalida = json.getString("ExitDate");
		    else if (!json.isNull("hora_salida"))
				 horaSalida = json.getString("hora_salida");

		    //if (!json.isNull("parking"))
		    //parking = new Parking(json.getJSONObject("parking"));

		    //if (!json.isNull("segundos_pendientes"))
		    //segundosPendientes = json.getInt("segundos_pendientes");

		    if (!json.isNull("PendingSeconds"))
				 segundosPendientes = json.getInt("PendingSeconds") * 60;

		    if (!json.isNull("saldo"))
				 saldo = json.getDouble("saldo");

		    if (!json.isNull("cds_id_rate"))
				 cdsRateId = json.getInt("cds_id_rate");
		    else if (!json.isNull("CdsRateId"))
				 cdsRateId = json.getInt("CdsRateId");
	  }

	  protected Boleto (Parcel in)
	  {
		    id = in.readInt();
		    estacionamientoId = in.readInt();
		    pensionId = in.readInt();
		    usuarioId = in.readInt();
		    horaEntrada = in.readString();
		    horaBalance = in.readString();
		    horaCancelacion = in.readString();
		    horaSalida = in.readString();
		    activo = in.readInt();
		    segundosPendientes = in.readInt();
		    cdsRateId = in.readInt();
		    saldo = in.readDouble();
	  }

	  public static final Creator<Boleto> CREATOR = new Creator<Boleto>()
	  {
		    @Override
		    public Boleto createFromParcel (Parcel in)
		    {
				 return new Boleto(in);
		    }

		    @Override
		    public Boleto[] newArray (int size)
		    {
				 return new Boleto[size];
		    }
	  };

	  public Integer getId ()
	  {
		    return id;
	  }

	  public Integer getEstacionamientoId ()
	  {
		    return estacionamientoId;
	  }

	  public Integer getPensionId ()
	  {
		    return pensionId == 0 ? null : pensionId;
	  }

	  public void setPensionId (Integer pensionId)
	  {
		    this.pensionId = pensionId;
	  }

	  public Integer getUsuarioId ()
	  {
		    return usuarioId;
	  }

	  public String getHoraEntrada ()
	  {
		    return horaEntrada;
	  }

	  public String getHoraBalance ()
	  {
		    return horaBalance;
	  }

	  public String getHoraCancelacion ()
	  {
		    return horaCancelacion;
	  }

	  public String getHoraSalida ()
	  {
		    return horaSalida;
	  }

	  public int getActivo ()
	  {
		    return activo;
	  }

	  public Parking getParking ()
	  {
		    return parking;
	  }

	  public int getCdsRateId ()
	  {
		    return cdsRateId;
	  }

	  public void setCdsRateId (int cdsRateId)
	  {
		    this.cdsRateId = cdsRateId;
	  }

	  public long getSegundosPendientes ()
	  {
		    return segundosPendientes;
	  }

	  public double getSaldoPendiente ()
	  {
		    return saldo;
	  }

	  public void setSegundosPendientes (int segundosPendientes)
	  {
		    this.segundosPendientes = segundosPendientes;
	  }

	  @Override
	  public int describeContents ()
	  {
		    return 0;
	  }

	  @Override
	  public void writeToParcel (Parcel parcel, int i)
	  {
		    parcel.writeInt(id);
		    parcel.writeInt(estacionamientoId);
		    parcel.writeInt(pensionId);
		    parcel.writeInt(usuarioId);
		    parcel.writeString(horaEntrada);
		    parcel.writeString(horaBalance);
		    parcel.writeString(horaCancelacion);
		    parcel.writeString(horaSalida);
		    parcel.writeInt(activo);
		    parcel.writeInt(segundosPendientes);
		    parcel.writeInt(cdsRateId);
		    parcel.writeDouble(saldo);
	  }

	  public void setParking (Parking parking)
	  {
		    this.parking = parking;
	  }

	  public void setEstacionamientoId (Integer estacionamientoId)
	  {
		    this.estacionamientoId = estacionamientoId;
	  }
}
