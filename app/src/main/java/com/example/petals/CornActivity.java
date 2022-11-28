package com.example.petals;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petals.ml.BellpepperInt8;
import com.example.petals.ml.BellpepperModelmob;
import com.example.petals.ml.CornInt8New;
import com.example.petals.ml.CornModelmob2;
import com.example.petals.ml.Mobilenet;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class CornActivity extends AppCompatActivity {
    Button btn;
    ImageView belly;
    Button predict;
    Bitmap bitmap;
    TextView result,description;

    int imageSize = 224;
    String str_Corn_Cercospora_leaf_spot = "For gray spots in corn leaves, it is advised to spray Syngenta Trivapro A and B fungicide. This fungicide enters the plant system and is able to eradicate an established infection. Trivapro A: 400 mL/ac (1L/ha) + Trivapro B: 120 mL/ac";
    String str_Corn_Common_rust = "For Common rust in corn, it is advised to spray Syngenta kavach chlorothalonil fungicide 240-320 ML/ acre. This  fungicide enters the plant system and is able to eradicate an established infection. If the weather conditions remain favorable for late blight, then the spray of  fungicide may be repeated at 10 days intervals.";
    String str_Corn_Northern_Leaf_Blight = "For Common rust in corn, it is advised to spray Syngenta kavach chlorothalonil fungicide 240-320 ML/ acre. This  fungicide enters the plant system and is able to eradicate an established infection. If the weather conditions remain favorable for late blight, then the spray of  fungicide may be repeated at 10 days intervals.";
    String str_Corn_healthy = "Corn leaf seems to be healthy. This means there is no need to spray any fungicide. However, please have a look at other leaves as there might be some part of the plant which has just started to get infected with some disease.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bellpepper);
        btn = findViewById(R.id.capture);
        belly = findViewById(R.id.belly);
        predict=findViewById(R.id.predict);
        result=findViewById(R.id.result);
        description = findViewById(R.id.description);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        String[]labels=new String[1002];
//        int cnt=0;
//        try {
//            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(getAssets().open("labels.txt")));
//
//            Log.e("Response", "bufferedReader: " +bufferedReader);
//
//            String line=bufferedReader.readLine();
//            while (line!=null){
//                labels[cnt]=line;
//                cnt++;
//                line=bufferedReader.readLine();
//
//                Log.e("Response","line: " +line);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.e("Response", "bitmap: " +bitmap);
                    classifyImage(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CornActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                try {
                    if (ContextCompat.checkSelfPermission(CornActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(CornActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(CornActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(CornActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else if (options[item].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 1);
                    } else if (options[item].equals("Choose from Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, 2);
                    } else if (options[item].equals("Cancel")) {
                        try {
//                            belly.setImageDrawable();
                            // Toast.makeText(Bellpepper.this, "Image is R" + "removed", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                bitmap = (Bitmap) data.getExtras().get("data");
                Log.e("Response", "Uri: " + bitmap);
                belly.setImageBitmap(bitmap);
            }
            if (requestCode == 2) {

                Uri dat = data.getData();
                bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                }catch (IOException e){
                    e.printStackTrace();
                }

                belly.setImageBitmap(bitmap);
                bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false);

            }
        }
    }
    int getMax(float arr[]){
        int max=0;
        for (int i=0;i<arr.length;i++){
            if (arr[i]>arr[max])max=i;
        }
        return max;
    }


    public void classifyImage(Bitmap image){

        try {
            CornInt8New model = CornInt8New.newInstance(CornActivity.this);
            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            CornInt8New.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Corn has Gray leaf spot", "Corn has Common rust","Corn has Northern Leaf Blight","Corn seems healthy"};
            result.setText(classes[maxPos]);
            description.setVisibility(View.INVISIBLE);
            // adding description
            Log.e("Response", "maxPos: " +classes[maxPos]);
            Log.e("Response", "maxPos: " +maxPos);
            if (maxPos == 0){
                description.setVisibility(View.VISIBLE);
                description.setText(str_Corn_Cercospora_leaf_spot);
            }else if (maxPos == 1){
                description.setVisibility(View.VISIBLE);
                description.setText(str_Corn_Common_rust);
            } else if (maxPos == 2){
                description.setVisibility(View.VISIBLE);
                description.setText(str_Corn_Northern_Leaf_Blight);
            } else if (maxPos == 3) {
                description.setVisibility(View.VISIBLE);
                description.setText(str_Corn_healthy);
            }


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

}

