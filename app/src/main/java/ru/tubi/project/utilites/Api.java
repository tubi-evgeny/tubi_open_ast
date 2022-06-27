package ru.tubi.project.utilites;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @Headers({"Accept: application/json",
            "User-Agent: Your-App-Name",
            "Cache-Control: max-age=640000"
    })
    @POST("upload_image.php")
    Call<ResponsePOJO> uploadImage(
            @Field("EN_IMAGE") String encodeImage,
            @Field("EN_NAME") String imageName
    );
}
