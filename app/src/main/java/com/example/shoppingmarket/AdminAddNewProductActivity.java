package com.example.shoppingmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;

public class AdminAddNewProductActivity extends AppCompatActivity
{
    private String CategoryName; Description, Price, Pname, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductRef;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        CategoryName = getIntent().getExtras().get("category").toString();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Imagenes de Productos");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Productos");


        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        loadingBar = new ProgressBar(context this);


        InputProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });


        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

    }


    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("Image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && requestCode == RESULT_OK && data != null)
        {
            ImageUri=data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }


    private void ValidateProductData() {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();


        if (ImageUri == null) {
            Toast.makeText(context:this, text:
            "Mandatorio, Imagen de Producto...", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(context:this, text:"Por favor describir el producto", Toast.LENGTH_SHORT).
            show();
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(context:this, text:"Por favor precio del producto", Toast.LENGTH_SHORT).
            show();
        } else if (TextUtils.isEmpty(Name)) {
            Toast.makeText(context:this, text:"Por favor nombrar el producto", Toast.LENGTH_SHORT).
            show();
        }
        else
            {
            StorageProductInformation();
            }
        }

        private void StorageProductInformation()

            {
                loadingBar.setTitle("Agregar nuevo producto");
                loadingBar.setMessage("Por favor espere mientras agregamos producto.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();


                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat currentDate = new SimpleDateFormat( pattern:"MM dd, yyyy");
                saveCurentDate = currentDate.format(calendar.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat( pattern:"HH:mm:ss a");
                saveCurentTime = currentTime.format(calendar.getTime());

                productRandomKey = saveCurrentDate + saveCurrentTime;

                final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey+ ".jpg");

                final UploadTask uploadTask = filePath.putFile(ImageUri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String message = e.toString();
                        Toast.makeText(context: AdminAddNewProductActivity.this, text: "Error: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss ()
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        Toast.makeText(context: AdminAddNewProductActivity.this, text: "Imagen del Producto fue Registrada..", Toast.LENGTH_SHORT).show();

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                            {
                                if (!task.isSuccessful())
                                {
                                    throw task.getException();
                                }

                                downloadImageUrl = filePath.getDownloadUrl().toString();
                                return filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(context: AdminAddNewProductActivity.this, text:"Url de imagen se adquirió con éxito", Toast.LENGTH_SHORT).show();

                                    SaveProductInfoToDatabase();
                                }
                            }
                        });

                    }
                });

            }

            private void SaveProductInfoToDatabase()
            {
                HashMap<String, Object> productMap = new Hashmap <>();
                productMap.put("pid", productRandomKey);
                productMap.put("Fecha", saveCurrentDate);
                productMap.put("Hora", saveCurrentTime);
                productMap.put("Descripción", Description);
                productMap.put("Imagen", downloadImageUrl);
                productMap.put("Categoria", CategoryName);
                productMap.put("Precio", Price);
                productMap.put("pname", Pname);


                ProductRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(context:AdminAddNewProductActivity.this, text: "Carga del Producto Exitosa..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(context:AdminAddNewProductActivity.this, text: " Error: " + message, Toast.LENGTH_SHORT).show();
                        }

                    }
                })
            }


        }
    }

}
