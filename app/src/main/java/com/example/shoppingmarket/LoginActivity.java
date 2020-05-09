package com.example.shoppingmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingmarket.Model.Users;
import com.example.shoppingmarket.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity
{
    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressBar loadingBar;
    private TextView AdminLink, NotAdminLink;

    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        loadingBar = new ProgressBar(context this);


        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(context:this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });


        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });


        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginButton.setText(("Login"));
                AdminLink.setVisibility((View.VISIBLE));
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });

    }

    private void LoginUser()
    {
        String phone = InputPhoneNumber.getText() .toString();
        String password = InputPassword.getText() .toString();

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText (context:this, text:"Por favor ingresar su número de teléfono...", Toast.LENGTH_SHORT) .show();
        }

        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText (context:this, text:"Por favor ingresar su contraseña...", Toast.LENGTH_SHORT) .show();
        }
        else
        {
            loadingBar.setTitle("Ingresar a Cuenta");
            loadingBar.setMessage("Por favor espere mientras confirmamos credenciales.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount (phone, password);

        }
    }

    private void AllowAccessToAccount(String phone, final String password)
    {
        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RoofRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbname) .child(phone) .exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName) .child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText((context:LoginActivity.this, text:"Welcome Admin, you are logged in..", Toast.LENGTH_SHORT).show());
                                loadingBar.dismiss();

                                Intent intent= new Intent(packageContext:LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users"))
                            {
                                Toast.makeText((context:LoginActivity.this, text:"Ingresado a sistema.", Toast.LENGTH_SHORT).show());
                                loadingBar.dismiss();

                                Intent intent = new Intent (packageContext:LoginActivity, this, HomeActivity.class);
                                startActivity(intent);
                            }
                        else
                        {
                          loadingBar.dismiss();
                          Toast.makeText(context:LoginActivity.this, text:"Password in incorrect", Toast.LENGTH_SHORT).show());
                        }
                    }
                }

                else
                {
                    Toast.makeText(context:LoginActivity.this, text:"La Cuenta con el número " + phone + "no existe.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(context:LoginActivity.this, text:"Debe crear una cuenta.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    }