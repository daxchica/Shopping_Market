package com.example.shoppingmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity
{
    private ImageView veg_items, meat_fish, snacks1;
    private ImageView snacks2, personal_care, house_cleaning;
    private ImageView first_aid, office, liquor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        veg_items = (ImageView) findViewById(R.id.veg_items);
        meat_fish = (ImageView) findViewById(R.id.meat_fish_items);
        snacks1 = (ImageView) findViewById(R.id.snacks1_items);
        snacks2 = (ImageView) findViewById(R.id.snacks2_items);
        personal_care = (ImageView) findViewById(R.id.personal_care_items);
        house_cleaning = (ImageView) findViewById(R.id.house_cleaning_items);
        first_aid = (ImageView) findViewById(R.id.first_aid_items);
        office = (ImageView) findViewById(R.id.office_items);
        liquor = (ImageView) findViewById(R.id.liquor_items);


        veg_items.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                Intent.putExtra(name:"category", value:"veg_items");
                startActivity(intent);
            }
        });

        meat_fish.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(packageContext:AdminAddNewProductActivity.this, AdminAddNewProductActivity.class);
                Intent.putExtra(name:"category", value:"meat_fish");
                startActivity(intent);
            }
        });

        snacks1.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                Intent.putExtra(name:"category", value:"snacks1");
                startActivity(intent);
            }
        });

        snacks2.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(packageContext:AdminAddNewProductActivity.this, AdminAddNewProductActivity.class);
                Intent.putExtra(name:"category", value:"snacks2");
                startActivity(intent);
            }
        });

        personal_care.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                Intent.putExtra(name:"category", value:"personal_care");
                startActivity(intent);
            }
        });

        house_cleaning.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(packageContext:AdminAddNewProductActivity.this, AdminAddNewProductActivity.class);
                Intent.putExtra(name:"category", value:"house_cleaning");
                startActivity(intent);
            }
        });

        first_aid.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                Intent.putExtra(name:"category", value:"first_aid");
                startActivity(intent);
            }
        });

        office.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(packageContext:AdminAddNewProductActivity.this, AdminAddNewProductActivity.class);
                Intent.putExtra(name:"category", value:"office");
                startActivity(intent);
            }
        });

        liquor.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(packageContext:AdminAddNewProductActivity.this, AdminAddNewProductActivity.class);
                Intent.putExtra(name:"category", value:"liquor");
                startActivity(intent);
            }
        });


    }
}
