package com.example.hp.imageupload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    EditText text;
    ImageView image;
    Button choose_button,upload_button;
    static final int IMG_REQUEST=777;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        choose_button=findViewById(R.id.choose_image_button);
        upload_button=findViewById(R.id.upload_image_button);
        text=findViewById(R.id.user_image_description);
        image=findViewById(R.id.user_image);
        choose_button.setOnClickListener(this);
        upload_button.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.choose_image_button:
                selectImage();
                break;
            case R.id.upload_image_button:
                uploadImage();
                break;

        }
    }

    private void selectImage()
    {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,IMG_REQUEST);
    }

    private void uploadImage()
    {
        String Image=imageToString();
        Api api=ApiClient.getApiClient().create(Api.class);
        Call<ImageClass> call=api.uploadImage(Image);

        call.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
                ImageClass imageClass=response.body();
                Toast.makeText(MainActivity.this, imageClass.getResponse(), Toast.LENGTH_SHORT).show();
                text.setText("Image Uploaded!");
                choose_button.setEnabled(true);
                upload_button.setEnabled(false);
            }

            @Override
            public void onFailure(Call<ImageClass> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                text.setText(t.getMessage());
            }
        });
    }
    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMG_REQUEST&&resultCode==RESULT_OK&&data!=null)
        {
            Uri path=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                image.setImageBitmap(bitmap);
                choose_button.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String imageToString()
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte=byteArrayOutputStream.toByteArray();
        Base64.encodeToString(imgByte,Base64.DEFAULT);

        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }
}
