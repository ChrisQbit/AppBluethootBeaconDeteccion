package com.cdsautomatico.apparkame2.models.webServiceResponse;

import com.cdsautomatico.apparkame2.models.ExtendValidityModel;
import com.google.gson.annotations.Expose;

import retrofit2.Response;

public class GetPensionValidityResponse extends SimpleResponse
{
       @Expose
       private ExtendValidityModel pensionValidity;

       public GetPensionValidityResponse (Response response)
       {
              super(response);
       }

       public GetPensionValidityResponse (int code, String mensaje)
       {
              super(code, mensaje);
       }

       public GetPensionValidityResponse ()
       {
              super();
       }

       public ExtendValidityModel getPensionValidity ()
       {
              return pensionValidity;
       }

       public void setPensionValidity (ExtendValidityModel pensionValidity)
       {
              this.pensionValidity = pensionValidity;
       }
}
