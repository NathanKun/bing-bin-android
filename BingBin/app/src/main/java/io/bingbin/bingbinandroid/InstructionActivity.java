package io.bingbin.bingbinandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.bingbin.bingbinandroid.Models.Category;

public class InstructionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        Intent intent = getIntent();
        Category category = (Category) intent.getSerializableExtra("category");
        String categoryStr = category.toString();
        Log.d("Instruction Activity", categoryStr);


        TextView tv = findViewById(R.id.tv_instruction_title);
        ImageView iv = findViewById(R.id.iv_instruction_trashbin);
        Button btnRecycleit = findViewById(R.id.btn_instruction_recycleit);
        Button btnDone = findViewById(R.id.btn_instruction_done);

        String title = categoryStr.toLowerCase().substring(0,1).toUpperCase() +
                categoryStr.substring(1, categoryStr.length()).toLowerCase() +
                " trash bin";
        tv.setText(title);

        Bitmap trashbinImg = BitmapFactory.decodeResource(getResources(), category.getColor().getImageResource());
        iv.setImageBitmap(trashbinImg);

        btnRecycleit.setOnClickListener(view -> finish());
        btnDone.setOnClickListener(view -> finish());
    }
}
