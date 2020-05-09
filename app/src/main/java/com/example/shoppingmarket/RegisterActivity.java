package com.example.shoppingmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword;
    private ProgressBar loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        loadingBar = new ProgressBar(context this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount()
    {
        String name = InputName.getText() .toString();
        String phone = InputPhoneNumber.getText() .toString();
        String password = InputPassword.getText() .toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText (context:this, text:"Por favor ingresar nombre...", Toast.LENGTH_SHORT) .show();
        }

        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText (context:this, text:"Por favor ingresar su número de teléfono...", Toast.LENGTH_SHORT) .show();
        }

        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText (context:this, text:"Por favor ingresar contraseña...", Toast.LENGTH_SHORT) .show();
        }

        else
        {
            loadingBar.setTitle("Crear Cuenta");
            loadingBar.setMessage("Por favor espere mientras confirmamos credenciales.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatephoneNumber(name, phone, password);
        }

    }

    private void ValidatephoneNumber(String name, String phone, String password)
    {
        final DatabaseReference RootRef;
        RoofRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Número de Teléfono", phone);
                    userdataMap.put("Contraseña", password);
                    userdataMap.put("Nombre", name);

                    RootRef.child("Usuarios"). child(phone), updateChildren(userdataMap);
                            .addOnCompleteListener(new Loader.OnLoadCompleteListener(Void));
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                            {
                                    if (task.isSucessful())
                                    {
                                        Toast.makeText(context:RegisterActivity.this, text:"Felicitaciones, tu cuenta ha sido creada", Toast.LENGTH_SHORT) .show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(packageContext:RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(context:RegisterActivity.this, text="Error de la Red: Por favor, intentar otra vez", Toast.LENGTH_SHORT) .show();

                                    }
                }
                else
                {
                    Toast.makeText(context:RegisterActivity.this, text:"El Número " + phone + "ya está registrado.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(context:RegisterActivity.this, text:"Por favor, registre otro número de teléfono", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(packageContext:RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
